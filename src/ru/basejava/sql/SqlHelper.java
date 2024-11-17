package ru.basejava.sql;

import ru.basejava.exception.StorageException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SqlHelper {
    private final ConnectionFactory connectionFactory;

    public SqlHelper(String dbUrl, String dbUser, String dbPassword) {
        connectionFactory = () -> DriverManager.getConnection(dbUrl, dbUser, dbPassword);
    }

    @FunctionalInterface
    public interface ISqlExec<T> {
        T execute(PreparedStatement ps) throws SQLException;
    }

    public <T> T run(String sql, ISqlExec<T> exec) throws StorageException {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return exec.execute(ps);
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }
}
