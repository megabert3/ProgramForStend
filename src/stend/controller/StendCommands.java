package stend.controller;


import com.sun.jna.Memory;
import com.sun.jna.ptr.ByteByReference;

import com.sun.jna.ptr.PointerByReference;
import stend.model.StendDLL;
import java.io.IOException;



public class StendCommands {
    private int port;
    private String typeReferenceMeter;
    private StendDLL stend = StendDLL.INSTANCE;

    public StendCommands(int port, String refMeter) {
        this.port = port;
        this.typeReferenceMeter = refMeter;
    }


    //Включить напряжение и ток
    public boolean getUI() throws IOException {
//        int Phase;
//        double Rated_Volt;
//        double Rated_Curr;
//        double Rated_Freq;
//        int PhaseSrequence;
//        int Revers;
//        double Volt_Per;
//        double Curr_Per;
//        String IABC;
//        String CosP;
//        String SModel;
//
//        ConsoleHelper.getMessage("Выберите режим Phase - Режим:\n" +
//                "\t\t0 - Однофазный\n" +
//                "\t\t1 - Трех-фазный четырех-проводной\n" +
//                "\t\t2 - Трех-фазный трех-проводной\n" +
//                "\t\t3 - Трех-фазный трех-проводной реактив 90 градусов\n" +
//                "\t\t4 - Трех-фазный трех-проводной реактив 60 градусов\n" +
//                "\t\t5 - Трех-фазный четырех-проводной (реактив)\n" +
//                "\t\t6 - Трех-фазный трех-проводной 9реактив)\n" +
//                "\t\t7 - Однофазный реактив");
//        Phase = Integer.parseInt(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Введите значение напряжения Rated_Volt - напряжение (double)");
//        Rated_Volt = Double.parseDouble(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Введите значение тока Rated_Curr - ток (double)");
//        Rated_Curr = Double.parseDouble(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Введите значение частоты Rated_Freq - частота (double)");
//        Rated_Freq = Double.parseDouble(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Выберите PhaseSrequence - чередование фаз\n" +
//                "\t\t0 - Прямое\n" +
//                "\t\t1 - Обратное");
//        PhaseSrequence = Integer.parseInt(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Выберете Revers - направление тока\n" +
//                "\t\t0 - Прямой\n" +
//                "\t\t1 - Обратный");
//        Revers = Integer.parseInt(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Выберете Volt_Per - Процент по напряжению (0.0 - 100.0)");
//        Volt_Per = Double.parseDouble(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Выберете Curr_Per - Процент по току (0.0 - 100.0)");
//        Curr_Per = Double.parseDouble(ConsoleHelper.entString());
//
//        ConsoleHelper.getMessage("Выберете по каким фазам пустить ток: A, B, C, H - все");
//        IABC = ConsoleHelper.entString();
//
//        ConsoleHelper.getMessage("Выберите cosф например: \"1.0\", \"0.5L\", \"0.8C\"");
//        CosP = ConsoleHelper.entString();
//
//        ConsoleHelper.getMessage("Выберите модель эталонного счётчика:\n" +
//                "\t\tHY5303C-22, HS5320, SY3102, SY3302 (3 фазы)\n" +
//                "\t\tHY5101C-22, HY5101C-23, SY3803 (1 фаза)\n" +
//                "\t\tTC-3000C (1 фаза)\n" +
//                "\t\tTC-3000D (3 фазы)");
//        SModel = ConsoleHelper.entString();

        return stend.Adjust_UI(1, 230.0, 5.0, 50.0, 0, 0, 100.0, 100.0, "H", "1.0", typeReferenceMeter, port);
    }

    // Сброс всех ошибок
    public boolean errorClear() {
        return stend.Error_Clear(port);
    }

    // Выключение напряжения и тока (кнопка Стоп)
    public boolean powerOf() {
        return stend.Power_Off(port);
    }

