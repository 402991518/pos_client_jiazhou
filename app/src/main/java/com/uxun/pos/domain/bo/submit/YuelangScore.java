package com.uxun.pos.domain.bo.submit;


import java.util.Date;

public class YuelangScore {

    public String orgcode;
    public String posno;
    public String saleno;

    public String cardno; // 会员卡号
    public String tradeno; // 会员系统流水号
    public String tradeScore;// 本次获得积分
    public String tradeTime;// 交易时间
    public String tradeDept;// 交易分店
    public String tradeOperator;// 操作员
    public String canuseScore;// 卡可用积分
    public String cardBalance; // 卡余额
    public String payaAmount;// 本单应付金额
    public String scoreAmount;// 参数积分金额

    public String Create_By;
    public Date Time_Create;
}
