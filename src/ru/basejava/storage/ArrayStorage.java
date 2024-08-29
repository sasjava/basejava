package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

public class ArrayStorage {
    final int MAX_SIZE = 5;
    Resume[] storage = new Resume[MAX_SIZE];

    private int size;

    public void clear() {
        Arrays.fill(storage, 0, size, null);
//        for (int i = 0; i < size; i++) {
//            storage[i] = null;
//        }
        size = 0;
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        if (size == MAX_SIZE) {
            System.out.println("Storage is full. Resume " + uuid + " is not saved");
            return;
        }
        if (find(uuid) != null) {
            message_resume_exists(uuid);
            return;
        }
        storage[size] = r;
        size++;
    }

    public void update(Resume r, Resume rNew) {
        if (r == null) {
            return;
        }
        String uuid = r.getUuid();
        Resume resume = find(uuid);
        if (resume == null) {
            message_resume_missing(uuid);
            return;
        }
        String uuidNew = rNew.getUuid();
        r.setUuid(uuidNew);
    }

    public Resume get(String uuid) {
        Resume resume = find(uuid);
        if (resume == null) {
            message_resume_missing(uuid);
        }
        return resume;
    }

    public void delete(String uuid) {
        Resume resume = find(uuid);
        if (resume == null) {
            message_resume_missing(uuid);
            return;
        }
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                storage[i] = storage[size - 1];
                storage[size - 1] = null;
                size--;
                break;
            }
        }
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
//        Resume[] resumes = new Resume[size];
//        for (int i = 0; i < size; i++) resumes[i] = storage[i];
//        return resumes;
    }

    public int size() {
        return size;
    }

    private Resume find(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].getUuid().equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    private void message_resume_missing(String uuid) {
        System.out.println("Resume " + uuid + " is missing");
    }

    private void message_resume_exists(String uuid) {
        System.out.println("Resume " + uuid + " already exists");
    }
}
