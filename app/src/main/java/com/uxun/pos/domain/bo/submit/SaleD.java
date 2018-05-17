package com.uxun.pos.domain.bo.submit;

import java.math.BigDecimal;
import java.util.Date;

public class SaleD {

	public String SaleNo;
	public Short RowNo;

	public Integer YMD;// 销售日期
	public String OrgCode;// 组织机构代码
	public String PosNo;// POS机编号
	public String Cashier;// 收银员
	public Byte DataSource;// 数据来源

	public Date UploadTime;// 起始时间
	public Date CollectTime;// 终止时间

	public String Depot;// 库区
	public String ItemNo;// 商品店内码
	public String BarCode;// 商品条码
	public BigDecimal ItemQty;// 销售数量
	public BigDecimal ItemRate;// 折扣
	public BigDecimal SalePrice; // 原售价
	public BigDecimal RealPrice;// 实际售价
	public BigDecimal SaleAmount;// 应收金额
	public BigDecimal RealAmount;// 实收金额
	public BigDecimal DivideAmt;// 整单舍分分摊金额
	public BigDecimal DivideAmt2;// 单品舍分分摊金额
	public String RateType;// 促销类型
	public String PromNo;// 未知字段
	public String ItemColor;// 商品颜色
	public String ItemSize; // 商品尺寸
	public String CashKinds;// 未知字段
	public String OldSaleNo;// 退货单号
	public Short OldRowNo;// 退货单号
	public String SalesMan;// 未知字段
	public String ShoppeNo;// 未知字段
	public String OtherCode;// 未知字段
	public BigDecimal ScoreRule;// 积分规则
	public BigDecimal Integral;// 本次积分
	public BigDecimal PromScore; // 赠送积分
	public BigDecimal ScalePrice;// 正常扣率
	public BigDecimal PromScale; // 实际扣率
	public BigDecimal VendorApp;// 正常积分分摊
	public BigDecimal VendorVipApp;// 促销积分分摊

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
