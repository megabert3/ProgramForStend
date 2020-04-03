package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class ErrorCommand implements Commands, Serializable {

    private StendDLLCommands stendDLLCommands;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Следующая команда ErrorCommand?
    private boolean nextCommand;

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

    public ErrorCommand(String id, int phase, String current, int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
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
        } else {
            name = (iABC + ": " + cosP + "; " + currentPerсent + " " + current);
        }

        currPer = Double.parseDouble(currentPerсent) * 100;
        phaseSrequence = 0;
        voltPer = 100.0;
    }

    //Конструктор для создания объекта с вклатки "Влияние"
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

    //===================================================================================================
    //Команда выполнения для последовательного теста
    @Override
    public boolean execute() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                return false;
            }
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

            for (Meter meter : meterForTestList) {
                meter.setAmountMeasur(0);
                meter.setErrorResultChange(0);
                meter.returnResultCommand(index, channelFlag).setLastResult("N");
            }

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                return false;
            }

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return false;
            }

            //Устанавливаем местам импульсный выход
            stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода ErrorCommand");
                return false;
            }

            //Сказать константу счётчика стенду для кажого места
            stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

            //Для быстрой становки флага прошёл счётчик тест или нет
            Meter.CommandResult resultMeter;

            int resultNo;
            String strError;
            String[] strMass;
            String error;

            //Для сравнения
            double doubleErr;

            while (flagInStop.containsValue(false)) {

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды ErrorCommand из внешнего цикла перед опросом счётчиков");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода ErrorCommand");
                    return false;
                }

                for (Meter meter : meterForTestList) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды ErrorCommand из внутреннего цикла перед опросом счётчикА");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода ErrorCommand");
                        return false;
                    }

                    strError = stendDLLCommands.meterErrorRead(meter.getId());
                    strMass = strError.split(",");

                    if (strMass.length != 2) {
                        continue;
                    }

                    resultNo = Integer.parseInt(strMass[0]);
                    error = strMass[1];

                    if (resultNo != 0) {
                        resultMeter = meter.setResultsInErrorList(index, resultNo, error, channelFlag);
                        doubleErr = Double.parseDouble(error);

                        if (doubleErr > emax || doubleErr < emin) {
                            resultMeter.setLastResult("F" + error);
                            resultMeter.setPassTest(false);
                        } else {
                            resultMeter.setLastResult("P" + error);
                            resultMeter.setPassTest(true);
                        }

                        if (meter.getErrorResultChange() != resultNo) {
                            meter.setAmountMeasur(meter.getAmountMeasur() + 1);
                            meter.setErrorResultChange(resultNo);
                        }
                    }

                    if (meter.getAmountMeasur() >= countResult) {
                        flagInStop.put(meter.getId(), true);
                    }
                }
            }

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

            if (!Thread.currentThread().isInterrupted() && nextCommand) return true;

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Поймал ошибку Interrupted в команде ErrorCommand");
            System.out.println("Состояние нити до команты interrupt в команде ErrorCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
            return false;
        }

        return !Thread.currentThread().isInterrupted();
    }

    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                return;
            }

            //Выбор константы в зависимости от энергии
            if (channelFlag == 0 || channelFlag == 1) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
            } else {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
            }

            for (Meter meter : meterForTestList) {
                meter.returnResultCommand(index, channelFlag).setLastResult("N");
            }

            if (current.equals("Ib")) {
                ratedCurr = Ib;
            } else {
                ratedCurr = Imax;
            }

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            //Устанавливаем местам импульсный выход
            stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды ErrorCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            //Сказать константу счётчика стенду для кажого места
            stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

            //Для быстрой становки флага прошёл счётчик тест или нет
            Meter.CommandResult resultMeter;

            int resultNo;
            String strError;
            String[] strMass;
            String error;
            double doubleErr;

            while (!Thread.currentThread().isInterrupted()) {


                for (Meter meter : meterForTestList) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды ErrorCommand из внутреннего цикла перед опросом счётчикА");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода ErrorCommand");
                        return;
                    }

                    strError = stendDLLCommands.meterErrorRead(meter.getId());
                    strMass = strError.split(",");

                    if (strMass.length != 2) {
                        continue;
                    }

                    resultNo = Integer.parseInt(strMass[0]);
                    error = strMass[1];

                    if (resultNo != 0) {
                        resultMeter = meter.setResultsInErrorList(index, resultNo, error, channelFlag);
                        doubleErr = Double.parseDouble(error);

                        if (doubleErr > emax || doubleErr < emin) {
                            resultMeter.setLastResult("F" + error);
                            resultMeter.setLastResulString(error);
                            resultMeter.setPassTest(false);
                        } else {
                            resultMeter.setLastResult("P" + error);
                            resultMeter.setLastResulString(error);
                            resultMeter.setPassTest(true);
                        }
                    }
                }
                Thread.sleep(300);
            }

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Поймал ошибку Interrupted в команде ErrorCommand");
            System.out.println("Состояние нити до команты interrupt в команде ErrorCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
        }
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

    public void setCountResult(String countResult) {
        this.countResult = Integer.parseInt(countResult);
    }

    public String getCountResult() {
        return String.valueOf(countResult);
    }

    public void setNextCommand(boolean nextCommand) {
        this.nextCommand = nextCommand;
    }

    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public String toString() {
        if (iABC.equals("H")) {
            return (cosP + "; " + currentPerсent + " "  + current);
        } else return (iABC + ": " + cosP + "; " + currentPerсent + " "  + current);
    }
}