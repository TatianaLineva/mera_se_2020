package com.mera.Lesson9;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

class FileWorker {

    final static String MAIN_DIRECTORY_PATH = "C://SE2020_LESSON9", SUB_DIRECTORY_NAME = "Directory_", SLASH = "//";
    final static int SUB_DIRECTORIES_LIMIT = 3;

    final static String FILE_NAME = "File_", FILE_EXTENSION = ".txt";
    final static int FILES_LIMIT = 3;

    final static int CONTENT_NUMBERS_MIN = 10, CONTENT_NUMBERS_MAX = 200;

    final static int NUMBER_OF_CHARS_TO_PRINT = 10;

    static Random random = new Random();

    static void createDirectoriesAndFiles() {
        File mainDirectory = new File(MAIN_DIRECTORY_PATH);
        if (mainDirectory.mkdir()) {
            createFiles(MAIN_DIRECTORY_PATH);
            createSubDirectories();
        }
        else {
            System.out.println("Can't create main directory " + MAIN_DIRECTORY_PATH);
        }
    }

    static void createSubDirectories() {
        int subDirectoriesNumber = random.nextInt(SUB_DIRECTORIES_LIMIT) + 1;

        String subDirectoryPath = MAIN_DIRECTORY_PATH + SLASH + SUB_DIRECTORY_NAME + 1;

        for (int i = 1; i <= subDirectoriesNumber; i++) {
            File subDirectory = new File(subDirectoryPath);
            if (subDirectory.mkdir()) {
                createFiles(subDirectoryPath);
                subDirectoryPath += SLASH + SUB_DIRECTORY_NAME + (i + 1);
            }
            else {
                System.out.println("Can't create sub directory " + subDirectoryPath);
                break;
            }
        }
    }

    static void createFiles(String directoryPath) {
        int filesNumber = random.nextInt(FILES_LIMIT) + 1;

        for (int i = 1; i <= filesNumber; i++) {
            String fileName = directoryPath + SLASH + FILE_NAME + i + FILE_EXTENSION;
            File file = new File(fileName);
            try {
                if (file.createNewFile()) {
                    FileWriter fileWriter = new FileWriter(file);
                    int digitsNumber = random.nextInt(CONTENT_NUMBERS_MAX - CONTENT_NUMBERS_MIN + 1) + CONTENT_NUMBERS_MIN;
                    for (int j = 0; j < digitsNumber; j++) {
                        int digit = random.nextInt(10);
                        fileWriter.write(Integer.toString(digit));
                    }
                    fileWriter.close();
                }
            }
            catch (IOException exception) {
                System.out.println("Can't create file " + fileName);
            }
        }
    }

    static void printFilesPath() {
        Path path = Paths.get(MAIN_DIRECTORY_PATH);
        PathPrinterFileVisitor fileVisitor = new PathPrinterFileVisitor();

        System.out.println("Walk file tree:");
        try {
            Files.walkFileTree(path,fileVisitor);
        }
        catch (IOException exception){
            System.out.println("Can't walk file tree");
        }
    }

    static void findSmallestAndLargestFiles() {
        Path path = Paths.get(MAIN_DIRECTORY_PATH);
        SizeCheckerFileVisitor fileVisitor = new SizeCheckerFileVisitor();

        System.out.println("Walk file tree:");
        try {
            Files.walkFileTree(path,fileVisitor);
        }
        catch (IOException exception){
            System.out.println("Can't walk file tree");
        }
    }

    static void printSmallestFile() {
        System.out.println();
        System.out.println("Smallest file(s):");
        for (String filePath: SizeCheckerFileVisitor.getSmallestFilesPath()) {
            System.out.println(filePath);
            System.out.print("Content: ");
            char[] buf = new char[SizeCheckerFileVisitor.getMinFileSize()];
            try {
                printCharsFromFile(filePath, buf);
            }
            catch (IOException exception) {
                System.out.println("Can't print file content");
            }
        }
    }

    static void printLargestFile() {
        System.out.println();
        System.out.println("Largest file(s):");
        for (String filePath: SizeCheckerFileVisitor.getLargestFilesPath()) {
            System.out.println(filePath);
            System.out.print("First chars of content: ");
            char[] buf = new char[NUMBER_OF_CHARS_TO_PRINT];
            try {
                printCharsFromFile(filePath, buf);
            }
            catch (IOException exception) {
                System.out.println("Can't print file content");
            }
        }
    }

    static void printCharsFromFile(String fileName, char[] buf) throws IOException {
        try (FileReader fos = new FileReader(fileName)) {
            fos.read(buf);
        }
        System.out.println(new String(buf));
    }

    public static void deleteAll() {
        delete(new File(MAIN_DIRECTORY_PATH));
        System.out.println();
        System.out.println("Deleted all folders and files");
    }

    public static void delete(File file) {
        if (!file.exists())
            return;
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        file.delete();
    }
}