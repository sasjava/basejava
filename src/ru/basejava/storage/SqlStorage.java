package ru.basejava.storage;

import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.ContactType;
import ru.basejava.model.Resume;
import ru.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class SqlStorage implements Storage {
    private final SqlHelper sqlHelper;

    public SqlStorage(String dbUrl, String dbUser, String dbPassword) {
        sqlHelper = new SqlHelper(dbUrl, dbUser, dbPassword);
    }

    @Override
    public int size() {
        return sqlHelper.run("SELECT count(*) FROM resume",
                ps -> {
                    ResultSet rs = ps.executeQuery();
                    rs.next();
                    return rs.getInt(1);
                });
    }

    @Override
    public void clear() {
        sqlHelper.run("DELETE FROM resume", PreparedStatement::execute);
    }

    @Override
    public List<Resume> getAllSorted() {
        return sqlHelper.transactionalRun(conn -> {
            Map<String, Resume> resumesMap = new LinkedHashMap<>();
            try (PreparedStatement psR = conn.prepareStatement(
                    "SELECT uuid, full_name FROM resume ORDER BY full_name, uuid")) {
                ResultSet rsR = psR.executeQuery();
                while (rsR.next()) {
                    String uuid = rsR.getString("uuid");
                    Resume r = new Resume(uuid, rsR.getString("full_name"));
                    resumesMap.put(uuid, r);
                }
            }
            try (PreparedStatement psC = conn.prepareStatement(
                    "SELECT resume_uuid AS uuid, type, value FROM contact ORDER BY resume_uuid")) {
                ResultSet rsC = psC.executeQuery();
                while (rsC.next()) {
                    String uuid = rsC.getString("uuid");
                    ContactType type = ContactType.valueOf(rsC.getString("type"));
                    String value = rsC.getString("value");
                    resumesMap.get(uuid).addContact(type, value);
                }
            }
            return resumesMap.values().stream().toList();
        });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.run(
                "SELECT r.uuid, r.full_name, c.type, c.value FROM resume r" +
                        " LEFT JOIN contact c ON c.resume_uuid = r.uuid" +
                        " WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) throw new NotExistStorageException(uuid);
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        String type = rs.getString("type");
                        if (type == null) break;
                        r.addContact(ContactType.valueOf(type), rs.getString("value"));
                    } while (rs.next());
                    return r;
                });
    }

    @Override
    public void save(Resume r) {
        sqlHelper.transactionalRun(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO resume (uuid, full_name) VALUES (?, ?)")) {
                ps.setString(1, r.getUuid());
                ps.setString(2, r.getFullName());
                ps.execute();
            }
            insertContact(r, conn);
            return null;
        });
    }

    @Override
    public void update(Resume r) {
        String uuid = r.getUuid();
        sqlHelper.transactionalRun(conn -> {
            try (PreparedStatement ps = conn.prepareStatement(
                    "UPDATE resume SET full_name = ? WHERE uuid = ?")) {
                ps.setString(1, r.getFullName());
                ps.setString(2, uuid);
                if (ps.executeUpdate() == 0) throw new NotExistStorageException(uuid);
            }
            try (PreparedStatement ps = conn.prepareStatement(
                    "DELETE FROM contact WHERE resume_uuid = ?")) {
                ps.setString(1, uuid);
                ps.execute();
            }
            insertContact(r, conn);
            return null;
        });
    }

    @Override
    public void delete(String uuid) {
        int rows = sqlHelper.run("DELETE FROM resume WHERE uuid =?",
                ps -> {
                    ps.setString(1, uuid);
                    return ps.executeUpdate();
                });
        if (rows == 0) throw new NotExistStorageException(uuid);
    }

    private void insertContact(Resume r, Connection conn) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO contact (resume_uuid, type, value) VALUES (?, ?, ?)")) {
            for (Map.Entry<ContactType, String> e : r.getContacts().entrySet()) {
                ps.setString(1, r.getUuid());
                ps.setString(2, e.getKey().name());
                ps.setString(3, e.getValue());
                ps.addBatch();
            }
            ps.executeBatch();
        }
    }
}
