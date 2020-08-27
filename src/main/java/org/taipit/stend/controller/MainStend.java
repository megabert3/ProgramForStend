package org.taipit.stend.controller;

import org.taipit.stend.controller.viewController.errorFrame.refMeter.ThreePhaseStendRefParamController;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;

import java.util.Arrays;

public class MainStend {

    public static void main(String[] args) throws InterruptedException {
        try {

            StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();

            ThreePhaseStendRefParamController stendRefParametersForFrame = new ThreePhaseStendRefParamController();

            stendDLLCommands.selectCircuit(1);

            int channelFlag = 1;

            stendDLLCommands.setReviseMode(1);

            stendDLLCommands.getUIWithPhase(1, 230.0, 5.0, 50.0, 0, 0, 0.0, 100.0, 0.0, 100.0, "B",
                    "1.0");

            //stendDLLCommands.getUI(5, 230.0, 60, 50.0, 0, 0, 100.0, 0, "H", "1.0");

            for (int i = 1; i < 4; i++) {
                stendDLLCommands.setPulseChannel(i, channelFlag);
            }

            for (int i = 0; i < 7; i++) {

                for (int j = 1; j < 4; j++) {
                    System.out.println(stendDLLCommands.stMeterRead());
                }

                Thread.sleep(1000);
            }

            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}