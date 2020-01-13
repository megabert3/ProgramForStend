package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;


public class CreepCommand implements Commands {

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

    //Номинальное напряжение используемое в испытании
    private double Un;

    //Это трехфазный счётчик?
    private boolean treePhaseMeter;

    //Количество измерительных элементов (фрехфазный или однофазный)
    private int amountMeasElem;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> creepCommandResult = initCreepCommandResult();
    private HashMap<Integer, LocalMeter> metersList = initMeterList();

    public HashMap<Integer, Boolean> getCreepCommandResult() {
        return creepCommandResult;
    }

    public CreepCommand(StendDLLCommands stendDLLCommands, boolean gostTest, int channelFlag) {
        this.stendDLLCommands = stendDLLCommands;
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
    }

    @Override
    public void execute() {
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
            StendDLLCommands.amountActivePlacesForTest.get(result.getKey()).setCreepTest(result.getValue());
        }
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), true);
        }
        return init;
    }

    private HashMap<Integer, LocalMeter> initMeterList() {
        HashMap<Integer, LocalMeter> init = new HashMap<>();
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), new LocalMeter(meter.getKey()));
        }
        return  init;
    }

    //Расчётная формула времени теста по ГОСТ
    private long initTimeForGOSTTest() {
        if (treePhaseMeter) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

        double formulaResult = 600000000 / (constMeterForTest * amountMeasElem * Un * maxCurrMeter);

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

    public void setConstMeterForTest(int constMeterForTest) {
        this.constMeterForTest = constMeterForTest;
    }

    public void setMaxCurrMeter(int maxCurrMeter) {
        this.maxCurrMeter = maxCurrMeter;
    }

    public void setUn(int un) {
        Un = un;
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

    @Override
    public String toString() {
        return name;
    }

    private class LocalMeter {
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
