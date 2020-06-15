package com.mera.Lesson10;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

class URLReader {

    static List<String> URLs = new ArrayList<>();
    static Integer URLsSizeSequentialDownload;
    static CopyOnWriteArrayList<Integer> URLsSizeParallelDownload = new CopyOnWriteArrayList<>();

    static void setURLs(List<String> URLList) {
        URLs.clear();
        URLsSizeParallelDownload.clear();
        URLsSizeSequentialDownload = 0;

        for (int i = 0; i < URLList.size(); i++) {
            URLs.add(URLList.get(i));
            URLsSizeParallelDownload.add(i, 0);
        }
    }

    static void readSequentially() {
        System.out.println("Starting sequential reading...");

        long startTime = System.currentTimeMillis();

        for (String URL: URLs) {
            try {
                readURLSequentially(URL);
            }
            catch (IOException exception) {
                System.out.println("Can't read URL " + URL);
            }
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        System.out.println("Sequential download result:");
        System.out.println("Time: " + elapsedTime);
        System.out.println("Size: " + URLsSizeSequentialDownload);
    }

    static void readParallel() {
        System.out.println("Starting parallel reading...");
        long startTime = System.currentTimeMillis();

        Thread URLReaderThreadOne = new Thread(() -> {
            try {
                readURLParallel(URLs.get(0), 0);
            } catch (IOException e) {
                System.out.println("Can't read URL " + URLs.get(0));
            }
        });

        Thread URLReaderThreadTwo = new Thread(() -> {
            try {
                readURLParallel(URLs.get(1), 1);
            } catch (IOException e) {
                System.out.println("Can't read URL " + URLs.get(1));
            }
        });

        Thread URLReaderThreadThree = new Thread(() -> {
            try {
                readURLParallel(URLs.get(2), 2);
            } catch (IOException e) {
                System.out.println("Can't read URL " + URLs.get(2));
            }
        });

        URLReaderThreadOne.start();
        URLReaderThreadTwo.start();
        URLReaderThreadThree.start();

        try {
            URLReaderThreadOne.join();
            URLReaderThreadTwo.join();
            URLReaderThreadThree.join();
        }
        catch (InterruptedException exception) {
            System.out.println("Thread is interrupted");
        }

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - startTime;

        Integer URLSize = 0;
        for (Integer size: URLsSizeParallelDownload) {
            URLSize += size;
        }

        System.out.println("Parallel download result:");
        System.out.println("Time: " + elapsedTime);
        System.out.println("Size: " + URLSize);
    }

    static void readURLSequentially(String URL) throws IOException {
        Integer URLSize = 0;
        try (InputStream inputStream = new URL(URL).openStream()) {
                while (inputStream.read() != -1) {
                    URLSize++;
                }
        }
        URLsSizeSequentialDownload += URLSize;
        System.out.println("Successfully read URL " + URL + ", size " + URLSize);
    }

    static void readURLParallel(String URL, int index) throws IOException {
        Integer URLSize = 0;
        try (InputStream inputStream = new URL(URL).openStream()) {
            while (inputStream.read() != -1) {
                URLSize++;
            }
        }
        URLsSizeParallelDownload.set(index, URLSize);

        System.out.println("Successfully read URL " + URL + ", size " + URLSize);
    }
}