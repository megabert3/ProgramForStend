package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;
import stend.helper.exeptions.ConnectForStendExeption;
import stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class ErrorCommand implements Commands, Serializable {

// Включить напряжение и ток
// Phase - Режим:
// 		0 - Однофазный
//		1 - Трех-фазный четырех-проводной
//		2 - Трех-фазный трех-проводной
// 		3 - Трех-фазный трех-проводной реактив 90 градусов
// 		4 - Трех-фазный трех-проводной реактив 60 градусов
//		5 - Трех-фазный четырех-проводной (реактив)
//		6 - Трех-фазный трех-проводной (реактив)
//		7 - Однофазный реактив
// Rated_Volt - напряжение
// Rated_Curr - ток
// Rated_Freq - частота
// PhaseSrequence - чередование фаз
//		0 - Прямое
//		1 - Обратное
// Revers - направление тока
//		0 - Прямой
//		1 - Обратный
// Volt_Per - Процент по напряжению (0 - 100)
// Curr_Per - Процент по току (0 - 100)
// IABC - строка, определяющая фазы, по которым пустить ток: A, B, C, H - все
// CosP - строка  с косинусом угла. Например: "1.0", "0.5L", "0.8C"
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза)
//		TC-3000D (3 фазы)
// Dev_Port - номер com-порта

    private StendDLLCommands stendDLLCommands;

    //Необходим для быстрого доступа к Объекту класса Error
    private int index;

    //Команда для прерывания метода
    private boolean interrupt;

    //Лист с счётчиками для испытания
    private List<Meter> meterForTestList;

    //Максимальный порог ошибки
    private double emin = -1.0;

    //Минимальный порог ошибки
    private double emax = 1.0;

    //Кол-во импульсов для расчёта ошибки
    private int pulse = 20;

    //Имя точки для отображения в таблице
    private String name;

    //Базовый или максимальный ток из конструктора
    private String current;

    //Стринговый процент получаемый из конструктора
    private String currentPerсent;

    //id кнопки
    private String id;

    //Базовый ток
    private double Ib;

    //Максимальный ток
    private double Imax;

    //Режим
    private int phase;

    //Напряжение
    private double ratedVolt;

    //Процент от напряжения
    private double voltPer;

    //Ток
    private double ratedCurr;

    //Процен от тока
    private double currPer;

    //Частота
    private double ratedFreq;

    //Коэфициент мощности
    private String cosP;

    //Необходимо сделать в доп тестовом окне
    private int phaseSrequence;

    //Направление тока
    private int revers;

    //По каким фазам пустить ток
    private String iABC;

    //Активная ли точка
    private boolean active = true;

    //Импульсный выход
    private int channelFlag;

    //Количество повторов теста
    private int countResult = 2;

    //Константа счётчика для теста
    private int constantMeter;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop;

        public ErrorCommand(String id, int phase, String current,
                        int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currentPerсent = currentPercent;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;

        if (iABC.equals("H")) {
            name = (cosP + "; " + currentPerсent + " " + current.trim());
        } else name = (iABC + ": " + cosP + "; " + currentPerсent + " " + current);

        currPer = Double.parseDouble(currentPerсent) * 100;
        phaseSrequence = 0;
        voltPer = 100.0;
    }

    public ErrorCommand(String param, String id, int phase, String current,
                        double voltPer, int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currentPerсent = currentPercent;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
        this.voltPer = voltPer;

        //47.0%Un: 0.5L; 0.01 Ib
        name = (voltPer + "%" + param + "n: " + cosP + "; " + currentPerсent + " " + current.trim());

        currPer = Double.parseDouble(currentPerсent) * 100;
        phaseSrequence = 0;
    }

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void execute() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw new InterruptedTestException();

        //Выбор константы в зависимости от энергии
        if (channelFlag == 0 || channelFlag == 1) {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
        } else {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
        }

        flagInStop = initBoolList();

        if (current.equals("Ib")) {
            ratedCurr = Ib;
        } else {
           ratedCurr = Imax;
        }

        if (interrupt) throw new InterruptedTestException();

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        if (interrupt) throw new InterruptedTestException();
        //Сказать константу счётчика стенду для кажого места
        stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

        if (interrupt) throw new InterruptedTestException();
        /**
         * Разобраться с flagInStop.
         */
        while (flagInStop.containsValue(false)) {

                if (interrupt) throw new InterruptedTestException();
                int resultNo;
                String strError;
                String[] strMass;
                String error;

                for (Meter meter : meterForTestList) {
                    strError = stendDLLCommands.meterErrorRead(meter.getId());
                    System.out.println(meter.getId() + "  " + strError);
                    strMass = strError.split(",");
                    resultNo = Integer.parseInt(strMass[0]);
                    error = strMass[1];

                    meter.addLastErrorInErrorList(index, error, channelFlag);
                    meter.getLocalErrors()[resultNo] = error;

                    meter.setAmountMeasur(meter.getAmountMeasur() + 1);

                    if (meter.getAmountMeasur() >= countResult) {
                        flagInStop.put(meter.getId(), true);
                    }
                }
        }

        for (Meter meter : meterForTestList) {
            meter.addLocalErrorInMain(index, channelFlag);
            meter.setAmountMeasur(0);
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    private void viewParam() {
//        //Процент от напряжения
//        private double voltPer;
//
//        //Ток
//        private double ratedCurr;
//
//        //Процен от тока
//        private double currPer;
//
//        //Частота
//        private double ratedFreq;
//
//        //Коэфициент мощности
//        private String cosP;
//
//        //Необходимо сделать в доп тестовом окне
//        private int phaseSrequence;
//
//        //Направление тока
//        private int revers;
//
//        //По каким фазам пустить ток
//        private String iABC;
//
//        //Активная ли точка
//        private boolean active = true;
//
//        //Импульсный выход
//        private int channelFlag;
//
//        //Количество повторов теста
//        private int countResult = 2;
//
//        //Константа счётчика для теста
//        private int constantMeter;
        System.out.println("Минимально допустимая ошибка " + emin);
        System.out.println("Максимально допустимая ошибка " + emax);
        System.out.println("Кол-во импульсов для расчёта ошибки "+ pulse);
        System.out.println("Имя точки для отображения в таблице " + name);
        System.out.println("Базовый или максимальный ток из конструктора " + current);
        System.out.println("Стринговый процент получаемый из конструктора " + currentPerсent);
        System.out.println("Базовый ток " + Ib);
        System.out.println("Максимальный ток " + Imax);
        System.out.println("Напряжение " + ratedVolt);
        System.out.println("Процент от напряжения " + voltPer);
        System.out.println("Процент от тока " + currPer);
        System.out.println("Частота " + ratedFreq);
        System.out.println("Коэф мощности " + cosP);
        System.out.println("Ток " + ratedCurr);
    }

    //Опрашивает счётчики до нужно значения проходов
    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterForTestList.size());

        for (Meter meter : meterForTestList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }

    public void setPulse(String pulse) {
        this.pulse = Integer.parseInt(pulse);
    }

    public void setEmin(String emin) {
        this.emin = Double.parseDouble(emin);
    }

    public void setEmax(String emax) {
        this.emax = Double.parseDouble(emax);
    }

    public String getName() {
        return name;
    }

    public String getEmin() {
        return String.valueOf(emin);
    }

    public String getEmax() {
        return String.valueOf(emax);
    }

    public String getPulse() {
        return String.valueOf(pulse);
    }

    public void setImax(double imax) {
        Imax = imax;
    }

    public void setIb(double ib) {
        Ib = ib;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMeterForTestList(List<Meter> meterForTestList) {
        this.meterForTestList = meterForTestList;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public String toString() {
        if (iABC.equals("H")) {
            return (cosP + "; " + currentPerсent + " "  + current);
        } else return (iABC + ": " + cosP + "; " + currentPerсent + " "  + current);
    }
}
