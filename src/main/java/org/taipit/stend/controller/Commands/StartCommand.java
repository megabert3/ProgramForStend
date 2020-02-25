package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

public class StartCommand implements Commands, Serializable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private StendDLLCommands stendDLLCommands;

    private List<Meter> meterList;

    //Команда для прерывания метода
    private boolean interrupt;

    //Выставлять в зависимости от выбранного параметра перед тестом
    private int phase;

    //Номинальное напряжение
    private double ratedVolt;

    //Номинальный ток
    private double ratedCurr;

    //Номинальная частота
    private double ratedFreq;

    private int revers;

    //Возможно пригодится
    private double currPer;

    //Возможно пригодится
    private String iABC;

    private int channelFlag;

    private boolean active = true;

    //Имя точки для отображения в таблице
    private String name;

    //Время расчитывается по госту?
    private boolean gostTest;

    //Время теста введённое пользователем
    private String userTimeTest;

    //Количество импульсов для провала теста
    private int pulseValue;

    //Класс точности счётчика
    private double accuracyClass;

    //Константа счётчика для расчёта по ГОСТ формуле
    private int constMeterForTest;

    //Базовый ток счётчика для формулы по ГОСТ
    private double baseCurrMeter;

    //Это трехфазный счётчик?
    private boolean threePhaseMeter;

    //Датчик тока шунт?
    private boolean transfDetectShunt;

    //Количество измерительных элементов (фрехфазный или однофазный)
    private int amountMeasElem;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> startCommandResult;

    public StartCommand(int revers, int channelFlag, boolean gostTest, String userTimeTest) {
        this.userTimeTest = userTimeTest;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.gostTest = gostTest;

        if (!gostTest) {
            try {
                //Расчёт времени теста исходя из параметров введённых пользователем
                String[] timearr = userTimeTest.split(":");
                String hours = timearr[0];
                String mins = timearr[1];
                String seks = timearr[2];

                timeForTest = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;
            }catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println("Введены неверные данные");
            }

        }
    }

    public StartCommand(int revers, int channelFlag, boolean gostTest) {
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.gostTest = gostTest;
    }

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void execute() throws InterruptedException, InterruptedTestException, ConnectForStendExeption {

        if (interrupt) throw  new InterruptedTestException();
        startCommandResult = initCreepCommandResult();

        if (gostTest) {
            timeForTest = initTimeForGOSTTest();
        }

        if (interrupt) throw  new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, revers,
                100.0, 100.0, iABC, "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw  new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        if (interrupt) throw  new InterruptedTestException();
        Thread.sleep(stendDLLCommands.getPauseForStabization());

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;

        /**
         * Необходимо записывать время и результат в массив
         * Вид записи результата:
         * Начало это просто время теста
         * Как только пользователь нажимает старт (очистка и таймер теста)
         * Если прошёл "Время теста + P" и цвет пройденного теста
         * Если нет, то "Время теста + F" и цвет проваленного теста
         */

        while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
            if (interrupt) throw  new InterruptedTestException();

            for (Meter meter : meterList) {
                if (!meter.run(pulseValue, stendDLLCommands)) {
                    addTestTimeAndPass(meter, channelFlag, System.currentTimeMillis() - currTime);
                    startCommandResult.put(meter.getId(), true);
                }
            }
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    @Override
    public void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw  new InterruptedTestException();
        startCommandResult = initCreepCommandResult();

        if (gostTest) {
            timeForTest = initTimeForGOSTTest();
        }

        if (interrupt) throw  new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, revers,
                100.0, 100.0, iABC, "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw  new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        if (interrupt) throw  new InterruptedTestException();
        Thread.sleep(stendDLLCommands.getPauseForStabization());

        /**
         * Необходимо записывать время и результат в массив
         * Вид записи результата:
         * Начало это просто время теста
         * Как только пользователь нажимает старт (очистка и таймер теста)
         * Если прошёл "Время теста + P" и цвет пройденного теста
         * Если нет, то "Время теста + F" и цвет проваленного теста
         */

        while (!interrupt) {
            currTime = System.currentTimeMillis();
            timeEnd = currTime + timeForTest;

            while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
                if (interrupt) throw new InterruptedTestException();

                for (Meter meter : meterList) {
                    if (!meter.run(pulseValue, stendDLLCommands)) {
                        addTestTimeAndPass(meter, channelFlag, System.currentTimeMillis() - currTime);
                        startCommandResult.put(meter.getId(), true);
                    }
                }
            }
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    //Расчётная формула времени теста по ГОСТ
    public long initTimeForGOSTTest() {
        if (threePhaseMeter) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

        if (!transfDetectShunt) {
            if (accuracyClass == 1.0) {
                ratedCurr = baseCurrMeter * 0.002;
            } else {
                ratedCurr = baseCurrMeter * 0.001;
            }
        } else {
            ratedCurr = baseCurrMeter * 0.004;
        }

        double formulaResult = 2.3 * (60000 / (constMeterForTest * amountMeasElem * ratedVolt * ratedCurr));
        
        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    //В зависимости от направления тока переносит время прохождения теста в нужную строку
    private void addTestTimeAndPass(Meter meter, int channelFlag, long timePass) {
        Meter.StartResult commandResult;
        switch (channelFlag) {
            case  0: {
                commandResult = (Meter.StartResult) meter.getErrorListAPPls().get(index);
                commandResult.setTimeToPass(timePass);
                commandResult.setPassTest(true);
            }break;

            case  1: {
                commandResult = (Meter.StartResult) meter.getErrorListAPMns().get(index);
                commandResult.setTimeToPass(timePass);
                commandResult.setPassTest(true);
            }break;

            case  2: {
                commandResult = (Meter.StartResult) meter.getErrorListRPPls().get(index);
                commandResult.setTimeToPass(timePass);
                commandResult.setPassTest(true);
            }break;

            case  3: {
                commandResult = (Meter.StartResult) meter.getErrorListRPMns().get(index);
                commandResult.setTimeToPass(timePass);
                commandResult.setPassTest(true);
            }break;
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setTransfDetectShunt(boolean transfDetectShunt) {
        this.transfDetectShunt = transfDetectShunt;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPulseValue(int pulseValue) {
        this.pulseValue = pulseValue;
    }

    public void setConstMeterForTest(int constMeterForTest) {
        this.constMeterForTest = constMeterForTest;
    }

    public void setBaseCurrMeter(double baseCurrMeter) {
        this.baseCurrMeter = baseCurrMeter;
    }

    public void setThreePhaseMeter(boolean treePhaseMeter) {
        this.threePhaseMeter = treePhaseMeter;
    }

    public void setAccuracyClass(double accuracyClass) {
        this.accuracyClass = accuracyClass;
    }

    public void setRatedCurr(double ratedCurr) {
        this.ratedCurr = ratedCurr;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public boolean isGostTest() {
        return gostTest;
    }

    public String getUserTimeTest() {
        return userTimeTest;
    }

    public int getPulseValue() {
        return pulseValue;
    }

    public double getRatedCurr() {
        return ratedCurr;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMeterList(List<Meter> meterList) {
        this.meterList = meterList;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public long getTimeForTest() {
        return timeForTest;
    }
}
