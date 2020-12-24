package org.taipit.stend.model.result;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.helper.ConsoleHelper;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Внутренний класс для работы с несохранёнными результатами
 * испытаний.
 */
public class NotSavedResults implements Serializable{

    private static final long serialVersionUID = 14886969L;

    //Счётчики с несохранёнными результатами
    private List<Meter> metersWhoseResultsAreNotSaved = new ArrayList<>();

    private static NotSavedResults notSavedResultsInstance;

    private NotSavedResults(){}

    public static NotSavedResults getNotSavedResultsInstance(String dir) {

        if (notSavedResultsInstance == null) {
            notSavedResultsInstance = deserializationResults(dir);
        }

        return notSavedResultsInstance;
    }

    private static NotSavedResults deserializationResults(String dir) {

        try (FileInputStream fileInputStream = new FileInputStream(new File(dir).getCanonicalPath())) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            return (NotSavedResults) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            System.out.println("Нет файла с прошлыми результатами");

        } catch (IOException e) {
            ConsoleHelper.infoException("Возникла ошибка при работе с файлом содержащим прошлые результаты теста\n" + e.getMessage());
        } catch (ClassNotFoundException e) {
            ConsoleHelper.infoException("Возникла ошибка при выгрузке прошлых результатов теста\n" + e.getMessage());
        }

        return null;
    }

    public static void serializationNotSaveResults(List<Meter> metersWithResults, String dir) {

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(dir).getCanonicalPath())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);

            NotSavedResults notSavedResults = getNotSavedResultsInstance(dir);

            if (notSavedResults == null) {
                notSavedResultsInstance = new NotSavedResults();

                notSavedResults = notSavedResultsInstance;
            }

            notSavedResultsInstance.setMetersWhoseResultsAreNotSaved(metersWithResults);

            objectOutputStream.writeObject(notSavedResults);

            objectOutputStream.flush();

        } catch (FileNotFoundException e) {
            ConsoleHelper.infoException("Программе не удалось сохранить результаты теста на компьютер.\n" + e.getMessage());
        } catch (IOException e) {
            ConsoleHelper.infoException("Программе не удалось сохранить результаты теста на компьютер.\n" + e.getMessage());
        }
    }

    public List<Meter> getMetersWhoseResultsAreNotSaved() {
        return metersWhoseResultsAreNotSaved;
    }

    public void setMetersWhoseResultsAreNotSaved(List<Meter> metersWhoseResultsAreNotSaved) {
        this.metersWhoseResultsAreNotSaved = metersWhoseResultsAreNotSaved;
    }
}
