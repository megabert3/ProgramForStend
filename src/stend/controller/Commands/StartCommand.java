package stend.controller.Commands;

import stend.controller.Commands.Commands;
import stend.controller.StendDLLCommands;

import java.util.HashMap;

public class StartCommand implements Commands {

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
    private int pulseValue;

    private long timeForTest = 60000;
    private long timeEnd;
    private long currTime;

    private HashMap<Integer, Boolean> startCommandResult = initCreepCommandResult();
    private HashMap<Integer, Meter> metersList = initMeterList();

    public HashMap<Integer, Boolean> getCreepCommandResult() {
        return startCommandResult;
    }

    public StartCommand(StendDLLCommands stendDLLCommands, int phase, double ratedVolt, double ratedCurr, double ratedFreq,
                        int phaseSrequence, int revers, double voltPer, double currPer, int channelFlag, int pulseValue) {
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
        this.pulseValue = pulseValue;
    }

    @Override
    public void execute() {
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP);

        stendDLLCommands.setEnergyPulse(channelFlag);

        currTime = System.currentTimeMillis();
        timeEnd = currTime + timeForTest;
        while (startCommandResult.containsValue(false) && System.currentTimeMillis() <= timeEnd) {
            for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
                if (!(metersList.get(StendDLLCommands.amountActivePlacesForTest[i]).run())) {
                    startCommandResult.put(StendDLLCommands.amountActivePlacesForTest[i], true);
                }
            }
        }
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.length);
        for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
            init.put(StendDLLCommands.amountActivePlacesForTest[i], false);
        }
        return init;
    }

    private HashMap<Integer, Meter> initMeterList() {
        HashMap<Integer, Meter> init = new HashMap<>();
        for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
            init.put(StendDLLCommands.amountActivePlacesForTest[i], new Meter(StendDLLCommands.amountActivePlacesForTest[i]));
        }
        return  init;
    }

    private class Meter {
        int number;
        int counter = 0;
        boolean searchMark;

        Meter(int number) {
            this.number = number;
        }

        public boolean run() {
            if (counter < pulseValue) {
                if (!searchMark) {
                    stendDLLCommands.searchMark(number);
                    searchMark = true;
                } else {
                    if (stendDLLCommands.searchMarkResult(number)) {
                        counter++;
                        searchMark = false;
                    }
                }
            } else return false;
            return true;
        }
    }
}