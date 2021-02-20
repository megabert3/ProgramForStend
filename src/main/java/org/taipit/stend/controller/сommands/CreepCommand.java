package org.taipit.stend.controller.сommands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.StendConnectionException;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.TimeUnit;


/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за реализацию выполнения команды "Самоход".
 * Необходимо подать напряжение 115 Un, на некоторое время, если счётчик не выдал больше двух импульсов за отведённое время
 * значит тест пройден.
 * Подробнее о данном испытании написано в ГОСТ 31819-21
 *
 * За дополнительной информацией описания полей см. интерфейс Commands
 */
public class CreepCommand implements Commands, Serializable, Cloneable {

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private StendDLLCommands stendDLLCommands;

    //лист с счётчиками
    private List<Meter> meterList;

    //Режим
    private int phase;

    //Напряжение
    private double ratedVolt;

    //Ток
    private double ratedCurr;

    //Частота
    private double ratedFreq;

    //Необходимо сделать в доп тестовом окне
    private int phaseSrequence;

    //Направление тока
    private int revers;

    //Напряжение на фазе А
    private double voltPerA;

    //Напряжение на фазе B
    private double voltPerB;

    //Напряжение на фазе C
    private double voltPerC;

    //Процент от номинального напряжения
    private double voltPer;

    //Процен от тока
    private double currPer;

    //Коэфициент мощности
    private String cosP = "1.0";
    //Фазы на которые необходимо подать напряжене
    private String iABC = "H";
    //Импульсный выход установки (активная/реактивная энергия)
    private int channelFlag;

    private boolean active = true;

    //Имя точки для отображения в таблице
    private String name;

    private String id;

    //Время расчитывается по госту? (если нет значит задано пользователем)
    private boolean gostTest;

    //Количество импоульсов для провала теста
    private int pulseValue;

    //Время теста введённое пользователем
    private long userTimeTest;
    private long timeStart;
    private long timeEnd;

    private Timer timer;
    private TimerTask timerTask;
    private Thread currThread;

    //Мапа с результатами прохождения теста для каждого места на установке
    private HashMap<Integer, Boolean> creepCommandResult;

    /**
     *
     * @param threePhaseCommand - команда создана для трёхфазного стенда?
     * @param gostTest - время теста расчитывается по ГОСТУ или задано самим пользователем
     * @param name - имя точки испытания для отображения в таблице точек
     * @param id - для добавления или удаления испытания
     * @param channelFlag - Импульсный выход установки (активная/реактивная энергия, прямое/обратное направление тока)
     * @param userTimeTest - Время теста введённое пользователем
     * @param pulseValue - Количество импульсов для провала теста
     * @param voltPer - процент от напряжения
     */
    public CreepCommand(boolean threePhaseCommand, boolean gostTest, String name, String id, int channelFlag, long userTimeTest, int pulseValue, double voltPer) {
        this.threePhaseCommand = threePhaseCommand;
        this.name = name;
        this.id = id;
        this.gostTest = gostTest;
        this.channelFlag = channelFlag;
        this.userTimeTest = userTimeTest;
        this.pulseValue = pulseValue;
        this.voltPer = voltPer;
    }

    /**
     * Используется если испытание выполняется по ГОСТУ
     * @param threePhaseCommand - команда создана для трёхфазного стенда?
     * @param gostTest - время теста расчитывается по ГОСТУ или задано самим пользователем
     * @param name - имя точки испытания для отображения в таблице точек
     * @param id - для добавления или удаления испытания
     * @param channelFlag - - Импульсный выход установки (активная/реактивная энергия, прямое/обратное направление тока)
     */
    public CreepCommand(boolean threePhaseCommand, boolean gostTest, String name, String id,  int channelFlag) {
        this.threePhaseCommand = threePhaseCommand;
        this.gostTest = gostTest;
        this.name = name;
        this.id = id;
        this.channelFlag = channelFlag;
        this.pulseValue = 2;
        this.voltPer = 115;
    }

    @Override
    public void execute() throws StendConnectionException, InterruptedException {

        currThread = Thread.currentThread();

        int refMeterCount = 1;

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Номер измерения
        int countResult = 1;
        Meter.CreepResult creepResult;

        creepCommandResult = initCreepCommandResult();

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        // Если установка трехвазная
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
            stendDLLCommands.selectCircuit(0);

            stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP);
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.errorClear();

