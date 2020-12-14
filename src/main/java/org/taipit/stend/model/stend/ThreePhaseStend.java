package org.taipit.stend.model.stend;


import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за реализацию работы трехфазного стенда.
 */
public class ThreePhaseStend extends StendDLLCommands implements Serializable {

    private static ThreePhaseStend threePhaseStend;

    //типы эталонных счётчиков
    public static List<String> stendModelList = Arrays.asList("HS3303", "HS6303");
    public static List<String> refMetModelList = Arrays.asList("HY5303C-22", "TC-3000B", "TC-3000D", "HC3100", "HS5300H");

    private ThreePhaseStend() {}

    public static ThreePhaseStend getThreePhaseStendInstance() {
        if (threePhaseStend == null) {
            threePhaseStend = new ThreePhaseStend();
        }
        return threePhaseStend;
    }
}
