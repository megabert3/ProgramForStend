package org.taipit.stend.model.refMeter;

import org.taipit.stend.controller.StendDLLCommands;
import org.taipit.stend.controller.ThreePhaseStend;
import org.taipit.stend.helper.ConsoleHelper;

import java.util.Timer;
import java.util.TimerTask;

public class ThreePhaseRefMeterParameters {
/*
Трех-фазное подключение:
HY5303C-22, SY3302
Ua,Ub,Uc,Ia,Ib,Ic,Angle_UaIa, Angle_UbIb, Angle_UcIc, Pa , Pb , Pc , Qa , Qb , Qc , Sa , Sb , Sc ,
Pall(A.P.),Qall(R.P.),Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall, Angle_UaUb, Angle_UbUc
HS5320, SY3102
Ua,Ub,Uc,Ia,Ib,Ic, Angle _UaUb, Angle _UaUc, Angle _UaIa, Angle _UbIb, Angle _UcIc, Pa , Pb , Pc ,
Pall(A.P.), Qa , Qb , Qc , Qall(R.P.),Sa , Sb , Sc ,Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall
TC-3000D?
Ua , Ub , Uc , Ia , Ib , Ic , UI_Angle_a , UI_Angle_b , UI_Angle_c , Pa , Pb , Pc , Qa , Qb , Qc ,
Sa , Sb , Sc , Pall(A.P.) ,Qall( R.P.) ,Sall( Apparent power) , Freq , U_Range , I_Range
*/

    private StendDLLCommands threePhaseStend = ThreePhaseStend.getThreePhaseStendInstance();

    private double Ua;
    private double Ub;
    private double Uc;

    private double Ia;
    private double Ib;
    private double Ic;

    private double angleUaIa;
    private double angleUbIb;
    private double angleUcIc;

    private double Pa;
    private double Pb;
    private double Pc;

    private double Qa;
    private double Qb;
    private double Qc;

    private double Sa;
    private double Sb;
    private double Sc;

    private double Pall;
    private double Qall;
    private double Sall;

    private double F;

    private double PFa;
    private double PFb;
    private double PFc;
    private double PFall;

    private double angleUaUb;
    private double angleUbUc;

    private Timer timer;

    private TimerTask readReferenceParamTask;

