package ru.basejava.sql;

import org.postgresql.util.PSQLException;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.StorageException;

import java.sql.SQLException;

public class ExceptionUtil {
    private static final String ERR_UNIQUE_VIOLATION = "23505"; //http://www.postgresql.org/docs/9.3/static/errcodes-appendix.html

    private ExceptionUtil() {
    }

    public static StorageException convertException(SQLException e) {
        if (e instanceof PSQLException) {
            if (e.getSQLState().equals(ERR_UNIQUE_VIOLATION)) {
                return new ExistStorageException(e);
            }
        }
        return new StorageException(e);
    }
}
