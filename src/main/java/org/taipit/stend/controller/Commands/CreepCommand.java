package org.taipit.stend.controller.Commands;

import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;


public class CreepCommand implements Commands, Serializable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

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


    //Лист со столбами счётчикв для изменения флага и цвета
    private List<TableColumn<Meter.CommandResult, String>> tableColumnError;

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

    public CreepCommand(boolean gostTest, int channelFlag, String userTimeTest) {
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
        this.userTimeTest = userTimeTest;

        if (!gostTest) {
            try {
                String[] timearr = userTimeTest.split(":");
                String hours = timearr[0];
                String mins = timearr[1];
                String seks = timearr[2];

                timeForTest = ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;

            }catch (NumberFormatException e){
                e.printStackTrace();
                System.out.println("Неверные данные для теста");
            }

        }
    }

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
        }

        if (interrupt) throw new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;
        /**
         * Необходимо записывать время и результат в массив
         * Вид записи результата:
         * Начало это просто время теста
         * Как только пользователь нажимает старт (очистка и таймер теста)
         * Если прошёл "Время теста + P" и цвет пройденного теста
         * Если нет, то "Время теста + F" и цвет проваленного теста
         */

        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {
            if (interrupt) throw new InterruptedTestException();

            for (Meter meter : meterList) {
                if (!meter.run(pulseValue, stendDLLCommands)) {
                    addTestTimeAndFail(meter, channelFlag, System.currentTimeMillis() - currTime);
                    creepCommandResult.put(meter.getId(), false);
                }
            }
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    @Override
    public void executeForContinuousTest() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw new InterruptedTestException();
        creepCommandResult = initCreepCommandResult();

        if (gostTest) {
            timeForTest = initTimeForGOSTTest();
        }

        if (interrupt) throw new InterruptedTestException();
        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, 0, 0,
                voltPer, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        while (!interrupt) {
            currTime = System.currentTimeMillis();
            timeEnd = currTime + timeForTest;

            while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {
                if (interrupt) throw new InterruptedTestException();

                for (Meter meter : meterList) {
                    if (!meter.run(pulseValue, stendDLLCommands)) {
                        addTestTimeAndFail(meter, channelFlag, System.currentTimeMillis() - currTime);
                        creepCommandResult.put(meter.getId(), false);
                    }
                }
            }
        }

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
    public long initTimeForGOSTTest() {
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

    //В зависимости от направления тока переносит время провала теста в нужную строку
    private void addTestTimeAndFail(Meter meter, int channelFlag, long timeFail) {
        Meter.CreepResult commandResult;

        switch (channelFlag) {
            case  0: {
                commandResult = (Meter.CreepResult) meter.getErrorListAPPls().get(index);
                commandResult.setTimeToFail(timeFail);
                commandResult.setPassTest(false);
                /**
                 * Необходимо записывать время и результат в массив
                 */
            }break;

            case  1: {
                commandResult = (Meter.CreepResult) meter.getErrorListAPMns().get(index);
                commandResult.setTimeToFail(timeFail);
                commandResult.setPassTest(false);
            }break;

            case  2: {
                commandResult = (Meter.CreepResult) meter.getErrorListRPPls().get(index);
                commandResult.setTimeToFail(timeFail);
                commandResult.setPassTest(false);
            }break;

            case  3: {
                commandResult = (Meter.CreepResult) meter.getErrorListRPPls().get(index);
                commandResult.setTimeToFail(timeFail);
                commandResult.setPassTest(false);
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

    public void setIndex(int index) {
        this.index = index;
    }

    public long getTimeForTest() {
        return timeForTest;
    }

    public void setTableColumnError(List<TableColumn<Meter.CommandResult, String>> tableColumnError) {
        this.tableColumnError = tableColumnError;
    }
}
