package org.taipit.stend.controller.сommands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.StendConnectionException;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @autor Albert Khalimov
 * 
 * Данный класс отвечает за реализацию выполнения команды "Проверка счётного механизма".
 * Необходимо подать напряжение и ток либо на определённое время, либо на определённое количество времени,
 * затем сравнить то, что посчитал счётчик(и) и то, что посчитал поверосный стенд и уже исходя из этого определять прошёл ли счётчик испытание или нет
 *
 * За дополнительной информацией описания полей см. интерфейс Commands
 */

public class ConstantCommand implements Commands, Serializable, Cloneable {

    private StendDLLCommands stendDLLCommands;

    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand (позволяет быстро записать результат команды)
    private int index;

    //Эта команда будет проходить по времени?
    private boolean runTestToTime;
    //Количество энергии, которое необходимо для испытания
    private double kWToTest;

    //Лист с счётчиками для испытания
    private List<Meter> meterForTestList;

    //Максимальный порог ошибки
    private double eminProc;

    //Минимальный порог ошибки
    private double emaxProc;

    //Кол-во импульсов для расчёта ошибки (зарезервированно)
    private int pulse;

    //Имя точки испытания для отображения в таблице при создании методики поверки
    private String name;

    //id check box'a (для удаления или добавления точки)
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
    //Порядок следования фаз
    private int phaseSrequence;
    //Направление тока
    private int revers;
    //По каким фазам пустить ток
    private String iABC = "H";

    //Активная ли точка
    private boolean active = true;

    //Импульсный выход установки
    private int channelFlag;

    //Количество повторов теста
    private int countResult;

    //Константа счётчика для теста
    private int constantMeter;

    //Время испытания (введённое пользователем)
    private long timeTheTest;

    //Время начала теста
    private long timeStart;
    //Время окончания теста
    private long timeEnd;
    //Текущее время
    private long currTime;
    //Время теста для отображения в таблице (GUI)
    private String strTime;

    //Ссылка на текущую нить для проверки не подана ли команда стоп
    transient private Thread constantThread;

    transient private Timer timer;

    /**
     * Конструкторы
     * @param threePhaseStendCommand - команда создана для трёхфазного стенда?
     * @param runTestToTime - команду необходимо выполнить по количеству времени (иначе по энергии)
     * @param timeToTest - время теста
     * @param id - для быстрого поиска в методике поверки
     * @param name - имя для отображения в таблице (GUI)
     * @param voltPer - процент от номинального напряжения
     * @param currPer - процент от номинального тока
     * @param revers - обратное направление
     * @param channelFlag - имнульсный выход установки (активная, реактивная энергия)
     * @param eminProc - нижняя граница погрешности для прохождения теста счётчиком
     * @param emaxProc - верхняя граница погрешности для прохождения теста счётчиком
     */
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

    /**
     * Если необходимо провести тест по кол-ву энергии
     * @param threePhaseStendCommand
     * @param runTestToTime
     * @param kWToTest - количество энегрии для теста
     * @param id - см. выше
     * @param name - см. выше
     * @param voltPer - см. выше
     * @param currPer - см. выше
     * @param revers - см. выше
     * @param channelFlag - см. выше
     * @param eminProc - см. выше
     * @param emaxProc - см. выше
     */
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

    /**
     * Необходмо разбить на более мелкие методы
     */
    //===================================================================================================
    //Команда выполнения для последовательного теста
    @Override
    public void execute() throws StendConnectionException, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        timer = new Timer(true);
        constantThread = Thread.currentThread();

