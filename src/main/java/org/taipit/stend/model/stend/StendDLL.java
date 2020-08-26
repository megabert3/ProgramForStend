package org.taipit.stend.model.stend;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.DoubleByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;


public interface StendDLL extends Library {
    StendDLL INSTANCE = Native.load(("hscom.dll"), StendDLL.class);

//--------------------------------------------------------------------------- Adjust_UI
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
// Volt_Per - Процент по напряжению (0 - 100)
// Curr_Per - Процент по току (0 - 100)
// IABC - строка, определяющая фазы, по которым пустить ток: A, B, C, H - все
// CosP - строка  с косинусом угла. Например: "1.0", "0.5L", "0.8C"
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза)
//		TC-3000D (3 фазы)
// Dev_Port - номер com-порта
    boolean Adjust_UI(int Phase,
                      double Rated_Volt,
                      double Rated_Curr,
                      double Rated_Freq,
                      int PhaseSrequence,
                      int Revers,
                      double Volt_Per,
                      double Curr_Per,
                      String IABC,
                      String CosP,
                      String SModel,
                      int Dev_Port);

//--------------------------------------------------------------------------- Adjust_UI1
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
// Revers - направлениетока
//		0 - Прямой
//		1 - Обратный
// Volt_PerA - Процент по напряжению фазы А(0- 100)
// Volt_PerВ - Процент по напряжению фазы B(0- 100)
// Volt_PerС - Процент по напряжению фазы C(0- 100)
// Curr_Per - Процент по току (0-100)
// IABC - строка, определяющая фазы, по которым пустить ток: A, B, C, H - все
// CosP - строка  с кросинусом угла. Например: "1.0", "0.5L", "0.8C"
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза интегрированный)
//		TC-3000D (3 фазы интегрированный)
// Dev_Port - номер com-порта
    boolean Adjust_UI1(int Phase,
                       double Rated_Volt,
                       double Rated_Curr,
                       double Rated_Freq,
                       int PhaseSrequence,
                       int Revers,
                       double Volt_PerA,
                       double Volt_PerB,
                       double Volt_PerC,
                       double Curr_Per,
                       String IABC,
                       String CosP,
                       String SModel,
                       int Dev_Port);

//--------------------------------------------------------------------------- StdMeter_Read
// Получить данные со счетчика
// SData - указатель на указатель на буфер для записи данных счетчика.
// В него записываются данные, разделенные запятыми. Должен быть не менее 1024 байт
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза интегрированный)
//		TC-3000D (3 фазы интегрированный)
// Dev_Port - номер com-порта
// В ответ будет строка, в которой в зависимости от типа счетчика будут данные:
/*
Трех-фазное подключение:
HY5303C-22, SY3302
Ua,Ub,Uc,Ia,Ib,Ic,Angle_UaIa, Angle_UbIb, Angle_UcIc, Pa , Pb , Pc , Qa , Qb , Qc , Sa , Sb , Sc ,
Pall(A.P.),Qall(R.P.),Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall, Angle_UaUb, Angle_UbUc
HS5320, SY3102
Ua,Ub,Uc,Ia,Ib,Ic, Angle _UaUb, Angle _UaUc, Angle _UaIa, Angle _UaIb, Angle _UaIc, Pa , Pb , Pc ,
Pall(A.P.), Qa , Qb , Qc , Qall(R.P.),Sa , Sb , Sc ,Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall
TC-3000D?
Ua , Ub , Uc , Ia , Ib , Ic , UI_Angle_a , UI_Angle_b , UI_Angle_c , Pa , Pb , Pc , Qa , Qb , Qc ,
Sa , Sb , Sc , Pall(A.P.) ,Qall( R.P.) ,Sall( Apparent power) , Freq , U_Range , I_Range
Однофазное подключение:
HY5101C-22(Simulation Meter) : U,I,UI_Angle
HY5101C-23?SY3803 : U,I,UI_Angle,A.P.,R.P.,Apparent power, Freq
TC-3000C? Ua , Ub , Uc , Ia , Ib , Ic , UI_Angle_a , UI_Angle_b , UI_Angle_c , Pa , Pb , Pc , Qa ,
Qb , Qc , Sa , Sb , Sc , A.P. , R.P. , Apparent power , Freq , I_Range
*/
    boolean StdMeter_Read(PointerByReference SData,
                          String SModel,
                          int Dev_Port);

//--------------------------------------------------------------------------- Error_Read
// Получить ошибку навешенного счетчика
// MError - сюда будет помещена строка с ошибкой в виде Кол-во, значение
// Должен быть указатель на указатель. Размер не менее 1024 байт
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean Error_Read(PointerByReference MError,
                       int Meter_No,
                       int Dev_Port);

//--------------------------------------------------------------------------- Error_Start
// Запустить проверку навешенного счетчика (напряжение и ток должны быть включены)
// Meter_No - номер места
// Constant - постоянная
// Pulse - Усреднение
// Dev_Port - номер com-порта
    boolean Error_Start(int Meter_No,
                        double Constant,
                        int Pulse,
                        int Dev_Port);

//--------------------------------------------------------------------------- Set_Pulse_Channel
// Установка режима импульсов
// Meter_No - номер места
// ChannelFlag - Режим импульсов:
//		0 - Активная мощность (+)
// 		1 - Активная мощность (-)
// 		2 - Реактивная мощность (+)
// 		3 - Реактивная мощность (-)
// Dev_Port - номер com-порта
    boolean Set_Pulse_Channel(int Meter_No,
                              int ChannelFlag,
                              int Dev_Port);

