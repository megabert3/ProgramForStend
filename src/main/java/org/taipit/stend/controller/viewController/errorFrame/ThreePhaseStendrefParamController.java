package org.taipit.stend.controller.viewController.errorFrame;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.taipit.stend.controller.StendDLLCommands;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;


public class ThreePhaseStendrefParamController implements StendRefParametersForFrame {

    private StendDLLCommands stendDLLCommands;

    private double xOffset;

    private double yOffset;

    @FXML
    private TextField txtFldUA;

    @FXML
    private TextField txtFldIA;

    @FXML
    private TextField txtFldDegA;

    @FXML
    private TextField txtFldPFA;

    @FXML
    private TextField txtFldPA;

    @FXML
    private TextField txtFldQA;

    @FXML
    private TextField txtFldSA;

    @FXML
    private TextField txtFldUB;

    @FXML
    private TextField txtFldIB;

    @FXML
    private TextField txtFldDegB;

    @FXML
    private TextField txtFldPFB;

    @FXML
    private TextField txtFldPB;

    @FXML
    private TextField txtFldQB;

    @FXML
    private TextField txtFldSB;

    @FXML
    private TextField txtFldUC;

    @FXML
    private TextField txtFldIC;

    @FXML
    private TextField txtFldDegC;

    @FXML
    private TextField txtFldPFC;

    @FXML
    private TextField txtFldPC;

    @FXML
    private TextField txtFldQC;

    @FXML
    private TextField txtFldSC;

    @FXML
    private TextField txtFldF;

    @FXML
    private TextField txtFldPall;

    @FXML
    private TextField txtFldQall;

    @FXML
    private TextField txtFldSall;

    @FXML
    private TextField txtFldPFall;

    @FXML
    private TextField txtFldUaUb;

    @FXML
    private TextField txtFldUbUc;

    private Timer timer;

    private TimerTask timerTask;

    @FXML
    void initialize() {
        txtFldUA.setEditable(false);
        txtFldIA.setEditable(false);
        txtFldDegA.setEditable(false);
        txtFldPFA.setEditable(false);
        txtFldPA.setEditable(false);
        txtFldQA.setEditable(false);
        txtFldSA.setEditable(false);
        txtFldUB.setEditable(false);
        txtFldIB.setEditable(false);
        txtFldDegB.setEditable(false);
        txtFldPFB.setEditable(false);
        txtFldPB.setEditable(false);
        txtFldQB.setEditable(false);
        txtFldSB.setEditable(false);
        txtFldUC.setEditable(false);
        txtFldIC.setEditable(false);
        txtFldDegC.setEditable(false);
        txtFldPFC.setEditable(false);
        txtFldPC.setEditable(false);
        txtFldQC.setEditable(false);
        txtFldSC.setEditable(false);
        txtFldF.setEditable(false);
        txtFldPall.setEditable(false);
        txtFldQall.setEditable(false);
        txtFldSall.setEditable(false);
        txtFldPFall.setEditable(false);
        txtFldUaUb.setEditable(false);
        txtFldUbUc.setEditable(false);

    }

