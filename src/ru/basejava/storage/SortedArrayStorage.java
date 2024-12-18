package ru.basejava.storage;

import ru.basejava.model.Resume;

import java.util.Arrays;
import java.util.Comparator;

public class SortedArrayStorage extends AbstractArrayStorage {
    /*
    private static class ResumeComparator implements Comparator<Resume> {
        @Override
        public int compare(Resume o1, Resume o2) {
            return o1.getUuid().compareTo(o2.getUuid());
        }
    }
*/
    private static final Comparator<Resume> RESUME_COMPARATOR = Comparator.comparing(Resume::getUuid);

    @Override
    protected Integer findIndex(String uuid) {
        Resume searchKey = new Resume(uuid, "Dummy");
        return Arrays.binarySearch(storage, 0, size, searchKey, RESUME_COMPARATOR);
    }

    @Override
    protected void insertResume(Resume r, int searchKey) {
        int index = searchKey;
        index = -index - 1;
        System.arraycopy(storage, index, storage, index + 1, size - index);
        storage[index] = r;
    }

    @Override
    protected void deleteResume(int searchKey) {
        System.arraycopy(storage, searchKey + 1, storage, searchKey, size - searchKey - 1);
    }
}
