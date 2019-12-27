package stend.controller;


import stend.model.StendDLL;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

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

    private ResourceBundle res = ResourceBundle.getBundle("resourseFiles");

    private String stendModel = res.getString("stendModel");

    private String refMetModel = res.getString("refMeterModel");

    private int port = Integer.parseInt(res.getString("stendCOMPort"));

    public List<String> getStendModelList() {
        return stendModelList;
    }

    public List<String> getRefMetModelList() {
        return refMetModelList;
    }


}
