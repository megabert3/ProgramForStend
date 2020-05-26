package org.taipit.stend.controller;

import java.util.concurrent.TimeUnit;

public class MainStend {

    public static String getTime (long time) {
    return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
            TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
            TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }

    public static void main(String[] args) throws InterruptedException {

        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        stendDLLCommands.errorClear();

        stendDLLCommands.getUI(1, 230.0, 5, 50, 0, 0, 100, 0.4, "H", "1.0");
        System.out.println(stendDLLCommands.stMeterRead());
        Thread.sleep(3000);
        System.out.println(stendDLLCommands.stMeterRead());
        stendDLLCommands.setPulseChannel(1, 0);

        stendDLLCommands.crpstaStart(1);

        boolean result;

        long timeStart = System.currentTimeMillis();
        long time = timeStart + 15000;

        while (System.currentTimeMillis() < time) {
            result = stendDLLCommands.crpstaResult(1);
            System.out.println(stendDLLCommands.stMeterRead());
            System.out.println(getTime(System.currentTimeMillis() - timeStart));
            System.out.println(result);

            if (result) {
                System.out.println("Обновляю");
                stendDLLCommands.crpstaClear(1);
                stendDLLCommands.crpstaStart(1);
            }

            Thread.sleep(200);
        }

        stendDLLCommands.errorClear();
        stendDLLCommands.powerOf();
    }



//        String time = "104:60:60";
//
//        String[] timeArr = time.split(":");
//
//        long timeMls = (Integer.parseInt(timeArr[0]) * 3600 + Integer.parseInt(timeArr[1]) * 60 + Integer.parseInt(timeArr[2])) * 1000;
//
//        System.out.println(time);
//        System.out.println(timeMls);
//        System.out.println(getTime(timeMls));


//        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
//        try {
//
//            stendDLLCommands.errorClear();
//
//            stendDLLCommands.getUI(1, 230, 5, 50, 0, 0, 100, 0, "H", "1.0");
//
//            Thread.sleep(3000);
//
//            for (int i = 1; i < 3; i++) {
//                stendDLLCommands.setPulseChannel(i, 0);
//                stendDLLCommands.constTestStart(i, 8000);
//            }
//
//            stendDLLCommands.getUI(1, 230, 10, 50, 0, 0, 100, 100, "H", "1.0");
//
//            Thread.sleep(60000);
//
//            stendDLLCommands.powerOf();
//
//            for (int i = 1; i < 3; i++) {
//                System.out.println(stendDLLCommands.constProcRead(8000, i));
//            }
//
//
//        }catch (Exception e) {
//            stendDLLCommands.errorClear();
//            stendDLLCommands.powerOf();
//        }

// Старт теста констант
// Meter_No - номер места
// Constant - постоянная
// Dev_Port - номер com-порта

//        boolean ConstTest_Start(int Meter_No,
//        double Constant,
//        int Dev_Port);
//
////--------------------------------------------------------------------------- ConstPulse_Read
//// Чтение данных по энергии
//// MeterKWH - значения счетчика
//// StdKWH - Значение эталонника
//// Constant - постоянная
//// Meter_No - номер места
//// Dev_Port - номер com-порта

//        boolean ConstPulse_Read(String MeterKWH,
//                String StdKWH,
//        double Constant,
//        int Meter_No,
//        int Dev_Port);

}
