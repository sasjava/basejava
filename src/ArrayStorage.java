/**
 * Array based storage for Resumes
 */
public class ArrayStorage {
    Resume[] storage = new Resume[10000];

    void clear() {
        for (int i = 0; i < storage.length; i++) {
            storage[i] = null;
        }
    }

    void save(Resume r) {
        if (get(r.toString()) == null) {
            for (int i = 0; i < storage.length; i++) {
                if (storage[i] == null) {
                    storage[i] = r;
                    break;
                }
            }
        }
    }

    Resume get(String uuid) {
        for (Resume resume : storage) {
            if (resume == null) {
                break;
            }
            if (resume.toString().equals(uuid)) {
                return resume;
            }
        }
        return null;
    }

    void delete(String uuid) {
        boolean is_deleted = false;
        int i;
        for (i = 0; i < storage.length; i++) {
            if (storage[i] == null) {
                break;
            }
            if (storage[i].toString().equals(uuid)) {
                storage[i] = null;
                is_deleted = true;
                break;
            }
        }
        if (is_deleted & i < storage.length - 1) {
            for (int j = i; j < storage.length - 1; j++) {
                if (storage[j + 1] == null) {
                    break;
                } else {
                    storage[j] = storage[j + 1];
                    storage[j + 1] = null;
                }
            }
        }
    }

    /**
     * @return array, contains only Resumes in storage (without null)
     */
    Resume[] getAll() {
        Resume[] storageAll = new Resume[size()];
        if (storageAll.length > 0) {
            int j = 0;
            for (Resume resume : storage) {
                if (resume != null) {
                    storageAll[j++] = resume;
                }
            }
        }
        return storageAll;
    }

    int size() {
        int count = 0;
        for (Resume resume : storage) {
            if (resume != null) {
                count++;
            } else {
                break;
            }
        }
        return count;
    }
}
