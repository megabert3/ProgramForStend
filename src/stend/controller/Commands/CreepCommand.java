package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.exeptions.ConnectForStendExeption;
import stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CreepCommand implements Commands, Serializable {

    private StendDLLCommands stendDLLCommands;

    //лист с счётчиками
    private List<Meter> meterList;

    //Команда для прерывания метода
    private boolean interrupt;

    private int phase;

    private double ratedVolt;

    private double ratedFreq;

    private double voltPer;

    private int channelFlag;

    private boolean active = true;

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
    private boolean threePhaseMeter;

    //Количество измерительных элементов (фрехфазный или однофазный)
    private int amountMeasElem;

    private long timeForTest;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> creepCommandResult;

    public CreepCommand(boolean gostTest, int channelFlag) {
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
    }

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void execute() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw new InterruptedTestException();
        creepCommandResult = initCreepCommandResult();

        if (gostTest) {
            timeForTest = initTimeForGOSTTest();
        } else {
            timeForTest = initTimeForTest();
        }

        if (interrupt) throw new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {
            if (interrupt) throw new InterruptedTestException();

            for (Meter meter : meterList) {
                if (!meter.run(pulseValue, stendDLLCommands)) {
                    addTestTimeToFail(meter, channelFlag, System.currentTimeMillis() - currTime);
                    creepCommandResult.put(meter.getId(), false);
                }
            }
        }

        //Перенос результата теста в разультата каждого отдельного счётчика
        addTestResultInMeters(creepCommandResult, channelFlag);

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), true);
        }
        return init;
    }

    //Расчётная формула времени теста по ГОСТ
    private long initTimeForGOSTTest() {
        if (threePhaseMeter) {
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

    //В зависимости от направления тока переносит результаты теста в нужную строку
    private void addTestResultInMeters(Map<Integer, Boolean> metersResult, int channelFlag) {
        switch (channelFlag) {
            case 0: {
                for (Map.Entry<Integer, Boolean> metRes : metersResult.entrySet()) {
                    for (Meter meter : meterList) {
                        if (meter.getId() == metRes.getKey()) {

                            if (gostTest) {
                                meter.setCreepTestResultAPPlsGOST(metRes.getValue());
                            } else {
                                meter.setCreepTestResultAPPls(metRes.getValue());
                            }
                        }
                    }
                }
            }break;
            case 1: {
                for (Map.Entry<Integer, Boolean> metRes : metersResult.entrySet()) {
                    for (Meter meter : meterList) {
                        if (meter.getId() == metRes.getKey()) {

                            if (gostTest) {
                                meter.setCreepTestResultAPMnsGOST(metRes.getValue());
                            } else {
                                meter.setCreepTestResultAPMns(metRes.getValue());
                            }
                        }
                    }
                }
            }break;
            case 2: {
                for (Map.Entry<Integer, Boolean> metRes : metersResult.entrySet()) {
                    for (Meter meter : meterList) {
                        if (meter.getId() == metRes.getKey()) {

                            if (gostTest) {
                                meter.setCreepTestResultRPPlsGOST(metRes.getValue());
                            } else {
                                meter.setCreepTestResultRPPls(metRes.getValue());
                            }
                        }
                    }
                }
            }break;
            case 3: {
                for (Map.Entry<Integer, Boolean> metRes : metersResult.entrySet()) {
                    for (Meter meter : meterList) {
                        if (meter.getId() == metRes.getKey()) {

                            if (gostTest) {
                                meter.setCreepTestResultRPMnsGOST(metRes.getValue());
                            } else {
                                meter.setCreepTestResultRPMns(metRes.getValue());
                            }
                        }
                    }
                }
            }break;
        }
    }

    //В зависимости от направления тока переносит время провала теста в нужную строку
    private void addTestTimeToFail(Meter meter, int channelFlag, long timeFail) {
        switch (channelFlag) {
            case  0: {
                if (gostTest) {
                    meter.setCreepTestFailTimeAPPlsGOST(timeFail);
                } else {
                    meter.setCreepTestFailTimeAPPls(timeFail);
                }
            }break;

            case  1: {
                if (gostTest) {
                    meter.setCreepTestFailTimeAPMnsGOST(timeFail);
                } else {
                    meter.setCreepTestFailTimeAPMns(timeFail);
                }
            }break;

            case  2: {
                if (gostTest) {
                    meter.setCreepTestFailTimeRPPlsGOST(timeFail);
                } else {
                    meter.setCreepTestFailTimeRPPls(timeFail);
                }
            }break;

            case  3: {
                if (gostTest) {
                    meter.setCreepTestFailTimeRPMnsGOST(timeFail);
                } else {
                    meter.setCreepTestFailTimeRPMns(timeFail);
                }
            }break;
        }
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setConstMeterForTest(int constMeterForTest) {
        this.constMeterForTest = constMeterForTest;
    }

    public void setMaxCurrMeter(double maxCurrMeter) {
        this.maxCurrMeter = maxCurrMeter;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setThreePhaseMeter(boolean treePhaseMeter) {
        this.threePhaseMeter = treePhaseMeter;
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMeterList(List<Meter> meterList) {
        this.meterList = meterList;
    }

    @Override
    public String toString() {
        return name;
    }

}
