package ru.basejava.storage;

import ru.basejava.exception.StorageException;
import ru.basejava.model.Resume;
import ru.basejava.storage.serialization.SerializationStrategy;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PathStorage extends AbstractStorage<Path> {
    private final Path directory;
    private final SerializationStrategy serialization;

    public PathStorage(String dir, SerializationStrategy sS) {
        directory = Paths.get(dir);
        this.serialization = sS;
        Objects.requireNonNull(directory, "directory must not be null");
        if (!Files.isDirectory(directory) || !Files.isWritable(directory)) {
            throw new IllegalArgumentException(dir + " is not directory or is not writable");
        }
    }

    @Override
    protected Path getSearchKey(String uuid) {
        return directory.resolve(uuid);
    }

    @Override
    protected boolean isExist(Path path) {
        return Files.exists(path);
    }

    @Override
    protected Resume doGet(Path path) {
        try {
            return serialization.doRead(new BufferedInputStream(Files.newInputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Error reading Path", path.toString(), e);
        }
    }

    @Override
    protected void doSave(Resume r, Path path) {
        try {
            Files.createFile(path);
        } catch (IOException e) {
            throw new StorageException("Couldn't create Path ", path.toString(), e);
        }
        doUpdate(r, path);
    }

    @Override
    protected void doDelete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new StorageException("Error deleting Path", path.toString(), e);
        }
    }

    @Override
    protected void doUpdate(Resume r, Path path) {
        try {
            serialization.doWrite(r, new BufferedOutputStream(Files.newOutputStream(path)));
        } catch (IOException e) {
            throw new StorageException("Error writing Path", path.toString(), e);
        }
    }

    @Override
    protected List<Resume> doCopyAll() {
        List<Resume> listResumes;
        listResumes = getFilesList().map(this::doGet).collect(Collectors.toList());

        return listResumes;
    }

    @Override
    public int size() {
        return (int) getFilesList().count();
    }

    @Override
    public void clear() {
        getFilesList().forEach(this::doDelete);
    }

    private Stream<Path> getFilesList() {
        try {
            return Files.list(directory);
        } catch (IOException e) {
            throw new StorageException("Files list reading error", null, e);
        }
    }
}