        //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию
        setDefTestResults(channelFlag, index);

        //Устанавливаю режим теста в зависимости от количества импульсов для провала
        setTestMode();

        Thread.sleep(3000);

        timeStart = System.currentTimeMillis();
        timeEnd = timeStart + userTimeTest;

        timer = new Timer(true);

        //Таймер для отображения времени до окончания теста в GUI
        timerTask = new TimerTask() {
            @Override
            public void run() {
                Meter.CommandResult creepResult;
                if (currThread.isAlive()) {
                    for (Map.Entry<Integer, Boolean> flag : creepCommandResult.entrySet()) {
                        if (flag.getValue()) {
                            creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlag);
                            creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                        }
                    }
                } else {
                    timer.cancel();
                }
            }
        };

        timer.schedule(timerTask, 0, 275);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Непосредственно само выполнение теста
        if (pulseValue == 1) {
            startTestModeSearchMark(countResult);
        } else {
            startTestModeCount(countResult);
        }

        timer.cancel();

        //Выставляю результат теста счётчиков, которые прошли тест
        for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
            if (mapResultPass.getValue()) {
                creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
            }
        }

        stendDLLCommands.errorClear();
    }

    /**
     * Логика работы такая же как и у команды execute()
     * Более подробное описание см. там
     * @throws StendConnectionException
     * @throws InterruptedException
     */
    @Override
    public void executeForContinuousTest() throws StendConnectionException, InterruptedException {

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        currThread = Thread.currentThread();

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        //Номер измерения
        int countResult = 1;
        Meter.CreepResult creepResult;

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
            stendDLLCommands.selectCircuit(0);

            stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP);
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep(3000);

        while (Thread.currentThread().isAlive()) {

            int refMeterCount = 1;

            //Устанавливаю значения tableColumn, флаги и погрешности по умолчанию.
            setDefTestResults(channelFlag, index);

            setTestMode();

            creepCommandResult = initCreepCommandResult();

            timeStart = System.currentTimeMillis();
            timeEnd = timeStart + userTimeTest;

            timer = new Timer(true);


            timerTask = new TimerTask() {
                @Override
                public void run() {
                    Meter.CommandResult creepResult;
                    if (currThread.isAlive()) {
                        for (Map.Entry<Integer, Boolean> flag : creepCommandResult.entrySet()) {
                            if (flag.getValue()) {
                                creepResult = meterList.get(flag.getKey() - 1).returnResultCommand(index, channelFlag);
                                creepResult.setLastResultForTabView("N" + getTime(timeEnd - System.currentTimeMillis()));
                            }
                        }
                    } else {
                        timer.cancel();
                    }
                }
            };

            timer.schedule(timerTask, 0, 275);

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            if (pulseValue == 1) {
                startTestModeSearchMark(countResult);
            } else {
                startTestModeCount(countResult);
            }

            timer.cancel();

            //Выставляю результат теста счётчиков, которые прошли тест
            for (Map.Entry<Integer, Boolean> mapResultPass : creepCommandResult.entrySet()) {
                if (mapResultPass.getValue()) {
                    creepResult = (Meter.CreepResult) meterList.get(mapResultPass.getKey() - 1).returnResultCommand(index, channelFlag);
                    creepResult.setResultCreepCommand(creepResult.getTimeTheTest(), countResult, true);
                }
            }

            //Время на подумать оставить результаты или нет
            Thread.sleep(7000);

            countResult++;
        }
    }

    /**
     * Выставляет начальные значения результата теста
     * @return
     */
    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterList.size());
        for (Meter meter : meterList) {
            init.put(meter.getId(), true);
        }
        return init;
    }

    /**
     * В зависимости от количества импульсов для провала теста
     * выбирает режим работы установки,
     * если необходимо больше одного импульса, то необходимо выбирать команду
     * countStart иначе searchMark
     * @throws StendConnectionException
     */
    private void setTestMode() throws StendConnectionException {
        if (pulseValue == 1) {
            for (Meter meter : meterList) {
                stendDLLCommands.searchMark(meter.getId());
            }
        } else {
            for (Meter meter : meterList) {
                stendDLLCommands.countStart(meter.getId());
            }
        }
    }

    /**
     * Логика работы по поску более одного импульса, если счётчик выдал более одного импульса, то считается, что он провалил тест
     * @param countResult - номер измерения
     * @throws InterruptedException
     * @throws StendConnectionException
     */
    private void startTestModeCount(int countResult) throws InterruptedException, StendConnectionException {
        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (mapResult.getValue()) {

                    if (stendDLLCommands.countRead(mapResult.getKey()) >= pulseValue - 1) {

                        creepCommandResult.put(mapResult.getKey(), false);

                        Meter.CreepResult creepResult = (Meter.CreepResult) meterList.get(mapResult.getKey() - 1).returnResultCommand(index, channelFlag);

                        creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                    }
                }
            }

            Thread.sleep(400);
        }
    }

    /**
     * Логика работы по поску одного импульса, если счётчик выдал один импульс, то считается, что он провалил тест
     * @param countResult - номер измерения
     * @throws InterruptedException
     */
    private void startTestModeSearchMark(int countResult) throws InterruptedException {
        //Продолжать тест пока либо все счётчики не провалили его либо не вышло время испытания
        while (creepCommandResult.containsValue(true) && System.currentTimeMillis() <= timeEnd) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Map.Entry<Integer, Boolean> mapResult : creepCommandResult.entrySet()) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (mapResult.getValue()) {

                    if (stendDLLCommands.searchMarkResult(mapResult.getKey())) {

                        creepCommandResult.put(mapResult.getKey(), false);

                        Meter.CreepResult creepResult = (Meter.CreepResult) meterList.get(mapResult.getKey() - 1).returnResultCommand(index, channelFlag);

                        creepResult.setResultCreepCommand(getTime(System.currentTimeMillis() - timeStart), countResult, false);
                    }
                }
            }

            Thread.sleep(400);
        }
    }

    /**
     * Выставляет изначальные, стартовые значения результатам счётчиков
     * необходим если пользователь выбрал повторить тест
     * @param channelFlag
     * @param index
     */
    private void setDefTestResults(int channelFlag, int index) {
        for (Meter meter : meterList) {
            Meter.CommandResult creepResult = meter.returnResultCommand(index, channelFlag);
            creepResult.setLastResultForTabView("N");
            creepResult.setPassTest(null);
            creepResult.setLastResult("");
        }
    }

    /**
     * Переводит миллисекунды в формат hh:mm:ss
     * @param time - время в миллисекундах
     * @return
     */
    private String getTime(long time) {
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(time),
                TimeUnit.MILLISECONDS.toMinutes(time) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(time) % TimeUnit.MINUTES.toSeconds(1));
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public String getPauseForStabilization() {
        return "";
    }

    @Override
    public void setPauseForStabilization(double pauseForStabilization) {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public long getUserTimeTest() {
        return userTimeTest;
    }

    public String getUserTimeTestHHmmss() {
        return getTime(userTimeTest);
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

    public void setUserTimeTest(long userTimeTest) {
        this.userTimeTest = userTimeTest;
    }

    @Override
    public void setPulse(String pulse) {

    }

    @Override
    public void setCountResult(String countResult) {

    }

    @Override
    public void setEmax(String emax) {

    }

    @Override
    public void setEmin(String emin) {

    }

    public String getId() {
        return id;
    }

    public void setiABC(String iABC) {
        this.iABC = iABC;
    }

    public void setRatedCurr(double ratedCurr) {
        this.ratedCurr = ratedCurr;
    }

    public void setPhaseSrequence(int phaseSrequence) {
        this.phaseSrequence = phaseSrequence;
    }

    public void setRevers(int revers) {
        this.revers = revers;
    }

    public void setVoltPerA(double voltPerA) {
        this.voltPerA = voltPerA;
    }

    public void setVoltPerB(double voltPerB) {
        this.voltPerB = voltPerB;
    }

    public void setVoltPerC(double voltPerC) {
        this.voltPerC = voltPerC;
    }

    public void setCurrPer(double currPer) {
        this.currPer = currPer;
    }

    public void setCosP(String cosP) {
        this.cosP = cosP;
    }

    public int getChannelFlag() {
        return channelFlag;
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