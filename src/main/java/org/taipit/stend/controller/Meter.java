package org.taipit.stend.controller;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import org.taipit.stend.controller.Commands.*;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.metodics.Metodic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

/**
 * @autor Albert Khalimov
 *
 * Данный класс является программной реализацией испытываемого счётчика
 *
 * Объекты результата точек испытаний (Command result) находятся внитри данного класса
 */
public class Meter implements Serializable {
    /**
     * Для восстановления результатов прошлого теста, при создании
     * новых объектов CommandResult сравнивать хэш код со старыми результатами и если что возвращать старый результат
     */

    /**
     * Задаёт результаты испытаний по умолчанию
     */
    public Meter() {
        Properties properties = ConsoleHelper.properties;
        String result;

        //Заключительный результат
        totalResult = new Meter.TotalResult("T");

        //Результат самохода
        creepTest = new CreepResult("CRP", "", "");

        //Результат теста на чувствительность
        startTestAPPls = new StartResult("STAAP", "", "");
        startTestAPMns = new StartResult("STAAN", "", "");
        startTestRPPls = new StartResult("STARP", "", "");
        startTestRPMns = new StartResult("STARN", "", "");

        //Результат точности хода часов
        RTCTest = new RTCResult("RTC", "", "", "", "", "");

        //Результат проверки изоляции счётчика
        insulationTest = new InsulationResult("INS");

        //Внешний вид счётчика (годен или нет ОТК)
        appearensTest = new AppearensResult("APR");

        //Результат теста проверка счётного механизма
        constantTestAPPls = new ConstantResult("CNTAP", "", "");
        constantTestAPMns = new ConstantResult("CNTAN", "", "");
        constantTestRPPls = new ConstantResult("CNTRP", "", "");
        constantTestRPMns = new ConstantResult("CNTRN", "", "");

        //Резульат теста проверка работоспособности реле
        relayTest = new RelayResult("RLY", "", "");

        /**
         * Установка результатов из окна настроек
         */
        //Самоход
        result = properties.getProperty("propertiesController.reportPane.cmbBxCreep");
        if (result.equals("N")) {
            creepTest.setPassTest(null);
        } else if (result.equals("T")) {
            creepTest.setPassTest(true);
        } else {
            creepTest.setPassTest(false);
        }

        //Чувствительность
        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_APPls");
        if (result.equals("N")) {
            startTestAPPls.setPassTest(null);
        } else if (result.equals("T")) {
            startTestAPPls.setPassTest(true);
        } else {
            startTestAPPls.setPassTest(false);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_APMns");
        if (result.equals("N")) {
            startTestAPMns.setPassTest(null);
        } else if (result.equals("T")) {
            startTestAPMns.setPassTest(true);
        } else {
            startTestAPMns.setPassTest(false);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_RPPls");
        if (result.equals("N")) {
            startTestRPPls.setPassTest(null);
        } else if (result.equals("T")) {
            startTestRPPls.setPassTest(true);
        } else {
            startTestRPPls.setPassTest(false);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxSTA_RPMns");
        if (result.equals("N")) {
            startTestRPMns.setPassTest(null);
        } else if (result.equals("T")) {
            startTestRPMns.setPassTest(true);
        } else {
            startTestRPMns.setPassTest(false);
        }

        //Точность хода часов
        result = properties.getProperty("propertiesController.reportPane.cmbBxRTC");
        if (result.equals("N")) {
            RTCTest.setPassTest(null);
        } else if (result.equals("T")) {
            RTCTest.setPassTest(true);
        } else {
            RTCTest.setPassTest(false);
        }

        //Проверка счётного механизма
        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_APPls");
        if (result.equals("N")) {
            constantTestAPPls.setPassTest(null);
        } else if (result.equals("T")) {
            constantTestAPPls.setPassTest(true);
        } else {
            constantTestAPPls.setPassTest(false);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_APMns");
        if (result.equals("N")) {
            constantTestAPMns.setPassTest(null);
        } else if (result.equals("T")) {
            constantTestAPMns.setPassTest(true);
        } else {
            constantTestAPMns.setPassTest(false);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_RPPls");
        if (result.equals("N")) {
            constantTestRPPls.setPassTest(null);
        } else if (result.equals("T")) {
            constantTestRPPls.setPassTest(true);
        } else {
            constantTestRPPls.setPassTest(false);
        }

        result = properties.getProperty("propertiesController.reportPane.cmbBxConst_RPMns");
        if (result.equals("N")) {
            constantTestRPMns.setPassTest(null);
        } else if (result.equals("T")) {
            constantTestRPMns.setPassTest(true);
        } else {
            constantTestRPMns.setPassTest(false);
        }

        //Изоляция
        result = properties.getProperty("propertiesController.reportPane.cmbBxInsulation");
        if (result.equals("N")) {
            insulationTest.setPassTest(null);
        } else if (result.equals("T")) {
            insulationTest.setPassTest(true);
        } else {
            insulationTest.setPassTest(false);
        }

        //Внешний вид
        result = properties.getProperty("propertiesController.reportPane.cmbBxAppearance");
        if (result.equals("N")) {
            appearensTest.setPassTest(null);
        } else if (result.equals("T")) {
            appearensTest.setPassTest(true);
        } else {
            appearensTest.setPassTest(false);
        }

        //Реле
        result = properties.getProperty("propertiesController.reportPane.cmbBxRelay");
        if (result.equals("N")) {
            relayTest.setPassTest(null);
        } else if (result.equals("T")) {
            relayTest.setPassTest(true);
        } else {
            relayTest.setPassTest(false);
        }
    }

    //Методика по которой поверяется счётчик
    private Metodic metodic;

    //Номер посадочного места счётчика на установке
    private int id;

    //Уникальный ID счётчика
    private String unicalID;

    //Максимальный ток
    private float Imax;

    //Базовый ток
    private float Ib;

    //Номинальное напряжение
    private float Un;

    //Номиальная частота
    private float Fn;

    private String InomImax;

    //Тип измерительного элемента счётчика шунт/трансформатор
    private boolean typeOfMeasuringElementShunt;

    //Тип сети трёхфазная или однофазная
    private boolean typeCircuitThreePhase;

    //Класс точности активной энергии
    private float accuracyClassAP;

    //Класс точночти реактивной энергии
    private float accuracyClassRP;

    //Температура при которой испытывался счётчик
    private float temperature;

    //Влажность при которой испытывался счётчик
    private float humidity;

    //Оператор при котором испытывался счётчик
    private String operator;

    //Контроллёр
    private String controller;

    //Поверитель
    private String witness;

    //
    private String verificationDate;

    //Завод производитель
    private String factoryManufacturer;

    //Номер партии
    private String batchNo;

    //Последняя дата изменения
    private String lastModifiedDate;

    //Тестовый режим
    private String testMode;

    //Пример: Однотарифный однофазный
    private String typeMeter;

    //Установлен ли счётчик на посадочное место
    private boolean activeSeat = false;

    //Записать результаты в базу
    private boolean saveResults = true;

    //Количество измерений погрешности
    private int amountMeasur;

    //Необходимо для количетва вычислений
    private int errorResultChange;

    //Серийный номер счётчика
    private String serNoMeter;

    //Константа активной энергии счётчика
    private String constantMeterAP;

    //Константа реактивной энергии счётчика
    private String constantMeterRP;

    //Модель счётчика
    private String modelMeter;

    //Результаты тестов
    //-----------------------------------------------------------------------
    //CRP Самоход
    //STAAP Чувствтельность AP+
    //STAAN Чувствтельность AP-
    //STARP Чувствтельность RP+
    //STARN Чувствтельность RP-
    //RTC ТХЧ
    //CNTAP Константа
    //CNTAN Константа
    //CNTRP Константа
    //CNTRN Константа
    //RLY Реле
    //INS Изоляция
    //APR Внешний вид

    //Общий результат
    private Meter.TotalResult totalResult;

    //Самоход
    private CreepResult creepTest;

    //Чувствительность
    private StartResult startTestAPPls;
    private StartResult startTestAPMns;
    private StartResult startTestRPPls;
    private StartResult startTestRPMns;

    //Точность хода часов
    private RTCResult RTCTest;

    //Изоляция
    private InsulationResult insulationTest;

    //Внешний вид
    private AppearensResult appearensTest;

    //Проверка счётного механизма
    private ConstantResult constantTestAPPls;
    private ConstantResult constantTestAPMns;
    private ConstantResult constantTestRPPls;
    private ConstantResult constantTestRPMns;

    //Реле
    private RelayResult relayTest;

    //Лист с результатами п овсем направлениям
    private List<CommandResult> errorListAPPls = new ArrayList<>();
    private List<CommandResult> errorListAPMns = new ArrayList<>();
    private List<CommandResult> errorListRPPls = new ArrayList<>();
    private List<CommandResult> errorListRPMns = new ArrayList<>();

    /**
     * Создаёт объект результата для каждой точки испытания из методики поверки
     * @param command - точка испытания
     * @param chanelFlag - направление и тип мощности
     * @param id - id при добавлении точки испытания
     * @param testErrorTableFrameController - окно испытаний
     */
    public void createError(Commands command, int chanelFlag , String id, TestErrorTableFrameController testErrorTableFrameController) {

        CommandResult result = null;

        switch (chanelFlag) {
            //AP+
            case 0: {
                //Создания объекта результата теста погрешность счётчика
                if (command instanceof ErrorCommand) {

                    ErrorCommand errorCommand = (ErrorCommand) command;

                    //Если из влияния
                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    result = new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax());

                    errorListAPPls.add(result);

                    //Создания объекта результата теста самоход
                } else if (command instanceof CreepCommand) {
                    CreepCommand creepCommand = (CreepCommand) command;

                    //Если по госту, расчитываю и
                    //отображаю время теста
                    if (creepCommand.isGostTest()) {

                        result = new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()), String.valueOf(creepCommand.getPulseValue()));
                        creepCommand.setUserTimeTest(testErrorTableFrameController.getTimeToCreepTestGOSTAP());

                        //То отображаю то время, которое указал пользователь
                    } else {
                        result = new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue()));
                    }

                    errorListAPPls.add(result);

                    //Создания объекта результата теста чувствительность
                } else if (command instanceof StartCommand) {

                    StartCommand startCommand = (StartCommand) command;

                    //Если по госту, расчитываю и
                    //отображаю время теста
                    if (startCommand.isGostTest()) {

                        result = new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()), String.valueOf(startCommand.getPulseValue()));

                        //Время теста для всплывающей подсказки
                        startCommand.setUserTimeTest(testErrorTableFrameController.getTimeToStartTestGOSTAP());
                        startCommand.setRatedCurr(calculateCurrentForSTA(testErrorTableFrameController, chanelFlag));

                        //То отображаю то время, которое указал пользователь
                    } else {

                        result = new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue()));
                    }

