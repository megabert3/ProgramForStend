package stend.controller;

import stend.helper.ConsoleHelper;


public class MainStend {
    public static void main(String[] args) {
        try {
            ConsoleHelper.getMessage("Введите номер порта");
            StendCommands stend = new StendCommands(Integer.parseInt(ConsoleHelper.entString()));

            System.out.println(stend.getUI());

            Thread.sleep(10000);

            stend.errorClear();
            stend.powerOf();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
