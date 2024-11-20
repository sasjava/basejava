package ru.basejava.storage;

import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;
import ru.basejava.sql.SqlHelper;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

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
        return sqlHelper.run("SELECT uuid, full_name FROM resume ORDER BY full_name, uuid",
                ps -> {
                    List<Resume> resumeList = new ArrayList<>();
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        resumeList.add(new Resume(
                                rs.getString("uuid"),
                                rs.getString("full_name")));
                    }
                    return resumeList;
                });
    }

    @Override
    public Resume get(String uuid) {
        return sqlHelper.run("SELECT * FROM resume WHERE uuid = ?",
                ps -> {
                    ps.setString(1, uuid);
                    ResultSet rs = ps.executeQuery();
                    if (!rs.next()) {
                        throw new NotExistStorageException(uuid);
                    }
                    return new Resume(uuid, rs.getString("full_name"));
                });
    }

    @Override
    public void save(Resume r) {
        String uuid = r.getUuid();
        sqlHelper.run("INSERT INTO resume (uuid, full_name) VALUES (?, ?)",
                ps -> {
                    ps.setString(1, uuid);
                    ps.setString(2, r.getFullName());
                    return ps.executeUpdate();
                });
    }

    @Override
    public void update(Resume r) {
        String uuid = r.getUuid();
        int rows = sqlHelper.run("UPDATE resume SET full_name = ? WHERE uuid = ?",
                ps -> {
                    ps.setString(1, r.getFullName());
                    ps.setString(2, uuid);
                    return ps.executeUpdate();
                });
        if (rows == 0) {
            throw new NotExistStorageException(uuid);
        }
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
}
