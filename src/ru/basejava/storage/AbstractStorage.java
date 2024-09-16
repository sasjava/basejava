package ru.basejava.storage;

import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

public abstract class AbstractStorage implements Storage {
    public final Resume get(String uuid) {
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        }
        return get(index);
    }

    public void save(Resume r) {
        String uuid = r.getUuid();
        check_overflow(uuid);
        int index = findIndex(uuid);
        if (index >= 0) {
            throw new ExistStorageException(uuid);
        }
        insertResume(r);
    }

    public final void delete(String uuid) {
        int index = findIndex(uuid);
        if (index >= 0) {
            deleteResume(index);
        } else {
            throw new NotExistStorageException(uuid);
        }
    }

    public final void update(Resume rNew) {
        String uuid = rNew.getUuid();
        int index = findIndex(uuid);
        if (index < 0) {
            throw new NotExistStorageException(uuid);
        } else {
            updateResume(index, rNew);
        }
    }

    protected abstract int findIndex(String uuid);

    protected abstract void deleteResume(int index);

    protected abstract void updateResume(int index, Resume rNew);

    protected abstract Resume get(int Index);

    protected abstract void insertResume(Resume r);

    protected abstract void check_overflow(String uuid);
}
