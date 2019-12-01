package stend.controller;

import com.sun.jna.Memory;

import com.sun.jna.ptr.PointerByReference;
import stend.model.StendDLL;


public class StendDLLCommands {
    private StendDLL stend = StendDLL.INSTANCE;

    //Порт для связи с установкой
    private int port;

    //Тип эталонного счётчика
    private String typeReferenceMeter;
    private boolean threePhaseStend;

    //Кол-во постадочных мест для счётчиков
    private int amountPlaces;

    //Активно ли посадочное место
    private boolean activePlace;

    //Константа счётчика
    private int constant;

    //Колличество повторов измерений
    private int countResult;

    //Пауза для стабилизации и установки заданных пар-ров установки
    private int pauseForStabization;

    //Колличество выбранных мест для испытания
    private boolean[] amountActivePlaces = initializationAmountActivePlaces();


    //Константы счётчиков
    private double[] constantsForMetersOnPlaces = initializationconstantsForMetersOnPlaces();

    private String [] threePhaseStendNames = new String[]{"HY5303C-22", "HS5320", "SY3102", "SY3302", "TC-3000D"};
    private String [] onePhaseStendNames = new String[]{"HY5101C-22", "HY5101C-23", "SY3803", "TC-3000C"};

    public StendDLLCommands(int port, String refMeter) {
        this.port = port;
        this.typeReferenceMeter = refMeter;
    }

    //Инициализирует посадочные места и устанавливает значения флага
    private boolean[] initializationAmountActivePlaces() {
        boolean[] init = new boolean[amountPlaces];
        for (int i = 1; i <= amountPlaces; i++) {
            init[i] = false;
        }
        return init;
    }

    //Активирует или деактивирует посадочное место
    public void setActivePlace(int number, boolean active) {
        amountActivePlaces[number] = active;
    }

    public boolean[] getAmountActivePlaces() {
        return amountActivePlaces;
    }

    //Инициализирует значения констант у посадочных мест
    private double[] initializationconstantsForMetersOnPlaces() {
        double[] init = new double[amountPlaces];
        for (int i = 1; i <= amountPlaces; i++) {
            init[i] = 0.0;
        }
        return init;
    }

    public double[] getConstantsForMetersOnPlaces() {
        return constantsForMetersOnPlaces;
    }

    //Инициализирует значение константы у каждого счётчика
    public void setConstant(int number, double constant) {
        constantsForMetersOnPlaces[number] = constant;
    }

    public int getAmountPlaces() {
        return amountPlaces;
    }

    //Пауза для стабилизации счётчика
    public int getPauseForStabization() {
        return pauseForStabization;
    }

    public int getConstant() {
        return constant;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }

    //Проверяет сколько фаз у стенда
    public void howManyPhase() {

    }


    public boolean isThreePhaseStend() {
        return threePhaseStend;
    }

    //Включить напряжение и ток
    //Test
    public boolean getUI(double curr) {
        return stend.Adjust_UI(1, 230.0, curr, 50.0, 0, 0, 100.0, 100.0, "H", "1.0", typeReferenceMeter, port);
    }
    //Включить напряжение и ток без регулеровки пофазного напряжения
    public boolean getUI(int phase,
                         double ratedVolt,
                         double ratedCurr,
                         double ratedFreq,
                         int phaseSrequence,
                         int revers,
                         double voltPer,
                         double currPer,
                         String iABC,
                         String cosP) {

        return stend.Adjust_UI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP, typeReferenceMeter, port);
    }

    //Включить напряжение и ток с регулировкой пофазного напряжения
    public boolean getUIWithPhase (int phase,
                                   double ratedVolt,
                                   double ratedCurr,
                                   double ratedFreq,
                                   int phaseSrequence,
                                   int revers,
                                   double voltPerA,
                                   double voltPerB,
                                   double voltPerC,
                                   double currPer,
                                   String iABC,
                                   String cosP) {
        return stend.Adjust_UI1(phase,ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP, typeReferenceMeter, port);
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
        PointerByReference pointer = new PointerByReference(new Memory(1024));
        stend.StdMeter_Read(pointer, typeReferenceMeter, port);
        return pointer.getValue().getString(0, "ASCII");
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
    // Старт CRPSTA
    public boolean crpstaStart(int meterNo) {
        return stend.CRPSTA_start(meterNo, port);
    }

    // результат CRPSTA

    public boolean crpstaResult(int meterNo) {
        return stend.CRPSTA_Result(meterNo, port);
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
    // Поиск метки
    public boolean searchMark(int meterNo) {
        return stend.Search_mark(meterNo, port);
    }

    /**
     * Спросить и протестить
     * */
    // результат поиска метки
    public boolean searchMarkResult(int meterNo) {
        return stend.Search_mark_Result(meterNo, port);
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
        PointerByReference pointer = new PointerByReference(new Memory(1024));
        stend.Clock_Error_Read(pointer, freq, errType, meterNo, port);
        return pointer.getValue().getString(0, "ASCII");
    }

    // Закрыть порт
    public String dllPortClose() {
        PointerByReference pointer = new PointerByReference(new Memory(1024));
        stend.Dll_Port_Close(pointer);
        return pointer.getValue().getString(0, "ASCII");
    }

    @Override
    public String toString() {
        return String.valueOf(this);
    }
}
