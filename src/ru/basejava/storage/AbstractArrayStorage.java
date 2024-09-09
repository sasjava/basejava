package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;

public abstract class AbstractArrayStorage implements Storage {
    protected static final int MAX_SIZE = 10001;
    protected Resume[] storage = new Resume[MAX_SIZE];
    protected int size = 0;

    public int size() {
        return size;
    }

    public void clear() {
        Arrays.fill(storage, 0, size, null);
        size = 0;
    }

    public Resume[] getAll() {
        return Arrays.copyOf(storage, size);
    }

    public final Resume get(String uuid) {
        Resume resume = null;
        int index = findIndex(uuid);
        if (index == -1) {
            messageResumeMissing(uuid);
        } else {
            resume = storage[index];
        }
        return resume;
    }

    public final void save(Resume r) {
        String uuid = r.getUuid();
        if (size == MAX_SIZE) {
            System.out.println("Storage is full. Resume " + uuid + " is not saved");
            return;
        }
        if (findIndex(uuid) >= 0) {
            messageResumeExists(uuid);
        } else {
            insertResume(r);
            size++;
        }
    }

    public final void update(Resume rNew) {
        String uuid = rNew.getUuid();
        int index = findIndex(uuid);
        if (index == -1) {
            messageResumeMissing(uuid);
        } else {
            updateResume(index, rNew);
        }
    }

    public final void delete(String uuid) {
        int index = findIndex(uuid);
        if (index != -1) {
            deleteResume(index);
            size--;
        } else {
            messageResumeMissing(uuid);
        }
    }

    protected abstract int findIndex(String uuid);

    protected abstract void insertResume(Resume r);

    protected abstract void updateResume(int index, Resume rNew);

    protected abstract void deleteResume(int index);

    protected void messageResumeMissing(String uuid) {
        System.out.println("Resume " + uuid + " is missing");
    }

    protected void messageResumeExists(String uuid) {
        System.out.println("Resume " + uuid + " already exists");
    }
}
