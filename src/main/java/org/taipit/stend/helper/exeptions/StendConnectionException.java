package org.taipit.stend.helper.exeptions;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за вывод ошибок связанных с работой стенда.
 */
public class StendConnectionException extends Exception {
    public StendConnectionException() {
    }

    public StendConnectionException(String message) {
        super(message);
    }
}
