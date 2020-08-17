package org.taipit.stend.controller.viewController.errorFrame.refMeter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.Commands.ImbalansUCommand;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.stend.StendDLLCommands;

import java.util.Arrays;


public class ThreePhaseStendRefParamController implements StendRefParametersForFrame {

    private StendDLLCommands stendDLLCommands;

    private Boolean refTypeHY5303C22 = true;

    private Double currPhaseA;
    private Double currPhaseB;
    private Double currPhaseC;

    private Double UPhaseA;
    private Double UPhaseB;
    private Double UPhaseC;

    //Флаги для отображения по каким фаза ошибка
    private boolean Ua;
    private boolean Ub;
    private boolean Uc;

    private boolean Ia;
    private boolean Ib;
    private boolean Ic;

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

    public void initRefType(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;

        String typeRefMeter = stendDLLCommands.getTypeReferenceMeter();
        if (typeRefMeter.equals("HY5303C-22") || typeRefMeter.equals("SY3302")) {
            refTypeHY5303C22 = true;
        } else if (typeRefMeter.equals("HS5320") || typeRefMeter.equals("SY3102")) {
            refTypeHY5303C22 = false;
        }
    }

    public void readParameters() throws InterruptedException {
        String[] meterParam = stendDLLCommands.stMeterRead().split(",");
        System.out.println(Arrays.toString(meterParam));

        try {
            if (meterParam.length == 28) {
                if (refTypeHY5303C22) {

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

                    equalsParan(meterParam[0], meterParam[1], meterParam[2],
                            meterParam[3], meterParam[4], meterParam[5]);

                } else {
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

                    equalsParan(meterParam[0], meterParam[1], meterParam[2],
                            meterParam[3], meterParam[4], meterParam[5]);
                }
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    private void equalsParan(String Ua, String Ub, String Uc, String Ia, String Ib, String Ic) throws InterruptedException {
        double value;
        boolean b = false;

        value = Double.parseDouble(Ua);
        if (value < UPhaseA - (UPhaseA * 0.2)) {
            this.Ua = true;
            b = true;
        }

        value = Double.parseDouble(Ub);
        if (value < UPhaseB - (UPhaseB * 0.2)) {
            this.Ub = true;
            b = true;
        }

        value = Double.parseDouble(Uc);
        if (value < UPhaseC - (UPhaseC * 0.2)) {
            this.Uc = true;
            b = true;
        }

        value = Double.parseDouble(Ia);
        if (value < currPhaseA - (currPhaseA * 0.2)) {
            this.Ia = true;
            b = true;
        }

        value = Double.parseDouble(Ib);
        if (value < currPhaseB - (currPhaseB * 0.2)) {
            this.Ib = true;
            b = true;
        }

        value = Double.parseDouble(Ic);
        if (value < currPhaseC - (currPhaseC * 0.2)) {
            this.Ic = true;
            b = true;
        }

        if (b) {
            StringBuilder stringBuilder = new StringBuilder("Авария по цепи(-ям)\n");
            if (this.Ua) {
                stringBuilder.append("Ua ");
            }

            if (this.Ub) {
                stringBuilder.append("Ub ");
            }

            if (this.Uc) {
                stringBuilder.append("Uc ");
            }

            if (this.Ia) {
                stringBuilder.append("Ia ");
            }

            if (this.Ib) {
                stringBuilder.append("Ib ");
            }

            if (this.Ic) {
                stringBuilder.append("Ic ");
            }

            ConsoleHelper.infoException(stringBuilder.toString());

            this.Ua = false;
            this.Ub = false;
            this.Uc = false;

            this.Ia = false;
            this.Ib = false;
            this.Ic = false;

            stendDLLCommands.errorClear();
            stendDLLCommands.powerOf();

            TestErrorTableFrameController.blockBtns.setValue(false);

            throw new InterruptedException(stringBuilder.toString());
        }
    }



    @Override
    public void transferParameters(Commands command) {
        double current;
        double U;

        if (command instanceof ImbalansUCommand) {

            current = command.getRatedCurr() * command.getCurrPer();
            UPhaseA = command.getRatedVolt() * command.getVoltPerA();
            UPhaseB = command.getRatedVolt() * command.getVoltPerB();
            UPhaseC = command.getRatedVolt() * command.getVoltPerC();

            currPhaseA = current;
            currPhaseB = current;
            currPhaseC = current;

        } else {

            if (command.isThreePhaseCommand()) {
                current = command.getRatedCurr() * (command.getCurrPer() / 100);
                U = command.getRatedVolt() * (command.getVoltPer() / 100);

                UPhaseA = U;
                UPhaseB = U;
                UPhaseC = U;

                switch (command.getiABC()) {
                    case "H": {
                        currPhaseA = current;
                        currPhaseB = current;
                        currPhaseC = current;
                    }break;
                    case "A": {
                        currPhaseA = current;
                        currPhaseB = 0.0;
                        currPhaseC = 0.0;
                    }break;
                    case "B": {
                        currPhaseA = 0.0;
                        currPhaseB = current;
                        currPhaseC = 0.0;
                    }break;
                    case "C": {
                        currPhaseA = 0.0;
                        currPhaseB = 0.0;
                        currPhaseC = current;
                    }break;
                }

            } else {
                String phase = ConsoleHelper.properties.getProperty("phaseOnOnePhaseMode");

                current = command.getRatedCurr() * command.getCurrPer();
                U = command.getRatedVolt() * command.getRatedVolt();

                switch (phase) {
                    case "A": {
                        currPhaseA = current;
                        currPhaseA = 0.0;
                        currPhaseA = 0.0;

                        UPhaseA = U;
                        UPhaseB = 0.0;
                        UPhaseC = 0.0;
                    }break;
                    case "B": {
                        currPhaseA = 0.0;
                        currPhaseA = current;
                        currPhaseA = 0.0;

                        UPhaseA = 0.0;
                        UPhaseB = U;
                        UPhaseC = 0.0;
                    }break;
                    case "C": {
                        currPhaseA = 0.0;
                        currPhaseA = 0.0;
                        currPhaseA = current;

                        UPhaseA = 0.0;
                        UPhaseB = 0.0;
                        UPhaseC = U;
                    }break;
                }
            }
        }
    }

    public void transferParameters(Double Ua, Double Ub, Double Uc, Double Ia, Double Ib, Double Ic) {
        UPhaseA = Ua;
        UPhaseB = Ub;
        UPhaseC = Uc;

        currPhaseA = Ia;
        currPhaseB = Ib;
        currPhaseC = Ic;
    }

    public void addMovingActions() {
        Stage thisStage = (Stage) txtFldUA.getScene().getWindow();

        Scene thisScene = txtFldUA.getScene();

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
