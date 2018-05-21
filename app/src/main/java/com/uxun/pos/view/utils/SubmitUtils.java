package com.uxun.pos.view.utils;

import com.uxun.pos.domain.bo.Ware;
import com.uxun.pos.domain.bo.pay.PayBean;
import com.uxun.pos.domain.bo.pay.PayBeanAlipay;
import com.uxun.pos.domain.bo.pay.PayBeanScan;
import com.uxun.pos.domain.bo.pay.PayBeanUnion;
import com.uxun.pos.domain.bo.pay.PayBeanWechat;
import com.uxun.pos.domain.bo.submit.SaleD;
import com.uxun.pos.domain.bo.submit.SaleH;
import com.uxun.pos.domain.bo.submit.SaleP;
import com.uxun.pos.domain.bo.submit.SaleS;
import com.uxun.pos.domain.bo.submit.Submiter;
import com.uxun.pos.domain.bo.submit.YuelangPrize;
import com.uxun.pos.domain.bo.submit.YuelangReback;
import com.uxun.pos.domain.bo.submit.YuelangScore;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Goods;
import com.uxun.pos.global.Application;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.view.adapter.PayAdapter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//提交销售数据的工具类
public class SubmitUtils {

    private static SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    // 基本数据
    private static String saleno;
    private static Integer ymd;
    private static String orgcode;
    private static String posno;
    private static String cashier;
    private static Byte datasource;
    private static Date datetime;

    public static String getSubmitJson(PayAdapter payAdapter, String memberErrMsg) {
        init();
        Submiter submiter = new Submiter(createH(payAdapter, memberErrMsg), createD(payAdapter), createP(payAdapter), null, createS(payAdapter));
        return GsonUtils.getInstance().toJson(submiter);
    }


    public static String getSubmitJson(PayAdapter payAdapter, YuelangScore yuelangScore, List<YuelangPrize> yuelangPrizes, YuelangReback yuelangReback) {
        init();
        Submiter submiter = new Submiter(createH(payAdapter, null), createD(payAdapter), createP(payAdapter), null, createS(payAdapter));
        submiter.yuelangScore = yuelangScore;
        submiter.yuelangPrizes = yuelangPrizes;
        submiter.yuelangReback = yuelangReback;
        return GsonUtils.getInstance().toJson(submiter);
    }

    // 初始化数据
    private static final void init() {
        saleno = ConstantLogin.loginData.saleno;
        ymd = Integer.parseInt(saleno.substring(0, 8));
        orgcode = ConstantLogin.loginData.device.OrgCode;
        posno = ConstantLogin.loginData.device.PosNo;
        cashier = ConstantLogin.loginData.user.UserId;
        datasource = 1;
        datetime = new Date();
    }

    // 创建H表
    private static SaleH createH(PayAdapter payAdapter, String memberErrMsg) {
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
        saleH.BeginDate = payAdapter.getWareAdapter().getBeginData();
        saleH.EndDate = datetime;
        // 非空信息
        saleH.DivideAmt = BigDecimal.ZERO;
        saleH.EstimationScale = 0;
        // 销售信息
        saleH.Amount = payAdapter.getWareAdapter().getTotalSale();
        saleH.RealAmt = payAdapter.getWareAdapter().getTotalReal();
        saleH.ExtCol10 = memberErrMsg;
        saleH.ExtCol1 = Application.getVersionName();
        saleH.ExtCol2 = Application.getVersionCode();
        return saleH;
    }