    public void initTimer(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;

        String typeRefMeter = stendDLLCommands.getTypeReferenceMeter();

        if (typeRefMeter.equals("HY5303C-22") || typeRefMeter.equals("SY3302")) {
            timerTask = new TimerTask() {
                String[] meterParam = new String[0];

                @Override
                public void run() {
                    meterParam = stendDLLCommands.stMeterRead().split(",");

                    if (meterParam.length != 0) {
                        //Ua,Ub,Uc,Ia,Ib,Ic,Angle_UaIa, Angle_UbIb, Angle_UcIc, Pa , Pb , Pc , Qa , Qb , Qc , Sa , Sb , Sc ,
                        //Pall(A.P.),Qall(R.P.),Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall, Angle_UaUb, Angle_UbUc
                        System.out.println(Arrays.toString(meterParam));
                        try {
                            txtFldUA.setText(meterParam[0]);
                            txtFldUB.setText(meterParam[1]);
                            txtFldUC.setText(meterParam[2]);
                            txtFldIA.setText(meterParam[3]);
                            txtFldIB.setText(meterParam[4]);
                            txtFldIC.setText(meterParam[5]);
                            txtFldDegA.setText(meterParam[6]);
                            txtFldDegB.setText(meterParam[7]);
                            txtFldDegC.setText(meterParam[8]);
                            txtFldPA.setText(meterParam[9]);
                            txtFldPB.setText(meterParam[10]);
                            txtFldPC.setText(meterParam[11]);
                            txtFldQA.setText(meterParam[12]);
                            txtFldQB.setText(meterParam[13]);
                            txtFldQC.setText(meterParam[14]);
                            txtFldSA.setText(meterParam[15]);
                            txtFldSB.setText(meterParam[16]);
                            txtFldSC.setText(meterParam[17]);
                            txtFldPall.setText(meterParam[18]);
                            txtFldQall.setText(meterParam[19]);
                            txtFldSall.setText(meterParam[20]);
                            txtFldF.setText(meterParam[21]);
                            txtFldPFA.setText(meterParam[22]);
                            txtFldPFB.setText(meterParam[23]);
                            txtFldPFC.setText(meterParam[24]);
                            txtFldPFall.setText(meterParam[25]);
                            txtFldUaUb.setText(meterParam[26]);
                            txtFldUbUc.setText(meterParam[27]);
                        } catch (ArrayIndexOutOfBoundsException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };

        } else if (typeRefMeter.equals("HS5320") || typeRefMeter.equals("SY3102")) {
            timerTask = new TimerTask() {
                String[] meterParam = new String[0];

                @Override
                public void run() {
                    meterParam = stendDLLCommands.stMeterRead().split(",");

                    if (meterParam.length != 0) {
                        //Ua,Ub,Uc,Ia,Ib,Ic, Angle _UaUb, Angle _UbUc, Angle _UaIa, Angle _UbIb, Angle _UcIc, Pa , Pb , Pc ,
                        //Pall(A.P.), Qa , Qb , Qc , Qall(R.P.),Sa , Sb , Sc ,Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall
                        txtFldUA.setText(meterParam[0]);
                        txtFldUB.setText(meterParam[1]);
                        txtFldUC.setText(meterParam[2]);
                        txtFldIA.setText(meterParam[3]);
                        txtFldIB.setText(meterParam[4]);
                        txtFldIC.setText(meterParam[5]);
                        txtFldUaUb.setText(meterParam[6]);
                        txtFldUbUc.setText(meterParam[7]);
                        txtFldDegA.setText(meterParam[8]);
                        txtFldDegB.setText(meterParam[9]);
                        txtFldDegC.setText(meterParam[10]);
                        txtFldPA.setText(meterParam[11]);
                        txtFldPB.setText(meterParam[12]);
                        txtFldPC.setText(meterParam[13]);
                        txtFldPall.setText(meterParam[14]);
                        txtFldQA.setText(meterParam[15]);
                        txtFldQB.setText(meterParam[16]);
                        txtFldQC.setText(meterParam[17]);
                        txtFldQall.setText(meterParam[18]);
                        txtFldSA.setText(meterParam[19]);
                        txtFldSB.setText(meterParam[20]);
                        txtFldSC.setText(meterParam[21]);
                        txtFldSall.setText(meterParam[22]);
                        txtFldF.setText(meterParam[23]);
                        txtFldPFA.setText(meterParam[24]);
                        txtFldPFB.setText(meterParam[25]);
                        txtFldPFC.setText(meterParam[26]);
                        txtFldPFall.setText(meterParam[27]);
                    }
                }
            };
        }

        timer = new Timer(true);

        timer.schedule(timerTask, 0, 2000);
    }

    public void addMovingActions() {
        Stage thisStage = (Stage) txtFldUA.getScene().getWindow();

        Scene thisScene = txtFldUA.getScene();

        thisStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                timer.cancel();
                thisStage.close();
            }
        });

        thisScene.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = thisStage.getX() - event.getScreenX();
                yOffset = thisStage.getY() - event.getScreenY();
            }
        });
        thisScene.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                thisStage.setX(event.getScreenX() + xOffset);
                thisStage.setY(event.getScreenY() + yOffset);
            }
        });
    }
}
