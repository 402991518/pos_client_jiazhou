package com.uxun.pos.utils;

import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.TicketConfig;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/4/16.
 */

public class ReprintUtils {

    public static class Bean_P {

        public BigDecimal amount;// 付款金额
        public String descr;// 付款描述

        public String ExtCol1;
        public String ExtCol2;
        public String ExtCol3;
        public String ExtCol4;
        public String ExtCol5;
        public String ExtCol6;

        public Bean_P(BigDecimal amount, String descr) {
            this.amount = amount;
            this.descr = descr;
        }
    }

    public static class Bean_D {
        public String itemno; // 商品条码
        public String itemname; // 商品名称
        public BigDecimal quantity;// 销售数量
        public BigDecimal realPrice;// 商品实价
        public BigDecimal subRealPrice;// 小计实价

        public Bean_D(String itemno, String itemname, BigDecimal quantity, BigDecimal realPrice, BigDecimal subRealPrice) {
            super();
            this.itemno = itemno;
            this.itemname = itemname;
            this.quantity = quantity;
            this.realPrice = realPrice;
            this.subRealPrice = subRealPrice;
        }
    }

    public static class Bean_H {
        public String saleno;// 销售单号
        public String oldSaleno;// 退货单号
        public Date tradeDate;// 交易日期
        public BigDecimal saleAmount;// 应收金额
        public BigDecimal realAmount;// 实收金额

        public Bean_H(String saleno, String oldSaleno, Date tradeDate, BigDecimal saleAmount, BigDecimal realAmount) {
            super();
            this.saleno = saleno;
            this.oldSaleno = oldSaleno;
            this.tradeDate = tradeDate;
            this.saleAmount = saleAmount;
            this.realAmount = realAmount;
        }
    }

    private static final int TEXT_LENGTH = 36;// 字符宽度
    private static final String LINE_SEPARATOR = "*";// 分隔符

    private static ArrayList<String> list;

    private static ReprintUtils.Bean_H h;
    private static List<ReprintUtils.Bean_D> ds;
    private static List<ReprintUtils.Bean_P> ps;


    public static ArrayList<String> print(ReprintUtils.Bean_H h, List<ReprintUtils.Bean_D> ds, List<ReprintUtils.Bean_P> ps, List<String> memberInfo, String posno, String casher, String orgcode) {
        // 初始化
        list = new ArrayList<>();
        ReprintUtils.h = h;
        ReprintUtils.ds = ds;
        ReprintUtils.ps = ps;

        //1.头
        createHead();

        //2.标题
        createTitle(posno, casher, orgcode);
        createSep();

        //3.商品明细
        create_D();
        createSep();

        //4.付款明细
        create_P();
        createSep();

        if (h.oldSaleno != null) {
            list.add("退货单号:" + h.saleno);
            list.add("原销售单号:" + h.oldSaleno);
        } else {
            list.add("销售单号:" + h.saleno);
        }

        list.add("交易时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(h.tradeDate));
        list.add("重印时间:" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        if (memberInfo != null) {
            list.addAll(memberInfo);
        }
        //5.尾
        createFoot();
        return list;
    }

    // 头
    private static void createHead() {
        TicketConfig ticketConfig = ConstantLogin.loginData.ticketConfig;
        center(ticketConfig.TicketHeadTitle);
        center(ticketConfig.TicketHead1Text);
        center(ticketConfig.TicketHead2Text);
        center(ticketConfig.TicketHead3Text);
        center(ticketConfig.TicketHead4Text);
        center(ticketConfig.TicketHead5Text);
        center(ticketConfig.TicketHead6Text);
        center(ticketConfig.TicketHead7Text);
        center(ticketConfig.TicketHead8Text);
        center(ticketConfig.TicketHead9Text);
        center(ticketConfig.TicketHead10Text);
    }

    // 尾
    private static void createFoot() {
        TicketConfig ticketConfig = ConstantLogin.loginData.ticketConfig;
        center(ticketConfig.TicketTail1Text);
        center(ticketConfig.TicketTail2Text);
        center(ticketConfig.TicketTail3Text);
        center(ticketConfig.TicketTail4Text);
        center(ticketConfig.TicketTail5Text);
        center(ticketConfig.TicketTail6Text);
        center(ticketConfig.TicketTail7Text);
        center(ticketConfig.TicketTail8Text);
        center(ticketConfig.TicketTail9Text);
        center(ticketConfig.TicketTail10Text);
    }


    // 收银员 店号 收款机号
    private static void createTitle(String posno, String casher, String orgcode) {
        String text = StringUtils.textCasherOrgcodePosno(casher, orgcode, posno, TEXT_LENGTH);
        list.add(text);
    }

    // 分割行
    private static void createSep() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < TEXT_LENGTH; i++) {
            stringBuilder.append(LINE_SEPARATOR);
        }
        String text = stringBuilder.toString();
        if (!text.equals(StringUtils.NULL)) {
            list.add(text);
        }
    }

    // 文本居中
    private static void center(String text) {
        if (text == null || "".equals(text.trim())) {
            return;
        }
        List<String> temp = StringUtils.center(text.trim(), TEXT_LENGTH);
        for (int i = 0; i < temp.size(); i++) {
            list.add(temp.get(i));
        }
    }

    // 商品明细
    private static void create_D() {
        if (ds != null && ds.size() > 0) {
            for (int i = 0; i < ds.size(); i++) {
                ReprintUtils.Bean_D bean_d = ds.get(i);
                list.add((i + 1) + "." + "条码:" + bean_d.itemno);
                list.add("名称:" + bean_d.itemname);
                list.add("价格:" + bean_d.quantity.stripTrailingZeros().toPlainString() + " * " + bean_d.realPrice + " = " + bean_d.subRealPrice);
            }
        }
    }

    //付款明细
    private static void create_P() {
        list.add("应    付：" + h.saleAmount);
        list.add("实    付：" + h.realAmount);
        if (h.saleAmount.compareTo(h.realAmount) > 0) {
            list.add("优惠金额：" + h.saleAmount.subtract(h.realAmount));
        }
        for (int i = 0; i < ps.size(); i++) {
            ReprintUtils.Bean_P bean_p = ps.get(i);
            if (bean_p.amount.compareTo(new BigDecimal(0)) > 0) {
                list.add("付    款:" + bean_p.descr + "    " + bean_p.amount);
            } else {
                list.add("回    找:" + bean_p.descr + "    " + bean_p.amount);
            }
            processExtCol(bean_p.ExtCol1);
            processExtCol(bean_p.ExtCol2);
            processExtCol(bean_p.ExtCol3);
            processExtCol(bean_p.ExtCol4);
            processExtCol(bean_p.ExtCol5);
            processExtCol(bean_p.ExtCol6);
        }
    }

    //打印扩展字段
    private static void processExtCol(String extCol) {
        if (extCol != null) {
            list.add(extCol);
        }
    }
}
