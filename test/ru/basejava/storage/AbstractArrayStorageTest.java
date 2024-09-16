package ru.basejava.storage;

import org.junit.Assert;
import org.junit.Test;
import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;

public abstract class AbstractArrayStorageTest extends AbstractStorageTest {
    protected AbstractArrayStorageTest(Storage storage) {
        this.storage = storage;
    }

    @Test
    public final void checkOverflow() {
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