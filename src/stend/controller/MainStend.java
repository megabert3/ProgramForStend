package stend.controller;

import stend.helper.ConsoleHelper;

import java.io.IOException;


public class MainStend {
    public static void main(String[] args) throws IOException {
//        //ConsoleHelper.getMessage("HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)\n" +
//                "\t\tHY5101C-22, HY5101C-23, SY3803 (1 фаза)\n"+
//                "\t\tTC-3000C (1 фаза интегрированный)\n" +
//                "\t\tTC-3000D (3 фазы интегрированный)");
        //String refMetName = ConsoleHelper.entString();
        StendCommands stend = new StendCommands(9, "HY5303C-22");
        try {
            stend.errorClear();
            stend.getUI();
            Thread.sleep(2500);
            stend.errorStart(2, 8000.0, 7);
            Thread.sleep(10000);

            ConsoleHelper.getMessage(stend.meterErrorRead(2));
            ConsoleHelper.getMessage(stend.stMeterRead());

        }catch (Exception e){
            e.printStackTrace();

        }finally {
            stend.powerOf();
        }
    }
}
