package org.taipit.stend.controller;

import javafx.beans.property.SimpleStringProperty;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;

import java.util.ArrayList;
import java.util.List;

public class Meter {

    private int id;

    //Максимальный ток
    private double Imax;

    //Базовый ток
    private double Ib;

    //Номинальное напряжение
    private double Un;

    //Номиальная частота
    private double Fn;

    //Тип измерительного элемента счётчика шунт/трансформатор
    private boolean typeOfMeasuringElementShunt;

    //Тип сети трёхфазная или однофазная
    private boolean typeCircuitThreePhase;

    //Класс точности активной энергии
    private double accuracyClassAP;

    //Класс точночти реактивной энергии
    private double accuracyClassRP;

    private String operator;

    private String controller;

    private String witness;

    //Локальный массив ошибок нужен для быстрого теста
    private String[] localErrors = new String[10];

    //Установлен ли счётчик на посадочное место
    private boolean active = true;

    //Количество измерений погрешности
    private int amountMeasur;

    //Количество импульсов полученных в результате теста чувств. сам.
    private int amountImn;

    //Поиск метки сам. чувств.
    boolean searchMark;

    //Серийный номер счётчика
    private String serNoMeter;

    //Константа активной энергии счётчика
    private String constantMeterAP;

    //Константа реактивной энергии счётчика
    private String constantMeterRP;

    //Серийный номер счётчика
    private String modelMeter;


    //Лист с ошибками
    private List<CommandResult> errorListAPPls = new ArrayList<>();
    private List<CommandResult> errorListAPMns = new ArrayList<>();
    private List<CommandResult> errorListRPPls = new ArrayList<>();
    private List<CommandResult> errorListRPMns = new ArrayList<>();


