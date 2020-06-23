package org.taipit.stend.controller;


import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.ss.util.RegionUtil;
import org.taipit.stend.model.ExcelReport;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import java.util.*;


public class MainStend {
    public TreeMap<String, Map> tree;

    public MainStend() {
        tree = new TreeMap<>(comparatorForInflABC);
    }

    public static void main(String[] args) {
        //new ExcelReport().createExcelReport();
        MainStend mainStend = new MainStend();
        MainStend.InfABCGroup group = new InfABCGroup();

        mainStend.tree.put("U;90;A;L;0.5;Imax;1.0", new HashMap());
        mainStend.tree.put("U;90;A;L;0.5;Imax;0.9", new HashMap());
        mainStend.tree.put("U;90;A;L;0.5;Imax;0.8", new HashMap());
        mainStend.tree.put("U;90;A;L;0.5;Ib;1.0", new HashMap());
        mainStend.tree.put("U;90;A;L;0.5;Ib;0.9", new HashMap());
        mainStend.tree.put("U;90;A;L;0.5;Ib;0.8", new HashMap());


        for (Map.Entry<String, Map> map : mainStend.tree.entrySet()) {
            System.out.println(map.getKey());
            group.putResultInGroup(map.getKey(), map.getValue());
        }

        group.getElements();
    }

    public static class InfABCGroup {
        //F;55;A;L;0.5;Imax;0.02

        Map<String, Map> mainMap = new HashMap<>();

        Map<String, Map> ABC;
        Map<String, Map> valueLC0;
        Map<String, Map> valueCurrent;

        String UorFproc;
        String AorBorC;
        String powerFactor;
        String currentProc;

        public boolean putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");
            UorFproc = idResult[1] + idResult[0] + "n";
            AorBorC = idResult[2];
            currentProc = idResult[6] + idResult[5];

            if (idResult[3].equals("0")) {
                powerFactor = idResult[4];
            } else {
                powerFactor = idResult[4] + idResult[3];
            }

            //Если уже есть элементы в этой группе
            if (mainMap.size() != 0) {
                //Если приходящий элемент не соответствует типу основновной группы
                if (mainMap.get(UorFproc) == null) {
                    return false;
                    //Если соответствует
                } else {
                    //Вниз по иерархии к пофазному
                    ABC = mainMap.get(UorFproc);
                    //Если новая фаза
                    if (ABC.get(AorBorC) == null) {

                        valueLC0 = new HashMap<>();
                        valueCurrent = new HashMap<>();

                        ABC.put(AorBorC, valueLC0);

                        valueLC0.put(powerFactor, valueCurrent);

                        valueCurrent.put(currentProc, commandResultMap);
                        return true;

                    } else {
                        valueLC0 = ABC.get(AorBorC);

                        if (valueLC0.get(powerFactor) == null) {

                            valueCurrent = new HashMap<>();

                            valueLC0.put(powerFactor, valueCurrent);

                            valueCurrent.put(currentProc, commandResultMap);
                            return true;

                        } else {
                            valueCurrent = valueLC0.get(powerFactor);

                            valueCurrent.put(currentProc, commandResultMap);
                            return true;
                        }
                    }
                }
                //Если это первый элемент приходящий в группу
            } else {
                ABC = new HashMap<>();
                valueLC0 = new HashMap<>();
                valueCurrent = new HashMap<>();

                mainMap.put(UorFproc, ABC);

                ABC.put(AorBorC, valueLC0);

                valueLC0.put(powerFactor, valueCurrent);

                valueCurrent.put(currentProc, commandResultMap);
                return true;
            }
        }

        public void getElements() {
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : mainMap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println("    " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("        " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("            " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }
    }

    Comparator<String> comparatorForInflABC = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //F;55;A;L;0.5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("U") && arrO2[0].equals("F")) {
                return -1;
            } else if (arrO1[0].equals("F") && arrO2[0].equals("U")) {
                return 1;
            } else if (arrO1[0].equals("U") && arrO2[0].equals("U") ||
                    arrO1[0].equals("F") && arrO2[0].equals("F")) {
                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return -1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return 1;
                } else {
                    if (arrO1[2].equals("A") && !arrO2[2].equals("A")) {
                        return -1;
                    } else if (!arrO1[2].equals("A") && arrO2[2].equals("A")) {
                        return  1;
                    } else if (arrO1[2].equals("B") && arrO2[2].equals("C")) {
                        return -1;
                    } else if (arrO1[2].equals("C") && arrO2[2].equals("B")) {
                        return 1;
                    } else if (arrO1[2].equals("A") && arrO2[2].equals("A") ||
                            arrO1[2].equals("B") && arrO2[2].equals("B") ||
                            arrO1[2].equals("C") && arrO2[2].equals("C")) {

                        if (arrO1[3].equals("0") && !arrO2[3].equals("0")) {
                            return -1;
                        } else if (!arrO1[3].equals("0") && arrO2[3].equals("0")) {
                            return 1;
                        } else if (arrO1[3].equals("L") && arrO2[3].equals("C")) {
                            return -1;
                        } else if (arrO1[3].equals("C") && arrO2[3].equals("L")) {
                            return 1;
                        } else if ((arrO1[3].equals("0") && arrO2[3].equals("0")) ||
                                (arrO1[3].equals("L") && arrO2[3].equals("L")) ||
                                (arrO1[3].equals("C") && arrO2[3].equals("C"))) {

                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else {
                                if (arrO1[5].equals("Imax") && arrO2[5].equals("Ib")) {
                                    return -1;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Imax")) {
                                    return 1;
                                } else if (arrO1[5].equals("Imax") && arrO2[5].equals("Imax")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else return 0;
                                } else if (arrO1[5].equals("Ib") && arrO2[5].equals("Ib")) {
                                    if (Float.parseFloat(arrO1[6]) > Float.parseFloat(arrO2[6])) {
                                        return -1;
                                    } else if (Float.parseFloat(arrO1[6]) < Float.parseFloat(arrO2[6])) {
                                        return 1;
                                    } else return 0;
                                } else return 0;
                            }
                        } else return 0;
                    } else return 0;
                }
            } else return 0;
        }
    };
}

