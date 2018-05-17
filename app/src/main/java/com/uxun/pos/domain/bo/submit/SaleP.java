package com.uxun.pos.domain.bo.submit;

import java.math.BigDecimal;
import java.util.Date;

public class SaleP {

	public String SaleNo;
	public Short RowNo;

	public Integer YMD;// 销售日期
	public String OrgCode;// 组织机构代码
	public String PosNo;// POS机编号
	public String Cashier; // 收银员
	public Byte DataSource; // 数据来源

	public Date UploadTime;// 起始时间
	public Date CollectTime;// 终止时间

	public String PayType;// 付款类别
	public String PayClass;// 付款分类
	public String ClassNo;// 分类号码
	public BigDecimal Amount;// 消费金额
	public BigDecimal ExchangeRate;// 汇率
	public BigDecimal RealMount;// 实际金额
	public String OldSaleNo;// 退货单号
	public String TermSn;// 刷卡流水号(银联)
	public String DealerNo;// 商户号(银联)
	public Date TradeTime;// 交易时间(银联)
	public BigDecimal ScalePrice;// 正常扣率
	public BigDecimal PromScale;// 实际扣率

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
