package org.taipit.stend.model;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

public class ExcelReport {

    public ExcelReport() {
        CRPSTAother = new TreeMap<>(comparatorCRPSTAother);

        inflABCAPPls = new TreeMap<>(comparatorForInflABC);
        inflAPPls = new TreeMap<>(comparatorForInfl);
        ABCAPPls = new TreeMap<>(comparatorForABC);
        totalErrorAPPls = new TreeMap<>(comparatorForTotalError);
        imbalansAPPls = new TreeMap<>();

        inflABCAPMns = new TreeMap<>(comparatorForInflABC);
        inflAPMns = new TreeMap<>(comparatorForInfl);
        ABCAPMns = new TreeMap<>(comparatorForABC);
        totalErrorAPMns = new TreeMap<>(comparatorForTotalError);
        imbalansAPMns = new TreeMap<>();

        inflABCRPPls = new TreeMap<>(comparatorForInflABC);
        inflRPPls = new TreeMap<>(comparatorForInfl);
        ABCRPPls = new TreeMap<>(comparatorForABC);
        totalErrorRPPls = new TreeMap<>(comparatorForTotalError);
        imbalansRPPls = new TreeMap<>();

        inflABCRPMns = new TreeMap<>(comparatorForInflABC);
        inflRPMns = new TreeMap<>(comparatorForInfl);
        ABCRPMns = new TreeMap<>(comparatorForABC);
        totalErrorRPMns = new TreeMap<>(comparatorForTotalError);
        imbalansRPMns = new TreeMap<>();

        CRPSTAother.put("CRP", new HashMap<>());
        CRPSTAother.put("STAAP", new HashMap<>());
        CRPSTAother.put("STAAN", new HashMap<>());
        CRPSTAother.put("STARP", new HashMap<>());
        CRPSTAother.put("STARN", new HashMap<>());
        CRPSTAother.put("RTC", new HashMap<>());
        CRPSTAother.put("CNTAP", new HashMap<>());
        CRPSTAother.put("CNTAN", new HashMap<>());
        CRPSTAother.put("CNTRP", new HashMap<>());
        CRPSTAother.put("CNTRN", new HashMap<>());
        CRPSTAother.put("INS", new HashMap<>());
        CRPSTAother.put("APR", new HashMap<>());
    }

    private List<Meter> meters;

    private Map<String, Map<Integer, Meter.CommandResult>> CRPSTAother;

    private Map<String, Map<Integer, Meter.CommandResult>> inflABCAPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> inflAPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> ABCAPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> totalErrorAPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> imbalansAPPls;

    private Map<String, Map<Integer, Meter.CommandResult>> inflABCAPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> inflAPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> ABCAPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> totalErrorAPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> imbalansAPMns;

    private Map<String, Map<Integer, Meter.CommandResult>> inflABCRPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> inflRPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> ABCRPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> totalErrorRPPls;
    private Map<String, Map<Integer, Meter.CommandResult>> imbalansRPPls;

    private Map<String, Map<Integer, Meter.CommandResult>> inflABCRPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> inflRPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> ABCRPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> totalErrorRPMns;
    private Map<String, Map<Integer, Meter.CommandResult>> imbalansRPMns;

