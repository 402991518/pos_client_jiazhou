package com.uxun.pos.view.utils;

import com.uxun.pos.domain.bo.submit.SaleD;
import com.uxun.pos.domain.bo.submit.SaleH;
import com.uxun.pos.domain.bo.submit.SaleP;
import com.uxun.pos.domain.bo.submit.SaleS;
import com.uxun.pos.domain.bo.submit.Submiter;
import com.uxun.pos.domain.bo.submit.YuelangReback;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Proc;
import com.uxun.pos.global.Application;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.view.reback.RebackPay;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class RebackSubmitUtils {

    private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 基本数据
    private static String saleno;
    private static Integer ymd;
    private static String orgcode;
    private static String posno;
    private static String cashier;
    private static Byte datasource;
    private static Date datetime;


    public static String getSubmitJson(Proc proc, List<RebackPay.Pay> pays, String returnMan, String memeberErrInfo, Date datetime) {
        init();
        RebackSubmitUtils.datetime = datetime;
        Submiter submiter = new Submiter(createH(proc, returnMan, memeberErrInfo), createD(proc), createP(proc, pays), null, createS(proc, pays));
        return GsonUtils.getInstance().toJson(submiter);
    }

    public static String getSubmitJson(Proc proc, List<RebackPay.Pay> pays, String returnMan, YuelangReback yuelangReback, Date datetime) {
        init();
        RebackSubmitUtils.datetime = datetime;
        Submiter submiter = new Submiter(createH(proc, returnMan, null), createD(proc), createP(proc, pays), null, createS(proc, pays));
        submiter.yuelangReback = yuelangReback;
        return GsonUtils.getInstance().toJson(submiter);
    }

    // 初始化数据
    private static void init() {
        saleno = ConstantLogin.loginData.saleno;
        ymd = Integer.parseInt(saleno.substring(0, 8));
        orgcode = ConstantLogin.loginData.device.OrgCode;
        posno = ConstantLogin.loginData.device.PosNo;
        cashier = ConstantLogin.loginData.user.UserId;
        datasource = 1;
    }

    // 创建H表
    private static SaleH createH(Proc proc, String returnMan, String memeberErrInfo) {
        SaleH saleH = new SaleH();
        // 基本信息
        saleH.SaleNo = saleno;
        saleH.YMD = ymd;
        saleH.OrgCode = orgcode;
        saleH.PosNo = posno;
        saleH.Cashier = cashier;
        saleH.DataSource = datasource;
        // 时间信息
        saleH.CollectTime = datetime;
        saleH.UploadTime = datetime;
        saleH.BeginDate = datetime;
        saleH.EndDate = datetime;
        // 非空信息
        saleH.DivideAmt = BigDecimal.ZERO;
        saleH.EstimationScale = 0;
        // 销售信息
        saleH.Amount = proc.h.Amount.multiply(new BigDecimal(-1));
        saleH.RealAmt = proc.h.RealAmt.multiply(new BigDecimal(-1));
        saleH.OldSaleNo = proc.h.SaleNo;

        saleH.ReturnMan = returnMan;
        saleH.ExtCol10 = memeberErrInfo;
        saleH.ExtCol1 = Application.getVersionName();
        saleH.ExtCol2 = Application.getVersionCode();
        return saleH;
    }

    //创建D表
    private static List<SaleD> createD(Proc proc) {
        List<Proc.D> ds = proc.ds;
        List<SaleD> saleDs = new ArrayList<>();
        for (int i = 0; i < ds.size(); i++) {
            Proc.D d = ds.get(i);
            SaleD saleD = new SaleD();
            // 基本信息
            saleD.RowNo = (short) (i + 1);
            saleD.SaleNo = saleno;
            saleD.YMD = ymd;
            saleD.OrgCode = orgcode;
            saleD.PosNo = posno;
            saleD.Cashier = cashier;
            saleD.DataSource = datasource;
            saleD.CollectTime = datetime;
            saleD.UploadTime = datetime;
            saleD.OldSaleNo = proc.h.SaleNo;
            saleD.OldRowNo = (short) (i + 1);
            // 销售信息
            saleD.SalePrice = d.SalePrice;
            saleD.ItemQty = d.ItemQty.multiply(new BigDecimal(-1));
            saleD.RealPrice = d.RealPrice;
            saleD.ItemRate = d.ItemRate;
            saleD.RealAmount = d.RealAmount.multiply(new BigDecimal(-1));
            saleD.SaleAmount = d.SaleAmount.multiply(new BigDecimal(-1));
            // 商品信息
            saleD.Depot = d.Depot;
            saleD.ItemColor = d.ItemColor;
            saleD.ItemSize = d.ItemSize;
            saleD.ItemNo = d.ItemNo;
            saleD.BarCode = d.BarCode;
            saleD.PromScale = d.PromScale;
            saleD.PromScore = d.PromScore;
            saleD.ScalePrice = d.ScalePrice;
            saleD.ScoreRule = d.scorerule;
            // 非空值区
            saleD.DivideAmt = new BigDecimal(0);
            saleD.DivideAmt2 = new BigDecimal(0);
            saleD.VendorApp = new BigDecimal(0);
            saleD.VendorVipApp = new BigDecimal(0);
            saleD.ShoppeNo = "";
            saleD.OtherCode = "";
            saleD.CashKinds = "";
            saleD.SalesMan = "";
            // 变价信息
            saleD.RateType = "0";
            saleD.PromNo = "";
            saleD.Integral = new BigDecimal(0);
            saleD.ExtCol1 = d.itemname;
            saleDs.add(saleD);
        }
        return saleDs;
    }

    //创建P表
    private static List<SaleP> createP(Proc proc, List<RebackPay.Pay> pays) {
        List<Proc.P> ps = proc.ps;
        List<SaleP> salePs = new ArrayList<>();
        if (ps != null) {
            for (int i = 0; i < ps.size(); i++) {
                Proc.P p = ps.get(i);
                SaleP saleP = new SaleP();
                // 基本信息
                saleP.RowNo = (short) (i + 1);
                saleP.SaleNo = saleno;
                saleP.YMD = ymd;
                saleP.OrgCode = orgcode;
                saleP.PosNo = posno;
                saleP.Cashier = cashier;
                saleP.DataSource = datasource;
                saleP.CollectTime = datetime;
                saleP.UploadTime = datetime;
                // 付款信息
                saleP.RealMount = p.RealMount.multiply(new BigDecimal(-1));
                saleP.Amount = p.Amount.multiply(new BigDecimal(-1));
                saleP.PayType = p.PayType;
                saleP.ClassNo = p.ClassNo;
                saleP.PayClass = p.PayClass;
                saleP.ExchangeRate = p.ExchangeRate;
                saleP.ScalePrice = p.ScalePrice;
                saleP.PromScale = p.PromScale;
                saleP.ClassNo = p.ClassNo;
                saleP.ExchangeRate = p.ExchangeRate;
                saleP.ScalePrice = p.ScalePrice;
                saleP.PromScale = p.PromScale;
                // 其他信息
                saleP.TermSn = "";// 刷卡流水号(银联)
                saleP.DealerNo = "";// 商户号(银联)
                saleP.TradeTime = null;
                saleP.OldSaleNo = proc.h.SaleNo;
                try {
                    RebackPay.Pay pay = pays.get(i);
                    if (pay.other != null) {
                        String type = pay.type;
                        if ("13".equals(type) || "14".equals(type) || "18".equals(type) || "03".equals(type)) {
                            saleP.DealerNo = processNullText(pay.other.get("merchantId"));//商户号
                            saleP.ExtCol1 = "流水号:" + processNullText(pay.other.get("traceNo"));
                            saleP.ExtCol2 = "参考号:" + processNullText(pay.other.get("referenceNo"));
                            saleP.ExtCol3 = "批次号" + processNullText(pay.other.get("batchNo"));
                            saleP.ExtCol4 = "终端号:" + processNullText(pay.other.get("terminalId"));
                            saleP.ExtCol5 = "商户名称:" + processNullText(pay.other.get("merchantName"));
                            saleP.ExtCol6 = "交易单号:" + processNullText(pay.other.get("transactionNumber"));
                            if ("03".equals(type)) {
                                saleP.TermSn = processNullText(pay.other.get("traceNo"));//流水号
                                saleP.ExtCol7 = "卡号:" + processNullText(pay.other.get("cardNo"));
                            } else {
                                saleP.TermSn = processNullText(pay.other.get("transactionNumber"));//交易单号
                            }
                        }
                    }
                } catch (Exception e) {
                }
                salePs.add(saleP);
            }
        }
        return salePs;
    }

    private static List<SaleS> createS(Proc proc, List<RebackPay.Pay> pays) {
        List<Proc.P> ps = proc.ps;
        List<SaleS> saleSs = new ArrayList<>();
        if (ps != null) {
            for (int i = 0; i < ps.size(); i++) {
                Proc.P p = ps.get(i);
                BigDecimal total = proc.h.RealAmt;//总付款额
                BigDecimal amount = p.RealMount;// 本次付款额度
                BigDecimal added = new BigDecimal(0);// 分摊金额累计
                List<Proc.D> ds = proc.ds;
                for (int j = 0; j < ds.size(); j++) {
                    Proc.D d = ds.get(j);
                    BigDecimal scale;
                    if (total.compareTo(BigDecimal.ZERO) != 0) {
                        scale = d.RealAmount.divide(total, 2, RoundingMode.HALF_UP);// 付款比例
                    } else {
                        scale = p.RealMount;
                    }
                    BigDecimal reals = scale.multiply(amount).divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);// 分摊金额
                    if (j < ds.size() - 1) {
                        added = added.add(reals);
                    } else {
                        reals = amount.subtract(added);
                    }
                    SaleS saleS = new SaleS();
                    // 基本信息
                    saleS.RowNo = (short) (i + 1);
                    saleS.SerialNo = (short) (j + 1);
                    saleS.SaleNo = saleno;
                    saleS.YMD = ymd;
                    saleS.OrgCode = orgcode;
                    saleS.PosNo = posno;
                    saleS.Cashier = cashier;
                    saleS.DataSource = datasource;
                    saleS.CollectTime = datetime;
                    saleS.UploadTime = datetime;
                    // 商品信息
                    saleS.Depot = d.Depot;
                    saleS.ItemNo = d.ItemNo;
                    saleS.PayType = p.PayType;
                    saleS.PayClass = p.PayClass;
                    saleS.ClassNo = p.ClassNo;
                    // 分摊信息
                    saleS.Amount = reals.multiply(new BigDecimal(-1));
                    saleS.RealMount = reals.multiply(new BigDecimal(-1));
                    saleS.RealAmount = p.RealMount.multiply(new BigDecimal(-1));
                    saleS.ExchangeRate = p.ExchangeRate;
                    // 其他信息
                    // 其他信息
                    saleS.TermSn = "";// 刷卡流水号(银联)
                    saleS.DealerNo = "";// 商户号(银联)
                    saleS.TradeTime = null;
                    saleS.OldSaleNo = proc.h.SaleNo;
                    try {
                        RebackPay.Pay pay = pays.get(i);
                        if (pay.other != null) {
                            String type = pay.type;
                            if ("13".equals(type) || "14".equals(type) || "18".equals(type) || "03".equals(type)) {
                                saleS.DealerNo = processNullText(pay.other.get("merchantId"));//商户号
                                saleS.ExtCol1 = "流水号:" + processNullText(pay.other.get("traceNo"));
                                saleS.ExtCol2 = "参考号:" + processNullText(pay.other.get("referenceNo"));
                                saleS.ExtCol3 = "批次号" + processNullText(pay.other.get("batchNo"));
                                saleS.ExtCol4 = "终端号:" + processNullText(pay.other.get("terminalId"));
                                saleS.ExtCol5 = "商户名称:" + processNullText(pay.other.get("merchantName"));
                                saleS.ExtCol6 = "交易单号:" + processNullText(pay.other.get("transactionNumber"));
                                if ("03".equals(type)) {
                                    saleS.TermSn = processNullText(pay.other.get("traceNo"));//流水号
                                    saleS.ExtCol7 = "卡号:" + processNullText(pay.other.get("cardNo"));
                                } else {
                                    saleS.TermSn = processNullText(pay.other.get("transactionNumber"));//交易单号
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                    saleSs.add(saleS);
                }
            }
        }
        return saleSs;
    }


    private static String processNullText(String src) {
        return src != null ? src : "";
    }
}
