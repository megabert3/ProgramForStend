package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StartCommand implements Commands, Serializable {

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private boolean interrupt;

    private boolean nextCommand;

    private StendDLLCommands stendDLLCommands;

    private List<Meter> meterList;

    //Выставлять в зависимости от выбранного параметра перед тестом
    private int phase;

    //Номинальное напряжение
    private double ratedVolt;

    //Номинальный ток
    private double ratedCurr;

    //Номинальная частота
    private double ratedFreq;

    private double voltPer = 100;

    private int revers;

    //Возможно пригодится
    private double currPer;

    //Возможно пригодится
    private String iABC = "H";

    private int channelFlag;

    private boolean active = true;

    //Имя точки для отображения в таблице
    private String name;

    private String id;

    //Время расчитывается по госту?
    private boolean gostTest;

    //Время теста введённое пользователем
    private long userTimeTest;

    //Количество импульсов для провала теста
    private int pulseValue;

    //Время теста введённое пользователем
    private long timeStart;
    private long timeEnd;
    private long currTime;
    private String strTime;

    private HashMap<Integer, Boolean> startCommandResult;

    public StartCommand(boolean threePhaseCommand, String name, String id, int revers, int channelFlag, boolean gostTest, long userTimeTest, int pulseValue, double currPer) {
        this.threePhaseCommand = threePhaseCommand;
        this.name = name;
        this.id = id;
        this.userTimeTest = userTimeTest;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.gostTest = gostTest;
        this.pulseValue = pulseValue;
        this.currPer = currPer;
    }

    public StartCommand(boolean threePhaseCommand, String name, String id, int revers, int channelFlag, boolean gostTest) {
        this.name = name;
        this.id = id;
        this.threePhaseCommand = threePhaseCommand;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.gostTest = gostTest;
        this.pulseValue = 2;
    }

    @Override
    public boolean execute() throws ConnectForStendExeption, InterruptedException {

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Meter meterForCalculate = meterList.get(0);

        if (gostTest) {
            if (meterForCalculate.isTypeOfMeasuringElementShunt()) {
                double current = 0.004 * meterForCalculate.getIb();
                currPer = current * 100 / ratedCurr;
            } else {
                if (meterForCalculate.getAccuracyClassAP() <= 0.5) {
                    double current = 0.001 * meterForCalculate.getIb();
                    currPer = current * 100 / ratedCurr;
                } else {
                    double current = 0.002 * meterForCalculate.getIb();
                    currPer = current * 100 / ratedCurr;
                }
            }
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }
        } else {
            if (!threePhaseCommand) {
                if (iABC.equals("A")) {
                    if (stendDLLCommands.selectCircuit(0)) throw new ConnectForStendExeption();
                    iABC = "H";
                } else if (iABC.equals("B")) {
                    if (stendDLLCommands.selectCircuit(1)) throw new ConnectForStendExeption();
                    iABC = "H";
                }
            }
        }

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.StartResult startResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        startCommandResult = initStartCommandResult();

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);

        stendDLLCommands.setReviseMode(1);

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, 0,
                voltPer, currPer, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(500);

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (!mapResult.getValue()) {
                    meter = meterList.get(mapResult.getKey() - 1);
                    startResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);

                    if (stendDLLCommands.crpstaResult(mapResult.getKey())) {

                        meter.setAmountImn(meter.getAmountImn() + 1);

                        if (meter.getAmountImn() > pulseValue) {
                            startCommandResult.put(mapResult.getKey(), true);
                            startResult.setResultStartCommand(getTime(System.currentTimeMillis() - timeStart), countResult, true, channelFlag);
                        } else {
                            stendDLLCommands.crpstaClear(mapResult.getKey());

                            stendDLLCommands.crpstaStart(mapResult.getKey());

                            startResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    } else {
                        startResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                    }
                }
            }
            Thread.sleep(350);
        }

        //Выставляю результат теста счётчиков, которые не прошли тест
        for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {
            if (!mapResult.getValue()) {
                startResult = (Meter.StartResult) meterList.get(mapResult.getKey() - 1).returnResultCommand(index, channelFlag);
                startResult.setResultStartCommand(startResult.getTimeTheTest(), countResult, false, channelFlag);
            }
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        if (!Thread.currentThread().isInterrupted() && nextCommand) return true;

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        return !Thread.currentThread().isInterrupted();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }
        } else {
            if (!threePhaseCommand) {
                if (iABC.equals("A")) {
                    if (stendDLLCommands.selectCircuit(0)) throw new ConnectForStendExeption();
                    iABC = "H";
                } else if (iABC.equals("B")) {
                    if (stendDLLCommands.selectCircuit(1)) throw new ConnectForStendExeption();
                    iABC = "H";
                }
            }
        }

        Meter meterForCalculate = meterList.get(0);
        if (gostTest) {
            if (meterForCalculate.isTypeOfMeasuringElementShunt()) {
                double current = 0.004 * meterForCalculate.getIb();
                currPer = current * 100 / ratedCurr;
            } else {
                if (meterForCalculate.getAccuracyClassAP() <= 0.5) {
                    double current = 0.001 * meterForCalculate.getIb();
                    currPer = current * 100 / ratedCurr;
                } else {
                    double current = 0.002 * meterForCalculate.getIb();
                    currPer = current * 100 / ratedCurr;
                }
            }
        }

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.StartResult startResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        stendDLLCommands.setReviseMode(1);

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, 0,
                voltPer, currPer, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(500);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (!Thread.currentThread().isInterrupted()) {

            startCommandResult = initStartCommandResult();

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

            while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    if (!mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey() - 1);
                        startResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);

                        if (stendDLLCommands.crpstaResult(mapResult.getKey())) {

                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() > pulseValue) {
                                startCommandResult.put(mapResult.getKey(), true);
                                startResult.setResultStartCommand(getTime(System.currentTimeMillis() - timeStart), countResult, true, channelFlag);
                            } else {
                                stendDLLCommands.crpstaClear(mapResult.getKey());

                                stendDLLCommands.crpstaStart(mapResult.getKey());

                                startResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }
                        } else {
                            startResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                }
                Thread.sleep(400);
            }

            //Выставляю результат теста счётчиков, которые не прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : startCommandResult.entrySet()) {
                if (!mapResultPass.getValue()) {
                    startResult = (Meter.StartResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                    startResult.setResultStartCommand(startResult.getTimeTheTest(), countResult, false, channelFlag);
                }
            }
            countResult++;
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
    }

    private HashMap<Integer, Boolean> initStartCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    //Переносит время провала теста в нужную строку
    private void addTestTimeFail(Meter meter, int channelFlag, String timeFail, int countResult) {
        switch (channelFlag) {
            case 0: {
                meter.setStartTestAPPls(false);
            }break;
            case 1: {
                meter.setStartTestAPMns(false);
            }break;
            case 2: {
                meter.setStartTestRPPls(false);
            }break;
            case 3: {
                meter.setStartTestRPMns(false);
            }
        }
        Meter.StartResult commandResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);
        commandResult.setPassTest(false);
        commandResult.setLastResultForTabView("F" + timeFail + " П");
        commandResult.setLastResult(timeFail);
        commandResult.getResults()[countResult] = timeFail + " П";
    }

    //Переносит время провала теста в нужную строку
    private void addTestTimePass(Meter meter, int channelFlag, String timePass, int countResult) {
        switch (channelFlag) {
            case 0: {
                meter.setStartTestAPPls(true);
            }break;
            case 1: {
                meter.setStartTestAPMns(true);
            }break;
            case 2: {
                meter.setStartTestRPPls(true);
            }break;
            case 3: {
                meter.setStartTestRPMns(true);
            }break;
        }
        Meter.StartResult commandResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);
        commandResult.setPassTest(true);
        commandResult.setLastResultForTabView("P" + timePass + " +");
        commandResult.setLastResult(timePass);
        commandResult.getResults()[countResult] = timePass + " Г";
    }

    //reset
    private void setDefTestResults(int channelFlag, int index) {
        for (Meter meter : meterList) {
            Meter.StartResult startResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);
            startResult.setLastResultForTabView("N");
            startResult.setPassTest(null);
            startResult.setLastResult("");
            meter.setAmountImn(0);
            stendDLLCommands.crpstaStart(meter.getId());
        }
    }

    private String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void setIndex(int index) {
        this.index = index;
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

    public long getUserTimeTest() {
        return userTimeTest;
    }

    public void setUserTimeTest(long timeForTest) {
        this.userTimeTest = timeForTest;
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

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void setNextCommand(boolean nextCommand) {
        this.nextCommand = nextCommand;
    }

    @Override
    public void setPulse(String pulse) {

    }

    @Override
    public void setCountResult(String countResult) {

    }

    @Override
    public void setEmax(String emax) {

    }

    @Override
    public void setEmin(String emin) {

    }

    public String getId() {
        return id;
    }
}
