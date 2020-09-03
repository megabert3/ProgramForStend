package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class RelayCommand implements Commands, Serializable, Cloneable {

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

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
    private int revers = 0;

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

    private int channelFlag = 0;

    private int channelFlagForSave;

    private boolean active = true;

    //Имя точки для отображения в таблице
    private String name;

    private String id;

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

    private HashMap<Integer, Boolean> relayCommandResult;

    public RelayCommand(boolean threePhaseCommand, String name, String id, int channelFlag, long userTimeTest, int pulseValue, double ratedCurr) {
        this.threePhaseCommand = threePhaseCommand;
        this.name = name;
        this.id = id;
        this.userTimeTest = userTimeTest;
        this.channelFlagForSave = channelFlag;
        this.pulseValue = pulseValue;
        this.ratedCurr = ratedCurr;
    }

    @Override
    public void execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.errorClear();

        int refMeterCount = 1;

        currThread = Thread.currentThread();

        //Номер измерения
        int countResult = 1;

        Meter.RelayResult relayResult;

        relayCommandResult = initRelayCommandResult();

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        TestErrorTableFrameController.transferParam(this);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

                stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
            } else {
                stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        } else {
            stendDLLCommands.selectCircuit(0);

            stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP);
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        TestErrorTableFrameController.refreshRefMeterParameters();

        setTestMode();

        Thread.sleep(2000);

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        timer = new Timer(true);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                Meter.CommandResult creepResult;
                if (currThread.isAlive()) {
                    for (Map.Entry<Integer, Boolean> flag : relayCommandResult.entrySet()) {
                        if (flag.getValue()) {
                            creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlagForSave);
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                } else {
                    timer.cancel();
                }
            }
        };

        timer.schedule(timerTask, 0, 275);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (pulseValue == 1) {
            startTestModeSearchMark(refMeterCount, countResult);
        } else {
            startTestModeCount(refMeterCount, countResult);
        }

        timer.cancel();

        //Выставляю результат теста счётчиков, которые не прошли тест
        for (Map.Entry<Integer, Boolean> mapResult : relayCommandResult.entrySet()) {
            if (mapResult.getValue()) {
                relayResult = (Meter.RelayResult) meterList.get(mapResult.getKey() - 1).returnResultCommand(index, channelFlagForSave);
                relayResult.setResultRelayCommand(relayResult.getTimeTheTest(), countResult, true);
            }
        }

        stendDLLCommands.errorClear();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        currThread = Thread.currentThread();

        //Номер измерения
        int countResult = 1;
        Meter.RelayResult relayResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        TestErrorTableFrameController.transferParam(this);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

                stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
            } else {
                stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        } else {
            stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP);
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        TestErrorTableFrameController.refreshRefMeterParameters();

        Thread.sleep(2000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        while (Thread.currentThread().isAlive()) {
            stendDLLCommands.errorClear();

            int refMeterCount = 1;

            relayCommandResult = initRelayCommandResult();

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            setTestMode();

            TestErrorTableFrameController.refreshRefMeterParameters();

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

            timer = new Timer(true);

            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Meter.CommandResult relayResult;
                    if (currThread.isAlive()) {
                        for (Map.Entry<Integer, Boolean> flag : relayCommandResult.entrySet()) {
                            if (flag.getValue()) {
                                relayResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlagForSave);
                                relayResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }
                        }
                    } else {
                        timer.cancel();
                    }
                }
            };

            timer.schedule(timerTask, 0, 275);

            if (pulseValue == 1) {
                startTestModeSearchMark(refMeterCount, countResult);
            } else {
                startTestModeCount(refMeterCount, countResult);
            }

            timer.cancel();

            //Выставляю результат теста счётчиков, которые не прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : relayCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    mapResultPass.setValue(false);
                    relayResult = (Meter.RelayResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlagForSave);
                    relayResult.setResultRelayCommand(relayResult.getTimeTheTest(), countResult, false);
                }
            }
            countResult++;

            //Время на подумать оставлять результат или нет
            Thread.sleep(7000);
        }

        stendDLLCommands.errorClear();
    }

    private HashMap<Integer, Boolean> initRelayCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), true);
        }
        return init;
    }

    private void startTestModeCount(int refMeterCount, int countResult) throws InterruptedException, ConnectForStendExeption {
        while (relayCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

            if (refMeterCount % 8 == 0) {
                TestErrorTableFrameController.refreshRefMeterParameters();
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : relayCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (mapResult.getValue()) {
                    Meter meter = meterList.get(mapResult.getKey() - 1);
                    Meter.RelayResult relayResult = (Meter.RelayResult) meter.returnResultCommand(index, channelFlagForSave);

                    if (stendDLLCommands.countRead(mapResult.getKey()) >= pulseValue - 1) {

                        relayCommandResult.put(mapResult.getKey(), false);
                        relayResult.setResultRelayCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                    }
                }
            }

            refMeterCount++;

            Thread.sleep(400);
        }
    }

    private void startTestModeSearchMark(int refMeterCount, int countResult) throws InterruptedException {
        while (relayCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

            if (refMeterCount % 8 == 0) {
                TestErrorTableFrameController.refreshRefMeterParameters();
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : relayCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (mapResult.getValue()) {
                    Meter meter = meterList.get(mapResult.getKey() - 1);
                    Meter.RelayResult relayResult = (Meter.RelayResult) meter.returnResultCommand(index, channelFlagForSave);

                    if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {

                        relayCommandResult.put(mapResult.getKey(), false);
                        relayResult.setResultRelayCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                    }
                }
            }

            refMeterCount++;

            Thread.sleep(400);
        }
    }

    //reset
    private void setDefTestResults(int channelFlag, int index) {
        for (Meter meter : meterList) {
            Meter.RelayResult relayResult = (Meter.RelayResult) meter.returnResultCommand(index, channelFlagForSave);
            relayResult.setLastResultForTabView("N");
            relayResult.setPassTest(null);
            relayResult.setLastResult("");
        }
    }

    private void setTestMode() throws ConnectForStendExeption {
        if (pulseValue == 1) {
            for (Meter meter : meterList) {
                stendDLLCommands.searchMark(meter.getId());
            }
        } else {
            for (Meter meter : meterList) {
                stendDLLCommands.countStart(meter.getId());
            }
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

    public String getPauseForStabilization() {
        return "";
    }

    @Override
    public void setPauseForStabilization(double pauseForStabilization) {
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
