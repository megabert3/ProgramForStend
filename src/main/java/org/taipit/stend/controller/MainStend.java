package org.taipit.stend.controller;

import javafx.scene.control.TableColumn;

import java.util.Arrays;

public class MainStend {

    public static void main(String[] args) throws InterruptedException {

////        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
////
////        stendDLLCommands.getUI(1, 230, 0.0, 50.0, 0, 0,
////                100.0, 0.0, "H", "1.0");
////
////        Thread.sleep(7000);
////
////        for (int i = 1; i < 4; i++) {
////            stendDLLCommands.setPulseChannel(i, 4);
////            stendDLLCommands.clockErrorStart(i, 1.0, 10);
////        }
////
////        while (!interrupt) {
////
////            Thread.sleep(12000);
////
////
////            System.out.println("Reading");
////            for (int i = 1; i < 4; i++) {
////                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, i));
////            }
////
////            for (int i = 1; i < 4; i++) {
////                stendDLLCommands.clockErrorStart(i, 1.0, 10);
////            }
//        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
//
//        stendDLLCommands.getUI(1, 230, 1.0, 50.0, 0, 0,
//                100.0, 100.0, "H", "1.0");
//
//        Thread.sleep(3000);
//
//        for (int i = 0; i < 4; i++) {
//            stendDLLCommands.setPulseChannel(i, 0);
//            stendDLLCommands.errorStart(i, 8000, 5);
//        }
//
//        while (true) {
//
//            for (int i = 1; i < 4; i++) {
//                String strError = stendDLLCommands.meterErrorRead(i);
//                System.out.println(strError);
//                String []strMass = strError.split(",");
//
//                System.out.println(Arrays.toString(strMass));
////                strMass = strError.split(",");
////                resultNo = Integer.parseInt(strMass[0]);
////                error = strMass[1];
//            }
//            Thread.sleep(1000);
//        }

    }

}