    public void createError(Commands command, int numberArrayList , String name, TestErrorTableFrameController testErrorTableFrameController) {
        switch (numberArrayList) {
            case 0: {
                if (command instanceof ErrorCommand) {
                    errorListAPPls.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {

                    CreepResult creepResult = new CreepResult(name);

                    //Отображаю время теста
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()));
                    } else {

                        creepResult.setLastResult(getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListAPPls.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()));
                    }else {
                        startResult.setLastResult(getTime(((StartCommand) command).getTimeForTest()));
                    }

                    errorListAPPls.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListAPPls.add(new RTCResult(name));
                }
            } break;
            case 1: {
                if (command instanceof ErrorCommand) {
                    errorListAPMns.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    CreepResult creepResult = new CreepResult(name);

                    //Отображаю время теста
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()));
                    } else {
                        creepResult.setLastResult(getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListAPMns.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()));
                    }else {
                        startResult.setLastResult(getTime(((StartCommand) command).getTimeForTest()));
                    }

                    errorListAPMns.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListAPMns.add(new RTCResult(name));
                }
            } break;
            case 2: {
                if (command instanceof ErrorCommand) {
                    errorListRPPls.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {

                    CreepResult creepResult = new CreepResult(name);

                    //Отображаю время теста
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()));
                    } else {
                        creepResult.setLastResult(getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListRPPls.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()));
                    }else {
                        startResult.setLastResult(getTime(((StartCommand) command).getTimeForTest()));
                    }

                    errorListRPPls.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListRPPls.add(new RTCResult(name));
                }
            } break;
            case 3: {
                if (command instanceof ErrorCommand) {
                    errorListRPMns.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {

                    CreepResult creepResult = new CreepResult(name);

                    //Отображаю время теста
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()));
                    } else {
                        creepResult.setLastResult(getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListRPMns.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()));
                    }else {
                        startResult.setLastResult(getTime(((StartCommand) command).getTimeForTest()));
                    }

                    errorListRPMns.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListRPMns.add(new RTCResult(name));
                }
            } break;
        }
    }

    //Установка результата теста
    public void setResultsInErrorList(int index, int resultNo, String error, int energyPulseChanel) {
        CommandResult commandResult;
        switch (energyPulseChanel) {
            case 0: {
                commandResult = errorListAPPls.get(index);
                commandResult.setLastResult(error);
                commandResult.getResults()[resultNo] = error;
            }break;

            case 1: {
                commandResult = errorListAPMns.get(index);
                commandResult.setLastResult(error);
                commandResult.getResults()[resultNo] = error;
            }break;

            case 2: {
                commandResult = errorListRPPls.get(index);
                commandResult.setLastResult(error);
                commandResult.getResults()[resultNo] = error;
            }break;

            case 3: {
                commandResult = errorListRPMns.get(index);
                commandResult.setLastResult(error);
                commandResult.getResults()[resultNo] = error;
            }break;
        }
    }

    private String getTime(long mlS){
        long s = mlS / 1000;
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    //Данная команда необходима для тестов самоход и чувствительность
    public boolean run(int pulseValue, StendDLLCommands stendDLLCommands) {
        if (amountImn < pulseValue) {
            if (!searchMark) {
                stendDLLCommands.searchMark(id);
                searchMark = true;
            } else {
                if (stendDLLCommands.searchMarkResult(id)) {
                    amountImn++;
                    searchMark = false;
                }
            }
        } else return false;
        return true;
    }

    public String[] getLocalErrors() {
        return localErrors;
    }

    public String getConstantMeterAP() {
        return constantMeterAP;
    }

    public String getConstantMeterRP() {
        return constantMeterRP;
    }

    public String getModelMeter() {
        return modelMeter;
    }

    public String getSerNoMeter() {
        return serNoMeter;
    }

    public void setConstantMeterRP(String constantMeterRP) {
        this.constantMeterRP = constantMeterRP;
    }

    public void setModelMeter(String modelMeter) {
        this.modelMeter = modelMeter;
    }

    public void setConstantMeterAP(String constantMeterAP) {
        this.constantMeterAP = constantMeterAP;
    }

    public void setSerNoMeter(String serNoMeter) {
        this.serNoMeter = serNoMeter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }

    public List<CommandResult> getErrorListAPPls() {
        return errorListAPPls;
    }

    public List<CommandResult> getErrorListAPMns() {
        return errorListAPMns;
    }

    public List<CommandResult> getErrorListRPPls() {
        return errorListRPPls;
    }

    public List<CommandResult> getErrorListRPMns() {
        return errorListRPMns;
    }

    public int getAmountMeasur() {
        return amountMeasur;
    }

    public void setAmountMeasur(int amountMeasur) {
        this.amountMeasur = amountMeasur;
    }

    public void setImax(double imax) {
        Imax = imax;
    }

    public void setIb(double ib) {
        Ib = ib;
    }

    public void setUn(double un) {
        Un = un;
    }

    public void setFn(double fn) {
        Fn = fn;
    }

    public void setTypeOfMeasuringElementShunt(boolean typeOfMeasuringElementShunt) {
        this.typeOfMeasuringElementShunt = typeOfMeasuringElementShunt;
    }

    public void setTypeCircuitThreePhase(boolean typeCircuitThreePhase) {
        this.typeCircuitThreePhase = typeCircuitThreePhase;
    }

    public void setAccuracyClassAP(double accuracyClassAP) {
        this.accuracyClassAP = accuracyClassAP;
    }

    public void setAccuracyClassRP(double accuracyClassRP) {
        this.accuracyClassRP = accuracyClassRP;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }

    public double getImax() {
        return Imax;
    }

    public double getIb() {
        return Ib;
    }

    public double getUn() {
        return Un;
    }

    public double getFn() {
        return Fn;
    }

    public boolean isTypeOfMeasuringElementShunt() {
        return typeOfMeasuringElementShunt;
    }

    public boolean isTypeCircuitThreePhase() {
        return typeCircuitThreePhase;
    }

    public double getAccuracyClassAP() {
        return accuracyClassAP;
    }

    public double getAccuracyClassRP() {
        return accuracyClassRP;
    }

    public String getOperator() {
        return operator;
    }

    public String getController() {
        return controller;
    }

    public String getWitness() {
        return witness;
    }


    //Абстрактный класс для записи результата каждой точки
    public abstract class CommandResult {

        //Имя команды
        String nameCommand;

        //Последний результат теста
        SimpleStringProperty lastResult = new SimpleStringProperty();

        //Верхняя граница погрешности
        String maxError;

        //Нижняя граница погрешности
        String minError;

        //10-ть последних результатов
        String[] results = new String[10];

        //Погрешность в диапазоне (прошла тест или нет)
        boolean passTest;

        public String getLastResult() {
            return lastResult.get();
        }

        public SimpleStringProperty lastResultProperty() {
            return lastResult;
        }

        public void setLastResult(String lastResult) {
            this.lastResult.set(lastResult);
        }

        public String getNameCommand() {
            return nameCommand;
        }

        public String[] getResults() {
            return results;
        }

        public void setNameCommand(String nameCommand) {
            this.nameCommand = nameCommand;
        }

        public void setResults(String[] results) {
            this.results = results;
        }

        public void setMaxError(String maxError) {
            this.maxError = maxError;
        }

        public void setMinError(String minError) {
            this.minError = minError;
        }

        public String getMaxError() {
            return maxError;
        }

        public String getMinError() {
            return minError;
        }

        public boolean isPassTest() {
            return passTest;
        }

        public void setPassTest(boolean passTest) {
            this.passTest = passTest;
        }

    }

    //Класс для записи результата исполнения ErrorCommnad
    private class ErrorResult extends CommandResult {

        ErrorResult(String name) {
            super.setNameCommand(name);
        }
    }

    //Класс для записи результата исполнения CreepCommnad
    public class CreepResult extends CommandResult {

        //Время провала теста
        long timeToFail;

        CreepResult(String name) {
            super.setNameCommand(name);
            super.setPassTest(true);
        }

        public long getTimeToFail() {
            return timeToFail;
        }

        public void setTimeToFail(long timeToFail) {
            this.timeToFail = timeToFail;
        }
    }

    //Класс для записи результата исполнения StartCommnad
    public class StartResult extends CommandResult {

        //Время прохождения теста
        long timeToPass;

        StartResult(String name) {
            super.setNameCommand(name);
        }

        public void setTimeToPass(long timeToPass) {
            this.timeToPass = timeToPass;
        }

        private String getTime(long mlS){
            long hours = mlS * 1000 / 3600;
            long minutes = ((mlS * 1000) % 3600) / 60;
            long seconds = (mlS * 1000) % 60;
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
    }

    //Класс для записи результата исполнения StartCommnad
    private class RTCResult extends CommandResult {

        RTCResult(String name) {
            super.setNameCommand(name);
        }
    }
}
