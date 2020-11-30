package org.taipit.stend.model.stend;

import com.sun.jna.Memory;

import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import jssc.SerialPortList;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.StendConnectionException;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;


public abstract class StendDLLCommands {
    private StendDLL stend = StendDLL.INSTANCE;

    private Properties properties = ConsoleHelper.properties;

    //Порт для связи с установкой
    private int port;

    private String stendModel = properties.getProperty("stendModel");

    //Класс точности
    private String stendAccuracyClass = properties.getProperty("stendAccuracyClass");

    //Серийный номер
    private String serNo = properties.getProperty("stendSerNo");

    //Свидетельство о поверке
    private String certificate = properties.getProperty("param.certificate");

    //Дата последней поверки
    private String dateLastVerification = properties.getProperty("param.dateLastVerification");

    //Дата следующей поверки
    private String dateNextVerification = properties.getProperty("param.dateNextVerification");

    //Тип эталонного счётчика
    private String typeReferenceMeter = properties.getProperty("refMeterModel");

    //Кол-во постадочных мест для счётчиков
    private int amountPlaces;

    //Пауза для стабилизации и установки заданных пар-ров установки
    private int pauseForStabization;

    //Количество (активных мест)
    private boolean[] amountActivePlaces = initializationAmountActivePlaces();

    //Номинальное напряжение
    private double Unom;

    //Максимальный ток
    private double Imax;

    //Базовый ток
    private double Ib;

    //Номинальная частота
    private double Fn;

    //Константа счётчика
    private double constant;

    //Необходимо для быстрого обхода в цикле
    private HashMap<Integer, Meter> amountActivePlacesForTest; // = initAmountActivePlacesForTest();

    public StendDLLCommands() {
        String port = ConsoleHelper.properties.getProperty("stendCOMPort").trim().substring(3).trim();
        this.port = Integer.parseInt(port);
        this.amountPlaces = Integer.parseInt(ConsoleHelper.properties.getProperty("stendAmountPlaces"));
        this.pauseForStabization = Integer.parseInt(ConsoleHelper.properties.getProperty("pauseForStabization"));
    }

    //Инициализирует посадочные места и устанавливает значения флага
    private boolean[] initializationAmountActivePlaces() {
        boolean[] init = new boolean[amountPlaces + 1];
        for (int i = 1; i <= amountPlaces; i++) {
            init[i] = true;
        }
        return init;
    }

    //Оставляет только места необходимые для теста
    private HashMap<Integer, Meter> initAmountActivePlacesForTest() {
        HashMap<Integer, Meter> init = new HashMap<>();
        for (int i = 1; i < amountActivePlaces.length; i++) {
            if (amountActivePlaces[i]) {
                init.put(i, new Meter());
            }
        }
        return init;
    }

    //Активирует или деактивирует посадочное место
    public void setActivePlace(int number, boolean active) {
        amountActivePlaces[number] = active;
        amountActivePlacesForTest = initAmountActivePlacesForTest();
    }

    /**
     * //Устанавливает значение импульсного выхода у установки для каждого метса
     * @param meterList
     * Лист со счётчиками, которые необходимо испытать
     * @param channelFlag
     * //0 - активная энергия в прямом направлении тока
     * //1 - активная энергия в обратном направлении тока
     * //2 - реактивная энергия в прямом направлении тока
     * //3 - реактивная энергия в обратном направлении тока
     *
     * @throws StendConnectionException
     * @throws InterruptedException
     */

    public synchronized void setEnergyPulse (List<Meter> meterList, int channelFlag) throws StendConnectionException, InterruptedException {
        for (Meter meter : meterList) {
            int i = 0;
            boolean setEnergy = setPulseChannel(meter.getId(), channelFlag);

            if (!setEnergy) {
                while (2 > i) {
                    if (setPulseChannel(meter.getId(), channelFlag)) {
                        return;
                    } else {
                        Thread.sleep(10);
                        i++;
                    }
                }
                throw new StendConnectionException("Не удалось записать \"Set_Pulse_Channel\" месту: " + meter.getId());
            }
        }
    }

