package org.taipit.stend.controller;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;

public class MainStend {

    public static void main(String[] args) {
        String pow = "0,25L";
        String now = pow.substring(0, pow.length() - 1);
        System.out.println(now);

//        File excelFile = new File("C:\\Users\\bert1\\Desktop\\test\\test.xlsx");
//        Workbook wb = new XSSFWorkbook();
//
//        CellStyle style = wb.createCellStyle();
//
//        Sheet testSheet = wb.createSheet("testSheet");
//        Row row = testSheet.createRow(0);
//        Cell cell = row.createCell(1);
//        XSSFFont font = ((XSSFWorkbook) wb).createFont();
//        font.setColor(XSSFFont.COLOR_RED);
//        style.setFont(font);
//        cell.setCellValue("dfsdf");
//        cell.setCellStyle(style);
//
//
//
//
//        try (FileOutputStream fileOutputStream = new FileOutputStream(excelFile)) {
//            wb.write(fileOutputStream);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//
//    }
//
//    private static void createCell(Workbook wb, Row row, int column, HorizontalAlignment halign, VerticalAlignment valign) {
//        Cell cell = row.createCell(column);
//        cell.setCellValue("Align It hjhbn;.jbh");
//        CellStyle cellStyle = wb.createCellStyle();
//        cellStyle.setAlignment(halign);
//        cellStyle.setVerticalAlignment(valign);
//        cell.setCellStyle(cellStyle);
//    }
    }
}
