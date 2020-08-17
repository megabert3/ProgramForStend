package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

public class ImbalansUCommand implements Commands, Serializable, Cloneable {

    private StendDLLCommands stendDLLCommands;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Команда для прерывания метода
    private boolean interrupt;

    //Лист с счётчиками для испытания
    private List<Meter> meterForTestList;

    //Максимальный порог ошибки
    private double emin = -1.0;

    //Минимальный порог ошибки
    private double emax = 1.0;

    //Кол-во импульсов для расчёта ошибки
    private int pulse = 20;

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

    //Напряжение по фазе A
    private double voltPerA;

    //Напряжение по фазе B
    private double voltPerB;

    //Напряжение по фазе C
    private double voltPerC;

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
    private int countResult = 2;

    //Константа счётчика для теста
    private int constantMeter;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop;

    public ImbalansUCommand(String id, int phase, String current, int revers, String cosP, int channelFlag,
                            double voltPerPhaseA, double voltPerPhaseB, double voltPerPhaseC) {
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
        this.voltPerA = voltPerPhaseA;
        this.voltPerB = voltPerPhaseB;
        this.voltPerC = voltPerPhaseC;

        //"Imb;B;R;N"
        String[] arrId = id.split(";");

        if (arrId[2].equals("A")) {

            if (arrId[3].equals("P")) {
                name = (arrId[1] + "; ImbU AP+");
            } else {
                name = (arrId[1] + "; ImbU AP-");
            }

        } else {

            if (arrId[3].equals("P")) {
                name = (arrId[1] + "; ImbU RP+");
            } else {
                name = (arrId[1] + "; ImbU RP-");
            }
        }

        currPer = 100.0;
        iABC = "H";
        phaseSrequence = 0;
        voltPer = 100.0;
    }

    @Override
    public void execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        int refMeterCount = 1;

        //Выбор константы в зависимости от энергии
        if (channelFlag == 0 || channelFlag == 1) {
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

        stendDLLCommands.setReviseMode(1);

        TestErrorTableFrameController.transferParam(this);

        if (!stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP)) throw new ConnectForStendExeption();


        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        TestErrorTableFrameController.refreshRefMeterParameters();

        Thread.sleep(3000); //stendDLLCommands.getPauseForStabization()

        TestErrorTableFrameController.refreshRefMeterParameters();

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

            if (refMeterCount % 11 == 0) {
                TestErrorTableFrameController.refreshRefMeterParameters();
            }

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Meter meter : meterForTestList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                strError = stendDLLCommands.meterErrorRead(meter.getId());
                strMass = strError.split(",");

                if (strMass.length != 2) {
                    continue;
                }

                resultNo = Integer.parseInt(strMass[0]);
                error = strMass[1];

                if (resultNo != 0) {
                    resultMeter = meter.returnResultCommand(index, channelFlag);
                    doubleErr = Double.parseDouble(error);

                    if (doubleErr > emax || doubleErr < emin) {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("F" + error, resultNo, error, false);
                    } else {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("P" + error, resultNo, error, true);
                    }

                    if (meter.getErrorResultChange() != resultNo) {
                        meter.setAmountMeasur(meter.getAmountMeasur() + 1);
                        meter.setErrorResultChange(resultNo);
                    }
                }

                if (meter.getAmountMeasur() >= countResult) {
                    flagInStop.put(meter.getId(), true);
                }
            }

            refMeterCount++;
            Thread.sleep(300);
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        int refMeterCount = 1;

        //Выбор константы в зависимости от энергии
        if (channelFlag == 0 || channelFlag == 1) {
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

        stendDLLCommands.setReviseMode(1);

        TestErrorTableFrameController.transferParam(this);

        if (!stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP)) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        TestErrorTableFrameController.refreshRefMeterParameters();

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

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
        double doubleErr;

        while (Thread.currentThread().isAlive()) {

            if (refMeterCount % 11 == 0) {
                TestErrorTableFrameController.refreshRefMeterParameters();
            }

            for (Meter meter : meterForTestList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                strError = stendDLLCommands.meterErrorRead(meter.getId());
                strMass = strError.split(",");

                if (strMass.length != 2) {
                    continue;
                }

                resultNo = Integer.parseInt(strMass[0]);
                error = strMass[1];

                if (resultNo != 0) {
                    resultMeter = meter.returnResultCommand(index, channelFlag);
                    doubleErr = Double.parseDouble(error);

                    if (doubleErr > emax || doubleErr < emin) {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("F" + error, resultNo, error, false);
                    } else {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("P" + error, resultNo, error, true);
                    }
                }
            }

            refMeterCount++;
            Thread.sleep(300);
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    //Опрашивает счётчики до нужно значения проходов
    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterForTestList.size());

        for (Meter meter : meterForTestList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

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

    public void setPhase(int phase) {
        this.phase = phase;
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

    public boolean isThreePhaseCommand() {
        return true;
    }

    @Override
    public String toString() {
        if (iABC.equals("H")) {
            return (cosP + "; " + currentPerсent + " "  + current);
        } else return (iABC + ": " + cosP + "; " + currentPerсent + " "  + current);
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