    public boolean setRefClock(int setFlag) {
        return stend.SetRefClock(setFlag, port);
    }

    public HashMap<Integer, Meter> getAmountActivePlacesForTest() {
        return amountActivePlacesForTest;
    }

    public int getAmountPlaces() {
        return amountPlaces;
    }

    public double getConstant() {
        return constant;
    }

    public double getUnom() {
        return Unom;
    }

    public double getImax() {
        return Imax;
    }

    public double getIb() {
        return Ib;
    }

    public double getFn() {
        return Fn;
    }

    public void setImax(double imax) {
        Imax = imax;
    }

    public void setUnom(double unom) {
        Unom = unom;
    }

    public void setIb(double ib) {
        Ib = ib;
    }

    public void setStend(StendDLL stend) {
        this.stend = stend;
    }

    public void setFn(double fn) {
        Fn = fn;
    }

    public void setConstant(double constant) {
        this.constant = constant;
    }

    //Пауза для стабилизации счётчика
    public int getPauseForStabization() {
        return pauseForStabization;
    }

    public void setPauseForStabization(int pauseForStabization) {
        this.pauseForStabization = pauseForStabization;
    }

    //Включить напряжение и ток без регулеровки пофазного напряжения
    public void getUI(int phase,
                         double ratedVolt,
                         double ratedCurr,
                         double ratedFreq,
                         int phaseSrequence,
                         int revers,
                         double voltPer,
                         double currPer,
                         String iABC,
                         String cosP) throws StendConnectionException {
        /**test*/
//        System.out.println("Треад " + Thread.currentThread().getName());
//        System.out.println("Вошёл в getUI");
        boolean b = stend.Adjust_UI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP, typeReferenceMeter, port);

//        System.out.println("Треад " + Thread.currentThread().getName());
//        System.out.println("Вышел из getUI");

