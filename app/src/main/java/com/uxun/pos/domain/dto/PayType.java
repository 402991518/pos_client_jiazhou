package com.uxun.pos.domain.dto;

import java.math.BigDecimal;

/**
 * @author Administrator 付款菜单
 */
public class PayType {
	public String OrgCode;// 组织机构
	public String PosNo;// 收银机号

	public String PayType;// 付款方式
	public String PayDescr;// 方式描述
	public String PayKind;// 付款类型：1-现金类，2-银行类，3-储值卡类，4-券类，5-外币类，6-移动支付
	public String Currency;// 币种
	public BigDecimal ExchangeRate;// 汇率(银行卡时，以s_pay_kind为准)
	public Short OrderId;// 排序ID
	public Byte IsScore;// 是否积分:0-不积分，1-积分
	public Byte RecCardNo;// 是否记录卡号:0-不，1-是
	public Byte RecTradeNo;// 是否记录交易号:0-不，1-是

	public Byte OpenCashdrawer;// 收银结算后是否开钱箱：0-不开，1-开
	public Byte Payment;// 是否缴款:0-不，1-是
	public Byte Interface;// 是否调用接口:0-不，1-是
	public Byte PopPayWin;// 是否弹出支付窗口:0-不，1-是
	public Byte BuildAccount;// 是否生成销售应付款:0-不，1-是

	public Byte IsModify;// 是否可修改:0-不，1-是
	public Byte Status;// 启用:0-不启用，1-启用

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
