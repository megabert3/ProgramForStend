package stend.controller;

import stend.helper.ConsoleHelper;

import java.util.ArrayList;


public class ErrorCommand implements Commands{
    private static StendDLLCommands stendDLLCommands;

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


    //Нужен сеттер т.к. инициализация будет с другого класса
    private int constant;

    //Необходимо для быстрого обхода в цикле
    private int[] amountActivePlacesForTest = getAmountActivePlacesForTest();

    private ArrayList<double[]> errorList = new ArrayList<>(amountActivePlacesForTest.length);

    public ArrayList<double[]> getErrorList() {
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

        for (int value : amountActivePlacesForTest) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(value, constant, pulse);
        }

        //Тут надо что-то придумать
        while (errorList.contains(null)) {
            for (int i = 0; i < amountActivePlacesForTest.length; i++) {
                errorList.add(i, stendDLLCommands.meterErrorReadMass(getAmountActivePlacesForTest()[i], stendDLLCommands.getCountResult()));
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stendDLLCommands.powerOf();
        stendDLLCommands.errorClear();

        return true;
    }

    //Тут тоже необходимо подумать
    private int[] getAmountActivePlacesForTest() {
        ArrayList<Integer> list = new ArrayList<>();
        for (int i = 1; i <= stendDLLCommands.getAmountActivePlaces().length; i++) {
            if (stendDLLCommands.getAmountActivePlaces()[i]) {
                list.add(i);
            }
        }
        int[] init = new int[list.size()];

        for (int i = 0; i  < list.size(); i++) {
            init[i] = list.get(i);
        }

        return init;
    }

    public void setConstant(int constant) {
        this.constant = constant;
    }
}
