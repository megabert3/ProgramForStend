package stend.controller;

import stend.helper.ConsoleHelper;
import stend.model.StendDLL;

import java.io.IOException;

public class StendCommands {
    private int port;
    private StendDLL stend = StendDLL.INSTANCE;

    public StendCommands(int port) {
        this.port = port;
    }


    //Включить напряжение и ток
    public boolean getUI() throws IOException {

        int Phase;
        double Rated_Volt;
        double Rated_Curr;
        double Rated_Freq;
        int PhaseSrequence;
        int Revers;
        double Volt_Per;
        double Curr_Per;
        String IABC;
        String CosP;
        String SModel;

        ConsoleHelper.getMessage("Выберите режим Phase - Режим:\n" +
                "\t\t0 - Однофазный\n" +
                "\t\t1 - Трех-фазный четырех-проводной\n" +
                "\t\t2 - Трех-фазный трех-проводной\n" +
                "\t\t3 - Трех-фазный трех-проводной реактив 90 градусов\n" +
                "\t\t4 - Трех-фазный трех-проводной реактив 60 градусов\n" +
                "\t\t5 - Трех-фазный четырех-проводной (реактив)\n" +
                "\t\t6 - Трех-фазный трех-проводной 9реактив)\n" +
                "\t\t7 - Однофазный реактив");
        Phase = Integer.parseInt(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Введите значение напряжения Rated_Volt - напряжение (double)");
        Rated_Volt = Double.parseDouble(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Введите значение тока Rated_Curr - ток (double)");
        Rated_Curr = Double.parseDouble(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Введите значение частоты Rated_Freq - частота (double)");
        Rated_Freq = Double.parseDouble(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Выберите PhaseSrequence - чередование фаз\n" +
                "\t\t0 - Прямое\n" +
                "\t\t1 - Обратное");
        PhaseSrequence = Integer.parseInt(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Выберете Revers - направление тока\n" +
                "\t\t0 - Прямой\n" +
                "\t\t1 - Обратный");
        Revers = Integer.parseInt(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Выберете Volt_Per - Процент по напряжению (0.0 - 100.0)");
        Volt_Per = Double.parseDouble(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Выберете Curr_Per - Процент по току (0.0 - 100.0)");
        Curr_Per = Double.parseDouble(ConsoleHelper.entString());

        ConsoleHelper.getMessage("Выберете по каким фазам пустить ток: A, B, C, H - все");
        IABC = ConsoleHelper.entString();

        ConsoleHelper.getMessage("Выберите cosф например: \"1.0\", \"0.5L\", \"0.8C\"");
        CosP = ConsoleHelper.entString();

        ConsoleHelper.getMessage("Выберите модель эталонного счётчика:\n" +
                "\t\tHY5303C-22, HS5320, SY3102, SY3302 (3 фазы)\n" +
                "\t\tHY5101C-22, HY5101C-23, SY3803 (1 фаза)\n" +
                "\t\tTC-3000C (1 фаза)\n" +
                "\t\tTC-3000D (3 фазы)");
        SModel = ConsoleHelper.entString();

        return stend.Adjust_UI(Phase, Rated_Volt, Rated_Curr, Rated_Freq, PhaseSrequence, Revers, Volt_Per, Curr_Per, IABC, CosP, SModel, port);
    }

    // Сброс всех ошибок
    public boolean errorClear() {
        return stend.Error_Clear(port);
    }

    // Выключение напряжения и тока (кнопка Стоп)
    public boolean powerOf() {
     return stend.Power_Off(port);
    }
}
