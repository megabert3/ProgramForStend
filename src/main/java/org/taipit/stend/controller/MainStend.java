package org.taipit.stend.controller;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.taipit.stend.model.ExcelReport;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MainStend {
    public TreeMap<String, HashMap> tree;

    public MainStend() {
        tree = new TreeMap<>(comparatorCRPSTAother);
    }

    public static void main(String[] args) {
        //new ExcelReport().createExcelReport();
        MainStend mainStend = new MainStend();

        mainStend.tree.put("STAAP", new HashMap<>());
        mainStend.tree.put("RTC", new HashMap<>());
        mainStend.tree.put("CRP", new HashMap<>());
        mainStend.tree.put("STAAN", new HashMap<>());
        mainStend.tree.put("STARN", new HashMap<>());
        mainStend.tree.put("CNTAP", new HashMap<>());
        mainStend.tree.put("CNTRP", new HashMap<>());
        mainStend.tree.put("APR", new HashMap<>());
        mainStend.tree.put("CNTAN", new HashMap<>());
        mainStend.tree.put("STARP", new HashMap<>());
        mainStend.tree.put("INS", new HashMap<>());
        mainStend.tree.put("CNTRN", new HashMap<>());

        for (Map.Entry<String, HashMap> elem : mainStend.tree.entrySet()) {
            System.out.println(elem.getKey());
        }
    }

    Comparator<String> comparatorCRPSTAother = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //CRP Самоход
            //STAAP Чувствтельность AP+
            //STAAN Чувствтельность AP-
            //STARP Чувствтельность RP+
            //STARN Чувствтельность RP-
            //RTC ТХЧ
            //CNTAP Константа
            //CNTAN Константа
            //CNTRP Константа
            //CNTRN Константа
            //INS Изоляция
            //APR Внешний вид

            if (o1.equals("CRP") && !o2.equals("CRP")) {
                return -1;
            } else if (!o1.equals("CRP") && o2.equals("CRP")) {
                return 1;
            } else if (o1.equals("STAAP") && !o2.equals("STAAP")) {
                return -1;
            } else if (!o1.equals("STAAP") && o2.equals("STAAP")) {
                return 1;
            } else if (o1.equals("STAAN") && !o2.equals("STAAN")) {
                return -1;
            } else if (!o1.equals("STAAN") && o2.equals("STAAN")) {
                return 1;
            } else if (o1.equals("STARP") && !o2.equals("STARP")) {
                return -1;
            } else if (!o1.equals("STARP") && o2.equals("STARP")) {
                return 1;
            } else if (o1.equals("STARN") && !o2.equals("STARN")) {
                return -1;
            } else if (!o1.equals("STARN") && o2.equals("STARN")) {
                return 1;
            } else if (o1.equals("RTC") && !o2.equals("RTC")) {
                return -1;
            } else if (!o1.equals("RTC") && o2.equals("RTC")) {
                return 1;
            } else if (o1.equals("CNTAP") && !o2.equals("CNTAP")) {
                return -1;
            } else if (!o1.equals("CNTAP") && o2.equals("CNTAP")) {
                return 1;
            } else if (o1.equals("CNTAN") && !o2.equals("CNTAN")) {
                return -1;
            } else if (!o1.equals("CNTAN") && o2.equals("CNTAN")) {
                return 1;
            } else if (o1.equals("CNTRP") && !o2.equals("CNTRP")) {
                return -1;
            } else if (!o1.equals("CNTRP") && o2.equals("CNTRP")) {
                return 1;
            } else if (o1.equals("CNTRN") && !o2.equals("CNTRN")) {
                return -1;
            } else if (!o1.equals("CNTRN") && o2.equals("CNTRN")) {
                return 1;
            } else if (o1.equals("INS") && !o2.equals("INS")) {
                return -1;
            } else if (!o1.equals("INS") && o2.equals("INS")) {
                return 1;
            } else if (o1.equals("APR") && !o2.equals("APR")) {
                return -1;
            } else if (!o1.equals("APR") && o2.equals("APR")) {
                return 1;
            } else return 0;
        }
    };
}

