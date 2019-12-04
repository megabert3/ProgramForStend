package stend.controller;

import stend.helper.ConsoleHelper;

import java.util.HashMap;


public class ErrorCommand implements Commands{

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

    //Массив погрешностей одного счётчика
    private HashMap<Integer, double[]> errorList = initErrorList();

    public HashMap<Integer, double[]> getErrorList() {
        return errorList;
    }

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

        for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(StendDLLCommands.amountActivePlacesForTest[i], stendDLLCommands.getConstant(), pulse);
        }

        while (StendDLLCommands.flagInStop.containsValue(false)) {
            int resultNo;
            String strError;
            String[] strMass;
            double error;

            for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
                strError = stendDLLCommands.meterErrorRead(StendDLLCommands.amountActivePlacesForTest[i]);
                strMass = strError.split(",");
                resultNo = Integer.parseInt(strMass[0]);
                error = Double.parseDouble(strMass[1]);
                errorList.get(StendDLLCommands.amountActivePlacesForTest[i])[resultNo] = error;

                if (resultNo >= countResult) {
                    StendDLLCommands.flagInStop.put(StendDLLCommands.amountActivePlacesForTest[i], true);
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


    private HashMap<Integer, double[]> initErrorList() {
        HashMap<Integer, double[]> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.length);
        for (int i = 0; i < StendDLLCommands.amountActivePlacesForTest.length; i++) {
            init.put(StendDLLCommands.amountActivePlacesForTest[i], new double[10]);
        }
        return init;
    }
}
