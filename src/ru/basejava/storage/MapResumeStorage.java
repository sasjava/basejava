package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MapResumeStorage extends AbstractStorage<Resume> {
    protected final Map<String, Resume> map = new HashMap<>();

    @Override
    protected Resume getSearchKey(String uuid) {
        return map.get(uuid);
    }

    @Override
    protected boolean isExist(Resume searchKey) {
        return searchKey != null;
    }

    @Override
    protected Resume doGet(Resume searchKey) {
        return searchKey;
    }

    @Override
    protected void doSave(Resume r, Resume searchKey) {
        map.put(r.getUuid(), r);
    }

    @Override
    protected void doDelete(Resume searchKey) {
        map.remove(searchKey.getUuid());
    }

    @Override
    protected void doUpdate(Resume rNew, Resume r) {
        map.replace(r.getUuid(), rNew);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public void clear() {
        map.clear();
    }
}
