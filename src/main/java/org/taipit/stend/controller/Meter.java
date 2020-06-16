package org.taipit.stend.controller;

import javafx.beans.property.SimpleStringProperty;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Meter implements Serializable{
    /**
     * Для восстановления результатов прошлого теста, при создании
     * новых объектов CommandResult сравнивать хэш код со старыми результатами и если что возвращать старый результат
     */

    private int id;

    private String unicalID;

    //Максимальный ток
    private float Imax;

    //Базовый ток
    private float Ib;

    //Номинальное напряжение
    private float Un;

    //Номиальная частота
    private float Fn;

    private String InomImax;

    //Тип измерительного элемента счётчика шунт/трансформатор
    private boolean typeOfMeasuringElementShunt;

    //Тип сети трёхфазная или однофазная
    private boolean typeCircuitThreePhase;

    //Класс точности активной энергии
    private float accuracyClassAP;

    //Класс точночти реактивной энергии
    private float accuracyClassRP;

    private float temperature;

    private float humidity;

    private String operator;

    private String controller;

    private String witness;

    private String verificationDate;

    private String factoryManufacturer;

    private String batchNo;

    private String lastModifiedDate;

    private String testMode;

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

    //Модель счётчика
    private String modelMeter;

    //Результаты тестов
    //-----------------------------------------------------------------------

    //Общий результат
    private Boolean finalAllTestResult;

    //Самоход
    private CreepResult creepTest = null;

    //Чувствительность
    private StartResult startTestAPPls = null;
    private StartResult startTestAPMns = null;
    private StartResult startTestRPPls = null;
    private StartResult startTestRPMns = null;

    //Точность хода часов
    private RTCResult RTCTest = null;

    //Изоляция
    private InsulationResult insulationTest = new InsulationResult("INS");

    //Внешний вид
    private AppearensResult appearensTest = new AppearensResult("APR");

    //Проверка счётного механизма
    private ConstantResult constantTestAPPls = null;
    private ConstantResult constantTestAPMns = null;
    private ConstantResult constantTestRPPls = null;
    private ConstantResult constantTestRPMns = null;

    //Лист с ошибками
    private List<CommandResult> errorListAPPls = new ArrayList<>();
    private List<CommandResult> errorListAPMns = new ArrayList<>();
    private List<CommandResult> errorListRPPls = new ArrayList<>();
    private List<CommandResult> errorListRPMns = new ArrayList<>();

    public void createError(Commands command, int numberArrayList , String id, TestErrorTableFrameController testErrorTableFrameController) {
        switch (numberArrayList) {
            case 0: {
                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    errorListAPPls.add(new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax()));

                } else if (command instanceof CreepCommand) {
                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {
                        errorListAPPls.add(new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()), String.valueOf(creepCommand.getPulseValue())));
                    } else {
                        errorListAPPls.add(new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue())));
                    }

                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {
                        errorListAPPls.add(new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()), String.valueOf(startCommand.getPulseValue())));
                    } else {
                        errorListAPPls.add(new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue())));
                    }

                } else if (command instanceof RTCCommand) {
                    RTCCommand rtcCommand = (RTCCommand) command;
                    errorListAPPls.add(new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), rtcCommand.getFreg()));

                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;
                    errorListAPPls.add(new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax())));

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;
                    errorListAPPls.add(new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax()));
                }
            } break;

            case 1: {
                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    errorListAPMns.add(new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax()));

                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {
                        errorListAPMns.add(new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()), String.valueOf(creepCommand.getPulseValue())));
                    } else {
                        errorListAPMns.add(new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue())));
                    }

                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {
                        errorListAPMns.add(new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()), String.valueOf(startCommand.getPulseValue())));
                    } else {
                        errorListAPMns.add(new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue())));
                    }

                } else if (command instanceof RTCCommand) {

                    RTCCommand rtcCommand = (RTCCommand) command;
                    errorListAPMns.add(new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), rtcCommand.getFreg()));

                } else if (command instanceof ConstantCommand) {

                    ConstantCommand constantCommand = (ConstantCommand) command;
                    errorListAPMns.add(new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax())));

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;
                    errorListAPMns.add(new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax()));
                }
            } break;

            case 2: {
                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    errorListRPPls.add(new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax()));

                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {
                        errorListRPPls.add(new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()), String.valueOf(creepCommand.getPulseValue())));
                    } else {
                        errorListRPPls.add(new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue())));
                    }

                } else if (command instanceof StartCommand) {

                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {
                        errorListRPPls.add(new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()), String.valueOf(startCommand.getPulseValue())));
                    } else {
                        errorListRPPls.add(new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue())));
                    }

                } else if (command instanceof RTCCommand) {

                    RTCCommand rtcCommand = (RTCCommand) command;
                    errorListRPPls.add(new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), rtcCommand.getFreg()));

                } else if (command instanceof ConstantCommand) {

                    ConstantCommand constantCommand = (ConstantCommand) command;
                    errorListRPPls.add(new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax())));

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;
                    errorListRPPls.add(new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax()));
                }
            } break;

            case 3: {
                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    errorListRPMns.add(new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax()));

                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {
                        errorListRPMns.add(new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()), String.valueOf(creepCommand.getPulseValue())));
                    } else {
                        errorListRPMns.add(new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue())));
                    }

                } else if (command instanceof StartCommand) {

                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {
                        errorListRPMns.add(new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()), String.valueOf(startCommand.getPulseValue())));
                    } else {
                        errorListRPMns.add(new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue())));
                    }

                } else if (command instanceof RTCCommand) {

                    RTCCommand rtcCommand = (RTCCommand) command;
                    errorListRPMns.add(new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), rtcCommand.getFreg()));

                } else if (command instanceof ConstantCommand) {

                    ConstantCommand constantCommand = (ConstantCommand) command;
                    errorListRPMns.add(new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax())));

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;
                    errorListRPMns.add(new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax()));
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
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mlS),
                TimeUnit.MILLISECONDS.toMinutes(mlS) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(mlS) % TimeUnit.MINUTES.toSeconds(1));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Meter meter = (Meter) obj;

        if (this.Un != meter.Un) return false;
        if (this.Imax != meter.Imax) return false;
        if (this.Ib != meter.Ib) return false;
        if (this.accuracyClassAP != meter.accuracyClassAP) return false;
        if (!this.modelMeter.equals(meter.modelMeter)) return false;
        if (!this.factoryManufacturer.equals(meter.factoryManufacturer)) return false;
        if (this.temperature != meter.temperature) return false;
        if (this.humidity != meter.humidity) return false;
        if (!this.operator.equals(meter.operator)) return false;
        if (!this.controller.equals(meter.controller)) return false;
        if (!this.witness.equals(meter.witness)) return false;
        if (!this.testMode.equals(meter.testMode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (Un + Imax + Ib + accuracyClassAP + modelMeter.hashCode() + factoryManufacturer.hashCode() +
                        temperature + humidity + operator.hashCode() + controller.hashCode() + witness.hashCode() + testMode.hashCode() * 31);
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

    public void setImax(float imax) {
        Imax = imax;
    }

    public void setIb(float ib) {
        Ib = ib;
    }

    public void setUn(float un) {
        Un = un;
    }

    public void setFn(float fn) {
        Fn = fn;
    }

    public void setTypeOfMeasuringElementShunt(boolean typeOfMeasuringElementShunt) {
        this.typeOfMeasuringElementShunt = typeOfMeasuringElementShunt;
    }

    public void setTypeCircuitThreePhase(boolean typeCircuitThreePhase) {
        this.typeCircuitThreePhase = typeCircuitThreePhase;
    }

    public void setAccuracyClassAP(float accuracyClassAP) {
        this.accuracyClassAP = accuracyClassAP;
    }

    public void setAccuracyClassRP(float accuracyClassRP) {
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

    public float getImax() {
        return Imax;
    }

    public float getIb() {
        return Ib;
    }

    public float getUn() {
        return Un;
    }

    public float getFn() {
        return Fn;
    }

    public boolean isTypeOfMeasuringElementShunt() {
        return typeOfMeasuringElementShunt;
    }

    public boolean isTypeCircuitThreePhase() {
        return typeCircuitThreePhase;
    }

    public float getAccuracyClassAP() {
        return accuracyClassAP;
    }

    public float getAccuracyClassRP() {
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

    public CreepResult getCreepTest() {
        return creepTest;
    }

    public void setCreepTest(CreepResult creepTest) {
        this.creepTest = creepTest;
    }

    public StartResult getStartTestAPPls() {
        return startTestAPPls;
    }

    public void setStartTestAPPls(StartResult startTestAPPls) {
        this.startTestAPPls = startTestAPPls;
    }

    public StartResult getStartTestAPMns() {
        return startTestAPMns;
    }

    public void setStartTestAPMns(StartResult startTestAPMns) {
        this.startTestAPMns = startTestAPMns;
    }

    public StartResult getStartTestRPPls() {
        return startTestRPPls;
    }

    public void setStartTestRPPls(StartResult startTestRPPls) {
        this.startTestRPPls = startTestRPPls;
    }

    public StartResult getStartTestRPMns() {
        return startTestRPMns;
    }

    public void setStartTestRPMns(StartResult startTestRPMns) {
        this.startTestRPMns = startTestRPMns;
    }

    public RTCResult getRTCTest() {
        return RTCTest;
    }

    public void setRTCTest(RTCResult RTCTest) {
        this.RTCTest = RTCTest;
    }

    public InsulationResult getInsulationTest() {
        return insulationTest;
    }

    public void setInsulationTest(InsulationResult insulationTest) {
        this.insulationTest = insulationTest;
    }

    public AppearensResult getAppearensTest() {
        return appearensTest;
    }

    public void setAppearensTest(AppearensResult appearensTest) {
        this.appearensTest = appearensTest;
    }

    public boolean isSaveResults() {
        return saveResults;
    }

    public void setSaveResults(boolean saveResults) {
        this.saveResults = saveResults;
    }

    public ConstantResult getConstantTestAPPls() {
        return constantTestAPPls;
    }

    public void setConstantTestAPPls(ConstantResult constantTestAPPls) {
        this.constantTestAPPls = constantTestAPPls;
    }

    public ConstantResult getConstantTestAPMns() {
        return constantTestAPMns;
    }

    public void setConstantTestAPMns(ConstantResult constantTestAPMns) {
        this.constantTestAPMns = constantTestAPMns;
    }

    public ConstantResult getConstantTestRPPls() {
        return constantTestRPPls;
    }

    public void setConstantTestRPPls(ConstantResult constantTestRPPls) {
        this.constantTestRPPls = constantTestRPPls;
    }

    public ConstantResult getConstantTestRPMns() {
        return constantTestRPMns;
    }

    public void setConstantTestRPMns(ConstantResult constantTestRPMns) {
        this.constantTestRPMns = constantTestRPMns;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getHumidity() {
        return humidity;
    }

    public void setHumidity(float humidity) {
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

    public String getTypeMeter() {
        return typeMeter;
    }

    public void setTypeMeter(String typeMeter) {
        this.typeMeter = typeMeter;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    //==============================================================================================
    //Абстрактный класс для записи результата каждой точки
    public abstract class CommandResult implements Serializable {

        CommandResult(String id) {
            this.id = id;
            this.lastResultForTabView = new SimpleStringProperty();
        }

        //Последний результат теста
        private transient SimpleStringProperty lastResultForTabView;

        //Идентификатор команды
        private String id;

        private String lastResult;

        //Верхняя граница погрешности
        private String maxError;

        //Нижняя граница погрешности
        private String minError;

        //Погрешность в диапазоне (прошла тест или нет)
        private Boolean passTest = null;

        //10-ть последних результатов
        private String[] results = new String[10];

        public String getId() {
            return id;
        }

        void setId(String id) {
            this.id = id;
        }

        public Boolean getPassTest() {
            return passTest;
        }

        public void setPassTest(Boolean passTest) {
            this.passTest = passTest;
        }

        public Boolean isPassTest() {
            return passTest;
        }

        public void setPassTest(boolean passTest) {
            this.passTest = passTest;
        }

        public String getLastResultForTabView() {
            return lastResultForTabView.get();
        }

        public SimpleStringProperty lastResultForTabViewProperty() {
            return lastResultForTabView;
        }

        public void setLastResultForTabView(String lastResultForTabView) {
            this.lastResultForTabView.set(lastResultForTabView);
        }

        public String getLastResult() {
            return lastResult;
        }

        public void setLastResult(String lastResult) {
            this.lastResult = lastResult;
        }

        public String getMaxError() {
            return maxError;
        }

        public void setMaxError(String maxError) {
            this.maxError = maxError;
        }

        public String getMinError() {
            return minError;
        }

        public void setMinError(String minError) {
            this.minError = minError;
        }

        public String[] getResults() {
            return results;
        }

        public void setResults(String[] results) {
            this.results = results;
        }
    }

    //Класс для записи результата исполнения ErrorCommnad
    public class ErrorResult extends CommandResult implements Serializable {

        ErrorResult(String id, String minError, String maxError) {
            super(id);
            super.minError = minError;
            super.maxError = maxError;
        }

        public void setResultErrorCommand(String errForTab, int resultNo, String error, boolean passOrNot) {
            super.lastResultForTabView.setValue(errForTab);
            super.results[resultNo] = error;
            super.lastResult = error;
            super.passTest = passOrNot;
        }
    }

    //Класс для записи результата исполнения CreepCommnad
    public class CreepResult extends CommandResult implements Serializable {

        //Время провала теста
        private String timeTheFailTest;

        private String timeTheTest;

        private String maxPulse;

        CreepResult(String id, String timeTheTest, String maxPulse) {
            super(id);
            this.timeTheTest = timeTheTest;
            this.maxPulse = maxPulse;
            super.lastResultForTabView.setValue("N" + timeTheTest);
        }

        public void setResultCreepCommand(String time, int resultNo, boolean passOrNot) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + time + " P");
                super.lastResult = time + " P";
                super.results[resultNo] = time + " P";
                super.passTest = true;
                creepTest = this;
            } else {
                super.lastResultForTabView.setValue("F" + time + " F");
                this.timeTheFailTest = time;
                super.lastResult = time + " F";
                super.results[resultNo] = time + " F";
                super.passTest = false;
                creepTest = this;
            }
        }

        public String getTimeTheFailTest() {
            return timeTheFailTest;
        }

        public void setTimeTheFailTest(String timeTheFailTest) {
            this.timeTheFailTest = timeTheFailTest;
        }

        public String getTimeTheTest() {
            return timeTheTest;
        }

        public String getMaxPulse() {
            return maxPulse;
        }
    }

    //Класс для записи результата исполнения StartCommnad
    public class StartResult extends CommandResult implements Serializable {

        //Время прохождения теста
        private String timeThePassTest;

        private String timeTheTest;

        private String maxPulse;

        StartResult(String id, String timeTheTest, String maxPulse) {
            super(id);
            this.timeTheTest = timeTheTest;
            this.maxPulse = maxPulse;
            super.lastResultForTabView.setValue("N" + timeTheTest);
        }

        public void setResultStartCommand(String time, int resultNo, boolean passOrNot, int chanelFlag) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + time + " P");
                this.timeThePassTest = time;
                super.lastResult = time + " P";
                super.results[resultNo] = time + " P";
                super.passTest = true;

                switch (chanelFlag) {
                    case 0: startTestAPPls =  this;
                        break;
                    case 1: startTestAPMns = this;
                        break;
                    case 2: startTestRPPls = this;
                        break;
                    case 3: startTestRPPls = this;
                        break;
                }

            } else {
                super.lastResultForTabView.setValue("F" + time + " F");
                super.lastResult = time + " F";
                super.results[resultNo] = time + " F";
                super.passTest = false;

                switch (chanelFlag) {
                    case 0: startTestAPPls = this;
                        break;
                    case 1: startTestAPMns = this;
                        break;
                    case 2: startTestRPPls = this;
                        break;
                    case 3: startTestRPPls = this;
                        break;
                }
            }
        }

        public String getTimeTheTest() {
            return timeTheTest;
        }
    }

    //Класс для записи результата исполнения StartCommnad
    public class RTCResult extends CommandResult implements Serializable {

        double freg;

        RTCResult(String id, String emin, String emax, double freg) {
            super(id);
            super.minError = emin;
            super.maxError = emax;
            this.freg = freg;
        }

        public void setResultRTCCommand(String error, int resultNo, boolean passOrNot) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + error + " P");
                super.lastResult = error;
                super.results[resultNo] = error;
                super.passTest = true;
                RTCTest = this;

            } else {
                super.lastResultForTabView.setValue("F" + error + " F");
                super.lastResult = error;
                super.results[resultNo] = error;
                super.passTest = false;
                RTCTest = this;
            }
        }

        public double getFreg() {
            return freg;
        }
    }

    public class ConstantResult extends CommandResult implements Serializable {

        private String kwMeter;

        private String kwRefMeter;

        ConstantResult(String id, String emin, String emax) {
            super(id);
            super.minError = emin;
            super.maxError = emax;
        }

        public void setResultConstantCommand(String result, int resultNo, boolean passOrNot, int chanelFlag, String kwMeter, String kwRefMeter) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + result + " P");
                super.lastResult = result;
                super.results[resultNo] = result;
                super.passTest = true;
                this.kwMeter = kwMeter;
                this.kwRefMeter = kwRefMeter;

                switch (chanelFlag) {
                    case 0: constantTestAPPls = this;
                        break;
                    case 1: constantTestAPMns = this;
                        break;
                    case 2: constantTestRPPls = this;
                        break;
                    case 3: constantTestRPMns = this;
                        break;
                }

            } else {
                super.lastResultForTabView.setValue("F" + result + " F");
                super.lastResult = result;
                super.results[resultNo] = result;
                super.passTest = false;
                this.kwMeter = kwMeter;
                this.kwRefMeter = kwRefMeter;

                switch (chanelFlag) {
                    case 0: constantTestAPPls = this;
                        break;
                    case 1: constantTestAPMns = this;
                        break;
                    case 2: constantTestRPPls = this;
                        break;
                    case 3: constantTestRPMns = this;
                        break;
                }
            }
        }

        public String getKwMeter() {
            return kwMeter;
        }

        public void setKwMeter(String kwMeter) {
            this.kwMeter = kwMeter;
        }

        public String getKwRefMeter() {
            return kwRefMeter;
        }
    }

    public class ImbUResult extends CommandResult implements Serializable {

        public ImbUResult(String id, String emin, String emax) {
            super(id);
            super.minError = emin;
            super.maxError = emax;
        }

        public void setResultImbCommand(String errForTab, int resultNo, String error, boolean passOrNot) {
            super.lastResultForTabView.setValue(errForTab);
            super.results[resultNo] = error;
            super.lastResult = error;
            super.passTest = passOrNot;
        }

    }

    public class InsulationResult extends CommandResult implements Serializable {

        InsulationResult(String id) {
            super(id);
            super.passTest = true;
        }
    }

    public class AppearensResult extends CommandResult implements Serializable {

        AppearensResult(String id) {
            super(id);
            super.passTest = true;
        }
    }
}
