package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ConstantCommand implements Commands, Serializable {

    private StendDLLCommands stendDLLCommands;

    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Есть ли следующая команда?
    private boolean nextCommand;

    //Эта команда будет проходить по времени?
    private boolean runTestToTime;

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
    private String iABC = "H";

    //Активная ли точка
    private boolean active = true;

    //Импульсный выход
    private int channelFlag;

    //Количество повторов теста
    private int countResult;

    //Константа счётчика для теста
    private int constantMeter;

    //Время теста введённое пользователем
    private long timeTheTest;

    private long timeStart;
    private long timeEnd;
    private long currTime;
    private String strTime;

    private Timer timer;

    public ConstantCommand(boolean threePhaseStendCommand, boolean runTestToTime, long timeToTest, String id,
                           String name, double voltPer, double currPer, int revers, int channelFlag, double eminProc, double emaxProc) {
        this.threePhaseCommand = threePhaseStendCommand;
        this.runTestToTime = runTestToTime;
        this.timeTheTest = timeToTest;
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

        int countResult = 1;

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, 0, iABC, cosP)) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(5000);

        //Устанавливаю
        for (Meter meter : meterForTestList) {
            stendDLLCommands.constTestStart(meter.getId(), constantMeter);
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (runTestToTime) {

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + timeTheTest;

            while (System.currentTimeMillis() < timeEnd) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                currTime = timeEnd - System.currentTimeMillis();

                strTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
                        TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
                        TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));

                for (Meter meter : meterForTestList) {
                    Meter.CommandResult errorResult = meter.returnResultCommand(index, channelFlag);
                    errorResult.setLastResultForTabView("N" + strTime);
                }

                Thread.sleep(500);
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

            //Получаю результат
            double result;
            String kwRefMeter;
            String kwMeter;

            for (Meter meter : meterForTestList) {

                String[] kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                Meter.ConstantResult constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                kwRefMeter = kw[0];
                kwMeter = kw[1];

                if (result < eminProc || result > emaxProc) {
                    constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                } else {
                    constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                }
            }

        } else {
            double refMeterEnergy = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            Meter.ConstantResult constantResult;
            String[] kw = null;

            while (kWToTest > refMeterEnergy) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (Meter meter : meterForTestList) {
                    kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                    constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                    constantResult.setLastResultForTabView("N" + kw[1]);
                }

                if (kw != null) {
                    refMeterEnergy = Double.parseDouble(kw[0]);
                }

                Thread.sleep(1000);
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

            //Получаю результат
            double result;
            String kwRefMeter;
            String kwMeter;

            for (Meter meter : meterForTestList) {

                kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                kwRefMeter = kw[0];
                kwMeter = kw[1];

                if (result < eminProc || result > emaxProc) {
                    constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                } else {
                    constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
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

        int countResult = 1;

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, 0, iABC, cosP)) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(5000);

        while (!Thread.currentThread().isInterrupted()) {

            //Устанавливаю
            for (Meter meter : meterForTestList) {
                stendDLLCommands.constTestStart(meter.getId(), constantMeter);
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            if (runTestToTime) {

                if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                timeStart = System.currentTimeMillis();
                timeEnd = timeStart + timeTheTest;

                while (System.currentTimeMillis() < timeEnd) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    currTime = timeEnd - System.currentTimeMillis();

                    strTime = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(currTime),
                            TimeUnit.MILLISECONDS.toMinutes(currTime) % TimeUnit.HOURS.toMinutes(1),
                            TimeUnit.MILLISECONDS.toSeconds(currTime) % TimeUnit.MINUTES.toSeconds(1));

                    for (Meter meter : meterForTestList) {
                        Meter.CommandResult errorResult = meter.returnResultCommand(index, channelFlag);
                        errorResult.setLastResultForTabView("N" + strTime);
                    }

                    Thread.sleep(500);
                }

                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

                //Получаю результат
                double result;
                String kwRefMeter;
                String kwMeter;

                for (Meter meter : meterForTestList) {

                    String[] kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                    result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                    Meter.ConstantResult constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                    kwRefMeter = kw[0];
                    kwMeter = kw[1];

                    if (result < eminProc || result > emaxProc) {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                    } else {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                    }
                }

            } else {
                double refMeterEnergy = 0;

                if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                Meter.ConstantResult constantResult;
                String[] kw = null;

                while (kWToTest > refMeterEnergy) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    for (Meter meter : meterForTestList) {
                        kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                        constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                        constantResult.setLastResultForTabView("N" + kw[1]);
                    }

                    if (kw != null) {
                        refMeterEnergy = Double.parseDouble(kw[0]);
                    }

                    Thread.sleep(1000);
                }

                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

                //Получаю результат
                double result;
                String kwRefMeter;
                String kwMeter;

                for (Meter meter : meterForTestList) {

                    kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                    result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                    constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                    kwRefMeter = kw[0];
                    kwMeter = kw[1];

                    if (result < eminProc || result > emaxProc) {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                    } else {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                    }
                }
            }

            countResult++;

            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, 0, iABC, cosP)) throw new ConnectForStendExeption();

            Thread.sleep(5000);
        }
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

    public long getTimeTheTest() {
        return timeTheTest;
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

    public void setRatedCurr(double ratedCurr) {
        this.ratedCurr = ratedCurr;
    }
}
