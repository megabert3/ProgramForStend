package org.taipit.stend.controller.сommands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.StendConnectionException;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за реализацию выполнения команды "Погрешность".
 * Поверочный стенд выставляет параметры напряжения, тока и др. согласно пользовательским
 * и в дальнейшем сравнивает мощность посчитанную счетчиком(и) с той, которую посчитал эталонный счётчик.
 * В результате расчитывается относительная погрешность счётчика
 *
 * За дополнительной информацией описания полей см. интерфейс Commands
 */
public class ErrorCommand implements Commands, Serializable, Cloneable {

    private StendDLLCommands stendDLLCommands;

    //Эта команда из методики для трёхфазного теста?
    private boolean threePhaseCommand;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Параметр влияния (частота или напряжение)
    private String param = "";

    private double procentParan;

    //Лист с счётчиками для испытания
    private List<Meter> meterForTestList;

    //Максимальный порог ошибки
    private double emin = -1.0;

    //Минимальный порог ошибки
    private double emax = 1.0;

    //Кол-во импульсов для расчёта ошибки
    private int pulse = 5;

    //Время для стабилизации параметров необходимых для испытания
    private double pauseForStabilization = 2;

    //Имя точки для отображения в таблице
    private String name;

    //Базовый или максимальный ток из конструктора
    private String current;

    //Стринговый процент получаемый из конструктора
    private String currentPerсent;

    //id кнопки
    private String id;

    //Базовый ток
    private double Ib;

    //Максимальный ток
    private double Imax;

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

    //Процент от напряжения
    private double voltPer;

    //Процен от тока
    private double currPer;

    //По каким фазам пустить ток
    private String iABC;

    //Коэфициент мощности
    private String cosP;

    //Активная ли точка
    private boolean active = true;

    //Импульсный выход
    private int channelFlag;

    //Количество повторов теста
    private int countResult = 2;

    //Константа счётчика для теста
    private int constantMeter;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop;

    /**
     * Конструктор для формирования параметров точки испытания
     * @param threePhaseStendCommand - команда для трехфазного стенда?
     * @param id - для добавления или удаления точки испытания
     * @param phase - режим работы стенда
     * @param current - ток
     * @param revers - направление
     * @param currentPercent - процент от тока
     * @param iABC - фазы, на которые необходимо подать ток
     * @param cosP - COSФ или коэфициент активной мощности
     * @param channelFlag - Импульсный выход установки (активная/реактивная энергия, прямое/обратное направление тока)
     *
     * Так же информацию можно найти в stendDLL
     */
    public ErrorCommand(boolean threePhaseStendCommand, String id, int phase, String current, int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
        this.threePhaseCommand = threePhaseStendCommand;
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currentPerсent = currentPercent;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;

        if (iABC.equals("H")) {
            name = (cosP + "; " + currentPerсent + " " + current.trim());
        } else {
            name = (iABC + "; " + cosP + "; " + currentPerсent + " " + current);
        }

        currPer = Double.parseDouble(currentPerсent) * 100;
        voltPer = 100.0;
    }

    /**
     * Конструктор для формирования параметров точки испытания, а так же дополнительного
     * влияния по частоте или напряжению
     *
     * @param threePhaseStendCommand - см. выше
     * @param strPhase
     * @param param - параметр влияния, либо по частоте либо по папряжению
     * @param id - см. выше
     * @param phase - см. выше
     * @param current - см. выше
     * @param paramPer - процент влияния от номинальной частоты или номинального напряжения
     * @param revers - см. выше
     * @param currentPercent - см. выше
     * @param iABC - см. выше
     * @param cosP - см. выше
     * @param channelFlag - см. выше
     */
    //Конструктор для создания объекта с вклатки "Влияние"
    public ErrorCommand(boolean threePhaseStendCommand, String strPhase, String param, String id, int phase, String current,
                        double paramPer, int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
        this.threePhaseCommand = threePhaseStendCommand;
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currentPerсent = currentPercent;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
        this.param = param;

        if (param.equals("U")) {
            this.voltPer = paramPer;
            this.procentParan = paramPer;
        } else if (param.equals("F")) {
            this.voltPer = 100;
            this.procentParan = paramPer;
        }

        //Примеры отображения
        //47.0%Un: 0.5L; 0.01 Ib
        //A: 47.0%Un: 0.5L; 0.01 Ib
        name = (strPhase + paramPer + "%" + param + "n; " + cosP + "; " + currentPerсent + " " + current.trim());

        currPer = Double.parseDouble(currentPerсent) * 100;
    }

