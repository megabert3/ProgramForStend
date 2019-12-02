package stend.controller;


import stend.helper.ConsoleHelper;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;

public class MainStend {
    public static void main(String[] args) {
        // Включить напряжение и ток
// Phase - Режим:
// 		0 - Однофазный
//		1 - Трех-фазный четырех-проводной
//		2 - Трех-фазный трех-проводной
// 		3 - Трех-фазный трех-проводной реактив 90 градусов
// 		4 - Трех-фазный трех-проводной реактив 60 градусов
//		5 - Трех-фазный четырех-проводной (реактив)
//		6 - Трех-фазный трех-проводной (реактив)
//		7 - Однофазный реактив
// Rated_Volt - напряжение
// Rated_Curr - ток
// Rated_Freq - частота
// PhaseSrequence - чередование фаз
//		0 - Прямое
//		1 - Обратное
// Revers - направление тока
//		0 - Прямой
//		1 - Обратный
// Volt_Per - Процент по напряжению (0- 100)
// Curr_Per - Процент по току (0-100)
// IABC - строка, определяющая фазы, по которым пустить ток: A, B, C, H - все
// CosP - строка  с косинусом угла. Например: "1.0", "0.5L", "0.8C"
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза)
//		TC-3000D (3 фазы)
// Dev_Port - номер com-порта
        StendDLLCommands stendDLLCommands = new StendDLLCommands(9, "HY5303C-22");
        ErrorCommand errorCommand = new ErrorCommand(stendDLLCommands, 1, 230.0, 5.0, 50.0, 0,
                0, 100.0, 100.0, "H", "1.0", 0, 10);
        try {
            errorCommand.execute();
            for (double[] errMass : errorCommand.getErrorList()) {
                ConsoleHelper.getMessage(Arrays.toString(errMass));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
