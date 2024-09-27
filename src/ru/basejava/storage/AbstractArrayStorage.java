package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

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

    @Override
    protected Integer getSearchKey(String uuid) {
        return findIndex(uuid);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        int index = (int) searchKey;
        return index >= 0;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        int index = (int) searchKey;
        return storage[index];
    }

    @Override
    protected void doUpdate(Resume rNew, Object searchKey) {
        int index = (int) searchKey;
        storage[index] = rNew;
    }

    @Override
    public void doSave(Resume r, Object searchKey) {
        if (size >= MAX_SIZE) {
            throw new StorageException(STORAGE_OVERFLOW, r.getUuid());
        }
        insertResume(r, searchKey);
        size++;
    }

    @Override
    protected void doDelete(Object searchKey) {
        deleteResume(searchKey);
        storage[size - 1] = null;
        size--;
    }

    @Override
    protected List<Resume> doCopyAll() {
        return Arrays.asList(Arrays.copyOf(storage, size));
    }

    protected abstract Integer findIndex(String uuid);

    protected abstract void insertResume(Resume r, Object searchKey);

    protected abstract void deleteResume(Object searchKey);
}
