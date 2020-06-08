package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.List;

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

    private double voltPer;

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
        this.channelFlag = channelFlag;
    }

    @Override
    public void execute() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        int ErrorCommand = 1;

        channelFlag = 4;

        stendDLLCommands.setReviseMode(1);

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
                voltPerC = voltPer;
                if (!stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPerA, voltPerB, voltPerC, currPer, iABC, cosP)) throw new ConnectForStendExeption();
            } else {
                if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                        voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();
            }
        } else {
            if (!stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                    voltPer, currPer, iABC, cosP)) throw new ConnectForStendExeption();
        }

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        TestErrorTableFrameController.refreshRefMeterParameters();

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

        for (Meter meter : meterList) {
            stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC);
        }

        int count = 0;
        Meter.RTCResult rtcCommand;
        double result;

        while (count < countResultTest) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 100));

            for (Meter meter : meterList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                try {
                    rtcCommand = (Meter.RTCResult) meter.returnResultCommand(index, channelFlag);

                    if (errorType == 0) {

                        result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId())) - freg;

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, true);
                        }

                    } else if (errorType == 1){

                        result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId()));

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, true);
                        }

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Пустая строчка");
                }
            }
            count++;
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        if (stendDLLCommands instanceof ThreePhaseStend) {
            if (!threePhaseCommand) {
                iABC = "C";
            }
        }

        stendDLLCommands.setReviseMode(1);

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                100.0, 0.0, iABC, "1.0")) throw new ConnectForStendExeption();

        //Разблокирую интерфейc кнопок
        TestErrorTableFrameController.blockBtns.setValue(false);

        Thread.sleep(5000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        stendDLLCommands.setRefClock(1);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        for (Meter meter : meterList) {
            stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC);
        }

        int count = 0;
        Meter.RTCResult rtcCommand;
        double result;

        while (!Thread.currentThread().isInterrupted()) {

            Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

            for (Meter meter : meterList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                try {
                    rtcCommand = (Meter.RTCResult) meter.returnResultCommand(index, channelFlag);

                    if (errorType == 0) {

                        result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId())) - freg;

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, true);
                        }

                    } else if (errorType == 1){

                        result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId()));

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, false);
                        } else {
                            rtcCommand.setResultRTCCommand(String.valueOf(result), count, true);
                        }

                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Пустая строчка");
                }
            }
            count++;
        }

        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

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

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
