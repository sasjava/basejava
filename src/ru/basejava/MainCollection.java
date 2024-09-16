package ru.basejava;

import ru.basejava.model.Resume;
import ru.basejava.storage.ListStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;

public class MainCollection {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final Resume resume1;
    private static final Resume resume2;
    private static final Resume resume3;

    static {
        resume1 = new Resume(UUID_1);
        resume2 = new Resume(UUID_2);
        resume3 = new Resume(UUID_3);
    }

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList();
        collection.add(resume1);
        collection.add(resume2);
        collection.add(resume3);

        Iterator<Resume> iterator = collection.iterator();
        while (iterator.hasNext()) {
            Resume r = iterator.next();
            //System.out.println(r);
            if (Objects.equals(r.getUuid(), UUID_1)) {
                iterator.remove();
            }
        }
        //System.out.println(collection.toString());

        ListStorage listStorage = new ListStorage();
        listStorage.save(resume3);
        listStorage.save(resume1);
        listStorage.save(resume2);

        Resume[] resumes = listStorage.getAll();
        MainArray.printAll(listStorage);
        System.out.println("GET UUID_1:" + listStorage.get(UUID_1));
    }
}
