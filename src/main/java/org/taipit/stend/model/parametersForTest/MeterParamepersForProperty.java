package org.taipit.stend.model.parametersForTest;

import org.taipit.stend.helper.ConsoleHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за сохранение и вывод предлагаемых параметров для проведения теста.
 */
public class MeterParamepersForProperty {

    //паттерн Singleton
    private static MeterParamepersForProperty meterParamepersForProperty;

    //Параметры, которые будут предлагаться
    private List<MeterParameter> meterParameterList = initParametersList();

    private MeterParamepersForProperty() {}

    public static MeterParamepersForProperty getInstance() {
        if (meterParamepersForProperty == null) {
            meterParamepersForProperty = new MeterParamepersForProperty();
        }
        return meterParamepersForProperty;
    }

    private List<MeterParameter> initParametersList() {

        //Файл настрек где лежат параметры
        Properties properties = ConsoleHelper.properties;

        List<MeterParameter> meterParametrList = new ArrayList<>();

        //Вырианты параметра "Модель счётчика"
        MeterParameter meterModel = new MeterParameter("Модель счётчика", "meterModels");
        meterModel.setParameterValues(Arrays.asList(properties.getProperty("meterModels").split(", ")));
        meterParametrList.add(meterModel);

        //Вырианты параметра "Оператор"
        MeterParameter meterOperator = new MeterParameter("Оператор", "Operators");
        meterOperator.setParameterValues(Arrays.asList(properties.getProperty("Operators").split(", ")));
        meterParametrList.add(meterOperator);

        //Вырианты параметра "Контроллёр"
        MeterParameter meterController = new MeterParameter("Контроллёр", "Controllers");
        meterController.setParameterValues(Arrays.asList(properties.getProperty("Controllers").split(", ")));
        meterParametrList.add(meterController);

        //Вырианты параметра "Поверитель"
        MeterParameter witneses = new MeterParameter("Поверитель", "Witneses");
        witneses.setParameterValues(Arrays.asList(properties.getProperty("Witneses").split(", ")));
        meterParametrList.add(witneses);

        //Вырианты параметра "Напряжение"
        MeterParameter Un = new MeterParameter("Напряжение", "Unom");
        Un.setParameterValues(Arrays.asList(properties.getProperty("Unom").split(", ")));
        meterParametrList.add(Un);

        //Вырианты параметра "Ток"
        MeterParameter Inom = new MeterParameter("Ток", "InomAndImax");
        Inom.setParameterValues(Arrays.asList(properties.getProperty("InomAndImax").split(", ")));
        meterParametrList.add(Inom);

        //Вырианты параметра "Частота"
        MeterParameter Fnom = new MeterParameter("Частота", "Fnom");
        Fnom.setParameterValues(Arrays.asList(properties.getProperty("Fnom").split(", ")));
        meterParametrList.add(Fnom);

        //Вырианты параметра "Завод изготовитель"
        MeterParameter factoryManufacturer = new MeterParameter("Завод изготовитель", "meterManufacturer");
        factoryManufacturer.setParameterValues(Arrays.asList(properties.getProperty("meterManufacturer").split(", ")));
        meterParametrList.add(factoryManufacturer);

        return meterParametrList;
    }

    public List<MeterParameter> getMeterParameterList() {
        return meterParameterList;
    }
}