//--------------------------------------------------------------------------- Set_485_Channel
// Установка режима 485 интерфейса
// Meter_No - номер места
// Open_Flag - состояние интерфейса:
// 		0 - выключен
//		1 - включен
// Dev_Port - номер com-порта
    boolean Set_485_Channel(int Meter_No,
                            int OpenFlag,
                            int Dev_Port);

//--------------------------------------------------------------------------- Search_mark
// Поиск метки
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean Search_mark(int Meter_No,
                        int Dev_Port);

//--------------------------------------------------------------------------- CRPSTA_start
// Старт CRPSTA
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean CRPSTA_start(int Meter_No,
                         int Dev_Port);

//--------------------------------------------------------------------------- CRPSTA_clear
// Очистка CRPSTA
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean CRPSTA_clear(int Meter_No,
                         int Dev_Port);

//--------------------------------------------------------------------------- Search_mark_Result
// результат поиска метки
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean Search_mark_Result(int Meter_No,
                               int Dev_Port);

//--------------------------------------------------------------------------- CRPSTA_Result
// результат CRPSTA
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean CRPSTA_Result(int Meter_No,
                          int Dev_Port);

//--------------------------------------------------------------------------- Error_Clear
// Сброс всех ошибок
// Dev_Port - номер com-порта
    boolean Error_Clear(int Dev_Port);

//--------------------------------------------------------------------------- Power_Off
// Выключение напряжения и тока (кнопка Стоп)
// Dev_Port - номер com-порта
    boolean Power_Off(int Dev_Port);

//--------------------------------------------------------------------------- Power_Pause
// Выключение нагрузки (тока)
// Dev_Port - номер com-порта
    boolean Power_Pause(int Dev_Port);

//--------------------------------------------------------------------------- Read_SerialNumber
// Чтение серийного номера.
// Meter_SerialNumber - место под результат
// Meter_No - номер места
// Meter_Port - номер com-порта счетчика
// Функция для внутреннего использования. Не предназначена для пользовательского ПО
    boolean Read_SerialNumber(String Meter_SerialNumber,
                              int Meter_No,
                              int Meter_Port);

//--------------------------------------------------------------------------- ConstTest_Start
// Старт теста констант
// Meter_No - номер места
// Constant - постоянная
// Dev_Port - номер com-порта
    boolean ConstTest_Start(int Meter_No,
                            double Constant,
                            int Dev_Port);

//--------------------------------------------------------------------------- ConstPulse_Read
// Чтение данных по энергии
// MeterKWH - значения счетчика
// StdKWH - Значение эталонника
// Constant - постоянная
// Meter_No - номер места
// Dev_Port - номер com-порта
    boolean ConstPulse_Read(DoubleByReference MeterKWH,
                            DoubleByReference StdKWH,
                            double Constant,
                            int Meter_No,
                            int Dev_Port);

//--------------------------------------------------------------------------- SelectCircuit
// Выбор цепи
// Circuit:
//	 	0 A
//      1 B
//		2 AB
// Dev_Port - номер com-порта
    boolean SelectCircuit(int Circuit,
                          int Dev_Port);

//--------------------------------------------------------------------------- CutNeutral
// Отключить нейтраль
// CuttingFlag:
//		0 - Отключить
//		1 - Включить
// Dev_Port - номер com-порта
    boolean CutNeutral(int CuttingFlag,
                       int Dev_Port);

//--------------------------------------------------------------------------- SetRefClock
//Переключить с эталонного счётчика на блок точности хода часов
//SetFlag:
//0--OFF (After testing)
//1—ON (Before testing)
// Dev_Port - номер com-порта
    boolean SetRefClock(int SetFlag,
                        int Dev_Port);

//--------------------------------------------------------------------------- Clock_Error_Start
// Старт теста ТХЧ
// Meter_No - номер счетчика (нумерация с 1)
// Freq - частота в герцах. Для большинства случаев 1
// Duration - длительность замера в количестве импульсов Freq
// Dev_Port - номер com-порта
    boolean Clock_Error_Start(int Meter_No,
                              double Freq,
                              int Duration,
                              int Dev_Port);

//--------------------------------------------------------------------------- Clock_Error_Read
// Прочитать результаты теста ТХЧ. Должна вызываться по прошествии времени, отведенного на тест
// + запас в пару секунд
// MError - сюда будет помещена строка с результатом теста
// 	Должен быть указатель на указатель на буфер. Размер не менее 1024 байт
// Freq - Частота, которая задавалась при старте.
// ErrType - Тип результата:
// 0 - Погрешность в единицах частоты
// 1 - Суточная погрешность в секундах
// 2 - Относительная погрешность
// Meter_No - номер счетчика (нумерация с 1)
// Dev_Port - номер com-порта
    boolean Clock_Error_Read(PointerByReference MError,
                             double Freq,
                             int ErrType,
                             int Meter_No,
                             int Dev_Port);

//--------------------------------------------------------------------------- Dll_Port_Close
// Закрыть порт
    boolean Dll_Port_Close(PointerByReference close);


    //Мод для включения малых токов <300 мА
    //Если не выставляются малые токи, то необходимов выставлять либо 0 либо 3
    boolean Set_ReviseMode(int mode);

    boolean Set_ReviseTime(double time);

    boolean Set_NoRevise(boolean b);

    boolean Power_Revise();

//--------------------------------------------------------------------------- Count_Start
    boolean Count_Start(int Meter_No,
                        int Dev_Port);
//--------------------------------------------------------------------------- Count_Read
    boolean Count_Read(IntByReference MPulse,
                       int Meter_No,
                       int Dev_Port);
}
