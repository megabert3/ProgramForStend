package org.taipit.stend.model.parametersForTest;

import java.util.ArrayList;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является реализацией конкретного параметра для списка параметров предлагаемых для запуска теста.
 */
public class MeterParameter implements Parameter {

    //Название параметра
    private String nameParam;

    private String id;

    //Значения параметра
    private List<String> parameterValues = new ArrayList<>();

    public MeterParameter(String nameParam, String id) {
        this.nameParam = nameParam;
        this.id = id;
    }

    public String getNameParam() {
        return nameParam;
    }

    public void setNameParam(String nameParam) {
        this.nameParam = nameParam;
    }

    public List<String> getParameterValues() {
        return parameterValues;
    }

    public void setParameterValues(List<String> parameterValues) {
        this.parameterValues = parameterValues;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
