package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;

import java.util.HashMap;
import java.util.Map;

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
    private HashMap<Integer, LocalMeter> metersList = initMeterList();

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
            for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
                if (!(metersList.get(meter.getKey()).run())) {
                    startCommandResult.put(meter.getKey(), true);
                }
            }
        }
        addResultOnMeter();
    }

    private HashMap<Integer, Boolean> initCreepCommandResult() {
        HashMap<Integer, Boolean> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), false);
        }
        return init;
    }

    private HashMap<Integer, LocalMeter> initMeterList() {
        HashMap<Integer, LocalMeter> init = new HashMap<>();
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), new LocalMeter(meter.getKey()));
        }
        return  init;
    }

    //Довавляет результат в счётчик в зависимости от направления тока
    private void addResultOnMeter() {
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            switch (channelFlag) {
                case 0: {
                    meter.getValue().setStartTestActiveEnergyDirect(startCommandResult.get(meter.getKey()));
                }break;
                case 1: {
                    meter.getValue().setStartTestActiveEnergyReverse(startCommandResult.get(meter.getKey()));
                }break;
                case 2: {
                    meter.getValue().setStartTestReactiveEnergyDirect(startCommandResult.get(meter.getKey()));
                }break;
                case 3: {
                    meter.getValue().setStartTestReactiveEnergyReverse(startCommandResult.get(meter.getKey()));
                }
            }
        }
    }


    private class LocalMeter {
        int number;
        int counter = 0;
        boolean searchMark;

        LocalMeter(int number) {
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
