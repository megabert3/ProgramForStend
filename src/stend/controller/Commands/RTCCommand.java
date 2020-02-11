package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;

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

    //Массив погрешностей одного счётчика
    private HashMap<Integer, String> errorRTCList = new HashMap<>();

    public HashMap<Integer, String> getErrorRTCList() {
        return errorRTCList;
    }

    public RTCCommand(int pulseForRTC, double freg, int countResult, int errorType, double errorForFalseTest) {
        this.pulseForRTC = pulseForRTC;
        this.freg = freg;
        this.countResult = countResult;
        this.errorType = errorType;
        this.errorForFalseTest = errorForFalseTest;
    }

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void execute() {
        int count = 0;
        stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                0.0, 0.0, "H", "1.0");

        stendDLLCommands.setEnergyPulse(meterList, channelFlag);

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());

        for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
            stendDLLCommands.clockErrorStart(meter.getKey(), freg, pulseForRTC);
        }

        try {
            while (count < countResult) {
                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));
                for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
                    meter.getValue().setRTCTestResult(stendDLLCommands.clockErrorRead(freg, errorType, meter.getKey()));
                }
                count++;
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
        stendDLLCommands.errorClear();
        stendDLLCommands.powerOf();
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
