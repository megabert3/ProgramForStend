package org.taipit.stend.controller;

import com.sun.jna.WString;
import org.taipit.stend.model.stend.StendDLL;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;

public class MainStend {

    public static void main(String[] args) throws InterruptedException {

        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();


        int channelFlag = 0;

        stendDLLCommands.setReviseMode(1);
//
        System.out.println(stendDLLCommands.setNoRevise(true));

        //System.out.println(stendDLLCommands.setReviseTime(-15));
//
//        long time = System.currentTimeMillis();
        StendDLL stend = StendDLL.INSTANCE;

        System.out.println(stend.Error_Clear(9));

        stend.Adjust_UI(1, 230.0, 5.0, 50.0, 0, 0,100.0, 100.0, "H", "1.0", "HY5303C-22", 9);
//
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
    }
}