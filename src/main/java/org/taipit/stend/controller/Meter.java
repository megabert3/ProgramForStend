package org.taipit.stend.controller;

import javafx.beans.property.SimpleStringProperty;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Meter implements Serializable{
    /**
     * Для восстановления результатов прошлого теста, при создании
     * новых объектов CommandResult сравнивать хэш код со старыми результатами и если что возвращать старый результат
     */

    private int id;

    private String unicalID;

    //Максимальный ток
    private double Imax;

    //Базовый ток
    private double Ib;

    //Номинальное напряжение
    private double Un;

    //Номиальная частота
    private double Fn;

    private String InomImax;

    //Тип измерительного элемента счётчика шунт/трансформатор
    private boolean typeOfMeasuringElementShunt;

    //Тип сети трёхфазная или однофазная
    private boolean typeCircuitThreePhase;

    //Класс точности активной энергии
    private double accuracyClassAP;

    //Класс точночти реактивной энергии
    private double accuracyClassRP;

    private String temperature;

    private String humidity;

    private String operator;

    private String controller;

    private String witness;

    private String verificationDate;

    private String factoryManufacturer;

    private String batchNo;

    private String lastModifiedDate;

    //Пример: Однотарифный однофазный
    private String typeMeter;

    //Установлен ли счётчик на посадочное место
    private boolean activeSeat = true;

    //Записать результаты в базу
    private boolean saveResults = true;

    //Количество измерений погрешности
    private int amountMeasur;

    //Необходимо для количетва вычислений
    private int errorResultChange;

    //Количество импульсов полученных в результате теста чувств. сам.
    private int amountImn;

    //Серийный номер счётчика
    private String serNoMeter;

    //Константа активной энергии счётчика
    private String constantMeterAP;

    //Константа реактивной энергии счётчика
    private String constantMeterRP;

    //Серийный номер счётчика
    private String modelMeter;

    //Результаты тестов
    //-----------------------------------------------------------------------

    //Общий результат
    private Boolean finalAllTestResult;
    //Самоход
    private Boolean creepTest = null;

    //Чувствительность
    private Boolean startTestAPPls = null;
    private Boolean startTestAPMns = null;
    private Boolean startTestRPPls = null;
    private Boolean startTestRPMns = null;

    //Точность хода часов
    private Boolean RTCTest = null;

    //Изоляция
    private Boolean insulationTest = null;

    //Внешний вид
    private Boolean appearensTest = null;

    //Проверка счётного механизма
    private Boolean constantTestAPPls = null;
    private Boolean constantTestAPMns = null;
    private Boolean constantTestRPPls = null;
    private Boolean constantTestRPMns = null;

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
                        creepResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()));
                    } else {

                        creepResult.setLastResult("N" + getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListAPPls.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()));
                    }else {
                        startResult.setLastResult("N" + getTime(((StartCommand) command).getTimeForTest()));
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
                        creepResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()));
                    } else {
                        creepResult.setLastResult("N" + getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListAPMns.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()));
                    }else {
                        startResult.setLastResult("N" + getTime(((StartCommand) command).getTimeForTest()));
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
                        creepResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()));
                    } else {
                        creepResult.setLastResult("N" + getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListRPPls.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()));
                    }else {
                        startResult.setLastResult("N" + getTime(((StartCommand) command).getTimeForTest()));
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
                        creepResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()));
                    } else {
                        creepResult.setLastResult("N" + getTime(((CreepCommand) command).getTimeForTest()));
                    }

                    errorListRPMns.add(creepResult);

                } else if (command instanceof StartCommand) {

                    StartResult startResult = new StartResult(name);

                    //Отображаю время теста
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult("N" + getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()));
                    }else {
                        startResult.setLastResult("N" + getTime(((StartCommand) command).getTimeForTest()));
                    }

                    errorListRPMns.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListRPMns.add(new RTCResult(name));
                }
            } break;
        }
    }

    public Meter.CommandResult returnResultCommand (int index, int energyPulseChanel) {

        switch (energyPulseChanel) {
            case 0: {
                return errorListAPPls.get(index);
            }

            case 1: {
                return errorListAPMns.get(index);
            }

            case 2: {
                return errorListRPPls.get(index);
            }

            case 3: {
                return errorListRPMns.get(index);
            }
        }
        return null;
    }

    //Установка результата теста
    public Meter.CommandResult setResultsInErrorList(int index, int resultNo, String error, int energyPulseChanel) {
        CommandResult commandResult;
        switch (energyPulseChanel) {
            case 0: {
                commandResult = errorListAPPls.get(index);
                commandResult.setLastResulString(error);
                commandResult.getResults()[resultNo] = error;
                return commandResult;
            }

            case 1: {
                commandResult = errorListAPMns.get(index);
                commandResult.setLastResulString(error);
                commandResult.getResults()[resultNo] = error;
                return commandResult;
            }

            case 2: {
                commandResult = errorListRPPls.get(index);
                commandResult.setLastResulString(error);
                commandResult.getResults()[resultNo] = error;
                return commandResult;
            }

            case 3: {
                commandResult = errorListRPMns.get(index);
                commandResult.setLastResulString(error);
                commandResult.getResults()[resultNo] = error;
                return commandResult;
            }
        }
        return null;
    }

    //окончательный вердикт
    public Boolean meterPassOrNotAlltests() {
        Boolean yesOrNo = null;

        if (!errorListAPPls.isEmpty()) {
            for (CommandResult commandResult : errorListAPPls) {
                if (commandResult.isPassTest() != null) {
                    if (!commandResult.isPassTest()) {
                        return false;
                    } else {
                        yesOrNo = true;
                    }
                }
            }
        }

        if (!errorListAPMns.isEmpty()) {
            for (CommandResult commandResult : errorListAPMns) {
                if (commandResult.isPassTest() != null) {
                    if (!commandResult.isPassTest()) {
                        return false;
                    } else {
                        yesOrNo = true;
                    }
                }
            }
        }

        if (!errorListRPPls.isEmpty()) {
            for (CommandResult commandResult : errorListRPPls) {
                if (commandResult.isPassTest() != null) {
                    if (!commandResult.isPassTest()) {
                        return false;
                    } else {
                        yesOrNo = true;
                    }
                }
            }
        }

        if (!errorListRPMns.isEmpty()) {
            for (CommandResult commandResult : errorListRPMns) {
                if (commandResult.isPassTest() != null) {
                    if (!commandResult.isPassTest()) {
                        return false;
                    } else {
                        yesOrNo = true;
                    }
                }
            }
        }
        return yesOrNo;
    }

    //Переводит миллисекунды в формат hh:mm:ss
    private String getTime(long mlS){
        long s = mlS / 1000;
        long hours = s / 3600;
        long minutes = (s % 3600) / 60;
        long seconds = s % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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

    public void setActiveSeat(boolean activeSeat) {
        this.activeSeat = activeSeat;
    }

    public boolean isActiveSeat() {
        return activeSeat;
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

    public int getErrorResultChange() {
        return errorResultChange;
    }

    public void setErrorResultChange(int errorResultChange) {
        this.errorResultChange = errorResultChange;
    }

    public void setAmountImn(int amountImn) {
        this.amountImn = amountImn;
    }

    public int getAmountImn() {
        return amountImn;
    }

    public Boolean getCreepTest() {
        return creepTest;
    }

    public void setCreepTest(Boolean creepTest) {
        this.creepTest = creepTest;
    }

    public Boolean getStartTestAPPls() {
        return startTestAPPls;
    }

    public void setStartTestAPPls(Boolean startTestAPPls) {
        this.startTestAPPls = startTestAPPls;
    }

    public Boolean getStartTestAPMns() {
        return startTestAPMns;
    }

    public void setStartTestAPMns(Boolean startTestAPMns) {
        this.startTestAPMns = startTestAPMns;
    }

    public Boolean getStartTestRPPls() {
        return startTestRPPls;
    }

    public void setStartTestRPPls(Boolean startTestRPPls) {
        this.startTestRPPls = startTestRPPls;
    }

    public Boolean getStartTestRPMns() {
        return startTestRPMns;
    }

    public void setStartTestRPMns(Boolean startTestRPMns) {
        this.startTestRPMns = startTestRPMns;
    }

    public Boolean getRTCTest() {
        return RTCTest;
    }

    public void setRTCTest(Boolean RTCTest) {
        this.RTCTest = RTCTest;
    }

    public Boolean getInsulationTest() {
        return insulationTest;
    }

    public void setInsulationTest(Boolean insulationTest) {
        this.insulationTest = insulationTest;
    }

    public Boolean getAppearensTest() {
        return appearensTest;
    }

    public void setAppearensTest(Boolean appearensTest) {
        this.appearensTest = appearensTest;
    }

    public boolean isSaveResults() {
        return saveResults;
    }

    public void setSaveResults(boolean saveResults) {
        this.saveResults = saveResults;
    }

    public Boolean getConstantTestAPPls() {
        return constantTestAPPls;
    }

    public void setConstantTestAPPls(Boolean constantTestAPPls) {
        this.constantTestAPPls = constantTestAPPls;
    }

    public Boolean getConstantTestAPMns() {
        return constantTestAPMns;
    }

    public void setConstantTestAPMns(Boolean constantTestAPMns) {
        this.constantTestAPMns = constantTestAPMns;
    }

    public Boolean getConstantTestRPPls() {
        return constantTestRPPls;
    }

    public void setConstantTestRPPls(Boolean constantTestRPPls) {
        this.constantTestRPPls = constantTestRPPls;
    }

    public Boolean getConstantTestRPMns() {
        return constantTestRPMns;
    }

    public void setConstantTestRPMns(Boolean constantTestRPMns) {
        this.constantTestRPMns = constantTestRPMns;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return humidity;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getFactoryManufacturer() {
        return factoryManufacturer;
    }

    public void setFactoryManufacturer(String factoryManufacturer) {
        this.factoryManufacturer = factoryManufacturer;
    }

    public Boolean getFinalAllTestResult() {
        return finalAllTestResult;
    }

    public void setFinalAllTestResult(Boolean finalAllTestResult) {
        this.finalAllTestResult = finalAllTestResult;
    }

    public String getUnicalID() {
        return unicalID;
    }

    public void setUnicalID(String unicalID) {
        this.unicalID = unicalID;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getInomImax() {
        return InomImax;
    }

    public void setInomImax(String inomImax) {
        InomImax = inomImax;
    }

    //Абстрактный класс для записи результата каждой точки
    public abstract class CommandResult implements Serializable{

        //Имя команды
        String nameCommand;

        //Последний результат теста
        transient SimpleStringProperty lastResult;

        String lastResulString;

        //Верхняя граница погрешности
        String maxError;

        //Нижняя граница погрешности
        String minError;

        //Погрешность в диапазоне (прошла тест или нет)
        Boolean passTest = null;

        //10-ть последних результатов
        String[] results = new String[10];

        CommandResult() {
            this.lastResult = new SimpleStringProperty();
        }

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

        void setNameCommand(String nameCommand) {
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

        Boolean isPassTest() {
            return passTest;
        }

        public void setPassTest(boolean passTest) {
            this.passTest = passTest;
        }

        public String getLastResulString() {
            return lastResulString;
        }

        public void setLastResulString(String lastResulString) {
            this.lastResulString = lastResulString;
        }

    }

    //Класс для записи результата исполнения ErrorCommnad
    public class ErrorResult extends CommandResult implements Serializable {

        ErrorResult(String name) {
            super.setNameCommand(name);
        }
    }

    //Класс для записи результата исполнения CreepCommnad
    public class CreepResult extends CommandResult implements Serializable{

        //Время провала теста
        String timeToAllTest;

        CreepResult(String name) {
            super.setNameCommand(name);
            super.setPassTest(true);
        }

        public String getTimeToAllTest() {
            return timeToAllTest;
        }

        public void setTimeToAllTest(String timeToAllTest) {
            this.timeToAllTest = timeToAllTest;
        }
    }

    //Класс для записи результата исполнения StartCommnad
    public class StartResult extends CommandResult implements Serializable{

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
    private class RTCResult extends CommandResult implements Serializable{

        RTCResult(String name) {
            super.setNameCommand(name);
        }
    }

    private class Constant extends CommandResult implements Serializable{

        Constant(String name) {
            super.setNameCommand(name);
        }
    }
}
