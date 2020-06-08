package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StartCommand implements Commands, Serializable, Cloneable {

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private boolean interrupt;

    private boolean nextCommand;

    private StendDLLCommands stendDLLCommands;

    private List<Meter> meterList;

    //Режим
    private int phase;

    //Напряжение
    private double ratedVolt;

    //Ток
    private double ratedCurr;

    //Частота
    private double ratedFreq;

    //Необходимо сделать в доп тестовом окне
    private int phaseSrequence;

    //Направление тока
    private int revers;

    //Напряжение на фазе А
    private double voltPerA;

    //Напряжение на фазе B
    private double voltPerB;

    //Напряжение на фазе C
    private double voltPerC;

    private double voltPer = 100;

    //Процен от тока
    private double currPer;

    //Коэфициент мощности
    private String cosP = "1.0";

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

    private Timer timer;
    private TimerTask timerTask;
    private Thread currThread;

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
    public void execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        int refMeterCount = 1;

        currThread = Thread.currentThread();

        timer = new Timer(true);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Meter.CommandResult creepResult;
                if (currThread.isAlive()) {
                    for (Map.Entry<Integer, Boolean> flag : startCommandResult.entrySet()) {
                        if (!flag.getValue()) {
                            creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlag);
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                } else {
                    timer.cancel();
                }
            }
        };

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

        startCommandResult = initStartCommandResult();

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);

        stendDLLCommands.setReviseMode(1);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
                voltPerC = voltPer;
                if (!stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP)) throw new ConnectForStendExeption();
            } else {
                if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();
            }
        } else {
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();
        }

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        TestErrorTableFrameController.refreshRefMeterParameters();

        Thread.sleep(1000); //Пауза для стабилизации

        TestErrorTableFrameController.refreshRefMeterParameters();

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        timer.schedule(timerTask, 0, 350);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
            if (refMeterCount % 8 == 0) {
                TestErrorTableFrameController.refreshRefMeterParameters();
            }

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

                        if (meter.getAmountImn() >= pulseValue) {
                            startCommandResult.put(mapResult.getKey(), true);
                            startResult.setResultStartCommand(getTime(System.currentTimeMillis() - timeStart), countResult, true, channelFlag);
                        } else {
                            stendDLLCommands.crpstaClear(mapResult.getKey());

                            stendDLLCommands.crpstaStart(mapResult.getKey());
                        }
                    }
                }
            }

            refMeterCount++;
            Thread.sleep(400);
        }

        //Выставляю результат теста счётчиков, которые не прошли тест
        for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {
            if (!mapResult.getValue()) {
                mapResult.setValue(true);
                startResult = (Meter.StartResult) meterList.get(mapResult.getKey() - 1).returnResultCommand(index, channelFlag);
                startResult.setResultStartCommand(startResult.getTimeTheTest(), countResult, false, channelFlag);
            }
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        currThread = Thread.currentThread();

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Meter.CommandResult creepResult;
                if (currThread.isAlive()) {
                    for (Map.Entry<Integer, Boolean> flag : startCommandResult.entrySet()) {
                        if (!flag.getValue()) {
                            creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlag);
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                } else {
                    timer.cancel();
                }
            }
        };

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

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
                voltPerC = voltPer;
                if (!stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP)) throw new ConnectForStendExeption();
            } else {
                if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();
            }
        } else {
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();
        }

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        TestErrorTableFrameController.refreshRefMeterParameters();

        Thread.sleep(1000); //Время стабилизации

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (Thread.currentThread().isAlive()) {
            int refMeterCount = 1;

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            timer = new Timer(true);

            startCommandResult = initStartCommandResult();

            timer.schedule(timerTask, 0, 350);

            while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {

                if (refMeterCount % 8 == 0) {
                    TestErrorTableFrameController.refreshRefMeterParameters();
                }

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    if (!mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey() - 1);
                        if (stendDLLCommands.crpstaResult(mapResult.getKey())) {

                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() >= pulseValue) {
                                startCommandResult.put(mapResult.getKey(), true);

                                startResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);
                                startResult.setResultStartCommand(getTime(System.currentTimeMillis() - timeStart), countResult, true, channelFlag);
                            } else {
                                stendDLLCommands.crpstaClear(mapResult.getKey());

                                stendDLLCommands.crpstaStart(mapResult.getKey());
                            }
                        }
                    }
                }

                refMeterCount++;
                Thread.sleep(400);
            }

            //Выставляю результат теста счётчиков, которые не прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : startCommandResult.entrySet()) {
                if (!mapResultPass.getValue()) {
                    mapResultPass.setValue(true);
                    startResult = (Meter.StartResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                    startResult.setResultStartCommand(startResult.getTimeTheTest(), countResult, false, channelFlag);
                }
            }
            countResult++;
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    private HashMap<Integer, Boolean> initStartCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), false);
        }
        return init;
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

    public void setPhaseSrequence(int phaseSrequence) {
        this.phaseSrequence = phaseSrequence;
    }

    public void setRevers(int revers) {
        this.revers = revers;
    }

    public void setVoltPerA(double voltPerA) {
        this.voltPerA = voltPerA;
    }

    public void setVoltPerB(double voltPerB) {
        this.voltPerB = voltPerB;
    }

    public void setVoltPerC(double voltPerC) {
        this.voltPerC = voltPerC;
    }

    public void setCurrPer(double currPer) {
        this.currPer = currPer;
    }

    public void setCosP(String cosP) {
        this.cosP = cosP;
    }

    public void setiABC(String iABC) {
        this.iABC = iABC;
    }

    public int getChannelFlag() {
        return channelFlag;
    }

    public void setVoltPer(double voltPer) {
        this.voltPer = voltPer;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
