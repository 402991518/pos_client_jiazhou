package com.uxun.pos.view.reback;


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
import com.google.gson.reflect.TypeToken;
import com.uxun.pos.R;
import com.uxun.pos.domain.bo.submit.YuelangReback;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Proc;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.HttpClientUtils;
import com.uxun.pos.utils.PrintUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.dialog.DialogYN;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.RebackSubmitUtils;
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

public class RebackSubmit extends RootActivity {


    private Handler memberInfoResultProcessHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            processMemberInfoResult(msg);
        }
    };


    protected TextView message;
    protected LinearLayout cancel;

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

    private String saleInfo;
    private String memeberErrInfo = null;
    private YuelangReback yuelangReback;


    private String returnMan;
    private Proc proc; //退货原始数据
    private List<RebackPay.Pay> pays; //退货付款数据

    private int printcount;

    private Date datetime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initData();
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

    //初始化数据
    private void initData() {
        returnMan = this.getIntent().getStringExtra("returnMan");
        String json1 = this.getIntent().getStringExtra("json1");
        String json2 = this.getIntent().getStringExtra("json2");
        proc = GsonUtils.getInstance().fromJson(json1, Proc.class);
        pays = GsonUtils.getInstance().fromJson(json2, new TypeToken<List<RebackPay.Pay>>() {
        }.getType());
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
                setResult(1);
                finish();
                ConstantLogin.loginData.saleno = saleInfo;
            }
        });
    }

    //1.扣除积分信息
    private void submitMemberInfo() {
        memeberErrInfo = null;
        disableClick();
        if (proc.yuelangScore == null) {
            textView_1.setText("原单没有参与积分,无需扣减积分!");
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.VISIBLE);
            failureView_1.setVisibility(View.GONE);
            submitSaleInfo();
            return;
        }

        if (ConstantLogin.loginData.zhaokeyi == null) {
            textView_1.setText("后台没有设置招客易会员接口参数,无法扣减积分");
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.GONE);
            failureView_1.setVisibility(View.VISIBLE);
            submitSaleInfo();
            return;
        }
        //1.显示加载圈
        textView_1.setText("正在请求扣除会员积分信息，请稍后...");
        loadingView_1.setVisibility(View.VISIBLE);
        successView_1.setVisibility(View.GONE);
        failureView_1.setVisibility(View.GONE);
        //2.准备数据
        Map<String, Object> wrapper = new HashMap<>(2);
        wrapper.put("cmd_type", "904");
        Map<String, Object> temp = new HashMap<>(4);
        temp.put("MACID", ConstantLogin.loginData.zhaokeyi.macid);//机器ID,招客易系统后台提供
        temp.put("UCARDNO", ConstantLogin.loginData.zhaokeyi.ucardno);//操作员ID,招客易系统后台提供
        temp.put("UPASS", ConstantLogin.loginData.zhaokeyi.upass);//操作员登录密码,招客易系统后台提供
        temp.put("CARDNO", proc.yuelangScore.cardno);//卡面号,真实卡号
        temp.put("LSHAO", proc.yuelangScore.tradeno);//流水号,消费奖励的流水号
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
                    message.obj = "扣减积分出错," + e.getMessage();
                } finally {
                    message.sendToTarget();
                }
            }
        }).start();
    }

    //2.积分扣减结果处理
    private void processMemberInfoResult(Message message) {
        String errMsg = null;
        try {
            if (message.what == 0) {
                Map<String, Object> content = GsonUtils.getInstance().fromJson(message.obj.toString(), Map.class);
                int code = (int) ((double) content.get("Statuscode"));
                if (0 == code) {
                    errMsg = "扣减积分出错:" + content.get("Message") + "";
                } else {
                    //1.解析结果
                    yuelangReback = new YuelangReback();

                    yuelangReback.orgcode = ConstantLogin.loginData.device.OrgCode;
                    yuelangReback.posno = ConstantLogin.loginData.device.PosNo;
                    yuelangReback.saleno = ConstantLogin.loginData.saleno;

                    yuelangReback.cardno = (String) content.get("CARDNO");//会员卡号
                    yuelangReback.tradeno = (String) content.get("LSHAO");// 退单流水号
                    yuelangReback.tradeTime = (String) content.get("XFDATE");//退单时间
                    yuelangReback.tradeDept = (String) content.get("YXFDM");//交易分店

                    yuelangReback.tradeScore = (String) content.get("BCXFJF");//本次退积分
                    yuelangReback.canuseScore = (String) content.get("KYJF");//现在卡可用积分
                    yuelangReback.scoreAmount = (String) content.get("BCXFJE");//参数积分金额

                    yuelangReback.Time_Create = new Date();
                    yuelangReback.Create_By = ConstantLogin.loginData.user.UserId;
                }
            } else {
                errMsg = message.obj + "";
            }
        } catch (Exception e) {
            errMsg = "扣减积分出错," + e.getMessage();
        }
        //2.处理结果
        if (errMsg != null) {
            textView_1.setText(errMsg);
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.GONE);
            failureView_1.setVisibility(View.VISIBLE);
            memeberErrInfo = errMsg;
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = errMsg;
            builder.defaultButtonY = "重新扣减积分";
            builder.defaultButtonN = "不扣减积分";
            builder.clickYesTitle = "提示";
            builder.clickYesMessage = "你将重新发起扣除积分的请求！";
            builder.clickYesButton = "确定";
            builder.clickNoTitle = "警告";
            builder.clickNoMessage = "本退货单将不会扣减积分";
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
            textView_1.setText("会员积分扣减成功");
            loadingView_1.setVisibility(View.GONE);
            successView_1.setVisibility(View.VISIBLE);
            failureView_1.setVisibility(View.GONE);
            submitSaleInfo();
        }
    }

    //提交退货订单信息
    private void submitSaleInfo() {
        disableClick();
        textView_2.setText("正在提交退货订单,请稍后...");
        loadingView_2.setVisibility(View.VISIBLE);
        successView_2.setVisibility(View.GONE);
        failureView_2.setVisibility(View.GONE);

        String json;
        datetime = new Date();
        if (memeberErrInfo != null && !"".equals(memeberErrInfo)) {
            json = RebackSubmitUtils.getSubmitJson(proc, this.pays, this.returnMan, this.memeberErrInfo, datetime);
        } else {
            json = RebackSubmitUtils.getSubmitJson(proc, this.pays, this.returnMan, this.yuelangReback, datetime);
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
            textView_2.setText("退货订单提交出错:" + errMsg);
            loadingView_2.setVisibility(View.GONE);
            successView_2.setVisibility(View.GONE);
            failureView_2.setVisibility(View.VISIBLE);
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = "退货订单提交出错:" + errMsg;
            builder.defaultButtonY = "重新提交订单";
            builder.defaultButtonN = "重新提交订单";
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
            textView_2.setText("退货订单提交成功");
            loadingView_2.setVisibility(View.GONE);
            successView_2.setVisibility(View.VISIBLE);
            failureView_2.setVisibility(View.GONE);
            printSaleInfo();
        }
    }

    //打印销售小票
    private void printSaleInfo() {
        disableClick();
        textView_3.setText("正在打印退货订单，请稍后...");
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
            textView_3.setText("退货订单打印出错！");
            loadingView_3.setVisibility(View.GONE);
            successView_3.setVisibility(View.GONE);
            failureView_3.setVisibility(View.VISIBLE);

            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = "退货订单打印出错";
            builder.defaultButtonY = "重新打印退货订单";
            builder.defaultButtonN = "取消打印";
            new DialogYN(this, builder) {
                @Override
                public void clickY() {
                    printSaleInfo();
                    dismiss();
                }

                @Override
                public void clickN() {
                    message.setText("退货完成,请返回！");
                    cancel.setClickable(true);
                    dismiss();
                }
            };
        } else {
            cancel.setClickable(true);
            message.setText("退货完成,请返回！");
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
                builder.defaultMessage = "是否打印第" + (printcount + 1) + "联退货小票";
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


    private String createPrintData() {
        PrintUtils.Bean_H print_h = new PrintUtils.Bean_H(ConstantLogin.loginData.saleno, proc.h.SaleNo, datetime, proc.h.Amount.multiply(new BigDecimal(-1)), proc.h.RealAmt.multiply(new BigDecimal(-1)));
        List<PrintUtils.Bean_D> print_ds = new ArrayList<>();
        List<Proc.D> ds = proc.ds;
        for (int i = 0; i < ds.size(); i++) {
            Proc.D d = ds.get(i);
            print_ds.add(new PrintUtils.Bean_D(d.BarCode, d.itemname, d.SaleQty.multiply(new BigDecimal(-1)), d.RealPrice, d.SalePrice.multiply(new BigDecimal(-1))));
        }
        List<PrintUtils.Bean_P> print_ps = new ArrayList<>();
        List<Proc.P> ps = proc.ps;
        if (ps != null) {
            for (int i = 0; i < ps.size(); i++) {
                Proc.P p = ps.get(i);
                PrintUtils.Bean_P printP = new PrintUtils.Bean_P(p.RealMount.multiply(new BigDecimal(-1)), p.PayDescr);
                RebackPay.Pay pay = this.pays.get(i);
                if (pay.other != null) {
                    printP.ExtCol1 = "流水号:" + processNullText(pay.other.get("traceNo"));
                    printP.ExtCol2 = "参考号:" + processNullText(pay.other.get("referenceNo"));
                    printP.ExtCol3 = "批次号:" + processNullText(pay.other.get("batchNo"));
                    printP.ExtCol4 = "终端号:" + processNullText(pay.other.get("terminalId"));
                    printP.ExtCol5 = "商户名称:" + processNullText(pay.other.get("merchantName"));
                    printP.ExtCol6 = "交易单号:" + processNullText(pay.other.get("transactionNumber"));
                }
                print_ps.add(printP);
            }
        }

        ArrayList<String> list;
        if (memeberErrInfo != null && !"".equals(memeberErrInfo)) {
            list = PrintUtils.print(print_h, print_ds, print_ps, memeberErrInfo);
        } else {
            list = PrintUtils.print(print_h, print_ds, print_ps, null, null, yuelangReback);
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
