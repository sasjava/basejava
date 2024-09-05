package ru.basejava.storage;

import ru.basejava.model.Resume;

public interface Storage {
    int size();

    void clear();

    Resume[] getAll();

    Resume get(String uuid);

    void save(Resume r);

    void update(Resume r);

    void delete(String uuid);
}
