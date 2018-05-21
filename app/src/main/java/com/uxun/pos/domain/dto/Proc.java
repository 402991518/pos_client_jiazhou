package com.uxun.pos.domain.dto;

import com.uxun.pos.domain.bo.submit.YuelangPrize;
import com.uxun.pos.domain.bo.submit.YuelangReback;
import com.uxun.pos.domain.bo.submit.YuelangScore;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Proc {

    // 表头
    public static class H {
        public String SaleNo;
        public Integer YMD;
        public String OrgCode;
        public String PosNo;
        public String Cashier;
        public Byte DataSource;
        public Date BeginDate;
        public Date EndDate;

        public BigDecimal Amount;
        public BigDecimal RealAmt;

        public String ReturnMan;
        public String DiscountMan;
        public String ChangePriceMan;
        public String PresentMan;
        public String MemcardNo;

        public BigDecimal DivideAmt;
        public String DjSaleNo;
        public Byte EstimationScale;
        public String ExpandNo;
        public String OldSaleNo;

        public String Reserve1;
        public String Reserve2;
        public String Reserve3;

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

    // 销售商品明细
    public static class D {

        public String SaleNo;
        public Short RowNo;
        public Integer YMD;// 销售日期
        public String OrgCode;// 组织机构代码
        public String PosNo; // POS机编号
        public String Cashier; // 收银员
        public String Depot;// 库区
        public String ItemNo;// 商品店内码
        public String BarCode;// 商品条码
        public BigDecimal ItemQty;// 销售数量
        public BigDecimal SaleQty;

        public BigDecimal ItemRate; // 折扣
        public BigDecimal SalePrice;// 原售价
        public BigDecimal RealPrice;// 实际售价
        public BigDecimal SaleAmount;// 应收金额
        public BigDecimal RealAmount;// 实收金额
        public BigDecimal DivideAmt;// 整单舍分分摊金额
        public BigDecimal DivideAmt2;// 单品舍分分摊金额

        public String RateType; // 促销类型
        public String PromNo; // 未知字段
        public String ItemColor;// 商品颜色
        public String ItemSize;// 商品尺寸
        public String CashKinds;// 未知字段
        public String OldSaleNo;// 退货单号
        public Short OldRowNo;// 退货单号
        public String SalesMan;// 未知字段
        public String ShoppeNo;// 未知字段
        public String OtherCode;// 未知字段

        public BigDecimal LastScoreRule;// 积分规则
        public BigDecimal Integral;// 本次积分
        public BigDecimal PromScore;// 赠送积分
        public BigDecimal ScalePrice; // 正常扣率
        public BigDecimal PromScale;// 实际扣率
        public BigDecimal VendorApp; // 正常积分分摊
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

        public String itemname;
        public String itemunit;
        public String attitude;
        public Character weightype;
        public String classcode;
        public BigDecimal scorerule;
        public String managetype;
        public String GrdCode;
        public String ItemSpec;
    }

    // 付款明细
    public static class P {

        public String SaleNo;
        public Short RowNo;
        public Integer YMD;// 销售日期
        public String OrgCode;// 组织机构代码
        public String PosNo;// POS机编号
        public String Cashier;// 收银员
        public String PayType;// 数据来源
        public String PayClass;// 付款分类
        public String ClassNo;// 分类号码
        public Short OldRowNo;
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
        public String PayDescr;
        public String Currency;
        public String PayKind;
    }

    public H h;
    public List<D> ds;
    public List<P> ps;

    public YuelangScore yuelangScore;// 粤浪积分（销售产生）
    public YuelangReback yuelangReback;// 粤浪退积分(退货产生)
    public List<YuelangPrize> yuelangPrizes;// 粤浪卡券信息

}
