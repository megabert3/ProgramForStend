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

import java.util.concurrent.TimeUnit;

public class MainStend {

    public static String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static void main(String[] args) {
        new ExcelReport().createExcelReport();



//        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
//
//        ThreePhaseRefMeterParameters refMeterParameters = new ThreePhaseRefMeterParameters();
//
//        String std;
//
//        stendDLLCommands.setReviseMode(1);
//
//        long timeEnd;
//        long time = System.currentTimeMillis();
//        //stendDLLCommands.getUI(0, 230.0, 5, 50, 0, 0, 100, 100, "С", "1.0");
//        stendDLLCommands.getUIWithPhase(1, 230.0, 5, 50, 0, 0, 0, 0, 100, 100, "A", "1.0");
//        timeEnd = System.currentTimeMillis() - time;
//        System.out.println("Time to set command Adjust_UI " + timeEnd + " ms with RefType: " + stendDLLCommands.getTypeReferenceMeter());
//
//
//        Thread.sleep(5000);
//
//        time = System.currentTimeMillis();
//        std = stendDLLCommands.stMeterRead();
//        timeEnd = System.currentTimeMillis() - time;
//        System.out.println("Time to set command StdMeter_Read " + timeEnd + " ms with RefType: " + stendDLLCommands.getTypeReferenceMeter());
//        System.out.println("Values returned command StdMeter_Read \n" + std);
//        stendDLLCommands.powerOf();

    }
}
//
//
//
////        String time = "104:60:60";
////
////        String[] timeArr = time.split(":");
////
////        long timeMls = (Integer.parseInt(timeArr[0]) * 3600 + Integer.parseInt(timeArr[1]) * 60 + Integer.parseInt(timeArr[2])) * 1000;
////
////        System.out.println(time);
////        System.out.println(timeMls);
////        System.out.println(getTime(timeMls));
//
//
////        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
////        try {
////
////            stendDLLCommands.errorClear();
////
////            stendDLLCommands.getUI(1, 230, 5, 50, 0, 0, 100, 0, "H", "1.0");
////
////            Thread.sleep(3000);
////
////            for (int i = 1; i < 3; i++) {
////                stendDLLCommands.setPulseChannel(i, 0);
////                stendDLLCommands.constTestStart(i, 8000);
////            }
////
////            stendDLLCommands.getUI(1, 230, 10, 50, 0, 0, 100, 100, "H", "1.0");
////
////            Thread.sleep(60000);
////
////            stendDLLCommands.powerOf();
////
////            for (int i = 1; i < 3; i++) {
////                System.out.println(stendDLLCommands.constProcRead(8000, i));
////            }
////
////
////        }catch (Exception e) {
////            stendDLLCommands.errorClear();
////            stendDLLCommands.powerOf();
////        }
//
//// Старт теста констант
//// Meter_No - номер места
//// Constant - постоянная
//// Dev_Port - номер com-порта
//
////        boolean ConstTest_Start(int Meter_No,
////        double Constant,
////        int Dev_Port);
////
//////--------------------------------------------------------------------------- ConstPulse_Read
////// Чтение данных по энергии
////// MeterKWH - значения счетчика
////// StdKWH - Значение эталонника
////// Constant - постоянная
////// Meter_No - номер места
////// Dev_Port - номер com-порта
//
////        boolean ConstPulse_Read(String MeterKWH,
////                String StdKWH,
////        double Constant,
////        int Meter_No,
////        int Dev_Port);

