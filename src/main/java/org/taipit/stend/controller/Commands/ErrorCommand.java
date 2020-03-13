package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


public class ErrorCommand implements Commands, Serializable {



    private StendDLLCommands stendDLLCommands;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Следующая команда ErrorCommand?
    private boolean nextCommandError;

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
    public void execute() throws InterruptedTestException, ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

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
            }

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
            //Устанавливаем местам импульсный выход
            stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
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

                for (Meter meter : meterForTestList) {
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

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
            if (nextCommandError) return;

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        }catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
        }
    }

    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

            //Выбор константы в зависимости от энергии
            if (channelFlag == 0 || channelFlag == 1) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
            } else {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
            }

            if (current.equals("Ib")) {
                ratedCurr = Ib;
            } else {
                ratedCurr = Imax;
            }

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
            //Устанавливаем местам импульсный выход
            stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

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
                    }
                }

                Thread.sleep(300);
            }

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        }catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
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

    public void setNextCommandError(boolean nextCommandError) {
        this.nextCommandError = nextCommandError;
    }

    @Override
    public String toString() {
        if (iABC.equals("H")) {
            return (cosP + "; " + currentPerсent + " "  + current);
        } else return (iABC + ": " + cosP + "; " + currentPerсent + " "  + current);
    }
}