        if (!b) {
            throw new StendConnectionException("Не удалось подать мощность: Adjust_UI");
        }
    }

    //Включить напряжение и ток с регулировкой пофазного напряжения
    public void getUIWithPhase (int phase,
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
                                   String cosP) throws StendConnectionException {

        boolean b = stend.Adjust_UI1(phase,ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP, typeReferenceMeter, port);
        if (b) {
            throw new StendConnectionException("Не удалось подать мощность: Adjust_U_WithPhase");
        }
    }

    // Сброс всех ошибок
    public void errorClear() throws StendConnectionException {
        int i = 0;
        boolean b = stend.Error_Clear(port);

        if (!b) {
            while (i < 2) {
                if (stend.Error_Clear(port)) {
                    return;
                } else {
                    i++;
                    try {
                        Thread.sleep(100);
                    }catch (InterruptedException e) {
                        return;
                    }
                }
            }
            throw new StendConnectionException("Не удалось очистить погрешность Error_Clear");
        }
    }

    // Выключение напряжения и тока (кнопка Стоп)
    public void powerOf() throws StendConnectionException {
        if (!stend.Power_Off(port)) {
            throw new StendConnectionException("Не удалось выключить пощность");
        }
    }

    //Получить данные с эталонного счётчика счетчика
    public synchronized String stMeterRead() {
        PointerByReference pointer = new PointerByReference(new Memory(1024));

        boolean b = stend.StdMeter_Read(pointer, typeReferenceMeter, port);

        if (!b) {
            System.out.println("Не удалось считать значения эталонного счётчика");
        }

        return pointer.getValue().getString(0, "ASCII");
    }

    public String stMeterRead(String typeReferenceMeter) {
        PointerByReference pointer = new PointerByReference(new Memory(1024));
        stend.StdMeter_Read(pointer, typeReferenceMeter, port);
        return pointer.getValue().getString(0, "ASCII");
    }

    // Получить ошибку навешенного счетчика
    public String meterErrorRead(int meterNo) {
        PointerByReference pointer = new PointerByReference(new Memory(1024));

        boolean b = stend.Error_Read(pointer, meterNo, port);

        if (!b) {
            System.out.println("Не удалось считать погрешность Error_Read");
        }

        return pointer.getValue().getString(0, "ASCII");
    }

    //Сказать константу счётчика стенду для кажого места
    public boolean setMetersConstantToStend(List<Meter> metersList, int constant, int amountImpulse) throws StendConnectionException {
        for (Meter meter : metersList) {
            if (!errorStart(meter.getId(), constant, amountImpulse)) throw new StendConnectionException("не удалось записать команду Error_Start");
        }
        return true;
    }

    // Запустить проверку навешенного счетчика (напряжение и ток должны быть включены)
    public boolean errorStart(int meterNo, double constant, int pulse) {
        return stend.Error_Start(meterNo, constant, pulse, port);
    }


    // Установка режима импульсов
    public boolean setPulseChannel(int meterNo, int channelFlag) throws StendConnectionException {
        return stend.Set_Pulse_Channel(meterNo, channelFlag, port);
    }

    // Установка режима 485 интерфейса
    public boolean set485Channel(int meterNo, int openFlag) {
        return stend.Set_485_Channel(meterNo, openFlag, port);
    }

    // Старт CRPSTA
    public void crpstaStart(int meterNo) throws StendConnectionException {
        boolean b = stend.CRPSTA_start(meterNo, port);
        if (!b) {
            throw new StendConnectionException("Не удалось записать команду CRPSTA_start");
        }
    }

    // результат CRPSTA
    public void crpstaResult(int meterNo) throws StendConnectionException {
        boolean b = stend.CRPSTA_Result(meterNo, port);
        if (!b) {
            throw new StendConnectionException("Не удалось записть команду CRPSTA_Result");
        }
    }

    // Очистка CRPSTA
    public void crpstaClear(int meterNo) throws StendConnectionException {
        boolean b = stend.CRPSTA_clear(meterNo, port);
        if (!b) {
            throw new StendConnectionException("Не удалось записть команду CRPSTA_clear");
        }
    }

    // Поиск метки
    public void searchMark(int meterNo) throws StendConnectionException {
        if (!stend.Search_mark(meterNo, port)) {
            throw new StendConnectionException("Не удалось записать команду Search_mark");
        }
    }

    // результат поиска метки
    public boolean searchMarkResult(int meterNo) {
        return stend.Search_mark_Result(meterNo, port);
    }

    // Выключение нагрузки (тока)
    public void powerPause() throws StendConnectionException {
        boolean b = stend.Power_Pause(port);
        if (!b) {
            throw new StendConnectionException("Не удалось записать команду Power_Pause");
        }
    }

    // Чтение серийного номера.
    // Функция для внутреннего использования. Не предназначена для пользовательского ПО
    public boolean readSerialNumber (String meterSerialNumber, int meterNo) {
        return stend.Read_SerialNumber(meterSerialNumber, meterNo, port);
    }

    // Старт теста констант
    public void constTestStart (int meterNo, double constant) throws InterruptedException, StendConnectionException {
        int i = 0;
        boolean b = stend.ConstTest_Start(meterNo, constant, port);

        if (!b) {
            while (i < 2) {
                if (stend.ConstTest_Start(meterNo, constant, port)) {
                    return;
                } else {
                    i++;
                    Thread.sleep(10);
                }
            }
            throw new StendConnectionException("Не удалось записать \"ConstTest_Start\" месту: " + meterNo);
        }
    }


    // Чтение данных по энергии
    public double constProcRead(double constant, int meterNo) throws StendConnectionException {
        DoubleByReference pointerMeterKWH = new DoubleByReference();
        DoubleByReference pointerStdKWH = new DoubleByReference();

        if (!stend.ConstPulse_Read(pointerMeterKWH, pointerStdKWH, constant, meterNo, port)) {
            throw new StendConnectionException("Не удалось подать команду ConstPulse_Read");
        }

        Double meterKWH = pointerMeterKWH.getValue();
        Double stdKWH = pointerStdKWH.getValue();

        return new BigDecimal(((meterKWH - stdKWH) / stdKWH) * 100)
                .setScale(2, RoundingMode.HALF_UP).doubleValue();
    }

    // Чтение данных по энергии
    public String constStdEnergyRead(double constant, int meterNo) throws StendConnectionException {
        DoubleByReference pointerMeterKWH = new DoubleByReference();
        DoubleByReference pointerStdKWH = new DoubleByReference();

        if (!stend.ConstPulse_Read(pointerMeterKWH, pointerStdKWH, constant, meterNo, port)) {
            throw new StendConnectionException("Не удалось подать команду ConstPulse_Read");
        }

        String bigDecimalMeterKWH = new BigDecimal(pointerMeterKWH.getValue()).setScale(5, RoundingMode.HALF_UP).toString();
        String bigDecimalStdKWH = new BigDecimal(pointerStdKWH.getValue()).setScale(5, RoundingMode.HALF_UP).toString();

        return bigDecimalMeterKWH + "," + bigDecimalStdKWH;
    }

    // Выбор цепи
    public void selectCircuit(int circuit) throws StendConnectionException {
        if (!stend.SelectCircuit(circuit, port)) throw new StendConnectionException("Не удалось переключить цепь SelectCircuit");
    }

    // Отключить нейтраль
    public void cutNeutral(int cuttingFlag) throws StendConnectionException {
        if (!stend.CutNeutral(cuttingFlag, port)) {
            throw new StendConnectionException("Не удалось записать команду cutNeutral");
        }
    }

    // Старт теста ТХЧ
    public void clockErrorStart(int meterNo, double freq, int duration) throws StendConnectionException {
        if (!stend.Clock_Error_Start(meterNo, freq, duration, port)) {
            throw new StendConnectionException("Не удалось записать команду clockErrorStart");
        }
    }

    // Прочитать результаты теста ТХЧ. Должна вызываться по прошествии времени, отведенного на тест
    // + запас в пару секунд
    public String clockErrorRead (double freq, int errType, int meterNo) {
        PointerByReference pointer = new PointerByReference(new Memory(1024));
        stend.Clock_Error_Read(pointer, freq, errType, meterNo, port);
        return pointer.getValue().getString(0, "ASCII");
    }

    // Закрыть порт
    public void dllPortClose() {
        stend.Dll_Port_Close();
    }

    public void countStart(int meterNo) throws StendConnectionException {
        if (!stend.Count_Start(meterNo, port)) {
            throw new StendConnectionException("Не удалось записать команду Count_Start");
        }
    }

    public int countRead(int meterNo) throws StendConnectionException {
        IntByReference pointer = new IntByReference();
        if (!stend.Count_Read(pointer, meterNo, port)) {
            throw new StendConnectionException("Не удалось записать команду Count_Read");
        }
        return pointer.getValue();
    }

