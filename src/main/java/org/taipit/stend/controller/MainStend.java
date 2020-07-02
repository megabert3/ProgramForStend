package org.taipit.stend.controller;

import org.apache.commons.collections4.MultiMapUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.taipit.stend.model.ExcelReport;

import javax.annotation.processing.SupportedSourceVersion;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


public class MainStend {
    public TreeMap<String, Map> tree;

    public MainStend() {
        tree = new TreeMap<>();
    }

    public static void main(String[] args) throws IOException {
        new ExcelReport().createExcelReport();

//        //1;A;A;P;0.2 Ib;0.5C
//        List<Meter> meterList = new ArrayList<>();
//
//        meterList.add(new Meter());
//        meterList.add(new Meter());
//        meterList.add(new Meter());
//
//        for (Meter meter : meterList) {
//            List<Meter.CommandResult> errorList = meter.getErrorListAPPls();
//            //55.0 U;1;A;A;P;0.2 Ib;0.5C
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.2 Ib;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.1 Ib;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.2 Imax;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.1 Imax;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.2 Ib;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.1 Ib;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.2 Imax;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.1 Imax;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.2 Ib;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.1 Ib;1.0", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.2 Imax;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;A;A;P;0.1 Imax;1.0", "-1", "1"));
//
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.2 Ib;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.1 Ib;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.2 Imax;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.1 Imax;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.2 Ib;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.1 Ib;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.2 Imax;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.1 Imax;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.2 Ib;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.1 Ib;1.0", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.2 Imax;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;B;A;P;0.1 Imax;1.0", "-1", "1"));
//
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.2 Ib;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.1 Ib;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.2 Imax;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.1 Imax;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.2 Ib;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.1 Ib;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.2 Imax;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.1 Imax;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.2 Ib;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.1 Ib;1.0", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.2 Imax;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 U;1;C;A;P;0.1 Imax;1.0", "-1", "1"));
//
//
//
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.2 Ib;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.1 Ib;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.2 Imax;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.1 Imax;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.2 Ib;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.1 Ib;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.2 Imax;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.1 Imax;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.2 Ib;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.1 Ib;1.0", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.2 Imax;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;A;A;P;0.1 Imax;1.0", "-1", "1"));
//
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.2 Ib;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.1 Ib;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.2 Imax;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.1 Imax;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.2 Ib;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.1 Ib;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.2 Imax;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.1 Imax;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.2 Ib;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.1 Ib;1.0", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.2 Imax;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;B;A;P;0.1 Imax;1.0", "-1", "1"));
//
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.2 Ib;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.1 Ib;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.2 Imax;0.5C", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.1 Imax;0.5C", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.2 Ib;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.1 Ib;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.2 Imax;0.5L", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.1 Imax;0.5L", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.2 Ib;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.1 Ib;1.0", "-1", "1"));
//
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.2 Imax;1.0", "-1", "1"));
//            errorList.add(meter.new ErrorResult("55.0 F;1;C;A;P;0.1 Imax;1.0", "-1", "1"));
//
//            for (Meter.CommandResult result : errorList) {
//
//                double x = (Math.random()*((Float.parseFloat(result.getMaxError()) - Float.parseFloat(result.getMinError())) + 1)) + Float.parseFloat(result.getMinError());
//                System.out.println(x);
//                System.out.println();
//
//                if (x < Float.parseFloat(result.getMinError()) || x > Float.parseFloat(result.getMaxError())) {
//                    result.setPassTest(false);
//                } else {
//                    result.setPassTest(true);
//                }
//
//                result.setLastResult(new BigDecimal(x).setScale(3, RoundingMode.HALF_UP).toString());
//            }
//
//        }

        //MainStend mainStend = new MainStend();

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

        //MainStend.InfABCGroup group = mainStend.new InfABCGroup();

