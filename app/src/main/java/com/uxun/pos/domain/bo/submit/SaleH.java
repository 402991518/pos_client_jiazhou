package com.uxun.pos.domain.bo.submit;

import java.math.BigDecimal;
import java.util.Date;

 
public class SaleH {

	public String SaleNo;// 销售单号

	public Integer YMD;// 销售日期
	public String OrgCode;// 组织机构代码
	public String PosNo;// POS机编号
	public String Cashier;// 收银员
	public Byte DataSource;// 数据来源

	public Date BeginDate; // 起始时间
	public Date EndDate; // 终止时间
	public Date UploadTime;// 起始时间
	public Date CollectTime; // 终止时间

	public BigDecimal Amount; // 销售金额
	public BigDecimal RealAmt;// 实收金额
	public BigDecimal DivideAmt;// 舍分金额

	public String ReturnMan;// 退货人
	public String DiscountMan; // 打折人
	public String ChangePriceMan;// 变价人
	public String PresentMan;// 赠送人
	public String MemcardNo;// 会员卡号
	public String OldSaleNo;// 退货原单号

	public Byte EstimationScale;// 顾客评价(0未评价)
	public String ExpandNo;// 扩展单号
	public String Reserve1;// 未知字段
	public String Reserve2;// 未知字段
	public String Reserve3;// 未知字段

	public String ExtCol1;
	public String ExtCol2;
	public String ExtCol3;
	public String ExtCol4;
	public String ExtCol5;
	public String ExtCol6;
	public String ExtCol7;
	public String ExtCol8;
	public String ExtCol9;
	public String ExtCol10;

}
