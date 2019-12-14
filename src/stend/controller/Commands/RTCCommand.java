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
    private double ratedCurr;
    private double ratedFreq;
    private int phaseSrequence;
    private int revers;
    private double voltPer;
    private double currPer;
    private String iABC = "H";
    private String cosP = "1.0";

    private int channelFlag;

    //Количество повторов теста
    private int countResult = 3;
    //Частота
    private double freg = 1.000000;

    //Количество импульсов для считывания
    private int pulse = 10;

    //Тип измерения
    private int errorType = 0;


    //Массив погрешностей одного счётчика
    private HashMap<Integer, String> errorRTCList = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());

    public HashMap<Integer, String> getErrorRTCList() {
        return errorRTCList;
    }

    public RTCCommand(StendDLLCommands stendDLLCommands, int phase, double ratedVolt, double ratedCurr, double ratedFreq, int phaseSrequence,
                        int revers, double voltPer, double currPer, int channelFlag, int pulse) {
        this.stendDLLCommands = stendDLLCommands;
        this.phase = phase;
        this.ratedVolt = ratedVolt;
        this.ratedCurr = ratedCurr;
        this.ratedFreq = ratedFreq;
        this.phaseSrequence = phaseSrequence;
        this.revers = revers;
        this.voltPer = voltPer;
        this.currPer = currPer;
        this.channelFlag = channelFlag;
        this.pulse = pulse;
    }

    @Override
    public void execute() {
        int count = 0;
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP);

        stendDLLCommands.setEnergyPulse(channelFlag);

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());

        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            stendDLLCommands.clockErrorStart(meter.getKey(), freg, pulse);
        }

        try {
            while (count < countResult) {
                Thread.sleep((pulse * 1000) + 2000);
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
}
