package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class CreepCommand implements Commands, Serializable {

    private StendDLLCommands stendDLLCommands;

    private int phase;

    private double ratedVolt;

    private double ratedFreq;

    private double voltPer;

    private int channelFlag;

    //Имя точки для отображения в таблице
    private String name;

    //Время расчитывается по госту?
    private boolean gostTest;

    //Время теста введённое пользователем
    private String userTimeTest;

    //Количество импоульсов для провала теста
    private int pulseValue;

    //Константа счётчика для расчёта по ГОСТ формуле
    private int constMeterForTest;

    //Максимальный ток счётчика для формулы по ГОСТ
    private double maxCurrMeter;

    //Это трехфазный счётчик?
    private boolean treePhaseMeter;

    //Количество измерительных элементов (фрехфазный или однофазный)
    private int amountMeasElem;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> creepCommandResult;
    private HashMap<Integer, LocalMeter> metersList;

    public HashMap<Integer, Boolean> getCreepCommandResult() {
        return creepCommandResult;
    }

    public CreepCommand(boolean gostTest, int channelFlag) {
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
    }

    @Override
    public void execute() {
        creepCommandResult = initCreepCommandResult();
        metersList = initMeterList();

        if (gostTest) {
            timeForTest = initTimeForGOSTTest();
        } else {
            timeForTest = initTimeForTest();
        }

        stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, "H", "1.0");

        stendDLLCommands.setEnergyPulse(channelFlag);

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {
            for (Map.Entry<Integer, LocalMeter> localMeter : metersList.entrySet()) {
                if (!(metersList.get(localMeter.getKey()).run())) {
                    creepCommandResult.put(localMeter.getKey(), false);
                }
            }
        }

        //Перенос результата теста в разультата каждого отдельного счётчика
        for (Map.Entry<Integer, Boolean> result : creepCommandResult.entrySet()) {
            stendDLLCommands.getAmountActivePlacesForTest().get(result.getKey()).setCreepTest(result.getValue());
        }
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(stendDLLCommands.getAmountActivePlacesForTest().size());
        for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
            init.put(meter.getKey(), true);
        }
        return init;
    }

    private HashMap<Integer, LocalMeter> initMeterList() {
        HashMap<Integer, LocalMeter> init = new HashMap<>();
        for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
            init.put(meter.getKey(), new LocalMeter(meter.getKey()));
        }
        return  init;
    }

    //Расчётная формула времени теста по ГОСТ
    private long initTimeForGOSTTest() {
        if (treePhaseMeter) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

        double formulaResult = 600000000 / (constMeterForTest * amountMeasElem * ratedVolt * maxCurrMeter);

        //Округляю результат до 3х знаков
        BigDecimal bigDecimalResult = new BigDecimal(String.valueOf(formulaResult)).setScale(3, BigDecimal.ROUND_CEILING);

        String[] timeArr = String.valueOf(bigDecimalResult).split("\\.");

        //Округляю значение после запятой до целых секунд
        BigDecimal bigDecimal = new BigDecimal(Integer.parseInt(timeArr[1]) * 0.06).setScale(0, BigDecimal.ROUND_CEILING);

        return ((Integer.parseInt(timeArr[0]) * 60) + bigDecimal.intValue()) * 1000;
    }

    //Расчёт времени теста исходя из параметров введённых пользователем
    private long initTimeForTest() {
        String[] timearr = userTimeTest.split(":");
        String hours = timearr[0];
        String mins = timearr[1];
        String seks = timearr[2];

        return ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setConstMeterForTest(int constMeterForTest) {
        this.constMeterForTest = constMeterForTest;
    }

    public void setMaxCurrMeter(int maxCurrMeter) {
        this.maxCurrMeter = maxCurrMeter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTreePhaseMeter(boolean treePhaseMeter) {
        this.treePhaseMeter = treePhaseMeter;
    }

    public void setPulseValue(int pulseValue) {
        this.pulseValue = pulseValue;
    }

    public void setUserTimeTest(String userTimeTest) {
        this.userTimeTest = userTimeTest;
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

    @Override
    public String toString() {
        return name;
    }

    private class LocalMeter implements Serializable{
        int number;
        int counter = 0;
        boolean searchMark;

        LocalMeter(int number) {
            this.number = number;
        }

        boolean run() {
            if (counter < pulseValue) {
                if (!searchMark) {
                    stendDLLCommands.searchMark(number);
                    searchMark = true;
                } else {
                    if (stendDLLCommands.searchMarkResult(number)) {
                        counter++;
                        searchMark = false;
                    }
                }
            } else return false;
            return true;
        }
    }
}
