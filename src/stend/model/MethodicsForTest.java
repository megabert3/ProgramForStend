package stend.model;

import stend.helper.exeptions.InfoExeption;

import java.util.HashMap;

public class MethodicsForTest {

    public static HashMap<String, MethodicsForTest> methodicsMap = new HashMap<>();

    public void addMethodicListToMap(String name, MethodicsForTest methodicList) throws InfoExeption {
        if (methodicsMap.containsKey(name)) throw new InfoExeption();
        methodicsMap.put(name, methodicList);
    }
}
