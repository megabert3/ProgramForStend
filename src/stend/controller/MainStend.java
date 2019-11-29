package stend.controller;

import java.io.IOException;


public class MainStend {
    public static void main(String[] args) throws IOException, InterruptedException {
//        //ConsoleHelper.getMessage("HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)\n" +
//                "\t\tHY5101C-22, HY5101C-23, SY3803 (1 фаза)\n"+
//                "\t\tTC-3000C (1 фаза интегрированный)\n" +
//                "\t\tTC-3000D (3 фазы интегрированный)");
        //String refMetName = ConsoleHelper.entString();
        StendDLLCommands stend = new StendDLLCommands(9, "HY5303C-22");
        try {
            stend.getUI(5.0);
            stend.setPulseChannel(1, 0);
            Thread.sleep(4000);
            stend.errorStart(1, 8000, 5);
            Thread.sleep(20000);
            stend.meterErrorRead(1);
//            stend.getUI(0.0);
//            String exit = "";
//            while (!(exit.equalsIgnoreCase("exit"))){
//                stend.errorClear();
//                ConsoleHelper.getMessage("Коилчество импульсов");
//                int impl = Integer.parseInt(ConsoleHelper.entString());
//                ConsoleHelper.getMessage("Пауза");
//                int pause =1000 * Integer.parseInt(ConsoleHelper.entString());
//                ConsoleHelper.getMessage("Тип ошибки");
//                int type = Integer.parseInt(ConsoleHelper.entString());
//                stend.clockErrorStart(2, 1.0, impl);
//                Thread.sleep(pause);
//                ConsoleHelper.getMessage(stend.clockErrorRead(1.0, type, 2));
//                ConsoleHelper.getMessage("Выйти?");
//                exit = ConsoleHelper.entString();
            //}
            //ConsoleHelper.getMessage(String.valueOf(stend.crpstaResult(2)));
            //stend.crpstaClear(2);
            //ConsoleHelper.getMessage(String.valueOf(stend.searchMarkResult(2)));
            //stend.errorStart(2, 8000.0, 7);
            //Thread.sleep(10000);
            //ConsoleHelper.getMessage(stend.meterErrorRead(2));
            //stend.getUI(10.0);
            //stend.errorStart(2, 8000.0, 7);
            //Thread.sleep(3500);
            //ConsoleHelper.getMessage(stend.meterErrorRead(2));
            //ConsoleHelper.getMessage(stend.stMeterRead());

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            stend.errorClear();
            stend.powerOf();
        }
    }
}
