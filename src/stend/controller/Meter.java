package stend.controller;

public class Meter {
    private long id;
    private double[] errors = new double[10];

    //Серийный номер счётчика
    private String serNoMeter;

    //Константа активной энергии счётчика
    private int constantMeterAP;

    //Константа реактивной энергии счётчика
    private int constantMeterRP;

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

    public int getConstantMeterAP() {
        return constantMeterAP;
    }

    public int getConstantMeterRP() {
        return constantMeterRP;
    }

    public String getModelMeter() {
        return modelMeter;
    }

    public String getSerNoMeter() {
        return serNoMeter;
    }

    public void setConstantMeterRP(int constantMeterRP) {
        this.constantMeterRP = constantMeterRP;
    }

    public void setModelMeter(String modelMeter) {
        this.modelMeter = modelMeter;
    }

    public void setConstantMeterAP(int constantMeterAP) {
        this.constantMeterAP = constantMeterAP;
    }

    public void setSerNoMeter(String serNoMeter) {
        this.serNoMeter = serNoMeter;
    }
}
