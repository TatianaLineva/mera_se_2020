package com.mera.Lesson10;

import java.util.ArrayList;
import java.util.List;

class Multithreading {
    public static void main(String[] args) {
        List<String> URLList = new ArrayList<>();
        URLList.add("https://developer.android.com/");
        URLList.add("https://developer.android.com/about");
        URLList.add("https://developer.android.com/news");

        URLReader.setURLs(URLList);
        URLReader.readSequentially();
        URLReader.readParallel();
    }
}