    private void sortAndAddMeterErrorsResult(List<Meter> meters) {

        List<Meter.CommandResult> commandResultList;
        String[] id;

        for (int i = 0; i < meters.size(); i++) {

            commandResultList = meters.get(i).getErrorListAPPls();
            //Добавление точек результата из AP+
            if (!commandResultList.isEmpty()) {
                for (Meter.CommandResult result : commandResultList) {

                    if (result instanceof Meter.ErrorResult) {
                        id = result.getId().split(";");

                        //Если с окна влияния
                        if (id.length == 7) {
                            //Если АБС
                            if (!id[2].equals("H")) {
                                addElementsInInfABC(inflABCAPPls, result, i);
                            } else {
                                addElementsInInfl(inflAPPls, result, i);
                            }

                        //Если не с окна влияния
                        } else if (id.length == 6) {
                            //Если АБС
                            if (!id[1].equals("H")) {
                                addElementsInABC(ABCAPPls, result, i);
                            } else {
                                addElementsInTotalError(totalErrorAPPls, result, i);
                            }
                        }

                    } else if (result instanceof Meter.ImbUResult) {
                        addElementsImb(imbalansAPPls, result, i);
                    }
                }
            }

            commandResultList = meters.get(i).getErrorListAPMns();
            //Добавление точек результата из AP-
            if (!commandResultList.isEmpty()) {
                for (Meter.CommandResult result : commandResultList) {

                    if (result instanceof Meter.ErrorResult) {
                        id = result.getId().split(";");

                        //Если с окна влияния
                        if (id.length == 7) {
                            //Если АБС
                            if (!id[2].equals("H")) {
                                addElementsInInfABC(inflABCAPMns, result, i);
                            } else {
                                addElementsInInfl(inflAPMns, result, i);
                            }

                            //Если не с окна влияния
                        } else if (id.length == 6) {
                            //Если АБС
                            if (!id[1].equals("H")) {
                                addElementsInABC(ABCAPMns, result, i);
                            } else {
                                addElementsInTotalError(totalErrorAPMns, result, i);
                            }
                        }

                    } else if (result instanceof Meter.ImbUResult) {
                        addElementsImb(imbalansAPMns, result, i);
                    }
                }
            }

            commandResultList = meters.get(i).getErrorListRPPls();
            //Добавление точек результата из RP+
            if (!commandResultList.isEmpty()) {
                for (Meter.CommandResult result : commandResultList) {

                    if (result instanceof Meter.ErrorResult) {
                        id = result.getId().split(";");

                        //Если с окна влияния
                        if (id.length == 7) {
                            //Если АБС
                            if (!id[2].equals("H")) {
                                addElementsInInfABC(inflABCRPPls, result, i);
                            } else {
                                addElementsInInfl(inflRPPls, result, i);
                            }

                            //Если не с окна влияния
                        } else if (id.length == 6) {
                            //Если АБС
                            if (!id[1].equals("H")) {
                                addElementsInABC(ABCRPPls, result, i);
                            } else {
                                addElementsInTotalError(totalErrorRPPls, result, i);
                            }
                        }

                    } else if (result instanceof Meter.ImbUResult) {
                        addElementsImb(imbalansRPPls, result, i);
                    }
                }
            }

            commandResultList = meters.get(i).getErrorListRPMns();
            //Добавление точек результата из RP-
            if (!commandResultList.isEmpty()) {
                for (Meter.CommandResult result : commandResultList) {

                    if (result instanceof Meter.ErrorResult) {
                        id = result.getId().split(";");

                        //Если с окна влияния
                        if (id.length == 7) {
                            //Если АБС
                            if (!id[2].equals("H")) {
                                addElementsInInfABC(inflABCRPMns, result, i);
                            } else {
                                addElementsInInfl(inflRPMns, result, i);
                            }

                            //Если не с окна влияния
                        } else if (id.length == 6) {
                            //Если АБС
                            if (!id[1].equals("H")) {
                                addElementsInABC(ABCRPMns, result, i);
                            } else {
                                addElementsInTotalError(totalErrorRPMns, result, i);
                            }
                        }

                    } else if (result instanceof Meter.ImbUResult) {
                        addElementsImb(imbalansRPMns, result, i);
                    }
                }
            }
        }

        //Добавление прочих результатов
        for (int i = 0; i < meters.size(); i++) {
            addElementsInCRPSTA(meters.get(i), i);
        }
    }

    private void addElementsInInfABC(Map<String, Map<Integer, Meter.CommandResult>> errorBlock, Meter.CommandResult result, int indexMeter) {
        //55.0 F;1;A;A;P;0.5 Ib;0.25L
        String[] idArr = result.getId().split(";");
        String[] procUorFandUorF = idArr[0].split(" ");
        String[] ImaxIb = idArr[5].split(" ");

        String key;
        //F;55;A;L;0,5;Imax;0.02
        if (idArr[6].contains("L") || idArr[6].contains("C")) {
            key = procUorFandUorF[1] + ";" + procUorFandUorF[0] + ";" + idArr[2] + ";" + idArr[6].substring(idArr[6].length() - 1) + ";"
                    + idArr[6].substring(0, idArr[6].length() - 1) + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        } else {
            key = procUorFandUorF[1] + ";" + procUorFandUorF[0] + ";" + idArr[2] + ";0;"
                    + idArr[6] + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        }

        if (errorBlock.get(key) != null) {
            errorBlock.get(key).put(indexMeter, result);
        } else {
            Map<Integer, Meter.CommandResult> errMap = new HashMap<>();
            errMap.put(indexMeter, result);
            errorBlock.put(key, errMap);
        }
    }

    private void addElementsInInfl(Map<String, Map<Integer, Meter.CommandResult>> errorBlock, Meter.CommandResult result, int indexMeter) {
        //55.0 F;1;H;A;P;0.5 Ib;0.25L
        String[] idArr = result.getId().split(";");
        String[] procUorFandUorF = idArr[0].split(" ");
        String[] ImaxIb = idArr[5].split(" ");

        String key;
        //F;55;L;0.5;Imax;0.02
        if (idArr[6].contains("L") || idArr[6].contains("C")) {
            key = procUorFandUorF[1] + ";" + procUorFandUorF[0] + ";" + idArr[6].substring(idArr[6].length() - 1) + ";"
                    + idArr[6].substring(0, idArr[6].length() - 1) + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        } else {
            key = procUorFandUorF[1] + ";" + procUorFandUorF[0] + ";0;"
                    + idArr[6] + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        }

        if (errorBlock.get(key) != null) {
            errorBlock.get(key).put(indexMeter, result);
        } else {
            Map<Integer, Meter.CommandResult> errMap = new HashMap<>();
            errMap.put(indexMeter, result);
            errorBlock.put(key, errMap);
        }
    }

