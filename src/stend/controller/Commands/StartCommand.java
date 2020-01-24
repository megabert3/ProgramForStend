package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class StartCommand implements Commands {

    private StendDLLCommands stendDLLCommands;

    //Выставлять в зависимости от выбранного параметра перед тестом
    private int phase;

    //Номинальное напряжение
    private double ratedVolt;

    //Номинальный ток
    private double ratedCurr;

    //Номинальная частота
    private double ratedFreq;

    private int revers;

    private double currPer;

    private String iABC;

    private int channelFlag;

    //Имя точки для отображения в таблице
    private String name;

    //Время расчитывается по госту?
    private boolean gostTest;

    //Время теста введённое пользователем
    private String userTimeTest;

    //Количество импульсов для провала теста
    private int pulseValue;

    //Класс точности счётчика
    private double accuracyClass;

    //Константа счётчика для расчёта по ГОСТ формуле
    private int constMeterForTest;

    //Базовый ток счётчика для формулы по ГОСТ
    private double baseCurrMeter;

    //Это трехфазный счётчик?
    private boolean treePhaseMeter;

    //Датчик тока трансформаторный?
    private boolean transfDetect;

    //Значение тока введённое пользователем
    private double userCurr;
    
    //Количество измерительных элементов (фрехфазный или однофазный)
    private int amountMeasElem;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> startCommandResult = initCreepCommandResult();
    private HashMap<Integer, LocalMeter> metersList = initMeterList();

    public HashMap<Integer, Boolean> getCreepCommandResult() {
        return startCommandResult;
    }

    public StartCommand(StendDLLCommands stendDLLCommands, int revers, int channelFlag, boolean gostTest) {
        this.stendDLLCommands = stendDLLCommands;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.gostTest = gostTest;
    }

    @Override
    public void execute() {
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq,0, revers,
                100.0, 100.0, iABC, "1.0");

        if (gostTest) {
            timeForTest = initTimeForGOSTTest();
        } else {
            timeForTest = initTimeForTest();
        }

        stendDLLCommands.setEnergyPulse(channelFlag);

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;
        while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
            for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
                if (!(metersList.get(meter.getKey()).run())) {
                    startCommandResult.put(meter.getKey(), true);
                }
            }
        }
        addResultOnMeter();
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), false);
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

    //Довавляет результат в счётчик в зависимости от направления тока
    private void addResultOnMeter() {
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            switch (channelFlag) {
                case 0: {
                    meter.getValue().setStartTestActiveEnergyDirect(startCommandResult.get(meter.getKey()));
                }break;
                case 1: {
                    meter.getValue().setStartTestActiveEnergyReverse(startCommandResult.get(meter.getKey()));
                }break;
                case 2: {
                    meter.getValue().setStartTestReactiveEnergyDirect(startCommandResult.get(meter.getKey()));
                }break;
                case 3: {
                    meter.getValue().setStartTestReactiveEnergyReverse(startCommandResult.get(meter.getKey()));
                }
            }
        }
    }

    //Расчётная формула времени теста по ГОСТ
    public long initTimeForGOSTTest() {
        if (treePhaseMeter) {
            amountMeasElem = 3;
        } else amountMeasElem = 1;

        if (transfDetect) {
            if (accuracyClass == 1.0) {
                ratedCurr = baseCurrMeter * 0.002;
            } else {
                ratedCurr = baseCurrMeter * 0.001;
            }
        } else {
            ratedCurr = baseCurrMeter * 0.004;
        }

        double formulaResult = 2.3 * (60000 / (constMeterForTest * amountMeasElem * ratedVolt * ratedCurr));
        
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

    public void setTransfDetect(boolean transfDetect) {
        this.transfDetect = transfDetect;
    }

    public void setGostTest(boolean gostTest) {
        this.gostTest = gostTest;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPulseValue(int pulseValue) {
        this.pulseValue = pulseValue;
    }

    public void setConstMeterForTest(int constMeterForTest) {
        this.constMeterForTest = constMeterForTest;
    }

    public void setBaseCurrMeter(double baseCurrMeter) {
        this.baseCurrMeter = baseCurrMeter;
    }

    public void setTreePhaseMeter(boolean treePhaseMeter) {
        this.treePhaseMeter = treePhaseMeter;
    }

    public void setAccuracyClass(double accuracyClass) {
        this.accuracyClass = accuracyClass;
    }

    public void setUserTimeTest(String userTimeTest) {
        this.userTimeTest = userTimeTest;
    }

    public void setRatedCurr(double ratedCurr) {
        this.ratedCurr = ratedCurr;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public boolean isGostTest() {
        return gostTest;
    }

    public String getUserTimeTest() {
        return userTimeTest;
    }

    public int getPulseValue() {
        return pulseValue;
    }

    public double getRatedCurr() {
        return ratedCurr;
    }

    private class LocalMeter {
        int number;
        int counter = 0;
        boolean searchMark;

        LocalMeter(int number) {
            this.number = number;
        }

        public boolean run() {
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
