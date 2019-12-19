package stend.controller;

import java.util.Arrays;
import java.util.List;

public class ThreePhaseStend extends StendDLLCommands{
    /**
     * Трехфазный стенд
     */
    private static ThreePhaseStend threePhaseStend;
    public List<String> stendModelList = Arrays.asList("HS3103", "HS6103");
    public List<String> refMetModelList = Arrays.asList("HY5303C-22", "TC-3000B", "TC-3000D", "HC3100", "HS5300H");

    private ThreePhaseStend() {}

    public static ThreePhaseStend getThreePhaseStendInstance() {
        if (threePhaseStend == null) {
            threePhaseStend = new ThreePhaseStend();
        }
        return threePhaseStend;
    }

    public List<String> getStendModelList() {
        return stendModelList;
    }

    public List<String> getRefMetModelList() {
        return refMetModelList;
    }
}
