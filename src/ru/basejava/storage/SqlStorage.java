package ru.basejava.storage;

import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.*;
import ru.basejava.sql.SqlHelper;
import ru.basejava.util.JsonParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    @FunctionalInterface
    public interface IAction {
        void add(Resume r, ResultSet rs) throws SQLException;
    }

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public int size() {
        return sqlHelper.run("SELECT count(*) FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    return rs.next() ? rs.getInt(1) : 0;
                });
    }

    @Override
    public void clear() {
        sqlHelper.run("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public Resume get(String uuid) {
        List<Resume> list = getWhere(uuid);
        if (list.size() == 0) {
            throw new NotExistStorageException(uuid);
        }
        return list.get(0);
    }

    @Override
    public List<Resume> getAllSorted() {
        return getWhere();
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalRun(conn -> {
            sqlHelper.execVoidQuery(conn, "INSERT INTO resume (uuid, full_name) VALUES (?, ?)",
                    ps -> {
                        ps.setString(1, r.getUuid());
                        ps.setString(2, r.getFullName());
                        ps.execute();
                    });
            insertContacts(r, conn);
            insertSections(r, conn);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        String uuid = r.getUuid();
        sqlHelper.transactionalRun(conn -> {
            if (sqlHelper.execQuery(conn, "UPDATE resume SET full_name = ? WHERE uuid = ?",
                    ps -> {
                        ps.setString(1, r.getFullName());
                        ps.setString(2, uuid);
                        return ps.executeUpdate();
                    }) == 0) {
                throw new NotExistStorageException(uuid);
            }
            sqlHelper.execQueryByUuid(conn, uuid, "DELETE FROM contact WHERE resume_uuid = ?");
            sqlHelper.execQueryByUuid(conn, uuid, "DELETE FROM section WHERE resume_uuid = ?");
            insertContacts(r, conn);
            insertSections(r, conn);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        sqlHelper.transactionalRun(conn -> {
            if (sqlHelper.execQueryByUuid(conn, uuid, "DELETE FROM resume WHERE uuid = ?") == 0) {
                throw new NotExistStorageException(uuid);
            }
            return null;
        });
    }

    private List<Resume> getWhere(String uuidPattern) {
        return sqlHelper.transactionalRun(conn -> {
            Map<String, Resume> resumesMap = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT uuid, full_name FROM resume WHERE uuid like ? ORDER BY full_name, uuid")) {
                ps.setString(1, uuidPattern);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    String uuid = rs.getString("uuid");
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    resumesMap.put(uuid, r);
                }
            }
            extractResumeData(conn, "SELECT resume_uuid AS uuid, type, value FROM contact WHERE resume_uuid like ? ORDER BY resume_uuid",
                    uuidPattern, resumesMap, this::addContactFromRS);
            extractResumeData(conn, "SELECT resume_uuid AS uuid, type, content FROM section WHERE resume_uuid like ? ORDER BY resume_uuid",
                    uuidPattern, resumesMap, this::addSectionFromRS);
            return resumesMap.values().stream().toList();
        });
    }

    private List<Resume> getWhere() {
        return getWhere("%");
    }

    private void extractResumeData(Connection conn, String sql, String uuidPattern,
                                   Map<String, Resume> resumesMap, IAction action) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, uuidPattern);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String uuid = rs.getString("uuid");
                Resume r = resumesMap.get(uuid);
                action.add(r, rs);
            }
        }
    }

    private void insertContacts(Resume r, Connection conn) throws SQLException {
        sqlHelper.execVoidQuery(conn, "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)",
                ps -> execInsertContacts(r, ps));
    }

    private void insertSections(Resume r, Connection conn) throws SQLException {
        sqlHelper.execVoidQuery(conn, "INSERT INTO section (resume_uuid, type, content) VALUES (?, ?, ?)",
                ps -> execInsertSections(r, ps));
    }

    private void execInsertContacts(Resume r, PreparedStatement ps) throws SQLException {
        for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
            ContactType cType = e.getKey();
            ps.setString(1, r.getUuid());
            ps.setString(2, cType.name());
            ps.setString(3, e.getValue());
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void execInsertSections(Resume r, PreparedStatement ps) throws SQLException {
        for (Map.Entry<SectionType, AbstractSection> e : r.getSections().entrySet()) {
            SectionType sType = e.getKey();
            AbstractSection section = e.getValue();
            ps.setString(1, r.getUuid());
            ps.setString(2, sType.name());
            String content = JsonParser.write(section, AbstractSection.class);
//            switch (sType) {
//                case OBJECTIVE, PERSONAL ->
//                        content = ((TextSection) e.getValue()).getContent(); //Позиция, Личные качества
//                case ACHIEVEMENT, QUALIFICATIONS ->
//                        content = String.join("\n", ((ListSection) e.getValue()).getItems()); //Достижения, Квалификация
//                default -> throw new IllegalStateException("Unexpected value: " + sType);
//            }
            ps.setString(3, content);
//            ps.setString(3, content);
            ps.addBatch();
        }
        ps.executeBatch();
    }

    private void addContactFromRS(Resume r, ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String value = rs.getString("value");
        if (type != null & value != null) {
            r.addContact(ContactType.valueOf(type), value);
        }
    }

    private void addSectionFromRS(Resume r, ResultSet rs) throws SQLException {
        String type = rs.getString("type");
        String content = rs.getString("content");
        if (type != null & content != null) {
            SectionType sType = SectionType.valueOf(type);
            r.addSection(sType, JsonParser.read(content, AbstractSection.class));
//            switch (sType) {
//                case OBJECTIVE, PERSONAL -> r.addSection(sType, new TextSection(content));  //Позиция, Личные качества
//                case ACHIEVEMENT, QUALIFICATIONS -> r.addSection(sType,
//                        new ListSection(Arrays.stream(content.split("\n")).toList()));//Достижения, Квалификация
//                default -> throw new IllegalStateException("Unexpected value: " + sType);
//            }
        }
    }
}
