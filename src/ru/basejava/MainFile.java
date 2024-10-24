package ru.basejava;

import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
import java.util.Objects;

public class MainFile {
    public static void main(String[] args) {
//        String filePath = ".\\";
//
//        File file = new File(filePath);
//        try {
//            System.out.println(file.getCanonicalPath());
//        } catch (IOException e) {
//            throw new RuntimeException("Error", e);
//        }

//        File dir = new File("./");
//        System.out.println(dir.isDirectory());
//        String[] list = dir.list();
//        if (list != null) {
//            for (String name : list) {
//                System.out.println(name);
//            }
//        }

//        try (FileInputStream fis = new FileInputStream(filePath)) {
//            System.out.println(fis.read());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
        File dir = new File("./"); //"./src/ru/basejava/model"
        printDirList(dir, 0);
        System.out.println(Objects.requireNonNull(dir.list()).length);
    }

    private static void printDirList(File file, int level) {
        if (file.isHidden()) {
            return;
        }
        System.out.println("  ".repeat(level) + file.getName());

        if (file.isDirectory()) {
            File[] list = file.listFiles();
            assert list != null;
            for (File f : list) {
                printDirList(f, level + 1);
            }
        }
    }
}
