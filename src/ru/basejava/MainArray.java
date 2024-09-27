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
            System.out.print("Введите одну из команд - (list | size | save fullName | update uuid fullName | delete uuid | get uuid | clear | exit): ");
            String[] params = reader.readLine().trim().toLowerCase().split(" ");
            if (params.length < 1 || params.length > 3) {
                System.out.println("Неверная команда.");

                continue;
            }
            String param = null;
            if (params.length > 1) {
                param = params[1].intern();
            }
            switch (params[0]) {
                case "list" -> printAll();
                case "size" -> System.out.println(ARRAY_STORAGE.size());
                case "save" -> {
                    r = new Resume(param);
                    ARRAY_STORAGE.save(r);
                    printAll();
                }
                case "update" -> {
                    r = new Resume(param, params[2]);
                    ARRAY_STORAGE.update(r);
                    printAll();
                }
                case "delete" -> {
                    ARRAY_STORAGE.delete(param);
                    printAll();
                }
                case "get" -> System.out.println(ARRAY_STORAGE.get(param));
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
        for (Resume r : storage.getAllSorted().toArray(new Resume[0])) {
            if (r == null) {
                return;
            }
            System.out.println(r.getUuid() + ": " + r.getFullName());
        }
    }

    private static void printAll() {
        Resume[] all = ARRAY_STORAGE.getAllSorted().toArray(new Resume[0]);
        System.out.println("----------------------------");
        if (all.length == 0) {
            System.out.println("Empty");
        } else {
            printAll(ARRAY_STORAGE);
        }
        System.out.println("----------------------------");
    }
}
