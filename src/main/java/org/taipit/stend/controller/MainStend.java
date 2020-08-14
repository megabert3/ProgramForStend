package org.taipit.stend.controller;

import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import sun.awt.windows.ThemeReader;

import java.math.BigDecimal;
import java.util.Locale;


public class MainStend {

    public static void main(String[] args) throws InterruptedException {

        double freg = 1.0000014;
        System.out.println(String.format(Locale.ROOT, "%.7f", freg));

//        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
//
//        int channelFlag = 1;
//
//        stendDLLCommands.setReviseMode(1);
//
//        stendDLLCommands.getUIWithPhase(1, 230, 0, 0, 0, 0,
//                100, 100, 100, 0, "H", "1.0");
//
//        Thread.sleep(5000);
//
//        if (!stendDLLCommands.setRefClock(1)) {
//            System.out.println("Нет блока ТХЧ");
//        }
//
//        for (int i = 1; i <= 3; i++) {
//            stendDLLCommands.setPulseChannel(i, channelFlag);
//        }
//
//        for (int i = 1; i <= 3; i++) {
//            stendDLLCommands.clockErrorStart(i, 1, 10);
//        }
//
//        int i = 0;
//
//        while (i < 160) {
//
//            for (int j = 1; j <= 3; j++) {
//                System.out.println(stendDLLCommands.clockErrorRead(1, 1, j));
//            }
//
//            i++;
//            Thread.sleep(300);
//        }
//
//        stendDLLCommands.errorClear();
//        stendDLLCommands.powerOf();
    }
}