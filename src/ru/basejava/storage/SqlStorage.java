package ru.basejava.storage;

import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.ContactType;
import ru.basejava.model.Resume;
import ru.basejava.sql.SqlHelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        List<Resume> resumes = new ArrayList<>();
        return sqlHelper.transactionalRun(conn -> {
            try (PreparedStatement psR = conn.prepareStatement(
                    "SELECT uuid, full_name FROM resume ORDER BY full_name, uuid")) {
                ResultSet rsR = psR.executeQuery();
                try (PreparedStatement psC = conn.prepareStatement(
                        "SELECT resume_uuid uuid, type, value FROM contact ORDER BY resume_uuid")) {
                    ResultSet rsC = psC.executeQuery();
                    Map<ContactType, String> contacts = new HashMap<>();
                    Map<String, Map<ContactType, String>> contactsAll = new HashMap<>();
                    while (rsC.next()) {
                        String uuid = rsC.getString("uuid");
                        ContactType type = ContactType.valueOf(rsC.getString("type"));
                        String value = rsC.getString("value");
                        if (contactsAll.put(uuid, null) == null) {
                            contacts = new HashMap<>();
                        }
                        contacts.put(type, value);
                        contactsAll.replace(uuid, contacts);
                    }
                    while (rsR.next()) {
                        Resume r = new Resume(
                                rsR.getString("uuid"),
                                rsR.getString("full_name"));
                        contacts = contactsAll.get(r.getUuid());
                        for (Map.Entry<ContactType, String> e : contacts.entrySet()) {
                            r.addContact(e.getKey(), e.getValue());
                        }
                        resumes.add(r);
                    }
                }
            }
            return resumes;
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
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    Resume r = new Resume(uuid, rs.getString("full_name"));
                    do {
                        r.addContact(
                                ContactType.valueOf(rs.getString("type")),
                                rs.getString("value"));
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
                if (ps.executeUpdate() == 0) {
                    throw new NotExistStorageException(uuid);
                }
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
        if (rows == 0) {
            throw new NotExistStorageException(uuid);
        }
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
