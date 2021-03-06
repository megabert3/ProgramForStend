package org.taipit.stend.model.metodics;

import org.taipit.stend.controller.сommands.Commands;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за создание методики поверки для трехфазного стенда.
 * Является неким контейнером содержащий точки испытаний, которые необходимо выполнить.
 */
 public class MethodicForThreePhaseStend implements Metodic, Cloneable, Serializable {

    //Имя методики
    private String metodicName;

    //Если параметры при которых должна проводиться методика зафиксированны
    private boolean bindsParameters;

    //Содержит ли методика несохранённые результаты
    private boolean containtsLastNotSaveResults = false;

    /**
     * Данные параметры относятся к испытываемому счётчику
     */
    //Номинальное напряжение
    private String Unom;

    //Максимальный и минимальный ток
    private String ImaxAndInom;

    //Номинальная частота сети
    private String Fnom;

    //Класс точности активной энергии
    private String accuracyClassMeterAP;

    //Класс точности реактивной энергии
    private String accuracyClassMeterRP;

    //Тип измерительного элемента
    private String typeOfMeasuringElementShunt;

    //Тип счётчика (трех/одно фазный, много/одно тарифный)
    private String typeMeter;

    //Постоянная счётчика при подсчёте активной энергии (imp*kW*h)
    private String constantAP;

    //Постоянная счётчика при подсчёте реактивной энергии (imp*kW*h
    private String constantRP;

    //Завод изготовитель счётчика
    private String factoryManufactuter;

    //Модель счётчика (модель с наименованием)
    private String meterModel;

    /**
     * Содержит листы с командами которые необходимо выполнить установке
     * 0 - активная энергия в прямом направлении
     * 1 - активная энергия в обратном направлении
     * 2 - реактивная энергия в прямом направлении
     * 3 - реактивная энергия в обратном направлении
     */
    private Map<Integer, List<Commands>> commandsMap = new HashMap<>(4);

    //Объект для сохранения точек (коммад) связанных с влиянием (U, F)
    private InfluenceMetodic influenceMetodic = new InfluenceMetodic();

    //Объект для сохранения точек Самохода, чувствительности и влияния и т.д.
    private CreepStartRTCConst creepStartRTCConst = new CreepStartRTCConst();

    public MethodicForThreePhaseStend() {
        commandsMap.put(0, new ArrayList<Commands>());
        commandsMap.put(1, new ArrayList<Commands>());
        commandsMap.put(2, new ArrayList<Commands>());
        commandsMap.put(3, new ArrayList<Commands>());
    }

    //get's and set's
    public Map<Integer, List<Commands>> getCommandsMap() {
        return commandsMap;
    }

    public String getMetodicName() {
        return metodicName;
    }

    public void setMetodicName(String methodicName) {
        this.metodicName = methodicName;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    //Точки влияния
    public void setSaveInflListForCollumAPPls(List<Commands> saveInflListForCollumAPPls) {
        influenceMetodic.influenceCommandsMap.put(0, saveInflListForCollumAPPls);
    }

    public void setSaveInflListForCollumAPMns(List<Commands> saveInflListForCollumAPMns) {
        influenceMetodic.influenceCommandsMap.put(1, saveInflListForCollumAPMns);
    }

    public void setSaveInflListForCollumRPPls(List<Commands> saveInflListForCollumRPPls) {
        influenceMetodic.influenceCommandsMap.put(2, saveInflListForCollumRPPls);
    }

    public void setSaveInflListForCollumRPMns(List<Commands> saveInflListForCollumRPMns) {
        influenceMetodic.influenceCommandsMap.put(3, saveInflListForCollumRPMns);
    }

    public List<Commands> getSaveInflListForCollumAPPls() {
        return influenceMetodic.influenceCommandsMap.get(0);
    }

    public List<Commands> getSaveInflListForCollumAPMns() {
        return influenceMetodic.influenceCommandsMap.get(1);
    }

    public List<Commands> getSaveInflListForCollumRPPls() {
        return influenceMetodic.influenceCommandsMap.get(2);
    }

    public List<Commands> getSaveInflListForCollumRPMns() {
        return influenceMetodic.influenceCommandsMap.get(3);
    }

    //Инбаланс
    public void setSaveInfluenceInbUAPPls(String[] saveInfluenceInbUAPPls) {
        influenceMetodic.saveInfluenceInbUAPPls = saveInfluenceInbUAPPls;
    }

    public void setSaveInfluenceInbUAPMns(String[] saveInfluenceInbUAPMns) {
        influenceMetodic.saveInfluenceInbUAPMns = saveInfluenceInbUAPMns;
    }

    public void setSaveInfluenceInbURPPls(String[] saveInfluenceInbURPPls) {
        influenceMetodic.saveInfluenceInbURPPls = saveInfluenceInbURPPls;
    }

    public void setSaveInfluenceInbURPMns(String[] saveInfluenceInbURPMns) {
        influenceMetodic.saveInfluenceInbURPMns = saveInfluenceInbURPMns;
    }

    public String[] getSaveInfluenceInbUAPPls() {
        return influenceMetodic.saveInfluenceInbUAPPls;
    }

    public String[] getSaveInfluenceInbUAPMns() {
        return influenceMetodic.saveInfluenceInbUAPMns;
    }

    public String[] getSaveInfluenceInbURPPls() {
        return influenceMetodic.saveInfluenceInbURPPls;
    }

    public String[] getSaveInfluenceInbURPMns() {
        return influenceMetodic.saveInfluenceInbURPMns;
    }

    //Влияние AP+
    public float[] getSaveInfluenceUprocAllPhaseAPPls() {
        return influenceMetodic.saveInfluenceUprocAllPhaseAPPls;
    }

    public void setSaveInfluenceUprocAllPhaseAPPls(float[] saveInfluenceUprocAllPhaseAPPls) {
        this.influenceMetodic.saveInfluenceUprocAllPhaseAPPls = saveInfluenceUprocAllPhaseAPPls;
    }

    public float[] getSaveInfluenceUprocPhaseAAPPls() {
        return influenceMetodic.saveInfluenceUprocPhaseAAPPls;
    }

    public void setSaveInfluenceUprocPhaseAAPPls(float[] saveInfluenceUprocPhaseAAPPls) {
        this.influenceMetodic.saveInfluenceUprocPhaseAAPPls = saveInfluenceUprocPhaseAAPPls;
    }

    public float[] getSaveInfluenceUprocPhaseBAPPls() {
        return influenceMetodic.saveInfluenceUprocPhaseBAPPls;
    }

    public void setSaveInfluenceUprocPhaseBAPPls(float[] saveInfluenceUprocPhaseBAPPls) {
        this.influenceMetodic.saveInfluenceUprocPhaseBAPPls = saveInfluenceUprocPhaseBAPPls;
    }

    public float[] getSaveInfluenceUprocPhaseCAPPls() {
        return influenceMetodic.saveInfluenceUprocPhaseCAPPls;
    }

    public void setSaveInfluenceUprocPhaseCAPPls(float[] saveInfluenceUprocPhaseCAPPls) {
        this.influenceMetodic.saveInfluenceUprocPhaseCAPPls = saveInfluenceUprocPhaseCAPPls;
    }

    public float[] getSaveInfluenceFprocAllPhaseAPPls() {
        return influenceMetodic.saveInfluenceFprocAllPhaseAPPls;
    }

    public void setSaveInfluenceFprocAllPhaseAPPls(float[] saveInfluenceFprocAllPhaseAPPls) {
        this.influenceMetodic.saveInfluenceFprocAllPhaseAPPls = saveInfluenceFprocAllPhaseAPPls;
    }

    public float[] getSaveInfluenceFprocPhaseAAPPls() {
        return influenceMetodic.saveInfluenceFprocPhaseAAPPls;
    }

    public void setSaveInfluenceFprocPhaseAAPPls(float[] saveInfluenceFprocPhaseAAPPls) {
        this.influenceMetodic.saveInfluenceFprocPhaseAAPPls = saveInfluenceFprocPhaseAAPPls;
    }

    public float[] getSaveInfluenceFprocPhaseBAPPls() {
        return influenceMetodic.saveInfluenceFprocPhaseBAPPls;
    }

    public void setSaveInfluenceFprocPhaseBAPPls(float[] saveInfluenceFprocPhaseBAPPls) {
        this.influenceMetodic.saveInfluenceFprocPhaseBAPPls = saveInfluenceFprocPhaseBAPPls;
    }

    public float[] getSaveInfluenceFprocPhaseCAPPls() {
        return influenceMetodic.saveInfluenceFprocPhaseCAPPls;
    }

    public void setSaveInfluenceFprocPhaseCAPPls(float[] saveInfluenceFprocPhaseCAPPls) {
        this.influenceMetodic.saveInfluenceFprocPhaseCAPPls = saveInfluenceFprocPhaseCAPPls;
    }

    //Влияние AP-
    public float[] getSaveInfluenceUprocAllPhaseAPMns() {
        return influenceMetodic.saveInfluenceUprocAllPhaseAPMns;
    }

    public void setSaveInfluenceUprocAllPhaseAPMns(float[] saveInfluenceUprocAllPhaseAPMns) {
        this.influenceMetodic.saveInfluenceUprocAllPhaseAPMns = saveInfluenceUprocAllPhaseAPMns;
    }

    public float[] getSaveInfluenceUprocPhaseAAPMns() {
        return influenceMetodic.saveInfluenceUprocPhaseAAPMns;
    }

    public void setSaveInfluenceUprocPhaseAAPMns(float[] saveInfluenceUprocPhaseAAPMns) {
        this.influenceMetodic.saveInfluenceUprocPhaseAAPMns = saveInfluenceUprocPhaseAAPMns;
    }

    public float[] getSaveInfluenceUprocPhaseBAPMns() {
        return influenceMetodic.saveInfluenceUprocPhaseBAPMns;
    }

    public void setSaveInfluenceUprocPhaseBAPMns(float[] saveInfluenceUprocPhaseBAPMns) {
        this.influenceMetodic.saveInfluenceUprocPhaseBAPMns = saveInfluenceUprocPhaseBAPMns;
    }

    public float[] getSaveInfluenceUprocPhaseCAPMns() {
        return influenceMetodic.saveInfluenceUprocPhaseCAPMns;
    }

    public void setSaveInfluenceUprocPhaseCAPMns(float[] saveInfluenceUprocPhaseCAPMns) {
        this.influenceMetodic.saveInfluenceUprocPhaseCAPMns = saveInfluenceUprocPhaseCAPMns;
    }

    public float[] getSaveInfluenceFprocAllPhaseAPMns() {
        return influenceMetodic.saveInfluenceFprocAllPhaseAPMns;
    }

    public void setSaveInfluenceFprocAllPhaseAPMns(float[] saveInfluenceFprocAllPhaseAPMns) {
        this.influenceMetodic.saveInfluenceFprocAllPhaseAPMns = saveInfluenceFprocAllPhaseAPMns;
    }

    public float[] getSaveInfluenceFprocPhaseAAPMns() {
        return influenceMetodic.saveInfluenceFprocPhaseAAPMns;
    }

    public void setSaveInfluenceFprocPhaseAAPMns(float[] saveInfluenceFprocPhaseAAPMns) {
        this.influenceMetodic.saveInfluenceFprocPhaseAAPMns = saveInfluenceFprocPhaseAAPMns;
    }

    public float[] getSaveInfluenceFprocPhaseBAPMns() {
        return influenceMetodic.saveInfluenceFprocPhaseBAPMns;
    }

    public void setSaveInfluenceFprocPhaseBAPMns(float[] saveInfluenceFprocPhaseBAPMns) {
        this.influenceMetodic.saveInfluenceFprocPhaseBAPMns = saveInfluenceFprocPhaseBAPMns;
    }

    public float[] getSaveInfluenceFprocPhaseCAPMns() {
        return influenceMetodic.saveInfluenceFprocPhaseCAPMns;
    }

    public void setSaveInfluenceFprocPhaseCAPMns(float[] saveInfluenceFprocPhaseCAPMns) {
        this.influenceMetodic.saveInfluenceFprocPhaseCAPMns = saveInfluenceFprocPhaseCAPMns;
    }

    public float[] getSaveInfluenceUprocAllPhaseRPPls() {
        return influenceMetodic.saveInfluenceUprocAllPhaseRPPls;
    }

    public void setSaveInfluenceUprocAllPhaseRPPls(float[] saveInfluenceUprocAllPhaseRPPls) {
        this.influenceMetodic.saveInfluenceUprocAllPhaseRPPls = saveInfluenceUprocAllPhaseRPPls;
    }

    public float[] getSaveInfluenceUprocPhaseARPPls() {
        return influenceMetodic.saveInfluenceUprocPhaseARPPls;
    }

    public void setSaveInfluenceUprocPhaseARPPls(float[] saveInfluenceUprocPhaseARPPls) {
        this.influenceMetodic.saveInfluenceUprocPhaseARPPls = saveInfluenceUprocPhaseARPPls;
    }

    //Влияние RP+
    public float[] getSaveInfluenceUprocPhaseBRPPls() {
        return influenceMetodic.saveInfluenceUprocPhaseBRPPls;
    }

    public void setSaveInfluenceUprocPhaseBRPPls(float[] saveInfluenceUprocPhaseBRPPls) {
        this.influenceMetodic.saveInfluenceUprocPhaseBRPPls = saveInfluenceUprocPhaseBRPPls;
    }

    public float[] getSaveInfluenceUprocPhaseCRPPls() {
        return influenceMetodic.saveInfluenceUprocPhaseCRPPls;
    }

    public void setSaveInfluenceUprocPhaseCRPPls(float[] saveInfluenceUprocPhaseCRPPls) {
        this.influenceMetodic.saveInfluenceUprocPhaseCRPPls = saveInfluenceUprocPhaseCRPPls;
    }

    public float[] getSaveInfluenceFprocAllPhaseRPPls() {
        return influenceMetodic.saveInfluenceFprocAllPhaseRPPls;
    }

    public void setSaveInfluenceFprocAllPhaseRPPls(float[] saveInfluenceFprocAllPhaseRPPls) {
        this.influenceMetodic.saveInfluenceFprocAllPhaseRPPls = saveInfluenceFprocAllPhaseRPPls;
    }

    public float[] getSaveInfluenceFprocPhaseARPPls() {
        return influenceMetodic.saveInfluenceFprocPhaseARPPls;
    }

    public void setSaveInfluenceFprocPhaseARPPls(float[] saveInfluenceFprocPhaseARPPls) {
        this.influenceMetodic.saveInfluenceFprocPhaseARPPls = saveInfluenceFprocPhaseARPPls;
    }

    public float[] getSaveInfluenceFprocPhaseBRPPls() {
        return influenceMetodic.saveInfluenceFprocPhaseBRPPls;
    }

    public void setSaveInfluenceFprocPhaseBRPPls(float[] saveInfluenceFprocPhaseBRPPls) {
        this.influenceMetodic.saveInfluenceFprocPhaseBRPPls = saveInfluenceFprocPhaseBRPPls;
    }

    public float[] getSaveInfluenceFprocPhaseCRPPls() {
        return influenceMetodic.saveInfluenceFprocPhaseCRPPls;
    }

    public void setSaveInfluenceFprocPhaseCRPPls(float[] saveInfluenceFprocPhaseCRPPls) {
        this.influenceMetodic.saveInfluenceFprocPhaseCRPPls = saveInfluenceFprocPhaseCRPPls;
    }

    //Влияние RP-
    public float[] getSaveInfluenceUprocAllPhaseRPMns() {
        return influenceMetodic.saveInfluenceUprocAllPhaseRPMns;
    }

    public void setSaveInfluenceUprocAllPhaseRPMns(float[] saveInfluenceUprocAllPhaseRPMns) {
        this.influenceMetodic.saveInfluenceUprocAllPhaseRPMns = saveInfluenceUprocAllPhaseRPMns;
    }

    public float[] getSaveInfluenceUprocPhaseARPMns() {
        return influenceMetodic.saveInfluenceUprocPhaseARPMns;
    }

    public void setSaveInfluenceUprocPhaseARPMns(float[] saveInfluenceUprocPhaseARPMns) {
        this.influenceMetodic.saveInfluenceUprocPhaseARPMns = saveInfluenceUprocPhaseARPMns;
    }

    public float[] getSaveInfluenceUprocPhaseBRPMns() {
        return influenceMetodic.saveInfluenceUprocPhaseBRPMns;
    }

    public void setSaveInfluenceUprocPhaseBRPMns(float[] saveInfluenceUprocPhaseBRPMns) {
        this.influenceMetodic.saveInfluenceUprocPhaseBRPMns = saveInfluenceUprocPhaseBRPMns;
    }

    public float[] getSaveInfluenceUprocPhaseCRPMns() {
        return influenceMetodic.saveInfluenceUprocPhaseCRPMns;
    }

    public void setSaveInfluenceUprocPhaseCRPMns(float[] saveInfluenceUprocPhaseCRPMns) {
        this.influenceMetodic.saveInfluenceUprocPhaseCRPMns = saveInfluenceUprocPhaseCRPMns;
    }

    public float[] getSaveInfluenceFprocAllPhaseRPMns() {
        return influenceMetodic.saveInfluenceFprocAllPhaseRPMns;
    }

    public void setSaveInfluenceFprocAllPhaseRPMns(float[] saveInfluenceFprocAllPhaseRPMns) {
        this.influenceMetodic.saveInfluenceFprocAllPhaseRPMns = saveInfluenceFprocAllPhaseRPMns;
    }

    public float[] getSaveInfluenceFprocPhaseARPMns() {
        return influenceMetodic.saveInfluenceFprocPhaseARPMns;
    }

    public void setSaveInfluenceFprocPhaseARPMns(float[] saveInfluenceFprocPhaseARPMns) {
        this.influenceMetodic.saveInfluenceFprocPhaseARPMns = saveInfluenceFprocPhaseARPMns;
    }

    public float[] getSaveInfluenceFprocPhaseBRPMns() {
        return influenceMetodic.saveInfluenceFprocPhaseBRPMns;
    }

    public void setSaveInfluenceFprocPhaseBRPMns(float[] saveInfluenceFprocPhaseBRPMns) {
        this.influenceMetodic.saveInfluenceFprocPhaseBRPMns = saveInfluenceFprocPhaseBRPMns;
    }

    public float[] getSaveInfluenceFprocPhaseCRPMns() {
        return influenceMetodic.saveInfluenceFprocPhaseCRPMns;
    }

    public void setSaveInfluenceFprocPhaseCRPMns(float[] saveInfluenceFprocPhaseCRPMns) {
        this.influenceMetodic.saveInfluenceFprocPhaseCRPMns = saveInfluenceFprocPhaseCRPMns;
    }

    public Map<Integer, List<Commands>> getCreepStartRTCConstCommandsMap() {
        return creepStartRTCConst.creepStartRTCConstCommandsMap;
    }

   //======================================================================================
    public boolean isBindsParameters() {
        return bindsParameters;
    }

    public void setBindsParameters(boolean bindsParameters) {
        this.bindsParameters = bindsParameters;
    }

    public String getUnom() {
        return Unom;
    }

    public void setUnom(String unom) {
        Unom = unom;
    }

    public String getImaxAndInom() {
        return ImaxAndInom;
    }

    public void setImaxAndInom(String imaxAndInom) {
        ImaxAndInom = imaxAndInom;
    }

    public String getFnom() {
        return Fnom;
    }

    public void setFnom(String fnom) {
        Fnom = fnom;
    }

    public String getAccuracyClassMeterAP() {
        return accuracyClassMeterAP;
    }

    public void setAccuracyClassMeterAP(String accuracyClassMeterAP) {
        this.accuracyClassMeterAP = accuracyClassMeterAP;
    }

    public String getAccuracyClassMeterRP() {
        return accuracyClassMeterRP;
    }

    public void setAccuracyClassMeterRP(String accuracyClassMeterRP) {
        this.accuracyClassMeterRP = accuracyClassMeterRP;
    }

    public String isTypeOfMeasuringElementShunt() {
        return typeOfMeasuringElementShunt;
    }

    public void setTypeOfMeasuringElementShunt(String typeOfMeasuringElementShunt) {
        this.typeOfMeasuringElementShunt = typeOfMeasuringElementShunt;
    }

    public String getTypeMeter() {
        return typeMeter;
    }

    public void setTypeMeter(String typeMeter) {
        this.typeMeter = typeMeter;
    }

    public String getConstantAP() {
        return constantAP;
    }

    public void setConstantAP(String constantAP) {
        this.constantAP = constantAP;
    }

    public String getConstantRP() {
        return constantRP;
    }

    public void setConstantRP(String constantRP) {
        this.constantRP = constantRP;
    }

    public String getFactoryManufactuter() {
        return factoryManufactuter;
    }

    public void setFactoryManufactuter(String factoryManufactuter) {
        this.factoryManufactuter = factoryManufactuter;
    }

    public String getMeterModel() {
        return meterModel;
    }

    public void setMeterModel(String meterModel) {
        this.meterModel = meterModel;
    }

    public String getTypeOfMeasuringElementShunt() {
        return typeOfMeasuringElementShunt;
    }

//===========================================================================================

    /**
     * Данный клас является контейнером для точек испытаний созданых из окна влияния
     */
    class InfluenceMetodic implements Cloneable, Serializable{

        InfluenceMetodic() {
            //Точки активной энергии в прямом направлении тока (AP+)
            influenceCommandsMap.put(0, new ArrayList<>());
            //Точки активной энергии в обратном направлении тока (AP-)
            influenceCommandsMap.put(1, new ArrayList<>());
            //Точки реактивной энергии в прямом направлении тока (RP+)
            influenceCommandsMap.put(2, new ArrayList<>());
            //Точки реактивной энергии в обратном направлении тока (RP-)
            influenceCommandsMap.put(3, new ArrayList<>());
        }

        //Мапа с точками испытаний для разных направлений и типов мощности
        private Map<Integer, List<Commands>> influenceCommandsMap = new HashMap<>(4);

        //AP+
        //Влияние по напряжению
        //Все фазы
        private float[] saveInfluenceUprocAllPhaseAPPls = new float[0];
        //Фаза А
        private float[] saveInfluenceUprocPhaseAAPPls = new float[0];
        //Фаза B
        private float[] saveInfluenceUprocPhaseBAPPls = new float[0];
        //Фаза С
        private float[] saveInfluenceUprocPhaseCAPPls = new float[0];

        //Влияние по частоте
        //Все фазы
        private float[] saveInfluenceFprocAllPhaseAPPls = new float[0];
        //Фаза А
        private float[] saveInfluenceFprocPhaseAAPPls = new float[0];
        //Фаза B
        private float[] saveInfluenceFprocPhaseBAPPls = new float[0];
        //Фаза С
        private float[] saveInfluenceFprocPhaseCAPPls = new float[0];

        //Имбаланс напряжений
        private String[] saveInfluenceInbUAPPls = new String[0];

        //AP-
        private float[] saveInfluenceUprocAllPhaseAPMns = new float[0];
        private float[] saveInfluenceUprocPhaseAAPMns = new float[0];
        private float[] saveInfluenceUprocPhaseBAPMns = new float[0];
        private float[] saveInfluenceUprocPhaseCAPMns = new float[0];

        private float[] saveInfluenceFprocAllPhaseAPMns = new float[0];
        private float[] saveInfluenceFprocPhaseAAPMns = new float[0];
        private float[] saveInfluenceFprocPhaseBAPMns = new float[0];
        private float[] saveInfluenceFprocPhaseCAPMns = new float[0];

        private String[] saveInfluenceInbUAPMns = new String[0];

        //RP+
        private float[] saveInfluenceUprocAllPhaseRPPls = new float[0];
        private float[] saveInfluenceUprocPhaseARPPls = new float[0];
        private float[] saveInfluenceUprocPhaseBRPPls = new float[0];
        private float[] saveInfluenceUprocPhaseCRPPls = new float[0];

        private float[] saveInfluenceFprocAllPhaseRPPls = new float[0];
        private float[] saveInfluenceFprocPhaseARPPls = new float[0];
        private float[] saveInfluenceFprocPhaseBRPPls = new float[0];
        private float[] saveInfluenceFprocPhaseCRPPls = new float[0];

        private String[] saveInfluenceInbURPPls = new String[0];

        //RP-
        private float[] saveInfluenceUprocAllPhaseRPMns = new float[0];
        private float[] saveInfluenceUprocPhaseARPMns = new float[0];
        private float[] saveInfluenceUprocPhaseBRPMns = new float[0];
        private float[] saveInfluenceUprocPhaseCRPMns = new float[0];

        private float[] saveInfluenceFprocAllPhaseRPMns = new float[0];
        private float[] saveInfluenceFprocPhaseARPMns = new float[0];
        private float[] saveInfluenceFprocPhaseBRPMns = new float[0];
        private float[] saveInfluenceFprocPhaseCRPMns = new float[0];

        private String[] saveInfluenceInbURPMns = new String[0];

        @Override
        public Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

    //===========================================================================
    /**
     * Внутренний класс отвечающий за точки самохода, чувствительности, ТХЧ, Константы и т.д.
     */
    class CreepStartRTCConst implements Serializable, Cloneable {

        CreepStartRTCConst() {
            creepStartRTCConstCommandsMap.put(0, new ArrayList<>());
            creepStartRTCConstCommandsMap.put(1, new ArrayList<>());
            creepStartRTCConstCommandsMap.put(2, new ArrayList<>());
            creepStartRTCConstCommandsMap.put(3, new ArrayList<>());
        }

        private Map<Integer, List<Commands>> creepStartRTCConstCommandsMap = new HashMap<>(4);

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }

}