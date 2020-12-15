package org.taipit.stend.model;

import org.taipit.stend.controller.Meter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является хранилищем всех сохранённых результатов теста.
 */
public class ResultsTest implements Serializable {

    private static final long serialVersionUID = 1111111L;

    private static ResultsTest resultsTestInstance = deserializationResults();

    //private Map<String, Meter> mapAllResults = new TreeMap<>();
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
            System.out.println("Файл не найден");
            e.printStackTrace();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
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
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }return false;
    }

    public List<Meter> getListAllResults() {
        return listAllResults;
    }
}
