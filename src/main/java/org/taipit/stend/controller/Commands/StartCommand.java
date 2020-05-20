package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private int revers;

    //Возможно пригодится
    private double currPer;

    //Возможно пригодится
    private String iABC;

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

    private long timeForTest;
    private long timeEnd;
    private long currTime;

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
    }

    @Override
    public boolean execute() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
                return false;
            }
            startCommandResult = initCreepCommandResult();

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, revers,
                    100.0, 100.0, iABC, "1.0")) throw new ConnectForStendExeption();

            //Разблокирую интерфейc кнопок
            TestErrorTableFrameController.blockBtns.setValue(false);

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return false;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return false;
            }

            //Номер измерения
            int countResult = 1;
            Meter meter;
            Meter.CommandResult errorCommand;

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            for (Meter meterForReset : meterList) {
                setDefTestrResults(meterForReset, channelFlag, index);
            }

            currTime = System.currentTimeMillis();
            timeEnd = currTime + timeForTest;

            while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return false;
                }

                for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внутреннего цикла");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                        return false;
                    }

                    if (!mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey() - 1);
                        errorCommand = meter.returnResultCommand(index, channelFlag);

                        if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() > pulseValue) {
                                addTestTimePass(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult);
                                startCommandResult.put(mapResult.getKey(), true);
                            } else {
                                stendDLLCommands.searchMark(mapResult.getKey());
                                errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }

                        } else {
                            errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                }
                Thread.sleep(500);
            }

            //Выставляю результат теста счётчиков, которые прошли тест
            for (Map.Entry<Integer, Boolean> mapResultFail : startCommandResult.entrySet()) {
                if (!mapResultFail.getValue()) {
                    addTestTimeFail(meterList.get(mapResultFail.getKey() - 1), channelFlag, getTime(timeForTest), channelFlag);
                }
            }

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

            if (!Thread.currentThread().isInterrupted() && nextCommand) {
                return true;
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Поймал ошибку Interrupted в команде StartCommand");
            System.out.println("Состояние нити до команты interrupt в команде StartCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
            return false;
        }
        return !Thread.currentThread().isInterrupted();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
                return;
            }

            startCommandResult = initCreepCommandResult();

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, revers,
                    100.0, 100.0, iABC, "1.0")) throw new ConnectForStendExeption();

            //Разблокирую интерфейc кнопок
            TestErrorTableFrameController.blockBtns.setValue(false);

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            //Номер измерения
            int countResult = 1;
            Meter meter;
            Meter.CommandResult errorCommand;

            while (!Thread.currentThread().isInterrupted()) {

                //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
                for (Meter meterForReset : meterList) {
                    setDefTestrResults(meterForReset, channelFlag, index);
                }

                currTime = System.currentTimeMillis();
                timeEnd = currTime + timeForTest;

                while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                        return;
                    }

                    for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {

                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Получил сигнал о завершении потока из команды StartCommand из внутреннего цикла");
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                            System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                            return;
                        }

                        if (!mapResult.getValue()) {
                            meter = meterList.get(mapResult.getKey() - 1);
                            errorCommand = meter.returnResultCommand(index, channelFlag);

                            if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
                                meter.setAmountImn(meter.getAmountImn() + 1);

                                if (meter.getAmountImn() >= pulseValue) {
                                    addTestTimePass(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult);
                                    startCommandResult.put(mapResult.getKey(), true);
                                } else {
                                    stendDLLCommands.searchMark(mapResult.getKey());
                                    errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                                }

                            } else {
                                errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }
                        }
                    }
                    Thread.sleep(500);
                }

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды StartCommand");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return;
                }

                //Выставляю результат теста счётчиков, которые прошли тест
                for (Map.Entry<Integer, Boolean> mapResultFail : startCommandResult.entrySet()) {
                    if (!mapResultFail.getValue()) {
                        addTestTimeFail(meterList.get(mapResultFail.getKey() - 1), channelFlag, getTime(timeForTest), channelFlag);
                    }
                }

                countResult++;
            }

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Поймал ошибку Interrupted в команде StartCommand");
            System.out.println("Состояние нити до команты interrupt в команде StartCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
        }
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    //Переводит милисикунды в нужный формат
    private String getTime(long mlS){
        long s = mlS / 1000;
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
    private void setDefTestrResults(Meter meter, int channelFlag, int index) {
        Meter.StartResult startResult = (Meter.StartResult) meter.returnResultCommand(index, channelFlag);
        startResult.setLastResultForTabView(null);
        startResult.setPassTest(false);
        meter.setAmountImn(0);
        stendDLLCommands.searchMark(meter.getId());
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
