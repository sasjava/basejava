package ru.basejava.storage;

import ru.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    @Override
    protected Integer getSearchKey(String uuid) {
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
    protected void doSave(Resume r) {
        storage[size] = r;
        size++;
    }

    @Override
    protected void doDelete(Object searchKey) {
        int index = (int) searchKey;
        storage[index] = storage[size - 1];
        size--;
        storage[size] = null;
    }
}
