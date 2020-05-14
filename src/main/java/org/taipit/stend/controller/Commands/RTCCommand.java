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

    private String id;

    //Команда из методики для трехфазной установки?
    private boolean threePhaseCommand;

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
    private String countResult;

    //Количество повторов теста
    private int countResultTest;

    //Частота
    private double freg;

    //Имя команды
    private String name;

    //Количество импульсов для считывания
    private int pulseForRTC;

    //Тип измерения
    private int errorType;

    public RTCCommand(boolean threePhaseCommand, String id , String name,int pulseForRTC, double freg, int countResultTest, int errorType, double errorForFalseTest, int channelFlagForSave) {
        this.threePhaseCommand = threePhaseCommand;
        this.id = id;
        this.name = name;
        this.pulseForRTC = pulseForRTC;
        this.freg = freg;
        this.countResultTest = countResultTest;
        this.errorType = errorType;
        this.errorForFalseTest = errorForFalseTest;
        this.channelFlagForSave = channelFlagForSave;
    }

    @Override
    public boolean execute() throws ConnectForStendExeption {
        try {
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                return false;
            }

            int count = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                    100.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            //Разблокирую интерфейc кнопок
            TestErrorTableFrameController.blockBtns.setValue(false);

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return false;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return false;
            }

            for (Meter meter : meterList) {
                if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC))
                    throw new ConnectForStendExeption();
            }

            Meter.CommandResult errorCommand;

            while (count < countResultTest) {

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return false;
                }

                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return false;
                }

                for (Meter meter : meterList) {
                    if (Thread.currentThread().isInterrupted()) {
                        System.out.println("Получил сигнал о завершении потока из команды StartCommand из внешнего цикла");
                        if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                        if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                        System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                        return false;
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

            if (!Thread.currentThread().isInterrupted() && nextCommand) {
                return true;
            }

            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();

        }catch (InterruptedException e) {
            System.out.println("Поймал ошибку Interrupted в команде StartCommand");
            System.out.println("Состояние нити до команты interrupt в команде StartCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
            return false;
        }
        return !Thread.currentThread().isInterrupted();
    }

    @Override
    public void executeForContinuousTest() throws ConnectForStendExeption {
        try {

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                return;
            }

            int count = 0;

            if (!stendDLLCommands.getUI(phase, ratedVolt, 0.0, 0.0, 0, 0,
                    0.0, 0.0, "H", "1.0")) throw new ConnectForStendExeption();

            //Разблокирую интерфейc кнопок
            TestErrorTableFrameController.blockBtns.setValue(false);

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            stendDLLCommands.setEnergyPulse(meterList, channelFlag);

            Thread.sleep(stendDLLCommands.getPauseForStabization());

            if (Thread.currentThread().isInterrupted()) {
                System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                return;
            }

            for (Meter meter : meterList) {
                if (!stendDLLCommands.clockErrorStart(meter.getId(), freg, pulseForRTC))
                    throw new ConnectForStendExeption();
            }

            while (!Thread.currentThread().isInterrupted()) {

                Thread.sleep((pulseForRTC * 1000) + (pulseForRTC * 200));

                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                    if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                    if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                    System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                    return;
                }

                for (Meter meter : meterList) {
                    try {
                        if (Thread.currentThread().isInterrupted()) {
                            System.out.println("Получил сигнал о завершении потока из команды RTCCommand");
                            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
                            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
                            System.out.println("Выключил напряжение, ток, отчистил значения и вышел из метода");
                            return;
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
            System.out.println("Поймал ошибку Interrupted в команде StartCommand");
            System.out.println("Состояние нити до команты interrupt в команде StartCommand " + Thread.currentThread().getState());
            Thread.currentThread().interrupt();
            System.out.println("Узнаю состояние нити после команды interrupt " + Thread.currentThread().getState());
            if (!stendDLLCommands.errorClear()) throw new ConnectForStendExeption();
            if (!stendDLLCommands.powerOf()) throw new ConnectForStendExeption();
            System.out.println("Выключил напряжение и ток, очистил результаты и вышел из метода");
            return;
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

    public String getCountResult() {
        return null;
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

    @Override
    public void setPulse(String pulse) {

    }

    @Override
    public void setCountResult(String countResult) {

    }

    @Override
    public void setEmax(String emax) {

    }

    @Override
    public void setEmin(String emin) {

    }

    public int getCountResultTest() {
        return countResultTest;
    }

    public void setCountResultTest(int countResultTest) {
        this.countResultTest = countResultTest;
    }
}
