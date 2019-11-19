package stend.controller;

import stend.helper.ConsoleHelper;


public class MainStend {
    public static void main(String[] args) {
        try {
            ConsoleHelper.getMessage("Введите номер порта");
            int portNumb = Integer.parseInt(ConsoleHelper.entString());
            ConsoleHelper.getMessage("Введите тип эталонного счётчика\n" +
                    "HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)\n" +
                    "HY5101C-22, HY5101C-23, SY3803 (1 фаза)\n" +
                    "TC-3000C (1 фаза)\n" +
                    "TC-3000D (3 фазы)");
            String refMetName = ConsoleHelper.entString();
            StendCommands stend = new StendCommands(portNumb, refMetName);

            System.out.println(stend.getUI());

            Thread.sleep(10000);

            stend.errorClear();
            stend.powerOf();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
