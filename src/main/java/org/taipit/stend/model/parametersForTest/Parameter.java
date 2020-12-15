package org.taipit.stend.model.parametersForTest;

/**
 * @autor Albert Khalimov
 *
 * Данный интерфейс выполняет роль расширения списка возможных предлагаемых параметров
 * для реализации полиморфизма
 */
public interface Parameter {

    String getNameParam();
    void setNameParam(String nameParam);
}
