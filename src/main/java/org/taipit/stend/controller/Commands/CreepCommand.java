package org.taipit.stend.controller.Commands;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreepCommand implements Commands, Serializable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private StendDLLCommands stendDLLCommands;

    //лист с счётчиками
    private List<Meter> meterList;

    //Команда для прерывания метода
    private boolean interrupt;

    private int phase;

    private double ratedVolt;

    private double ratedFreq;

    private double voltPer;

    private int channelFlag;

    private boolean active = true;


    //Лист со столбами счётчикв для изменения флага и цвета
    private List<TableColumn<Meter.CommandResult, String>> tableColumnError;

    //Имя точки для отображения в таблице
    private String name;

    //Время расчитывается по госту?
    private boolean gostTest;

    //Время теста введённое пользователем
    private String userTimeTest;

    //Количество импоульсов для провала теста
    private int pulseValue = 2;

//    //Константа счётчика для расчёта по ГОСТ формуле
//    private int constMeterForTest;
//
//    //Максимальный ток счётчика для формулы по ГОСТ
//    private double maxCurrMeter;
//
//    //Это трехфазный счётчик?
//    private boolean threePhaseMeter;
//
//    //Количество измерительных элементов (фрехфазный или однофазный)
//    private int amountMeasElem;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> creepCommandResult;

    //Настройка для отдельного поля счётчика изменения цвета погрешности перед началом теста
    private transient Callback<TableColumn<Meter.CommandResult, String>, TableCell<Meter.CommandResult, String>> cellFactoryStartTest =
            new Callback<TableColumn<Meter.CommandResult, String>, TableCell<Meter.CommandResult, String>>() {
                public TableCell call(TableColumn p) {
                    return new TableCell<Meter.CommandResult, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);
                                setText(item);
                        }
                    };
                }
            };

    //Настройка для отдельного поля счётчика изменения цвета погрешности после окончания теста
    private transient Callback<TableColumn<Meter.CommandResult, String>, TableCell<Meter.CommandResult, String>> cellFactoryEndTest =
            new Callback<TableColumn<Meter.CommandResult, String>, TableCell<Meter.CommandResult, String>>() {
                public TableCell call(TableColumn p) {
                    return new TableCell<Meter.CommandResult, String>() {
                        @Override
                        public void updateItem(String item, boolean empty) {
                            super.updateItem(item, empty);

                            if (item == null || empty) {
                                setText("");
                            } else {
                                if (item.contains("+")) {
                                    setText(item);
                                    setTextFill(Color.BLUE);
                                } else if (item.contains("-")) {
                                    setText(item);
                                    setTextFill(Color.RED);
                                }
                            }
                        }
                    };
                }
            };


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
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void execute() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw new InterruptedTestException();
        creepCommandResult = initCreepCommandResult();

        if (interrupt) throw new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        //Номер измерения
        int countResult = 1;
        Meter meter;

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
        for (Meter meter1 : meterList) {
            setDefTestrResults(meter1, channelFlag, index);
        }

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {
            if (interrupt) throw new InterruptedTestException();

            for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {
                if (mapResult.getValue()) {
                    meter = meterList.get(mapResult.getKey() - 1);

                    //Если на данный момен времени всё хорошо и счётчик не получал импульсов
                    if (meter.run(pulseValue, stendDLLCommands)) {

                        meter.setResultsInErrorList(index, countResult, getTime(timeEnd - System.currentTimeMillis()), channelFlag);

                        //Если количество импульсов переваливает и тест провален
                    } else {
                        //Если счётчик провалили тест
                        addTestTimeAndFail(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult, tableColumnError);
                        creepCommandResult.put(meter.getId(), false);
                    }
                }
            }
            Thread.sleep(500);
        }

        //Выставляю результат теста счётчиков, которые прошли тест
        for(Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
            if (mapResultPass.getValue()) {
                tableColumnError.get(mapResultPass.getKey() - 1).setCellFactory(cellFactoryEndTest);
                meterList.get(mapResultPass.getKey()).setResultsInErrorList(index, countResult, getTime(timeForTest) + " " + "+", channelFlag);
            }
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    @Override
    public void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw new InterruptedTestException();
        creepCommandResult = initCreepCommandResult();

        if (interrupt) throw new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        //Номер измерения
        int countResult = 1;
        Meter meter;

        while (!interrupt) {

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            for (Meter meter1 : meterList) {
                setDefTestrResults(meter1, channelFlag, index);
            }

            currTime = System.currentTimeMillis();
            timeEnd = currTime + timeForTest;

            while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {
                if (interrupt) throw new InterruptedTestException();

                for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {
                    if (mapResult.getValue()) {
                        meter = meterList.get(mapResult.getKey());

                        //Если на данный момен времени всё хорошо и счётчик не получал импульсов
                        if (meter.run(pulseValue, stendDLLCommands)) {

                            meter.setResultsInErrorList(index, countResult, getTime(timeEnd - System.currentTimeMillis()), channelFlag);

                            //Если количество импульсов переваливает и тест провален
                        } else {
                            //Если счётчик провалили тест
                            addTestTimeAndFail(meter, channelFlag, getTime(System.currentTimeMillis() - currTime), countResult, tableColumnError);
                            creepCommandResult.put(meter.getId(), false);
                        }
                    }
                }
                Thread.sleep(500);
            }

            //Выставляю результат теста счётчиков, которые прошли тест
            for(Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    tableColumnError.get(mapResultPass.getKey() - 1).setCellFactory(cellFactoryEndTest);
                    meterList.get(mapResultPass.getKey()).setResultsInErrorList(index, countResult, getTime(timeForTest) + " " + "+", channelFlag);
                }
            }

            countResult++;
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

    private void setDefTestrResults(Meter meter, int channelFlag, int index) {
        Meter.CreepResult creepResult;

        switch (channelFlag) {
            case 0: {
                creepResult = (Meter.CreepResult) meter.getErrorListAPPls().get(index);
                creepResult.setLastResult(null);
                creepResult.setPassTest(true);
                tableColumnError.get(meter.getId() - 1).setCellFactory(cellFactoryStartTest);
            } break;
            case 1: {
                creepResult = (Meter.CreepResult) meter.getErrorListAPMns().get(index);
                creepResult.setLastResult("");
                creepResult.setPassTest(true);
                tableColumnError.get(meter.getId() - 1).setCellFactory(cellFactoryStartTest);
            } break;
            case 2: {
                creepResult = (Meter.CreepResult) meter.getErrorListRPPls().get(index);
                creepResult.setLastResult("");
                creepResult.setPassTest(true);
                tableColumnError.get(meter.getId() - 1).setCellFactory(cellFactoryStartTest);
            } break;
            case 3: {
                creepResult = (Meter.CreepResult) meter.getErrorListRPMns().get(index);
                creepResult.setLastResult("");
                creepResult.setPassTest(true);
                tableColumnError.get(meter.getId() - 1).setCellFactory(cellFactoryStartTest);
            } break;
        }
    }

    //В зависимости от направления тока переносит время провала теста в нужную строку
    private void addTestTimeAndFail(Meter meter, int channelFlag, String timeFail, int countResult, List<TableColumn<Meter.CommandResult, String>> columnList) {
        Meter.CreepResult commandResult;

        switch (channelFlag) {
            case  0: {
                commandResult = (Meter.CreepResult) meter.getErrorListAPPls().get(index);
                commandResult.setTimeToAllTest(timeFail);
                commandResult.setPassTest(false);
                columnList.get(meter.getId() - 1).setCellFactory(cellFactoryEndTest);
                commandResult.setLastResult(timeFail + " " + "-");
                commandResult.getResults()[countResult] = commandResult.getLastResult();
            }break;

            case  1: {
                commandResult = (Meter.CreepResult) meter.getErrorListAPMns().get(index);
                commandResult.setTimeToAllTest(timeFail);
                commandResult.setPassTest(false);
                columnList.get(meter.getId() - 1).setCellFactory(cellFactoryEndTest);
                commandResult.setLastResult(timeFail + " " + "-");
                commandResult.getResults()[countResult] = commandResult.getLastResult();
            }break;

            case  2: {
                commandResult = (Meter.CreepResult) meter.getErrorListRPPls().get(index);
                commandResult.setTimeToAllTest(timeFail);
                commandResult.setPassTest(false);
                columnList.get(meter.getId() - 1).setCellFactory(cellFactoryEndTest);
                commandResult.setLastResult(timeFail + " " + "-");
                commandResult.getResults()[countResult] = commandResult.getLastResult();
            }break;

            case  3: {
                commandResult = (Meter.CreepResult) meter.getErrorListRPMns().get(index);
                commandResult.setTimeToAllTest(timeFail);
                commandResult.setPassTest(false);
                columnList.get(meter.getId() - 1).setCellFactory(cellFactoryEndTest);
                commandResult.setLastResult(timeFail + " " + "-");
                commandResult.getResults()[countResult] = commandResult.getLastResult();

            }break;
        }
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

    public void setTableColumnError(List<TableColumn<Meter.CommandResult, String>> tableColumnError) {
        this.tableColumnError = tableColumnError;
    }

    public void setTimeForTest(long timeForTest) {
        this.timeForTest = timeForTest;
    }

}
