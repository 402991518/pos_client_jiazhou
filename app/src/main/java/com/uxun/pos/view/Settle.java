package com.uxun.pos.view;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.bo.pay.PayBean;
import com.uxun.pos.domain.bo.pay.PayBeanAlipay;
import com.uxun.pos.domain.bo.pay.PayBeanCash;
import com.uxun.pos.domain.bo.pay.PayBeanScan;
import com.uxun.pos.domain.bo.pay.PayBeanUnion;
import com.uxun.pos.domain.bo.pay.PayBeanWechat;
import com.uxun.pos.domain.constant.ConstantPayAdapter;
import com.uxun.pos.domain.constant.ConstantWareAdapter;
import com.uxun.pos.domain.dto.PayType;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.PayTypeUtisl;
import com.uxun.pos.view.adapter.PayAdapter;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.DialogYN;
import com.uxun.pos.view.dialog.PayCash;
import com.uxun.pos.view.dialog.PayMove;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.widget.CustomButtom;
import com.uxun.pos.view.widget.listview.ScrollerLayout;
import com.uxun.pos.view.widget.listview.ScrollerListView;
import com.uxun.pos.view.widget.listview.ScrollerMenu;

import java.math.BigDecimal;

//结算界面
public class Settle extends RootActivity {

    private PayAdapter payAdapter;

    protected TextView msg;
    protected CustomButtom back;
    protected RelativeLayout backContainer;

    protected TextView total;
    protected ScrollerListView listView;

    private boolean submit;

