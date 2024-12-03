package ru.basejava;

import ru.basejava.storage.SqlStorage;
import ru.basejava.storage.Storage;

import java.io.*;
import java.util.Properties;

public class Config {
    private static final File PROP_FILE = new File("C:/Java/GitHub/basejava", "/config/resumes.properties");
    private static final Config INSTANCE = new Config();
    private final File storageDir;
    private final Storage storage;

    public File getStorageDir() {
        return storageDir;
    }

    public Storage getStorage() {
        return storage;
    }

    public Config() {
        try (InputStream is = new FileInputStream(PROP_FILE.getAbsolutePath())) {
            Properties props = new Properties();
            props.load(is);
            storageDir = new File(props.getProperty("storage.dir"));
            storage = new SqlStorage(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"));
        } catch (IOException e) {
            throw new IllegalStateException("Invalid config file " + PROP_FILE.getAbsolutePath());
        }
    }

    public static Config get() {
        return INSTANCE;
    }


}
