package org.taipit.stend.model;

import org.taipit.stend.helper.exeptions.InfoExсeption;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MethodicsForTest implements Serializable {

    private static final long serialVersionUID = 19930826L;

    private static MethodicsForTest methodicsForTestInstance = deserializationMetodics();

    //Значения коэффициента мощности
    private List<String> powerFactor = Arrays.asList("1.0", "0.5L", "0.5C", "0.25L", "0.25C", "0.8L", "0.8C");

    //Значения выставленного тока
    private List<String> current = Arrays.asList("1.0 Imax", "0.5 Imax", "0.2 Imax", "0.5 Ib", "1.0 Ib","0.2 Ib", "0.1 Ib", "0.05 Ib", "0.02 Ib", "0.01 Ib", "0.01 Ib");

    private MethodicsForTest() {
    }

    public static MethodicsForTest getMethodicsForTestInstance() {

        if (methodicsForTestInstance == null) {
            methodicsForTestInstance = new MethodicsForTest();
        }
        return methodicsForTestInstance;
    }

    //Лист со всеми методиками
    private ArrayList<Methodic> methodics = new ArrayList<>();

    //Добавление методики в список
    public boolean addMethodicToList(String name, Methodic methodicList) throws InfoExсeption {
        for (Methodic methodic : methodics) {
            if (methodic.getMethodicName().equals(name)) throw new InfoExсeption();
        }
        methodicList.setMethodicName(name);
        methodics.add(methodicList);
        return true;
    }

    //Удаление методики
    public boolean deleteMethodic(String name) {
        for (Methodic methodic : methodics) {

            if (methodic.getMethodicName().equals(name)) {
                methodics.remove(methodic);
                return true;
            }
        }
        return false;
    }

    //Запись сохранённых данных в файл
    public boolean serializationMetodics() {

        try (FileOutputStream fileOutputStream = new FileOutputStream("metodics")) {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(MethodicsForTest.getMethodicsForTestInstance());

            objectOutputStream.flush();
            return true;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }return false;
    }


    //Считывание сохранённых методик
    private static MethodicsForTest deserializationMetodics() {
        try(FileInputStream fileInputStream = new FileInputStream("metodics")) {
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);

            return (MethodicsForTest) objectInputStream.readObject();

        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (ClassNotFoundException ignore) {
            ignore.printStackTrace();
        }
        return null;
    }

    public ArrayList<Methodic> getMethodics() {
        return methodics;
    }

    public List<String> getPowerFactor() {
        return powerFactor;
    }

    public List<String> getCurrent() {
        return current;
    }

    public Methodic getMethodic(String name) {
        for (Methodic methodic : methodics) {
            if (methodic.getMethodicName().equals(name)) {
                return methodic;
            }
        }
        return null;
    }
}
