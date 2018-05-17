package com.uxun.pos.domain.bo.submit;

import java.math.BigDecimal;
import java.util.Date;

public class SaleR {

	public String SaleNo;
	public Short RowNo;
	public Short SerialNo;

	public Integer YMD;// 销售日期
	public String OrgCode;// 组织机构代码
	public String PosNo; // POS机编号
	public String Cashier;// 收银员
	public Byte DataSource;// 数据来源

	public Date UploadTime;// 起始时间
	public Date CollectTime;// 终止时间

	public String ItemNo;// 商品店内码
	public BigDecimal Amount;// 原金额
	public BigDecimal RealAmt;// 现金额
	public String RateType;// 促销类型
	public String PromNo;// 促销单号
	public String OldSaleNo;// 退货单号

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
