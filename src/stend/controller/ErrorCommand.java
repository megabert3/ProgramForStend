package stend.controller;

public class ErrorCommand implements Commands{
    private StendDLLCommands stendDLLCommands;

    private int Phase;
    private double Rated_Volt;
    private double Rated_Curr;
    private double Rated_Freq;
    private int PhaseSrequence;
    private int Revers;
    private double Volt_PerA;
    private double Volt_PerB;
    private double Volt_PerC;
    private double Curr_Per;
    private String IABC;
    private String CosP;



    public ErrorCommand(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;
    }

    @Override
    public boolean execute() {
        if (stendDLLCommands.isThreePhaseStend()) {
            //stendDLLCommands.getUIWithPhase()
        }
        return false;
    }
}
