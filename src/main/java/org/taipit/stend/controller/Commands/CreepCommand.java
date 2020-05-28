package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


public class CreepCommand implements Commands, Serializable {

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private boolean interrupt;

    private boolean nextCommand;

    private String iABC = "H";

    private StendDLLCommands stendDLLCommands;

    //лист с счётчиками
    private List<Meter> meterList;

    private int phase;

    private double ratedVolt;

    private double ratedFreq;

    private double voltPer;

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
    private long currTime;
    private String strTime;

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
    }

    @Override
    public boolean execute() throws ConnectForStendExeption, InterruptedException {

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }
        }

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        creepCommandResult = initCreepCommandResult();

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);

        stendDLLCommands.setReviseMode(1);

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(500);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

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

                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    } else {
                        creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                    }
                }
            }
            Thread.sleep(350);
        }

        //Выставляю результат теста счётчиков, которые прошли тест
        for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
            if (mapResultPass.getValue()) {
                creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
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
        }

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        setDefTestResults(channelFlag, index);

        stendDLLCommands.setReviseMode(1);

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(500);

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

        while (!Thread.currentThread().isInterrupted()) {

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            creepCommandResult = initCreepCommandResult();

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

            while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

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

                                creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }
                        } else {
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                }
                Thread.sleep(350);
            }

            //Выставляю результат теста счётчиков, которые прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                    creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
                }
            }

            countResult++;
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
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
}
