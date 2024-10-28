package ru.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.basejava.ResumeTestData;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected Storage storage;
    private static final String FNAME_1 = "B";
    private static final String FNAME_2 = "B";
    private static final String FNAME_3 = "A";
    private static final String FNAME_NEW = "New";
    private static final String DUMMY = "dummy";
    private static final Resume R1 = ResumeTestData.createResumeData("uuid1", FNAME_1);
    private static final Resume R2 = ResumeTestData.createResumeData("uuid2", FNAME_2);
    private static final Resume R3 = ResumeTestData.createResumeData("uuid3", FNAME_3);
    private static final Resume R_NEW = ResumeTestData.createResumeData("uuidnew", FNAME_NEW);
    private static final Resume R_DUMMY = ResumeTestData.createResumeData("uuiddummy", DUMMY);

    public AbstractStorageTest() {
        this.storage = new ArrayStorage();
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public final void setUp() {
        storage.clear();
        storage.save(R1);
        storage.save(R2);
        storage.save(R3);
    }

    @Test
    public final void size() {
        assertSize(3);
    }

    @Test
    public final void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(new Resume[0], storage.getAllSorted().toArray(new Resume[0]));
    }

    @Test
    public final void getAll() {
        Resume[] resumes = storage.getAllSorted().toArray(new Resume[0]);
        assertEquals(storage.size(), resumes.length);
        Resume[] expectedArr = {R1, R2, R3};
        Arrays.sort(resumes);
        Arrays.sort(expectedArr);
        assertArrayEquals(expectedArr, resumes);
        for (Resume resume : resumes) {
            String uuid = resume.getUuid();
            assertEquals(storage.get(uuid), resume);
        }
    }

    @Test
    public final void getAllSorted() {
        List<Resume> list = storage.getAllSorted();
        assertEquals(storage.size(), list.size());

        Resume[] resumes = list.toArray(new Resume[0]);
        Resume[] expectedArr = {R3, R2, R1};
        if (R1.getUuid().compareTo(R2.getUuid()) < 0) {
            expectedArr[1] = R1;
            expectedArr[2] = R2;
        }
        assertArrayEquals(expectedArr, resumes);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), expectedArr[i]);
        }
    }

    @Test
    public final void get() {
        assertGet(R1);
        assertGet(R2);
        assertGet(R3);
    }

    @Test
    public final void save() {
        int size = storage.size();
        storage.save(R_NEW);
        assertSize(++size);
        assertGet(R_NEW);
    }

    @Test
    public final void update() {
        Resume resume = new Resume(R1.getUuid(), FNAME_NEW);
        storage.update(resume);
        assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public final void delete() {
        int size = storage.size();
        String uuid1 = R1.getUuid();
        storage.delete(uuid1);
        assertSize(--size);
        storage.get(uuid1);
    }

    @Test(expected = NotExistStorageException.class)
    public final void getNotExist() {
        storage.get(DUMMY);
    }

    @Test(expected = ExistStorageException.class)
    public final void saveExist() {
        storage.save(R1);
    }

    @Test(expected = NotExistStorageException.class)
    public final void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test(expected = NotExistStorageException.class)
    public final void updateNotExist() {
        storage.update(R_DUMMY);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}