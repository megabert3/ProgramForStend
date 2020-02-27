package org.taipit.stend.controller;


import java.io.File;
import java.io.IOException;

public class MainStend {

    public static void main(String[] args) throws IOException {
        File file = new File(".\\src\\main\\resources\\stendProperties.properties").getCanonicalFile();
        System.out.println(file.getPath());
    }
}
