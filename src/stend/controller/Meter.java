package stend.controller;

import javafx.beans.property.SimpleStringProperty;
import stend.controller.Commands.*;

import java.math.BigDecimal;
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

    //Время для теста Самоход ГОСТ
    private long timeToCreepTestGOST;

    //Время для теста Самоход
    private long timeToCreepTest;

    //Время для теста Чувствительность ГОСТ
    private long timeToStartTestGOST;

    //Время для теста Чувствительность
    private long timeToStartTest;

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


    public void createError(Commands command, int numberArrayList , String name) {
        switch (numberArrayList) {
            case 0: {
                if (command instanceof ErrorCommand) {
                    errorListAPPls.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    CreepResult creepResult = new CreepResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForGOSTTest()));
                    } else {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForTest()));
                    }

                    errorListAPPls.add(creepResult);

                } else if (command instanceof StartCommand) {
                    StartResult startResult = new StartResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForGOSTTest()));
                    } else {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForTest()));
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

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForGOSTTest()));
                    } else {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForTest()));
                    }

                    errorListAPPls.add(creepResult);

                } else if (command instanceof StartCommand) {
                    StartResult startResult = new StartResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForGOSTTest()));
                    } else {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForTest()));
                    }
                    errorListAPPls.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListAPMns.add(new RTCResult(name));
                }
            } break;
            case 2: {
                if (command instanceof ErrorCommand) {
                    errorListRPPls.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    CreepResult creepResult = new CreepResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForGOSTTest()));
                    } else {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForTest()));
                    }

                    errorListAPPls.add(creepResult);

                } else if (command instanceof StartCommand) {
                    StartResult startResult = new StartResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForGOSTTest()));
                    } else {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForTest()));
                    }
                    errorListAPPls.add(startResult);

                } else if (command instanceof RTCCommand) {
                    errorListRPPls.add(new RTCResult(name));
                }
            } break;
            case 3: {
                if (command instanceof ErrorCommand) {
                    errorListRPMns.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    CreepResult creepResult = new CreepResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((CreepCommand) command).isGostTest()) {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForGOSTTest()));
                    } else {
                        creepResult.setLastResult(creepResult.getTime(((CreepCommand) command).initTimeForTest()));
                    }

                    errorListAPPls.add(creepResult);

                } else if (command instanceof StartCommand) {
                    StartResult startResult = new StartResult(name);

                    //Устанавливаю отображение времени на тест для команды на гост и введённое пользователем время
                    if (((StartCommand) command).isGostTest()) {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForGOSTTest()));
                    } else {
                        startResult.setLastResult(startResult.getTime(((StartCommand) command).initTimeForTest()));
                    }
                    errorListAPPls.add(startResult);

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

    //Формула для расчёта времени теста на самоход по ГОСТУ
    public long initTimeForCreepTestGOST(boolean threePhaseMeter, int constMeterForTest, double ratedVolt, double maxCurrMeter) {
        int amountMeasElem;

        if (threePhaseMeter) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

        double formulaResult = 600000000 / (constMeterForTest * amountMeasElem * ratedVolt * maxCurrMeter);

        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return  ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    //Расчёт времени теста на самоход введённая пользователем
    public long initTimeForCreepTest(String userTimeTest) {
        String[] timearr = userTimeTest.split(":");
        String hours = timearr[0];
        String mins = timearr[1];
        String seks = timearr[2];

        return ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;
    }


    //Расчётная формула времени теста на чувствительность по ГОСТ
    public long initTimeForStartGOSTTest(double accuracyClass, int constMeterForTest) {
        int amountMeasElem;
        double ratedCurr;

        if (typeCircuitThreePhase) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

        if (typeOfMeasuringElementShunt) {
            if (accuracyClass == 1.0) {
                ratedCurr = Ib * 0.002;
            } else {
                ratedCurr = Ib * 0.001;
            }
        } else {
            ratedCurr = Ib * 0.004;
        }

        double formulaResult = 2.3 * (60000 / (constMeterForTest * amountMeasElem * Un * ratedCurr));

        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    //Расчёт времени теста на чувствительность исходя из параметров введённых пользователем
    public long initTimeForStartTest(String userTimeTest) {
        String[] timearr = userTimeTest.split(":");
        String hours = timearr[0];
        String mins = timearr[1];
        String seks = timearr[2];

        return ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;
    }


    private String getTime(long mlS){
        long hours = mlS * 1000 / 3600;
        long minutes = ((mlS * 1000) % 3600) / 60;
        long seconds = (mlS * 1000) % 60;
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
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
            super.setLastResult(name);
        }
    }

    //Класс для записи результата исполнения CreepCommnad
    public class CreepResult extends CommandResult {

        //Время провала теста
        long timeToFail;

        CreepResult(String name) {
            super.setNameCommand(name);
            super.setPassTest(true);
            super.setLastResult(name);
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
            super.setLastResult(name);
        }

        public long getTimeToPass() {
            return timeToPass;
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
            super.setLastResult(name);
        }
    }
}
