package org.taipit.stend.model;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является хранилищем всех сохранённых результатов теста.
 */
public class ResultsTest implements Serializable {

    private static final long serialVersionUID = 14886969L;

    private static ResultsTest resultsTestInstance = deserializationResults();

    private List<Meter> listAllResults = new ArrayList<>();

    private ResultsTest(){}

    public static ResultsTest getResultsTestInstance() {

        if (resultsTestInstance == null) {
            resultsTestInstance = new ResultsTest();
        }

        return resultsTestInstance;
    }

    public void addMeterRusults(List<Meter> meterList) {
        listAllResults.addAll(meterList);
    }

    private static ResultsTest deserializationResults() {

        try(FileInputStream fileInputStream = new FileInputStream(new File(".\\src\\main\\resources\\results").getCanonicalPath())) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            return (ResultsTest) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            ConsoleHelper.infoException("Программе не удалось найти файл с результатами тестов");

        } catch (IOException e) {
            ConsoleHelper.infoException("Возникла проблема при работе с файлом содержащим результаты\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            ConsoleHelper.infoException("Возникла проблема при выгрузке результатов теста\n" + e.getMessage());
        }

        return null;
    }

    public boolean serializationResults() {
        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(".\\src\\main\\resources\\results").getCanonicalPath())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(ResultsTest.getResultsTestInstance());

            objectOutputStream.flush();
            return true;

        } catch (FileNotFoundException e) {
            ConsoleHelper.infoException("Программе не удалось сохранить результаты теста на компьютер.\n" + e.getMessage());
        } catch (IOException e) {
            ConsoleHelper.infoException("Программе не удалось сохранить результаты теста на компьютер.\n" + e.getMessage());
            return false;
        }
        return false;
    }

    public List<Meter> getListAllResults() {
        return listAllResults;
    }
}
