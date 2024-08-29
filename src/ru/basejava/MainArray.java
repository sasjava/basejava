package ru.basejava;

import ru.basejava.model.Resume;
import ru.basejava.storage.ArrayStorage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Interactivesize test for ru.basejava.storage.ArrayStorage implementation
 * (just run, no need to understand)
 */
public class MainArray {
    private final static ArrayStorage ARRAY_STORAGE = new ArrayStorage();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;
        Resume rNew;
        while (true) {
            System.out.print("Введите одну из команд - (list | size | save uuid | update uuid new_uuid | delete uuid | get uuid | clear | exit): ");
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            if (params.length < 1 || params.length > 3) {
                System.out.println("Неверная команда.");
                continue;
            }
            String uuid = null;
            if (params.length >= 2) {
                uuid = params[1].intern();
            }
            String uuidNew = null;
            if (params.length >= 3) {
                uuidNew = params[2].intern();
            }
            switch (params[0]) {
                case "list":
                    printAll();
                    break;
                case "size":
                    System.out.println(ARRAY_STORAGE.size());
                    break;
                case "save":
                    r = new Resume();
                    r.setUuid(uuid);
                    ARRAY_STORAGE.save(r);
                    printAll();
                    break;
                case "update":
                    r = ARRAY_STORAGE.get(uuid);
                    if (r != null) {
                        rNew = new Resume();
                        rNew.setUuid(uuidNew);
                        ARRAY_STORAGE.update(r, rNew);
                        printAll();
                    }
                    break;
                case "delete":
                    ARRAY_STORAGE.delete(uuid);
                    printAll();
                    break;
                case "get":
                    System.out.println(ARRAY_STORAGE.get(uuid));
                    break;
                case "clear":
                    ARRAY_STORAGE.clear();
                    printAll();
                    break;
                case "exit":
                    return;
                default:
                    System.out.println("Неверная команда.");
                    break;
            }
        }
    }
    public static void printAll(ArrayStorage storage) {
        for (Resume r : storage.getAll()) {
            System.out.println(r.getUuid());
        }
    }
    private static void printAll() {
        Resume[] all = ARRAY_STORAGE.getAll();
        System.out.println("----------------------------");
        if (all.length == 0) {
            System.out.println("Empty");
        } else {
            printAll(ARRAY_STORAGE);
        }
        System.out.println("----------------------------");
    }
}
