package com.uxun.pos.domain.bo.submit;


import java.util.Date;

public class YuelangReback {

    public String orgcode;
    public String posno;
    public String saleno;

    public String cardno; // 会员卡号
    public String tradeno;// 退单流水号
    public String tradeTime;// 退单时间
    public String tradeDept;// 操作分店
    public String tradeScore;// 本次退积分
    public String canuseScore;// 现在卡可用积分
    public String scoreAmount;// 参数积分金额

    public String Create_By;
    public Date Time_Create;
}
