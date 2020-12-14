package org.taipit.stend.helper.exeptions;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за вывод информационных ошибок, возникающих при работе прораммы.
 */
public class InfoExсeption extends Exception {

    public InfoExсeption(){}

    public InfoExсeption(String mess) {
        super(mess);
    }
}
