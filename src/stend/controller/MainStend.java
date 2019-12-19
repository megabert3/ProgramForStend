package stend.controller;

import stend.helper.ConsoleHelper;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.ResourceBundle;

public class MainStend {
    public static void main(String[] args) {
        //Проверка ErrorCommand
//        StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//        ErrorCommand errorCommand = new ErrorCommand(stendDLLCommands, 1, 230.0, 5.0, 50.0, 0,
//                0, 100.0, 100.0, "H", "1.0", 0, 10);
//
//        try {
//            errorCommand.execute();
//            for (Map.Entry<Integer, double[]> map : errorCommand.getErrorList().entrySet()) {
//                ConsoleHelper.getMessage(map.getKey() + " : " + Arrays.toString(map.getValue()));
//            }
//        }catch (Exception e) {
//            e.printStackTrace();
//        }

        //Проверка CreepCommand
//        try {
//            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//            CreepCommand creepCommand = new CreepCommand(stendDLLCommands, 1, 230.0, 0.0, 50.0, 0,
//                    0, 100.0, 100.0, 0, 2);
//            Thread.sleep(2000);
//            creepCommand.execute();
//
//            for (Map.Entry<Integer, Boolean> map : creepCommand.getCreepCommandResult().entrySet()) {
//                System.out.println(map.getKey() + " : " + map.getValue());
//            }
//        }catch (Exception e) {
//            e.printStackTrace();

        //Проверка StartCommand
//        try {
//            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//            StartCommand startCommand = new StartCommand(stendDLLCommands, 1, 230.0, 0.0, 50.0, 0,
//                    0, 100.0, 100.0, 0, 2);
//            Thread.sleep(2000);
//            startCommand.execute();
//
//            for (Map.Entry<Integer, Boolean> map : startCommand.getCreepCommandResult().entrySet()) {
//                System.out.println(map.getKey() + " : " + map.getValue());
//            }
//        }catch (Exception e) {
//            e.printStackTrace();

//            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//            RTCCommand rtcCommand = new RTCCommand(stendDLLCommands, 1, 230.0, 0.0, 50.0, 0,
//                    0, 100.0, 100.0,0, 10);
//
//            rtcCommand.execute();
//            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
//            stendDLLCommands.getUI(0);
//            Thread.sleep(7000);
//
//            System.out.println(stendDLLCommands.setPulseChannel(1, 0));
//            System.out.println(stendDLLCommands.setPulseChannel(2, 0));
//            System.out.println(stendDLLCommands.setPulseChannel(3, 0));
//
//        for (int j = 1; j < 50; j++) {
//
//            stendDLLCommands.clockErrorStart(1, 1.0, 10);
//            stendDLLCommands.clockErrorStart(2, 1.0, 10);
//            stendDLLCommands.clockErrorStart(3, 1.0, 10);
//
//            Thread.sleep(10 * 1000);
//
//            for (int i = 0; i < 25; i++) {
//                Thread.sleep(200);
//                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, 1));
//                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, 2));
//                System.out.println(stendDLLCommands.clockErrorRead(1.0, 0, 3));
//                System.out.println();
//            }
//        }


//            StendDLLCommands stendDLLCommands = new ThreePhaseStend(9, "HY5303C-22");
//            stendDLLCommands.getUI(5);
//            Thread.sleep(7000);
//
//            stendDLLCommands.setPulseChannel(2, 0);
//            stendDLLCommands.setPulseChannel(3, 0);
//
//            stendDLLCommands.constTestStart(2, 800);
//            stendDLLCommands.constTestStart(3, 800);
//
//            ConsoleHelper.getMessage(String.valueOf(stendDLLCommands.constPulseRead("2.0", "3.0", 200, 2)));
//            ConsoleHelper.getMessage(String.valueOf(stendDLLCommands.constPulseRead("2.0", "3.0", 800, 3)));
//
//
//
//            stendDLLCommands.errorClear();
//            stendDLLCommands.powerOf();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("stendProperties");
//        MainStend mainStend = new MainStend();
//        Properties properties = mainStend.getProperties();
//
//        for (String key : properties.stringPropertyNames())
//        {
//            System.out.println(properties.get(key));
//        }

    }
    private Properties getProperties() {
        Properties properties = new Properties();
        ResourceBundle resourceBundle = ResourceBundle.getBundle("stendProperties.properties");
        System.out.println(resourceBundle.containsKey("stendType"));
        return properties;
    }
}
