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
    private List<Error> errorListAPPls = new ArrayList<>();
    private List<Error> errorListAPMns = new ArrayList<>();
    private List<Error> errorListRPPls = new ArrayList<>();
    private List<Error> errorListRPMns = new ArrayList<>();

    //-----------------------------------------------------
    //Результат теста самохода
    private boolean creepTestResultAPPls;
    private boolean creepTestResultAPMns;
    private boolean creepTestResultRPPls;
    private boolean creepTestResultRPMns;

    private boolean creepTestResultAPPlsGOST;
    private boolean creepTestResultAPMnsGOST;
    private boolean creepTestResultRPPlsGOST;
    private boolean creepTestResultRPMnsGOST;

    //Время провала теста на самоход
    private long creepTestFailTimeAPPls;
    private long creepTestFailTimeAPMns;
    private long creepTestFailTimeRPPls;
    private long creepTestFailTimeRPMns;

    private long creepTestFailTimeAPPlsGOST;
    private long creepTestFailTimeAPMnsGOST;
    private long creepTestFailTimeRPPlsGOST;
    private long creepTestFailTimeRPMnsGOST;

    //-----------------------------------------------------
    //Результат теста чувствительности к току
    private boolean startTestResultAPPls;
    private boolean startTestResultAPMns;
    private boolean startTestResultRPPls;
    private boolean startTestResultRPMns;

    private boolean startTestResultAPPlsGOST;
    private boolean startTestResultAPMnsGOST;
    private boolean startTestResultRPPlsGOST;
    private boolean startTestResultRPMnsGOST;

    //Время прохождения теста на чувствительность
    private long startTestPassTimeAPPls;
    private long startTestPassTimeAPMns;
    private long startTestPassTimeRPPls;
    private long startTestPassTimeRPMns;

    private long startTestPassTimeAPPlsGOST;
    private long startTestPassTimeAPMnsGOST;
    private long startTestPassTimeRPPlsGOST;
    private long startTestPassTimeRPMnsGOST;
    //-----------------------------------------------------
    //Результат теста проверка счётного механизма
    private boolean constActiveEnergyDirect;
    private boolean constReactiveEnergyDirect;
    private boolean constActiveEnergyReverse;
    private boolean constReactiveEnergyReverse;

    //-----------------------------------------------------
    //Результат теста RTC
    private String RTCTestErrorAPPls;
    private String RTCTestErrorAPMns;
    private String RTCTestErrorRPPls;
    private String RTCTestErrorRPMns;

    private boolean RTCTestResultAPPls;
    private boolean RTCTestResultAPMns;
    private boolean RTCTestResultRPPls;
    private boolean RTCTestResultRPMns;

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

    public int getAmountImn() {
        return amountImn;
    }

    public void setAmountImn(int amountImn) {
        this.amountImn = amountImn;
    }

    public boolean isCreepTestResultAPPls() {
        return creepTestResultAPPls;
    }

    public boolean isCreepTestResultAPMns() {
        return creepTestResultAPMns;
    }

    public boolean isCreepTestResultRPPls() {
        return creepTestResultRPPls;
    }

    public boolean isCreepTestResultRPMns() {
        return creepTestResultRPMns;
    }

    public boolean isCreepTestResultAPPlsGOST() {
        return creepTestResultAPPlsGOST;
    }

    public boolean isCreepTestResultAPMnsGOST() {
        return creepTestResultAPMnsGOST;
    }

    public boolean isCreepTestResultRPPlsGOST() {
        return creepTestResultRPPlsGOST;
    }

    public boolean isCreepTestResultRPMnsGOST() {
        return creepTestResultRPMnsGOST;
    }

    public long getCreepTestFailTimeAPPls() {
        return creepTestFailTimeAPPls;
    }

    public long getCreepTestFailTimeAPMns() {
        return creepTestFailTimeAPMns;
    }

    public long getCreepTestFailTimeRPPls() {
        return creepTestFailTimeRPPls;
    }

    public long getCreepTestFailTimeRPMns() {
        return creepTestFailTimeRPMns;
    }

    public long getCreepTestFailTimeAPPlsGOST() {
        return creepTestFailTimeAPPlsGOST;
    }

    public long getCreepTestFailTimeAPMnsGOST() {
        return creepTestFailTimeAPMnsGOST;
    }

    public long getCreepTestFailTimeRPPlsGOST() {
        return creepTestFailTimeRPPlsGOST;
    }

    public long getCreepTestFailTimeRPMnsGOST() {
        return creepTestFailTimeRPMnsGOST;
    }

    public boolean isStartTestResultAPPls() {
        return startTestResultAPPls;
    }

    public boolean isStartTestResultAPMns() {
        return startTestResultAPMns;
    }

    public boolean isStartTestResultRPPls() {
        return startTestResultRPPls;
    }

    public boolean isStartTestResultRPMns() {
        return startTestResultRPMns;
    }

    public boolean isStartTestResultAPPlsGOST() {
        return startTestResultAPPlsGOST;
    }

    public boolean isStartTestResultAPMnsGOST() {
        return startTestResultAPMnsGOST;
    }

    public boolean isStartTestResultRPPlsGOST() {
        return startTestResultRPPlsGOST;
    }

    public boolean isStartTestResultRPMnsGOST() {
        return startTestResultRPMnsGOST;
    }

    public long getStartTestPassTimeAPPls() {
        return startTestPassTimeAPPls;
    }

    public long getStartTestPassTimeAPMns() {
        return startTestPassTimeAPMns;
    }

    public long getStartTestPassTimeRPPls() {
        return startTestPassTimeRPPls;
    }

    public long getStartTestPassTimeRPMns() {
        return startTestPassTimeRPMns;
    }

    public long getStartTestPassTimeAPPlsGOST() {
        return startTestPassTimeAPPlsGOST;
    }

    public long getStartTestPassTimeAPMnsGOST() {
        return startTestPassTimeAPMnsGOST;
    }

    public long getStartTestPassTimeRPPlsGOST() {
        return startTestPassTimeRPPlsGOST;
    }

    public long getStartTestPassTimeRPMnsGOST() {
        return startTestPassTimeRPMnsGOST;
    }

    public boolean isConstActiveEnergyDirect() {
        return constActiveEnergyDirect;
    }

    public boolean isConstReactiveEnergyDirect() {
        return constReactiveEnergyDirect;
    }

    public boolean isConstActiveEnergyReverse() {
        return constActiveEnergyReverse;
    }

    public boolean isConstReactiveEnergyReverse() {
        return constReactiveEnergyReverse;
    }

    public void setRTCTestErrorAPPls(String RTCTestErrorAPPls) {
        this.RTCTestErrorAPPls = RTCTestErrorAPPls;
    }

    public void setRTCTestErrorAPMns(String RTCTestErrorAPMns) {
        this.RTCTestErrorAPMns = RTCTestErrorAPMns;
    }

    public void setRTCTestErrorRPPls(String RTCTestErrorRPPls) {
        this.RTCTestErrorRPPls = RTCTestErrorRPPls;
    }

    public void setRTCTestErrorRPMns(String RTCTestErrorRPMns) {
        this.RTCTestErrorRPMns = RTCTestErrorRPMns;
    }

    public void setRTCTestResultAPPls(boolean RTCTestResultAPPls) {
        this.RTCTestResultAPPls = RTCTestResultAPPls;
    }

    public void setRTCTestResultAPMns(boolean RTCTestResultAPMns) {
        this.RTCTestResultAPMns = RTCTestResultAPMns;
    }

    public void setRTCTestResultRPPls(boolean RTCTestResultRPPls) {
        this.RTCTestResultRPPls = RTCTestResultRPPls;
    }

    public void setRTCTestResultRPMns(boolean RTCTestResultRPMns) {
        this.RTCTestResultRPMns = RTCTestResultRPMns;
    }

    public void setCreepTestResultAPPls(boolean creepTestResultAPPls) {
        this.creepTestResultAPPls = creepTestResultAPPls;
    }

    public void setCreepTestResultAPMns(boolean creepTestResultAPMns) {
        this.creepTestResultAPMns = creepTestResultAPMns;
    }

    public void setCreepTestResultRPPls(boolean creepTestResultRPPls) {
        this.creepTestResultRPPls = creepTestResultRPPls;
    }

    public void setCreepTestResultRPMns(boolean creepTestResultRPMns) {
        this.creepTestResultRPMns = creepTestResultRPMns;
    }

    public void setCreepTestResultAPPlsGOST(boolean creepTestResultAPPlsGOST) {
        this.creepTestResultAPPlsGOST = creepTestResultAPPlsGOST;
    }

    public void setCreepTestResultAPMnsGOST(boolean creepTestResultAPMnsGOST) {
        this.creepTestResultAPMnsGOST = creepTestResultAPMnsGOST;
    }

    public void setCreepTestResultRPPlsGOST(boolean creepTestResultRPPlsGOST) {
        this.creepTestResultRPPlsGOST = creepTestResultRPPlsGOST;
    }

    public void setCreepTestResultRPMnsGOST(boolean creepTestResultRPMnsGOST) {
        this.creepTestResultRPMnsGOST = creepTestResultRPMnsGOST;
    }

    public void setCreepTestFailTimeAPPls(long creepTestFailTimeAPPls) {
        this.creepTestFailTimeAPPls = creepTestFailTimeAPPls;
    }

    public void setCreepTestFailTimeAPMns(long creepTestFailTimeAPMns) {
        this.creepTestFailTimeAPMns = creepTestFailTimeAPMns;
    }

    public void setCreepTestFailTimeRPPls(long creepTestFailTimeRPPls) {
        this.creepTestFailTimeRPPls = creepTestFailTimeRPPls;
    }

    public void setCreepTestFailTimeRPMns(long creepTestFailTimeRPMns) {
        this.creepTestFailTimeRPMns = creepTestFailTimeRPMns;
    }

    public void setCreepTestFailTimeAPPlsGOST(long creepTestFailTimeAPPlsGOST) {
        this.creepTestFailTimeAPPlsGOST = creepTestFailTimeAPPlsGOST;
    }

    public void setCreepTestFailTimeAPMnsGOST(long creepTestFailTimeAPMnsGOST) {
        this.creepTestFailTimeAPMnsGOST = creepTestFailTimeAPMnsGOST;
    }

    public void setCreepTestFailTimeRPPlsGOST(long creepTestFailTimeRPPlsGOST) {
        this.creepTestFailTimeRPPlsGOST = creepTestFailTimeRPPlsGOST;
    }

    public void setCreepTestFailTimeRPMnsGOST(long creepTestFailTimeRPMnsGOST) {
        this.creepTestFailTimeRPMnsGOST = creepTestFailTimeRPMnsGOST;
    }

    public void setStartTestResultAPPls(boolean startTestResultAPPls) {
        this.startTestResultAPPls = startTestResultAPPls;
    }

    public void setStartTestResultAPMns(boolean startTestResultAPMns) {
        this.startTestResultAPMns = startTestResultAPMns;
    }

    public void setStartTestResultRPPls(boolean startTestResultRPPls) {
        this.startTestResultRPPls = startTestResultRPPls;
    }

    public void setStartTestResultRPMns(boolean startTestResultRPMns) {
        this.startTestResultRPMns = startTestResultRPMns;
    }

    public void setStartTestResultAPPlsGOST(boolean startTestResultAPPlsGOST) {
        this.startTestResultAPPlsGOST = startTestResultAPPlsGOST;
    }

    public void setStartTestResultAPMnsGOST(boolean startTestResultAPMnsGOST) {
        this.startTestResultAPMnsGOST = startTestResultAPMnsGOST;
    }

    public void setStartTestResultRPPlsGOST(boolean startTestResultRPPlsGOST) {
        this.startTestResultRPPlsGOST = startTestResultRPPlsGOST;
    }

    public void setStartTestResultRPMnsGOST(boolean startTestResultRPMnsGOST) {
        this.startTestResultRPMnsGOST = startTestResultRPMnsGOST;
    }

    public void setStartTestPassTimeAPPls(long startTestPassTimeAPPls) {
        this.startTestPassTimeAPPls = startTestPassTimeAPPls;
    }

    public void setStartTestPassTimeAPMns(long startTestPassTimeAPMns) {
        this.startTestPassTimeAPMns = startTestPassTimeAPMns;
    }

    public void setStartTestPassTimeRPPls(long startTestPassTimeRPPls) {
        this.startTestPassTimeRPPls = startTestPassTimeRPPls;
    }

    public void setStartTestPassTimeRPMns(long startTestPassTimeRPMns) {
        this.startTestPassTimeRPMns = startTestPassTimeRPMns;
    }

    public void setStartTestPassTimeAPPlsGOST(long startTestPassTimeAPPlsGOST) {
        this.startTestPassTimeAPPlsGOST = startTestPassTimeAPPlsGOST;
    }

    public void setStartTestPassTimeAPMnsGOST(long startTestPassTimeAPMnsGOST) {
        this.startTestPassTimeAPMnsGOST = startTestPassTimeAPMnsGOST;
    }

    public void setStartTestPassTimeRPPlsGOST(long startTestPassTimeRPPlsGOST) {
        this.startTestPassTimeRPPlsGOST = startTestPassTimeRPPlsGOST;
    }

    public void setStartTestPassTimeRPMnsGOST(long startTestPassTimeRPMnsGOST) {
        this.startTestPassTimeRPMnsGOST = startTestPassTimeRPMnsGOST;
    }

    public void setConstActiveEnergyDirect(boolean constActiveEnergyDirect) {
        this.constActiveEnergyDirect = constActiveEnergyDirect;
    }

    public void setConstReactiveEnergyDirect(boolean constReactiveEnergyDirect) {
        this.constReactiveEnergyDirect = constReactiveEnergyDirect;
    }

    public void setConstActiveEnergyReverse(boolean constActiveEnergyReverse) {
        this.constActiveEnergyReverse = constActiveEnergyReverse;
    }

    public void setConstReactiveEnergyReverse(boolean constReactiveEnergyReverse) {
        this.constReactiveEnergyReverse = constReactiveEnergyReverse;
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