    private static List<SaleD> createD(PayAdapter payAdapter) {
        int count = payAdapter.getWareAdapter().getCount();
        List<SaleD> saleDs = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Ware ware = (Ware) payAdapter.getWareAdapter().getItem(i);
            Goods goods = ware.getGoods();
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
            // 销售信息
            saleD.SalePrice = goods.SALEPRICE;
            saleD.ItemQty = ware.getQuantity();
            saleD.RealPrice = ware.getRealPrice();
            saleD.ItemRate = ware.getRebate();
            saleD.RealAmount = ware.getSubRealPrice();
            saleD.SaleAmount = ware.getSubSalePrice();
            // 商品信息
            saleD.Depot = goods.DEPOT;
            saleD.ItemColor = goods.ITEMCOLOR;
            saleD.ItemSize = goods.ITEMSIZE;
            saleD.ItemNo = goods.ITEMNO;
            saleD.BarCode = goods.BARCODE;
            saleD.PromScale = goods.PROMSCALE;
            saleD.PromScore = goods.PROMSCORE;
            saleD.ScalePrice = goods.SCALEPRICE;
            saleD.ScoreRule = goods.SCORERULE;
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
            saleD.OldSaleNo = null;
            saleD.OldRowNo = null;
            saleD.Integral = new BigDecimal(0);
            saleD.ExtCol1 = goods.ITEMNAME;

            saleDs.add(saleD);
        }
        return saleDs;
    }

    private static List<SaleP> createP(PayAdapter payAdapter) {
        List<SaleP> salePs = new ArrayList<>();
        int count = payAdapter.getCount();
        for (int i = 0; i < count; i++) {
            SaleP saleP = new SaleP();
            PayBean payBean = (PayBean) payAdapter.getItem(i);
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
            saleP.RealMount = payBean.getAmount();
            saleP.Amount = payBean.getAmount();
            saleP.PayType = payBean.getPayType();
            saleP.ClassNo = "";
            saleP.PayClass = "";
            saleP.ExchangeRate = payBean.getExchangeRate();
            saleP.ScalePrice = new BigDecimal(0);
            saleP.PromScale = new BigDecimal(0);
            saleP.ClassNo = "";
            saleP.ExchangeRate = new BigDecimal(1);
            saleP.ScalePrice = new BigDecimal(0);
            saleP.PromScale = new BigDecimal(0);

            // 其他信息
            saleP.TermSn = "";// 刷卡流水号(银联)
            saleP.DealerNo = "";// 商户号(银联)
            saleP.TradeTime = null;
            saleP.OldSaleNo = "";

            if (payBean instanceof PayBeanWechat) {
                PayBeanWechat payBeanWechat = (PayBeanWechat) payBean;
                saleP.TermSn = payBeanWechat.getTransactionNumber();
                saleP.DealerNo = payBeanWechat.getMerchantId();
                try {
                    saleP.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanWechat.getDateTime());
                } catch (Exception e) {
                }
                saleP.ExtCol1 = "流水号:" + processNullText(payBeanWechat.getTraceNo());
                saleP.ExtCol2 = "参考号:" + processNullText(payBeanWechat.getReferenceNo());
                saleP.ExtCol3 = "批次号" + processNullText(payBeanWechat.getBatchNo());
                saleP.ExtCol4 = "终端号:" + processNullText(payBeanWechat.getTerminalId());
                saleP.ExtCol5 = "商户名称:" + processNullText(payBeanWechat.getMerchantName());
                saleP.ExtCol6 = "交易单号:" + processNullText(payBeanWechat.getTransactionNumber());
            }

            if (payBean instanceof PayBeanAlipay) {
                PayBeanAlipay payBeanAlipay = (PayBeanAlipay) payBean;
                saleP.TermSn = payBeanAlipay.getTransactionNumber();
                saleP.DealerNo = payBeanAlipay.getMerchantId();
                try {
                    saleP.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanAlipay.getDateTime());
                } catch (Exception e) {
                }
                saleP.ExtCol1 = "流水号:" + processNullText(payBeanAlipay.getTraceNo());
                saleP.ExtCol2 = "参考号:" + processNullText(payBeanAlipay.getReferenceNo());
                saleP.ExtCol3 = "批次号" + processNullText(payBeanAlipay.getBatchNo());
                saleP.ExtCol4 = "终端号:" + processNullText(payBeanAlipay.getTerminalId());
                saleP.ExtCol5 = "商户名称:" + processNullText(payBeanAlipay.getMerchantName());
                saleP.ExtCol6 = "交易单号:" + processNullText(payBeanAlipay.getTransactionNumber());
            }

            if (payBean instanceof PayBeanUnion) {
                PayBeanUnion payBeanUnion = (PayBeanUnion) payBean;
                saleP.TermSn = payBeanUnion.getTraceNo();
                saleP.DealerNo = payBeanUnion.getMerchantId();
                try {
                    saleP.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanUnion.getDateTime());
                } catch (Exception e) {
                }
                saleP.ExtCol1 = "流水号:" + processNullText(payBeanUnion.getTraceNo());
                saleP.ExtCol2 = "参考号:" + processNullText(payBeanUnion.getReferenceNo());
                saleP.ExtCol3 = "批次号" + processNullText(payBeanUnion.getBatchNo());
                saleP.ExtCol4 = "终端号:" + processNullText(payBeanUnion.getTerminalId());
                saleP.ExtCol5 = "商户名称:" + processNullText(payBeanUnion.getMerchantName());
                saleP.ExtCol6 = "交易单号:" + processNullText(payBeanUnion.getTransactionNumber());
                saleP.ExtCol7 = "卡号:" + processNullText(payBeanUnion.getCardNo());
            }


            if (payBean instanceof PayBeanScan) {
                PayBeanScan payBeanScan = (PayBeanScan) payBean;
                saleP.TermSn = payBeanScan.getTransactionNumber();
                saleP.DealerNo = payBeanScan.getMerchantId();
                try {
                    saleP.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanScan.getDateTime());
                } catch (Exception e) {
                }
                saleP.ExtCol1 = "流水号:" + processNullText(payBeanScan.getTraceNo());
                saleP.ExtCol2 = "参考号:" + processNullText(payBeanScan.getReferenceNo());
                saleP.ExtCol3 = "批次号" + processNullText(payBeanScan.getBatchNo());
                saleP.ExtCol4 = "终端号:" + processNullText(payBeanScan.getTerminalId());
                saleP.ExtCol5 = "商户名称:" + processNullText(payBeanScan.getMerchantName());
                saleP.ExtCol6 = "交易单号:" + processNullText(payBeanScan.getTransactionNumber());
            }

            salePs.add(saleP);
        }
        return salePs;
    }

    private static List<SaleS> createS(PayAdapter payAdapter) {
        List<SaleS> saleSs = new ArrayList<>();
        int count = payAdapter.getCount();
        for (int i = 0; i < count; i++) {
            PayBean payBean = (PayBean) payAdapter.getItem(i);
            BigDecimal total = payAdapter.getTotal();//总付款额
            BigDecimal amount = payBean.getAmount();// 本次付款额度
            BigDecimal added = new BigDecimal(0);// 分摊金额累计

            for (int j = 0; j < payAdapter.getWareAdapter().getCount(); j++) {
                Ware ware = (Ware) payAdapter.getWareAdapter().getItem(j);
                Goods goods = ware.getGoods();
                BigDecimal scale = ware.getSubRealPrice().divide(total, 2, RoundingMode.HALF_UP);// 付款比例
                BigDecimal reals = scale.multiply(amount).divide(BigDecimal.ONE, 2, RoundingMode.HALF_UP);// 分摊金额
                if (j < payAdapter.getWareAdapter().getCount() - 1) {
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
                saleS.Depot = goods.DEPOT;
                saleS.ItemNo = goods.ITEMNO;
                saleS.PayType = payBean.getPayType();
                saleS.PayClass = "";
                saleS.ClassNo = "";
                // 分摊信息
                saleS.Amount = reals;
                saleS.RealMount = reals;
                saleS.ExchangeRate = payBean.getExchangeRate();
                saleS.RealAmount = payBean.getAmount();
                // 其他信息
                // 其他信息
                saleS.TermSn = "";// 刷卡流水号(银联)
                saleS.DealerNo = "";// 商户号(银联)
                saleS.TradeTime = null;
                saleS.OldSaleNo = "";

                //微信支付信息
                if (payBean instanceof PayBeanWechat) {
                    PayBeanWechat payBeanWechat = (PayBeanWechat) payBean;
                    saleS.TermSn = payBeanWechat.getTransactionNumber();
                    saleS.DealerNo = payBeanWechat.getMerchantId();
                    try {
                        saleS.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanWechat.getDateTime());
                    } catch (Exception e) {
                    }
                    saleS.ExtCol1 = "流水号:" + processNullText(payBeanWechat.getTraceNo());
                    saleS.ExtCol2 = "参考号:" + processNullText(payBeanWechat.getReferenceNo());
                    saleS.ExtCol3 = "批次号" + processNullText(payBeanWechat.getBatchNo());
                    saleS.ExtCol4 = "终端号:" + processNullText(payBeanWechat.getTerminalId());
                    saleS.ExtCol5 = "商户名称:" + processNullText(payBeanWechat.getMerchantName());
                    saleS.ExtCol6 = "交易单号:" + processNullText(payBeanWechat.getTransactionNumber());
                }
                //支付宝支付信息
                if (payBean instanceof PayBeanAlipay) {
                    PayBeanAlipay payBeanAlipay = (PayBeanAlipay) payBean;
                    saleS.TermSn = payBeanAlipay.getTransactionNumber();
                    saleS.DealerNo = payBeanAlipay.getMerchantId();
                    try {
                        saleS.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanAlipay.getDateTime());
                    } catch (Exception e) {
                    }
                    saleS.ExtCol1 = "流水号:" + processNullText(payBeanAlipay.getTraceNo());
                    saleS.ExtCol2 = "参考号:" + processNullText(payBeanAlipay.getReferenceNo());
                    saleS.ExtCol3 = "批次号" + processNullText(payBeanAlipay.getBatchNo());
                    saleS.ExtCol4 = "终端号:" + processNullText(payBeanAlipay.getTerminalId());
                    saleS.ExtCol5 = "商户名称:" + processNullText(payBeanAlipay.getMerchantName());
                    saleS.ExtCol6 = "交易单号:" + processNullText(payBeanAlipay.getTransactionNumber());
                }
                //银联支付信息
                if (payBean instanceof PayBeanUnion) {
                    PayBeanUnion payBeanUnion = (PayBeanUnion) payBean;
                    saleS.TermSn = payBeanUnion.getTraceNo();
                    saleS.DealerNo = payBeanUnion.getMerchantId();
                    try {
                        saleS.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanUnion.getDateTime());
                    } catch (Exception e) {
                    }
                    saleS.ExtCol1 = "流水号:" + processNullText(payBeanUnion.getTraceNo());
                    saleS.ExtCol2 = "参考号:" + processNullText(payBeanUnion.getReferenceNo());
                    saleS.ExtCol3 = "批次号" + processNullText(payBeanUnion.getBatchNo());
                    saleS.ExtCol4 = "终端号:" + processNullText(payBeanUnion.getTerminalId());
                    saleS.ExtCol5 = "商户名称:" + processNullText(payBeanUnion.getMerchantName());
                    saleS.ExtCol6 = "交易单号:" + processNullText(payBeanUnion.getTransactionNumber());
                    saleS.ExtCol7 = "卡号:" + processNullText(payBeanUnion.getCardNo());
                }

                if (payBean instanceof PayBeanScan) {
                    PayBeanScan payBeanScan = (PayBeanScan) payBean;
                    saleS.TermSn = payBeanScan.getTransactionNumber();
                    saleS.DealerNo = payBeanScan.getMerchantId();
                    try {
                        saleS.TradeTime = SIMPLE_DATE_FORMAT.parse(payBeanScan.getDateTime());
                    } catch (Exception e) {
                    }
                    saleS.ExtCol1 = "流水号:" + processNullText(payBeanScan.getTraceNo());
                    saleS.ExtCol2 = "参考号:" + processNullText(payBeanScan.getReferenceNo());
                    saleS.ExtCol3 = "批次号" + processNullText(payBeanScan.getBatchNo());
                    saleS.ExtCol4 = "终端号:" + processNullText(payBeanScan.getTerminalId());
                    saleS.ExtCol5 = "商户名称:" + processNullText(payBeanScan.getMerchantName());
                    saleS.ExtCol6 = "交易单号:" + processNullText(payBeanScan.getTransactionNumber());
                }

                saleSs.add(saleS);
            }
        }
        return saleSs;
    }

    private static String processNullText(String src) {
        return src != null ? src : "";
    }
}
