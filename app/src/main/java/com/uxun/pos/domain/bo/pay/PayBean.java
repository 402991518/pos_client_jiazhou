package com.uxun.pos.domain.bo.pay;

import java.math.BigDecimal;

public class PayBean {

    private String payType;//付款类别

    private String payName;//付款名称

    private BigDecimal amount;//付款金额

    private BigDecimal exchangeRate = BigDecimal.ONE;

    public PayBean(String payType, String payName, BigDecimal amount) {
        this.payType = payType;
        this.payName = payName;
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }

    public void setPayType(String payType) {
        this.payType = payType;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public BigDecimal getExchangeRate() {
        return exchangeRate;
    }

    public void setExchangeRate(BigDecimal exchangeRate) {
        this.exchangeRate = exchangeRate;
    }
}
