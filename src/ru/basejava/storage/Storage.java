package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.List;

public interface Storage {
    int size();

    void clear();

    Resume[] getAll();

    List<Resume> getAllSorted();

    Resume get(String uuid);

    void save(Resume r);

    void update(Resume r);

    void delete(String uuid);
}
