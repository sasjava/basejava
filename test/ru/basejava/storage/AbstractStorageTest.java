package ru.basejava.storage;

import org.junit.Before;
import org.junit.Test;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.model.Resume;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public abstract class AbstractStorageTest {
    protected Storage storage;
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_NEW = "uuidNew";
    private static final String DUMMY = "dummy";
    private static final String FNAME_1 = "B";
    private static final String FNAME_2 = "B";
    private static final String FNAME_3 = "A";
    private static final Resume resume1 = new Resume(UUID_1, FNAME_1);
    private static final Resume resume2 = new Resume(UUID_2, FNAME_2);
    private static final Resume resume3 = new Resume(UUID_3, FNAME_3);
    private static final Resume resumeNew = new Resume(UUID_NEW);
    private static final Resume resumeDummy = new Resume(DUMMY);

    public AbstractStorageTest() {
        this.storage = new ArrayStorage();
    }

    protected AbstractStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public final void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public final void size() {
        assertSize(3);
    }

    @Test
    public final void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(new Resume[0], storage.getAll());
    }

    @Test
    public final void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(storage.size(), resumes.length);
        Resume[] expectedArr = {resume1, resume2, resume3};
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
        Resume[] expectedArr = {resume3, resume1, resume2};

        assertArrayEquals(expectedArr, resumes);
        for (int i = 0; i < list.size(); i++) {
            assertEquals(list.get(i), expectedArr[i]);
        }
    }

    @Test
    public final void get() {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
    }

    @Test
    public final void save() {
        int size = storage.size();
        storage.save(resumeNew);
        assertSize(++size);
        assertGet(resumeNew);
    }

    @Test
    public final void update() {
        Resume resume = resume1;
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public final void delete() {
        int size = storage.size();
        storage.delete(UUID_1);
        assertSize(--size);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public final void getNotExist() {
        storage.get(DUMMY);
    }

    @Test(expected = ExistStorageException.class)
    public final void saveExist() {
        storage.save(resume1);
    }

    @Test(expected = NotExistStorageException.class)
    public final void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test(expected = NotExistStorageException.class)
    public final void updateNotExist() {
        storage.update(resumeDummy);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}