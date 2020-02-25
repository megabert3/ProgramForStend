package org.taipit.stend.controller;


import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.StendDLL;

import java.util.Arrays;
import java.util.List;

public class OnePhaseStend extends StendDLLCommands{
    /**
     * Однофазный стенд
     */
    private static OnePhaseStend onePhaseStendInstance;
    public static List<String> stendModelList = Arrays.asList("HS3103", "HS6103");
    public static List<String> refMetModelList = Arrays.asList("HY5101C-22", "HY5101C-23", "TC-3000B", "TC-3000C", "HC3801", "HC3101A", "HS5100H");

    private OnePhaseStend() {}

    public static OnePhaseStend getOnePhaseStendInstance() {
        if (onePhaseStendInstance == null) {
            onePhaseStendInstance = new OnePhaseStend();
        }
        return onePhaseStendInstance;
    }

    private StendDLL stendDLL = StendDLL.INSTANCE;

    private String stendModel = ConsoleHelper.properties.getProperty("stendModel");

    private String refMetModel = ConsoleHelper.properties.getProperty("refMeterModel");

    private int port = Integer.parseInt(ConsoleHelper.properties.getProperty("stendCOMPort"));

    public List<String> getStendModelList() {
        return stendModelList;
    }

    public List<String> getRefMetModelList() {
        return refMetModelList;
    }


}
