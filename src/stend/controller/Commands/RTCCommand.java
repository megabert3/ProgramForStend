package stend.controller.Commands;

import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;

import java.util.HashMap;

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
    private String iABC;
    private String cosP;

    private int channelFlag;

    private int pulse;

    //Количество повторов теста
    private int countResult = 2;
    //Частота
    private double freg;
    //Количество импульсов для считывания
    private int duration;
    //Тип измерения
    private int errorType;

    //Массив погрешностей одного счётчика
    private HashMap<Integer, double[]> errorRTCList = initErrorRTCList();

    public HashMap<Integer, double[]> getErrorRTCList() {
        return errorRTCList;
    }

    public RTCCommand(StendDLLCommands stendDLLCommands, int phase, double ratedVolt, double ratedCurr, double ratedFreq, int phaseSrequence,
                        int revers, double voltPer, double currPer, String iABC, String cosP, int channelFlag, int pulse) {
        this.stendDLLCommands = stendDLLCommands;
        this.phase = phase;
        this.ratedVolt = ratedVolt;
        this.ratedCurr = ratedCurr;
        this.ratedFreq = ratedFreq;
        this.phaseSrequence = phaseSrequence;
        this.revers = revers;
        this.voltPer = voltPer;
        this.currPer = currPer;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
        this.pulse = pulse;
    }

    @Override
    public void execute() {
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP);

        stendDLLCommands.setEnergyPulse(channelFlag);

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());
        for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
            stendDLLCommands.clockErrorStart(StendDLLCommands.amountActivePlacesForTest[i], freg, duration);
        }
    }

    private  HashMap<Integer, double[]> initErrorRTCList() {
        HashMap<Integer, double[]> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.length);
        for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
            init.put(StendDLLCommands.amountActivePlacesForTest[i], new double[10]);
        }
        return init;
    }
}