    private void addElementsInABC(Map<String, Map<Integer, Meter.CommandResult>> errorBlock, Meter.CommandResult result, int indexMeter) {
        //1;A;A;P;0.2 Ib;0.5C
        String[] idArr = result.getId().split(";");
        String[] ImaxIb = idArr[4].split(" ");

        String key;
        //A;L;0.5;Imax;0.02
        if (idArr[5].contains("L") || idArr[5].contains("C")) {
            key = idArr[1] + ";" + idArr[5].substring(idArr[5].length() - 1) + ";"
                    + idArr[5].substring(0, idArr[5].length() - 1) + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        } else {
            key = idArr[1] + ";0;" + idArr[5] + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        }

        if (errorBlock.get(key) != null) {
            errorBlock.get(key).put(indexMeter, result);
        } else {
            Map<Integer, Meter.CommandResult> errMap = new HashMap<>();
            errMap.put(indexMeter, result);
            errorBlock.put(key, errMap);
        }
    }

    private void addElementsInTotalError(Map<String, Map<Integer, Meter.CommandResult>> errorBlock, Meter.CommandResult result, int indexMeter) {
        //1;H;A;P;0.2 Ib;0.5C
        String[] idArr = result.getId().split(";");
        String[] ImaxIb = idArr[4].split(" ");

        String key;
        //L;0,5;Imax;0.02
        if (idArr[5].contains("L") || idArr[5].contains("C")) {
            key = idArr[5].substring(idArr[5].length() - 1) + ";"
                    + idArr[5].substring(0, idArr[5].length() - 1) + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        } else {
            key = "0;" + idArr[5] + ";" + ImaxIb[1] + ";" + ImaxIb[0];
        }

        if (errorBlock.get(key) != null) {
            errorBlock.get(key).put(indexMeter, result);
        } else {
            Map<Integer, Meter.CommandResult> errMap = new HashMap<>();
            errMap.put(indexMeter, result);
            errorBlock.put(key, errMap);
        }
    }

    private void addElementsImb(Map<String, Map<Integer, Meter.CommandResult>> errorBlock, Meter.CommandResult result, int indexMeter) {
        String[] id = result.getId().split(";");
        String key = id[1];

        if (errorBlock.get(key) != null) {
            errorBlock.get(key).put(indexMeter, result);
        } else {
            Map<Integer, Meter.CommandResult> errMap = new HashMap<>();
            errMap.put(indexMeter, result);
            errorBlock.put(key, errMap);
        }
    }

    private void addElementsInCRPSTA(Meter meter, int indexMeter) {
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

        if (meter.getCreepTest() != null) {
            CRPSTAother.get("CRP").put(indexMeter, meter.getCreepTest());
        }

        if (meter.getStartTestAPPls() != null) {
            CRPSTAother.get("STAAP").put(indexMeter, meter.getStartTestAPPls());
        }
        if (meter.getStartTestAPMns() != null) {
            CRPSTAother.get("STAAN").put(indexMeter, meter.getStartTestAPMns());
        }
        if (meter.getStartTestRPPls() != null) {
            CRPSTAother.get("STARP").put(indexMeter, meter.getStartTestRPPls());
        }
        if (meter.getStartTestRPMns() != null) {
            CRPSTAother.get("STARN").put(indexMeter, meter.getStartTestRPMns());
        }

        if (meter.getRTCTest() != null) {
            CRPSTAother.get("RTC").put(indexMeter, meter.getRTCTest());
        }

        if (meter.getConstantTestAPPls() != null) {
            CRPSTAother.get("CNTAP").put(indexMeter, meter.getConstantTestAPPls());
        }
        if (meter.getConstantTestAPMns() != null) {
            CRPSTAother.get("CNTAN").put(indexMeter, meter.getConstantTestAPMns());
        }
        if (meter.getConstantTestRPPls() != null) {
            CRPSTAother.get("CNTRP").put(indexMeter, meter.getConstantTestRPPls());
        }
        if (meter.getConstantTestRPMns() != null) {
            CRPSTAother.get("CNTRN").put(indexMeter, meter.getConstantTestRPMns());
        }

        if (meter.getInsulationTest() != null) {
            CRPSTAother.get("INS").put(indexMeter, meter.getInsulationTest());
        }
        if (meter.getAppearensTest() != null) {
            CRPSTAother.get("APR").put(indexMeter, meter.getAppearensTest());
        }
    }

