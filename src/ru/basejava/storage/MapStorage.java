package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.HashMap;
import java.util.Map;

public class MapStorage extends AbstractStorage {
    protected Map<String, Resume> map = new HashMap<>();

    @Override
    protected String getSearchKey(String uuid) {
        if (map.containsKey(uuid)) {
            return uuid;
        }
        return null;
    }

    @Override
    protected Resume doGet(Object searchKey) {
        String key = (String) searchKey;
        return map.get(key);
    }

    @Override
    protected boolean isExist(Object searchKey) {
        String key = (String) searchKey;
        return key != null;
    }

    @Override
    protected void doUpdate(Resume rNew, Object searchKey) {
        String key = (String) searchKey;
        map.replace(key, rNew);
    }

    @Override
    protected void doSave(Resume r) {
        map.put(r.getUuid(), r);
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

    @Override
    public Resume[] getAll() {
        return map.values().toArray(new Resume[0]);
    }
}