//        mainStend.tree.put("F;55;A;L;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;L;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;A;L;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;L;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;A;L;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;L;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;A;L;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;L;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;A;C;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;C;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;A;C;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;C;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;A;C;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;C;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;A;C;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;C;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;A;0;1.0;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;0;1.0;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;A;0;1.0;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;A;0;1.0;Ib;0.01", new HashMap());
//
//
//        mainStend.tree.put("F;55;B;L;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;L;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;B;L;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;L;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;B;L;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;L;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;B;L;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;L;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;B;C;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;C;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;B;C;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;C;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;B;C;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;C;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;B;C;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;C;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;B;0;1.0;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;0;1.0;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;B;0;1.0;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;B;0;1.0;Ib;0.01", new HashMap());
//
//
//        mainStend.tree.put("F;55;C;L;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;L;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;C;L;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;L;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;C;L;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;L;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;C;L;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;L;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;C;C;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;C;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;C;C;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;C;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;C;C;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;C;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;C;C;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;C;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("F;55;C;0;1.0;Imax;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;0;1.0;Imax;0.01", new HashMap());
//        mainStend.tree.put("F;55;C;0;1.0;Ib;0.02", new HashMap());
//        mainStend.tree.put("F;55;C;0;1.0;Ib;0.01", new HashMap());
//
//
//
//
//        mainStend.tree.put("U;55;A;L;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;L;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;A;L;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;L;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;A;L;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;L;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;A;L;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;L;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;A;C;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;C;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;A;C;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;C;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;A;C;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;C;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;A;C;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;C;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;A;0;1.0;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;0;1.0;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;A;0;1.0;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;A;0;1.0;Ib;0.01", new HashMap());
//
//
//        mainStend.tree.put("U;55;B;L;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;L;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;B;L;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;L;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;B;L;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;L;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;B;L;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;L;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;B;C;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;C;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;B;C;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;C;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;B;C;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;C;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;B;C;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;C;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;B;0;1.0;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;0;1.0;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;B;0;1.0;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;B;0;1.0;Ib;0.01", new HashMap());
//
//
//        mainStend.tree.put("U;55;C;L;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;L;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;C;L;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;L;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;C;L;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;L;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;C;L;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;L;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;C;C;0.5;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;C;0.5;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;C;C;0.5;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;C;0.5;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;C;C;0.25;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;C;0.25;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;C;C;0.25;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;C;0.25;Ib;0.01", new HashMap());
//
//        mainStend.tree.put("U;55;C;0;1.0;Imax;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;0;1.0;Imax;0.01", new HashMap());
//        mainStend.tree.put("U;55;C;0;1.0;Ib;0.02", new HashMap());
//        mainStend.tree.put("U;55;C;0;1.0;Ib;0.01", new HashMap());
//
//        for (Map.Entry<String, Map> map : mainStend.tree.entrySet()) {
//            group.putResultInGroup(map.getKey(), map.getValue());
//        }

