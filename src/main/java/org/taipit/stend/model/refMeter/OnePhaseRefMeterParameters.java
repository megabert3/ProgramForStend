package org.taipit.stend.model.refMeter;

import org.taipit.stend.model.stend.OnePhaseStend;
import org.taipit.stend.model.stend.StendDLLCommands;
import org.taipit.stend.helper.ConsoleHelper;

import java.util.Timer;
import java.util.TimerTask;

public class OnePhaseRefMeterParameters implements RefMeterParameters{
//Однофазное подключение:
//HY5101C-22(Simulation Meter) : U,I,UI_Angle
//HY5101C-23?SY3803 : U,I,UI_Angle,A.P.,R.P.,Apparent power, Freq
//TC-3000C? Ua , Ub , Uc , Ia , Ib , Ic , UI_Angle_a , UI_Angle_b , UI_Angle_c , Pa , Pb , Pc , Qa ,
//Qb , Qc , Sa , Sb , Sc , A.P. , R.P. , Apparent power , Freq , I_Range

    private StendDLLCommands onePhaseStend = OnePhaseStend.getOnePhaseStendInstance();

    private double U;
    private double I;
    private double UIAngle;

    private double P;
    private double Q;
    private double S;

    private double F;

    private Timer timer;

    private TimerTask readReferenceParamTask;

    public void startReadRefMeterParameters() {

        String refMeterType = ConsoleHelper.properties.getProperty("refMeterModel");

        timer = new Timer(true);

        if (refMeterType.equals("HY5101C-23") || refMeterType.equals("SY3803")) {
            readReferenceParamTask = new TimerTask() {
                @Override
                public void run() {
                    //HY5101C-23?SY3803 : U,I,UI_Angle,A.P.,R.P.,Apparent power, Freq
                    String[] paramArr = onePhaseStend.stMeterRead().split(",");

                    U = Double.parseDouble(paramArr[0]);
                    I = Double.parseDouble(paramArr[1]);
                    UIAngle = Double.parseDouble(paramArr[2]);
                    P = Double.parseDouble(paramArr[3]);
                    Q = Double.parseDouble(paramArr[4]);
                    S = Double.parseDouble(paramArr[5]);
                    F = Double.parseDouble(paramArr[6]);
                }
            };
        }

        timer.schedule(readReferenceParamTask, 0, 2000);
    }

    public void finishReadRefMeterParameters() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        timer.cancel();
    }

    public double getU() {
        return U;
    }

    public double getI() {
        return I;
    }

    public double getUIAngle() {
        return UIAngle;
    }

    public double getP() {
        return P;
    }

    public double getQ() {
        return Q;
    }

    public double getS() {
        return S;
    }

    public double getF() {
        return F;
    }
}