    public void startReadRefMeterParameters() {

        String refMeterType = ConsoleHelper.properties.getProperty("refMeterModel");

        timer = new Timer(true);

        if (refMeterType.equals("HY5303C-22") || refMeterType.equals("SY3302")) {
            readReferenceParamTask = new TimerTask() {
                @Override
                public void run() {
                    String[] paramArr = threePhaseStend.stMeterRead().split(",");
                    //Ua,Ub,Uc,Ia,Ib,Ic,Angle_UaIa, Angle_UbIb, Angle_UcIc, Pa , Pb , Pc , Qa , Qb , Qc , Sa , Sb , Sc ,
                    //Pall(A.P.),Qall(R.P.),Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall, Angle_UaUb, Angle_UbUc

                    Ua = Double.parseDouble(paramArr[0]);
                    Ub = Double.parseDouble(paramArr[1]);
                    Uc = Double.parseDouble(paramArr[2]);

                    Ia = Double.parseDouble(paramArr[3]);
                    Ib = Double.parseDouble(paramArr[4]);
                    Ic = Double.parseDouble(paramArr[5]);

                    angleUaIa = Double.parseDouble(paramArr[6]);
                    angleUbIb = Double.parseDouble(paramArr[7]);
                    angleUcIc = Double.parseDouble(paramArr[8]);

                    Pa = Double.parseDouble(paramArr[9]);
                    Pb = Double.parseDouble(paramArr[10]);
                    Pc = Double.parseDouble(paramArr[11]);

                    Qa = Double.parseDouble(paramArr[12]);
                    Qb = Double.parseDouble(paramArr[13]);
                    Qc = Double.parseDouble(paramArr[14]);

                    Sa = Double.parseDouble(paramArr[15]);
                    Sb = Double.parseDouble(paramArr[16]);
                    Sc = Double.parseDouble(paramArr[17]);

                    Pall = Double.parseDouble(paramArr[18]);
                    Qall = Double.parseDouble(paramArr[19]);
                    Sall = Double.parseDouble(paramArr[20]);

                    F = Double.parseDouble(paramArr[21]);

                    PFa = Double.parseDouble(paramArr[22]);
                    PFb = Double.parseDouble(paramArr[23]);
                    PFc = Double.parseDouble(paramArr[24]);
                    PFall = Double.parseDouble(paramArr[25]);

                    angleUaUb = Double.parseDouble(paramArr[26]);
                    angleUbUc = Double.parseDouble(paramArr[27]);
                }
            };

        } else if (refMeterType.equals("HS5320") || refMeterType.equals("SY3102")) {
            readReferenceParamTask = new TimerTask() {
                @Override
                public void run() {
                    String[] paramArr = threePhaseStend.stMeterRead().split(",");
                    //Ua,Ub,Uc,Ia,Ib,Ic, Angle _UaUb, Angle _UaUc, Angle _UaIa, Angle _UbIb, Angle _UcIc, Pa , Pb , Pc ,
                    //Pall(A.P.), Qa , Qb , Qc , Qall(R.P.),Sa , Sb , Sc ,Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall

                    Ua = Double.parseDouble(paramArr[0]);
                    Ub = Double.parseDouble(paramArr[1]);
                    Uc = Double.parseDouble(paramArr[2]);

                    Ia = Double.parseDouble(paramArr[3]);
                    Ib = Double.parseDouble(paramArr[4]);
                    Ic = Double.parseDouble(paramArr[5]);

                    angleUaUb = Double.parseDouble(paramArr[6]);
                    angleUbUc = Double.parseDouble(paramArr[7]);

                    angleUaIa = Double.parseDouble(paramArr[8]);
                    angleUbIb = Double.parseDouble(paramArr[9]);
                    angleUcIc = Double.parseDouble(paramArr[10]);

                    Pa = Double.parseDouble(paramArr[11]);
                    Pb = Double.parseDouble(paramArr[12]);
                    Pc = Double.parseDouble(paramArr[13]);

                    Pall = Double.parseDouble(paramArr[14]);

                    Qa = Double.parseDouble(paramArr[15]);
                    Qb = Double.parseDouble(paramArr[16]);
                    Qc = Double.parseDouble(paramArr[17]);

                    Qall = Double.parseDouble(paramArr[18]);

                    Sa = Double.parseDouble(paramArr[19]);
                    Sb = Double.parseDouble(paramArr[20]);
                    Sc = Double.parseDouble(paramArr[21]);

                    Sall = Double.parseDouble(paramArr[22]);

                    F = Double.parseDouble(paramArr[23]);

                    PFa = Double.parseDouble(paramArr[24]);
                    PFb = Double.parseDouble(paramArr[25]);
                    PFc = Double.parseDouble(paramArr[26]);
                    PFall = Double.parseDouble(paramArr[27]);
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

    public double getUb() {
        return Ub;
    }

    public double getUc() {
        return Uc;
    }

    public double getIa() {
        return Ia;
    }

    public double getIb() {
        return Ib;
    }

    public double getIc() {
        return Ic;
    }

    public double getAngleUaIa() {
        return angleUaIa;
    }

    public double getAngleUbIb() {
        return angleUbIb;
    }

    public double getAngleUcIc() {
        return angleUcIc;
    }

    public double getPa() {
        return Pa;
    }

    public double getPb() {
        return Pb;
    }

    public double getPc() {
        return Pc;
    }

    public double getQa() {
        return Qa;
    }

    public double getQb() {
        return Qb;
    }

    public double getQc() {
        return Qc;
    }

    public double getSa() {
        return Sa;
    }

    public double getSb() {
        return Sb;
    }

    public double getSc() {
        return Sc;
    }

    public double getPall() {
        return Pall;
    }

    public double getQall() {
        return Qall;
    }

    public double getSall() {
        return Sall;
    }

    public double getF() {
        return F;
    }

    public double getPFa() {
        return PFa;
    }
}
