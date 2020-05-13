package org.taipit.stend.controller;

public class MainStend {

    public static void main(String[] args) {
        StendDLLCommands stendDLLCommands = ThreePhaseStend.getThreePhaseStendInstance();
        try {

            stendDLLCommands.errorClear();

            stendDLLCommands.getUI(1, 230, 5, 50, 0, 0, 100, 0, "H", "1.0");

            Thread.sleep(3000);

            for (int i = 1; i < 3; i++) {
                stendDLLCommands.setPulseChannel(i, 0);
                stendDLLCommands.constTestStart(i, 8000);
            }

            stendDLLCommands.getUI(1, 230, 10, 50, 0, 0, 100, 100, "H", "1.0");

            Thread.sleep(60000);

            stendDLLCommands.powerOf();

            for (int i = 1; i < 3; i++) {
                System.out.println(stendDLLCommands.constPulseRead(8000, i));
            }


        }catch (Exception e) {
            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }

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
}
