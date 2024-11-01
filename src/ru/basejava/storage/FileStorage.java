package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;
import ru.basejava.storage.serialization.SerializationStrategy;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FileStorage extends AbstractStorage<File> {
    private final File directory;
    private final SerializationStrategy serialization;

    public FileStorage(File directory, SerializationStrategy sS) {
        this.serialization = sS;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!directory.isDirectory()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is not directory");
        }
        if (!directory.canRead() || !directory.canWrite()) {
            throw new IllegalArgumentException(directory.getAbsolutePath() + "is can not read/write directory");
        }
        this.directory = directory;
    }

    @Override
    protected File getSearchKey(String uuid) {
        return new File(directory, uuid);
    }

    @Override
    protected boolean isExist(File file) {
        return file.exists();
    }

    @Override
    protected Resume doGet(File file) {
        try {
            return serialization.doRead(new BufferedInputStream(new FileInputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error reading file", file.getName(), e);
        }
    }

    @Override
    protected void doSave(Resume r, File file) {
        try {
            file.createNewFile();
        } catch (IOException e) {
            throw new StorageException("Couldn't create file " + file.getAbsolutePath(), file.getName(), e);
        }
        doUpdate(r, file);
    }

    @Override
    protected void doDelete(File file) {
        if (!file.delete()) {
            throw new StorageException("Error deleting file", file.getName());
        }
    }

    @Override
    protected void doUpdate(Resume r, File file) {
        try {
            serialization.doWrite(r, new BufferedOutputStream(new FileOutputStream(file)));
        } catch (IOException e) {
            throw new StorageException("Error writing file", file.getName(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> listResumes = new ArrayList<>();

        File[] listFiles = getListFiles();
        for (File f : listFiles) {
            Resume r = this.doGet(f);
            listResumes.add(r);
        }
        return listResumes;
    }

    @Override
    public int size() {
        return getListFiles().length;
    }

    @Override
    public void clear() {
        File[] listFiles = getListFiles();
        for (File f : listFiles) {
            doDelete(f);
        }
    }

    private File[] getListFiles() {
        File[] files = directory.listFiles();
        if (files == null) {
            throw new StorageException("Directory reading error", null);
        }
        return files;
    }
}
