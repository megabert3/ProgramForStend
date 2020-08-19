package org.taipit.stend.controller.viewController.errorFrame.refMeter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.model.stend.StendDLLCommands;

import java.util.Arrays;


public class OnePhaseStendRefParamController implements StendRefParametersForFrame {

    private StendDLLCommands stendDLLCommands;

    private Boolean refTypeHY5303C22 = null;

    private double xOffset;

    private double yOffset;

    @FXML
    private TextField txtFldU;

    @FXML
    private TextField txtFldI;

    @FXML
    private TextField txtFldP;

    @FXML
    private TextField txtFldQ;

    @FXML
    private TextField txtFldS;

    @FXML
    private TextField txtFldF;

    @FXML
    private TextField txtFldDeg;

/*
Однофазное подключение:
HY5101C-22(Simulation Meter) : U,I,UI_Angle
HY5101C-23?SY3803 : U,I,UI_Angle,A.P.,R.P.,Apparent power, Freq
TC-3000C? Ua , Ub , Uc , Ia , Ib , Ic , UI_Angle_a , UI_Angle_b , UI_Angle_c , Pa , Pb , Pc , Qa ,
Qb , Qc , Sa , Sb , Sc , A.P. , R.P. , Apparent power , Freq , I_Range
*/

    @FXML
    void initialize() {
        txtFldU.setEditable(false);
        txtFldI.setEditable(false);
        txtFldDeg.setEditable(false);
        txtFldP.setEditable(false);
        txtFldQ.setEditable(false);
        txtFldS.setEditable(false);
        txtFldF.setEditable(false);
    }

    public void initRefType(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;

        String typeRefMeter = stendDLLCommands.getTypeReferenceMeter();

        if (typeRefMeter.equals("HY5101C-23") || typeRefMeter.equals("SY3803")) {
            refTypeHY5303C22 = true;
        }
    }

    public void readParameters() {
        String[] meterParam = stendDLLCommands.stMeterRead().split(",");
        //U,I,UI_Angle,A.P.,R.P.,Apparent power, Freq
        System.out.println(Arrays.toString(meterParam));

        try {
            if (meterParam.length == 7) {
                if (refTypeHY5303C22) {
                    txtFldU.setText(meterParam[0]);
                    txtFldI.setText(meterParam[1]);
                    txtFldDeg.setText(meterParam[2]);
                    txtFldP.setText(meterParam[3]);
                    txtFldQ.setText(meterParam[4]);
                    txtFldS.setText(meterParam[5]);
                    txtFldF.setText(meterParam[6]);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void transferParameters(Commands command) {

    }

    public void addMovingActions() {
        Stage thisStage = (Stage) txtFldU.getScene().getWindow();

        Scene thisScene = txtFldU.getScene();

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

    @Override
    public StendRefParametersForFrame getStendRefParametersForFrame() {
        return this;
    }
}