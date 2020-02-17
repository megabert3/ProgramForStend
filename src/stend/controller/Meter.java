package stend.controller;

import javafx.beans.property.SimpleStringProperty;

import java.util.ArrayList;
import java.util.List;

public class Meter {

    private int id;

    //Локальный массив ошибок нужен для быстрого теста
    private String[] localErrors = new String[10];

    //Установлен ли счётчик на посадочное место
    private boolean active = true;

    private int amountMeasur;

    //Серийный номер счётчика
    private String serNoMeter;

    //Константа активной энергии счётчика
    private String constantMeterAP;

    //Константа реактивной энергии счётчика
    private String constantMeterRP;

    //Серийный номер счётчика
    private String modelMeter;

    //Лист с ошибками
    private List<Error> errorListAPPls = new ArrayList<>();
    private List<Error> errorListAPMns = new ArrayList<>();
    private List<Error> errorListRPPls = new ArrayList<>();
    private List<Error> errorListRPMns = new ArrayList<>();

    //Результат теста чувствительности к току
    private boolean startTestActiveEnergyDirect;
    private boolean startTestReactiveEnergyDirect;
    private boolean startTestActiveEnergyReverse;
    private boolean startTestReactiveEnergyReverse;

    //Результат теста проверка счётного механизма
    private boolean constActiveEnergyDirect;
    private boolean constReactiveEnergyDirect;
    private boolean constActiveEnergyReverse;
    private boolean constReactiveEnergyReverse;

    //Результат теста RTC
    private String RTCTestResult;

    //Результат теста самохода
    private boolean creepTest;

    public void createError(int numberArrayList ,String name) {
        switch (numberArrayList) {
            case 0: errorListAPPls.add(new Error(name)); break;
            case 1: errorListAPMns.add(new Error(name)); break;
            case 2: errorListRPPls.add(new Error(name)); break;
            case 3: errorListRPMns.add(new Error(name)); break;
        }
    }

    //Добавление последнего значения в класс еррорс
    public void addLastErrorInErrorList(int index, String error, int energyPulseChanel) {

        switch (energyPulseChanel) {
            case 0: {
                errorListAPPls.get(index).setLastError(error);
            }break;

            case 1: {
                errorListAPMns.get(index).setLastError(error);
            }break;

            case 2: {
                errorListRPPls.get(index).setLastError(error);
            }break;

            case 3: {
                errorListRPMns.get(index).setLastError(error);
            }break;
        }
    }

    //Добавление локального массива погрешностей в погрешность
    public void addLocalErrorInMain(int index, int energyPulseChanel) {

        switch (energyPulseChanel) {
            case 0: {
                errorListAPPls.get(index).setErrors(localErrors);
                localErrors = new String[10];
            }

            case 1: {
                errorListAPMns.get(index).setErrors(localErrors);
                localErrors = new String[10];
            }

            case 2: {
                errorListRPPls.get(index).setErrors(localErrors);
                localErrors = new String[10];
            }

            case 3: {
                errorListRPMns.get(index).setErrors(localErrors);
                localErrors = new String[10];
            }
        }
    }

    public void setCreepTest(boolean creepTest) {
        this.creepTest = creepTest;
    }

    public String[] getLocalErrors() {
        return localErrors;
    }

    public void setRTCTestResult(String RTCTestResult) {
        this.RTCTestResult = RTCTestResult;
    }

    public void setStartTestActiveEnergyDirect(boolean startTestActiveEnergyDirect) {
        this.startTestActiveEnergyDirect = startTestActiveEnergyDirect;
    }

    public void setStartTestReactiveEnergyDirect(boolean startTestReactiveEnergyDirect) {
        this.startTestReactiveEnergyDirect = startTestReactiveEnergyDirect;
    }

    public void setStartTestActiveEnergyReverse(boolean startTestActiveEnergyReverse) {
        this.startTestActiveEnergyReverse = startTestActiveEnergyReverse;
    }

    public void setStartTestReactiveEnergyReverse(boolean startTestReactiveEnergyReverse) {
        this.startTestReactiveEnergyReverse = startTestReactiveEnergyReverse;
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

    public List<Error> getErrorListAPPls() {
        return errorListAPPls;
    }

    public List<Error> getErrorListAPMns() {
        return errorListAPMns;
    }

    public List<Error> getErrorListRPPls() {
        return errorListRPPls;
    }

    public List<Error> getErrorListRPMns() {
        return errorListRPMns;
    }

    public int getAmountMeasur() {
        return amountMeasur;
    }

    public void setAmountMeasur(int amountMeasur) {
        this.amountMeasur = amountMeasur;
    }

    public class Error {

        Error(String nameTestPointError) {
            this.nameTestPointError = nameTestPointError;
        }

        String nameTestPointError;

        SimpleStringProperty lastError = new SimpleStringProperty("15");

        public SimpleStringProperty lastErrorProperty() {
            return lastError;
        }

        public String getLastError() {
            return lastError.get();
        }

        public void setLastError(String lastError) {
            this.lastError.set(lastError);
        }

        String[] errors = new String[10];

        public void setErrors(String[] errors) {
            this.errors = errors;
        }

        public String[] getErrors() {
            return errors;
        }

        public String getNameTestPointError() {
            return nameTestPointError;
        }

        public void setNameTestPointError(String nameTestPointError) {
            this.nameTestPointError = nameTestPointError;
        }
    }
}
