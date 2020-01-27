package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;

import java.util.HashMap;
import java.util.Map;

public class RTCCommand implements Commands {

    private StendDLLCommands stendDLLCommands;

    private int phase;

    private double ratedVolt;

    private int channelFlag = 4;

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
    private HashMap<Integer, String> errorRTCList = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());

    public HashMap<Integer, String> getErrorRTCList() {
        return errorRTCList;
    }

    public RTCCommand(StendDLLCommands stendDLLCommands, int pulseForRTC, double freg, int countResult, int errorType, double errorForFalseTest) {
        this.stendDLLCommands = stendDLLCommands;
        this.pulseForRTC = pulseForRTC;
        this.freg = freg;
        this.countResult = countResult;
        this.errorType = errorType;
        this.errorForFalseTest = errorForFalseTest;
    }

    @Override
    public void execute() {
        int count = 0;
        stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                0.0, 0.0, "H", "1.0");

        stendDLLCommands.setEnergyPulse(channelFlag);

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());

        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            stendDLLCommands.clockErrorStart(meter.getKey(), freg, pulseForRTC);
        }

        try {
            while (count < countResult) {
                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));
                for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
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
}
