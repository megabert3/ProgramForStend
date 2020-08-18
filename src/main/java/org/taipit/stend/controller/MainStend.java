package org.taipit.stend.controller;

import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import sun.awt.windows.ThemeReader;

import java.math.BigDecimal;
import java.util.Locale;


public class MainStend {

    public static void main(String[] args) throws InterruptedException {

        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();

        int channelFlag = 0;

        stendDLLCommands.setReviseMode(1);

        stendDLLCommands.getUIWithPhase(1, 230, 5, 50, 0, 0,
                100, 100, 100, 100, "H", "1.0");

        Thread.sleep(5000);

        for (int i = 1; i <= 3; i++) {
            stendDLLCommands.setPulseChannel(i, channelFlag);
        }

        for (int i = 1; i <= 3; i++) {
            System.out.println(stendDLLCommands.countStart(i));
        }

        for (int j = 0; j < 50; j++) {

            for (int i = 1; i < 4; i++) {
                System.out.println("Счётчик: "+ i + "Количество импульсов " + stendDLLCommands.countRead(i));
            }

            j++;
            Thread.sleep(500);
        }

        stendDLLCommands.errorClear();
        stendDLLCommands.powerOf();
    }
}