    public void createExcelReport() {
        Workbook wb = new HSSFWorkbook();
        Sheet mainSheet = wb.createSheet("Результат");

        Meter testMeter = new Meter();
        testMeter.setUn(230);
        testMeter.setIb(5);
        testMeter.setImax(60);
        testMeter.setFn(50);
        testMeter.setAccuracyClassAP(1);
        testMeter.setAccuracyClassRP(2);
        testMeter.setConstantMeterAP("3200");
        testMeter.setConstantMeterAP("3200");
        testMeter.setTemperature(24);
        testMeter.setHumidity(64);
        testMeter.setFactoryManufacturer("Тайпит ИП");
        testMeter.setVerificationDate(new Date().toString());
        testMeter.setController("Чубака");
        testMeter.setOperator("Дарт Вейдер");
        testMeter.setWitness("Принцесса Лея");
        testMeter.setModelMeter("НЕВА 303 1S0");
        testMeter.setTestMode("3P4W");

        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();

        createHeadInformation(wb, mainSheet, testMeter, stendDLLCommands);

        try (OutputStream outputStream = new FileOutputStream("C:\\Users\\bert1\\Desktop\\test.xls")){
            wb.write(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createHeadInformation(Workbook wb, Sheet sheet, Meter meter, StendDLLCommands stendDLLCommands) {
        final String MAIN_REPORT_NAME = "ПРОТОКОЛ ПОВЕРКИ СЧЁТЧИКОВ " + meter.getModelMeter();

        final String FACILITY = "Поверочная установка";
        final String TXTA5 = "Наименование:";
        final String TXTA6 = "Серийный номер:";
        final String TXTA7 = "Образцовый счетчик:";
        final String TXTG5 = "Класс точности:";
        final String TXTG6 = "Свидетельсто о поверке:";
        final String TXTM5 = "Дата поверки:";
        final String TXTM6 = "Дата след. поверки:";


        Font Bauhaus_15_Bold = createFontStyle(wb, 15, "Calisto MT", true, false, false);
        Font Calibri_11_BoldItalic = createFontStyle(wb, 11, "Calibri", true, true, false);
        Font Bauhaus_12_Bold = createFontStyle(wb, 12, "Bauhaus 93", true, false, false);
        Font Calibri_11_Bold = createFontStyle(wb, 11, "Calibri", true, false, false);

        CellStyle leftCenter = ExcelReport.createCellStyle(wb, HorizontalAlignment.LEFT, VerticalAlignment.CENTER);
        CellStyle centerCenter = ExcelReport.createCellStyle(wb, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);

        CellRangeAddress headRegion = CellRangeAddress.valueOf("A1:Z3");
        CellStyle cellStyle = ExcelReport.createCellStyle(wb, HorizontalAlignment.CENTER, VerticalAlignment.CENTER);
        cellStyle.setFont(Bauhaus_15_Bold);
        Cell headCell = sheet.createRow(0).createCell(0);
        headCell.setCellStyle(cellStyle);
        headCell.setCellValue(MAIN_REPORT_NAME);
        sheet.addMergedRegion(headRegion);


        CellRangeAddress region1 = CellRangeAddress.valueOf("A4:R4");
        leftCenter.setFont(Bauhaus_12_Bold);
        Cell A4 = sheet.createRow(3).createCell(0);
        A4.setCellStyle(leftCenter);
        A4.setCellValue(FACILITY);
        RegionUtil.setBorderTop(BorderStyle.DASH_DOT, region1, sheet);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, region1, sheet);
        sheet.addMergedRegion(region1);

        CellRangeAddress A5C5 = CellRangeAddress.valueOf("A5:C5");
        Cell A5 = sheet.createRow(4).createCell(0);
        leftCenter.setFont(Calibri_11_BoldItalic);
        A5.setCellStyle(leftCenter);
        A5.setCellValue(TXTA5);
        RegionUtil.setBorderBottom(BorderStyle.THIN, A5C5, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, A5C5, sheet);
        sheet.addMergedRegion(A5C5);

        CellRangeAddress A6C6 = CellRangeAddress.valueOf("A6:C6");
        Cell A6 = sheet.createRow(5).createCell(0);
        A6.setCellStyle(leftCenter);
        A6.setCellValue(TXTA6);
        RegionUtil.setBorderBottom(BorderStyle.THIN, A6C6, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, A6C6, sheet);
        sheet.addMergedRegion(A6C6);

        CellRangeAddress A7C7 = CellRangeAddress.valueOf("A7:C7");
        Cell A7 = sheet.createRow(6).createCell(0);
        A7.setCellStyle(leftCenter);
        A7.setCellValue(TXTA7);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, A7C7, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, A7C7, sheet);
        sheet.addMergedRegion(A7C7);

        CellRangeAddress D5F5 = CellRangeAddress.valueOf("D5:F5");
        Cell D5 = sheet.getRow(4).createCell(3);
        D5.setCellValue(stendDLLCommands.getStendModel());
        centerCenter.setFont(Calibri_11_Bold);
        D5.setCellStyle(centerCenter);
        RegionUtil.setBorderBottom(BorderStyle.THIN, D5F5, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, D5F5, sheet);
        sheet.addMergedRegion(D5F5);

        CellRangeAddress D6F6 = CellRangeAddress.valueOf("D6:F6");
        Cell D6 = sheet.getRow(5).createCell(3);
        D6.setCellValue(stendDLLCommands.getSerNo());
        D6.setCellStyle(centerCenter);
        RegionUtil.setBorderBottom(BorderStyle.THIN, D6F6, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, D6F6, sheet);
        sheet.addMergedRegion(D6F6);

        CellRangeAddress D7F7 = CellRangeAddress.valueOf("D7:F7");
        Cell D7 = sheet.getRow(6).createCell(3);
        D7.setCellValue(stendDLLCommands.getTypeReferenceMeter());
        D7.setCellStyle(centerCenter);
        RegionUtil.setBorderBottom(BorderStyle.MEDIUM, D7F7, sheet);
        RegionUtil.setBorderRight(BorderStyle.MEDIUM, D7F7, sheet);
        sheet.addMergedRegion(D7F7);

        CellRangeAddress G5I5 = CellRangeAddress.valueOf("G5:I5");
        Cell G5 = sheet.getRow(4).createCell(6);
        G5.setCellStyle(leftCenter);
        G5.setCellValue(TXTG5);
        RegionUtil.setBorderBottom(BorderStyle.THIN, G5I5, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, G5I5, sheet);
        sheet.addMergedRegion(G5I5);

        CellRangeAddress G6I6 = CellRangeAddress.valueOf("G6:I6");
        Cell G6 = sheet.getRow(5).createCell(6);
        G6.setCellStyle(leftCenter);
        G6.setCellValue(TXTG6);
        RegionUtil.setBorderBottom(BorderStyle.THIN, G6I6, sheet);
        RegionUtil.setBorderRight(BorderStyle.THIN, G6I6, sheet);
        sheet.addMergedRegion(G6I6);

        //Тут я додумался создать функцию
        createMergeZone("J5:L5", sheet.getRow(4).createCell(9), stendDLLCommands.getStendAccuracyClass(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("J6:L6", sheet.getRow(5).createCell(9), stendDLLCommands.getCertificate(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("M5:O5", sheet.getRow(4).createCell(12), TXTM5, leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("M6:O6", sheet.getRow(5).createCell(12), TXTM6, leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("P5:R5", sheet.getRow(4).createCell(15), stendDLLCommands.getDateLastVerification(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("P6:R6", sheet.getRow(5).createCell(15), stendDLLCommands.getDateNextVerification(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("G7:R7", sheet.getRow(6).createCell(6), "", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.MEDIUM, BorderStyle.MEDIUM);

        createMergeZone("A8:R8", sheet.createRow(7).createCell(0), "", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.DASH_DOT);

        createMergeZone("A9:R9", sheet.createRow(8).createCell(0), "Счётчик", leftCenter,
                Bauhaus_12_Bold, sheet, BorderStyle.MEDIUM);

        sheet.createRow(9);
        sheet.createRow(10);
        sheet.createRow(11);
        sheet.createRow(12);

        createMergeZone("A10:C10", sheet.getRow(9).createCell(0), "Номинальное напряжение:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("A11:C11", sheet.getRow(10).createCell(0), "Базовый ток:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("A12:C12", sheet.getRow(11).createCell(0), "Максимальный ток:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("A13:C13", sheet.getRow(12).createCell(0), "Частота:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.MEDIUM, BorderStyle.THIN);

        createMergeZone("D10:F10", sheet.getRow(9).createCell(3), meter.getUn() + " В", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("D11:F11", sheet.getRow(10).createCell(3), meter.getIb() + " А", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("D12:F12", sheet.getRow(11).createCell(3), meter.getImax() + " А", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("D13:F13", sheet.getRow(12).createCell(3), meter.getFn() + " Гц", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.MEDIUM, BorderStyle.MEDIUM);

        createMergeZone("G10:I10", sheet.getRow(9).createCell(6), "Класс точности AP:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("G11:I11", sheet.getRow(10).createCell(6), "Класс точности RP:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("J10:L10", sheet.getRow(9).createCell(9), String.valueOf(meter.getAccuracyClassAP()), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("J11:L11", sheet.getRow(10).createCell(9), String.valueOf(meter.getAccuracyClassRP()), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("M10:O10", sheet.getRow(9).createCell(12), "Постоянная AP:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("M11:O11", sheet.getRow(10).createCell(12), "Постоянная RP:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("P10:R10", sheet.getRow(9).createCell(15), meter.getConstantMeterAP(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("P11:R11", sheet.getRow(10).createCell(15), meter.getConstantMeterAP(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("G12:R13", sheet.getRow(11).createCell(6), "", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.MEDIUM, BorderStyle.MEDIUM, BorderStyle.MEDIUM);

        createMergeZone("A14:R14", sheet.createRow(13).createCell(0), "", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.DASH_DOT);

        createMergeZone("A15:R15", sheet.createRow(14).createCell(0), "Общее", leftCenter,
                Bauhaus_12_Bold, sheet, BorderStyle.MEDIUM);

        sheet.createRow(15);
        sheet.createRow(16);
        sheet.createRow(17);

        createMergeZone("A16:C16", sheet.getRow(15).createCell(0), "Температура:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("A17:C17", sheet.getRow(16).createCell(0), "Влажность:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("D16:F16", sheet.getRow(15).createCell(3), meter.getTemperature() + " ℃", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("D17:F17", sheet.getRow(16).createCell(3), String.valueOf(meter.getHumidity()), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("A18:C18", sheet.getRow(17).createCell(0), "Режим испытаний:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.MEDIUM, BorderStyle.THIN);

        createMergeZone("D18:F18", sheet.getRow(17).createCell(3), meter.getTestMode(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.MEDIUM, BorderStyle.MEDIUM);

        createMergeZone("G16:I16", sheet.getRow(15).createCell(6), "Компания производитель:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("G17:I17", sheet.getRow(16).createCell(6), "Дата:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("J16:L16", sheet.getRow(15).createCell(9), meter.getFactoryManufacturer(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("J17:L17", sheet.getRow(16).createCell(9), meter.getVerificationDate(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("G18:L18", sheet.getRow(17).createCell(9), "", centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.MEDIUM, BorderStyle.MEDIUM);

        createMergeZone("M16:O16", sheet.getRow(15).createCell(12), "Поверитель:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("M17:O17", sheet.getRow(16).createCell(12), "Оператор:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.THIN, BorderStyle.THIN);

        createMergeZone("M18:O18", sheet.getRow(17).createCell(12), "Контролёр:", leftCenter,
                Calibri_11_BoldItalic, sheet, BorderStyle.MEDIUM, BorderStyle.THIN);

        createMergeZone("P16:R16", sheet.getRow(15).createCell(15), meter.getWitness(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("P17:R17", sheet.getRow(16).createCell(15), meter.getOperator(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.THIN, BorderStyle.MEDIUM);

        createMergeZone("P18:R18", sheet.getRow(17).createCell(15), meter.getController(), centerCenter,
                Calibri_11_Bold, sheet, BorderStyle.MEDIUM, BorderStyle.MEDIUM);
    }

    private void createMergeZone(String address, Cell cell, String cellText, CellStyle style, Font font, Sheet sheet,
                                 BorderStyle bottom, BorderStyle right, BorderStyle left, BorderStyle top) {
        CellRangeAddress cellAddresses = CellRangeAddress.valueOf(address);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(cellText);
        RegionUtil.setBorderBottom(bottom, cellAddresses, sheet);
        RegionUtil.setBorderRight(right, cellAddresses, sheet);
        RegionUtil.setBorderLeft(left, cellAddresses, sheet);
        RegionUtil.setBorderTop(top, cellAddresses, sheet);
        sheet.addMergedRegion(cellAddresses);
    }

    private void createMergeZone(String address, Cell cell, String cellText, CellStyle style, Font font, Sheet sheet,
                                 BorderStyle bottom, BorderStyle right, BorderStyle left) {
        CellRangeAddress cellAddresses = CellRangeAddress.valueOf(address);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(cellText);
        RegionUtil.setBorderBottom(bottom, cellAddresses, sheet);
        RegionUtil.setBorderRight(right, cellAddresses, sheet);
        RegionUtil.setBorderLeft(left, cellAddresses, sheet);
        sheet.addMergedRegion(cellAddresses);
    }

    private void createMergeZone(String address, Cell cell, String cellText, CellStyle style, Font font, Sheet sheet,
                                 BorderStyle bottom, BorderStyle right) {
        CellRangeAddress cellAddresses = CellRangeAddress.valueOf(address);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(cellText);
        RegionUtil.setBorderBottom(bottom, cellAddresses, sheet);
        RegionUtil.setBorderRight(right, cellAddresses, sheet);
        sheet.addMergedRegion(cellAddresses);
    }

    private void createMergeZone(String address, Cell cell, String cellText, CellStyle style, Font font, Sheet sheet,
                                 BorderStyle bottom) {
        CellRangeAddress cellAddresses = CellRangeAddress.valueOf(address);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(cellText);
        RegionUtil.setBorderBottom(bottom, cellAddresses, sheet);
        sheet.addMergedRegion(cellAddresses);
    }

    private void createMergeZone(String address, Cell cell, String cellText, CellStyle style, Font font, Sheet sheet) {
        CellRangeAddress cellAddresses = CellRangeAddress.valueOf(address);
        style.setFont(font);
        cell.setCellStyle(style);
        cell.setCellValue(cellText);
        sheet.addMergedRegion(cellAddresses);
    }

    private static Font createFontStyle(Workbook wb, int size, String fontName, boolean bold, boolean setItalic, boolean setStrikeOut) {
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) size);
        font.setFontName(fontName);
        font.setItalic(setItalic);
        font.setStrikeout(setStrikeOut);
        font.setBold(bold);
        return font;
    }

    private static CellStyle createCellStyle(Workbook wb, Font font, HorizontalAlignment halign, VerticalAlignment valign,
                                             BorderStyle borderBottom, BorderStyle borderLeft, BorderStyle borderRight, BorderStyle borderTop,
                                             short borderBottomColor, short borderLeftColor, short borderRightColor, short borderTopColor,
                                             short fillForegroundColor) {

        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setBorderBottom(borderBottom);
        cellStyle.setBorderLeft(borderLeft);
        cellStyle.setBorderRight(borderRight);
        cellStyle.setBorderTop(borderTop);
        cellStyle.setBottomBorderColor(borderBottomColor);
        cellStyle.setLeftBorderColor(borderLeftColor);
        cellStyle.setRightBorderColor(borderRightColor);
        cellStyle.setTopBorderColor(borderTopColor);
        cellStyle.setFillBackgroundColor(fillForegroundColor);
        return cellStyle;
    }


    private static CellStyle createCellStyle(Workbook wb,HorizontalAlignment halign, VerticalAlignment valign,
                                             BorderStyle borderBottom, BorderStyle borderLeft, BorderStyle borderRight, BorderStyle borderTop) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setBorderBottom(borderBottom);
        cellStyle.setBorderLeft(borderLeft);
        cellStyle.setBorderRight(borderRight);
        cellStyle.setBorderTop(borderTop);
        return cellStyle;
    }

    private static CellStyle createCellStyle(Workbook wb,HorizontalAlignment halign, VerticalAlignment valign) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        return cellStyle;
    }

    private static CellStyle createCellStyle(Workbook wb, Font font, HorizontalAlignment halign, VerticalAlignment valign,
                                             BorderStyle borderBottom, BorderStyle borderLeft, BorderStyle borderRight, BorderStyle borderTop) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        cellStyle.setBorderBottom(borderBottom);
        cellStyle.setBorderLeft(borderLeft);
        cellStyle.setBorderRight(borderRight);
        cellStyle.setBorderTop(borderTop);
        return cellStyle;
    }

    private static CellStyle createCellStyle(Workbook wb, Font font, HorizontalAlignment halign, VerticalAlignment valign) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setFont(font);
        cellStyle.setAlignment(halign);
        cellStyle.setVerticalAlignment(valign);
        return cellStyle;
    }

    private static CellStyle createCellStyle(Workbook wb, BorderStyle borderBottom, BorderStyle borderLeft, BorderStyle borderRight, BorderStyle borderTop) {
        CellStyle cellStyle = wb.createCellStyle();
        cellStyle.setBorderBottom(borderBottom);
        cellStyle.setBorderLeft(borderLeft);
        cellStyle.setBorderRight(borderRight);
        cellStyle.setBorderTop(borderTop);
        return cellStyle;
    }

    //Компараторы

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

    Comparator<String> comparatorForInfl = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //F;55;L;0.5;Imax;0.02
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

                    if (arrO1[2].equals("0") && !arrO2[2].equals("0")) {
                        return -1;
                    } else if (!arrO1[2].equals("0") && arrO2[2].equals("0")) {
                        return 1;
                    } else if (arrO1[2].equals("L") && arrO2[2].equals("C")) {
                        return -1;
                    } else if (arrO1[2].equals("C") && arrO2[2].equals("L")) {
                        return 1;
                    } else if ((arrO1[2].equals("0") && arrO2[2].equals("0")) ||
                            (arrO1[2].equals("L") && arrO2[2].equals("L")) ||
                            (arrO1[2].equals("C") && arrO2[2].equals("C"))) {

                        if (Float.parseFloat(arrO1[3]) > Float.parseFloat(arrO2[3])) {
                            return -1;
                        } else if (Float.parseFloat(arrO1[3]) < Float.parseFloat(arrO2[3])) {
                            return 1;
                        } else {
                            if (arrO1[4].equals("Imax") && arrO2[4].equals("Ib")) {
                                return -1;
                            } else if (arrO1[4].equals("Ib") && arrO2[4].equals("Imax")) {
                                return 1;
                            } else if (arrO1[4].equals("Imax") && arrO2[4].equals("Imax")) {
                                if (Float.parseFloat(arrO1[5]) > Float.parseFloat(arrO2[5])) {
                                    return -1;
                                } else if (Float.parseFloat(arrO1[5]) < Float.parseFloat(arrO2[5])) {
                                    return 1;
                                } else return 0;
                            } else if (arrO1[4].equals("Ib") && arrO2[4].equals("Ib")) {
                                if (Float.parseFloat(arrO1[5]) > Float.parseFloat(arrO2[5])) {
                                    return -1;
                                } else if (Float.parseFloat(arrO1[5]) < Float.parseFloat(arrO2[5])) {
                                    return 1;
                                } else return 0;
                            } else return 0;
                        }
                    } else return 0;
                }
            } else return 0;
        }
    };

    Comparator<String> comparatorForABC = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //A;L;0.5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("A") && !arrO2[0].equals("A")) {
                return -1;
            } else if (!arrO1[0].equals("A") && arrO2[0].equals("A")) {
                return 1;
            } else if (arrO1[0].equals("B") && arrO2[0].equals("C")) {
                return -1;
            } else if (arrO1[0].equals("C") && arrO2[0].equals("B")) {
                return 1;
            } else if (arrO1[0].equals("A") && arrO2[0].equals("A") ||
                    arrO1[0].equals("B") && arrO2[0].equals("B") ||
                    arrO1[0].equals("C") && arrO2[0].equals("C")) {

                if (arrO1[1].equals("0") && !arrO2[1].equals("0")) {
                    return -1;
                } else if (!arrO1[1].equals("0") && arrO2[1].equals("0")) {
                    return 1;
                } else if (arrO1[1].equals("L") && arrO2[1].equals("C")) {
                    return -1;
                } else if (arrO1[1].equals("C") && arrO2[1].equals("L")) {
                    return 1;
                } else if ((arrO1[1].equals("0") && arrO2[1].equals("0")) ||
                        (arrO1[1].equals("L") && arrO2[1].equals("L")) ||
                        (arrO1[1].equals("C") && arrO2[1].equals("C"))) {

                    if (Float.parseFloat(arrO1[2]) > Float.parseFloat(arrO2[2])) {
                        return -1;
                    } else if (Float.parseFloat(arrO1[2]) < Float.parseFloat(arrO2[2])) {
                        return 1;
                    } else {
                        if (arrO1[3].equals("Imax") && arrO2[3].equals("Ib")) {
                            return -1;
                        } else if (arrO1[3].equals("Ib") && arrO2[3].equals("Imax")) {
                            return 1;
                        } else if (arrO1[3].equals("Imax") && arrO2[3].equals("Imax")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else return 0;
                        } else if (arrO1[3].equals("Ib") && arrO2[3].equals("Ib")) {
                            if (Float.parseFloat(arrO1[4]) > Float.parseFloat(arrO2[4])) {
                                return -1;
                            } else if (Float.parseFloat(arrO1[4]) < Float.parseFloat(arrO2[4])) {
                                return 1;
                            } else return 0;
                        } else return 0;
                    }
                } else return 0;
            } else return 0;
        }
    };

    Comparator<String> comparatorForTotalError = new Comparator<String>() {
        @Override
        public int compare(String o1, String o2) {
            //L;0,5;Imax;0.02
            String[] arrO1 = o1.split(";");
            String[] arrO2 = o2.split(";");

            if (arrO1[0].equals("0") && !arrO2[0].equals("0")) {
                return -1;
            } else if (!arrO1[0].equals("0") && arrO2[0].equals("0")) {
                return 1;
            } else if (arrO1[0].equals("L") && arrO2[0].equals("C")) {
                return -1;
            } else if (arrO1[0].equals("C") && arrO2[0].equals("L")) {
                return 1;
            } else if ((arrO1[0].equals("0") && arrO2[0].equals("0")) ||
                    (arrO1[0].equals("L") && arrO2[0].equals("L")) ||
                    (arrO1[0].equals("C") && arrO2[0].equals("C"))) {

                if (Float.parseFloat(arrO1[1]) > Float.parseFloat(arrO2[1])) {
                    return -1;
                } else if (Float.parseFloat(arrO1[1]) < Float.parseFloat(arrO2[1])) {
                    return 1;
                } else {
                    if (arrO1[2].equals("Imax") && arrO2[2].equals("Ib")) {
                        return -1;
                    } else if (arrO1[2].equals("Ib") && arrO2[2].equals("Imax")) {
                        return 1;
                    } else if (arrO1[2].equals("Imax") && arrO2[2].equals("Imax")) {
                        if (Float.parseFloat(arrO1[3]) > Float.parseFloat(arrO2[3])) {
                            return -1;
                        } else if (Float.parseFloat(arrO1[3]) < Float.parseFloat(arrO2[3])) {
                            return 1;
                        } else return 0;
                    } else if (arrO1[2].equals("Ib") && arrO2[2].equals("Ib")) {
                        if (Float.parseFloat(arrO1[3]) > Float.parseFloat(arrO2[3])) {
                            return -1;
                        } else if (Float.parseFloat(arrO1[3]) < Float.parseFloat(arrO2[3])) {
                            return 1;
                        } else return 0;
                    } else return 0;
                }
            } else return 0;
        }
    };


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
