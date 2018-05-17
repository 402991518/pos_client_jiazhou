package com.uxun.pos.view.utils;

import com.uxun.pos.domain.bo.submit.Submiter;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.view.adapter.PayAdapter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2018/4/2.
 */

public class MemberUils {



    private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 基本数据
    private static String saleno;
    private static Integer ymd;
    private static String orgcode;
    private static String posno;
    private static String cashier;
    private static Byte datasource;
    private static Date datetime;

    public static String getSubmitJson(PayAdapter payAdapter) {

        return GsonUtils.getInstance().toJson("");
    }


}
