package org.taipit.stend.controller;

public class MainStend {
    public static boolean interrupt = false;

    public static void main(String[] args) throws InterruptedException {

        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();

        stendDLLCommands.getUI(1, 230, 0.0, 50.0, 0, 0,
                100.0, 0.0, "H", "1.0");

        Thread.sleep(7000);

        for (int i = 1; i < 4; i++) {
            stendDLLCommands.setPulseChannel(i, 4);
            stendDLLCommands.clockErrorStart(i, 1.0, 10);
        }

        while (!interrupt) {

            Thread.sleep(12000);


            System.out.println("Reading");
            for (int i = 1; i < 4; i++) {
                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, i));
            }

            for (int i = 1; i < 4; i++) {
                stendDLLCommands.clockErrorStart(i, 1.0, 10);
            }

        }
    }

}
