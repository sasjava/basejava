/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    private int size;

    void clear() {
        for (int i = 0; i < size; i++) {
            storage[i] = null;
        }
        size = 0;
    }

    void save(Resume r) {
        if (get(r.toString()) == null) {
            storage[size] = r;
            size++;
        }
    }

    Resume get(String uuid) {
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                return storage[i];
            }
        }
        return null;
    }

    void delete(String uuid) {
        int index = -1;
        for (int i = 0; i < size; i++) {
            if (storage[i].toString().equals(uuid)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            storage[index] = storage[size - 1];
            storage[size - 1] = null;
            size--;
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] storageAll = new Resume[size()];
        if (storageAll.length > 0) {
            for (int i = 0; i < size; i++) {
                storageAll[i] = storage[i];
            }
        }
        return storageAll;
    }

    int size() {
        return size;
    }
}
