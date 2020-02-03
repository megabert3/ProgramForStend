package stend.controller;

public class Meter {

    private String id;
    private double[] errors = new double[10];

    private boolean active = true;

    //Серийный номер счётчика
    private String serNoMeter;

    //Константа активной энергии счётчика
    private String constantMeterAP;

    //Константа реактивной энергии счётчика
    private String constantMeterRP;

    //Серийный номер счётчика
    private String modelMeter;

    //Результат теста чувствительности к току
    private boolean startTestActiveEnergyDirect;
    private boolean startTestReactiveEnergyDirect;
    private boolean startTestActiveEnergyReverse;
    private boolean startTestReactiveEnergyReverse;

    //Результат теста проверка счётного механизма
    private boolean constActiveEnergyDirect;
    private boolean constReactiveEnergyDirect;
    private boolean constActiveEnergyReverse;
    private boolean constReactiveEnergyReverse;

    //Результат теста RTC
    private String RTCTestResult;

    //Результат теста самохода
    private boolean creepTest;

    public void setErrors(double[] errors) {
        this.errors = errors;
    }

    public void setCreepTest(boolean creepTest) {
        this.creepTest = creepTest;
    }

    public double[] getErrors() {
        return errors;
    }

    public void setRTCTestResult(String RTCTestResult) {
        this.RTCTestResult = RTCTestResult;
    }

    public void setStartTestActiveEnergyDirect(boolean startTestActiveEnergyDirect) {
        this.startTestActiveEnergyDirect = startTestActiveEnergyDirect;
    }

    public void setStartTestReactiveEnergyDirect(boolean startTestReactiveEnergyDirect) {
        this.startTestReactiveEnergyDirect = startTestReactiveEnergyDirect;
    }

    public void setStartTestActiveEnergyReverse(boolean startTestActiveEnergyReverse) {
        this.startTestActiveEnergyReverse = startTestActiveEnergyReverse;
    }

    public void setStartTestReactiveEnergyReverse(boolean startTestReactiveEnergyReverse) {
        this.startTestReactiveEnergyReverse = startTestReactiveEnergyReverse;
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

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isActive() {
        return active;
    }
}
