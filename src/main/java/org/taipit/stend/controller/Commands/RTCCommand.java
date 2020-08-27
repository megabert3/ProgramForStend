package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.model.stend.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

public class RTCCommand implements Commands, Serializable, Cloneable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private String id;

    //Команда из методики для трехфазной установки?
    private boolean threePhaseCommand;

    private boolean nextCommand;

    private List<Meter> meterList;

    private StendDLLCommands stendDLLCommands;

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

    private double voltPer = 100;

    //Процен от тока
    private double currPer;

    //Коэфициент мощности
    private String cosP = "1.0";

    private String iABC = "H";

    private int channelFlag;

    //Для сохранения результата теста в нужное направление
    private int channelFlagForSave;

    private boolean active = true;

    //Дианазон ошибки
    private double errorForFalseTest;

    //Количество повторов теста
    private String countResult;

    //Количество повторов теста
    private int countResultTest;

    //Частота
    private double freg;

    //Имя команды
    private String name;

    //Количество импульсов для считывания
    private int pulseForRTC;

    //Тип измерения
    private int errorType;

    public RTCCommand(boolean threePhaseCommand, String id , String name, int pulseForRTC, double freg, int countResultTest,
                      int errorType, double errorForFalseTest, int channelFlag) {
        this.threePhaseCommand = threePhaseCommand;
        this.id = id;
        this.name = name;
        this.pulseForRTC = pulseForRTC;
        this.freg = freg;
        this.countResultTest = countResultTest;
        this.errorType = errorType;
        this.errorForFalseTest = errorForFalseTest;
        this.channelFlagForSave = channelFlag;
    }

    @Override
    public void execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        channelFlag = 4;

        stendDLLCommands.setReviseMode(1);

        TestErrorTableFrameController.transferParam(this);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = ConsoleHelper.properties.getProperty("phaseOnOnePhaseMode");

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

        TestErrorTableFrameController.refreshRefMeterParameters();

        //Время стабилизации
        Thread.sleep(5000);

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        stendDLLCommands.setRefClock(1);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Преобразую время в миллисекунды
        BigDecimal timeTestInMls = new BigDecimal((pulseForRTC * freg) * 1000).setScale(0, BigDecimal.ROUND_UP);

        int count = 0;
        Meter.RTCResult rtcCommand;
        String resultStr;
        double result;

        while (count < countResultTest) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Meter meter : meterList) {
                stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC);
            }

            Thread.sleep(timeTestInMls.longValue() + 500);

            for (Meter meter : meterList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                rtcCommand = (Meter.RTCResult) meter.returnResultCommand(index, channelFlagForSave);

                resultStr = stendDLLCommands.clockErrorRead(freg, errorType, meter.getId());

                if (!resultStr.isEmpty()) {
                    if (errorType == 0) {

                        result = Double.parseDouble(resultStr) - freg;

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT,"%.7f", result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT, "%.7f", result), count, true);
                        }

                    } else if (errorType == 1){

                        result = Double.parseDouble(resultStr);

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT, "%.2f", result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT, "%.2f", result), count, true);
                        }

                    }
                }
            }
            count++;
        }

        stendDLLCommands.errorClear();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        channelFlag = 4;

        stendDLLCommands.setReviseMode(1);

        TestErrorTableFrameController.transferParam(this);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {

                iABC = ConsoleHelper.properties.getProperty("phaseOnOnePhaseMode");

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

        TestErrorTableFrameController.refreshRefMeterParameters();

        //Время стабилизации
        Thread.sleep(5000);

        TestErrorTableFrameController.refreshRefMeterParameters();

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        stendDLLCommands.setRefClock(1);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Преобразую время в миллисекунды
        BigDecimal timeTestInMls = new BigDecimal((pulseForRTC * freg) * 1000).setScale(0, BigDecimal.ROUND_UP);

        int count = 0;
        Meter.RTCResult rtcCommand;
        String resultStr;
        double result;

        while (Thread.currentThread().isAlive()) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Meter meter : meterList) {
                stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC);
            }

            System.out.println(timeTestInMls.longValue() + 500);

            Thread.sleep(timeTestInMls.longValue() + 500);

            for (Meter meter : meterList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                rtcCommand = (Meter.RTCResult) meter.returnResultCommand(index, channelFlagForSave);

                resultStr = stendDLLCommands.clockErrorRead(freg, errorType, meter.getId());

                if (!resultStr.isEmpty()) {
                    if (errorType == 0) {

                        result = Double.parseDouble(resultStr) - freg;

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT,"%.7f", result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT, "%.7f", result), count, true);
                        }

                    } else if (errorType == 1){

                        result = Double.parseDouble(resultStr);

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT, "%.2f", result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.format(Locale.ROOT, "%.2f", result), count, true);
                        }

                    }
                }
            }
        }

        stendDLLCommands.errorClear();

        stendDLLCommands.powerOf();
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public double getErrorForFalseTest() {
        return errorForFalseTest;
    }

    public int getPulseForRTC() {
        return pulseForRTC;
    }

    public int getErrorType() {
        return errorType;
    }

    public String getCountResult() {
        return null;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setMeterList(List<Meter> meterList) {
        this.meterList = meterList;
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

    public void setErrorForFalseTest(double errorForFalseTest) {
        this.errorForFalseTest = errorForFalseTest;
    }

    public int getCountResultTest() {
        return countResultTest;
    }

    public void setCountResultTest(int countResultTest) {
        this.countResultTest = countResultTest;
    }

    public String getId() {
        return id;
    }

    public double getFreg() {
        return freg;
    }

    public boolean isThreePhaseCommand() {
        return threePhaseCommand;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
