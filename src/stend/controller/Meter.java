package stend.controller;

public class Meter {
    private long id;
    private double[] errors = new double[10];

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
}
