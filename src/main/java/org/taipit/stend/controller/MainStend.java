package org.taipit.stend.controller;

import javafx.scene.control.TableColumn;

import java.lang.reflect.Array;
import java.util.Arrays;

public class MainStend {

    public static void main(String[] args) throws InterruptedException {
        String[] srt = {"1", "2", "3"};
        String str2 = Arrays.toString(srt);
        System.out.println(str2.substring(1,str2.length() - 1));
    }

}
