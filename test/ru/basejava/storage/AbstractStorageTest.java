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
    private static final Resume resume1 = ResumeTestData.createResumeData("uuid1", FNAME_1); //new Resume(FNAME_1);
    private static final Resume resume2 = ResumeTestData.createResumeData("uuid2", FNAME_2); //new Resume(FNAME_2);
    private static final Resume resume3 = ResumeTestData.createResumeData("uuid3", FNAME_3); //new Resume(FNAME_3);
    private static final Resume resumeNew = ResumeTestData.createResumeData("uuidnew", FNAME_NEW); //new Resume(FNAME_NEW);
    private static final Resume resumeDummy = ResumeTestData.createResumeData("uuiddummy", DUMMY); //new Resume(DUMMY);

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
        assertArrayEquals(new Resume[0], storage.getAllSorted().toArray(new Resume[0]));
    }

    @Test
    public final void getAll() {
        Resume[] resumes = storage.getAllSorted().toArray(new Resume[0]);
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
        Resume[] expectedArr = {resume3, resume2, resume1};
        if (resume1.getUuid().compareTo(resume2.getUuid()) < 0) {
            expectedArr[1] = resume1;
            expectedArr[2] = resume2;
        }
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
        Resume resume = new Resume(resume1.getUuid(), FNAME_NEW);
        storage.update(resume);
        assertEquals(resume, storage.get(resume.getUuid()));
    }

    @Test(expected = NotExistStorageException.class)
    public final void delete() {
        int size = storage.size();
        String uuid1 = resume1.getUuid();
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