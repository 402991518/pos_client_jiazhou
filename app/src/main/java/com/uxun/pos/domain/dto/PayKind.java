package com.uxun.pos.domain.dto;

import java.math.BigDecimal;

/**
 * @author Administrator 银行卡汇率信息
 */

public class PayKind {
    public String PayType;// 付款类别
    public String PayClass;// 发卡行编码
    public String KindDescr;// 发卡行名称
    public String Currency;// 币种
    public BigDecimal ExchangeRate;// 汇率
    public Short PayDefault;// 默认付款
    public Character PayKind;// 类型(1-境内卡,2-境外卡)
    public BigDecimal ScalePrice;// 汇率手续费率(%)
    public Short OrderId;// 排序ID
}
