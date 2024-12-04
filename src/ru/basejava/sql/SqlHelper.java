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
    @FunctionalInterface
    public interface IVoidSqlExec {
        void execute(PreparedStatement ps) throws SQLException;
    }

    @FunctionalInterface
    public interface ISqlTransaction<T> {
        T execute(Connection conn) throws SQLException;
    }

    public <T> T run(String sql, ISqlExec<T> exec) throws StorageException {
        try (Connection conn = connectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            return exec.execute(ps);
        } catch (SQLException e) {
            throw ExceptionUtil.convertException(e);
        }
    }

    public <T> T transactionalRun(ISqlTransaction<T> exec) {
        try (Connection conn = connectionFactory.getConnection()) {
            try {
                conn.setAutoCommit(false);
                T res = exec.execute(conn);
                conn.commit();
                return res;
            } catch (SQLException e) {
                conn.rollback();
                throw ExceptionUtil.convertException(e);
            }
        } catch (SQLException e) {
            throw new StorageException(e);
        }
    }

    public int execQueryByUuid(Connection conn, String uuid, String sql) throws SQLException {
        return execQuery(conn, sql, ps -> {
            ps.setString(1, uuid);
            return ps.executeUpdate();
        });
    }

    public <T> T execQuery(Connection conn, String sql, SqlHelper.ISqlExec<T> exec) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            return exec.execute(ps);
        }
    }
    public void execVoidQuery(Connection conn, String sql, SqlHelper.IVoidSqlExec exec) throws SQLException {
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            exec.execute(ps);
        }
    }
}
