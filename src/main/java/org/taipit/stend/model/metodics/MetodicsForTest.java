package org.taipit.stend.model.metodics;

import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.InfoExсeption;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за хранение всех созданных методик испытаний.
 */
public class MetodicsForTest implements Serializable {

    private static final long serialVersionUID = 19930824L;

    //Паттерн Singleton
    private static MetodicsForTest metodicsForTestInstance = deserializationMetodics();

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("1.0 Imax", "0.5 Imax", "0.2 Imax", "0.5 Ib", "1.0 Ib","0.2 Ib", "0.1 Ib", "0.05 Ib", "0.02 Ib", "0.01 Ib");

    private MetodicsForTest() {
    }

    public static MetodicsForTest getMetodicsForTestInstance() {

        if (metodicsForTestInstance == null) {
            metodicsForTestInstance = new MetodicsForTest();
        }

        return metodicsForTestInstance;
    }

    //Лист со всеми методиками
    private ArrayList<Metodic> methodicForStends = new ArrayList<>();

    /**
     * Добавление новой методики в хранилище методик
     * @param name
     * @param methodicForStendList
     * @return
     * @throws InfoExсeption
     */
    public void addMethodicToList(String name, Metodic methodicForStendList) throws InfoExсeption {

        for (Metodic metodic : methodicForStends) {
            if (metodic.getMetodicName().equals(name)) throw new InfoExсeption("Методика с таким именем уже существует");
        }

        methodicForStendList.setMetodicName(name);
        methodicForStends.add(methodicForStendList);
    }

    /**
     * Удаляет методику и листа (хранилища)
     * @param name
     * @return
     */
    public boolean deleteMethodic(String name) {
        for (Metodic methodicForStend : methodicForStends) {

            if (methodicForStend.getMetodicName().equals(name)) {
                methodicForStends.remove(methodicForStend);
                return true;
            }
        }
        return false;
    }

    /**
     * Производит сериализацию всех методик
     * @return
     */
    public boolean serializationMetodics() {

        try (FileOutputStream fileOutputStream = new FileOutputStream(new File(".\\src\\main\\resources\\methodicForStends").getCanonicalPath())) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(MetodicsForTest.getMetodicsForTestInstance());

            objectOutputStream.flush();
            return true;

        } catch (FileNotFoundException e) {
            ConsoleHelper.infoException(e.getMessage());
        } catch (IOException e) {
            ConsoleHelper.infoException(e.getMessage());
            return false;
        } return false;
    }


    /**
     * Производит дессериализацию всех методик поверки из файла
     * @return
     */
    private static MetodicsForTest deserializationMetodics() {
        try(FileInputStream fileInputStream = new FileInputStream(new File(".\\src\\main\\resources\\methodicForStends").getCanonicalPath())) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            return (MetodicsForTest) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            ConsoleHelper.infoException(e.getMessage());

        } catch (IOException | ClassNotFoundException e) {
            ConsoleHelper.infoException(e.getMessage());
        }
        return null;
    }

    public ArrayList<Metodic> getMethodicForStends() {
        return methodicForStends;
    }

    public List<String> getPowerFactor() {
        return powerFactor;
    }

    public List<String> getCurrent() {
        return current;
    }

    public Metodic getMetodic(String name) {
        for (Metodic methodicForStend : methodicForStends) {
            if (methodicForStend.getMetodicName().equals(name)) {
                return methodicForStend;
            }
        }
        return null;
    }
}
