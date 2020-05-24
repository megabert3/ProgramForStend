package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
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

    //Время теста введённое пользователем
    private long timeStart;
    private long timeEnd;
    private long currTime;
    private String strTime;

    private Timer timer;

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
    public boolean execute() throws ConnectForStendExeption, InterruptedException {

        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (System.currentTimeMillis() < timeEnd) {
                    currTime = timeEnd - System.currentTimeMillis();

                    strTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
                            TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));

                    for (Map.Entry<Integer, Boolean> map : startCommandResult.entrySet()) {
                        if (map.getValue()) {
                            Meter.CommandResult errorResult = meterList.get(map.getKey() - 1).returnResultCommand(index, channelFlag);
                            errorResult.setLastResultForTabView("N" + strTime);
                        }
                    }

                } else {
                    timer.cancel();
                    System.out.println("Остановлен");
                }
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

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.StartResult startResult;

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        startCommandResult = initStartCommandResult();

        timer = new Timer(true);

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                100, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(500);

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        timer.schedule(timerTask, 0, 500);

        if (Thread.currentThread().isInterrupted()) {
            timer.cancel();
            throw new InterruptedException();
        }

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        setDefTestResults(channelFlag, index);


        if (Thread.currentThread().isInterrupted()) {
            timer.cancel();
            throw new InterruptedException();
        }

        while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {

            if (Thread.currentThread().isInterrupted()) {
                timer.cancel();
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    timer.cancel();
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
                        }
                    }
                }
            }
            Thread.sleep(400);
        }

        //Выставляю результат теста счётчиков, которые прошли тест
        for (Map.Entry<Integer, Boolean> mapResultPass : startCommandResult.entrySet()) {
            if (mapResultPass.getValue()) {
                startResult = (Meter.StartResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                startResult.setResultStartCommand(startResult.getTimeTheTest(), countResult, true);
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

        creepCommandResult = initStartCommandResult();

        setDefTestResults(channelFlag, index);

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                100, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(500);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Номер измерения
        int countResult = 1;
        Meter meter;
        Meter.CreepResult creepResult;

        while (!Thread.currentThread().isInterrupted()) {

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            timer = new Timer(true);

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

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

                        if (stendDLLCommands.crpstaResult(mapResult.getKey())) {

                            meter.setAmountImn(meter.getAmountImn() + 1);

                            if (meter.getAmountImn() > pulseValue) {
                                creepCommandResult.put(mapResult.getKey(), false);
                                creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                            } else {
                                stendDLLCommands.crpstaClear(mapResult.getKey());

                                stendDLLCommands.crpstaStart(mapResult.getKey());
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
            countResult++;
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
    }

//    @Override
//    public boolean execute() throws ConnectForStendExeption {
//        try {
//            if (Thread.currentThread().isInterrupted()) {
//                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
//                return false;
//            }
//            startCommandResult = initStartCommandResult();
//
//            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, revers,
//                    100.0, 100.0, iABC, "1.0")) throw new ConnectForStendExeption();
//
//            //Разблокирую интерфейc кнопок
//            TestErrorTableFrameController.blockBtns.setValue(false);
//
//            if (Thread.currentThread().isInterrupted()) {
//                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
//                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                return false;
//            }
//
//            stendDLLCommands.setEnergyPulse(meterList, channelFlag);
//
//            Thread.sleep(stendDLLCommands.getPauseForStabization());
//
//            if (Thread.currentThread().isInterrupted()) {
//                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
//                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                return false;
//            }
//
//            //Номер измерения
//            int countResult = 1;
//            Meter meter;
//            Meter.CommandResult errorCommand;
//
//            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
//            for (Meter meterForReset : meterList) {
//                setDefTestResults(meterForReset, channelFlag, index);
//            }
//
//            currTime = System.currentTimeMillis();
//            timeEnd = currTime + timeForTest;
//
//            while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
//
//                if (Thread.currentThread().isInterrupted()) {
//                    System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
//                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                    return false;
//                }
//
//                for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {
//
//                    if (Thread.currentThread().isInterrupted()) {
//                        System.out.println("Получил сигнал о завершении потока из команды CreepCommand из внутреннего цикла");
//                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                        return false;
//                    }
//
//                    if (!mapResult.getValue()) {
//                        meter = meterList.get(mapResult.getKey() - 1);
//                        errorCommand = meter.returnResultCommand(index, channelFlag);
//
//                        if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
//                            meter.setAmountImn(meter.getAmountImn() + 1);
//
//                            if (meter.getAmountImn() > pulseValue) {
//                                addTestTimePass(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult);
//                                startCommandResult.put(mapResult.getKey(), true);
//                            } else {
//                                stendDLLCommands.searchMark(mapResult.getKey());
//                                errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
//                            }
//
//                        } else {
//                            errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
//                        }
//                    }
//                }
//                Thread.sleep(500);
//            }
//
//            //Выставляю результат теста счётчиков, которые прошли тест
//            for (Map.Entry<Integer, Boolean> mapResultFail : startCommandResult.entrySet()) {
//                if (!mapResultFail.getValue()) {
//                    addTestTimeFail(meterList.get(mapResultFail.getKey() - 1), channelFlag, getTime(timeForTest), channelFlag);
//                }
//            }
//
//            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//
//            if (!Thread.currentThread().isInterrupted() && nextCommand) {
//                return true;
//            }
//
//            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//
//        }catch (InterruptedException e) {
//            System.out.println("Поймал ошибку Interrupted в команде StartCommand");
//            System.out.println("Состояние нити до команты interrupt в команде StartCommand " + Thread.currentThread().getState());
//            Thread.currentThread().interrupt();
//            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
//            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
//            return false;
//        }
//        return !Thread.currentThread().isInterrupted();
//    }
//
//    @Override
//    public void executeForContinuousTest() throws ConnectForStendExeption {
//        try {
//            if (Thread.currentThread().isInterrupted()) {
//                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
//                return;
//            }
//
//            startCommandResult = initStartCommandResult();
//
//            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, 0, revers,
//                    100.0, 100.0, iABC, "1.0")) throw new ConnectForStendExeption();
//
//            //Разблокирую интерфейc кнопок
//            TestErrorTableFrameController.blockBtns.setValue(false);
//
//            if (Thread.currentThread().isInterrupted()) {
//                System.out.println("Получил сигнал о завершении потока из команды StartCommand");
//                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                return;
//            }
//
//            stendDLLCommands.setEnergyPulse(meterList, channelFlag);
//
//            Thread.sleep(stendDLLCommands.getPauseForStabization());
//
//            //Номер измерения
//            int countResult = 1;
//            Meter meter;
//            Meter.CommandResult errorCommand;
//
//            while (!Thread.currentThread().isInterrupted()) {
//
//                //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
//                for (Meter meterForReset : meterList) {
//                    setDefTestResults(meterForReset, channelFlag, index);
//                }
//
//                currTime = System.currentTimeMillis();
//                timeEnd = currTime + timeForTest;
//
//                while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
//
//                    if (Thread.currentThread().isInterrupted()) {
//                        System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
//                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                        return;
//                    }
//
//                    for (Map.Entry<Integer, Boolean> mapResult : startCommandResult.entrySet()) {
//
//                        if (Thread.currentThread().isInterrupted()) {
//                            System.out.println("Получил сигнал о завершении потока из команды StartCommand из внутреннего цикла");
//                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                            System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                            return;
//                        }
//
//                        if (!mapResult.getValue()) {
//                            meter = meterList.get(mapResult.getKey() - 1);
//                            errorCommand = meter.returnResultCommand(index, channelFlag);
//
//                            if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {
//                                meter.setAmountImn(meter.getAmountImn() + 1);
//
//                                if (meter.getAmountImn() >= pulseValue) {
//                                    addTestTimePass(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult);
//                                    startCommandResult.put(mapResult.getKey(), true);
//                                } else {
//                                    stendDLLCommands.searchMark(mapResult.getKey());
//                                    errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
//                                }
//
//                            } else {
//                                errorCommand.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
//                            }
//                        }
//                    }
//                    Thread.sleep(500);
//                }
//
//                if (Thread.currentThread().isInterrupted()) {
//                    System.out.println("Получил сигнал о завершении потока из команды StartCommand");
//                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
//                    return;
//                }
//
//                //Выставляю результат теста счётчиков, которые прошли тест
//                for (Map.Entry<Integer, Boolean> mapResultFail : startCommandResult.entrySet()) {
//                    if (!mapResultFail.getValue()) {
//                        addTestTimeFail(meterList.get(mapResultFail.getKey() - 1), channelFlag, getTime(timeForTest), channelFlag);
//                    }
//                }
//
//                countResult++;
//            }
//
//            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//
//        }catch (InterruptedException e) {
//            System.out.println("Поймал ошибку Interrupted в команде StartCommand");
//            System.out.println("Состояние нити до команты interrupt в команде StartCommand " + Thread.currentThread().getState());
//            Thread.currentThread().interrupt();
//            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
//            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
//            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
//            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
//        }
//    }

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
