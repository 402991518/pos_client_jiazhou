package com.uxun.pos.utils;


import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.PayType;

import java.util.ArrayList;
import java.util.List;

public class PayTypeUtisl {

    // private static List<PayType> list = ConstantLogin.loginData.payTypes;

    public static final String KEY_01 = "01";//现金--
    public static final String KEY_02 = "02";//港币--
    public static final String KEY_03 = "03";//银行卡--
    public static final String KEY_04 = "04";//储值卡--
    public static final String KEY_05 = "05";//银行转帐--
    public static final String KEY_06 = "06";//积分--

    public static final String KEY_08 = "08";//美元--
    public static final String KEY_09 = "09";//支票--

    public static final String KEY_11 = "11";//零钱包--
    public static final String KEY_12 = "12";//现金券--
    public static final String KEY_13 = "13";//微信支付--
    public static final String KEY_14 = "14";//支付宝--
    public static final String KEY_15 = "15";//红包--
    public static final String KEY_16 = "16";//储值透支
    public static final String KEY_17 = "17";//信用卡--
    public static final String KEY_18 = "18";//银行二维码--

    private static List<PayType> init() {
        List<PayType> list = new ArrayList<>();
        if (list.size() <= 0) {
            List<PayType> temps = ConstantLogin.loginData.payTypes;
            for (int i = 0; i < temps.size(); i++) {
                PayType payType = temps.get(i);
                if (payType != null) {
                    if (payType.PayType != null && (!"".equals(payType.PayType)) && payType.PayDescr != null && (!"".equals(payType.PayDescr))) {
                        list.add(payType);
                    }
                }
            }
        }
        return list;
    }

    public static List<PayType> getList() {
        return init();
    }

    //根据付款编码查找付款类别
    public static PayType getPayType(String type) {
        List<PayType> list = init();
        for (int i = 0; i < list.size(); i++) {
            PayType payType = list.get(i);
            if (type.equals(payType.PayType)) {
                return payType;
            }
        }
        return null;
    }
}
