package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.*;

public class MapUuidStorage extends AbstractStorage {
    protected Map<String, Resume> map = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        return uuid;
    }

    @Override
    protected boolean isExist(Object searchKey) {
        String key = (String) searchKey;
        return map.containsKey(key);
    }

    @Override
    protected Resume doGet(Object searchKey) {
        String key = (String) searchKey;
        return map.get(key);
    }

    @Override
    protected void doSave(Resume r, Object searchKey) {
        map.put(r.getUuid(), r);
    }

    @Override
    protected void doUpdate(Resume rNew, Object searchKey) {
        String key = (String) searchKey;
        map.replace(key, rNew);
    }

    @Override
    protected List<Resume> doCopyAll() {
        return new ArrayList<>(map.values());
    }

    @Override
    protected void doDelete(Object searchKey) {
        String key = (String) searchKey;
        map.remove(key);
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
