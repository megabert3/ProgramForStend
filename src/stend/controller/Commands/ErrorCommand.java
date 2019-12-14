package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;

import java.util.HashMap;
import java.util.Map;


public class ErrorCommand implements Commands {

    private StendDLLCommands stendDLLCommands;

    private int phase;
    private double ratedVolt;
    private double ratedCurr;
    private double ratedFreq;
    private int phaseSrequence;
    private int revers;

    private double voltPerA;
    private double voltPerB;
    private double voltPerC;

    private double voltPer;
    private double currPer;
    private String iABC;
    private String cosP;

    private int channelFlag;
    private int pulse;

    //Количество повторов теста
    private int countResult = 2;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop = initBoolList();

    public ErrorCommand(StendDLLCommands stendDLLCommands, int phase, double ratedVolt, double ratedCurr, double ratedFreq, int phaseSrequence,
                        int revers, double voltPer, double currPer, String iABC, String cosP, int channelFlag, int pulse) {
        this.stendDLLCommands = stendDLLCommands;
        this.phase = phase;
        this.ratedVolt = ratedVolt;
        this.ratedCurr = ratedCurr;
        this.ratedFreq = ratedFreq;
        this.phaseSrequence = phaseSrequence;
        this.revers = revers;
        this.voltPerA = voltPer;
        this.voltPerB = voltPer;
        this.voltPerC = voltPer;
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

        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(meter.getKey(), stendDLLCommands.getConstant(), pulse);
        }

        while (flagInStop.containsValue(false)) {
            int resultNo;
            String strError;
            String[] strMass;
            double error;

            for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
                strError = stendDLLCommands.meterErrorRead(meter.getKey());
                strMass = strError.split(",");
                resultNo = Integer.parseInt(strMass[0]);
                error = Double.parseDouble(strMass[1]);
                StendDLLCommands.amountActivePlacesForTest.get(meter.getKey()).getErrors()[resultNo] = error;

                if (resultNo >= countResult) {
                    flagInStop.put(meter.getKey(), true);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stendDLLCommands.powerOf();
        stendDLLCommands.errorClear();
    }

    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), false);
        }
        return init;
    }
}
