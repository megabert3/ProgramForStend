package stend.controller;

import com.sun.xml.internal.bind.v2.runtime.reflect.Lister;
import stend.helper.ConsoleHelper;

import java.util.ArrayList;
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

    //Необходимо для быстрого обхода в цикле
    private int[] amountActivePlacesForTest = getAmountActivePlacesForTest();

    private double[][] errorsMeters = new double[stendDLLCommands.getAmountPlaces()][9];



    public ErrorCommand(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
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

        for (int i = 0; i < amountActivePlacesForTest.length; i++) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(amountActivePlacesForTest[i], stendDLLCommands.getConstantsForMetersOnPlaces()[i], pulse);

        }

        //Тут надо что-то придумать
        while (true) {
            for (int i = 1; i < stendDLLCommands.getAmountPlaces(); i++) {
                if (stendDLLCommands.getAmountActivePlaces()[i]) {

                }
            }

        }
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
}