    //===================================================================================================
    //Команда выполнения для последовательного теста
    @Override
    public void execute() throws StendConnectionException, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Выбор константы счётчика в зависимости от энергии
        if (channelFlag < 2) {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
        } else {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
        }

        flagInStop = initBoolList();

        if (current.equals("Ib")) {
            ratedCurr = Ib;
        } else {
            ratedCurr = Imax;
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Высчитываю частоту, которую необходимо выставить
        if (param.equals("F")) {
            ratedFreq = ratedFreq * new BigDecimal(procentParan / 100).setScale(5, RoundingMode.HALF_UP).doubleValue();
        }

        //Выставляю параметры выбранные пользователем
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

            if (iABC.equals("B")) {
                stendDLLCommands.selectCircuit(1);
                iABC = "H";

                stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            } else {
                stendDLLCommands.selectCircuit(0);
                iABC = "H";

                stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep((long) pauseForStabilization * 1000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        resetError();

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Сказать константу счётчика стенду для кажого места
        stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Для быстрой становки флага прошёл счётчик тест или нет
        Meter.CommandResult resultMeter;

        int resultNo;
        String strError;
        String[] strMass;
        String error;

        //Для сравнения
        double doubleErr;

        while (flagInStop.containsValue(false)) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            //Пробегаюсь по счётчикам и узнаю погрешность
            for (Meter meter : meterForTestList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                //Формат - номер измерения погрешности и погрешность. Пример: 3,-0.231
                strError = stendDLLCommands.meterErrorRead(meter.getId());
                strMass = strError.split(",");

                if (strMass.length == 2) {

                    resultNo = Integer.parseInt(strMass[0]);
                    error = strMass[1];

                    //Если результат измерения поступил
                    if (resultNo != 0) {

                        resultMeter = meter.returnResultCommand(index, channelFlag);
                        doubleErr = Double.parseDouble(error);

                        if (doubleErr > emax || doubleErr < emin) {
                            ((Meter.ErrorResult) resultMeter).setResultErrorCommand("F" + error, resultNo, error, false);
                        } else {
                            ((Meter.ErrorResult) resultMeter).setResultErrorCommand("P" + error, resultNo, error, true);
                        }

                        //Если получен новый результат относительной погрешности счётчика
                        if (meter.getErrorResultChange() != resultNo) {
                            meter.setAmountMeasur(meter.getAmountMeasur() + 1);
                            meter.setErrorResultChange(resultNo);
                        }
                    }

                    //Если получено дастаточно значений результата погрешности у счётчика, значит можно переходить к следующей точке
                    if (meter.getAmountMeasur() >= countResult) {
                        flagInStop.put(meter.getId(), true);
                    }
                }
            }

            Thread.sleep(300);
        }

        stendDLLCommands.errorClear();
    }

    /**
     * Логика такая же как и у команды execute только в циле см. выше
     * @throws StendConnectionException
     * @throws InterruptedException
     */
    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws StendConnectionException, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Выбор константы в зависимости от энергии
        if (channelFlag < 2) {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
        } else {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
        }

        if (current.equals("Ib")) {
            ratedCurr = Ib;
        } else {
            ratedCurr = Imax;
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (param.equals("F")) {
            ratedFreq = new BigDecimal(ratedFreq * (procentParan / 100)).setScale(5, RoundingMode.HALF_UP).doubleValue();
        }

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

            if (iABC.equals("B")) {
                stendDLLCommands.selectCircuit(1);
                iABC = "H";

                stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            } else {
                stendDLLCommands.selectCircuit(0);
                iABC = "H";

                stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP);
            }
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep((long) pauseForStabilization * 1000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        //Сказать константу счётчика стенду для кажого места
        stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Для быстрой становки флага прошёл счётчик тест или нет
        Meter.CommandResult resultMeter;

        int resultNo;
        String strError;
        String[] strMass;
        String error;
        double doubleErr;

        //Выполнять до тех пор пока пользователь не сменил точку испытания
        while (Thread.currentThread().isAlive()) {

            for (Meter meter : meterForTestList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                strError = stendDLLCommands.meterErrorRead(meter.getId());
                strMass = strError.split(",");

                if (strMass.length != 2) {

                    resultNo = Integer.parseInt(strMass[0]);
                    error = strMass[1];

                    if (resultNo != 0) {
                        resultMeter = meter.returnResultCommand(index, channelFlag);
                        doubleErr = Double.parseDouble(error);

                        if (doubleErr > emax || doubleErr < emin) {
                            ((Meter.ErrorResult) resultMeter).setResultErrorCommand("F" + error, resultNo, error, false);
                        } else {
                            ((Meter.ErrorResult) resultMeter).setResultErrorCommand("P" + error, resultNo, error, true);
                        }
                    }
                }
            }

            Thread.sleep(300);
        }

        stendDLLCommands.errorClear();
    }

    /**
     * Опрашивает счётчики до нужно значения проходов
     * т.е. количество значений относительной погрешности, которые необходимо получить
     * от счётчика, после чего флаг переставляется на значение true
     * @return
     */
    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterForTestList.size());

