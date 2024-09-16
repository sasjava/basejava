package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.ArrayList;

public class ListStorage extends AbstractStorage {
    protected ArrayList<Resume> arrayList = new ArrayList();

    @Override
    public int size() {
        return arrayList.size();
    }

    @Override
    public void clear() {
        arrayList.clear();
    }

    @Override
    public Resume[] getAll() {
        return arrayList.toArray(Resume[]::new);
    }

    protected int findIndex(String uuid) {
        Resume resume = new Resume(uuid);
        return arrayList.indexOf(resume);
    }

    @Override
    protected void deleteResume(int index) {
        arrayList.remove(index);
    }

    protected void updateResume(int index, Resume rNew) {
        arrayList.set(index, rNew);
    }

    @Override
    protected Resume get(int index) {
        return arrayList.get(index);
    }

    @Override
    protected void insertResume(Resume r) {
        arrayList.add(r);
    }

    @Override
    protected void check_overflow(String uuid) {
    }
}
