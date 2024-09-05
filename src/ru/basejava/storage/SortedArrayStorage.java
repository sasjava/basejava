package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

public class SortedArrayStorage extends AbstractArrayStorage {

    @Override
    protected int findIndex(String uuid) {
        Resume searchKey = new Resume();
        searchKey.setUuid(uuid);
        return Arrays.binarySearch(storage, 0, size, searchKey);
    }

    @Override
    protected void insert(Resume r) {
        String uuid = r.getUuid();
        int index = -findIndex(uuid) - 1;
        size++;
        for (int i = size; i > index; i--) {
            storage[i] = storage[i - 1];
        }
        storage[index] = r;
    }

    @Override
    protected void updateItem(int index, Resume rNew) {
        storage[index] = rNew;
    }

    @Override
    protected void deleteItem(int index) {
        for (int i = index; i < size - 1; i++) {
            storage[i] = storage[i + 1];
        }
        storage[size] = null;
        size--;
    }

}
