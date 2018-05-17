package com.uxun.pos.domain.bo.pay;

import java.math.BigDecimal;

public class PayBeanAlipay extends PayBean {

    private String transType;//交易类型,如微信支付、支付宝支付、通联钱包支付
    private String traceNo;//流水号
    private String referenceNo;//参考号
    private String batchNo;// 批次号
    private String terminalId;// 终端号
    private String merchantId;//商户号
    private String merchantName;//商户名称
    private String transactionNumber;//交易单号
    private String dateTime;// 交易时间

    public PayBeanAlipay(String payType, String payName, BigDecimal amount) {
        super(payType, payName, amount);
    }

    public String getTransType() {
        return transType;
    }

    public void setTransType(String transType) {
        this.transType = transType;
    }

    public String getTraceNo() {
        return traceNo;
    }

    public void setTraceNo(String traceNo) {
        this.traceNo = traceNo;
    }

    public String getReferenceNo() {
        return referenceNo;
    }

    public void setReferenceNo(String referenceNo) {
        this.referenceNo = referenceNo;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }
}
