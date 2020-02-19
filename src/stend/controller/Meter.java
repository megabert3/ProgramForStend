package stend.controller;

import javafx.beans.property.SimpleStringProperty;
import stend.controller.Commands.*;

import java.util.ArrayList;
import java.util.List;

public class Meter {

    private int id;

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
                    errorListAPPls.add(new CreepResult(name));

                } else if (command instanceof StartCommand) {
                    errorListAPPls.add(new StartResult(name));

                } else if (command instanceof RTCCommand) {
                    errorListAPPls.add(new RTCResult(name));
                }
            } break;
            case 1: {
                if (command instanceof ErrorCommand) {
                    errorListAPMns.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    errorListAPMns.add(new CreepResult(name));

                } else if (command instanceof StartCommand) {
                    errorListAPMns.add(new StartResult(name));

                } else if (command instanceof RTCCommand) {
                    errorListAPMns.add(new RTCResult(name));
                }
            } break;
            case 2: {
                if (command instanceof ErrorCommand) {
                    errorListRPPls.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    errorListRPPls.add(new CreepResult(name));

                } else if (command instanceof StartCommand) {
                    errorListRPPls.add(new StartResult(name));

                } else if (command instanceof RTCCommand) {
                    errorListRPPls.add(new RTCResult(name));
                }
            } break;
            case 3: {
                if (command instanceof ErrorCommand) {
                    errorListRPMns.add(new ErrorResult(name));

                } else if (command instanceof CreepCommand) {
                    errorListRPMns.add(new CreepResult(name));

                } else if (command instanceof StartCommand) {
                    errorListRPMns.add(new StartResult(name));

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
    }

    //Класс для записи результата исполнения StartCommnad
    private class RTCResult extends CommandResult {

        RTCResult(String name) {
            super.setNameCommand(name);
            super.setLastResult(name);
        }
    }
}
