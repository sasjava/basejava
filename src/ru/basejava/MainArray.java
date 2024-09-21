package ru.basejava;

import ru.basejava.model.Resume;
import ru.basejava.storage.ListStorage;
import ru.basejava.storage.Storage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Interactivesize test for ru.basejava.storage.ArrayStorage implementation
 * (just run, no need to understand)
 */
public class MainArray {
    private final static Storage ARRAY_STORAGE = new ListStorage();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Resume r;
        while (true) {
            System.out.print("Введите одну из команд - (list | size | save uuid | update uuid | delete uuid | get uuid | clear | exit): ");
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            if (params.length < 1 || params.length > 2) {
                System.out.println("Неверная команда.");
                continue;
            }
            String uuid = null;
            if (params.length >= 2) {
                uuid = params[1].intern();
            }
            switch (params[0]) {
                case "list" -> printAll();
                case "size" -> System.out.println(ARRAY_STORAGE.size());
                case "save" -> {
                    r = new Resume(uuid);
                    ARRAY_STORAGE.save(r);
                    printAll();
                }
                case "update" -> {
                    r = ARRAY_STORAGE.get(uuid);
                    if (r != null) {
                        ARRAY_STORAGE.update(r);
                        printAll();
                    }
                }
                case "delete" -> {
                    ARRAY_STORAGE.delete(uuid);
                    printAll();
                }
                case "get" -> System.out.println(ARRAY_STORAGE.get(uuid));
                case "clear" -> {
                    ARRAY_STORAGE.clear();
                    printAll();
                }
                case "exit" -> {
                    return;
                }
                default -> System.out.println("Неверная команда.");
            }
        }
    }

    public static void printAll(Storage storage) {
        for (Resume r : storage.getAll()) {
            if (r == null) {
                return;
            }
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