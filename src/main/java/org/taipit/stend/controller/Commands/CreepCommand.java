package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CreepCommand implements Commands, Serializable, Cloneable {

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private boolean interrupt;

    private boolean nextCommand;

    private StendDLLCommands stendDLLCommands;

    //лист с счётчиками
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

    private double voltPer;

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

    //Количество импоульсов для провала теста
    private int pulseValue;

    //Время теста введённое пользователем
    private long userTimeTest;
    private long timeStart;
    private long timeEnd;

    private Timer timer;
    private TimerTask timerTask;
    private Thread currThread;

    private HashMap<Integer, Boolean> creepCommandResult;

    public CreepCommand(boolean threePhaseCommand, boolean gostTest, String name, String id, int channelFlag, long userTimeTest, int pulseValue, double voltPer) {
        this.threePhaseCommand = threePhaseCommand;
        this.name = name;
        this.id = id;
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
        this.userTimeTest = userTimeTest;
        this.pulseValue = pulseValue;
        this.voltPer = voltPer;
    }

    public CreepCommand(boolean threePhaseCommand, boolean gostTest, String name, String id,  int channelFlag) {
        this.threePhaseCommand = threePhaseCommand;
        this.gostTest = gostTest;
        this.name = name;
        this.id = id;
        this.channelFlag = channelFlag;
        this.pulseValue = 2;
        this.voltPer = 115;
    }

    @Override
    public void execute() throws ConnectForStendExeption, InterruptedException {

        currThread = Thread.currentThread();

        int refMeterCount = 0;

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        timer = new Timer(true);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Meter.CommandResult creepResult;
                if (currThread.isAlive()) {
                    for (Map.Entry<Integer, Boolean> flag : creepCommandResult.entrySet()) {
                        if (flag.getValue()) {
                            creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlag);
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                } else {
                    timer.cancel();
                }
            }
        };

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        creepCommandResult = initCreepCommandResult();

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

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);

        Thread.sleep(500); //Пауза для стабилизации

        TestErrorTableFrameController.refreshRefMeterParameters();

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        timer.schedule(timerTask, 0, 300);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

            if (refMeterCount % 7 == 0) {
                TestErrorTableFrameController.refreshRefMeterParameters();
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (mapResult.getValue()) {
                    meter = meterList.get(mapResult.getKey() - 1);

                    if (stendDLLCommands.crpstaResult(mapResult.getKey())) {

                        meter.setAmountImn(meter.getAmountImn() + 1);

                        if (meter.getAmountImn() >= pulseValue) {
                            creepCommandResult.put(mapResult.getKey(), false);

                            creepResult = (Meter.CreepResult) meterList.get(mapResult.getKey() - 1).returnResultCommand(index, channelFlag);

                            creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
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

        //Выставляю результат теста счётчиков, которые прошли тест
        for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
            if (mapResultPass.getValue()) {
                mapResultPass.setValue(true);
                creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
            }
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
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
                    for (Map.Entry<Integer, Boolean> flag : creepCommandResult.entrySet()) {
                        if (flag.getValue()) {
                            creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlag);
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                } else {
                    timer.cancel();
                }
            }
        };

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

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

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(500); // Пауза для стабилизации

        TestErrorTableFrameController.refreshRefMeterParameters();

        while (Thread.currentThread().isAlive()) {

            int refMeterCount = 0;

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            creepCommandResult = initCreepCommandResult();

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

            timer = new Timer(true);

            timer.schedule(timerTask, 0, 350);

            while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

                if (refMeterCount % 7 == 0) {
                    TestErrorTableFrameController.refreshRefMeterParameters();
                }

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    if (mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey() - 1);
                        creepResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);

                        if (stendDLLCommands.crpstaResult(mapResult.getKey())) {

                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() >= pulseValue) {
                                creepCommandResult.put(mapResult.getKey(), false);
                                creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
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

            //Выставляю результат теста счётчиков, которые прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    mapResultPass.setValue(true);
                    creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                    creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
                }
            }

            countResult++;
        }
    }


    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), true);
        }
        return init;
    }

    //reset
    private void setDefTestResults(int channelFlag, int index) {
        for (Meter meter : meterList) {
            Meter.CreepResult creepResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);
            creepResult.setLastResultForTabView("N");
            creepResult.setPassTest(null);
            creepResult.setLastResult("");
            meter.setAmountImn(0);
            stendDLLCommands.crpstaStart(meter.getId());
        }
    }

    private String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPulseValue(int pulseValue) {
        this.pulseValue = pulseValue;
    }

    public void setVoltPer(double voltPer) {
        this.voltPer = voltPer;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public boolean isGostTest() {
        return gostTest;
    }

    public int getPulseValue() {
        return pulseValue;
    }

    public double getVoltPer() {
        return voltPer;
    }

    public long getUserTimeTest() {
        return userTimeTest;
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

    @Override
    public String toString() {
        return name;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setUserTimeTest(long userTimeTest) {
        this.userTimeTest = userTimeTest;
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

    public double getRatedVolt() {
        return ratedVolt;
    }

    public void setiABC(String iABC) {
        this.iABC = iABC;
    }

    public void setRatedCurr(double ratedCurr) {
        this.ratedCurr = ratedCurr;
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

    public int getChannelFlag() {
        return channelFlag;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