    protected TextView resubmit;
    protected TextView complete;
    protected View space;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initView();
        this.initEvent();
    }

    //初始化组件
    private void initView() {
        payAdapter = new PayAdapter(listView, ConstantWareAdapter.wareAdapter);
        updateUI();
        if (payAdapter.getTotal().compareTo(payAdapter.getIncome()) == 0) {
            submit();
        }
        total.setGravity(Gravity.CENTER | Gravity.LEFT);
    }

    //初始化事件
    private void initEvent() {
        listView.addScrollerMenuBuilders(new ScrollerMenu.Builder("撤销付款", Color.WHITE, Color.rgb(244, 68, 68), 252, new ScrollerMenu.OnMenuClickListener() {
            @Override
            public void onClick() {
                onDelButtonClickResult(payAdapter.get().getPayType());
            }
        }));
        back.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if (submit) {
                    setResult(Activity.RESULT_OK);
                    finish();
                } else {
                    back();
                }
            }
        });
        back.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    backContainer.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.default_bg_color));
                    msg.setBackgroundColor(ContextCompat.getColor(Application.getContext(), R.color.default_bg_color));
                }
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    backContainer.setBackgroundColor(Color.parseColor("#C1C1C1"));
                    msg.setBackgroundColor(Color.parseColor("#C1C1C1"));
                }
                return false;
            }
        });
        resubmit.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                submit();
            }
        });
        complete.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                setResult(Activity.RESULT_OK);
                finish();
            }
        });
    }

    //删除按钮事件响应
    public void onDelButtonClickResult(String keyCode) {
        if (submit) {
            new DialogOK(this, "提示", "销售完成，不允许撤销");
            ScrollerLayout item = (ScrollerLayout) listView.getChildAt(payAdapter.getCursor());
            item.smoothCloseMenu();
            return;
        }
        //1.现金撤销
        if (PayTypeUtisl.KEY_01.equals(keyCode)) {
            openCashCancelUI();
        }
        //2.微信付款撤销
        else if (PayTypeUtisl.KEY_13.equals(keyCode)) {
            openWechatCancelUI();
        }
        //3.支付宝付款撤销
        else if (PayTypeUtisl.KEY_14.equals(keyCode)) {
            openAlipayCancelUI();
        }
        //4.银联付款撤销
        else if (PayTypeUtisl.KEY_03.equals(keyCode)) {
            openUnionCancelUI();
        }
        //5.银联扫码付款撤销
        else if (PayTypeUtisl.KEY_18.equals(keyCode)) {
            openUnionScanCancelUI();
        }
    }

    //付款按钮事件响应
    public void onPayButtonClickResult(String keyCode) {
        if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) < 0) {
            //1.现金
            if (keyCode.equals(PayTypeUtisl.KEY_01)) {
                openCashPayUI();
            }
            //2.微信支付
            if (keyCode.equals(PayTypeUtisl.KEY_13)) {
                openWechatPayUI();
            }
            //3.支付宝支付
            if (keyCode.equals(PayTypeUtisl.KEY_14)) {
                openAlipayPayUI();
            }
            //4.支付宝支付
            if (keyCode.equals(PayTypeUtisl.KEY_03)) {
                openUnionPayUI();
            }
            //5.银行扫码撤销
            if (keyCode.equals(PayTypeUtisl.KEY_18)) {
                openPayScanUI();
            }
        }
    }

    //响应Activity结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //1.微信支付返回结果
        if (requestCode == 1) {
            processWechatPayResult(resultCode, data);
        }
        //2.支付宝支付返回结果
        else if (requestCode == 2) {
            processAlipayPayResult(resultCode, data);
        }
        //3.银行卡支付返回结果
        else if (requestCode == 3) {
            processUnionPayResult(resultCode, data);
        }
        //4.银行二维码付返回结果
        else if (requestCode == 8) {
            processUnionScanResult(resultCode, data);
        }
        //5.微信支付撤销
        else if (requestCode == 4) {
            processWechatCancelResult(resultCode, data);
        }
        //6.支付宝撤销
        else if (requestCode == 5) {
            processAlipayCancelResult(resultCode, data);
        }
        //7.银行卡撤销
        else if (requestCode == 6) {
            processUnionCancelResult(resultCode, data);
        }
        //8.银行卡扫码撤销
        else if (requestCode == 9) {
            processUnionScanCancelResult(resultCode, data);
        }
        //9.
        else if (requestCode == 7) {
            if (resultCode == 0) {
                submit = true;
                complete.setVisibility(View.VISIBLE);
                resubmit.setVisibility(View.GONE);
                space.setVisibility(View.VISIBLE);
            } else {
                complete.setVisibility(View.GONE);
                resubmit.setVisibility(View.VISIBLE);
                space.setVisibility(View.VISIBLE);
            }
        }
    }


    //删除付款方式
    private void delPayBean() {
        complete.setVisibility(View.GONE);
        resubmit.setVisibility(View.GONE);
        space.setVisibility(View.GONE);
        payAdapter.del();
        updateUI();
        int count = payAdapter.getCount();
        if (count > 0) {
            PayBean last = (PayBean) payAdapter.getItem(count - 1);
            if (last.getAmount().compareTo(BigDecimal.ZERO) < 0) {
                payAdapter.delLast();
                BigDecimal new_reback = payAdapter.getTotal().subtract(payAdapter.getIncome());
                if (new_reback.compareTo(BigDecimal.ZERO) < 0) {
                    this.payAdapter.add(new PayBean(PayTypeUtisl.KEY_01, "现金支付", new_reback));
                    updateUI();
                    if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) >= 0) {
                        submit();
                    }
                }
            }
        }
    }

    /*
    * 以下为打开支付界面
    * */

    //打开现金支付UI
    private void openCashPayUI() {
        final BigDecimal need = payAdapter.getTotal().subtract(payAdapter.getIncome());
        new PayCash(this, need) {
            @Override
            public void processPayCashResult(BigDecimal amount) {
                processPayCashPayResult(amount);
            }
        };
    }

    //打开微信支付UI
    private void openWechatPayUI() {
        new PayMove(this, payAdapter.getTotal().subtract(payAdapter.getIncome()), "微信支付") {
            @Override
            public void processPayCashResult(BigDecimal amount) {
                startThridService(1, "transName", "扫码支付", "type", "02", "amount", String.format("%012d", amount.multiply(new BigDecimal(100)).intValue()));
            }
        };
    }

    //打开支付宝支付UI
    private void openAlipayPayUI() {
        new PayMove(this, payAdapter.getTotal().subtract(payAdapter.getIncome()), "支付宝") {
            @Override
            public void processPayCashResult(BigDecimal amount) {
                startThridService(2, "transName", "扫码支付", "type", "02", "amount", String.format("%012d", amount.multiply(new BigDecimal(100)).intValue()));
            }
        };
    }

    //打开银行卡支付UI
    private void openUnionPayUI() {
        new PayMove(this, payAdapter.getTotal().subtract(payAdapter.getIncome()), "银行卡") {
            @Override
            public void processPayCashResult(BigDecimal amount) {
                startThridService(3, "transName", "消费", "type", "01", "amount", String.format("%012d", amount.multiply(new BigDecimal(100)).intValue()));
            }
        };
    }

    //打开银行卡支付UI
    private void openPayScanUI() {
        new PayMove(this, payAdapter.getTotal().subtract(payAdapter.getIncome()), "银行二维码") {
            @Override
            public void processPayCashResult(BigDecimal amount) {
                startThridService(8, "transName", "扫码支付", "type", "02", "amount", String.format("%012d", amount.multiply(new BigDecimal(100)).intValue()));
            }
        };
    }

    /*
    * 以下为处理支付的结果
    * */

    //处理现金支付的结果
    private void processPayCashPayResult(BigDecimal amount) {
        BigDecimal left = payAdapter.getTotal().subtract(payAdapter.getIncome());
        PayType payType = PayTypeUtisl.getPayType(PayTypeUtisl.KEY_01);
        this.payAdapter.add(new PayBeanCash(payType.PayType, payType.PayDescr, amount));
        if (amount.compareTo(left) > 0) {
            this.payAdapter.add(new PayBeanCash(payType.PayType, payType.PayDescr, left.subtract(amount)));
        }
        updateUI();
        if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) >= 0) {
            submit();
        }
    }

    //处理微信支付的结果
    private void processWechatPayResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                PayType payType = PayTypeUtisl.getPayType(PayTypeUtisl.KEY_13);
                PayBeanWechat payBeanWechat = new PayBeanWechat(payType.PayType, payType.PayDescr, new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)));
                payBeanWechat.setTransType(data.getStringExtra("transType"));
                payBeanWechat.setBatchNo(data.getStringExtra("batchNo"));
                payBeanWechat.setReferenceNo(data.getStringExtra("referenceNo"));
                payBeanWechat.setTraceNo(data.getStringExtra("traceNo"));
                payBeanWechat.setTerminalId(data.getStringExtra("terminalId"));
                payBeanWechat.setMerchantId(data.getStringExtra("merchantId"));
                payBeanWechat.setMerchantName(data.getStringExtra("merchantName"));
                payBeanWechat.setTransactionNumber(data.getStringExtra("transactionNumber"));
                payBeanWechat.setDateTime(data.getStringExtra("dateTime"));
                this.payAdapter.add(payBeanWechat);
                updateUI();
                if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) >= 0) {
                    submit();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "微信付款出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "微信付款出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "微信付款异常", e.getMessage());
        }
    }

    //处理支付宝支付的结果
    private void processAlipayPayResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                PayType payType = PayTypeUtisl.getPayType(PayTypeUtisl.KEY_14);
                PayBeanAlipay payBeanAlipay = new PayBeanAlipay(payType.PayType, payType.PayDescr, new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)));
                payBeanAlipay.setTransType(data.getStringExtra("transType"));
                payBeanAlipay.setBatchNo(data.getStringExtra("batchNo"));
                payBeanAlipay.setReferenceNo(data.getStringExtra("referenceNo"));
                payBeanAlipay.setTraceNo(data.getStringExtra("traceNo"));
                payBeanAlipay.setTerminalId(data.getStringExtra("terminalId"));
                payBeanAlipay.setMerchantId(data.getStringExtra("merchantId"));
                payBeanAlipay.setMerchantName(data.getStringExtra("merchantName"));
                payBeanAlipay.setTransactionNumber(data.getStringExtra("transactionNumber"));
                payBeanAlipay.setDateTime(data.getStringExtra("dateTime"));
                this.payAdapter.add(payBeanAlipay);
                updateUI();
                if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) >= 0) {
                    submit();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "支付宝付款出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "支付宝付款出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "支付宝付款异常", e.getMessage());
        }
    }

    //处理银联支付的结果
    private void processUnionPayResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                PayType payType = PayTypeUtisl.getPayType(PayTypeUtisl.KEY_03);
                PayBeanUnion payBeanUnion = new PayBeanUnion(payType.PayType, payType.PayDescr, new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)));
                payBeanUnion.setCardNo(data.getStringExtra("cardNo"));
                payBeanUnion.setTransType(data.getStringExtra("transType"));
                payBeanUnion.setBatchNo(data.getStringExtra("batchNo"));
                payBeanUnion.setReferenceNo(data.getStringExtra("referenceNo"));
                payBeanUnion.setTraceNo(data.getStringExtra("traceNo"));
                payBeanUnion.setTerminalId(data.getStringExtra("terminalId"));
                payBeanUnion.setMerchantId(data.getStringExtra("merchantId"));
                payBeanUnion.setMerchantName(data.getStringExtra("merchantName"));
                payBeanUnion.setTransactionNumber(data.getStringExtra("transactionNumber"));
                payBeanUnion.setDateTime(data.getStringExtra("dateTime"));
                this.payAdapter.add(payBeanUnion);
                updateUI();
                if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) >= 0) {
                    submit();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "银行卡付款出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "银行卡付款出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "银行卡付款异常", e.getMessage());
        }
    }

    //处理支付宝支付的结果
    private void processUnionScanResult(int resultCode, Intent data) {
        PayType payType = PayTypeUtisl.getPayType(PayTypeUtisl.KEY_18);
        try {
            if (resultCode == Activity.RESULT_OK) {
                PayBeanScan payBeanScan = new PayBeanScan(payType.PayType, payType.PayDescr, new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)));
                payBeanScan.setTransType(data.getStringExtra("transType"));
                payBeanScan.setBatchNo(data.getStringExtra("batchNo"));
                payBeanScan.setReferenceNo(data.getStringExtra("referenceNo"));
                payBeanScan.setTraceNo(data.getStringExtra("traceNo"));
                payBeanScan.setTerminalId(data.getStringExtra("terminalId"));
                payBeanScan.setMerchantId(data.getStringExtra("merchantId"));
                payBeanScan.setMerchantName(data.getStringExtra("merchantName"));
                payBeanScan.setTransactionNumber(data.getStringExtra("transactionNumber"));
                payBeanScan.setDateTime(data.getStringExtra("dateTime"));
                this.payAdapter.add(payBeanScan);
                updateUI();
                if (payAdapter.getIncome().compareTo(payAdapter.getTotal()) >= 0) {
                    submit();
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, payType.PayDescr + "付款出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, payType.PayDescr + "付款出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, payType.PayDescr + "付款异常", e.getMessage());
        }
    }

    /*
    * 以下为打开删除支付的界面
    * */

    //打开现金支付撤销的UI
    private void openCashCancelUI() {
        if (payAdapter.get().getAmount().compareTo(BigDecimal.ZERO) < 0) {
            new DialogOK(this, "警告", "回找金额不允许撤销！");
        } else {
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = "你确定要撤销该笔现金付款吗？";
            builder.defaultButtonY = "确定撤销";
            builder.defaultButtonN = "不撤销";
            new DialogYN(this, builder) {
                @Override
                public void clickY() {
                    delPayBean();
                    dismiss();
                }

                @Override
                public void clickN() {
                    ScrollerLayout item = (ScrollerLayout) listView.getChildAt(payAdapter.getCursor());
                    item.smoothCloseMenu();
                    dismiss();
                }
            };
        }
    }

    //打开微信支付撤销的UI
    private void openWechatCancelUI() {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "警告！";
        builder.defaultMessage = "你确定要将该笔微信付款退还给顾客吗？";
        builder.defaultButtonY = "退还顾客";
        builder.defaultButtonN = "不退还";
        new DialogYN(this, builder) {
            @Override
            public void clickY() {
                PayBeanWechat payBeanWechat = (PayBeanWechat) payAdapter.get();
                startThridService(4, "transName", "扫码撤销", "type", "02", "oldTransactionNumber", payBeanWechat.getTransactionNumber());
                dismiss();
            }

            @Override
            public void clickN() {
                ScrollerLayout item = (ScrollerLayout) listView.getChildAt(payAdapter.getCursor());
                item.smoothCloseMenu();
                dismiss();
            }
        };
    }

    //撤销支付宝付款
    private void openAlipayCancelUI() {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "警告！";
        builder.defaultMessage = "你确定要将该笔支付宝付款退还给顾客吗？";
        builder.defaultButtonY = "退还顾客";
        builder.defaultButtonN = "不退还";
        new DialogYN(this, builder) {
            @Override
            public void clickY() {
                PayBeanAlipay payBeanAlipay = (PayBeanAlipay) payAdapter.get();
                startThridService(5, "transName", "扫码撤销", "type", "02", "oldTransactionNumber", payBeanAlipay.getTransactionNumber());
                dismiss();
            }

            @Override
            public void clickN() {
                ScrollerLayout item = (ScrollerLayout) listView.getChildAt(payAdapter.getCursor());
                item.smoothCloseMenu();
                dismiss();
            }
        };
    }

    //撤销银联付款
    private void openUnionCancelUI() {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "警告！";
        builder.defaultMessage = "你确定要将该笔银行卡付款退还给顾客吗？";
        builder.defaultButtonY = "退还顾客";
        builder.defaultButtonN = "不退还";
        new DialogYN(this, builder) {
            @Override
            public void clickY() {
                PayBeanUnion payBeanUnion = (PayBeanUnion) payAdapter.get();
                startThridService(6, "transName", "消费撤销", "type", "01", "oldTrace", payBeanUnion.getTraceNo());
                dismiss();
            }

            @Override
            public void clickN() {
                ScrollerLayout item = (ScrollerLayout) listView.getChildAt(payAdapter.getCursor());
                item.smoothCloseMenu();
                dismiss();
            }
        };
    }

    //打开银行卡扫码撤销的UI
    private void openUnionScanCancelUI() {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "警告！";
        builder.defaultMessage = "你确定要将该笔付款退还给顾客吗？";
        builder.defaultButtonY = "退还顾客";
        builder.defaultButtonN = "不退还";
        new DialogYN(this, builder) {
            @Override
            public void clickY() {
                PayBeanScan payBeanScan = (PayBeanScan) payAdapter.get();
                startThridService(9, "transName", "扫码撤销", "type", "02", "oldTransactionNumber", payBeanScan.getTransactionNumber());
                dismiss();
            }

            @Override
            public void clickN() {
                ScrollerLayout item = (ScrollerLayout) listView.getChildAt(payAdapter.getCursor());
                item.smoothCloseMenu();
                dismiss();
            }
        };
    }

    /*
    * 处理支付撤销的结果
    * */
    //处理微信撤销的结果
    private void processUnionScanCancelResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                delPayBean();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "微信撤销出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "微信撤销出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "微信撤销付异常", e.getMessage());
        }
    }

    //处理支付宝撤销的结果
    private void processAlipayCancelResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                delPayBean();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "支付宝撤销出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "支付宝撤销出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "支付宝撤销付异常", e.getMessage());
        }
    }

    //处理支银联撤销的结果
    private void processUnionCancelResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                delPayBean();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "银行卡撤销出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "银行卡撤销出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "银行卡撤销付异常", e.getMessage());
        }
    }

    //处理微信撤销的结果
    private void processWechatCancelResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                delPayBean();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, "扫码撤销出错", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, "扫码撤销出错", "接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, "扫码撤销付异常", e.getMessage());
        }
    }


    //提交销售数据
    public void submit() {
        ConstantPayAdapter.payAdapter = this.payAdapter;
        this.startActivityForResult(new Intent(this, Submit.class), 7);
    }

    //更细系统UI
    private void updateUI() {
        //1.列表的显示与隐藏
        if (payAdapter.getCount() == 0) {
            listView.setVisibility(View.GONE);
            backContainer.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.VISIBLE);
            backContainer.setVisibility(View.GONE);
        }
        //2.开启动画
        total.setTextSize(SystemConfig.font_size * 2);

        //3.同步应付续付回找金额
        BigDecimal total = payAdapter.getTotal();
        BigDecimal income = payAdapter.getIncome();
        if (income.compareTo(BigDecimal.ZERO) == 0) {
            this.total.setText("应付:" + total.toString());
        } else {
            if (income.compareTo(total) < 0) {
                this.total.setText("续付:" + total.subtract(income).toString());
            } else {
                this.total.setText("回找:" + income.subtract(total).toString());
            }
        }
    }

    //启动第三方服务
    private void startThridService(int requestCode, String... params) {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.landicorp.thirdpayservice", "com.landicorp.thirdpayservice.MainActivity"));
            if (params != null) {
                for (int i = 0; i < params.length - 1; ) {
                    String key = params[i++];
                    String val = params[i++];
                    if (key != null && val != null) {
                        intent.putExtra(key, val);
                    }
                }
            }
            startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            new DialogOK(Settle.this, "提示", "本机尚未安装第三方接口服务！");
        }
    }

    //退出付款
    private void back() {
        if (payAdapter.getCount() == 0) {
            DialogYN.Builder builder = new DialogYN.Builder();
            builder.defaultTitle = "警告！";
            builder.defaultMessage = "本单尚未付款，你确定要返回吗？";
            builder.defaultButtonY = "继续付款";
            builder.defaultButtonN = "确定返回";
            builder.clickYesTitle = "提示";
            builder.clickYesMessage = "请选择一种付款方式继续付款！";
            builder.clickYesButton = "确定";
            builder.clickNoTitle = "警告";
            builder.clickNoMessage = "你取消了对本单的结算！";
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
                    if (!ok) {
                        finish();
                    }
                    dismiss();
                }
            };
        }
    }

    @Override
    protected void clickEnter() {

    }

    @Override
    protected void clickEscape() {
    }
}
