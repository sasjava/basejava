package ru.basejava.storage;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import ru.basejava.exception.ExistStorageException;
import ru.basejava.exception.NotExistStorageException;
import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

public abstract class AbstractArrayStorageTest {
    private final Storage storage;

    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_NEW = "uuidNew";

    public AbstractArrayStorageTest() {
        this.storage = new ArrayStorage();
    }

    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Before
    public void setUp() {
        storage.clear();
        storage.save(new Resume(UUID_1));
        storage.save(new Resume(UUID_2));
        storage.save(new Resume(UUID_3));
    }

    @Test
    public void size() {
        Assert.assertEquals(3, storage.size());
    }

    @Test
    public void clear() {
        storage.clear();
        Assert.assertEquals(0, storage.size());
    }

    @Test
    public void getAll() {
        Resume[] resumes = storage.getAll();
        Assert.assertEquals(storage.size(), resumes.length);
        for (Resume resume : resumes) {
            String uuid = resume.getUuid();
            Assert.assertEquals(storage.get(uuid), resume);
        }
    }

    @Test
    public void get() {
        Assert.assertEquals(storage.get(UUID_1), new Resume(UUID_1));
        Assert.assertEquals(storage.get(UUID_2), new Resume(UUID_2));
        Assert.assertEquals(storage.get(UUID_3), new Resume(UUID_3));
    }

    @Test
    public void save() {
        int size = storage.size();
        Resume resume = new Resume(UUID_NEW);
        storage.save(resume);
        Assert.assertEquals(++size, storage.size());
        Assert.assertEquals(resume, storage.get(UUID_NEW));
    }

    @Test
    public void update() {
        Resume resume = new Resume(UUID_1);
        storage.update(resume);
        Assert.assertEquals(resume, storage.get(UUID_1));
    }

    @Test(expected = NotExistStorageException.class)
    public void delete() {
        int size = storage.size();
        storage.delete(UUID_1);
        Assert.assertEquals(--size, storage.size());
        storage.get(UUID_1);
    }

    @Test(expected = NotExistStorageException.class)
    public void getNotExist() {
        storage.get("dummy");
    }

    @Test(expected = ExistStorageException.class)
    public void checkExist() {
        storage.save(new Resume(UUID_1));
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
}