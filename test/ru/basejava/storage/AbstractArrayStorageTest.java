package ru.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final Resume resume1 = new Resume(UUID_1);
    private static final String UUID_2 = "uuid2";
    private static final Resume resume2 = new Resume(UUID_2);
    private static final String UUID_3 = "uuid3";
    private static final Resume resume3 = new Resume(UUID_3);
    private static final String UUID_NEW = "uuidNew";
    private static final Resume resumeNew = new Resume(UUID_NEW);
    private static final String DUMMY = "dummy";
    private static final Resume resumeDummy = new Resume(DUMMY);

    public AbstractArrayStorageTest() {
        this.storage = new ArrayStorage();
    }

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(resume1);
        storage.save(resume2);
        storage.save(resume3);
    }

    @Test
    public void size() {
        assertSize(3);
    }

    @Test
    public void clear() {
        storage.clear();
        assertSize(0);
        assertArrayEquals(new Resume[0], storage.getAll());
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        assertEquals(storage.size(), resumes.length);
        Resume[] expectedArr = {resume1, resume2, resume3};
        assertArrayEquals(expectedArr, resumes);
        for (Resume resume : resumes) {
            String uuid = resume.getUuid();
            assertEquals(storage.get(uuid), resume);
        }
    }

    @Test
    public void get() {
        assertGet(resume1);
        assertGet(resume2);
        assertGet(resume3);
    }

    @Test
    public void save() {
        int size = storage.size();
        storage.save(resumeNew);
        assertSize(++size);
        assertGet(resumeNew);
    }

    @Test
    public void update() {
        Resume resume = resume1;
        storage.update(resume);
        assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int size = storage.size();
        storage.delete(UUID_1);
        assertSize(--size);
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get(DUMMY);
    }

    @Test(expected = ExistStorageException.class)
    public void saveExist() {
        storage.save(resume1);
    }

    @Test
    public void checkOverflow() {
        for (int i = storage.size(); i < AbstractArrayStorage.MAX_SIZE; i++) {
            try {
                storage.save(new Resume());
            } catch (StorageException exception) {
                if (exception.getMessage().equals(AbstractArrayStorage.STORAGE_OVERFLOW)) {
                    Assert.fail();
                }
            }
        }
        try {
            storage.save(new Resume());
            Assert.fail();
        } catch (StorageException exception) {
            if (!exception.getMessage().equals(AbstractArrayStorage.STORAGE_OVERFLOW)) {
                Assert.fail();
            }
        }
    }

    @Test(expected = NotExistStorageException.class)
    public void deleteNotExist() {
        storage.delete(DUMMY);
    }

    @Test(expected = NotExistStorageException.class)
    public void updateNotExist() {
        storage.update(resumeDummy);
    }

    private void assertSize(int size) {
        assertEquals(size, storage.size());
    }

    private void assertGet(Resume r) {
        assertEquals(r, storage.get(r.getUuid()));
    }
}