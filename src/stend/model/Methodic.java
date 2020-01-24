package stend.model;

import stend.controller.Commands.Commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Methodic {

    private String methodicName;
    private Map<Integer, List<Commands>> commandsMap = new HashMap<>(4);

    //Объект для сохранения точек связанных с влиянием
    private InfluenceMetodic influenceMetodic = new InfluenceMetodic();


    public void setSaveInflListForCollumAPPls(List<Commands> saveInflListForCollumAPPls) {
        influenceMetodic.influenceCommandsMap.put(0, saveInflListForCollumAPPls);
    }

    public void setSaveInflListForCollumAPMns(List<Commands> saveInflListForCollumAPMns) {
        influenceMetodic.influenceCommandsMap.put(1, saveInflListForCollumAPMns);
    }

    public void setSaveInflListForCollumRPPls(List<Commands> saveInflListForCollumRPPls) {
        influenceMetodic.influenceCommandsMap.put(3, saveInflListForCollumRPPls);
    }

    public void setSaveInflListForCollumRPMns(List<Commands> saveInflListForCollumRPMns) {
        influenceMetodic.influenceCommandsMap.put(4, saveInflListForCollumRPMns);
    }

    public void setSaveInfluenceUprocAPPls(double[] saveInfluenceUprocAPPls) {
        influenceMetodic.saveInfluenceUprocAPPls = saveInfluenceUprocAPPls;
    }

    public void setSaveInfluenceFprocAPPls(double[] saveInfluenceFprocAPPls) {
        influenceMetodic.saveInfluenceFprocAPPls = saveInfluenceFprocAPPls;
    }

    public void setSaveInfluenceInbUAPPls(String[] saveInfluenceInbUAPPls) {
        influenceMetodic.saveInfluenceInbUAPPls = saveInfluenceInbUAPPls;
    }

    public void setSaveInfluenceUprocAPMns(double[] saveInfluenceUprocAPMns) {
        influenceMetodic.saveInfluenceUprocAPMns = saveInfluenceUprocAPMns;
    }

    public void setSaveInfluenceFprocAPMns(double[] saveInfluenceFprocAPMns) {
        influenceMetodic.saveInfluenceFprocAPMns = saveInfluenceFprocAPMns;
    }

    public void setSaveInfluenceInbUAPMns(String[] saveInfluenceInbUAPMns) {
        influenceMetodic.saveInfluenceInbUAPMns = saveInfluenceInbUAPMns;
    }

    public void setSaveInfluenceUprocRPPls(double[] saveInfluenceUprocRPPls) {
        influenceMetodic.saveInfluenceUprocRPPls = saveInfluenceUprocRPPls;
    }

    public void setSaveInfluenceFprocRPPls(double[] saveInfluenceFprocRPPls) {
        influenceMetodic.saveInfluenceFprocRPPls = saveInfluenceFprocRPPls;
    }

    public void setSaveInfluenceInbURPPls(String[] saveInfluenceInbURPPls) {
        influenceMetodic.saveInfluenceInbURPPls = saveInfluenceInbURPPls;
    }

    public void setSaveInfluenceUprocRPMns(double[] saveInfluenceUprocRPMns) {
        influenceMetodic.saveInfluenceUprocRPMns = saveInfluenceUprocRPMns;
    }

    public void setSaveInfluenceFprocRPMns(double[] saveInfluenceFprocRPMns) {
        influenceMetodic.saveInfluenceFprocRPMns = saveInfluenceFprocRPMns;
    }

    public void setSaveInfluenceInbURPMns(String[] saveInfluenceInbURPMns) {
        influenceMetodic.saveInfluenceInbURPMns = saveInfluenceInbURPMns;
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
        return influenceMetodic.influenceCommandsMap.get(0);
    }

    public double[] getSaveInfluenceUprocAPPls() {
        return influenceMetodic.saveInfluenceUprocAPPls;
    }

    public double[] getSaveInfluenceFprocAPPls() {
        return influenceMetodic.saveInfluenceFprocAPPls;
    }

    public String[] getSaveInfluenceInbUAPPls() {
        return influenceMetodic.saveInfluenceInbUAPPls;
    }

    public double[] getSaveInfluenceUprocAPMns() {
        return influenceMetodic.saveInfluenceUprocAPMns;
    }

    public double[] getSaveInfluenceFprocAPMns() {
        return influenceMetodic.saveInfluenceFprocAPMns;
    }

    public String[] getSaveInfluenceInbUAPMns() {
        return influenceMetodic.saveInfluenceInbUAPMns;
    }

    public double[] getSaveInfluenceUprocRPPls() {
        return influenceMetodic.saveInfluenceUprocRPPls;
    }

    public double[] getSaveInfluenceFprocRPPls() {
        return influenceMetodic.saveInfluenceFprocRPPls;
    }

    public String[] getSaveInfluenceInbURPPls() {
        return influenceMetodic.saveInfluenceInbURPPls;
    }

    public double[] getSaveInfluenceUprocRPMns() {
        return influenceMetodic.saveInfluenceUprocRPMns;
    }

    public double[] getSaveInfluenceFprocRPMns() {
        return influenceMetodic.saveInfluenceFprocRPMns;
    }

    public String[] getSaveInfluenceInbURPMns() {
        return influenceMetodic.saveInfluenceInbURPMns;
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
            commandsMap.get(numb).clear();
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

    //Внутренний класс отвечающий за точки влияния
    class InfluenceMetodic {

        InfluenceMetodic() {
            influenceCommandsMap.put(0, saveInflListForCollumAPPls);
            influenceCommandsMap.put(1, saveInflListForCollumAPMns);
            influenceCommandsMap.put(2, saveInflListForCollumRPPls);
            influenceCommandsMap.put(3, saveInflListForCollumRPMns);
        }

        private Map<Integer, List<Commands>> influenceCommandsMap = new HashMap<>(4);


        //листы с точками после сохранения
        private List<Commands> saveInflListForCollumAPPls = new ArrayList<>();
        private List<Commands> saveInflListForCollumAPMns = new ArrayList<>();
        private List<Commands> saveInflListForCollumRPPls = new ArrayList<>();
        private List<Commands> saveInflListForCollumRPMns = new ArrayList<>();

        //Для теста влияния значения в в процентах
        //AP-
        double[] saveInfluenceUprocAPPls = new double[0];
        double[] saveInfluenceFprocAPPls = new double[0];
        String[] saveInfluenceInbUAPPls = new String[0];

        //AP-
        double[] saveInfluenceUprocAPMns = new double[0];
        double[] saveInfluenceFprocAPMns = new double[0];
        String[] saveInfluenceInbUAPMns = new String[0];

        //RP+
        double[] saveInfluenceUprocRPPls = new double[0];
        double[] saveInfluenceFprocRPPls = new double[0];
        private String[] saveInfluenceInbURPPls = new String[0];

        //RP-
        private double[] saveInfluenceUprocRPMns = new double[0];
        private double[] saveInfluenceFprocRPMns = new double[0];
        private String[] saveInfluenceInbURPMns = new String[0];
    }
}