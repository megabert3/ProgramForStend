package org.taipit.stend.model;

import org.taipit.stend.helper.ConsoleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MeterParamepersForProperty {

    private static MeterParamepersForProperty meterParamepersForProperty;

    private List<MeterParameter> meterParameterList = initParametersList();

    private MeterParamepersForProperty() {}

    public static MeterParamepersForProperty getInstance() {
        if (meterParamepersForProperty == null) {
            meterParamepersForProperty = new MeterParamepersForProperty();
        }
        return meterParamepersForProperty;
    }

    private List<MeterParameter> initParametersList() {
        Properties properties = ConsoleHelper.properties;
        List<MeterParameter> meterParametrList = new ArrayList<>();

        MeterParameter meterModel = new MeterParameter("Модель счётчика", "meterModels");
        meterModel.setParameterValues(Arrays.asList(properties.getProperty("meterModels").split(", ")));
        meterParametrList.add(meterModel);

        MeterParameter meterOperator = new MeterParameter("Оператор", "Operators");
        meterOperator.setParameterValues(Arrays.asList(properties.getProperty("Operators").split(", ")));
        meterParametrList.add(meterOperator);

        MeterParameter meterController = new MeterParameter("Контроллёр", "Controllers");
        meterController.setParameterValues(Arrays.asList(properties.getProperty("Controllers").split(", ")));
        meterParametrList.add(meterController);

        MeterParameter witneses = new MeterParameter("Поверитель", "Witneses");
        witneses.setParameterValues(Arrays.asList(properties.getProperty("Witneses").split(", ")));
        meterParametrList.add(witneses);

        MeterParameter Un = new MeterParameter("Напряжение", "Unom");
        Un.setParameterValues(Arrays.asList(properties.getProperty("Unom").split(", ")));
        meterParametrList.add(Un);

        MeterParameter Inom = new MeterParameter("Ток", "InomAndImax");
        Inom.setParameterValues(Arrays.asList(properties.getProperty("InomAndImax").split(", ")));
        meterParametrList.add(Inom);

        MeterParameter Fnom = new MeterParameter("Частота", "Fnom");
        Fnom.setParameterValues(Arrays.asList(properties.getProperty("Fnom").split(", ")));
        meterParametrList.add(Fnom);

        MeterParameter factoryManufacturer = new MeterParameter("Завод изготовитель", "meterManufacturer");
        factoryManufacturer.setParameterValues(Arrays.asList(properties.getProperty("meterManufacturer").split(", ")));
        meterParametrList.add(factoryManufacturer);

        return meterParametrList;
    }

    public List<MeterParameter> getMeterParameterList() {
        return meterParameterList;
    }
}
