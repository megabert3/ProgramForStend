package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreepCommand implements Commands, Serializable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    boolean interrupt;

    boolean nextCommand;

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

    //Время расчитывается по госту?
    private boolean gostTest;

    //Время теста введённое пользователем
    private String userTimeTest;

    //Количество импоульсов для провала теста
    private int pulseValue = 2;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> creepCommandResult;

    public CreepCommand(boolean gostTest, int channelFlag, String userTimeTest) {
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

    public CreepCommand(boolean gostTest, int channelFlag) {
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
    }

    @Override
    public boolean execute() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды CreepCommand");
                return false;
            }

            creepCommandResult = initCreepCommandResult();

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                    voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды CreepCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return false;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды CreepCommand");
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

            while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внешнего цикла");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return false;
                }

                for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внутреннего цикла");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                        return false;
                    }

                    if (mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey() - 1);
                        errorCommand = meter.returnResultCommand(index, channelFlag);

                        if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() > pulseValue) {
                                addTestTimeFail(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult);
                                creepCommandResult.put(mapResult.getKey(), false);
                            } else {
                                stendDLLCommands.searchMark(mapResult.getKey());
                                errorCommand.setLastResult("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }

                        } else {
                            errorCommand.setLastResult("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                }
                Thread.sleep(500);
            }

            //Выставляю результат теста счётчиков, которые прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    addTestTimePass(meterList.get(mapResultPass.getKey() - 1), channelFlag, getTime(timeForTest), channelFlag);
                }
            }

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

            if (!Thread.currentThread().isInterrupted() && nextCommand) return true;

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Состояние нити до команты interrupt в команде CreepCommand " + Thread.currentThread().getState());
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
                System.out.println("Получил сигнал о завершении потока из команды CreepCommand");
                return;
            }
            creepCommandResult = initCreepCommandResult();

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                    voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды CreepCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды CreepCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            //Номер измерения
            int countResult = 1;
            Meter.CommandResult errorCommand;
            Meter meter;

            while (!Thread.currentThread().isInterrupted()) {

                //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
                for (Meter meterForReset : meterList) {
                    setDefTestrResults(meterForReset, channelFlag, index);
                }

                currTime = System.currentTimeMillis();
                timeEnd = currTime + timeForTest;

                while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внешнего цикла");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                        return;
                    }

                    for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внутреннего цикла");
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                            System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                            return;
                        }

                        if (mapResult.getValue()) {
                            meter = meterList.get(mapResult.getKey() - 1);
                            errorCommand = meter.returnResultCommand(index, channelFlag);

                            if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
                                meter.setAmountImn(meter.getAmountImn() + 1);

                                if (meter.getAmountImn() >= pulseValue) {
                                    addTestTimeFail(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult);
                                    creepCommandResult.put(mapResult.getKey(), false);
                                } else {
                                    stendDLLCommands.searchMark(mapResult.getKey());
                                    errorCommand.setLastResult("N" + getTime(timeEnd - System.currentTimeMillis()));
                                }

                            } else {
                                errorCommand.setLastResult("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }
                        }
                    }
                    Thread.sleep(500);
                }

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внешнего цикла");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return;
                }

                //Выставляю результат теста счётчиков, которые прошли тест
                for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                    if (mapResultPass.getValue()) {
                        addTestTimePass(meterList.get(mapResultPass.getKey() - 1), channelFlag, getTime(timeForTest), channelFlag);
                    }
                }

                countResult++;
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Поймал ошибку Interrupted в команде CreepCommand");
            System.out.println("Состояние нити до команты interrupt в команде CreepCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
            return;
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
    private void setDefTestrResults(Meter meter, int channelFlag, int index) {
        Meter.CreepResult creepResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);
        creepResult.setLastResult(null);
        creepResult.setPassTest(true);
        meter.setAmountImn(0);
        stendDLLCommands.searchMark(meter.getId());
    }

    //Переносит время провала теста в нужную строку
    private void addTestTimeFail(Meter meter, int channelFlag, String timeFail, int countResult) {
        meter.setCreepTest(false);
        Meter.CreepResult commandResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);
        commandResult.setPassTest(false);
        commandResult.setLastResult("F" + timeFail + " " + "П");
        commandResult.getResults()[countResult] = commandResult.getLastResult().substring(1, commandResult.getLastResult().length() - 2);
    }

    //Переносит время провала теста в нужную строку
    private void addTestTimePass(Meter meter, int channelFlag, String timeFail, int countResult) {
        meter.setCreepTest(true);
        Meter.CreepResult commandResult = (Meter.CreepResult) meter.returnResultCommand(index, channelFlag);
        commandResult.setPassTest(true);
        commandResult.setLastResult("P" + timeFail + " " + "Г");
        commandResult.getResults()[countResult] = commandResult.getLastResult().substring(1, commandResult.getLastResult().length() - 2);
    }

    //Переводит милисикунды в нужный формат
    private String getTime(long mlS){
        long s = mlS / 1000;
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
}
