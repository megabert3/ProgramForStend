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


            int channelFlag = 0;

            System.out.println(stendDLLCommands.setReviseMode(0));
            System.out.println(stendDLLCommands.setReviseTime(4.5));
//
           //System.out.println(stendDLLCommands.setNoRevise(true));
//
            long time = System.currentTimeMillis();
            stendDLLCommands.getUI(1, 230.0, 80.0, 50.0, 0, 0, 100.0, 100, "H", "1.0");
            System.out.println(System.currentTimeMillis() - time);

            //Ua,Ub,Uc,Ia,Ib,Ic,Angle_UaIa, Angle_UbIb, Angle_UcIc, Pa , Pb , Pc , Qa , Qb , Qc , Sa , Sb , Sc ,
            //Pall(A.P.),Qall(R.P.),Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall, Angle_UaUb, Angle_UbUc

            for (int i = 0; i < 10; i++) {
                String[] meterParam = stendDLLCommands.stMeterRead().split(",");
                System.out.println(Arrays.toString(meterParam));
                Thread.sleep(1000);
            }
//
//        System.out.println(System.currentTimeMillis() - time);
//
//        Thread.sleep(10000);
//
////        for (int i = 1; i <= 3; i++) {
////            stendDLLCommands.setPulseChannel(i, channelFlag);
////        }
////
////        for (int i = 1; i <= 3; i++) {
////            System.out.println(stendDLLCommands.countStart(i));
////        }
////
////        for (int j = 0; j < 50; j++) {
////
////            for (int i = 1; i < 4; i++) {
////                System.out.println("Счётчик: "+ i + "Количество импульсов " + stendDLLCommands.countRead(i));
////            }
////
////            j++;
////            Thread.sleep(500);
////        }
//
            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}