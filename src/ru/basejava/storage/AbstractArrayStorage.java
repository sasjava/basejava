package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage extends AbstractStorage {

    protected Resume[] storage = new Resume[MAX_SIZE];
    protected int size = 0;
    protected static final int MAX_SIZE = 5;
    protected static final String STORAGE_OVERFLOW = "Storage overflow";

    public int size() {
        return size;
    }

    public void clear() {
        if (size > 0) {
            Arrays.fill(storage, 0, size, null);
            size = 0;
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    @Override
    protected Resume get(int index) {
        return storage[index];
    }

    @Override
    protected void check_overflow(String uuid) {
        if (size >= MAX_SIZE) {
            throw new StorageException(STORAGE_OVERFLOW, uuid);
        }
    }
}
