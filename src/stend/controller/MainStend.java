package stend.controller;



import java.util.Map;

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
        try {
            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
            CreepCommand creepCommand = new CreepCommand(stendDLLCommands, 1, 230.0, 0.0, 50.0, 0,
                    0, 100.0, 100.0, 0, 2);
            Thread.sleep(2000);
            creepCommand.execute();

            for (Map.Entry<Integer, Boolean> map : creepCommand.getCreepCommandResult().entrySet()) {
                System.out.println(map.getKey() + " : " + map.getValue());
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally{
            StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }

    }
}
