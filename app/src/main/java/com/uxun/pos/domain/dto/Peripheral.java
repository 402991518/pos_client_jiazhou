package com.uxun.pos.domain.dto;

/**
 * @author Administrator 外围设备信息
 */

public class Peripheral {

	public String OrgCode;// 商场编码
	public String PosNo;// 收银机号
	public Byte Print_Mode;// 打印方式：0-不打印，1-整单打印，2-逐行打印
	public Byte Print_Format;// 打印格式：1-标准，2-省纸
	public Byte Print_Head_First;// 是否先打印票头：0-否，1-是
	public String Printer_Model;// 打印机类型(TM-U220PD)
	public String Printer_Port;// 打印机端口(NONE,COM1,LPT1)
	public String Printer_Para;// 打印机端口参数(9600,N,8,1)

	public Byte Paper_Width;// 小票打印字符长度(大纸一般是40，小纸一般是32)
	public Byte Barcode_Width;// 条码打印字符长度
	public Byte Price_Width;// 单价打印字符长度
	public Byte Quantity_Width;// 数量打印字符长度
	public Byte Amount_Width;// 金额打印字符长度

	public Byte Printer_Feedline;// 打印结束后走纸行数
	public Byte Printer_BlackLabel;// 打印纸黑标打印(针对发票纸)：0-否，1-是
	public Byte Printer_BlackLabel_Num;// 黑标打印时每张纸的打印行数(针对发票纸)

	public String CashDrawer_Port;// 钱箱端口(NONE,COM1,LPT1,MainBoard)

	public String Display_Model;// 顾显类型(PD9000)
	public String Display_Port;// 顾显端口(NONE,COM1,LPT1)

	public String Display_Para;// 顾显端口参数(9600,N,8,1)
	public Byte Display_Chinese;// 顾显支持中文显示：0-否，1-是
	public String Display_show;// 中文顾显显示的欢迎词

	public String CashPrinter_Model;// 券打印机类型(TM-U220PD)
	public String CashPrinter_Port;// 券打印机端口(NONE,COM1,LPT1)
	public String CashPrinter_Para;// 券打印机端口参数(9600,N,8,1)
	public Byte CashPaper_Width;// 券打印字符长度(大纸一般是40，小纸一般是32)
	public Byte CashPrinter_Feedline;// 券打印结束后走纸行数

	public String Mark_Model;// 评价机类型(SL-PJ-KEY4)
	public String Mark_Port;// 评价机端口(NONE,COM1)
	public String Mark_Para;// 评价机端口端口参数(9600,N,8,1)

	public String ICcard_Model;// IC读写卡器类型(XRH200，HRF-35USB，EYE-U010)
	public String ICcard_Port;// IC读写卡器端口(NONE,USB,COM1)
	public String ICcard_Para;// IC读写卡器端口参数(9600,N,8,1)

	public String VisualCard_Model;// 可视卡打印机类型
	public String VisualCard_Port;// 可视卡打印机端口(NONE,COM1,LPT1)
	public String VisualCard_Para;// 可视卡打印机端口参数(9600,N,8,1)

	public String Remark;// 备注
	public String ExtCol1;// 扩展字段
	public String ExtCol2;// 扩展字段
	public String ExtCol3;// 扩展字段
	public String ExtCol4;// 扩展字段
	public String ExtCol5;// 扩展字段
	public String ExtCol6;// 扩展字段
	public String ExtCol7;// 扩展字段
	public String ExtCol8;// 扩展字段
	public String ExtCol9;// 扩展字段
	public String ExtCol10;// 扩展字段
}
