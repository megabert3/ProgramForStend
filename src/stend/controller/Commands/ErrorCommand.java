package stend.controller.Commands;

import stend.controller.Meter;
import stend.controller.StendDLLCommands;
import stend.helper.ConsoleHelper;

import java.util.HashMap;
import java.util.Map;


public class ErrorCommand implements Commands {

    private StendDLLCommands stendDLLCommands;

    //Максимальный порог ошибки
    private String emin = "-1.0";

    //Минимальный порог ошибки
    private String emax = "1.0";

    //Кол-во импульсов для расчёта ошибки
    private String pulse = "20";

    //Имя точки для отображения в таблице
    private String name;

    //Базовый или максимальный ток из конструктора
    private String current;

    //id кнопки
    private String id;

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

    //Расчётный процент из
    private double currPer;

    //Стринговый процент получаемый из конструктора
    private String currentPerсent;
    private String iABC;
    private String cosP;

    //Импульсный выход
    private int channelFlag;

    //Количество повторов теста
    private int countResult = 2;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop;

        public ErrorCommand(String id, int phase, String current,
                        int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currentPerсent = currentPercent;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;

        if (iABC.equals("H")) {
            name = (cosP + "; " + currentPerсent + " " + current.trim());
        } else name = (iABC + ": " + cosP + "; " + currentPerсent + " " + current);

        currPer = Double.parseDouble(currentPerсent) * 100;
        phaseSrequence = 0;
        voltPer = 100.0;
    }

    public ErrorCommand(String param, String id, int phase, String current,
                        double voltPer, int revers, String currentPercent, String iABC, String cosP, int channelFlag) {
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.currentPerсent = currentPercent;
        this.iABC = iABC;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
        this.voltPer = voltPer;

        //47.0%Un: 0.5L; 0.01 Ib
        name = (voltPer + "%" + param + "n: " + cosP + "; " + currentPerсent + " " + current.trim());

        currPer = Double.parseDouble(currentPerсent) * 100;
        phaseSrequence = 0;
    }

    @Override
    public void execute() {
            flagInStop = initBoolList();

        if (current.equals("Ib")) {
            ratedCurr = Ib;
        } else {
            ratedCurr = Imax;
        }
        stendDLLCommands.getUI(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPer, currPer, iABC, cosP);

        stendDLLCommands.setEnergyPulse(channelFlag);

        ConsoleHelper.sleep(stendDLLCommands.getPauseForStabization());

        for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
            //Подумать над константой, скорее всего необходимо будет сделать одной для всех
            stendDLLCommands.errorStart(meter.getKey(), stendDLLCommands.getConstant(), Integer.parseInt(pulse));
        }

        while (flagInStop.containsValue(false)) {
            int resultNo;
            String strError;
            String[] strMass;
            double error;

            for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
                strError = stendDLLCommands.meterErrorRead(meter.getKey());
                strMass = strError.split(",");
                resultNo = Integer.parseInt(strMass[0]);
                error = Double.parseDouble(strMass[1]);
                stendDLLCommands.getAmountActivePlacesForTest().get(meter.getKey()).getErrors()[resultNo] = error;

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
        HashMap<Integer, Boolean> init = new HashMap<>(stendDLLCommands.getAmountActivePlacesForTest().size());

        for (Map.Entry<Integer, Meter> meter : stendDLLCommands.getAmountActivePlacesForTest().entrySet()) {
            init.put(meter.getKey(), false);
        }
        return init;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public void setRatedFreq(double ratedFreq) {
        this.ratedFreq = ratedFreq;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public void setEmin(String emin) {
        this.emin = emin;
    }

    public void setEmax(String emax) {
        this.emax = emax;
    }

    public String getName() {
        return name;
    }

    public String getEmin() {
        return emin;
    }

    public String getEmax() {
        return emax;
    }

    public String getPulse() {
        return pulse;
    }

    public void setImax(double imax) {
        Imax = imax;
    }

    public void setIb(double ib) {
        Ib = ib;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        if (iABC.equals("H")) {
            return (cosP + "; " + currentPerсent + " "  + current);
        } else return (iABC + ": " + cosP + "; " + currentPerсent + " "  + current);
    }
}
