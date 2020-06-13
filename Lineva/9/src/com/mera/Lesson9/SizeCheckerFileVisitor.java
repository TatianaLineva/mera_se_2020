package com.mera.Lesson9;

import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

import static java.nio.file.FileVisitResult.*;
import static com.mera.Lesson9.FileWorker.*;

class SizeCheckerFileVisitor extends SimpleFileVisitor<Path> {

    private static int minFileSize = CONTENT_NUMBERS_MAX, maxFileSize = 0;
    private static List<String> smallestFilesPath = new ArrayList<>();
    private static List<String> largestFilesPath = new ArrayList<>();

    static public int getMinFileSize() {
        return minFileSize;
    }

    static public int getMaxFileSize() {
        return maxFileSize;
    }

    static public List<String> getSmallestFilesPath() {
        return smallestFilesPath;
    }

    static public List<String> getLargestFilesPath() {
        return largestFilesPath;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {
        int fileSize = (int)attr.size();
        String filePath = file.toAbsolutePath().toString();

        System.out.println(filePath + ", size " + fileSize);

        if (fileSize < minFileSize) {
            minFileSize = fileSize;
            smallestFilesPath.clear();
            smallestFilesPath.add(filePath);
        }
        else if (fileSize == minFileSize) {
            smallestFilesPath.add(filePath);
        }

        if (fileSize > maxFileSize) {
            maxFileSize = fileSize;
            largestFilesPath.clear();
            largestFilesPath.add(filePath);
        }
        else if (fileSize == maxFileSize) {
            largestFilesPath.add(filePath);
        }

        return CONTINUE;
    }
}