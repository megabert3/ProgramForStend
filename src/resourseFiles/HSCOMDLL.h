//---------------------------------------------------------------------------

#ifndef HSCOMDLLH
#define HSCOMDLLH
//---------------------------------------------------------------------------
// прототипы функции библиотеки

//--------------------------------------------------------------------------- Adjust_UI
// Включить напряжение и ток
// Phase - Режим:
// 		0 - Однофазный
//		1 - Трех-фазный четырех-проводной
//		2 - Трех-фазный трех-проводной
// 		3 - Трех-фазный трех-проводной реактив 90 градусов
// 		4 - Трех-фазный трех-проводной реактив 60 градусов
//		5 - Трех-фазный четырех-проводной (реактив)
//		6 - Трех-фазный трех-проводной 9реактив)
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
// Volt_Per - Процент по напряжению (0- 100)
// Curr_Per - Процент по току (0-100)
// IABC - строка, определяющая фазы, по которым пустить ток: A, B, C, H - все
// CosP - строка  с кросинусом угла. Например: "1.0", "0.5L", "0.8C"
// SModel - Строка с моделью счетчика:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 фазы)
//		HY5101C-22, HY5101C-23, SY3803 (1 фаза)
//		TC-3000C (1 фаза)
//		TC-3000D (3 фазы)
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Adjust_UI(
	int Phase,
	double Rated_Volt,
	double Rated_Curr,
	double Rated_Freq,
	int PhaseSrequence,
	int Revers,
	double Volt_Per,
	double Curr_Per,
	char *IABC,
	char *CosP,
	char *SModel,
	int Dev_Port);
extern fncTPFH_Adjust_UI *TPFH_Adjust_UI;

//--------------------------------------------------------------------------- Adjust_UI1
// Включить напряжение и ток
// Phase - Режим:
// 		0 - Однофазный
//		1 - Трех-фазный четырех-проводной
//		2 - Трех-фазный трех-проводной
// 		3 - Трех-фазный трех-проводной реактив 90 градусов
// 		4 - Трех-фазный трех-проводной реактив 60 градусов
//		5 - Трех-фазный четырех-проводной (реактив)
//		6 - Трех-фазный трех-проводной 9реактив)
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
typedef bool __declspec(dllimport) __stdcall fncTPFH_Adjust_UI1(
	int Phase,
	double Rated_Volt,
	double Rated_Curr,
	double Rated_Freq,
	int PhaseSrequence,
	int Revers,
	double Volt_PerA,
	double Volt_PerB,
	double Volt_PerC,
	double Curr_Per,
	char *IABC,
	char *CosP,
	char *SModel,
	int Dev_Port);
extern fncTPFH_Adjust_UI1 *TPFH_Adjust_UI1;

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
typedef bool __declspec(dllimport) __stdcall fncTPFH_StdMeter_Read(
	void *SData,
	char *SModel,
	int Dev_Port);
extern fncTPFH_StdMeter_Read *TPFH_StdMeter_Read;

