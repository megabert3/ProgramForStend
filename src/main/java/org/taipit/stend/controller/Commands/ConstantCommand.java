package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.OnePhaseStendRefParamController;
import org.taipit.stend.controller.viewController.errorFrame.refMeter.ThreePhaseStendRefParamController;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ConstantCommand implements Commands, Serializable, Cloneable {

    private StendDLLCommands stendDLLCommands;

    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

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

    //Напряжение на фазе А
    private double voltPerA;

    //Напряжение на фазе B
    private double voltPerB;

    //Напряжение на фазе C
    private double voltPerC;

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

    transient private Thread constantThread;

    transient private Timer timer;

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
    public void execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        int refMeterCount = 1;

        timer = new Timer(true);
        constantThread = Thread.currentThread();

        try {
            //Выбор константы в зависимости от энергии
            if (channelFlag == 0 || channelFlag == 1) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
            } else if (channelFlag == 2 || channelFlag == 3) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
            }
        }catch (NumberFormatException e) {
            constantMeter = 0;
        }

        //Номер результата
        int countResult = 1;

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

                //Передаю необходимые параметры для эталонного счётчика
                ((ThreePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters (
                        voltPerA * (ratedVolt / 100), voltPerB * (ratedVolt / 100), voltPerC * (ratedVolt / 100), 0.0, 0.0, 0.0);

                stendDLLCommands.getUIWithPhase(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
            } else {

                //Передаю необходимые параметры для эталонного счётчика
                ((ThreePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters (
                        voltPer * (ratedVolt / 100), voltPer * (ratedVolt / 100), voltPer * (ratedVolt / 100), 0.0, 0.0, 0.0);

                stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        } else {
            //Передаю необходимые параметры для эталонного счётчика
            ((OnePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters (
                    voltPer * (ratedVolt / 100), 0.0);
            if (iABC.equals("H")) {
                stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);

            } else if (iABC.equals("A")) {
                stendDLLCommands.selectCircuit(0);
                iABC = "H";

            } else if (iABC.equals("B")) {
                stendDLLCommands.selectCircuit(1);
                iABC = "H";
            }

            stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP);
        }

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(TestErrorTableFrameController.timeToStabilization);

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        for (Meter meter : meterForTestList) {
            stendDLLCommands.constTestStart(meter.getId(), constantMeter);
        }

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (runTestToTime) {

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    String[] kw;
                    if (constantThread.isAlive()) {
                        for (Meter meter : meterForTestList) {
                            kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                            double result = Double.parseDouble(kw[0]);

                            if (result == 0) {
                                ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                TestErrorTableFrameController.getStaticBtnStop().fire();
                                timer.cancel();
                            }
                        }
                        timer.cancel();
                    } else timer.cancel();
                }
            };

            //Передаю параметры, которые должны быть выставлены
            TestErrorTableFrameController.transferParam(this);

            if (stendDLLCommands instanceof ThreePhaseStend) {
                if (!threePhaseCommand) {

                    iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                    switch (iABC) {
                        case "A": voltPerA = voltPer; break;
                        case "B": voltPerB = voltPer; break;
                        case "C": voltPerC = voltPer; break;
                    }

                    stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
                } else {
                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            } else {
                if (iABC.equals("H")) {
                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("A")) {
                    stendDLLCommands.selectCircuit(0);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("B")) {
                    stendDLLCommands.selectCircuit(1);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            }

            TestErrorTableFrameController.refreshRefMeterParameters();

            timer.schedule(timerTask, 10000);

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + timeTheTest;

            while (System.currentTimeMillis() < timeEnd) {

                TestErrorTableFrameController.refreshRefMeterParameters();

                if (refMeterCount % 7 == 0) {
                    TestErrorTableFrameController.refreshRefMeterParameters();
                }

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

                refMeterCount++;

                Thread.sleep(275);
            }

            stendDLLCommands.powerOf();

            TestErrorTableFrameController.refreshRefMeterParametersWithoutChecking();

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

            //Если тест запущен по количеству энергии
        } else {

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!constantThread.isAlive()) {
                        for (Meter meter : meterForTestList) {
                            double result = Double.parseDouble(meter.returnResultCommand(index, channelFlag).getLastResultForTabView().substring(1));
                            if (result == 0) {
                                ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                TestErrorTableFrameController.getStaticBtnStop().fire();
                                timer.cancel();
                            }
                        }

                        timer.cancel();
                    } else timer.cancel();
                }
            };

            double refMeterEnergy = 0;

            TestErrorTableFrameController.transferParam(this);

            if (stendDLLCommands instanceof ThreePhaseStend) {
                if (!threePhaseCommand) {

                    iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                    switch (iABC) {
                        case "A": voltPerA = voltPer; break;
                        case "B": voltPerB = voltPer; break;
                        case "C": voltPerC = voltPer; break;
                    }

                    stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
                } else {
                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            } else {

                if (iABC.equals("H")) {
                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("A")) {
                    stendDLLCommands.selectCircuit(0);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("B")) {
                    stendDLLCommands.selectCircuit(1);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            }

            TestErrorTableFrameController.refreshRefMeterParameters();

            timer.schedule(timerTask, 7000);

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            Meter.ConstantResult constantResult;
            String[] kw;
            double hightKw;

            while (kWToTest > refMeterEnergy) {
                TestErrorTableFrameController.refreshRefMeterParameters();

                if (refMeterCount % 4 == 0) {
                    TestErrorTableFrameController.refreshRefMeterParameters();
                }

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (Meter meter : meterForTestList) {
                    kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                    if (kw.length == 2) {
                        constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                        constantResult.setLastResultForTabView("N" + kw[0]);

                        hightKw = Double.parseDouble(kw[1]);

                        if (hightKw > refMeterEnergy) {
                            refMeterEnergy = hightKw;
                        }
                    }
                }

                refMeterCount++;

                Thread.sleep(1000);
            }

            stendDLLCommands.powerOf();

            TestErrorTableFrameController.refreshRefMeterParametersWithoutChecking();

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

        stendDLLCommands.errorClear();

        TestErrorTableFrameController.refreshRefMeterParametersWithoutChecking();
    }

    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        constantThread = Thread.currentThread();

        int countResult = 1;

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

                //Передаю необходимые параметры для эталонного счётчика
                ((ThreePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters(
                        voltPerA * (ratedVolt / 100), voltPerB * (ratedVolt / 100), voltPerC * (ratedVolt / 100), 0.0, 0.0, 0.0);

                stendDLLCommands.getUIWithPhase(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
            } else {

                //Передаю необходимые параметры для эталонного счётчика
                ((ThreePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters(
                        voltPer * (ratedVolt / 100), voltPer * (ratedVolt / 100), voltPer * (ratedVolt / 100), 0.0, 0.0, 0.0);

                stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        } else {
            ((OnePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters(
                    voltPer * (ratedVolt / 100), 0.0);

            stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP);
        }

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(TestErrorTableFrameController.timeToStabilization);

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        TestErrorTableFrameController.refreshRefMeterParameters();

        while (Thread.currentThread().isAlive()) {

            timer = new Timer(true);

            int refMeterCount = 1;

            for (Meter meter : meterForTestList) {
                stendDLLCommands.constTestStart(meter.getId(), constantMeter);
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            if (runTestToTime) {

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        String[] kw;
                        if (!constantThread.isAlive()) {
                            for (Meter meter : meterForTestList) {
                                kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                                double result = Double.parseDouble(kw[0]);

                                if (result == 0) {
                                    ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                    TestErrorTableFrameController.getStaticBtnStop().fire();
                                    timer.cancel();
                                }
                            }
                            timer.cancel();
                        } else timer.cancel();
                    }
                };

                TestErrorTableFrameController.refreshRefMeterParameters();

                TestErrorTableFrameController.transferParam(this);

                if (stendDLLCommands instanceof ThreePhaseStend) {
                    if (!threePhaseCommand) {

                        iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                        switch (iABC) {
                            case "A": voltPerA = voltPer; break;
                            case "B": voltPerB = voltPer; break;
                            case "C": voltPerC = voltPer; break;
                        }

                        stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
                    } else {
                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);
                    }
                } else {
                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }

                timer.schedule(timerTask, 10000);

                TestErrorTableFrameController.refreshRefMeterParameters();

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                timeStart = System.currentTimeMillis();
                timeEnd = timeStart + timeTheTest;

                while (System.currentTimeMillis() < timeEnd) {

                    if (refMeterCount % 7 == 0) {
                        TestErrorTableFrameController.refreshRefMeterParameters();
                    }

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

                    refMeterCount++;

                    Thread.sleep(275);
                }

                stendDLLCommands.powerOf();

                TestErrorTableFrameController.refreshRefMeterParameters();

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

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (!constantThread.isAlive()) {
                            for (Meter meter : meterForTestList) {
                                double result = Double.parseDouble(meter.returnResultCommand(index, channelFlag).getLastResultForTabView().substring(1));
                                if (result == 0) {
                                    ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                    TestErrorTableFrameController.getStaticBtnStop().fire();
                                    timer.cancel();
                                }
                            }

                            timer.cancel();
                        } else timer.cancel();
                    }
                };

                TestErrorTableFrameController.transferParam(this);

                if (stendDLLCommands instanceof ThreePhaseStend) {
                    if (!threePhaseCommand) {

                        iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                        switch (iABC) {
                            case "A": voltPerA = voltPer; break;
                            case "B": voltPerB = voltPer; break;
                            case "C": voltPerC = voltPer; break;
                        }

                        stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
                    } else {
                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);
                    }
                } else {
                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }

                timer.schedule(timerTask, 7000);

                TestErrorTableFrameController.refreshRefMeterParameters();

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                Meter.ConstantResult constantResult;
                String[] kw;
                double hightKw;

                while (kWToTest > refMeterEnergy) {

                    if (refMeterCount % 4 == 0) {
                        TestErrorTableFrameController.refreshRefMeterParameters();
                    }

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    for (Meter meter : meterForTestList) {
                        kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                        if (kw.length == 2) {
                            constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                            constantResult.setLastResultForTabView("N" + kw[0]);

                            hightKw = Double.parseDouble(kw[1]);

                            if (hightKw > refMeterEnergy) {
                                refMeterEnergy = hightKw;
                            }
                        }
                    }

                    refMeterCount++;

                    Thread.sleep(1000);
                }

                stendDLLCommands.powerOf();

                TestErrorTableFrameController.refreshRefMeterParameters();

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

            TestErrorTableFrameController.refreshRefMeterParametersWithoutChecking();

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            Thread.sleep(5000);

            countResult++;

            TestErrorTableFrameController.transferParam(this);

            if (stendDLLCommands instanceof ThreePhaseStend) {
                if (!threePhaseCommand) {

                    iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                    switch (iABC) {
                        case "A": voltPerA = voltPer; break;
                        case "B": voltPerB = voltPer; break;
                        case "C": voltPerC = voltPer; break;
                    }

                    //Передаю необходимые параметры для эталонного счётчика
                    ((ThreePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters(
                            voltPerA * (ratedVolt / 100), voltPerB * (ratedVolt / 100), voltPerC * (ratedVolt / 100), 0.0, 0.0, 0.0);

                    stendDLLCommands.getUIWithPhase(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
                } else {

                    //Передаю необходимые параметры для эталонного счётчика
                    ((ThreePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters(
                            voltPer * (ratedVolt / 100), voltPer * (ratedVolt / 100), voltPer * (ratedVolt / 100), 0.0, 0.0, 0.0);

                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            } else {
                ((OnePhaseStendRefParamController) TestErrorTableFrameController.getRefMeterController()).transferParameters(
                        voltPer * (ratedVolt / 100), 0.0);

                stendDLLCommands.getUI(phase, ratedVolt, 0.0, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }

            TestErrorTableFrameController.refreshRefMeterParametersWithoutChecking();

            stendDLLCommands.errorClear();

            Thread.sleep(7000);

            TestErrorTableFrameController.refreshRefMeterParametersWithoutChecking();
        }
    }

    private String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
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

    public boolean isRunTestToTime() {
        return runTestToTime;
    }

    public long getTimeTheTest() {
        return timeTheTest;
    }

    public String getTimeTheTestHHmmss() {
        return getTime(timeTheTest);
    }

    public double getkWToTest() {
        return kWToTest;
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

    public double getVoltPer() {
        return voltPer;
    }

    public double getCurrPer() {
        return currPer;
    }

    @Override
    public double getRatedVolt() {
        return ratedVolt;
    }

    @Override
    public double getVoltPerA() {
        return voltPerA;
    }

    @Override
    public double getVoltPerB() {
        return voltPerB;
    }

    @Override
    public double getVoltPerC() {
        return voltPerC;
    }

    @Override
    public double getRatedCurr() {
        return ratedCurr;
    }

    @Override
    public String getiABC() {
        return iABC;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }

    public boolean isThreePhaseCommand() {
        return threePhaseCommand;
    }
}
