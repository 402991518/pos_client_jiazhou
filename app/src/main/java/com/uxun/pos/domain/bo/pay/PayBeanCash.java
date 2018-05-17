package com.uxun.pos.domain.bo.pay;

import java.math.BigDecimal;

/**
 * Created by Administrator on 2018/3/30.
 */

public class PayBeanCash extends PayBean {

    public PayBeanCash(String payType, String payName, BigDecimal amount) {
        super(payType, payName, amount);
    }
}
