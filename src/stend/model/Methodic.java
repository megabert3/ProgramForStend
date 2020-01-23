package stend.model;

import stend.controller.Commands.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Methodic {

    private String methodicName;
    private Map<Integer, List<Commands>> commandsMap = new HashMap<>(4);


    //листы с точками после сохранения
    private List<Commands> saveInflListForCollumAPPls = new ArrayList<>();
    private List<Commands> saveInflListForCollumAPMns = new ArrayList<>();
    private List<Commands> saveInflListForCollumRPPls = new ArrayList<>();
    private List<Commands> saveInflListForCollumRPMns = new ArrayList<>();

    //Для теста влияния
    //AP-
    private double[] saveInfluenceUprocAPPls = new double[0];
    private double[] saveInfluenceFprocAPPls = new double[0];
    private String[] saveInfluenceInbUAPPls = new String[0];

    //AP-
    private double[] saveInfluenceUprocAPMns = new double[0];
    private double[] saveInfluenceFprocAPMns = new double[0];
    private String[] saveInfluenceInbUAPMns = new String[0];

    //RP+
    private double[] saveInfluenceUprocRPPls = new double[0];
    private double[] saveInfluenceFprocRPPls = new double[0];
    private String[] saveInfluenceInbURPPls = new String[0];

    //RP-
    private double[] saveInfluenceUprocRPMns = new double[0];
    private double[] saveInfluenceFprocRPMns = new double[0];
    private String[] saveInfluenceInbURPMns = new String[0];

    public void setSaveInflListForCollumAPPls(List<Commands> saveInflListForCollumAPPls) {
        this.saveInflListForCollumAPPls = saveInflListForCollumAPPls;
    }

    public void setSaveInflListForCollumAPMns(List<Commands> saveInflListForCollumAPMns) {
        this.saveInflListForCollumAPMns = saveInflListForCollumAPMns;
    }

    public void setSaveInflListForCollumRPPls(List<Commands> saveInflListForCollumRPPls) {
        this.saveInflListForCollumRPPls = saveInflListForCollumRPPls;
    }

    public void setSaveInflListForCollumRPMns(List<Commands> saveInflListForCollumRPMns) {
        this.saveInflListForCollumRPMns = saveInflListForCollumRPMns;
    }

    public void setSaveInfluenceUprocAPPls(double[] saveInfluenceUprocAPPls) {
        this.saveInfluenceUprocAPPls = saveInfluenceUprocAPPls;
    }

    public void setSaveInfluenceFprocAPPls(double[] saveInfluenceFprocAPPls) {
        this.saveInfluenceFprocAPPls = saveInfluenceFprocAPPls;
    }

    public void setSaveInfluenceInbUAPPls(String[] saveInfluenceInbUAPPls) {
        this.saveInfluenceInbUAPPls = saveInfluenceInbUAPPls;
    }

    public void setSaveInfluenceUprocAPMns(double[] saveInfluenceUprocAPMns) {
        this.saveInfluenceUprocAPMns = saveInfluenceUprocAPMns;
    }

    public void setSaveInfluenceFprocAPMns(double[] saveInfluenceFprocAPMns) {
        this.saveInfluenceFprocAPMns = saveInfluenceFprocAPMns;
    }

    public void setSaveInfluenceInbUAPMns(String[] saveInfluenceInbUAPMns) {
        this.saveInfluenceInbUAPMns = saveInfluenceInbUAPMns;
    }

    public void setSaveInfluenceUprocRPPls(double[] saveInfluenceUprocRPPls) {
        this.saveInfluenceUprocRPPls = saveInfluenceUprocRPPls;
    }

    public void setSaveInfluenceFprocRPPls(double[] saveInfluenceFprocRPPls) {
        this.saveInfluenceFprocRPPls = saveInfluenceFprocRPPls;
    }

    public void setSaveInfluenceInbURPPls(String[] saveInfluenceInbURPPls) {
        this.saveInfluenceInbURPPls = saveInfluenceInbURPPls;
    }

    public void setSaveInfluenceUprocRPMns(double[] saveInfluenceUprocRPMns) {
        this.saveInfluenceUprocRPMns = saveInfluenceUprocRPMns;
    }

    public void setSaveInfluenceFprocRPMns(double[] saveInfluenceFprocRPMns) {
        this.saveInfluenceFprocRPMns = saveInfluenceFprocRPMns;
    }

    public void setSaveInfluenceInbURPMns(String[] saveInfluenceInbURPMns) {
        this.saveInfluenceInbURPMns = saveInfluenceInbURPMns;
    }

    public List<Commands> getSaveInflListForCollumAPPls() {
        return saveInflListForCollumAPPls;
    }

    public List<Commands> getSaveInflListForCollumAPMns() {
        return saveInflListForCollumAPMns;
    }

    public List<Commands> getSaveInflListForCollumRPPls() {
        return saveInflListForCollumRPPls;
    }

    public List<Commands> getSaveInflListForCollumRPMns() {
        return saveInflListForCollumRPMns;
    }

    public double[] getSaveInfluenceUprocAPPls() {
        return saveInfluenceUprocAPPls;
    }

    public double[] getSaveInfluenceFprocAPPls() {
        return saveInfluenceFprocAPPls;
    }

    public String[] getSaveInfluenceInbUAPPls() {
        return saveInfluenceInbUAPPls;
    }

    public double[] getSaveInfluenceUprocAPMns() {
        return saveInfluenceUprocAPMns;
    }

    public double[] getSaveInfluenceFprocAPMns() {
        return saveInfluenceFprocAPMns;
    }

    public String[] getSaveInfluenceInbUAPMns() {
        return saveInfluenceInbUAPMns;
    }

    public double[] getSaveInfluenceUprocRPPls() {
        return saveInfluenceUprocRPPls;
    }

    public double[] getSaveInfluenceFprocRPPls() {
        return saveInfluenceFprocRPPls;
    }

    public String[] getSaveInfluenceInbURPPls() {
        return saveInfluenceInbURPPls;
    }

    public double[] getSaveInfluenceUprocRPMns() {
        return saveInfluenceUprocRPMns;
    }

    public double[] getSaveInfluenceFprocRPMns() {
        return saveInfluenceFprocRPMns;
    }

    public String[] getSaveInfluenceInbURPMns() {
        return saveInfluenceInbURPMns;
    }

    public Methodic() {
        commandsMap.put(0, new ArrayList<Commands>());
        commandsMap.put(1, new ArrayList<Commands>());
        commandsMap.put(2, new ArrayList<Commands>());
        commandsMap.put(3, new ArrayList<Commands>());
    }

    public boolean addCommandToList(Integer numb, ArrayList<Commands> list) {
        if (numb > 4 || numb < 0) return false;
        else {
            commandsMap.get(numb).addAll(list);
        }
        return true;
    }

    public Map<Integer, List<Commands>> getCommandsMap() {
        return commandsMap;
    }

    public String getMethodicName() {
        return methodicName;
    }

    void setMethodicName(String methodicName) {
        this.methodicName = methodicName;
    }
}