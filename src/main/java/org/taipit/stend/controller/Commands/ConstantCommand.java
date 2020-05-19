package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ConstantCommand implements Commands, Serializable {

    private StendDLLCommands stendDLLCommands;

    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Есть ли следующая команда?
    private boolean nextCommand;

    //Эта команда будет проходить по времени?
    private boolean runTestToTime;

    private String strTimeToTest;

    private long timeToTest;

    private double kWToTest;

    //Лист с счётчиками для испытания
    private List<Meter> meterForTestList;

    //Максимальный порог ошибки
    private double eminProc;

    //Минимальный порог ошибки
    private double emaxProc;

    //Кол-во импульсов для расчёта ошибки
    private int pulse;

    //Имя точки для отображения в таблице
    private String name;

    //id кнопки
    private String id;

    //Базовый ток
    private double Ib;

    //Режим
    private int phase;

    //Напряжение
    private double ratedVolt;

    //Процент от напряжения
    private double voltPer;

    //Ток
    private double ratedCurr;

    //Процен от тока
    private double currPer;

    //Частота
    private double ratedFreq;

    //Коэфициент мощности
    private String cosP;

    //Необходимо сделать в доп тестовом окне
    private int phaseSrequence;

    //Направление тока
    private int revers;

    //По каким фазам пустить ток
    private String iABC;

    //Активная ли точка
    private boolean active = true;

    //Импульсный выход
    private int channelFlag;

    //Количество повторов теста
    private int countResult;

    //Константа счётчика для теста
    private int constantMeter;

    public ConstantCommand(boolean threePhaseStendCommand, boolean runTestToTime, String strTimeToTest, String id,
                           String name, double voltPer, double currPer, int revers, int channelFlag, double eminProc, double emaxProc) {
        this.threePhaseCommand = threePhaseStendCommand;
        this.runTestToTime = runTestToTime;
        this.strTimeToTest = strTimeToTest;
        this.id = id;
        this.name = name;
        this.voltPer = voltPer;
        this.currPer = currPer;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.eminProc = eminProc;
        this.emaxProc = emaxProc;

        phaseSrequence = 0;
        cosP = "1.0";

        String[] timeArr = strTimeToTest.split(":");

        String hours = timeArr[0];
        String mins = timeArr[1];
        String seks = timeArr[2];

        timeToTest =  ((Integer.parseInt(hours) * 60 * 60) + (Integer.parseInt(mins) * 60) + Integer.parseInt(seks)) * 1000;
    }

    public ConstantCommand(boolean threePhaseStendCommand, boolean runTestToTime, double kWToTest, String id,
                           String name, double voltPer, double currPer, int revers, int channelFlag, double eminProc, double emaxProc) {
        this.threePhaseCommand = threePhaseStendCommand;
        this.runTestToTime = runTestToTime;
        this.kWToTest = kWToTest;
        this.id = id;
        this.name = name;
        this.voltPer = voltPer;
        this.currPer = currPer;
        this.revers = revers;
        this.channelFlag = channelFlag;
        this.eminProc = eminProc;
        this.emaxProc = emaxProc;

        phaseSrequence = 0;
        cosP = "1.0";
    }

    //===================================================================================================
    //Команда выполнения для последовательного теста
    @Override
    public boolean execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("execute_1");
            throw new InterruptedException();
        }


        ratedCurr = Ib;

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }

        } else {
            if (!threePhaseCommand) {
                if (iABC.equals("A")) {
                    if (!stendDLLCommands.selectCircuit(0)) throw new ConnectForStendExeption();
                } else if (iABC.equals("B")) {
                    if (!stendDLLCommands.selectCircuit(1)) throw new ConnectForStendExeption();
                }
                iABC = "H";
            }
        }

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        if (Thread.currentThread().isInterrupted()) {
            System.out.println("execute_2");
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, 0, iABC, cosP)) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(5000);

        //Устанавливаю
        for (Meter meter : meterForTestList) {
            stendDLLCommands.constTestStart(meter.getId(), constantMeter);
        }

        if (runTestToTime) {
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            Thread.sleep(timeToTest);

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

            //Получаю результат
            double result;
            for (Meter meter : meterForTestList) {

                result = stendDLLCommands.constPulseRead(constantMeter, meter.getId());
                Meter.ConstantResult commandResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                if (result < eminProc || result > emaxProc) {
                    commandResult.setPassTest(false);
                    commandResult.setLastResultForTabView("F" + result);
                    commandResult.getResults()[0] = result + "П";
                } else {
                    commandResult.setPassTest(true);
                    commandResult.setLastResultForTabView("P" + result);
                    commandResult.getResults()[0] = result + "Г";
                }
            }

        } else {
            double refMeterEnergy = 0;
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            while (kWToTest > refMeterEnergy) {
                refMeterEnergy = stendDLLCommands.constEnergyRead(constantMeter, 1);

                Thread.sleep(3000);
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

            //Получаю результат
            double result;
            for (Meter meter : meterForTestList) {

                result = stendDLLCommands.constPulseRead(constantMeter, meter.getId());
                Meter.ConstantResult commandResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                if (result < eminProc || result > emaxProc) {
                    commandResult.setPassTest(false);
                    commandResult.setLastResultForTabView("F" + result);
                    commandResult.getResults()[0] = result + "П";
                } else {
                    commandResult.setPassTest(true);
                    commandResult.setLastResultForTabView("P" + result);
                    commandResult.getResults()[0] = result + "Г";
                }
            }

        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        if (!Thread.currentThread().isInterrupted() && nextCommand) return true;

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        return !Thread.currentThread().isInterrupted();
    }

    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            System.out.println("execute_1");
            throw new InterruptedException();
        }

        ratedCurr = Ib;

        if (stendDLLCommands instanceof ThreePhaseStend) {

            if (threePhaseCommand) {
                iABC = "H";
                //Выбор константы в зависимости от энергии
                if (channelFlag <= 1) {
                    constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
                    phase = 1;
                } else {
                    constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
                    phase = 6;
                }
            } else {
                iABC = "C";
                if (channelFlag <= 1) {
                    constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
                    phase = 0;
                } else {
                    constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
                    phase = 7;
                }
            }
        } else {
            iABC = "H";
            if (channelFlag <= 1) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
                phase = 0;
            } else {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
                phase = 7;
            }
        }

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        if (Thread.currentThread().isInterrupted()) {
            System.out.println("execute_2");
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, 0, iABC, cosP)) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(5000);

        //Устанавливаю
        for (Meter meter : meterForTestList) {
            stendDLLCommands.constTestStart(meter.getId(), constantMeter);
        }

        if (runTestToTime) {
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            Thread.sleep(timeToTest);

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

            //Получаю результат
            double result;
            for (Meter meter : meterForTestList) {

                result = stendDLLCommands.constPulseRead(constantMeter, meter.getId());
                Meter.ConstantResult commandResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                if (result < eminProc || result > emaxProc) {
                    commandResult.setPassTest(false);
                    commandResult.setLastResultForTabView("F" + result);
                    commandResult.getResults()[0] = result + "П";
                } else {
                    commandResult.setPassTest(true);
                    commandResult.setLastResultForTabView("P" + result);
                    commandResult.getResults()[0] = result + "Г";
                }
            }

        } else {
            double refMeterEnergy = 0;
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            while (kWToTest > refMeterEnergy) {
                refMeterEnergy = stendDLLCommands.constEnergyRead(constantMeter, 1);

                Thread.sleep(3000);
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

            //Получаю результат
            double result;
            for (Meter meter : meterForTestList) {

                result = stendDLLCommands.constPulseRead(constantMeter, meter.getId());
                Meter.ConstantResult commandResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                if (result < eminProc || result > emaxProc) {
                    commandResult.setPassTest(false);
                    commandResult.setLastResultForTabView("F" + result);
                    commandResult.getResults()[0] = result + "П";
                } else {
                    commandResult.setPassTest(true);
                    commandResult.setLastResultForTabView("P" + result);
                    commandResult.getResults()[0] = result + "Г";
                }
            }

        }
    }

    //Опрашивает счётчики до нужно значения проходов
    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterForTestList.size());

        for (Meter meter : meterForTestList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }

    public void setPulse(String pulse) {
        this.pulse = Integer.parseInt(pulse);
    }

    public void setEmin(String emin) {

    }

    public void setEmax(String emax) {

    }

    public String getName() {
        return name;
    }

    public String getEmin() {
        return null;
    }

    public String getEmax() {
        return null;
    }

    public String getPulse() {
        return null;
    }

    public void setIb(double ib) {
        Ib = ib;
    }

    public String getId() {
        return id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMeterForTestList(List<Meter> meterForTestList) {
        this.meterForTestList = meterForTestList;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCountResult(String countResult) {
        this.countResult = Integer.parseInt(countResult);
    }

    public String getCountResult() {
        return null;
    }

    public void setNextCommand(boolean nextCommand) {
        this.nextCommand = nextCommand;
    }

    @Override
    public void setInterrupt(boolean interrupt) {

    }

    public boolean isRunTestToTime() {
        return runTestToTime;
    }

    public String getStrTimeToTest() {
        return strTimeToTest;
    }

    public double getkWToTest() {
        return kWToTest;
    }

    public double getVoltPer() {
        return voltPer;
    }

    public double getCurrPer() {
        return currPer;
    }

    public double getEmaxProc() {
        return emaxProc;
    }

    public double getEminProc() {
        return eminProc;
    }

    public void setEminProc(double eminProc) {
        this.eminProc = eminProc;
    }

    public void setEmaxProc(double emaxProc) {
        this.emaxProc = emaxProc;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }
}
