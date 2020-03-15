package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;
import org.taipit.stend.helper.exeptions.InterruptedTestException;

import java.io.Serializable;
import java.util.List;

public class RTCCommand implements Commands, Serializable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private List<Meter> meterList;

    private StendDLLCommands stendDLLCommands;

    private int phase;

    private double ratedVolt;

    private int channelFlag = 4;

    //Для сохранения результата теста в нужное направление
    private int channelFlagForSave;

    private boolean active = true;

    //Дианазон ошибки
    private double errorForFalseTest;

    //Количество повторов теста
    private int countResult;

    //Частота
    private double freg;

    //Имя команды
    private String name;

    //Количество импульсов для считывания
    private int pulseForRTC;

    //Тип измерения
    private int errorType;

    public RTCCommand(int pulseForRTC, double freg, int countResult, int errorType, double errorForFalseTest, int channelFlagForSave) {
        this.pulseForRTC = pulseForRTC;
        this.freg = freg;
        this.countResult = countResult;
        this.errorType = errorType;
        this.errorForFalseTest = errorForFalseTest;
        this.channelFlagForSave = channelFlagForSave;
    }

    @Override
    public void execute() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
            int count = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                    100.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

            for (Meter meter : meterList) {
                if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC))
                    throw new ConnectForStendExeption();
            }
            Meter.CommandResult errorCommand;

            while (count < countResult) {

                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

                if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

                for (Meter meter : meterList) {
                    if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

                    try {
                        //meter = meterList.get(mapResult.getKey() - 1);
                        errorCommand = meter.returnResultCommand(index, channelFlag);

                        double result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId())) - 1.000000;

                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            addRTCTestResult(meter, result, false, channelFlagForSave);
                        } else {
                            addRTCTestResult(meter, result, true, channelFlagForSave);
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        System.out.println("Пустая строчка");
                    }
                }


                count++;
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }catch (InterruptedTestException e) {
            e.printStackTrace();
            //Переход сразу к финалу
        }finally {
            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
            int count = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                    0.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();
            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) throw new InterruptedTestException();

            for (Meter meter : meterList) {
                if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC))
                    throw new ConnectForStendExeption();
            }

            while (!Thread.currentThread().isInterrupted()) {

                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

                for (Meter meter : meterList) {
                    try {
                        /**
                         * Проверить на тру или фолс в корневом методе
                         */
                        double result = Double.parseDouble(stendDLLCommands.clockErrorRead(freg, errorType, meter.getId())) - 1.000000;

                        /**
                         *Сохранять результат в массив результатов и изменять цвет в зависимости от того прошел тест или нет
                         */
                        if (result > errorForFalseTest || result < -errorForFalseTest) {
                            addRTCTestResult(meter, result, false, channelFlagForSave);
                        } else {
                            addRTCTestResult(meter, result, true, channelFlagForSave);
                        }

                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        System.out.println("Пустая строчка");
                    }
                }

                count++;
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }catch (InterruptedTestException e) {
            e.printStackTrace();
            //Переход сразу к финалу
        }finally {
            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();
        }
    }

    private void addRTCTestResultRass(Meter meter, double RTCError, boolean passOrNot, int channelFlagForSave) {

    }

    private void addRTCTestResult(Meter meter, double RTCError, boolean passOrNot, int channelFlagForSave) {
        Meter.CommandResult commandResult;
        meter.setRTCTest(passOrNot);

        switch (channelFlagForSave) {
            case 0: {
                commandResult = meter.getErrorListAPPls().get(index);
                commandResult.setLastResult(String.valueOf(RTCError));
                commandResult.setPassTest(passOrNot);
            }break;

            case 1: {
                commandResult = meter.getErrorListAPMns().get(index);
                commandResult.setLastResult(String.valueOf(RTCError));
                commandResult.setPassTest(passOrNot);
            }break;

            case 2: {
                commandResult = meter.getErrorListRPPls().get(index);
                commandResult.setLastResult(String.valueOf(RTCError));
                commandResult.setPassTest(passOrNot);
            }break;

            case 3: {
                commandResult = meter.getErrorListRPMns().get(index);
                commandResult.setLastResult(String.valueOf(RTCError));
                commandResult.setPassTest(passOrNot);
            }break;
        }
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setPhase(int phase) {
        this.phase = phase;
    }

    public void setRatedVolt(double ratedVolt) {
        this.ratedVolt = ratedVolt;
    }

    public double getErrorForFalseTest() {
        return errorForFalseTest;
    }

    public int getPulseForRTC() {
        return pulseForRTC;
    }

    public int getErrorType() {
        return errorType;
    }

    public int getCountResult() {
        return countResult;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public void setStendDLLCommands(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    public void setMeterList(List<Meter> meterList) {
        this.meterList = meterList;
    }

}
