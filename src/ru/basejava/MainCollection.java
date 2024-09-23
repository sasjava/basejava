package ru.basejava;

import ru.basejava.model.Resume;
import ru.basejava.storage.ArrayStorage;
import ru.basejava.storage.ListStorage;
import ru.basejava.storage.MapStorage;

import java.util.*;

public class MainCollection {
    private static final String UUID_1 = "uuid1";
    private static final String UUID_2 = "uuid2";
    private static final String UUID_3 = "uuid3";
    private static final String UUID_4 = "uuid4";
    private static final Resume RESUME1;
    private static final Resume RESUME2;
    private static final Resume RESUME3;
    private static final Resume RESUME4;

    static {
        RESUME1 = new Resume(UUID_1);
        RESUME2 = new Resume(UUID_2);
        RESUME3 = new Resume(UUID_3);
        RESUME4 = new Resume(UUID_4);
        RESUME1.setFullName("ВВВ");
        RESUME2.setFullName("БББ");
        RESUME3.setFullName("ВВВ");
        RESUME4.setFullName("ААА");
    }

    public static void main(String[] args) {
        Collection<Resume> collection = new ArrayList();
        collection.add(RESUME1);
        collection.add(RESUME2);
        collection.add(RESUME3);

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
        listStorage.save(RESUME3);
        listStorage.save(RESUME1);
        listStorage.save(RESUME2);

        Resume[] resumes = listStorage.getAll();
        //MainArray.printAll(listStorage);
        //System.out.println("GET UUID_1:" + listStorage.get(UUID_1));

        Map<String, Resume> map = new HashMap<>();
        map.put(UUID_1, RESUME1);
        map.put(UUID_2, RESUME2);
        map.put(UUID_3, RESUME3);
//        for (Map.Entry<String, Resume> entry : map.entrySet()) {
//            System.out.println(entry.getValue());
//        }

        MapStorage mapStorage = new MapStorage();
        mapStorage.save(RESUME3);
        mapStorage.save(RESUME1);
        mapStorage.save(RESUME2);

        //resumes = mapStorage.getAll();
        //MainArray.printAll(mapStorage);

        ArrayStorage arrStorage = new ArrayStorage();
        arrStorage.save(RESUME1);
        arrStorage.save(RESUME2);
        arrStorage.save(RESUME4);
        arrStorage.save(RESUME3);
        List<Resume> list = arrStorage.getAllSorted();
        System.out.println(list);
    }
}