//        group.getElements();
//
//        group.print();
    }

    public class CRPSTAotherGroup {
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

        private Map<String, Map> mainMap = new TreeMap<>(comparatorForCRPSTA);

        public void putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            mainMap.put(keyId, commandResultMap);
        }

        public void getElements() {
            for (String str : mainMap.keySet()) {
                System.out.println(str);
            }
        }
    }

    public class TotalErrorsGroup {
        //L;0.5;Imax;0.02

        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;

        private String PFkey;
        private String currKey;

        public TotalErrorsGroup() {
            powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
        }

        public void putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");

            currKey = idResult[3] + " " + idResult[2];

            if (idResult[0].equals("0")) {
                PFkey = idResult[1];
            } else {
                PFkey = idResult[1] + " " + idResult[0];
            }

            if (!powerFactorMap.containsKey(PFkey)) {
                currentMap = new TreeMap<>(comparatorForCurrent);

                powerFactorMap.put(PFkey, currentMap);

                currentMap.put(currKey, commandResultMap);
            } else {
                currentMap = powerFactorMap.get(PFkey);

                currentMap.put(currKey, commandResultMap);
            }

        }

        public void getElements() {
            System.out.println(powerFactorMap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : powerFactorMap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println("    " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("     " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("     " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }
    }

    public class ABCGroup {
        //A;L;0.5;Imax;0.02

        private Map<String, Map> ABCMap;
        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;

        private String ABCkey;
        private String PFkey;
        private String currKey;

        public ABCGroup() {
            ABCMap = new TreeMap<>(comparatorForABC);
        }

        public boolean putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");

            ABCkey = idResult[0];
            currKey = idResult[4] + " " + idResult[3];

            if (idResult[1].equals("0")) {
                PFkey = idResult[2];
            } else {
                PFkey = idResult[2] + " " + idResult[1];
            }

            if (!ABCMap.containsKey(ABCkey)) {

                powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                currentMap = new TreeMap<>(comparatorForCurrent);

                ABCMap.put(ABCkey, powerFactorMap);
                powerFactorMap.put(PFkey, currentMap);
                currentMap.put(currKey, commandResultMap);
                return true;
            } else {

                powerFactorMap = ABCMap.get(ABCkey);
                if (!powerFactorMap.containsKey(PFkey)) {

                    currentMap = new TreeMap<>(comparatorForCurrent);

                    powerFactorMap.put(PFkey, currentMap);
                    currentMap.put(currKey, commandResultMap);
                    return true;
                } else {
                    currentMap = powerFactorMap.get(PFkey);
                    currentMap.put(currKey, commandResultMap);
                    return true;
                }
            }
        }

        public void getElements() {
            System.out.println(ABCMap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : ABCMap.entrySet()) {
                System.out.println(mapEntry.getKey());
                ABC = mapEntry.getValue();

                for (Map.Entry<String, Map> abc : ABC.entrySet()) {
                    System.out.println(" " + abc.getKey());
                    valueLC0 = abc.getValue();

                    for (Map.Entry<String, Map> asfa : valueLC0.entrySet()) {
                        System.out.println("     " + asfa.getKey());

                        valueCurrent = asfa.getValue();

                        for (Map.Entry<String, Map> asfaasdas : valueCurrent.entrySet()) {
                            System.out.println("     " + asfaasdas.getKey());
                        }
                    }
                }
            }
        }
    }

    public class InfGroup {
        //F;55;L;0.5;Imax;0.02

        private Map<String, Map> UorFmap;

        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;

        private String UorFkey;
        private String PFkey;
        private String currKey;

        public InfGroup() {
            UorFmap = new TreeMap<>(comparatorForUorF);
        }

        public void putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            String[] idResult = keyId.split(";");
            UorFkey = idResult[1] + " %" + idResult[0] + "n";
            currKey = idResult[5] + " " + idResult[4];

            if (idResult[2].equals("0")) {
                PFkey = idResult[3];
            } else {
                PFkey = idResult[3] + " " + idResult[2];
            }

            if (UorFmap.get(UorFkey) == null) {
                powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                currentMap = new TreeMap<>(comparatorForCurrent);

                UorFmap.put(UorFkey, powerFactorMap);
                powerFactorMap.put(PFkey, currentMap);
                currentMap.put(currKey, commandResultMap);
            } else {
                powerFactorMap = UorFmap.get(UorFkey);

                if (powerFactorMap.get(PFkey) == null) {
                    currentMap = new TreeMap<>(comparatorForCurrent);

                    powerFactorMap.put(PFkey, currentMap);
                    currentMap.put(currKey, commandResultMap);
                } else {
                    currentMap = powerFactorMap.get(PFkey);

                    currentMap.put(currKey, commandResultMap);
                }
            }
        }

        public void getElements() {
            System.out.println(UorFmap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : UorFmap.entrySet()) {
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

    public class InfABCGroup {
        //F;55;A;L;0.5;Imax;0.02

        private int totalElements;
        private Map<String, Map> UorFmap;

        private Map<String, Map> ABCMap;
        private Map<String, Map> powerFactorMap;
        private Map<String, Map> currentMap;


        private String UorFkey;
        private String ABCkey;
        private String PFkey;
        private String currKey;

        public InfABCGroup() {
            UorFmap = new TreeMap<>(comparatorForUorF);
        }

        public void putResultInGroup(String keyId, Map<Integer, Meter.CommandResult> commandResultMap) {
            totalElements++;

            String[] idResult = keyId.split(";");
            UorFkey = idResult[1] + " %" + idResult[0] + "n";
            ABCkey = idResult[2];
            currKey = idResult[6] + " " + idResult[5];

            if (idResult[3].equals("0")) {
                PFkey = idResult[4];
            } else {
                PFkey = idResult[4] + " " + idResult[3];
            }

            if (UorFmap.get(UorFkey) == null) {
                ABCMap = new TreeMap<>(comparatorForABC);
                powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                currentMap = new TreeMap<>(comparatorForCurrent);

                UorFmap.put(UorFkey, ABCMap);
                ABCMap.put(ABCkey, powerFactorMap);
                powerFactorMap.put(PFkey, currentMap);
                currentMap.put(currKey, commandResultMap);
            } else {
                ABCMap = UorFmap.get(UorFkey);

                if (ABCMap.get(ABCkey) == null) {
                    powerFactorMap = new TreeMap<>(comparatorForPowerFactor);
                    currentMap = new TreeMap<>(comparatorForCurrent);

                    ABCMap.put(ABCkey, powerFactorMap);
                    powerFactorMap.put(PFkey, currentMap);
                    currentMap.put(currKey, commandResultMap);
                } else {
                    powerFactorMap = ABCMap.get(ABCkey);

                    if (powerFactorMap.get(PFkey) == null) {
                        currentMap = new TreeMap<>(comparatorForCurrent);

                        powerFactorMap.put(PFkey, currentMap);
                        currentMap.put(currKey, commandResultMap);
                    } else {
                        currentMap = powerFactorMap.get(PFkey);
                        currentMap.put(currKey, commandResultMap);
                    }
                }
            }
        }

        public void print() {

            if (totalElements == 0) return;

            int printIndexUorF = 0;
            int printIndexABC = 0;
            int printIndex = 0;

            Map<String, Integer> UorFCount = new TreeMap<>(comparatorForUorF);
            Map<String, Integer> ABCCount =  new TreeMap<>(comparatorForABC);
            Map<String, Map<String, Integer>> PFmapCount = new TreeMap<>(comparatorForABC);

            Map<String, Integer> PFcount;

            Map<String, Map> ABCmap;
            Map<String, Map> PFmap;

            String UorFKey;
            String ABCKey;
            String PFKey;

            for (Map.Entry<String, Map> UorFmap : UorFmap.entrySet()) {

                UorFKey = UorFmap.getKey();

                UorFCount.put(UorFKey, 0);

                ABCmap = UorFmap.getValue();

                for (Map.Entry<String, Map> mapABC : ABCmap.entrySet()) {
                    ABCKey = mapABC.getKey();

                    ABCCount.put(ABCKey, 0);

                    PFcount = new TreeMap<>(comparatorForPowerFactor);

                    PFmapCount.put(ABCKey, PFcount);

                    PFmap = mapABC.getValue();

                    for (Map.Entry<String, Map> mapPF : PFmap.entrySet()) {
                        PFKey = mapPF.getKey();

                        PFcount.put(PFKey, mapPF.getValue().size());
                        ABCCount.put(ABCKey, ABCCount.get(ABCKey) + mapPF.getValue().size());
                        UorFCount.put(UorFKey, UorFCount.get(UorFKey) + mapPF.getValue().size());
                    }
                }
            }
        }

        public void getElements() {
            System.out.println(UorFmap);
            Map<String, Map> ABC;
            Map<String, Map> valueLC0;
            Map<String, Map> valueCurrent;

            for (Map.Entry<String, Map> mapEntry : UorFmap.entrySet()) {
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

    private Comparator<String> comparatorForUorF = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.contains("U") && o2.contains("F")) {
                return -1;
            } else if (o1.contains("F") && o2.contains("U")) {
                return 1;
            } else if (o1.contains("U") && o2.contains("U") ||
                    o1.contains("F") && o2.contains("F")) {
                String[] arrO1 = o1.split(" ");
                String[] arro2 = o2.split(" ");

                if (Float.parseFloat(arrO1[0]) > Float.parseFloat(arro2[0])) {
                    return -1;
                } else if (Float.parseFloat(arrO1[0]) < Float.parseFloat(arro2[0])) {
                    return 1;
                } else return 0;
            } else return 0;
        }
    };

    private Comparator<String> comparatorForPowerFactor = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {

            if ((!o1.contains("L") && !o1.contains("C")) && (o2.contains("L") || o2.contains("C"))) {
                return -1;
            } else if ((o1.contains("L") || o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                return 1;
            } else if (o1.contains("L") && o2.contains("C")) {
                return -1;
            } else if (o1.contains("C") && o2.contains("L")) {
                return 1;
            } else if ((!o1.contains("L") && !o1.contains("C")) && (!o2.contains("L") && !o2.contains("C"))) {
                String[] pf1 = o1.split(" ");
                String[] pf2 = o2.split(" ");

                if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                    return -1;
                } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                    return 1;
                } else return 0;
            } else if (o1.contains("L") & o2.contains("L") ||
                    o1.contains("C") & o2.contains("C")) {
                String[] pf1 = o1.split(" ");
                String[] pf2 = o2.split(" ");

                if (Float.parseFloat(pf1[0]) > Float.parseFloat(pf2[0])) {
                    return -1;
                } else if (Float.parseFloat(pf1[0]) < Float.parseFloat(pf2[0])) {
                    return 1;
                } else return 0;
            } else return 0;
        }
    };

    private Comparator<String> comparatorForCurrent = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.contains("Imax") && o2.contains("Ib")) {
                return -1;
            } else if (o1.contains("Ib") && o2.contains("Imax")) {
                return 1;
            } else if (o1.contains("Imax") && o2.contains("Imax") ||
                    o1.contains("Ib") && o2.contains("Ib")) {
                String[] curr1 = o1.split(" ");
                String[] curr2 = o2.split(" ");

                if (Float.parseFloat(curr1[0]) > Float.parseFloat(curr2[0])) {
                    return -1;
                } else if (Float.parseFloat(curr1[0]) < Float.parseFloat(curr2[0])){
                    return 1;
                } else return 0;
            } else return 0;
        }
    };

    private Comparator<String> comparatorForABC = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            if (o1.equals("A") & !o2.equals("A")) {
                return -1;
            } else if (!o1.equals("A") & o2.equals("A")){
                return 1;
            }  else if (o1.equals("B") & o2.equals("C")) {
                return -1;
            } else if (o1.equals("C") & o2.equals("B")) {
                return 1;
            } else {
                return 0;
            }
        }
    };

    Comparator<String> comparatorForCRPSTA = new Comparator<String>() {
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



