package stend.controller;

import stend.helper.ConsoleHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


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

    private String[] strMass;

    //Нужен сеттер т.к. инициализация будет с другого класса
    private double[] constant = StendDLLCommands.constantsForMetersOnPlaces;

    //Необходимо для быстрого обхода в цикле
    private static int[] amountActivePlacesForTest = getAmountActivePlacesForTest();

    //Массив погрешностей одного счётчика
    private HashMap<Integer, double[]> errorList = initErrorList();

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop = initBoolList();


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
    public boolean execute() {
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP);

        //Тут необходимо придумать флаг
        for (int i = 1; i <= stendDLLCommands.getAmountPlaces(); i++) {
            stendDLLCommands.setPulseChannel(i, channelFlag);
        }

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());

        for (int i = 1; i <= amountActivePlacesForTest.length; i++) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(i, constant[i], pulse);
        }

        //Тут надо что-то придумать
        while (flagInStop.containsValue(false)) {
            for (int i = 1; i <= amountActivePlacesForTest.length; i++) {
                String strError = stendDLLCommands.meterErrorRead(i);
                strMass = strError.split(",");
                int resultNo = Integer.parseInt(strMass[0]);
                ConsoleHelper.getMessage(String.valueOf(resultNo));
                double error = Double.parseDouble(strMass[1]);
                errorList.get(i)[resultNo] = error;
                if (resultNo >= stendDLLCommands.getCountResult()) {
                    flagInStop.put(i, true);
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

        return true;
    }

    //Тут тоже необходимо подумать
    private static int[] getAmountActivePlacesForTest() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i < StendDLLCommands.amountActivePlaces.length; i++) {
            if (StendDLLCommands.amountActivePlaces[i]) {
                list.add(i);
            }
        }
        int[] init = new int[list.size()];

        for (int i = 0; i  < list.size(); i++) {
            init[i] = list.get(i);
        }

        return init;
    }

    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(amountActivePlacesForTest.length);
        for (int i = 1; i <= amountActivePlacesForTest.length; i++) {
            init.put(i, false);
        }
        return init;
    }

    private HashMap<Integer, double[]> initErrorList() {
        HashMap<Integer, double[]> init = new HashMap<>(amountActivePlacesForTest.length);
        for (int i = 1; i <= amountActivePlacesForTest.length; i++) {
            init.put(i, new double[10]);
        }
        return init;
    }

    public void setConstant(int number, double constant) {
        this.constant[number] = constant;
    }
}