//--------------------------------------------------------------------------- Error_Read
// Получить ошибку навешенного счетчика
// MError - сюда будет помещена строка с ошибкой в виде Кол-во, значение
// Должен быть указатель на указатель. Размер не менее 1024 байт
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Error_Read(
	void *MError,
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Error_Read *TPFH_Error_Read;

//--------------------------------------------------------------------------- Error_Start
// Запустить проверку навешенного счетчика (напряжение и ток должны быть включены)
// Meter_No - номер места
// Constant - постоянная
// Pulse - Усреднение
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Error_Start(
	int Meter_No,
	double Constant,
	int Pulse,
	int Dev_Port);
extern fncTPFH_Error_Start *TPFH_Error_Start;

//--------------------------------------------------------------------------- Set_Pulse_Channel
// Установка режима импульсов
// Meter_No - номер места
// ChannelFlag - Режим импульсов:
//		0 - Активная мощность (+)
// 		1 - Активная мощность (-)
// 		2 - Реактивная мощность (+)
// 		3 - Реактивная мощность (-)
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Set_Pulse_Channel(
	int Meter_No,
	int ChannelFlag,
	int Dev_Port);
extern fncTPFH_Set_Pulse_Channel *TPFH_Set_Pulse_Channel;

//--------------------------------------------------------------------------- Set_485_Channel
// Установка режима 485 интерфейса
// Meter_No - номер места
// Open_Flag - состояние интерфейса:
// 		0 - выключен
//		1 - включен
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Set_485_Channel(
	int Meter_No,
	int OpenFlag,
	int Dev_Port);
extern fncTPFH_Set_485_Channel *TPFH_Set_485_Channel;

//--------------------------------------------------------------------------- Search_mark
// Поиск метки
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Search_mark(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Search_mark *TPFH_Search_mark;

//--------------------------------------------------------------------------- CRPSTA_start
// Старт CRPSTA
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_CRPSTA_start(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_CRPSTA_start *TPFH_CRPSTA_start;

//--------------------------------------------------------------------------- CRPSTA_clear
// Очистка CRPSTA
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_CRPSTA_clear(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_CRPSTA_clear *TPFH_CRPSTA_clear;

//--------------------------------------------------------------------------- Search_mark_Result
// результат поиска метки
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Search_mark_Result(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Search_mark_Result *TPFH_Search_mark_Result;

//--------------------------------------------------------------------------- CRPSTA_Result
// результат CRPSTA
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_CRPSTA_Result(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_CRPSTA_Result *TPFH_CRPSTA_Result;

//--------------------------------------------------------------------------- Error_Clear
// Сброс всех ошибок
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Error_Clear(
	int Dev_Port);
extern fncTPFH_Error_Clear *TPFH_Error_Clear;

//--------------------------------------------------------------------------- Power_Off
// Выключение напряжения и тока (кнопка Стоп)
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Power_Off(
	int Dev_Port);
extern fncTPFH_Power_Off *TPFH_Power_Off;

//--------------------------------------------------------------------------- Power_Pause
// Выключение нагрузки (тока)
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Power_Pause(
	int Dev_Port);
extern fncTPFH_Power_Pause *TPFH_Power_Pause;

//--------------------------------------------------------------------------- Read_SerialNumber
// Чтение серийного номера.
// Meter_SerialNumber - место под результат
// Meter_No - номер места
// Meter_Port - номер com-порта счетчика
// Функция для внутреннего использования. Не предназначена для пользовательского ПО
typedef bool __declspec(dllimport) __stdcall fncTPFH_Read_SerialNumber(
	char *Meter_SerialNumber,
	int Meter_No,
	int Meter_Port);
extern fncTPFH_Read_SerialNumber *TPFH_Read_SerialNumber;

//--------------------------------------------------------------------------- ConstTest_Start
// Старт теста констант
// Meter_No - номер места
// Constant - постоянная
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_ConstTest_Start(
	int Meter_No,
	double Constant,
	int Dev_Port);
extern fncTPFH_ConstTest_Start *TPFH_ConstTest_Start;

//--------------------------------------------------------------------------- ConstPulse_Read
// Чтение данных по энергии
// MeterKWH - значения счетчика
// StdKWH - Значение эталонника
// Constant - постоянная
// Meter_No - номер места
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_ConstPulse_Read(
	double MeterKWH,
	double StdKWH,
	double Constant,
	int Meter_No,
	int Dev_Port);
extern fncTPFH_ConstPulse_Read *TPFH_ConstPulse_Read;

//--------------------------------------------------------------------------- SelectCircuit
// Выбор цепи
// Circuit:
//	 	0 A
//      1 B
//		2 AB
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_SelectCircuit(
	int Circuit,
	int Dev_Port);
extern fncTPFH_SelectCircuit *TPFH_SelectCircuit;

//--------------------------------------------------------------------------- CutNeutral
// Отключить нейтраль
// CuttingFlag:
//		0 - Отключить
//		1 - Включить
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_CutNeutral(
	int CuttingFlag,
	int Dev_Port);
extern fncTPFH_CutNeutral *TPFH_CutNeutral;

//--------------------------------------------------------------------------- Clock_Error_Start
// Старт теста ТХЧ
// Meter_No - номер счетчика (нумерация с 1)
// Freq - частота в герцах. Для большинства случаев 1
// Duration - длительность замера в количестве импульсов Freq
// Dev_Port - номер com-порта
typedef bool __declspec(dllimport) __stdcall fncTPFH_Clock_Error_Start(
	int Meter_No,
	double Freq,
	int Duration,
	int Dev_Port);
extern fncTPFH_Clock_Error_Start *TPFH_Clock_Error_Start;

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
typedef bool __declspec(dllimport) __stdcall fncTPFH_Clock_Error_Read(
	void *MError,
	double Freq,
	int ErrType,
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Clock_Error_Read *TPFH_Clock_Error_Read;

//--------------------------------------------------------------------------- Dll_Port_Close
// Закрыть порт
typedef bool __declspec(dllimport) __stdcall fncTPFH_Dll_Port_Close(void);
extern fncTPFH_Dll_Port_Close *TPFH_Dll_Port_Close;

bool TPF_InitHSCOMDLL(void);
void TPF_CloseHSCOMDLL(void);

#endif
