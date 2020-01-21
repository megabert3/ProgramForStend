package stend.model;

import stend.helper.exeptions.InfoExeption;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MethodicsForTest {

    private static MethodicsForTest methodicsForTestInstance;

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("1.0 Imax", "0.5 Imax", "0.2 Imax", "0.5 Ib", "1.0 Ib","0.2 Ib", "0.1 Ib", "0.05 Ib", "0.02 Ib", "0.01 Ib", "0.01 Ib");

    private MethodicsForTest() {}

    public static MethodicsForTest getMethodicsForTestInstance() {
        if (methodicsForTestInstance == null) {
            methodicsForTestInstance = new MethodicsForTest();
        }
        return methodicsForTestInstance;
    }

    private HashMap<String, Methodic> methodicsMap = new HashMap<>();

    public boolean addMethodicListToMap(String name, Methodic methodicList) throws InfoExeption {
        if (methodicsMap.containsKey(name)) throw new InfoExeption();
        methodicsMap.put(name, methodicList);
        return true;
    }

    public HashMap<String, Methodic> getMethodicsMap() {
        return methodicsMap;
    }

    public List<String> getPowerFactor() {
        return powerFactor;
    }

    public List<String> getCurrent() {
        return current;
    }
}