//    public boolean countRead(int pulse,int meterNo) {
//        boolean b = stend.Count_Read(pulse, meterNo, port);
//        System.out.println(b);
//        return b;
//    }

    public void setReviseMode(int mode) throws StendConnectionException {
        if (!stend.Set_ReviseMode(mode)) {
            throw new StendConnectionException("Не удалось записать команду Set_ReviseMode");
        }
    }

    public void setReviseTime(double timeSek) throws StendConnectionException {
        if (!stend.Set_ReviseTime(timeSek)) {
            throw new StendConnectionException("Не удалось записать команду Set_ReviseTime");
        }
    }

    public void setNoRevise(boolean b) throws StendConnectionException {
        if (!stend.Set_NoRevise(b)) {
            throw new StendConnectionException("Не удалось записать команду Set_NoRevise");
        }
    }

    //Список портов
    public static String[] massPort() {
        return SerialPortList.getPortNames();
    }

    public String getTypeReferenceMeter() {
        return typeReferenceMeter;
    }

    public String getStendAccuracyClass() {
        return stendAccuracyClass;
    }

    public String getSerNo() {
        return serNo;
    }

    public String getCertificate() {
        return certificate;
    }

    public String getDateLastVerification() {
        return dateLastVerification;
    }

    public String getDateNextVerification() {
        return dateNextVerification;
    }

    public String getStendModel() {
        return stendModel;
    }

}
