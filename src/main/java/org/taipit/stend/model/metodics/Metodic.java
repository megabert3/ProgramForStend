package org.taipit.stend.model.metodics;

import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.Meter;

import java.util.List;
import java.util.Map;

public interface Metodic extends Cloneable {

    String getMetodicName();

    void setMetodicName(String methodicName);

    boolean isBindsParameters();

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

    boolean isContaintsLastNotSaveResults();
    void setContaintsLastNotSaveResults(boolean containtsLastNotSaveResults);

    void setOldResultsNewMeters();

    void setNewResultsMeters(List<Meter> newResultsMeters);

    Object clone() throws CloneNotSupportedException;
}
