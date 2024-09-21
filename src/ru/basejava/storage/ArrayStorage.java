package ru.basejava.storage;

import ru.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer findIndex(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    protected void insertResume(Resume r, Object searchKey) {
        storage[size] = r;
    }

    @Override
    protected void deleteResume(Object searchKey) {
        int index = (int) searchKey;
        storage[index] = storage[size - 1];
    }
}
