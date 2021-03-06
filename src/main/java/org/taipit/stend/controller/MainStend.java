package org.taipit.stend.controller;

import org.taipit.stend.helper.exeptions.StendConnectionException;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;

/**
 * Тестовый класс
 */
public class MainStend {

    public static void main(String[] args) throws InterruptedException {
        try {
            long time = System.currentTimeMillis();

            StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    stendDLLCommands.getUI(1, 230.0, 5.0, 50.0, 0, 0, 100.0, 100.0, "H", "1.0");
                    System.out.println("2 " + (System.currentTimeMillis() - time));
                } catch (StendConnectionException stendConnectionException) {
                    System.out.println("dsgsdgs");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();

            stendDLLCommands.getUI(1, 230.0, 5.0, 50.0, 0, 0, 100.0, 100.0, "H", "1.0");
            System.out.println((System.currentTimeMillis()) - time);


            Thread.sleep(3000);

            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}