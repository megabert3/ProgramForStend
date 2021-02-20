package org.taipit.stend.model.metodics;

import org.taipit.stend.controller.сommands.Commands;

import java.util.List;
import java.util.Map;

/**
 * @autor Albert Khalimov
 *
 * Данный интерфейс отвечает за реализацию методики поверки
 */
public interface Metodic extends Cloneable {

    /**
     * @return Возвращает имя методики
     */
    String getMetodicName();

    /**
     * Устаннавливает имя методике
     * @param methodicName имя методики
     */
    void setMetodicName(String methodicName);

    /**
     * Фиксированны ли параметры у методики (Параметры задаются при создании)
     * @return
     */
    boolean isBindsParameters();

    /**
     * @return - Мап с точками испытаний для разных направлений тока и типов мощности
     */
    Map<Integer, List<Commands>> getCommandsMap();

    Map<Integer, List<Commands>> getCreepStartRTCConstCommandsMap();

    List<Commands> getSaveInflListForCollumAPPls();
    List<Commands> getSaveInflListForCollumAPMns();
    List<Commands> getSaveInflListForCollumRPPls();
    List<Commands> getSaveInflListForCollumRPMns();

    void setBindsParameters(boolean b);

    void setUnom(String s);
    void setImaxAndInom(String s);
    void setFnom(String s);
    void setAccuracyClassMeterAP(String s);
    void setAccuracyClassMeterRP(String s);
    void setTypeMeter(String s);
    void setTypeOfMeasuringElementShunt(String s);
    void setConstantAP(String s);
    void setConstantRP(String s);
    void setFactoryManufactuter(String s);
    void setMeterModel(String s);

    String getUnom();
    String getImaxAndInom();
    String getFnom();
    String getAccuracyClassMeterAP();
    String getAccuracyClassMeterRP();
    String getTypeMeter();
    String getTypeOfMeasuringElementShunt();
    String getConstantAP();
    String getConstantRP();
    String getFactoryManufactuter();
    String getMeterModel();

    Object clone() throws CloneNotSupportedException;
}
