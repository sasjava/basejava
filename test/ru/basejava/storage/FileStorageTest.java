package ru.basejava.storage;

import ru.basejava.storage.serialization.ObjectStreamSerialization;

public class FileStorageTest extends AbstractStorageTest {
    public FileStorageTest() {
        super(new FileStorage(STORAGE_DIR, new ObjectStreamSerialization()));
    }

}