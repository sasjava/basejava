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
    protected boolean isExist(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    @Override
    protected void doUpdate(Resume rNew, Object searchKey) {
        int index = (int) searchKey;
        storage[index] = rNew;
    }

    @Override
    public void save(Resume r) {
        if (size >= MAX_SIZE) {
            throw new StorageException(STORAGE_OVERFLOW, r.getUuid());
        }
        super.save(r);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (int) searchKey;
        return storage[index];
    }
}
