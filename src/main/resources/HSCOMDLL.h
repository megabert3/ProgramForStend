//---------------------------------------------------------------------------

#ifndef HSCOMDLLH
#define HSCOMDLLH
//---------------------------------------------------------------------------
// ��������� ������� ����������

//--------------------------------------------------------------------------- Adjust_UI
// �������� ���������� � ���
// Phase - �����:
// 		0 - ����������
//		1 - ����-������ �������-���������
//		2 - ����-������ ����-���������
// 		3 - ����-������ ����-��������� ������� 90 ��������
// 		4 - ����-������ ����-��������� ������� 60 ��������
//		5 - ����-������ �������-��������� (�������)
//		6 - ����-������ ����-��������� 9�������)
//		7 - ���������� �������
// Rated_Volt - ����������
// Rated_Curr - ���
// Rated_Freq - �������
// PhaseSrequence - ����������� ���
//		0 - ������
//		1 - ��������
// Revers - ���������������
//		0 - ������
//		1 - ��������
// Volt_Per - ������� �� ���������� (0- 100)
// Curr_Per - ������� �� ���� (0-100)
// IABC - ������, ������������ ����, �� ������� ������� ���: A, B, C, H - ���
// CosP - ������  � ���������� ����. ��������: "1.0", "0.5L", "0.8C"
// SModel - ������ � ������� ��������:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 ����)
//		HY5101C-22, HY5101C-23, SY3803 (1 ����)
//		TC-3000C (1 ����)
//		TC-3000D (3 ����)
// Dev_Port - ����� com-�����
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
// �������� ���������� � ���
// Phase - �����:
// 		0 - ����������
//		1 - ����-������ �������-���������
//		2 - ����-������ ����-���������
// 		3 - ����-������ ����-��������� ������� 90 ��������
// 		4 - ����-������ ����-��������� ������� 60 ��������
//		5 - ����-������ �������-��������� (�������)
//		6 - ����-������ ����-��������� 9�������)
//		7 - ���������� �������
// Rated_Volt - ����������
// Rated_Curr - ���
// Rated_Freq - �������
// PhaseSrequence - ����������� ���
//		0 - ������
//		1 - ��������
// Revers - ���������������
//		0 - ������
//		1 - ��������
// Volt_PerA - ������� �� ���������� ���� �(0- 100)
// Volt_Per� - ������� �� ���������� ���� B(0- 100)
// Volt_Per� - ������� �� ���������� ���� C(0- 100)
// Curr_Per - ������� �� ���� (0-100)
// IABC - ������, ������������ ����, �� ������� ������� ���: A, B, C, H - ���
// CosP - ������  � ���������� ����. ��������: "1.0", "0.5L", "0.8C"
// SModel - ������ � ������� ��������:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 ����)
//		HY5101C-22, HY5101C-23, SY3803 (1 ����)
//		TC-3000C (1 ���� ���������������)
//		TC-3000D (3 ���� ���������������)
// Dev_Port - ����� com-�����
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
// �������� ������ �� ��������
// SData - ��������� �� ��������� �� ����� ��� ������ ������ ��������.
// � ���� ������������ ������, ����������� ��������. ������ ���� �� ����� 1024 ����
// SModel - ������ � ������� ��������:
// 		HY5303C-22, HS5320, SY3102, SY3302 (3 ����)
//		HY5101C-22, HY5101C-23, SY3803 (1 ����)
//		TC-3000C (1 ���� ���������������)
//		TC-3000D (3 ���� ���������������)
// Dev_Port - ����� com-�����
// � ����� ����� ������, � ������� � ����������� �� ���� �������� ����� ������:
/*
����-������ �����������:
HY5303C-22, SY3302
Ua,Ub,Uc,Ia,Ib,Ic,Angle_UaIa, Angle_UbIb, Angle_UcIc, Pa , Pb , Pc , Qa , Qb , Qc , Sa , Sb , Sc ,
Pall(A.P.),Qall(R.P.),Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall, Angle_UaUb, Angle_UbUc
HS5320, SY3102
Ua,Ub,Uc,Ia,Ib,Ic, Angle _UaUb, Angle _UaUc, Angle _UaIa, Angle _UaIb, Angle _UaIc, Pa , Pb , Pc ,
Pall(A.P.), Qa , Qb , Qc , Qall(R.P.),Sa , Sb , Sc ,Sall(Apparent power), Freq ,PFa,PFb,PFc,PFall
TC-3000D?
Ua , Ub , Uc , Ia , Ib , Ic , UI_Angle_a , UI_Angle_b , UI_Angle_c , Pa , Pb , Pc , Qa , Qb , Qc ,
Sa , Sb , Sc , Pall(A.P.) ,Qall( R.P.) ,Sall( Apparent power) , Freq , U_Range , I_Range
���������� �����������:
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
// �������� ������ ����������� ��������
// MError - ���� ����� �������� ������ � ������� � ���� ���-��, ��������
// ������ ���� ��������� �� ���������. ������ �� ����� 1024 ����
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Error_Read(
	void *MError,
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Error_Read *TPFH_Error_Read;

//--------------------------------------------------------------------------- Error_Start
// ��������� �������� ����������� �������� (���������� � ��� ������ ���� ��������)
// Meter_No - ����� �����
// Constant - ����������
// Pulse - ����������
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Error_Start(
	int Meter_No,
	double Constant,
	int Pulse,
	int Dev_Port);
extern fncTPFH_Error_Start *TPFH_Error_Start;

//--------------------------------------------------------------------------- Set_Pulse_Channel
// ��������� ������ ���������
// Meter_No - ����� �����
// ChannelFlag - ����� ���������:
//		0 - �������� �������� (+)
// 		1 - �������� �������� (-)
// 		2 - ���������� �������� (+)
// 		3 - ���������� �������� (-)
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Set_Pulse_Channel(
	int Meter_No,
	int ChannelFlag,
	int Dev_Port);
extern fncTPFH_Set_Pulse_Channel *TPFH_Set_Pulse_Channel;

//--------------------------------------------------------------------------- Set_485_Channel
// ��������� ������ 485 ����������
// Meter_No - ����� �����
// Open_Flag - ��������� ����������:
// 		0 - ��������
//		1 - �������
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Set_485_Channel(
	int Meter_No,
	int OpenFlag,
	int Dev_Port);
extern fncTPFH_Set_485_Channel *TPFH_Set_485_Channel;

//--------------------------------------------------------------------------- Search_mark
// ����� �����
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Search_mark(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Search_mark *TPFH_Search_mark;

//--------------------------------------------------------------------------- CRPSTA_start
// ����� CRPSTA
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_CRPSTA_start(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_CRPSTA_start *TPFH_CRPSTA_start;

//--------------------------------------------------------------------------- CRPSTA_clear
// ������� CRPSTA
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_CRPSTA_clear(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_CRPSTA_clear *TPFH_CRPSTA_clear;

//--------------------------------------------------------------------------- Search_mark_Result
// ��������� ������ �����
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Search_mark_Result(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Search_mark_Result *TPFH_Search_mark_Result;

//--------------------------------------------------------------------------- CRPSTA_Result
// ��������� CRPSTA
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_CRPSTA_Result(
	int Meter_No,
	int Dev_Port);
extern fncTPFH_CRPSTA_Result *TPFH_CRPSTA_Result;

//--------------------------------------------------------------------------- Error_Clear
// ����� ���� ������
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Error_Clear(
	int Dev_Port);
extern fncTPFH_Error_Clear *TPFH_Error_Clear;

//--------------------------------------------------------------------------- Power_Off
// ���������� ���������� � ���� (������ ����)
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Power_Off(
	int Dev_Port);
extern fncTPFH_Power_Off *TPFH_Power_Off;

//--------------------------------------------------------------------------- Power_Pause
// ���������� �������� (����)
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Power_Pause(
	int Dev_Port);
extern fncTPFH_Power_Pause *TPFH_Power_Pause;

//--------------------------------------------------------------------------- Read_SerialNumber
// ������ ��������� ������.
// Meter_SerialNumber - ����� ��� ���������
// Meter_No - ����� �����
// Meter_Port - ����� com-����� ��������
// ������� ��� ����������� �������������. �� ������������� ��� ����������������� ��
typedef bool __declspec(dllimport) __stdcall fncTPFH_Read_SerialNumber(
	char *Meter_SerialNumber,
	int Meter_No,
	int Meter_Port);
extern fncTPFH_Read_SerialNumber *TPFH_Read_SerialNumber;

//--------------------------------------------------------------------------- ConstTest_Start
// ����� ����� ��������
// Meter_No - ����� �����
// Constant - ����������
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_ConstTest_Start(
	int Meter_No,
	double Constant,
	int Dev_Port);
extern fncTPFH_ConstTest_Start *TPFH_ConstTest_Start;

//--------------------------------------------------------------------------- ConstPulse_Read
// ������ ������ �� �������
// MeterKWH - �������� ��������
// StdKWH - �������� ����������
// Constant - ����������
// Meter_No - ����� �����
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_ConstPulse_Read(
	double MeterKWH,
	double StdKWH,
	double Constant,
	int Meter_No,
	int Dev_Port);
extern fncTPFH_ConstPulse_Read *TPFH_ConstPulse_Read;

//--------------------------------------------------------------------------- SelectCircuit
// ����� ����
// Circuit:
//	 	0 A
//      1 B
//		2 AB
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_SelectCircuit(
	int Circuit,
	int Dev_Port);
extern fncTPFH_SelectCircuit *TPFH_SelectCircuit;

//--------------------------------------------------------------------------- CutNeutral
// ��������� ��������
// CuttingFlag:
//		0 - ���������
//		1 - ��������
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_CutNeutral(
	int CuttingFlag,
	int Dev_Port);
extern fncTPFH_CutNeutral *TPFH_CutNeutral;

//--------------------------------------------------------------------------- Clock_Error_Start
// ����� ����� ���
// Meter_No - ����� �������� (��������� � 1)
// Freq - ������� � ������. ��� ����������� ������� 1
// Duration - ������������ ������ � ���������� ��������� Freq
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Clock_Error_Start(
	int Meter_No,
	double Freq,
	int Duration,
	int Dev_Port);
extern fncTPFH_Clock_Error_Start *TPFH_Clock_Error_Start;

//--------------------------------------------------------------------------- Clock_Error_Read
// ��������� ���������� ����� ���. ������ ���������� �� ���������� �������, ����������� �� ����
// + ����� � ���� ������
// MError - ���� ����� �������� ������ � ����������� �����
// 	������ ���� ��������� �� ��������� �� �����. ������ �� ����� 1024 ����
// Freq - �������, ������� ���������� ��� ������.
// ErrType - ��� ����������:
// 0 - ����������� � �������� �������
// 1 - �������� ����������� � ��������
// 2 - ������������� �����������
// Meter_No - ����� �������� (��������� � 1)
// Dev_Port - ����� com-�����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Clock_Error_Read(
	void *MError,
	double Freq,
	int ErrType,
	int Meter_No,
	int Dev_Port);
extern fncTPFH_Clock_Error_Read *TPFH_Clock_Error_Read;

//--------------------------------------------------------------------------- Dll_Port_Close
// ������� ����
typedef bool __declspec(dllimport) __stdcall fncTPFH_Dll_Port_Close(void);
extern fncTPFH_Dll_Port_Close *TPFH_Dll_Port_Close;

bool TPF_InitHSCOMDLL(void);
void TPF_CloseHSCOMDLL(void);

#endif
