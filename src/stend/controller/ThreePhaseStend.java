package stend.controller;

import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

public class ThreePhaseStend extends StendDLLCommands{
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

    private ResourceBundle res = ResourceBundle.getBundle("resourseFiles");

    private String stendModel = res.getString("stendModel");

    private String refMetModel = res.getString("refMeterModel");

    public List<String> getStendModelList() {
        return stendModelList;
    }

    public List<String> getRefMetModelList() {
        return refMetModelList;
    }

}
