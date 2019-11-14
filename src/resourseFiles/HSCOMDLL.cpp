// Модуль интерфейса для DLL
//---------------------------------------------------------------------------
#include <windows.h>
#pragma hdrstop

#include "HSCOMDLL.h"
//---------------------------------------------------------------------------
#pragma package(smart_init)

fncTPFH_Adjust_UI 			*TPFH_Adjust_UI 			= NULL;
fncTPFH_Adjust_UI1 			*TPFH_Adjust_UI1 			= NULL;
fncTPFH_StdMeter_Read 		*TPFH_StdMeter_Read 		= NULL;
fncTPFH_Error_Read 			*TPFH_Error_Read    		= NULL;
fncTPFH_Error_Start 		*TPFH_Error_Start			= NULL;
fncTPFH_Set_Pulse_Channel 	*TPFH_Set_Pulse_Channel		= NULL;
fncTPFH_Set_485_Channel 	*TPFH_Set_485_Channel		= NULL;
fncTPFH_Search_mark 		*TPFH_Search_mark			= NULL;
fncTPFH_CRPSTA_start 		*TPFH_CRPSTA_start			= NULL;
fncTPFH_CRPSTA_clear 		*TPFH_CRPSTA_clear			= NULL;
fncTPFH_CRPSTA_Result 		*TPFH_CRPSTA_Result			= NULL;
fncTPFH_Search_mark_Result 	*TPFH_Search_mark_Result	= NULL;
fncTPFH_Error_Clear 		*TPFH_Error_Clear			= NULL;
fncTPFH_Power_Off 			*TPFH_Power_Off				= NULL;
fncTPFH_Power_Pause 		*TPFH_Power_Pause			= NULL;
fncTPFH_Read_SerialNumber 	*TPFH_Read_SerialNumber		= NULL;
fncTPFH_ConstTest_Start 	*TPFH_ConstTest_Start		= NULL;
fncTPFH_ConstPulse_Read 	*TPFH_ConstPulse_Read		= NULL;
fncTPFH_SelectCircuit 		*TPFH_SelectCircuit			= NULL;
fncTPFH_CutNeutral 			*TPFH_CutNeutral			= NULL;
fncTPFH_Dll_Port_Close 		*TPFH_Dll_Port_Close		= NULL;
fncTPFH_Clock_Error_Start 	*TPFH_Clock_Error_Start		= NULL;
fncTPFH_Clock_Error_Read 	*TPFH_Clock_Error_Read		= NULL;

static HINSTANCE hDLL = NULL;

//---------------------------------------------------------------------------
// Функция возвращает точку входа экспортируемой dll функции. В случае ошибки
// вернет NULL и ругнется (возможно при битой или недостроенной либе)
static inline void *TPF_GetProcAddres(char *pFun)
{
	void *Ret;
	wchar_t *Buff;

	Ret = (void*)GetProcAddress(hDLL,pFun);

	if (Ret == NULL)
	{
		Buff = new wchar_t[strlen(pFun)+1];
		mbstowcs(Buff,pFun,strlen(pFun)+1);

		MessageBoxW(NULL,Buff,L"Нет функции",MB_OK|MB_ICONHAND);

		delete Buff;
	}

	return Ret;
}

//---------------------------------------------------------------------------
// Загружает DLL и присваивает точки входа экспортируемых функций extern - указателям
// На ошибочные указатели будет ругань
// Вернет ложь, если dll не загружена
bool TPF_InitHSCOMDLL(void)
{
	hDLL = LoadLibrary(L"hscom.dll");
	if (hDLL)
	{
		TPFH_Adjust_UI 			= (fncTPFH_Adjust_UI*) 			TPF_GetProcAddres("Adjust_UI");
		TPFH_Adjust_UI1 		= (fncTPFH_Adjust_UI1*) 		TPF_GetProcAddres("Adjust_UI1");
		TPFH_StdMeter_Read 		= (fncTPFH_StdMeter_Read*)		TPF_GetProcAddres("StdMeter_Read");
		TPFH_Error_Read			= (fncTPFH_Error_Read*)			TPF_GetProcAddres("Error_Read");
		TPFH_Error_Start		= (fncTPFH_Error_Start*)		TPF_GetProcAddres("Error_Start");
		TPFH_Error_Clear  		= (fncTPFH_Error_Clear*)		TPF_GetProcAddres("Error_Clear");
		TPFH_Set_Pulse_Channel  = (fncTPFH_Set_Pulse_Channel*)	TPF_GetProcAddres("Set_Pulse_Channel");
		TPFH_Set_485_Channel  	= (fncTPFH_Set_485_Channel*)	TPF_GetProcAddres("Set_485_Channel");
		TPFH_Search_mark  		= (fncTPFH_Search_mark*)		TPF_GetProcAddres("Search_mark");
		TPFH_Search_mark_Result = (fncTPFH_Search_mark_Result*)	TPF_GetProcAddres("Search_mark_Result");
		TPFH_CRPSTA_start  		= (fncTPFH_CRPSTA_start*)		TPF_GetProcAddres("CRPSTA_start");
		TPFH_CRPSTA_clear  		= (fncTPFH_CRPSTA_clear*)		TPF_GetProcAddres("CRPSTA_clear");
		TPFH_CRPSTA_Result  	= (fncTPFH_CRPSTA_Result*)		TPF_GetProcAddres("CRPSTA_Result");
		TPFH_Power_Off  		= (fncTPFH_Power_Off*)			TPF_GetProcAddres("Power_Off");
		TPFH_Power_Pause  		= (fncTPFH_Power_Pause*)		TPF_GetProcAddres("Power_Pause");
		TPFH_Read_SerialNumber  = (fncTPFH_Read_SerialNumber*)	TPF_GetProcAddres("Read_SerialNumber");
		TPFH_ConstTest_Start  	= (fncTPFH_ConstTest_Start*)	TPF_GetProcAddres("ConstTest_Start");
		TPFH_ConstPulse_Read  	= (fncTPFH_ConstPulse_Read*)	TPF_GetProcAddres("ConstPulse_Read");
		TPFH_SelectCircuit  	= (fncTPFH_SelectCircuit*)		TPF_GetProcAddres("SelectCircuit");
		TPFH_CutNeutral  		= (fncTPFH_CutNeutral*)			TPF_GetProcAddres("CutNeutral");
		TPFH_Dll_Port_Close  	= (fncTPFH_Dll_Port_Close*)	  	TPF_GetProcAddres("Dll_Port_Close");
		TPFH_Clock_Error_Start  = (fncTPFH_Clock_Error_Start*)	TPF_GetProcAddres("Clock_Error_Start");
		TPFH_Clock_Error_Read  	= (fncTPFH_Clock_Error_Read*)	TPF_GetProcAddres("Clock_Error_Read");

		return true;
	}

	return false;
}

//---------------------------------------------------------------------------
// Выгружает DLL из памяти
void TPF_CloseHSCOMDLL(void)
{
	if (hDLL != NULL) FreeLibrary(hDLL);
}
