package ru.basejava.storage;

import ru.basejava.storage.serialization.ObjectStreamSerialization;

public class PathStorageTest extends AbstractStorageTest {
    public PathStorageTest() {
        super(new PathStorage(STORAGE_DIR.getAbsolutePath(), new ObjectStreamSerialization()));
    }

}