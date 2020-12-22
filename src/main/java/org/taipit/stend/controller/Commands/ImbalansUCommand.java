package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.helper.exeptions.StendConnectionException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;


/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за реализацию выполнения команды "Имбаланс напряжений", данный класс применим только к трехфазным счётчикам.
 * Напряжение и ток выставляются только на те фазы, которые выбрал пользователь, отличие от ErrorCommand в том, что
 * в ErrorCommand напряжение выставлялось на все фазы, а ток подавался либо на все либо на определённые. Тут напряжение выставляется
 * только на необходимую для испытания фазу(ы). Снятие погрешности при этом такое же как и у команды ErrorCommand
 *
 * Поверочный стенд выставляет параметры напряжения, тока и др. согласно пользовательским
 * и в дальнейшем сравнивает мощность посчитанную счетчиком(и) с той, которую посчитал эталонный счётчик.
 * В результате расчитывается относительная погрешность счётчика
 *
 * За дополнительной информацией описания полей см. интерфейс Commands
 */
public class ImbalansUCommand implements Commands, Serializable, Cloneable {
    //Обект установки
    private StendDLLCommands stendDLLCommands;

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    //Лист с счётчиками для испытания
    private List<Meter> meterForTestList;

    //Максимальный порог ошибки
    private double emin = -1.0;

    //Минимальный порог ошибки
    private double emax = 1.0;

    //Кол-во импульсов для расчёта ошибки
    private int pulse = 5;

    //Время для стабилизации параметров необходимых для испытания
    private double pauseForStabilization = 2;

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

    //Режим
    private int phase;

    //Напряжение
    private double ratedVolt;

    //Напряжение по фазе A
    private double voltPerA;

    //Напряжение по фазе B
    private double voltPerB;

    //Напряжение по фазе C
    private double voltPerC;

    //Процент от напряжения
    private double voltPer;

    //Ток
    private double ratedCurr;

    //Процен от тока
    private double currPer;

    //Частота
    private double ratedFreq;

    //Коэфициент мощности
    private String cosP;

    //Необходимо сделать в доп тестовом окне
    private int phaseSrequence;

    //Направление тока
    private int revers;

    //По каким фазам пустить ток
    private String iABC;

    //Активная ли точка
    private boolean active = true;

    //Импульсный выход
    private int channelFlag;

    //Количество повторов теста
    private int countResult = 2;

    //Константа счётчика для теста
    private int constantMeter;

    //Флаг для прекращения сбора погрешности
    private HashMap<Integer, Boolean> flagInStop;

    /**
     * @param id - для добавления или удаления точки испытания
     * @param phase - режим работы стенда
     * @param current - ток
     * @param revers - направление тока
     * @param cosP - COSф или коэффициент мощности PF
     * @param channelFlag - Импульсный выход установки (активная/реактивная энергия, прямое/обратное направление тока)
     * @param voltPerPhaseA - Напряжение на фазе А
     * @param voltPerPhaseB - Напряжение на фазе B
     * @param voltPerPhaseC - Напряжение на фазе C
     */
    public ImbalansUCommand(String id, int phase, String current, int revers, String cosP, int channelFlag,
                            double voltPerPhaseA, double voltPerPhaseB, double voltPerPhaseC) {
        this.id = id;
        this.phase = phase;
        this.current = current;
        this.revers = revers;
        this.cosP = cosP;
        this.channelFlag = channelFlag;
        this.voltPerA = voltPerPhaseA;
        this.voltPerB = voltPerPhaseB;
        this.voltPerC = voltPerPhaseC;

        //"Imb;B;R;N"
        String[] arrId = id.split(";");

        if (arrId[2].equals("A")) {

            if (arrId[3].equals("P")) {
                name = (arrId[1] + "; ImbU AP+");
            } else {
                name = (arrId[1] + "; ImbU AP-");
            }

        } else {

            if (arrId[3].equals("P")) {
                name = (arrId[1] + "; ImbU RP+");
            } else {
                name = (arrId[1] + "; ImbU RP-");
            }
        }

        currPer = 100.0;
        iABC = "H";
        phaseSrequence = 0;
        voltPer = 100.0;
    }

    @Override
    public void execute() throws StendConnectionException, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Выбор константы в зависимости от энергии
        if (channelFlag < 2) {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
        } else {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
        }

        flagInStop = initBoolList();

