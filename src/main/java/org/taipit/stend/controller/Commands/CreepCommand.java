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

    //Время теста введённое пользователем
    private String userTimeTest;

    //Количество импоульсов для провала теста
    private int pulseValue = 2;

    private long timeForTest;
    private long timeStart;
    private long timeEnd;
    private long currTime;
    private String strTime;

    private Timer timer;

//    transient private TimerTask timerTask = new TimerTask() {
//        @Override
//        public void run() {
//            if (System.currentTimeMillis() < timeEnd) {
//                currTime = timeEnd - System.currentTimeMillis();
//
//                strTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
//                        TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
//                        TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));
//
//                for (Map.Entry<Integer, Boolean> map : creepCommandResult.entrySet()) {
//                    if (map.getValue()) {
//                        Meter.CommandResult errorResult = meterList.get(map.getKey() - 1).returnResultCommand(index, pulseValue);
//                        errorResult.setLastResultForTabView("N"+strTime);
//                    }
//                }
//
//            } else timer.cancel();
//        }
//    };

    private HashMap<Integer, Boolean> creepCommandResult;

    public CreepCommand(boolean threePhaseCommand, boolean gostTest, String name, String id, int channelFlag, String userTimeTest) {
        this.threePhaseCommand = threePhaseCommand;
        this.name = name;
        this.id = id;
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
        this.userTimeTest = userTimeTest;

        if (!gostTest) {
            try {
                String[] timearr = userTimeTest.split(":");
                String hours = timearr[0];
                String mins = timearr[1];
                String seks = timearr[2];

                timeForTest = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;

            }catch (NumberFormatException e){
                e.printStackTrace();
                System.out.println("Неверные данные для теста");
            }
        }
    }

    public CreepCommand(boolean threePhaseCommand, boolean gostTest, String name, String id,  int channelFlag) {
        this.threePhaseCommand = threePhaseCommand;
        this.gostTest = gostTest;
        this.name = name;
        this.id = id;
        this.channelFlag = channelFlag;
    }

    @Override
    public boolean execute() throws ConnectForStendExeption, InterruptedException {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() < timeEnd) {
                    currTime = timeEnd - System.currentTimeMillis();

                    strTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
                            TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));

                    for (Map.Entry<Integer, Boolean> map : creepCommandResult.entrySet()) {
                        if (map.getValue()) {
                            Meter.CommandResult errorResult = meterList.get(map.getKey() - 1).returnResultCommand(index, channelFlag);
                            errorResult.setLastResultForTabView("N"+strTime);
                        }
                    }

                } else timer.cancel();
            }
        };

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }
        }

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        creepCommandResult = initCreepCommandResult();

        timer = new Timer(true);

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(2000);

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);


        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + timeForTest;

        timer.schedule(timerTask, 0, 500);

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

            if (Thread.currentThread().isInterrupted()) {
                timer.cancel();
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    timer.cancel();
                    throw new InterruptedException();
                }

                if (mapResult.getValue()) {
                    meter = meterList.get(mapResult.getKey() - 1);
                    creepResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);

                    if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
                        stendDLLCommands.searchMark(mapResult.getKey());
                        meter.setAmountImn(meter.getAmountImn() + 1);

                        if (meter.getAmountImn() > pulseValue) {
                            creepCommandResult.put(mapResult.getKey(), false);
                            creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                        }
                    }
                }
            }
            Thread.sleep(400);
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

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() < timeEnd) {
                    currTime = timeEnd - System.currentTimeMillis();

                    strTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
                            TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));

                    for (Map.Entry<Integer, Boolean> map : creepCommandResult.entrySet()) {
                        if (map.getValue()) {
                            Meter.CommandResult errorResult = meterList.get(map.getKey() - 1).returnResultCommand(index, channelFlag);
                            errorResult.setLastResultForTabView("N"+strTime);
                        }
                    }

                } else timer.cancel();
            }
        };

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }
        }

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        creepCommandResult = initCreepCommandResult();

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(2000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

        while (!Thread.currentThread().isInterrupted()) {

            timer = new Timer(true);

            setDefTestResults(channelFlag, index);

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + timeForTest;

            timer.schedule(timerTask, 0, 500);

            while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

                if (Thread.currentThread().isInterrupted()) {
                    timer.cancel();
                    throw new InterruptedException();
                }

                for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                    if (Thread.currentThread().isInterrupted()) {
                        timer.cancel();
                        throw new InterruptedException();
                    }

                    if (mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey() - 1);
                        creepResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);

                        if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
                            stendDLLCommands.searchMark(mapResult.getKey());
                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() > pulseValue) {
                                creepCommandResult.put(mapResult.getKey(), false);
                                creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                            }
                        }
                    }
                }
                Thread.sleep(400);
            }

            //Выставляю результат теста счётчиков, которые прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                    creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
                }
            }
            countResult += 1;
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
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
            stendDLLCommands.searchMark(meter.getId());
        }
    }

    private String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }


//    //Переводит милисикунды в нужный формат
//    private String getTime(long mlS){
//        long s = mlS / 1000;
//        long hours = s / 3600;
//        long minutes = (s % 3600) / 60;
//        long seconds = s % 60;
//        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
//    }

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

    public String getUserTimeTest() {
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

    public long getTimeForTest() {
        return timeForTest;
    }

    public void setTimeForTest(long timeForTest) {
        this.timeForTest = timeForTest;
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