        for (Meter meter : meterForTestList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    /**
     * Делает ресет измеренных погрешностей у счётчиков
     */
    private void resetError() {
        for (Meter meter : meterForTestList) {
            meter.setAmountMeasur(0);
            meter.setErrorResultChange(0);
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
        this.emin = Double.parseDouble(emin);
    }

    public void setEmax(String emax) {
        this.emax = Double.parseDouble(emax);
    }

    public String getName() {
        return name;
    }

    public String getEmin() {
        return String.valueOf(emin);
    }

    public String getEmax() {
        return String.valueOf(emax);
    }

    public String getPulse() {
        return String.valueOf(pulse);
    }

    public void setImax(double imax) {
        Imax = imax;
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
        return String.valueOf(countResult);
    }

    @Override
    public String toString() {
        if (iABC.equals("H")) {
            return (cosP + "; " + currentPerсent + " "  + current);
        } else return (iABC + ": " + cosP + "; " + currentPerсent + " "  + current);
    }

    public void setPhase(int phase) {
        this.phase = phase;
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

    public void setRatedCurr(double ratedCurr) {
        this.ratedCurr = ratedCurr;
    }

    public void setPhaseSrequence(int phaseSrequence) {
        this.phaseSrequence = phaseSrequence;
    }

    public void setRevers(int revers) {
        this.revers = revers;
    }

    public void setVoltPer(double voltPer) {
        this.voltPer = voltPer;
    }

    public void setCurrPer(double currPer) {
        this.currPer = currPer;
    }

    public void setiABC(String iABC) {
        this.iABC = iABC;
    }

    public void setCosP(String cosP) {
        this.cosP = cosP;
    }

    public double getProcentParan() {
        return procentParan;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }

    public String getiABC() {
        return iABC;
    }

    public double getRatedVolt() {
        return ratedVolt;
    }

    public double getRatedCurr() {
        return ratedCurr;
    }

    public double getVoltPerA() {
        return voltPerA;
    }

    public double getVoltPerB() {
        return voltPerB;
    }

    public double getVoltPerC() {
        return voltPerC;
    }

    public double getCurrPer() {
        return currPer;
    }

    public double getVoltPer() {
        return voltPer;
    }

    public boolean isThreePhaseCommand() {
        return threePhaseCommand;
    }

    public String getPauseForStabilization() {
        return String.valueOf(pauseForStabilization);
    }

    public void setPauseForStabilization(double pauseForStabilization) {
        this.pauseForStabilization = pauseForStabilization;
    }
}
