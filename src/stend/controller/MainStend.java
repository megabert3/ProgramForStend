package stend.controller;


import stend.helper.ConsoleHelper;
import java.util.Map;


public class MainStend {
    public static void main(String[] args) {

        for (Map.Entry<Object, Object> prop: ConsoleHelper.properties.entrySet())
        {
            System.out.println(prop.getKey() + " : " + prop.getValue());
        }

        ConsoleHelper.properties.setProperty("stendAmountPlaces", "abc");

        System.out.println("---------------------------------------------");

        for (Map.Entry<Object, Object> prop: ConsoleHelper.properties.entrySet())
        {
            System.out.println(prop.getKey() + " : " + prop.getValue());
        }

        boolean stend = ConsoleHelper.properties.getProperty("stendType").equals("OnePhaseStend");
        System.out.println(stend);

    }
}