        if (current.equals("Ib")) {
            ratedCurr = Ib;
        } else {
            ratedCurr = Imax;
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep((long) pauseForStabilization * 1000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        resetError();

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Сказать константу счётчика стенду для кажого места
        stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Для быстрой становки флага прошёл счётчик тест или нет
        Meter.CommandResult resultMeter;

        int resultNo;
        String strError;
        String[] strMass;
        String error;

        //Для сравнения
        double doubleErr;

        while (flagInStop.containsValue(false)) {

            if (Thread.currentThread().isInterrupted()) {
                throw new InterruptedException();
            }

            for (Meter meter : meterForTestList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                strError = stendDLLCommands.meterErrorRead(meter.getId());
                strMass = strError.split(",");

                if (strMass.length != 2) {
                    continue;
                }

                resultNo = Integer.parseInt(strMass[0]);
                error = strMass[1];

                if (resultNo != 0) {
                    resultMeter = meter.returnResultCommand(index, channelFlag);
                    doubleErr = Double.parseDouble(error);

                    if (doubleErr > emax || doubleErr < emin) {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("F" + error, resultNo, error, false);
                    } else {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("P" + error, resultNo, error, true);
                    }

                    if (meter.getErrorResultChange() != resultNo) {
                        meter.setAmountMeasur(meter.getAmountMeasur() + 1);
                        meter.setErrorResultChange(resultNo);
                    }
                }

                if (meter.getAmountMeasur() >= countResult) {
                    flagInStop.put(meter.getId(), true);
                }
            }

            Thread.sleep(300);
        }

        stendDLLCommands.errorClear();
    }

    //Метод для цикличной поверки счётчиков
    @Override
    public void executeForContinuousTest() throws StendConnectionException, InterruptedException {
        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Выбор константы в зависимости от энергии
        if (channelFlag < 2) {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterAP());
        } else {
            constantMeter = Integer.parseInt(meterForTestList.get(0).getConstantMeterRP());
        }

        if (current.equals("Ib")) {
            ratedCurr = Ib;
        } else {
            ratedCurr = Imax;
        }

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        stendDLLCommands.getUIWithPhase(phase, ratedVolt, ratedCurr, ratedFreq, phaseSrequence, revers,
                voltPerA, voltPerB, voltPerC, currPer, iABC, cosP);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        Thread.sleep((long) pauseForStabilization * 1000);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Устанавливаем местам импульсный выход
        stendDLLCommands.setEnergyPulse(meterForTestList, channelFlag);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Сказать константу счётчика стенду для кажого места
        stendDLLCommands.setMetersConstantToStend(meterForTestList, constantMeter, pulse);

        if (Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }

        //Для быстрой становки флага прошёл счётчик тест или нет
        Meter.CommandResult resultMeter;

        int resultNo;
        String strError;
        String[] strMass;
        String error;
        double doubleErr;

        while (Thread.currentThread().isAlive()) {

            for (Meter meter : meterForTestList) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                strError = stendDLLCommands.meterErrorRead(meter.getId());
                strMass = strError.split(",");

                if (strMass.length != 2) {
                    continue;
                }

                resultNo = Integer.parseInt(strMass[0]);
                error = strMass[1];

                if (resultNo != 0) {
                    resultMeter = meter.returnResultCommand(index, channelFlag);
                    doubleErr = Double.parseDouble(error);

                    if (doubleErr > emax || doubleErr < emin) {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("F" + error, resultNo, error, false);
                    } else {
                        ((Meter.ImbUResult) resultMeter).setResultImbCommand("P" + error, resultNo, error, true);
                    }
                }
            }

            Thread.sleep(300);
        }

        stendDLLCommands.errorClear();
    }

    //Опрашивает счётчики до нужного значения проходов
    private HashMap<Integer, Boolean> initBoolList() {
        HashMap<Integer, Boolean> init = new HashMap<>(meterForTestList.size());

        for (Meter meter : meterForTestList) {
            init.put(meter.getId(), false);
        }
        return init;
    }

    private void resetError() {
        for (Meter meter : meterForTestList) {
            meter.setAmountMeasur(0);
            meter.setErrorResultChange(0);
        }
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
        this.pulse = Integer.parseInt(pulse);
    }

    public void setEmin(String emin) {
        this.emin = Double.parseDouble(emin);
    }

    public void setEmax(String emax) {
        this.emax = Double.parseDouble(emax);
    }

    public String getName() {
        return name;
    }

    public String getEmin() {
        return String.valueOf(emin);
    }

    public String getEmax() {
        return String.valueOf(emax);
    }

    public String getPulse() {
        return String.valueOf(pulse);
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

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setMeterForTestList(List<Meter> meterForTestList) {
        this.meterForTestList = meterForTestList;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setCountResult(String countResult) {
        this.countResult = Integer.parseInt(countResult);
    }

    public String getCountResult() {
        return String.valueOf(countResult);
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public double getVoltPer() {
        return voltPer;
    }

    public double getCurrPer() {
        return currPer;
    }

    @Override
    public double getRatedVolt() {
        return ratedVolt;
    }

    @Override
    public double getVoltPerA() {
        return voltPerA;
    }

    @Override
    public double getVoltPerB() {
        return voltPerB;
    }

    @Override
    public double getVoltPerC() {
        return voltPerC;
    }

    @Override
    public double getRatedCurr() {
        return ratedCurr;
    }

    @Override
    public String getiABC() {
        return iABC;
    }

    public boolean isThreePhaseCommand() {
        return true;
    }

    public String getPauseForStabilization() {
        return String.valueOf(pauseForStabilization);
    }

    @Override
    public void setPauseForStabilization(double pauseForStabilization) {
        this.pauseForStabilization = pauseForStabilization;
    }

    @Override
    public Commands clone() throws CloneNotSupportedException {
        return (Commands) super.clone();
    }
}