        try {
            //Выбор константы в зависимости от энергии
            if (channelFlag < 2) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
            } else {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
            }

        } catch (NumberFormatException e) {
            constantMeter = 0;
        }

        //Номер результата теста
        int countResult = 1;

        //Не нажал ли пользователь кнопку стоп
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        /**
         * Даю счётчикам включиться
         */
        //Если испытание проводится на трёхфазном стенде
        if (stendDLLCommands instanceof ThreePhaseStend) {
            //Но при этом команда создана не для трёхфазного стенда
            if (!threePhaseCommand) {

                //Устанавливаю ту фазу для испытания, которую выбрал пользователь
                iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

                /**
                 * Переделать реализацию
                 */

                //Устанавливаю необходимые ток и напряжение для испытания
                stendDLLCommands.getUIWithPhase(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
            } else {

                stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
            //Если испытание проводится на однофазном стенде
        } else {

            if (iABC.equals("H")) {
                stendDLLCommands.selectCircuit(0);

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

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Пауза для включения счётчиков (необходимо вывести в GUI)
        Thread.sleep(5000);

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        for (Meter meter : meterForTestList) {
            stendDLLCommands.constTestStart(meter.getId(), constantMeter);
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Если команду необходимо выполнить по определённому количеству времени
        if (runTestToTime) {

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    String[] kw;

                    if (constantThread.isAlive()) {
                        for (Meter meter : meterForTestList) {
                            try {
                                kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                                double result = Double.parseDouble(kw[0]);

                                if (result == 0.0) {
                                    ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                    timer.cancel();
                                }
                            } catch (StendConnectionException connectForStendExeption) {
                                connectForStendExeption.printStackTrace();
                            }
                        }
                        timer.cancel();
                    } else timer.cancel();
                }
            };

            //Выставляю напряжение и ток необходимые для проведения испытания
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
                    stendDLLCommands.selectCircuit(0);

                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("A")) {
                    stendDLLCommands.selectCircuit(0);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("B")) {
                    stendDLLCommands.selectCircuit(1);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            //Запускаю проверку на то, что все счётчики запустили и передают показания
            timer.schedule(timerTask, 10000);

            //Устанавливаю время теста
            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + timeTheTest;

            //Отображаю время испытаний в GUI
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

                Thread.sleep(275);
            }

            stendDLLCommands.powerOf();

            //Получаю результат теста для каждого счётчика
            Double result;
            String kwRefMeter;
            String kwMeter;

            for (Meter meter : meterForTestList) {

                String[] kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                Meter.ConstantResult constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                kwRefMeter = kw[1];
                kwMeter = kw[0];

                if (Double.parseDouble(kwMeter) != 0) {
                    if (result < eminProc || result > emaxProc) {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                    } else {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                    }
                } else {
                    constantResult.setResultConstantCommand("Нет импульсов", countResult, null, channelFlag, "", "");
                }
            }

            /**Если тест запущен по количеству энергии
             * выполнение практически такое же, что и по времени
             */
        } else {

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (constantThread.isAlive()) {

                        for (Meter meter : meterForTestList) {
                            try {
                               double result = Double.parseDouble(meter.returnResultCommand(index, channelFlag).getLastResultForTabView().substring(1));

                                if (result == 0) {
                                    ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                    timer.cancel();
                                }
                            }catch (NumberFormatException ignore) {}

                        }
                        timer.cancel();
                    } else timer.cancel();
                }
            };

            double refMeterEnergy = 0;

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
                    stendDLLCommands.selectCircuit(0);

                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("A")) {
                    stendDLLCommands.selectCircuit(0);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);

                } else if (iABC.equals("B")) {
                    stendDLLCommands.selectCircuit(1);
                    iABC = "H";

                    stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            timer.schedule(timerTask, 7000);

            Meter.ConstantResult constantResult;
            String[] kw;
            double hightKw;

            //Тут я постоянно считывю энергию, которую посчитал сам стенд и если она равна энергии теста ,то выключаю ток и считываю, что насчитал сам счётчик
            while (kWToTest > refMeterEnergy) {


                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                for (Meter meter : meterForTestList) {
                    kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");

                    constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                    constantResult.setLastResultForTabView("N" + kw[0]);

                    if (Double.parseDouble(kw[0]) != 0) {

                        hightKw = Double.parseDouble(kw[1]);

                        if (hightKw > refMeterEnergy) {
                            refMeterEnergy = hightKw;
                        }
                    }
                }

                Thread.sleep(1000);
            }

            stendDLLCommands.powerOf();

            //Получаю результат
            double result;
            String kwRefMeter;
            String kwMeter;

            for (Meter meter : meterForTestList) {

                kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                kwRefMeter = kw[1];
                kwMeter = kw[0];

                if (Double.parseDouble(kwMeter) != 0) {
                    if (result < eminProc || result > emaxProc) {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                    } else {
                        constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                    }
                } else {
                    constantResult.setResultConstantCommand("Нет импульсов", countResult, null, channelFlag, "", "");
                }
            }
        }

        stendDLLCommands.errorClear();
    }


    /**
     * Метод для цикличной поверки счётчиков, реализация такая же, что и для обычного теста, но только в цикле
     * коментарии см. там
     * @throws StendConnectionException
     * @throws InterruptedException
     */
    @Override
    public void executeForContinuousTest() throws StendConnectionException, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        try {
            //Выбор константы в зависимости от энергии
            if (channelFlag < 2) {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
            } else {
                constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
            }
        }catch (NumberFormatException e) {
            constantMeter = 0;
        }

        constantThread = Thread.currentThread();

        int countResult = 1;

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                switch (iABC) {
                    case "A": voltPerA = voltPer; break;
                    case "B": voltPerB = voltPer; break;
                    case "C": voltPerC = voltPer; break;
                }

                stendDLLCommands.getUIWithPhase(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
            } else {

                stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        } else {

            if (iABC.equals("H")) {
                stendDLLCommands.selectCircuit(0);

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

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(5000);

        for (Meter meter : meterForTestList) {
            meter.returnResultCommand(index, channelFlag).setLastResultForTabView("N");
        }

        while (Thread.currentThread().isAlive()) {

            timer = new Timer(true);

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
                        if (constantThread.isAlive()) {
                            for (Meter meter : meterForTestList) {
                                try {
                                    kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                                    double result = Double.parseDouble(kw[0]);

                                    if (result == 0) {
                                        ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                        timer.cancel();
                                    }
                                } catch (StendConnectionException connectForStendExeption) {
                                    connectForStendExeption.printStackTrace();
                                }
                            }
                            timer.cancel();
                        } else timer.cancel();
                    }
                };

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
                        stendDLLCommands.selectCircuit(0);

                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);

                    } else if (iABC.equals("A")) {
                        stendDLLCommands.selectCircuit(0);
                        iABC = "H";

                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);

                    } else if (iABC.equals("B")) {
                        stendDLLCommands.selectCircuit(1);
                        iABC = "H";

                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);
                    }
                }

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                timer.schedule(timerTask, 10000);

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

                    Thread.sleep(275);
                }

                stendDLLCommands.powerOf();

                //Получаю результат
                double result;
                String kwRefMeter;
                String kwMeter;

                for (Meter meter : meterForTestList) {

                    String[] kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                    result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                    Meter.ConstantResult constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                    kwRefMeter = kw[1];
                    kwMeter = kw[0];

                    if (Double.parseDouble(kwMeter) != 0) {
                        if (result < eminProc || result > emaxProc) {
                            constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                        } else {
                            constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                        }
                    } else {
                        constantResult.setResultConstantCommand("Нет импульсов", countResult, null, channelFlag, "", "");
                    }
                }

            } else {
                double refMeterEnergy = 0;

                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (constantThread.isAlive()) {
                            for (Meter meter : meterForTestList) {
                                try {
                                    double result = Double.parseDouble(meter.returnResultCommand(index, channelFlag).getLastResultForTabView().substring(1));

                                    if (result == 0) {
                                        ConsoleHelper.infoException("Не поступают импульсы с места № " + meter.getId());
                                        timer.cancel();
                                    }
                                }catch (NumberFormatException ignore) {}
                            }
                            timer.cancel();
                        } else timer.cancel();
                    }
                };

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
                        stendDLLCommands.selectCircuit(0);

                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);

                    } else if (iABC.equals("A")) {
                        stendDLLCommands.selectCircuit(0);
                        iABC = "H";

                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);

                    } else if (iABC.equals("B")) {
                        stendDLLCommands.selectCircuit(1);
                        iABC = "H";

                        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                                voltPer, currPer, iABC, cosP);
                    }
                }

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                timer.schedule(timerTask, 10000);

                Meter.ConstantResult constantResult;
                String[] kw;
                double hightKw;

                while (kWToTest > refMeterEnergy) {

                    if (Thread.currentThread().isInterrupted()) {
                        throw new InterruptedException();
                    }

                    for (Meter meter : meterForTestList) {
                        kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");


                        constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);

                        constantResult.setLastResultForTabView("N" + kw[0]);

                        if (Double.parseDouble(kw[0]) != 0) {
                            hightKw = Double.parseDouble(kw[1]);

                            if (hightKw > refMeterEnergy) {
                                refMeterEnergy = hightKw;
                            }
                        }
                    }

                    Thread.sleep(1000);
                }

                stendDLLCommands.powerOf();

                //Получаю результат
                double result;
                String kwRefMeter;
                String kwMeter;

                for (Meter meter : meterForTestList) {

                    kw = stendDLLCommands.constStdEnergyRead(constantMeter, meter.getId()).split(",");
                    result = stendDLLCommands.constProcRead(constantMeter, meter.getId());
                    constantResult = (Meter.ConstantResult) meter.returnResultCommand(index, channelFlag);
                    kwRefMeter = kw[1];
                    kwMeter = kw[0];

                    if (Double.parseDouble(kwMeter) != 0) {
                        if (result < eminProc || result > emaxProc) {
                            constantResult.setResultConstantCommand(String.valueOf(result), countResult, false, channelFlag, kwRefMeter, kwMeter);
                        } else {
                            constantResult.setResultConstantCommand(String.valueOf(result), countResult, true, channelFlag, kwRefMeter, kwMeter);
                        }
                    } else {
                        constantResult.setResultConstantCommand("Нет импульсов", countResult, null, channelFlag, "", "");
                    }
                }
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            Thread.sleep(5000);

            countResult++;

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            if (stendDLLCommands instanceof ThreePhaseStend) {
                if (!threePhaseCommand) {

                    iABC = TestErrorTableFrameController.phaseOnePhaseMode;

                    switch (iABC) {
                        case "A": voltPerA = voltPer; break;
                        case "B": voltPerB = voltPer; break;
                        case "C": voltPerC = voltPer; break;
                    }

                    stendDLLCommands.getUIWithPhase(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);
                } else {

                    stendDLLCommands.getUI(phase, ratedVolt, 0, ratedFreq, phaseSrequence, revers,
                            voltPer, currPer, iABC, cosP);
                }
            } else {

                if (iABC.equals("H")) {
                    stendDLLCommands.selectCircuit(0);

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

            stendDLLCommands.errorClear();

            Thread.sleep(7000);
        }
    }

    //Парсит миллисекунды в формат hh:mm:ss
    private String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }

    //Устанавливает команде стенд на котором выполняется поверка счётчиков
    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }
    //Устанавливает напряжение
    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }
    //Устанавливает частоту
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

    public String getPauseForStabilization() {
        return "";
    }

    @Override
    public void setPauseForStabilization(double pauseForStabilization) {
    }
}
