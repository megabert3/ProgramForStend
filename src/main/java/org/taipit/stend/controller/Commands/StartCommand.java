package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
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
    private double currPer = 100;

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

    public StartCommand(boolean threePhaseCommand, String name, String id, int revers, int channelFlag, boolean gostTest, long userTimeTest, int pulseValue, double ratedCurr) {
        this.threePhaseCommand = threePhaseCommand;
        this.name = name;
        this.id = id;
        this.userTimeTest = userTimeTest;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.gostTest = gostTest;
        this.pulseValue = pulseValue;
        this.ratedCurr = ratedCurr;
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

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.StartResult startResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        startCommandResult = initStartCommandResult();

        stendDLLCommands.setReviseMode(1);

        TestErrorTableFrameController.transferParam(this);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = ConsoleHelper.properties.getProperty("phaseOnOnePhaseMode");

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

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

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        timer.schedule(timerTask, 0, 275);

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

                    if (stendDLLCommands.countRead(mapResult.getKey()) >= pulseValue) {

                        startCommandResult.put(mapResult.getKey(), true);
                        startResult.setResultStartCommand(getTime(System.currentTimeMillis() - timeStart), countResult, true, channelFlag);
                    }
                }
            }

            refMeterCount++;
            Thread.sleep(400);
        }

        //Выставляю результат теста счётчиков, которые не прошли тест
        for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {
            if (!mapResult.getValue()) {
                //Для остановки таймера
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

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.StartResult startResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        stendDLLCommands.setReviseMode(1);

        TestErrorTableFrameController.transferParam(this);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = ConsoleHelper.properties.getProperty("phaseOnOnePhaseMode");

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

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

            timer.schedule(timerTask, 0, 275);

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

                        if (stendDLLCommands.countRead(mapResult.getKey()) >= pulseValue) {

                            startCommandResult.put(mapResult.getKey(), true);
                            startResult.setResultStartCommand(getTime(System.currentTimeMillis() - timeStart), countResult, true, channelFlag);
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
            stendDLLCommands.countStart(meter.getId());
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
    public String getUserTimeTestHHmmss() {
        return getTime(userTimeTest);
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

    public double getVoltPer() {
        return voltPer;
    }

    public double getCurrPer() {
        return currPer;
    }

    @Override
    public double getRatedVolt() {
        return ratedVolt;
    }

    @Override
    public double getVoltPerA() {
        return voltPerA;
    }

    @Override
    public double getVoltPerB() {
        return voltPerB;
    }

    @Override
    public double getVoltPerC() {
        return voltPerC;
    }

    @Override
    public String getiABC() {
        return iABC;
    }

    public boolean isThreePhaseCommand() {
        return threePhaseCommand;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
