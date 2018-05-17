package com.uxun.pos.domain.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Administrator 商品信息
 */
public class Goods implements Serializable {

	private static final long serialVersionUID = 3560823676002491788L;
	public BigDecimal SALEPRICE; // 售价
	public BigDecimal MEMPRICE;// 会员价
	public BigDecimal REBATE;// 会员折率
	public BigDecimal PURPRICE;// 不含税进价
	public BigDecimal PURPRICETAX;// 含税进价
	public BigDecimal WHOLEPRICE;// 团购价
	public Character REBATETYPE;// 打折方式(0 不打折、1 会员价、2 折率)
	public Character IFDISCOUNT;// 是否允许折上折(N 不允许、Y 允许)
	public String ITEMNO;// 店内码
	public String BARCODE;// 条形码
	public String ITEMNAME;// 商品全称
	public String ITEMSUBNAME;// 商品简称
	public String ITEMENNAME;// 商品英文名
	public String ITEMSUBNO;// 商品助记符
	public String ITEMSPEC;// 商品规格
	public String ITEMUNIT;// 商品单位
	public Character TYPETAX;// 税种
	public String PRODUCE;// 商品产地
	public String DEPARTCODE;// 部门编码
	public String VENDORNO;// 供应商
	public String LESCODE;// 租户
	public String GRDCODE;// 商铺
	public String ATTITUDE;// 商品性质
	public Character WEIGHTYPE;// 称重计量方式
	public String CODETYPE;// 称重商品编码方式
	public BigDecimal SCORERULE;// 积分规则
	public BigDecimal VENDORAPP;// 积分分摊
	public Date BEGINDATE;// 积分促销起始日期
	public Date ENDDATE;// 积分促销终止日期
	public BigDecimal PROMSCORE;// 促销积分规则
	public String PROMNO;// 促销流水号
	public BigDecimal VENDORVIPAPP;// 促销积分分摊
	public String DEPOT;// 库区
	public String ITEMEXPLAIN;// 商品介绍
	public String SHELF;// 货位
	public String GOODSLAY;// 层面深
	public String MFRNO;// 生产商
	public String BURDENING;// 配料表
	public String BESTOWALIF;// 贮存条件
	public String METHOD;// 食用方法
	public String ITEMGRADE;// 商品等级
	public String MANAGETYPE;// 经营方式
	public String BANDCODE;// 商品品牌
	public String CLASSCODE;// 商品小类
	public String REMARK;// 备注
	public BigDecimal ITEMQTY;// 库存数量
	public Character CHGTYPE;// 变换类型(Z 非商品明细条码、0 正常变换、1 削价条码)
	public String ITEMPACK;// 包装类型
	public BigDecimal ITEMPACKNUM;// 包装数
	public BigDecimal LOWESTRATE;// 最低许可折扣
	public String ITEMCOLOR;// 颜色
	public String ITEMSIZE;// 尺码
	public String EMPCODE;// 员工代码
	public BigDecimal SCALEPRICE;// 正常扣率
	public BigDecimal PROMSCALE;// 实际扣率
	public Character IFDEPOT;// 是否管库存@(Y 是、N 否)
	public Character Ifnegative;// 是否允许负库销售@(0 不允许、1 允许)
	public String ERRMSG;// 存储过程返回信息

	public Round round;// 单品小计金额舍入模式

}
