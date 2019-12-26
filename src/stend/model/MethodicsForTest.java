package stend.model;

import stend.helper.exeptions.InfoExeption;

import java.util.HashMap;

public class MethodicsForTest {

    private static MethodicsForTest methodicsForTestInstance;

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
}
