package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;
import stend.helper.exeptions.ConnectForStendExeption;
import stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RTCCommand implements Commands, Serializable {

    private List<Meter> meterList;

    //Команда для прерывания метода
    private boolean interrupt;

    private StendDLLCommands stendDLLCommands;

    private int phase;

    private double ratedVolt;

    private int channelFlag = 4;

    //Для сохранения результата теста в нужное направление
    private int channelFlagForSave;

    private boolean active = true;

    //Дианазон ошибки
    private double errorForFalseTest;

    //Количество повторов теста
    private int countResult;

    //Частота
    private double freg;

    //Имя команды
    private String name;

    //Количество импульсов для считывания
    private int pulseForRTC;

    //Тип измерения
    private int errorType;

    public RTCCommand(int pulseForRTC, double freg, int countResult, int errorType, double errorForFalseTest, int channelFlagForSave) {
        this.pulseForRTC = pulseForRTC;
        this.freg = freg;
        this.countResult = countResult;
        this.errorType = errorType;
        this.errorForFalseTest = errorForFalseTest;
        this.channelFlagForSave = channelFlagForSave;
    }

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void execute() throws InterruptedTestException, ConnectForStendExeption, InterruptedException {
        if (interrupt) throw new InterruptedTestException();
        int count = 0;

        if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                0.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

        if (interrupt) throw new InterruptedTestException();
        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        Thread.sleep(stendDLLCommands.getPauseForStabization());

        if (interrupt) throw new InterruptedTestException();
        for (Meter meter : meterList) {
            if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC)) throw new ConnectForStendExeption();
        }

        while (count < countResult) {

            if (interrupt) throw new InterruptedTestException();

            Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

            for (Meter meter : meterList) {
                try {
                    /**
                     * Проверить на тру или фолс в корневом методе
                     */
                    double result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId())) - 1.000000;

                    if (result > errorForFalseTest || result < -errorForFalseTest) {
                        addRTCTestResult(meter, result, false, channelFlagForSave);
                    } else {
                        addRTCTestResult(meter, result, true, channelFlagForSave);
                    }
                }catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("Пустая строчка");
                }
            }

            count++;
        }

        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
    }

    private void addRTCTestResult(Meter meter, double RTCError, boolean passOrNot, int channelFlagForSave) {
        switch (channelFlagForSave) {
            case 0: {
                meter.setRTCTestErrorAPPls(String.valueOf(RTCError));
                meter.setRTCTestResultAPPls(passOrNot);
            }break;

            case 1: {
                meter.setRTCTestErrorAPMns(String.valueOf(RTCError));
                meter.setRTCTestResultAPMns(passOrNot);
            }break;

            case 2: {
                meter.setRTCTestErrorRPPls(String.valueOf(RTCError));
                meter.setRTCTestResultRPPls(passOrNot);
            }break;

            case 3: {
                meter.setRTCTestErrorRPMns(String.valueOf(RTCError));
                meter.setRTCTestResultRPMns(passOrNot);
            }break;
        }
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

    public int getCountResult() {
        return countResult;
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
}
