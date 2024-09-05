package ru.basejava.storage;

import ru.basejava.model.Resume;

public class ArrayStorage extends AbstractArrayStorage {

    protected int findIndex(String uuid) {
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
    protected void insert(Resume r) {
        storage[size] = r;
        size++;
    }

    @Override
    protected void updateItem(int index, Resume rNew) {
        storage[index] = rNew;
    }

    @Override
    protected void deleteItem(int index) {
        storage[index] = storage[size - 1];
        storage[size - 1] = null;
        size--;
    }

}