                    errorListAPPls.add(result);

                    //Создания объекта результата теста Точность хода часов
                } else if (command instanceof RTCCommand) {
                    RTCCommand rtcCommand = (RTCCommand) command;

                    result = new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getFreg()),
                            String.valueOf(rtcCommand.getCountResultTest()), String.valueOf(rtcCommand.getPulseForRTC()));

                    errorListAPPls.add(result);

                    //Создания объекта результата теста проверка счётного механизма
                } else if (command instanceof ConstantCommand) {
                    ConstantCommand constantCommand = (ConstantCommand) command;

                    result = new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax()));

                    errorListAPPls.add(result);

                    //Создания объекта результата теста имбаланс напряжений
                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;

                    result = new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax());
                    errorListAPPls.add(result);

                    //Создания объекта результата теста проверка работоспособности реле
                } else if (command instanceof RelayCommand) {

                    result = new RelayResult(id, getTime(((RelayCommand) command).getUserTimeTest()), String.valueOf(((RelayCommand) command).getPulseValue()));
                    errorListAPPls.add(result);
                }

            } break;

            //Далее логика создания такая же как и для AP+, но только для других напрявлений и типов мощности (см. комментарии там)

            //AP-
            case 1: {

                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    result = new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax());
                    errorListAPMns.add(result);

                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {

                        result = new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTAP()), String.valueOf(creepCommand.getPulseValue()));
                        creepCommand.setUserTimeTest(testErrorTableFrameController.getTimeToCreepTestGOSTAP());

                    } else {
                        result = new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue()));

                    }

                    errorListAPMns.add(result);

                } else if (command instanceof StartCommand) {
                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {

                        result = new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTAP()), String.valueOf(startCommand.getPulseValue()));

                        startCommand.setUserTimeTest(testErrorTableFrameController.getTimeToStartTestGOSTAP());
                        startCommand.setRatedCurr(calculateCurrentForSTA(testErrorTableFrameController, chanelFlag));
                    } else {
                        result = new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue()));
                    }

                    errorListAPMns.add(result);

                } else if (command instanceof RTCCommand) {

                    RTCCommand rtcCommand = (RTCCommand) command;

                    result = new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getFreg()),
                            String.valueOf(rtcCommand.getCountResultTest()), String.valueOf(rtcCommand.getPulseForRTC()));

                    errorListAPMns.add(result);

                } else if (command instanceof ConstantCommand) {

                    ConstantCommand constantCommand = (ConstantCommand) command;

                    result = new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax()));

                    errorListAPMns.add(result);

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;

                    result = new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax());

                    errorListAPMns.add(result);

                }  else if (command instanceof RelayCommand) {

                    result = new RelayResult(id, getTime(((RelayCommand) command).getUserTimeTest()), String.valueOf(((RelayCommand) command).getPulseValue()));

                    errorListAPMns.add(result);
                }

            } break;

            //RP+
            case 2: {
                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    result = new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax());

                    errorListRPPls.add(result);

                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {

                        result = new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()), String.valueOf(creepCommand.getPulseValue()));
                        creepCommand.setUserTimeTest(testErrorTableFrameController.getTimeToCreepTestGOSTRP());

                    } else {

                        result = new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue()));

                    }

                    errorListRPPls.add(result);

                } else if (command instanceof StartCommand) {

                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {

                        result = new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()), String.valueOf(startCommand.getPulseValue()));
                        startCommand.setUserTimeTest(testErrorTableFrameController.getTimeToStartTestGOSTRP());

                        startCommand.setRatedCurr(calculateCurrentForSTA(testErrorTableFrameController, chanelFlag));
                    } else {

                        result = new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue()));

                    }

                    errorListRPPls.add(result);

                } else if (command instanceof RTCCommand) {

                    RTCCommand rtcCommand = (RTCCommand) command;

                    result = new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getFreg()),
                            String.valueOf(rtcCommand.getCountResultTest()), String.valueOf(rtcCommand.getPulseForRTC()));

                    errorListRPPls.add(result);

                } else if (command instanceof ConstantCommand) {

                    ConstantCommand constantCommand = (ConstantCommand) command;

                    result = new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax()));

                    errorListRPPls.add(result);

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;

                    result = new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax());

                    errorListRPPls.add(result);

                } else if (command instanceof RelayCommand) {

                    result = new RelayResult(id, getTime(((RelayCommand) command).getUserTimeTest()), String.valueOf(((RelayCommand) command).getPulseValue()));

                    errorListRPPls.add(result);
                }
            } break;

            //RP-
            case 3: {
                if (command instanceof ErrorCommand) {
                    ErrorCommand errorCommand = (ErrorCommand) command;

                    if (id.contains("U") || id.contains("F")) {
                        id = errorCommand.getProcentParan() + " " + errorCommand.getId();
                    }

                    result = new ErrorResult(id, errorCommand.getEmin(), errorCommand.getEmax());

                    errorListRPMns.add(result);

                } else if (command instanceof CreepCommand) {

                    CreepCommand creepCommand = (CreepCommand) command;

                    //Отображаю время теста
                    if (creepCommand.isGostTest()) {
                        result = new CreepResult(id, getTime(testErrorTableFrameController.getTimeToCreepTestGOSTRP()), String.valueOf(creepCommand.getPulseValue()));
                        creepCommand.setUserTimeTest(testErrorTableFrameController.getTimeToCreepTestGOSTRP());

                    } else {
                        result = new CreepResult(id, getTime(creepCommand.getUserTimeTest()), String.valueOf(creepCommand.getPulseValue()));

                    }

                    errorListRPMns.add(result);

                } else if (command instanceof StartCommand) {

                    StartCommand startCommand = (StartCommand) command;

                    //Отображаю время теста
                    if (startCommand.isGostTest()) {

                        result = new StartResult(id, getTime(testErrorTableFrameController.getTimeToStartTestGOSTRP()), String.valueOf(startCommand.getPulseValue()));
                        startCommand.setUserTimeTest(testErrorTableFrameController.getTimeToStartTestGOSTRP());

                        startCommand.setRatedCurr(calculateCurrentForSTA(testErrorTableFrameController, chanelFlag));
                    } else {
                        result = new StartResult(id, getTime(startCommand.getUserTimeTest()), String.valueOf(startCommand.getPulseValue()));
                    }

                    errorListRPMns.add(result);

                } else if (command instanceof RTCCommand) {

                    RTCCommand rtcCommand = (RTCCommand) command;

                    result = new RTCResult(id, String.valueOf(-rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getErrorForFalseTest()), String.valueOf(rtcCommand.getFreg()),
                            String.valueOf(rtcCommand.getCountResultTest()), String.valueOf(rtcCommand.getPulseForRTC()));

                    errorListRPMns.add(result);

                } else if (command instanceof ConstantCommand) {

                    ConstantCommand constantCommand = (ConstantCommand) command;

                    result = new ConstantResult(id, String.valueOf(constantCommand.getEmin()), String.valueOf(constantCommand.getEmax()));

                    errorListRPMns.add(result);

                } else if (command instanceof ImbalansUCommand) {

                    ImbalansUCommand imbalansUCommand = (ImbalansUCommand) command;

                    result = new ImbUResult(id, imbalansUCommand.getEmin(), imbalansUCommand.getEmax());

                    errorListRPMns.add(result);

                } else if (command instanceof RelayCommand) {

                    result = new RelayResult(id, getTime(((RelayCommand) command).getUserTimeTest()), String.valueOf(((RelayCommand) command).getPulseValue()));

                    errorListRPMns.add(result);
                }

            } break;
        }
    }

    /**
     * Возвращает объект результата счётчика (необходим для быстрого доступа и изменения результата)
     * @param index - порядеовый ноер
     * @param energyPulseChanel - тип мощности и направления
     * @return
     */
    public Meter.CommandResult returnResultCommand (int index, int energyPulseChanel) {

        switch (energyPulseChanel) {
            case 0: {
                return errorListAPPls.get(index);
            }

            case 1: {
                return errorListAPMns.get(index);
            }

            case 2: {
                return errorListRPPls.get(index);
            }

            case 3: {
                return errorListRPMns.get(index);
            }
        }
        return null;
    }

    //Устанавливает результаты текущему счётчику
    public void setResults(Meter meter) {
        errorListAPPls = meter.getErrorListAPPls();
        errorListAPMns = meter.getErrorListAPMns();
        errorListRPPls = meter.getErrorListRPPls();
        errorListRPMns = meter.getErrorListRPMns();
    }

    //Переводит миллисекунды в формат hh:mm:ss
    private String getTime(long mlS){
        return String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(mlS),
                TimeUnit.MILLISECONDS.toMinutes(mlS) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(mlS) % TimeUnit.MINUTES.toSeconds(1));
    }

    public boolean myEquals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Meter meter = (Meter) obj;

        if (this.Un != meter.Un) return false;
        if (this.Imax != meter.Imax) return false;
        if (this.Ib != meter.Ib) return false;
        if (this.accuracyClassAP != meter.accuracyClassAP) return false;
        if (!this.modelMeter.equals(meter.modelMeter)) return false;
        if (!this.factoryManufacturer.equals(meter.factoryManufacturer)) return false;
        if (this.temperature != meter.temperature) return false;
        if (this.humidity != meter.humidity) return false;
        if (!this.operator.equals(meter.operator)) return false;
        if (!this.controller.equals(meter.controller)) return false;
        if (!this.witness.equals(meter.witness)) return false;
        if (!this.testMode.equals(meter.testMode)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return (int) (Un + Imax + Ib + accuracyClassAP + modelMeter.hashCode() + factoryManufacturer.hashCode() +
                        temperature + humidity + operator.hashCode() + controller.hashCode() + witness.hashCode() + testMode.hashCode() * 31);
    }

    //=============================================================================== get's and set's

    public void setMetodic(Metodic metodic) {
        this.metodic = metodic;
    }

    public String getConstantMeterAP() {
        return constantMeterAP;
    }

    public String getConstantMeterRP() {
        return constantMeterRP;
    }

    public String getModelMeter() {
        return modelMeter;
    }

    public String getSerNoMeter() {
        return serNoMeter;
    }

    public void setConstantMeterRP(String constantMeterRP) {
        this.constantMeterRP = constantMeterRP;
    }

    public void setModelMeter(String modelMeter) {
        this.modelMeter = modelMeter;
    }

    public void setConstantMeterAP(String constantMeterAP) {
        this.constantMeterAP = constantMeterAP;
    }

    public void setSerNoMeter(String serNoMeter) {
        this.serNoMeter = serNoMeter;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setActiveSeat(boolean activeSeat) {
        this.activeSeat = activeSeat;
    }

    public boolean isActiveSeat() {
        return activeSeat;
    }

    public List<CommandResult> getErrorListAPPls() {
        return errorListAPPls;
    }

    public List<CommandResult> getErrorListAPMns() {
        return errorListAPMns;
    }

    public List<CommandResult> getErrorListRPPls() {
        return errorListRPPls;
    }

    public List<CommandResult> getErrorListRPMns() {
        return errorListRPMns;
    }

    public int getAmountMeasur() {
        return amountMeasur;
    }

    public void setAmountMeasur(int amountMeasur) {
        this.amountMeasur = amountMeasur;
    }

    public void setImax(float imax) {
        Imax = imax;
    }

    public void setIb(float ib) {
        Ib = ib;
    }

    public void setUn(float un) {
        Un = un;
    }

    public void setFn(float fn) {
        Fn = fn;
    }

    public void setTypeOfMeasuringElementShunt(boolean typeOfMeasuringElementShunt) {
        this.typeOfMeasuringElementShunt = typeOfMeasuringElementShunt;
    }

    public void setTypeCircuitThreePhase(boolean typeCircuitThreePhase) {
        this.typeCircuitThreePhase = typeCircuitThreePhase;
    }

    public void setAccuracyClassAP(float accuracyClassAP) {
        this.accuracyClassAP = accuracyClassAP;
    }

    public void setAccuracyClassRP(float accuracyClassRP) {
        this.accuracyClassRP = accuracyClassRP;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public void setController(String controller) {
        this.controller = controller;
    }

    public void setWitness(String witness) {
        this.witness = witness;
    }

    public float getImax() {
        return Imax;
    }

    public float getIb() {
        return Ib;
    }

    public float getUn() {
        return Un;
    }

    public float getFn() {
        return Fn;
    }

    public boolean isTypeOfMeasuringElementShunt() {
        return typeOfMeasuringElementShunt;
    }

    public boolean isTypeCircuitThreePhase() {
        return typeCircuitThreePhase;
    }

    public float getAccuracyClassAP() {
        return accuracyClassAP;
    }

    public float getAccuracyClassRP() {
        return accuracyClassRP;
    }

    public String getOperator() {
        return operator;
    }

    public String getController() {
        return controller;
    }

    public String getWitness() {
        return witness;
    }

    public int getErrorResultChange() {
        return errorResultChange;
    }

    public void setErrorResultChange(int errorResultChange) {
        this.errorResultChange = errorResultChange;
    }

    public CreepResult getCreepTest() {
        return creepTest;
    }

    public void setCreepTest(CreepResult creepTest) {
        this.creepTest = creepTest;
    }

    public StartResult getStartTestAPPls() {
        return startTestAPPls;
    }

    public void setStartTestAPPls(StartResult startTestAPPls) {
        this.startTestAPPls = startTestAPPls;
    }

    public StartResult getStartTestAPMns() {
        return startTestAPMns;
    }

    public void setStartTestAPMns(StartResult startTestAPMns) {
        this.startTestAPMns = startTestAPMns;
    }

    public StartResult getStartTestRPPls() {
        return startTestRPPls;
    }

    public void setStartTestRPPls(StartResult startTestRPPls) {
        this.startTestRPPls = startTestRPPls;
    }

    public StartResult getStartTestRPMns() {
        return startTestRPMns;
    }

    public void setStartTestRPMns(StartResult startTestRPMns) {
        this.startTestRPMns = startTestRPMns;
    }

    public RTCResult getRTCTest() {
        return RTCTest;
    }

    public void setRTCTest(RTCResult RTCTest) {
        this.RTCTest = RTCTest;
    }

    public InsulationResult getInsulationTest() {
        return insulationTest;
    }

    public void setInsulationTest(InsulationResult insulationTest) {
        this.insulationTest = insulationTest;
    }

    public AppearensResult getAppearensTest() {
        return appearensTest;
    }

    public void setAppearensTest(AppearensResult appearensTest) {
        this.appearensTest = appearensTest;
    }

    public boolean isSaveResults() {
        return saveResults;
    }

    public void setSaveResults(boolean saveResults) {
        this.saveResults = saveResults;
    }

    public ConstantResult getConstantTestAPPls() {
        return constantTestAPPls;
    }

    public void setConstantTestAPPls(ConstantResult constantTestAPPls) {
        this.constantTestAPPls = constantTestAPPls;
    }

    public ConstantResult getConstantTestAPMns() {
        return constantTestAPMns;
    }

    public void setConstantTestAPMns(ConstantResult constantTestAPMns) {
        this.constantTestAPMns = constantTestAPMns;
    }

    public ConstantResult getConstantTestRPPls() {
        return constantTestRPPls;
    }

    public void setConstantTestRPPls(ConstantResult constantTestRPPls) {
        this.constantTestRPPls = constantTestRPPls;
    }

    public ConstantResult getConstantTestRPMns() {
        return constantTestRPMns;
    }

    public void setConstantTestRPMns(ConstantResult constantTestRPMns) {
        this.constantTestRPMns = constantTestRPMns;
    }

    public String getTemperature() {
        return String.valueOf(temperature);
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public String getHumidity() {
        return String.valueOf(humidity);
    }

    public void setHumidity(float humidity) {
        this.humidity = humidity;
    }

    public String getVerificationDate() {
        return verificationDate;
    }

    public void setVerificationDate(String verificationDate) {
        this.verificationDate = verificationDate;
    }

    public String getFactoryManufacturer() {
        return factoryManufacturer;
    }

    public void setFactoryManufacturer(String factoryManufacturer) {
        this.factoryManufacturer = factoryManufacturer;
    }

    public Boolean getTotalResult() {

        return totalResult.getPassTest();
    }

    public void setTotalResult(Boolean totalResult) {
        this.totalResult.setPassTest(totalResult);
    }

    public String getUnicalID() {
        return unicalID;
    }

    public void setUnicalID(String unicalID) {
        this.unicalID = unicalID;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public String getInomImax() {
        return InomImax;
    }

    public void setInomImax(String inomImax) {
        InomImax = inomImax;
    }

    public String getTypeMeter() {
        return typeMeter;
    }

    public void setTypeMeter(String typeMeter) {
        this.typeMeter = typeMeter;
    }

    public String getTestMode() {
        return testMode;
    }

    public void setTestMode(String testMode) {
        this.testMode = testMode;
    }

    public Meter.TotalResult getTotalResultObject() {
        return totalResult;
    }

    public RelayResult getRelayTest() {
        return relayTest;
    }

    //==============================================================================================

    /**
     * Абстрактный класс для записи результата каждой точки испытания
     */
    public abstract class CommandResult implements Serializable {

        CommandResult(String id) {
            this.id = id;
            this.lastResultForTabView = new SimpleStringProperty();
        }

        //Последний результат теста для отображения в таблице TabView
        private transient SimpleStringProperty lastResultForTabView;

        //Для отображения последних результатов теста в подсказке
        private transient SimpleStringProperty errorsForTips;

        //Идентификатор команды
        private String id;

        //Последний результат
        private String lastResult = "";

        //Верхняя граница погрешности
        private String maxError = "";

        //Нижняя граница погрешности
        private String minError = "";

        //Погрешность в диапазоне (прошла тест или нет)
        private Boolean passTest = null;

        //10-ть последних результатов
        private String[] results = new String[10];

        //Обновляет список последних результатов при появлении нового в окне подсказки
        public void refreshTipsInfo() {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    if (errorsForTips == null) {
                        errorsForTips = new SimpleStringProperty("");
                    }

                    StringBuilder sb = new StringBuilder();

                    for (int i = 0; i < results.length; i++) {
                        if (results[i] != null) {
                            sb.append(i).append(": ").append(results[i]).append("\n");
                        }
                    }

                    if (!sb.toString().isEmpty()) {
                        errorsForTips.setValue(sb.toString());
                    } else {
                        errorsForTips.setValue("Нет результатов измерения");
                    }
                }
            });
        }

        public String getId() {
            return id;
        }

        void setId(String id) {
            this.id = id;
        }

        public Boolean getPassTest() {
            return passTest;
        }

        public void setPassTest(Boolean passTest) {
            this.passTest = passTest;
        }

        public Boolean isPassTest() {
            return passTest;
        }

        public String getLastResultForTabView() {
            return lastResultForTabView.get();
        }

        public SimpleStringProperty lastResultForTabViewProperty() {
            return lastResultForTabView;
        }

        public void setLastResultForTabView(String lastResultForTabView) {
            this.lastResultForTabView.setValue(lastResultForTabView);
        }

        public String getLastResult() {
            return lastResult;
        }

        public void setLastResult(String lastResult) {
            this.lastResult = lastResult;
        }

        public String getMaxError() {
            return maxError;
        }

        public void setMaxError(String maxError) {
            this.maxError = maxError;
        }

        public String getMinError() {
            return minError;
        }

        public void setMinError(String minError) {
            this.minError = minError;
        }

        public String[] getResults() {
            return results;
        }

        public void setResults(String[] results) {
            this.results = results;
        }

        public String getErrorsForTips() {
            return errorsForTips.get();
        }

        public SimpleStringProperty errorsForTipsProperty() {
            return errorsForTips;
        }

        public void setErrorsForTips(String errorsForTips) {
            this.errorsForTips.set(errorsForTips);
        }

        public void setSimpPropErrorsForTips() {
            this.errorsForTips = new SimpleStringProperty("");
        }
    }

    /**
     * Класс для записи результата испытания ErrorCommnad
     */
    public class ErrorResult extends CommandResult implements Serializable {

        public ErrorResult(String id, String minError, String maxError) {
            super(id);
            super.minError = minError;
            super.maxError = maxError;
        }

        //Устанавливает новый результат
        public void setResultErrorCommand(String errForTab, int resultNo, String error, boolean passOrNot) {
            super.lastResultForTabView.setValue(errForTab);
            super.results[resultNo] = error;
            super.lastResult = error;
            super.passTest = passOrNot;
            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }
    }

    /**
     * Класс для записи результата испытания CreepCommnad
     */
    public class CreepResult extends CommandResult implements Serializable {

        //Время провала теста, если он провален
        private String timeTheFailTest = "";

        //Время теста
        private String timeTheTest;

        //Количество импульсов для провала теста
        private String maxPulse;

        CreepResult(String id, String timeTheTest, String maxPulse) {
            super(id);
            this.timeTheTest = timeTheTest;
            this.maxPulse = maxPulse;
            super.lastResultForTabView.setValue("N" + timeTheTest);
        }

        //Устанавливает новый результат
        public void setResultCreepCommand(String time, int resultNo, boolean passOrNot) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + time + " P");
                super.lastResult = time + " P";
                super.results[resultNo] = time + " P";
                super.passTest = true;
            } else {
                super.lastResultForTabView.setValue("F" + time + " F");
                this.timeTheFailTest = time;
                super.lastResult = time + " F";
                super.results[resultNo] = time + " F";
                super.passTest = false;
            }

            creepTest = this;
            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }

        public void setMaxPulse(String maxPulse) {
            this.maxPulse = maxPulse;
        }

        public void setTimeTheTest(String timeTheTest) {
            this.timeTheTest = timeTheTest;
        }

        public String getTimeTheFailTest() {
            return timeTheFailTest;
        }

        public void setTimeTheFailTest(String timeTheFailTest) {
            this.timeTheFailTest = timeTheFailTest;
        }

        public String getTimeTheTest() {
            return timeTheTest;
        }

        public String getMaxPulse() {
            return maxPulse;
        }
    }

    /**
     * Класс для записи результата испытания StartCommnad
     */
    public class StartResult extends CommandResult implements Serializable {

        //Время прохождения теста
        private String timeThePassTest = "";

        //Время теста
        private String timeTheTest;

        //Количество импульсов для прохождения теста
        private String maxPulse;

        StartResult(String id, String timeTheTest, String maxPulse) {
            super(id);
            this.timeTheTest = timeTheTest;
            this.maxPulse = maxPulse;
            super.lastResultForTabView.setValue("N" + timeTheTest);
        }

        //Устанавливает новый результат
        public void setResultStartCommand(String time, int resultNo, boolean passOrNot, int chanelFlag) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + time + " P");
                this.timeThePassTest = time;
                super.lastResult = time + " P";
                super.results[resultNo] = time + " P";
                super.passTest = true;
            } else {
                super.lastResultForTabView.setValue("F" + time + " F");
                super.lastResult = time + " F";
                super.results[resultNo] = time + " F";
                super.passTest = false;
            }

            switch (chanelFlag) {
                case 0: startTestAPPls = this;
                    break;
                case 1: startTestAPMns = this;
                    break;
                case 2: startTestRPPls = this;
                    break;
                case 3: startTestRPMns = this;
                    break;
            }

            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }

        public void setMaxPulse(String maxPulse) {
            this.maxPulse = maxPulse;
        }

        public void setTimeTheTest(String timeTheTest) {
            this.timeTheTest = timeTheTest;
        }

        public String getTimeThePassTest() {
            return timeThePassTest;
        }

        public String getTimeTheTest() {
            return timeTheTest;
        }


        public String getMaxPulse() {
            return maxPulse;
        }
    }

    /**
     * Класс для записи результата испытания RTCCommand
     */
    public class RTCResult extends CommandResult implements Serializable {
        //Сачтота с который счётчик подаёт импульсы
        String freg;

        //Количество измерений (результатов теста)
        String amoutMeash;

        //Время измерения (количество импульсов необходимое для расчёта погрешности)
        String timeMeash;

        RTCResult(String id, String emin, String emax, String freg, String amoutMeash, String timeMeash) {
            super(id);
            super.minError = emin;
            super.maxError = emax;
            this.freg = freg;
            this.amoutMeash = amoutMeash;
            this.timeMeash = timeMeash;
        }

        //Устанавливает новый результат
        public void setResultRTCCommand(String error, int resultNo, boolean passOrNot) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + error + " P");
                super.lastResult = error;
                super.results[resultNo] = error;
                super.passTest = true;
            } else {
                super.lastResultForTabView.setValue("F" + error + " F");
                super.lastResult = error;
                super.results[resultNo] = error;
                super.passTest = false;
            }

            RTCTest = this;
            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }

        public String getFreg() {
            return freg;
        }

        public String getAmoutMeash() {
            return amoutMeash;
        }

        public String getTimeMeash() {
            return timeMeash;
        }

        public void setFreg(String freg) {
            this.freg = freg;
        }

        public void setAmoutMeash(String amoutMeash) {
            this.amoutMeash = amoutMeash;
        }

        public void setTimeMeash(String timeMeash) {
            this.timeMeash = timeMeash;
        }
    }

    /**
     * Класс для записи результата испытания ConstantCommand
     */
    public class ConstantResult extends CommandResult implements Serializable {

        //Количество энергии, которое посчитал испытываемый счётчик
        private String kwMeter = "";

        //Количество энергии, которое посчитал эталонный счётчик
        private String kwRefMeter = "";

        ConstantResult(String id, String emin, String emax) {
            super(id);
            super.minError = emin;
            super.maxError = emax;
        }

        //Устанавливает новый результат
        public void setResultConstantCommand(String result, int resultNo, Boolean passOrNot, int chanelFlag, String kwMeter, String kwRefMeter) {
            if (passOrNot == null) {
                super.lastResultForTabView.setValue("N" + result);
                super.lastResult = result;
                super.results[resultNo] = result;
                super.passTest = null;
                this.kwMeter = kwMeter;
                this.kwRefMeter = kwRefMeter;

            } else if (passOrNot) {
                super.lastResultForTabView.setValue("P" + result + " P");
                super.lastResult = result;
                super.results[resultNo] = result;
                super.passTest = true;
                this.kwMeter = kwMeter;
                this.kwRefMeter = kwRefMeter;

            } else {
                super.lastResultForTabView.setValue("F" + result + " F");
                super.lastResult = result;
                super.results[resultNo] = result;
                super.passTest = false;
                this.kwMeter = kwMeter;
                this.kwRefMeter = kwRefMeter;
            }

            switch (chanelFlag) {
                case 0: constantTestAPPls = this;
                    break;
                case 1: constantTestAPMns = this;
                    break;
                case 2: constantTestRPPls = this;
                    break;
                case 3: constantTestRPMns = this;
                    break;
            }

            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }

        public String getKwMeter() {
            return kwMeter;
        }

        public void setKwRefMeter(String kwRefMeter) {
            this.kwRefMeter = kwRefMeter;
        }

        public void setKwMeter(String kwMeter) {
            this.kwMeter = kwMeter;
        }

        public String getKwRefMeter() {
            return kwRefMeter;
        }
    }

    /**
     * Класс для записи результата испытания ImbalansUCommand
     */
    public class ImbUResult extends CommandResult implements Serializable {

        public ImbUResult(String id, String emin, String emax) {
            super(id);
            super.minError = emin;
            super.maxError = emax;
        }

        //Устанавливает новый результат
        public void setResultImbCommand(String errForTab, int resultNo, String error, boolean passOrNot) {
            super.lastResultForTabView.setValue(errForTab);
            super.results[resultNo] = error;
            super.lastResult = error;
            super.passTest = passOrNot;
            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }
    }

    /**
     * Класс для записи результата испытания Insulation (Изоляция)
     */
    public class InsulationResult extends CommandResult implements Serializable {

        InsulationResult(String id) {
            super(id);
        }
    }

    /**
     * Класс для записи результата испытания Appearens (Внешний вид)
     */
    public class AppearensResult extends CommandResult implements Serializable {

        AppearensResult(String id) {
            super(id);
        }
    }

    /**
     * Класс для записи результата испытания RelayCommand
     */
    public class RelayResult extends CommandResult implements Serializable {

        //Время провала теста
        private String timeTheFailTest = "";

        private String timeTheTest;

        private String maxPulse;

        RelayResult(String id, String timeTheTest, String maxPulse) {
            super(id);
            this.timeTheTest = timeTheTest;
            this.maxPulse = maxPulse;
            super.lastResultForTabView.setValue("N" + timeTheTest);
        }

        //Устанавливает новый результат
        public void setResultRelayCommand(String time, int resultNo, boolean passOrNot) {
            if (passOrNot) {
                super.lastResultForTabView.setValue("P" + time + " P");
                super.lastResult = time + " P";
                super.results[resultNo] = time + " P";
                super.passTest = true;
            } else {
                super.lastResultForTabView.setValue("F" + time + " F");
                this.timeTheFailTest = time;
                super.lastResult = time + " F";
                super.results[resultNo] = time + " F";
                super.passTest = false;
            }

            relayTest = this;
            refreshTipsInfo();
            TestErrorTableFrameController.saveResults = true;
        }

        public void setMaxPulse(String maxPulse) {
            this.maxPulse = maxPulse;
        }

        public void setTimeTheTest(String timeTheTest) {
            this.timeTheTest = timeTheTest;
        }

        public String getTimeTheFailTest() {
            return timeTheFailTest;
        }

        public void setTimeTheFailTest(String timeTheFailTest) {
            this.timeTheFailTest = timeTheFailTest;
        }

        public String getTimeTheTest() {
            return timeTheTest;
        }

        public String getMaxPulse() {
            return maxPulse;
        }
    }

    /**
     * Класс для записи результата испытания Заключающий результат
     */
    public class TotalResult extends CommandResult implements Serializable {

        TotalResult(String id) {
            super(id);
            super.passTest = null;
        }

        //Расчитывает заключающий результат (прошёл счётчик испытания или нет)
        public Boolean calculateTotalResult() {
            Boolean result = null;

            if (creepTest.getPassTest() != null) {
                if (!creepTest.isPassTest()) {
                    return false;
                } else result = true;
            }

            if (startTestAPPls.getPassTest() != null) {
                if (!startTestAPPls.isPassTest()) return false;
                else result = true;
            }

            if (startTestAPMns.getPassTest() != null) {
                if (!startTestAPMns.isPassTest()) return false;
                else result = true;
            }

            if (startTestRPPls.getPassTest() != null) {
                if (!startTestRPPls.isPassTest()) return false;
                else result = true;
            }

            if (startTestRPMns.getPassTest() != null) {
                if (!startTestRPMns.isPassTest()) return false;
                else result = true;
            }

            if (RTCTest.getPassTest() != null) {
                if (!RTCTest.isPassTest()) return false;
                else result = true;
            }

            if (insulationTest.getPassTest() != null) {
                if (!insulationTest.isPassTest()) return false;
                else result = true;
            }

            if (appearensTest.getPassTest() != null) {
                if (!appearensTest.isPassTest()) return false;
                else result = true;
            }

            if (constantTestAPPls.getPassTest() != null) {
                if (!constantTestAPPls.isPassTest()) return false;
                else result = true;
            }

            if (constantTestAPMns.getPassTest() != null) {
                if (!constantTestAPMns.isPassTest()) return false;
                else result = true;
            }

            if (constantTestRPPls.getPassTest() != null) {
                if (!constantTestRPPls.isPassTest()) return false;
                else result = true;
            }

            if (constantTestRPMns.getPassTest() != null) {
                if (!constantTestRPMns.isPassTest()) return false;
                else result = true;
            }

            if (relayTest.getPassTest() != null) {
                if (!relayTest.isPassTest()) return false;
                else result = true;
            }

            if (errorListAPPls.size() != 0) {
                for (Meter.CommandResult errResult : errorListAPPls) {
                    if (errResult instanceof ErrorResult) {
                        if (errResult.getPassTest() != null) {
                            if (!errResult.isPassTest()) {
                                return false;
                            } else {
                                result = true;
                            }
                        }
                    }
                }
            }

            if (errorListAPMns.size() != 0) {
                for (Meter.CommandResult errResult : errorListAPMns) {
                    if (errResult instanceof ErrorResult) {
                        if (errResult.getPassTest() != null) {
                            if (!errResult.isPassTest()) {
                                return false;
                            } else {
                                result = true;
                            }
                        }
                    }
                }
            }

            if (errorListRPPls.size() != 0) {
                for (Meter.CommandResult errResult : errorListRPPls) {
                    if (errResult instanceof ErrorResult) {
                        if (errResult.getPassTest() != null) {
                            if (!errResult.isPassTest()) {
                                return false;
                            } else {
                                result = true;
                            }
                        }
                    }
                }
            }

            if (errorListRPMns.size() != 0) {
                for (Meter.CommandResult errResult : errorListRPMns) {
                    if (errResult instanceof ErrorResult) {
                        if (errResult.getPassTest() != null) {
                            if (!errResult.isPassTest()) {
                                return false;
                            } else {
                                result = true;
                            }
                        }
                    }
                }
            }

            return result;
        }
    }

    /**
     *Рассчитывает ток по госту для испытания Чувствительность
     */
    private double calculateCurrentForSTA (TestErrorTableFrameController testErrorTableFrameController, int chanelFlag) {

        double ratedCurr;
        //Если активная энергия
        if (chanelFlag < 2) {
            //Если тип измерительного элемента счётчика шунт
            if (testErrorTableFrameController.isTypeOfMeasuringElementShunt()) {
                ratedCurr = 0.004 * testErrorTableFrameController.getIb();

                //Если трансформатор
            } else {

                //Если класт точности 0.5 и меньше
                if (testErrorTableFrameController.getAccuracyClassAP() <= 0.5) {
                    ratedCurr = 0.001 * testErrorTableFrameController.getIb();

                    //Если больше
                } else {
                    ratedCurr = 0.002 * testErrorTableFrameController.getIb();
                }
            }

            //Если реактивная энергия
        } else {
            if (testErrorTableFrameController.isTypeOfMeasuringElementShunt()) {
                ratedCurr = 0.004 * testErrorTableFrameController.getIb();

            } else {
                if (testErrorTableFrameController.getAccuracyClassRP() <= 0.5) {
                    ratedCurr = 0.001 * testErrorTableFrameController.getIb();

                } else {
                    ratedCurr = 0.002 * testErrorTableFrameController.getIb();
                }
            }
        }

        return ratedCurr;
    }
}
