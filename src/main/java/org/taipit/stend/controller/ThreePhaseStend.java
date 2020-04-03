package org.taipit.stend.controller;

import org.taipit.stend.helper.ConsoleHelper;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class ThreePhaseStend extends StendDLLCommands implements Serializable {
    /**
     * Трехфазный стенд
     */
    private static ThreePhaseStend threePhaseStend;
    public static List<String> stendModelList = Arrays.asList("HS3303", "HS6303");
    public static List<String> refMetModelList = Arrays.asList("HY5303C-22", "TC-3000B", "TC-3000D", "HC3100", "HS5300H");

    private ThreePhaseStend() {}

    public static ThreePhaseStend getThreePhaseStendInstance() {
        if (threePhaseStend == null) {
            threePhaseStend = new ThreePhaseStend();
        }
        return threePhaseStend;
    }

    private String stendModel = ConsoleHelper.properties.getProperty("stendModel");

    private String refMetModel = ConsoleHelper.properties.getProperty("refMeterModel");

    public List<String> getStendModelList() {
        return stendModelList;
    }

    public List<String> getRefMetModelList() {
        return refMetModelList;
    }

}