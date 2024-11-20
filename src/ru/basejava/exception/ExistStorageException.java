package ru.basejava.exception;

public class ExistStorageException extends StorageException {
    public ExistStorageException(String uuid) {
        super("Resume " + uuid + " already exist", uuid);
    }

    public ExistStorageException(Exception e) {
        this(e.getMessage(), e);
    }

    public ExistStorageException(String s, Exception e) {
        super(s, null, e);
    }
}
