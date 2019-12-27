package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;
import sun.reflect.generics.reflectiveObjects.ParameterizedTypeImpl;

import java.util.HashMap;
import java.util.Map;


public class ErrorCommand implements Commands {

    private StendDLLCommands stendDLLCommands;

    //Базовый или максимальный ток из конструктора
    private String current;

    //Базовый ток
    private double Ib;

    //Максимальный ток
    private double Imax;



    private int phase;
    private double ratedVolt;

    //Будет расчётной
    private double ratedCurr;

    private double ratedFreq;

    //Необходимо сделать в доп тестовом окне
    private int phaseSrequence;

    private int revers;

    private double voltPer;
    private double currPer;
    private String iABC;
    private String cosP;

    //Импульсный выход
    private int channelFlag;

    //Кол-во импульсов для расчёта ошибки
    private int pulse;

    //Количество повторов теста
    private int countResult = 2;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop = initBoolList();

    /**
        Не забудь создать второй конструктор для другого окна
     */
    public ErrorCommand(StendDLLCommands stendDLLCommands, int phase, String current,
                        int revers, double currPer, String iABC, String cosP, int channelFlag) {
        this.stendDLLCommands = stendDLLCommands;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currPer = currPer;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
    }

    @Override
    public void execute() {
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP);

        stendDLLCommands.setEnergyPulse(channelFlag);

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());

        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(meter.getKey(), stendDLLCommands.getConstant(), pulse);
        }

        while (flagInStop.containsValue(false)) {
            int resultNo;
            String strError;
            String[] strMass;
            double error;

            for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
                strError = stendDLLCommands.meterErrorRead(meter.getKey());
                strMass = strError.split(",");
                resultNo = Integer.parseInt(strMass[0]);
                error = Double.parseDouble(strMass[1]);
                StendDLLCommands.amountActivePlacesForTest.get(meter.getKey()).getErrors()[resultNo] = error;

                if (resultNo >= countResult) {
                    flagInStop.put(meter.getKey(), true);
                }
            }

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stendDLLCommands.powerOf();
        stendDLLCommands.errorClear();
    }

    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(StendDLLCommands.amountActivePlacesForTest.size());
        for (Map.Entry<Integer, Meter> meter : StendDLLCommands.amountActivePlacesForTest.entrySet()) {
            init.put(meter.getKey(), false);
        }
        return init;
    }

    public void setPulse(int pulse) {
        this.pulse = pulse;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }
}
