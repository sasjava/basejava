package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    final int MAX_SIZE = 10000;
    Resume[] storage = new Resume[MAX_SIZE];

    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        if (size == MAX_SIZE) {
            System.out.println("Storage is full. Resume " + uuid + " is not saved");
        } else if (findIndex(uuid) != -1) {
            messageResumeExists(uuid);
        } else {
            storage[size] = r;
            size++;
        }
    }

    public void update(Resume rNew) {
        String uuid = rNew.getUuid();
        int index = findIndex(uuid);
        if (index == -1) {
            messageResumeMissing(uuid);
        } else {
            storage[index] = rNew;
        }
    }

    public Resume get(String uuid) {
        Resume resume = null;
        int index = findIndex(uuid);
        if (index == -1) {
            messageResumeMissing(uuid);
        } else {
            resume = storage[index];
        }
        return resume;
    }

    public void delete(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        } else {
            messageResumeMissing(uuid);
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public int size() {
        return size;
    }

    private int findIndex(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                index = i;
                break;
            }
        }
        return index;
    }

    private void messageResumeMissing(String uuid) {
        System.out.println("Resume " + uuid + " is missing");
    }

    private void messageResumeExists(String uuid) {
        System.out.println("Resume " + uuid + " already exists");
    }
}
