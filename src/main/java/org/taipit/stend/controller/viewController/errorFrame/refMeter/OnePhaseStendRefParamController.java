package org.taipit.stend.controller.viewController.errorFrame.refMeter;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.taipit.stend.controller.Commands.Commands;
import org.taipit.stend.controller.viewController.errorFrame.TestErrorTableFrameController;
import org.taipit.stend.helper.ConsoleHelper;
import org.taipit.stend.model.stend.StendDLLCommands;

import java.util.Arrays;

/**
 * @autor Albert Khalimov
 *
 * Данный класс отвечает за отображение параметров полученных от эталонного счётчика однофазного стенда в GUI"
 */
public class OnePhaseStendRefParamController implements StendRefParametersForFrame {

    //Обект типа стенда (однофазный)
    private StendDLLCommands stendDLLCommands;

    //Тип эталонного счётчика (их много, но порядок следования параметров разный в двух случаях)
    //Если тип HY5303C22
    private Boolean refTypeHY5303C22 = false;

    //Наряжение
    private Double U;

    //Ток
    private Double I;

    //Флаги для отображения в GUI по каким фаза ошибка
    //Ошибка по цепи напряжения
    private boolean Uerr;
    //Ошибка по цепи тока
    private boolean Ierr;

    //Позиция смещения окна параметров в GUI
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

    //Делаю поля отображения только для чтения
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

    /**
     * Инициализирует тип эталонного счётчика установок
     * от этого зависит какой порядок параметров передаёт стенд и в каком порядке
     * их необходимо отображать в GUI
     * @param stendDLLCommands
     */
    public void initRefType(StendDLLCommands stendDLLCommands) {
        this.stendDLLCommands = stendDLLCommands;

        String typeRefMeter = stendDLLCommands.getTypeReferenceMeter();

        if (typeRefMeter.equals("HY5101C-23") || typeRefMeter.equals("SY3803")) {
            refTypeHY5303C22 = true;
        }
    }

    /**
     * Получает значения параметров от эталонного счётчика
     * парсит их и выводит в GUI
     * @throws InterruptedException
     */
    public void readParameters() throws InterruptedException {
        String[] meterParam = stendDLLCommands.stMeterRead().split(",");
        // Пример возвращаемой строки от эталонного счётчика:
        // U,I,UI_Angle,A.P.,R.P.,Apparent power, Freq
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
                equalsParan(meterParam[0], meterParam[1]);
            }
        }catch (ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    /**
     * Убрать
     */
    @Override
    public void readParametersWithoutCheckingParan() {
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


    /**
     * УБРАНО, ОСТАВЛЕННО ДЛЯ ИНФОРМАЦИИ И ВОЗМОЖНО ДЛЯ ДАЛЬНЕЙШЕЙ РЕАЛИЗАЦИИ
     *
     * Передаёт параметры, которые должны быть выставлены установкой если эталонный счётчик передаёт не те, значит ошибка
     */
    @Override
    public void transferParameters(Commands command) {
        I = command.getRatedCurr() * (command.getCurrPer() / 100);
        U = command.getRatedVolt() * (command.getVoltPer() / 100);
    }
    public void transferParameters(Double U, Double I) {
        /**
         Добавить проверку с максимально допустимыми возможностями установки и если что выкинуть исключение
         */
        this.U = U;
        this.I = I;
    }

    /**
     * УБРАНО, ОСТАВЛЕННО ДЛЯ ИНФОРМАЦИИ И ВОЗМОЖНО ДЛЯ ДАЛЬНЕЙШЕЙ РЕАЛИЗАЦИИ
     *
     * Сравнивает значения полученные от эталонного счётчика с теми, которые должны быть высталенны для испытаний
     * если они не соответствуют значит сработала авария по току или напряжению
     */
    private void equalsParan(String U, String I) throws InterruptedException {
        double value;
        boolean b = false;

        if (this.U != 0) {
            value = Double.parseDouble(U);
            if (value < this.U - (this.U * 0.2)) {
                this.Uerr = true;
                b = true;
            }
        }

        if (this.I != 0) {
            value = Double.parseDouble(I);
            if (value < this.I - (this.I * 0.2)) {
                this.Ierr = true;
                b = true;
            }
        }

        if (b) {
            StringBuilder stringBuilder = new StringBuilder("Авария по цепи(-ям)\n");
            if (this.Uerr) {
                stringBuilder.append("U ");
            }

            if (this.Ierr) {
                stringBuilder.append("I ");
            }

            ConsoleHelper.infoException(stringBuilder.toString());

            this.Ierr = false;
            this.Uerr = false;

            TestErrorTableFrameController.getStaticBtnStop().fire();

            throw new InterruptedException(stringBuilder.toString());
        }
    }

    /**
     * Передвигает окно параметров эталонного счётчика
     * по нажатию ЛКМ на него
     */
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