    //Получить данные с эталонного счётчика счетчика
    public String stMeterRead() {
        ByteByReference pointer = new ByteByReference((byte) 256);
        stend.StdMeter_Read(pointer.getPointer(), typeReferenceMeter, port);
        return String.valueOf(pointer.getValue());
    }

    // Получить ошибку навешенного счетчика
    public String meterErrorRead(int meterNo) {
        PointerByReference pointer = new PointerByReference(new Memory(1024));
        stend.Error_Read(pointer, meterNo, port);
        return pointer.getValue().getString(0, "ASCII");
    }

    /**
     * Спросить и протестить
     * */
    // Запустить проверку навешенного счетчика (напряжение и ток должны быть включены)
    public boolean errorStart(int meterNo, double constant, int pulse) {
        return stend.Error_Start(meterNo, constant, pulse, port);
    }

    /**
     * Спросить и протестить
     * */
    // Установка режима импульсов
    public boolean setPulseChannel(int meterNo, int channelFlag) {
        return stend.Set_Pulse_Channel(meterNo, channelFlag, port);
    }

    // Установка режима 485 интерфейса
    public boolean set485Channel(int meterNo, int openFlag) {
        return stend.Set_485_Channel(meterNo, openFlag, port);
    }

    /**
     * Спросить и протестить
     * */
    // Поиск метки
    public boolean searchMark(int meterNo) {
        return stend.Search_mark(meterNo, port);
    }

    /**
     * Спросить и протестить
     * */
    // Старт CRPSTA
    public boolean crpstaStart(int meterNo) {
        return stend.CRPSTA_start(meterNo, port);
    }

    /**
     * Спросить и протестить
     * */
    // Очистка CRPSTA
    public boolean crpstaClear(int meterNo) {
        return stend.CRPSTA_clear(meterNo, port);
    }

    /**
     * Спросить и протестить
     * */
    // результат поиска метки
    public boolean searchMarkResult(int meterNo) {
        return stend.Search_mark_Result(meterNo, port);
    }

    // результат CRPSTA
    public boolean crpstaResult(int meterNo) {
        return stend.CRPSTA_Result(meterNo, port);
    }

    // Выключение нагрузки (тока)
    public boolean powerPause() {
        return stend.Power_Pause(port);
    }

    // Чтение серийного номера.
    // Функция для внутреннего использования. Не предназначена для пользовательского ПО
    public boolean readSerialNumber (String meterSerialNumber, int meterNo) {
        return stend.Read_SerialNumber(meterSerialNumber, meterNo, port);
    }

    // Старт теста констант
    public boolean constTestStart (int meterNo, double constant) {
        return stend.ConstTest_Start(meterNo, constant, port);
    }

    // Чтение данных по энергии
    public boolean constPulseRead (double meterKWH, double stdKWH, double constant, int meterNo) {
        return stend.ConstPulse_Read(meterKWH, stdKWH, constant, meterNo, port);
    }

    // Выбор цепи
    public boolean selectCircuit(int circuit) {
        return stend.SelectCircuit(circuit, port);
    }

    // Отключить нейтраль
    public boolean cutNeutral(int cuttingFlag) {
        return stend.CutNeutral(cuttingFlag, port);
    }

    // Старт теста ТХЧ
    public boolean clockErrorStart(int meterNo, double freq, int duration) {
        return stend.Clock_Error_Start(meterNo, freq, duration, port);
    }

    // Прочитать результаты теста ТХЧ. Должна вызываться по прошествии времени, отведенного на тест
    // + запас в пару секунд
    public String clockErrorRead (double freq, int errType, int meterNo) {
        String meError = new String();
        stend.Clock_Error_Read(/*pointer*/meError, freq, errType, meterNo, port);
        return meError;
    }

    // Закрыть порт
    public String dllPortClose() {
        String close = new String();
        stend.Dll_Port_Close(close);
        return close;
    }

}
