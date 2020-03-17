package org.taipit.stend.controller.Commands;

import org.taipit.stend.controller.Meter;
import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.viewController.TestErrorTableFrameController;
import org.taipit.stend.helper.exeptions.ConnectForStendExeption;

import java.io.Serializable;
import java.util.List;

public class RTCCommand implements Commands, Serializable {

    //Необходим для быстрого доступа к Объекту класса resultCommand
    private int index;

    private boolean interrupt;

    private boolean nextCommand;

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
    public boolean execute() throws ConnectForStendExeption {
        try {

            int count = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                    100.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            if (TestErrorTableFrameController.interrupt) {
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                return false;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (TestErrorTableFrameController.interrupt) {
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                return false;
            }

            for (Meter meter : meterList) {
                if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC))
                    throw new ConnectForStendExeption();
            }

            Meter.CommandResult errorCommand;

            while (count < countResult) {
                if (TestErrorTableFrameController.interrupt) {
                    break;
                }

                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

                if (TestErrorTableFrameController.interrupt) {
                    break;
                }

                for (Meter meter : meterList) {
                    if (TestErrorTableFrameController.interrupt) {
                        break;
                    }

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

            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();

            if (!TestErrorTableFrameController.interrupt && nextCommand) {
                return true;
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            e.printStackTrace();
            TestErrorTableFrameController.interrupt = true;
        }
        return !TestErrorTableFrameController.interrupt;
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption {
        try {

            int count = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                    0.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            if (TestErrorTableFrameController.interrupt) {
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                return;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (TestErrorTableFrameController.interrupt) {
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                return;
            }

            for (Meter meter : meterList) {
                if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC))
                    throw new ConnectForStendExeption();
            }

            while (!TestErrorTableFrameController.interrupt) {

                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

                if (TestErrorTableFrameController.interrupt) {
                    break;
                }

                for (Meter meter : meterList) {
                    try {
                        if (TestErrorTableFrameController.interrupt) {
                            break;
                        }
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
            TestErrorTableFrameController.interrupt = true;
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

    @Override
    public void setInterrupt(boolean interrupt) {
        this.interrupt = interrupt;
    }

    @Override
    public void setNextCommand(boolean nextCommand) {
        this.nextCommand = nextCommand;
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
