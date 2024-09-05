package ru.basejava;

import ru.basejava.model.Resume;
import ru.basejava.storage.SortedArrayStorage;
import ru.basejava.storage.Storage;

/**
 * Test for your ArrayStorage implementation
 */
public class MainTestArrayStorage {
    static final Storage ARRAY_STORAGE = new SortedArrayStorage();

    public static void main(String[] args) {
        Resume r1 = new Resume();
        r1.setUuid("uuid1");
        Resume r2 = new Resume();
        r2.setUuid("uuid2");
        Resume r3 = new Resume();
        r3.setUuid("uuid3");
        Resume r4 = new Resume();
        r4.setUuid("uuid4");
        Resume r5 = new Resume();
        r5.setUuid("uuid5");
        Resume r6 = new Resume();
        r6.setUuid("uuid6");

        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r1);
        ARRAY_STORAGE.save(r5);
        ARRAY_STORAGE.save(r2);
        ARRAY_STORAGE.save(r6);
        ARRAY_STORAGE.save(r3);
        ARRAY_STORAGE.save(r4);

        System.out.println("Size: " + ARRAY_STORAGE.size());
        printAll();

        System.out.print("\nDelete: " + r1.getUuid() + "------------");
        ARRAY_STORAGE.delete(r3.getUuid());
        System.out.println("Size: " + ARRAY_STORAGE.size());
        printAll();

        System.out.print("\nDo clear------------");
        ARRAY_STORAGE.clear();
        printAll();

        System.out.println("Size: " + ARRAY_STORAGE.size());
    }

    private static void printAll() {
        System.out.println("\nGet All: ---------------------");
        MainArray.printAll(ARRAY_STORAGE);
    }
}
