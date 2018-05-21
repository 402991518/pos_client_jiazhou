package com.uxun.pos.view;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allinpay.usdk.core.data.BaseData;
import com.allinpay.usdk.core.data.Busi_Data;
import com.allinpay.usdk.core.data.RequestData;
import com.allinpay.usdk.core.data.ResponseData;
import com.google.gson.Gson;
import com.uxun.pos.R;
import com.uxun.pos.domain.bo.Ware;
import com.uxun.pos.domain.bo.pay.PayBean;
import com.uxun.pos.domain.bo.pay.PayBeanAlipay;
import com.uxun.pos.domain.bo.pay.PayBeanScan;
import com.uxun.pos.domain.bo.pay.PayBeanUnion;
import com.uxun.pos.domain.bo.pay.PayBeanWechat;
import com.uxun.pos.domain.bo.submit.YuelangPrize;
import com.uxun.pos.domain.bo.submit.YuelangScore;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.constant.ConstantMember;
import com.uxun.pos.domain.constant.ConstantPayAdapter;
import com.uxun.pos.domain.dto.Member;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.HttpClientUtils;
import com.uxun.pos.utils.PrintUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.adapter.PayAdapter;
import com.uxun.pos.view.adapter.WareAdapter;
import com.uxun.pos.view.dialog.DialogYN;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.SubmitUtils;
import com.uxun.pos.view.widget.FailureView;
import com.uxun.pos.view.widget.LoadingView;
import com.uxun.pos.view.widget.Order;
import com.uxun.pos.view.widget.SuccessView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Submit extends RootActivity {

    protected TextView message;
    protected LinearLayout cancel;
    protected TextView rebackAmount;

    protected Order order_1;
    protected Order order_2;
    protected Order order_3;

    protected TextView textView_1;
    protected TextView textView_2;
    protected TextView textView_3;

    protected LoadingView loadingView_1;
    protected LoadingView loadingView_2;
    protected LoadingView loadingView_3;

    protected SuccessView successView_1;
    protected SuccessView successView_2;
    protected SuccessView successView_3;

    protected FailureView failureView_1;
    protected FailureView failureView_2;
    protected FailureView failureView_3;

    protected LinearLayout container_1;
    protected LinearLayout container_2;
    protected LinearLayout container_3;

    private Handler memberInfoResultProcessHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            processMemberInfoResult(msg);
        }
    };


    private String memberErrMsg = null;
    private YuelangScore yuelangScore;
    private List<YuelangPrize> yuelangPrizes;


    private String saleInfo;
    private int printcount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
        submitMemberInfo();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            processPrintResult(resultCode, data);
        }
    }

    //初始化界面
    public void initView() {
        int maxCircleColor = Color.WHITE;
        int minCircleColor = ContextCompat.getColor(Application.getContext(), R.color.default_theme_color);
        order_1.setColor(maxCircleColor, minCircleColor);
        order_2.setColor(maxCircleColor, minCircleColor);
        order_3.setColor(maxCircleColor, minCircleColor);
        loadingView_1.setBackgroundColor(Color.WHITE);
        loadingView_2.setBackgroundColor(Color.WHITE);
        loadingView_3.setBackgroundColor(Color.WHITE);
        BigDecimal total = ConstantPayAdapter.payAdapter.getTotal();
        BigDecimal income = ConstantPayAdapter.payAdapter.getIncome();
        if (income.compareTo(total) > 0) {
            rebackAmount.setVisibility(View.VISIBLE);
            rebackAmount.setText("回找:" + income.subtract(total).toString());
        }
        disableClick();
    }

    //禁止点击
    private void disableClick() {
        cancel.setClickable(false);
        container_1.setClickable(false);
        container_2.setClickable(false);
        container_3.setClickable(false);
    }

    private void initEvent() {
        this.cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                setResult(0);
                finish();
                ConstantLogin.loginData.saleno = saleInfo;
            }
        });
    }

    //提交积分信息
    private void submitMemberInfo() {
        memberErrMsg = null;
        disableClick();
        if (ConstantMember.member == null) {
            textView_1.setText("没有刷会员卡,无需提交积分信息！");
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.VISIBLE);
            failureView_1.setVisibility(View.GONE);
            submitSaleInfo();
            return;
        }
        if (ConstantLogin.loginData.zhaokeyi == null) {
            textView_1.setText("后台没有设置招客易会员接口参数,无法提交积分信息！");
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.GONE);
            failureView_1.setVisibility(View.VISIBLE);
            submitSaleInfo();
            return;
        }
        //1.显示加载圈
        textView_1.setText("正在提交会员积分信息，请稍后...");
        loadingView_1.setVisibility(View.VISIBLE);
        successView_1.setVisibility(View.GONE);
        failureView_1.setVisibility(View.GONE);
        //2.准备数据
        Member member = ConstantMember.member;
        Map<String, Object> wrapper = new HashMap<>(2);
        wrapper.put("cmd_type", "903");
        Map<String, Object> temp = new HashMap<>(4);

        temp.put("MACID", ConstantLogin.loginData.zhaokeyi.macid);//机器ID,招客易系统后台提供
        temp.put("YueLangID", member.YueLangID);
        temp.put("TEMP_CARDNO", member.TEMP_CARDNO);
        temp.put("CARDNO", member.CARDNO);
        temp.put("BCYFJE", ConstantPayAdapter.payAdapter.getTotal().intValue() + "");//本单应付金额（元）
        temp.put("BCXFJE", ConstantPayAdapter.payAdapter.getTotal().intValue() + "");//可参数积分的金额

        int count = ConstantPayAdapter.payAdapter.getCount();
        TreeMap<BigDecimal, String> map = new TreeMap<>();
        for (int i = 0; i < count; i++) {
            PayBean payBean = (PayBean) ConstantPayAdapter.payAdapter.getItem(i);
            String payName;
            switch (payBean.getPayType()) {
                case "01":
                    payName = "现金";
                    break;
                case "03":
                    payName = "银行卡";
                    break;
                case "13":
                    payName = "微信";
                    break;
                case "14":
                    payName = "支付宝";
                    break;
                case "18":
                    payName = "扫码支付";
                    break;
                default:
                    payName = payBean.getPayName();
                    break;
            }
            map.put(payBean.getAmount(), payName);
        }

        temp.put("FKFS", map.lastEntry().getValue());//付款方式（如果招客易系统中不存在收银系统传过来的付款方式时，积分将为0）
        temp.put("REMARKS", ConstantLogin.loginData.saleno);//付款方式（如果招客易系统中不存在收银系统传过来的付款方式时，积分将为0）
        //3.请求网络
        Object[] object = new Object[]{temp};
        wrapper.put("data", object);
        final String jsonParams = GsonUtils.getInstance().toJson(wrapper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                Message message = memberInfoResultProcessHandler.obtainMessage();
                try {
                    message.what = 0;
                    message.obj = HttpClientUtils.requestHttp(ConstantLogin.loginData.zhaokeyi.crm_url, jsonParams);
                } catch (Exception e) {
                    message.what = 1;
                    message.obj = "提交会员信息出错。" + e.getMessage();
                } finally {
                    message.sendToTarget();
                }
            }
        }).start();
    }

    //积分信息结果处理
    private void processMemberInfoResult(Message message) {
        String errMsg = null;
        try {
            if (message.what == 0) {
                Map<String, Object> content = GsonUtils.getInstance().fromJson(message.obj.toString(), Map.class);
                int code = (int) ((double) content.get("Statuscode"));
                if (0 == code) {
                    errMsg = "积分信息提交出错:" + content.get("Message") + "";
                } else {
                    //1.解析结果
                    yuelangScore = new YuelangScore();

                    yuelangScore.orgcode = ConstantLogin.loginData.device.OrgCode;
                    yuelangScore.posno = ConstantLogin.loginData.device.PosNo;
                    yuelangScore.saleno = ConstantLogin.loginData.saleno;

                    yuelangScore.cardno = (String) content.get("CARDNO");//会员卡号
                    yuelangScore.tradeno = (String) content.get("LSHAO");//会员系统流水号
                    yuelangScore.tradeScore = (String) content.get("BCXFJF");//本次获得积分
                    yuelangScore.tradeTime = (String) content.get("XFDATE");//交易时间
                    yuelangScore.tradeDept = (String) content.get("YXFDM");//交易分店
                    yuelangScore.tradeOperator = (String) content.get("USERNAME");//操作员
                    yuelangScore.canuseScore = (String) content.get("KYJF");//卡可用积分
                    yuelangScore.cardBalance = (String) content.get("KJE");//卡余额
                    yuelangScore.payaAmount = (String) content.get("BCYFJE");//本单应付金额
                    yuelangScore.scoreAmount = (String) content.get("BCXFJE");//参数积分金额

                    yuelangScore.Time_Create = new Date();
                    yuelangScore.Create_By=ConstantLogin.loginData.user.UserId;

                    //2.赠券信息
                    try {
                        List list = (List) content.get("data_quan_zs");
                        if (list != null) {
                            yuelangPrizes = new ArrayList<>();
                            for (int i = 0; i < list.size(); i++) {
                                Map<String, String> map = (Map) list.get(i);
                                if (map != null) {
                                    YuelangPrize yuelangPrize = new YuelangPrize();
                                    yuelangPrize.orgcode = ConstantLogin.loginData.device.OrgCode;
                                    yuelangPrize.posno = ConstantLogin.loginData.device.PosNo;
                                    yuelangPrize.saleno = ConstantLogin.loginData.saleno;
                                    yuelangPrize.name = map.get("Q_Name");
                                    yuelangPrize.price = map.get("Q_PRICE");
                                    yuelangPrize.quantity = map.get("QTY");
                                    yuelangPrize.rowno=(i+1);
                                    yuelangPrize.Time_Create = new Date();
                                    yuelangPrize.Create_By=ConstantLogin.loginData.user.UserId;
                                    yuelangPrizes.add(yuelangPrize);
                                }
                            }
                        }
                    } catch (Exception e) {
                    }
                }
            } else {
                errMsg = "积分信息提交出错";
            }
        } catch (Exception e) {
            errMsg = "积分信息提交出错," + e.getMessage();
        }
        //2.处理结果
        if (errMsg != null) {
            textView_1.setText(errMsg);
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.GONE);
            failureView_1.setVisibility(View.VISIBLE);
            memberErrMsg = errMsg;
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = errMsg;
            builder.defaultButtonY = "重新提交积分";
            builder.defaultButtonN = "不提交积分";
            builder.clickYesTitle = "提示";
            builder.clickYesMessage = "你即将重新提交顾客积分信息";
            builder.clickYesButton = "确定";
            builder.clickNoTitle = "警告";
            builder.clickNoMessage = "你取消了提交顾客积分信息！";
            builder.clickNoButton = "确定";
            new DialogYN(this, builder) {
                @Override
                public void clickY() {
                    showAnimateY();
                }

                @Override
                public void clickN() {
                    showAnimateN();
                }

                @Override
                public void clickO(boolean ok) {
                    if (ok) {
                        submitMemberInfo();
                    } else {
                        submitSaleInfo();
                    }
                    dismiss();
                }
            };
        } else {
            textView_1.setText("会员积分信息提交成功");
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.VISIBLE);
            failureView_1.setVisibility(View.GONE);
            submitSaleInfo();
        }
    }

    //提交销售订单信息
    private void submitSaleInfo() {
        disableClick();
        textView_2.setText("正在提交销售订单,请稍后...");
        loadingView_2.setVisibility(View.VISIBLE);
        successView_2.setVisibility(View.GONE);
        failureView_2.setVisibility(View.GONE);

        String json;
        if (memberErrMsg != null) {
            json = SubmitUtils.getSubmitJson(ConstantPayAdapter.payAdapter, memberErrMsg);
        } else {
            json = SubmitUtils.getSubmitJson(ConstantPayAdapter.payAdapter, this.yuelangScore, this.yuelangPrizes, null);
        }

        VolleyUtils.post(NetworkConfig.getUrl() + "submit/submit", new Handler() {
            @Override
            public void handleMessage(Message msg) {
                processSaleInfoResult(msg);
            }
        }, "json", json);
    }

    //处理销售订单信息
    private void processSaleInfoResult(Message message) {
        //1.解析结果
        String errMsg = null;
        try {
            if (message.what == 0) {
                Map<String, Object> content = GsonUtils.getInstance().fromJson(message.obj.toString(), Map.class);
                String code = (String) content.get("code");
                if ("1".equals(code)) {
                    errMsg = content.get("message") + "";
                } else if ("2".equals(code)) {
                    errMsg = "RPC服务出错！" + content.get("message");
                } else if ("0".equals(code)) {
                    Gson gson = GsonUtils.getInstance();
                    saleInfo = gson.fromJson(gson.toJson(content.get("data")), String.class);
                }
            } else {
                errMsg = message.obj.toString() + "";
            }
        } catch (Exception e) {
            errMsg = "应用程序出错！" + e.getMessage();
        }
        //2.处理结果
        if (errMsg != null) {
            textView_2.setText("销售订单提交出错:" + errMsg);
            loadingView_2.setVisibility(View.GONE);
            successView_2.setVisibility(View.GONE);
            failureView_2.setVisibility(View.VISIBLE);
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = "销售订单提交出错:" + errMsg;
            builder.defaultButtonY = "重新提交订单";
            builder.defaultButtonN = "重新提交订单";
            builder.clickYesTitle = "警告";
            builder.clickYesMessage = "你即将重新提交销售订单！";
            builder.clickYesButton = "确定";
            builder.clickNoTitle = "警告";
            builder.clickNoMessage = "你即将重新提交销售订单！";
            builder.clickNoButton = "确定";
            new DialogYN(this, builder) {
                @Override
                public void clickY() {
                    submitSaleInfo();
                    dismiss();
                }

                @Override
                public void clickN() {
                    submitSaleInfo();
                    dismiss();
                }
            };
        } else {
            textView_2.setText("销售订单信息提交成功");
            loadingView_2.setVisibility(View.GONE);
            successView_2.setVisibility(View.VISIBLE);
            failureView_2.setVisibility(View.GONE);
            printSaleInfo();
        }
    }

    //打印销售小票
    private void printSaleInfo() {
        disableClick();
        textView_3.setText("正在打印销售订单，请稍后...");
        loadingView_3.setVisibility(View.VISIBLE);
        successView_3.setVisibility(View.GONE);
        failureView_3.setVisibility(View.GONE);
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.allinpay.usdk", "com.allinpay.usdk.MainActivity"));
            RequestData data = new RequestData();
            data.putValue(BaseData.BUSINESS_ID, Busi_Data.BUSI_MANAGER_SERVICE_PRINT);
            data.putValue("PRINT_VERSION", 0);
            data.putValue(BaseData.PRINT_APPEND_PAGE, 1);
            data.putValue("PRINT_APPEND_BOLD", false);
            data.putValue("PRINT_APPEND_SIZE", 32);
            data.putValue("PRINT_APPEND_LOCATE", 0);
            String printData = createPrintData();
            data.putValue("PRINT_APPEND_TEXT", printData);
            Bundle bundle = new Bundle();
            bundle.putSerializable(RequestData.KEY_ERTRAS, data);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        } catch (Exception e) {
            cancel.setClickable(true);
            textView_3.setText("本机尚未安装USDK,无法打印！");
            loadingView_3.setVisibility(View.VISIBLE);
            successView_3.setVisibility(View.GONE);
            failureView_3.setVisibility(View.GONE);
        }
    }

    //6.处理打印销售订单结果
    private void processPrintResult(int resultCode, Intent data) {
        boolean printResult = false;
        try {
            Bundle extras = data.getExtras();
            if (resultCode == 0 && extras != null && "00".equals(((ResponseData) extras.getSerializable(ResponseData.KEY_ERTRAS)).getValue(BaseData.REJCODE))) {
                printResult = true;
            }
        } catch (Exception e) {
        }

        if (!printResult) {
            textView_3.setText("销售订单打印出错！");
            loadingView_3.setVisibility(View.GONE);
            successView_3.setVisibility(View.GONE);
            failureView_3.setVisibility(View.VISIBLE);

            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = "销售订单打印出错";
            builder.defaultButtonY = "重新打印销";
            builder.defaultButtonN = "取消打印";
            new DialogYN(this, builder) {
                @Override
                public void clickY() {
                    printSaleInfo();
                    dismiss();
                }

                @Override
                public void clickN() {
                    dismiss();
                }
            };

        } else {
            cancel.setClickable(true);
            message.setText("销售订单处理完成");
            textView_3.setText("销售订单打印完成");
            loadingView_3.setVisibility(View.GONE);
            successView_3.setVisibility(View.VISIBLE);
            failureView_3.setVisibility(View.GONE);
            printcount++;

            Byte PrintTicketNum = ConstantLogin.loginData.device.PrintTicketNum;
            if (PrintTicketNum == null || PrintTicketNum < 1) {
                PrintTicketNum = 1;
            }
            if (printcount < PrintTicketNum) {
                DialogYN.Builder builder = new DialogYN.Builder();
                builder.defaultTitle = "提示！";
                builder.defaultMessage = "是否打印第" + (printcount + 1) + "联销售小票";
                builder.defaultButtonY = "打印";
                builder.defaultButtonN = "不打印";
                new DialogYN(this, builder) {
                    @Override
                    public void clickY() {
                        printSaleInfo();
                        dismiss();
                    }

                    @Override
                    public void clickN() {
                        dismiss();
                    }
                };
            }
        }
    }


    //获取要打印的数据
    private String createPrintData() {
        PayAdapter payAdapter = ConstantPayAdapter.payAdapter;
        WareAdapter wareAdapter = payAdapter.getWareAdapter();
        PrintUtils.Bean_H bean_h = new PrintUtils.Bean_H(ConstantLogin.loginData.saleno, null, wareAdapter.getBeginData(), wareAdapter.getTotalSale(), wareAdapter.getTotalReal());
        List<PrintUtils.Bean_D> ds = new ArrayList<>();
        for (int i = 0; i < wareAdapter.getCount(); i++) {
            Ware ware = (Ware) wareAdapter.getItem(i);
            ds.add(new PrintUtils.Bean_D(ware.getGoods().BARCODE, ware.getGoods().ITEMNAME, ware.getQuantity(), ware.getRealPrice(), ware.getSubRealPrice()));
        }
        List<PrintUtils.Bean_P> ps = new ArrayList<>();
        for (int i = 0; i < payAdapter.getCount(); i++) {
            PayBean payBean = (PayBean) payAdapter.getItem(i);
            PrintUtils.Bean_P bean_p = new PrintUtils.Bean_P(payBean.getAmount(), payBean.getPayName());
            if (payBean instanceof PayBeanWechat) {
                PayBeanWechat payBeanWechat = (PayBeanWechat) payBean;
                bean_p.ExtCol1 = "流水号:" + processNullText(payBeanWechat.getTraceNo());
                bean_p.ExtCol2 = "参考号:" + processNullText(payBeanWechat.getReferenceNo());
                bean_p.ExtCol3 = "批次号" + processNullText(payBeanWechat.getBatchNo());
                bean_p.ExtCol4 = "终端号:" + processNullText(payBeanWechat.getTerminalId());
                bean_p.ExtCol5 = "商户名称:" + processNullText(payBeanWechat.getMerchantName());
                bean_p.ExtCol6 = "交易单号:" + processNullText(payBeanWechat.getTransactionNumber());
            }

            if (payBean instanceof PayBeanAlipay) {
                PayBeanAlipay payBeanAlipay = (PayBeanAlipay) payBean;
                bean_p.ExtCol1 = "流水号:" + processNullText(payBeanAlipay.getTraceNo());
                bean_p.ExtCol2 = "参考号:" + processNullText(payBeanAlipay.getReferenceNo());
                bean_p.ExtCol3 = "批次号" + processNullText(payBeanAlipay.getBatchNo());
                bean_p.ExtCol4 = "终端号:" + processNullText(payBeanAlipay.getTerminalId());
                bean_p.ExtCol5 = "商户名称:" + processNullText(payBeanAlipay.getMerchantName());
                bean_p.ExtCol6 = "交易单号:" + processNullText(payBeanAlipay.getTransactionNumber());
            }

            if (payBean instanceof PayBeanUnion) {
                PayBeanUnion payBeanUnion = (PayBeanUnion) payBean;
                bean_p.ExtCol1 = "流水号:" + processNullText(payBeanUnion.getTraceNo());
                bean_p.ExtCol2 = "参考号:" + processNullText(payBeanUnion.getReferenceNo());
                bean_p.ExtCol3 = "批次号" + processNullText(payBeanUnion.getBatchNo());
                bean_p.ExtCol4 = "终端号:" + processNullText(payBeanUnion.getTerminalId());
                bean_p.ExtCol5 = "商户名称:" + processNullText(payBeanUnion.getMerchantName());
                bean_p.ExtCol6 = "交易单号:" + processNullText(payBeanUnion.getTransactionNumber());
            }

            if (payBean instanceof PayBeanScan) {
                PayBeanScan payBeanScan = (PayBeanScan) payBean;
                bean_p.ExtCol1 = "流水号:" + processNullText(payBeanScan.getTraceNo());
                bean_p.ExtCol2 = "参考号:" + processNullText(payBeanScan.getReferenceNo());
                bean_p.ExtCol3 = "批次号" + processNullText(payBeanScan.getBatchNo());
                bean_p.ExtCol4 = "终端号:" + processNullText(payBeanScan.getTerminalId());
                bean_p.ExtCol5 = "商户名称:" + processNullText(payBeanScan.getMerchantName());
                bean_p.ExtCol6 = "交易单号:" + processNullText(payBeanScan.getTransactionNumber());
            }
            ps.add(bean_p);
        }

        ArrayList<String> list;
        if (memberErrMsg != null) {
            list = PrintUtils.print(bean_h, ds, ps, memberErrMsg);
        } else {
            list = PrintUtils.print(bean_h, ds, ps, this.yuelangScore, this.yuelangPrizes, null);
        }
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            result.append(list.get(i) + "\r\n");
        }
        return result.toString();
    }


    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {

    }

    private static String processNullText(String src) {
        return src != null ? src : "";
    }
}
