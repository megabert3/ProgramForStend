package org.taipit.stend.controller.viewController.errorFrame.refMeter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.taipit.stend.controller.сommands.Commands;
import org.taipit.stend.controller.сommands.ImbalansUCommand;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.helper.exeptions.StendConnectionException;
import org.taipit.stend.model.stend.StendDLLCommands;

/**
 * @autor Albert Khalimov
 * Данный класс является контроллером окна "threePhaseStendrefParamFrame.fxml".
 * Данный класс отвечает за отображение параметров полученных от эталонного счётчика трехфазного стенда в GUI"
 */
public class ThreePhaseStendRefParamController implements StendRefParametersForFrame {

    //Обект типа стенда (трехфазный)
    private StendDLLCommands stendDLLCommands;

    //Тип эталонного счётчика (их много, но порядок следования параметров разный в двух случаях)
    //Если тип HY5303C22
    private Boolean refTypeHY5303C22 = true;

    //Значение тока по фазам
    private Double currPhaseA;
    private Double currPhaseB;
    private Double currPhaseC;

    //Значение напряжения по фазам
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

    //Позиция смещения окна параметров в GUI
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

    //Делаю поля отображения только для чтения
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

    /**
     * Инициализирует тип эталонного счётчика установок
     * от этого зависит какой порядок параметров передаёт стенд и в каком порядке
     * их необходимо отображать в GUI
     * @param stendDLLCommands
     */
    public void initRefType(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;

        String typeRefMeter = stendDLLCommands.getTypeReferenceMeter();

        if (typeRefMeter.equals("HY5303C-22") || typeRefMeter.equals("SY3302")) {
            refTypeHY5303C22 = true;

        } else if (typeRefMeter.equals("HS5320") || typeRefMeter.equals("SY3102")) {
            refTypeHY5303C22 = false;
        }
    }

    /**
     * Получает значения параметров от эталонного счётчика
     * парсит их и выводит в GUI
     * @throws InterruptedException
     */
    public void readParametersWithoutCheckingParan() {

        try {
            String[] meterParam = stendDLLCommands.stMeterRead().split(",");

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
                }
            }
        }catch (StendConnectionException ignored) {}
    }

    /**
     * Получает значения параметров от эталонного счётчика
     * парсит их и выводит в GUI
     * @throws InterruptedException
     */
    public void readParameters() {
        try {
            String[] meterParam = stendDLLCommands.stMeterRead().split(",");

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
                }

                //equalsParan(meterParam[0], meterParam[1], meterParam[2],
                //        meterParam[3], meterParam[4], meterParam[5]);
            }

        }catch (ArrayIndexOutOfBoundsException | NullPointerException | StendConnectionException ignored) {}
    }

    /**
     * УБРАНО, ОСТАВЛЕННО ДЛЯ ИНФОРМАЦИИ И ВОЗМОЖНО ДЛЯ ДАЛЬНЕЙШЕЙ РЕАЛИЗАЦИИ
     *
     * Сравнивает значения полученные от эталонного счётчика с теми, которые должны быть высталенны для испытаний
     * если они не соответствуют значит сработала авария по току или напряжению
     * @param Ua
     * @param Ub
     * @param Uc
     * @param Ia
     * @param Ib
     * @param Ic
     * @throws InterruptedException
     */
    private void equalsParan(String Ua, String Ub, String Uc, String Ia, String Ib, String Ic) throws InterruptedException {
        double value;
        boolean b = false;

        try {
            if (UPhaseA != 0) {
                value = Double.parseDouble(Ua);
                if (value < UPhaseA - (UPhaseA * 0.2)) {
                    this.Ua = true;
                    b = true;
                }
            }

            if (UPhaseB != 0) {
                value = Double.parseDouble(Ub);
                if (value < UPhaseB - (UPhaseB * 0.2)) {
                    this.Ub = true;
                    b = true;
                }
            }

            if (UPhaseC != 0) {
                value = Double.parseDouble(Uc);
                if (value < UPhaseC - (UPhaseC * 0.2)) {
                    this.Uc = true;
                    b = true;
                }
            }

            if (currPhaseA != 0) {
                value = Double.parseDouble(Ia);
                if (value < currPhaseA - (currPhaseA * 0.2)) {
                    this.Ia = true;
                    b = true;
                }
            }

            if (currPhaseB != 0) {
                value = Double.parseDouble(Ib);
                if (value < currPhaseB - (currPhaseB * 0.2)) {
                    this.Ib = true;
                    b = true;
                }
            }

            if (currPhaseC != 0) {
                value = Double.parseDouble(Ic);
                if (value < currPhaseC - (currPhaseC * 0.2)) {
                    this.Ic = true;
                    b = true;
                }
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

                TestErrorTableFrameController.getStaticBtnStop().fire();

                throw new InterruptedException(stringBuilder.toString());
            }
        }catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }

    /**
     * УБРАНО, ОСТАВЛЕННО ДЛЯ ИНФОРМАЦИИ И ВОЗМОЖНО ДЛЯ ДАЛЬНЕЙШЕЙ РЕАЛИЗАЦИИ
     *
     * Передаёт параметры, которые должны быть выставлены установкой если эталонный счётчик передаёт не те, значит ошибка
     * @param command
     */
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

                current = command.getRatedCurr() * (command.getCurrPer() / 100);
                U = command.getRatedVolt() * (command.getVoltPer() / 100);

                switch (phase) {
                    case "A": {
                        currPhaseA = current;
                        currPhaseB = 0.0;
                        currPhaseC = 0.0;

                        UPhaseA = U;
                        UPhaseB = 0.0;
                        UPhaseC = 0.0;
                    }break;
                    case "B": {
                        currPhaseA = 0.0;
                        currPhaseB = current;
                        currPhaseC = 0.0;

                        UPhaseA = 0.0;
                        UPhaseB = U;
                        UPhaseC = 0.0;
                    }break;
                    case "C": {
                        currPhaseA = 0.0;
                        currPhaseB = 0.0;
                        currPhaseC = current;

                        UPhaseA = 0.0;
                        UPhaseB = 0.0;
                        UPhaseC = U;
                    }break;
                }
            }
        }
    }
    public void transferParameters(Double Ua, Double Ub, Double Uc, Double Ia, Double Ib, Double Ic) {
        /**
         Добавить проверку с максимально допустимыми возможностями установки и если что выкинуть исключение
         */
        UPhaseA = Ua;
        UPhaseB = Ub;
        UPhaseC = Uc;

        currPhaseA = Ia;
        currPhaseB = Ib;
        currPhaseC = Ic;
    }

    /**
     * Передвигает окно параметров эталонного счётчика
     * по нажатию ЛКМ на него
     */
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
