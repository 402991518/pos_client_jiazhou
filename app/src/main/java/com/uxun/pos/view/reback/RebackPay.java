package com.uxun.pos.view.reback;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.dto.PayType;
import com.uxun.pos.domain.dto.Proc;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.PayTypeUtisl;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.DialogYN;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.widget.Order;
import com.uxun.pos.view.widget.SuccessView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


public class RebackPay extends RootActivity {

    //列表数据
    public class Pay {
        public String type;//付款编码
        String name;//付款名称
        String amount;//付款金额
        boolean reback;//是否退款
        public Map<String, String> other;
        String rebackId;//退货标志

        public Pay(String type, String name, String amount) {
            this.type = type;
            this.name = name;
            this.amount = amount;
        }
    }

    //列表适配器
    public static class PayAdapter extends RecyclerView.Adapter<PayAdapter.Holder> {

        public interface OnItemClickListener {
            void onItemClick(Pay pay, Holder holder);
        }

        public class Holder extends RecyclerView.ViewHolder {
            Order order;
            TextView name;
            TextView amount;
            ImageView operate;
            SuccessView successView;
            LinearLayout container_1;
            LinearLayout container_2;

            public Holder(View itemView) {
                super(itemView);
                order = itemView.findViewById(R.id.order);
                name = itemView.findViewById(R.id.name);
                amount = itemView.findViewById(R.id.amount);
                operate = itemView.findViewById(R.id.operate);
                successView = itemView.findViewById(R.id.successView);
                container_1 = itemView.findViewById(R.id.container_1);
                container_2 = itemView.findViewById(R.id.container_2);
            }
        }

        private Context context;

        private List<Pay> list = new ArrayList<>();

        private OnItemClickListener onItemClickListener;

        public PayAdapter(Context context) {
            this.context = context;
        }

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.reback_pay_item, parent, false);
            ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, parent.getHeight() / 6);
            view.setLayoutParams(layoutParams);
            return new Holder(view);
        }

        @Override
        public void onBindViewHolder(final Holder holder, int position) {
            final Pay pay = list.get(position);
            holder.order.setText("" + (position + 1));
            holder.name.setText(pay.name);
            holder.amount.setText(pay.amount);
            holder.successView.setTag(pay.type);
            if (pay.reback) {
                holder.container_1.setVisibility(View.GONE);
                holder.container_2.setVisibility(View.VISIBLE);
            } else {
                holder.container_1.setVisibility(View.VISIBLE);
                holder.container_2.setVisibility(View.GONE);
            }

            holder.operate.setOnClickListener(new ClickListener() {
                @Override
                protected void click(View view) {
                    if (onItemClickListener != null) {
                        onItemClickListener.onItemClick(pay, holder);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public void setList(List<Pay> list) {
            this.list.clear();
            if (list != null) {
                this.list = list;
            }
            notifyDataSetChanged();
        }


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }
    }

    //退款成功显示动画
    private Handler handler_succ = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            SuccessView successView = (SuccessView) msg.obj;
            successView.startAnimate();
        }
    };

    private Proc proc;
    protected TextView cancel;
    protected RecyclerView recyclerView;
    private PayAdapter payAdapter;

    private Pay pay;
    private PayAdapter.Holder holder;

    private String returnMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
        submit();
    }

    private void initData() {
        returnMan = this.getIntent().getStringExtra("returnMan");
        String json = this.getIntent().getStringExtra("json");
        proc = GsonUtils.getInstance().fromJson(json, Proc.class);
        if (proc != null) {
            List<Pay> pays = new ArrayList<>();
            if (proc.ps != null) {
                for (int i = 0; i < proc.ps.size(); i++) {
                    Proc.P p = proc.ps.get(i);
                    Pay pay = new Pay(p.PayType, p.PayDescr, p.RealMount.stripTrailingZeros().toPlainString());
                    if (p.TermSn != null) {
                        pay.rebackId = p.TermSn;
                    }
                    pays.add(pay);
                }
            }
            payAdapter.setList(pays);
        }
    }

    private void initView() {
        payAdapter = new PayAdapter(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(payAdapter);
    }

    private void initEvent() {
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                TextView textView = (TextView) view;
                if ("退货完成".equals(textView.getText().toString())) {
                    RebackPay.this.setResult(1);
                    RebackPay.this.finish();
                } else {
                    exit();
                }
            }
        });
        payAdapter.setOnItemClickListener(new PayAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Pay pay, PayAdapter.Holder holder) {
                dispatchReback(pay, holder);
            }
        });
    }

    //退出本界面
    private void exit() {
        boolean exit = true;
        List<Pay> list = this.payAdapter.list;
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).reback) {
                    exit = false;
                    break;
                }
            }
        }
        if (!exit) {
            new DialogOK(this, "提示", "已发生退款，不允许取消");
        } else {
            RebackPay.this.finish();
        }
    }

    //分发事件
    private void dispatchReback(Pay pay, PayAdapter.Holder holder) {
        PayType payType = PayTypeUtisl.getPayType(pay.type);
        if (payType == null) {
            new DialogOK(this, "错误", "没有找到付款编码:" + pay.type + ",无法退款!");
            return;
        }

        if (PayTypeUtisl.KEY_01.equals(pay.type)) {
            rebackCash();
            return;
        }

        if (PayTypeUtisl.KEY_13.equals(pay.type)) {
            rebackWechat(pay, holder);
            return;
        }

        if (PayTypeUtisl.KEY_14.equals(pay.type)) {
            rebackAlipay(pay, holder);
            return;
        }

        if (PayTypeUtisl.KEY_18.equals(pay.type)) {
            rebackScan(pay, holder);
            return;
        }

        if (PayTypeUtisl.KEY_03.equals(pay.type)) {
            rebackUnion(pay, holder);
            return;
        }
    }

    //分发结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //微信退款结果
        if (requestCode == 1) {
            rebackWechatResult(resultCode, data);
        }
        //支付宝退款结果
        else if (requestCode == 2) {
            rebackAlipayResult(resultCode, data);
        }
        //扫码退款结果
        else if (requestCode == 3) {
            rebackScanResult(resultCode, data);
        }
        //银行卡退款结果
        else if (requestCode == 4) {
            rebackUnionResult(resultCode, data);
        }
        //退款完成
        else if (requestCode == 5) {
            this.cancel.setText("退货完成");
        }

    }

    //现金退款
    private void rebackCash() {
        List<Pay> list = this.payAdapter.list;
        BigDecimal sum = BigDecimal.ZERO;
        for (int i = 0; i < list.size(); i++) {
            Pay pay = list.get(i);
            if (PayTypeUtisl.KEY_01.equals(pay.type)) {
                sum = sum.add(new BigDecimal(pay.amount));
            }
        }
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "提示！";
        builder.defaultMessage = "请退还顾客现金:" + sum.toString();
        builder.defaultButtonY = "确定退款";
        builder.defaultButtonN = "取消退款";
        builder.clickYesTitle = "提示";
        builder.clickYesMessage = "你已将现金:" + sum.toString() + "元退还给顾客!";
        builder.clickYesButton = "确定";
        builder.clickNoTitle = "提示";
        builder.clickNoMessage = "你取消了退还现金给顾客！";
        builder.clickNoButton = "确定";
        //3.具体退款
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
                    rebackCashResult();
                }
                dismiss();
            }
        };
    }

    //处理现金退款结果
    public void rebackCashResult() {
        //1.当页已经显示出来的显示动画
        int count = RebackPay.this.recyclerView.getChildCount();
        for (int i = 0; i < count; i++) {
            View view = RebackPay.this.recyclerView.getChildAt(i);
            SuccessView successView = view.findViewById(R.id.successView);
            if (PayTypeUtisl.KEY_01.equals(successView.getTag())) {
                view.findViewById(R.id.container_1).setVisibility(View.GONE);
                view.findViewById(R.id.container_2).setVisibility(View.VISIBLE);
                Message message = handler_succ.obtainMessage();
                message.obj = successView;
                message.sendToTarget();
            }
        }
        //2.本页没有显示出来的关闭退款安按钮
        List<Pay> list = RebackPay.this.payAdapter.list;
        for (int i = 0; i < list.size(); i++) {
            Pay pay = list.get(i);
            if (PayTypeUtisl.KEY_01.equals(pay.type)) {
                pay.reback = true;
            }
        }
        RebackPay.this.payAdapter.notifyDataSetChanged();
        submit();
    }

    //微信退款
    public void rebackWechat(final Pay pay, final PayAdapter.Holder holder) {
        //1.构建提示对话框
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "提示！";
        builder.defaultMessage = "请退还顾客" + pay.name + ":" + pay.amount;
        builder.defaultButtonY = "确定退款";
        builder.defaultButtonN = "取消退款";
        builder.clickYesTitle = "提示";
        builder.clickYesMessage = "你即将把" + pay.name + ":" + pay.amount + "退还顾客！";
        builder.clickYesButton = "确定";
        builder.clickNoTitle = "提示";
        builder.clickNoMessage = "你取消了退款请求";
        builder.clickNoButton = "确定";
        //2.具体退款
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
                    RebackPay.this.pay = pay;
                    RebackPay.this.holder = holder;
                    startThridService(1, "transName", "扫码撤销", "type", "02", "oldTransactionNumber", pay.rebackId);
                }
                dismiss();
            }
        };
    }

    //微信退款结果
    private void rebackWechatResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {

                //如果原金额和退款金额不等，直接提示
                if ((new BigDecimal(this.pay.amount).multiply(new BigDecimal(100))).compareTo(new BigDecimal(data.getStringExtra("amount"))) != 0) {
                    DialogYN.Builder builder = new DialogYN.Builder();
                    builder.defaultTitle = "郑重警告！";
                    builder.defaultMessage = "原金额:" + new BigDecimal(this.pay.amount).stripTrailingZeros().toPlainString() +
                            ",退款金额：" + new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString() + ",退款金额与原付款金额不等！";
                    builder.defaultButtonY = "退款出错";
                    builder.defaultButtonN = "退款出错";
                    //2.具体退款
                    new DialogYN(this, builder);
                    return;
                }

                this.pay.reback = true;
                this.pay.other = new LinkedHashMap<>();
                this.pay.other.put("transType", data.getStringExtra("transType"));//交易金额
                this.pay.other.put("amount", data.getStringExtra("amount"));//交易金额
                this.pay.other.put("traceNo", data.getStringExtra("traceNo"));//流水号
                this.pay.other.put("referenceNo", data.getStringExtra("referenceNo"));//参考号
                this.pay.other.put("batchNo", data.getStringExtra("batchNo"));//批次号
                this.pay.other.put("terminalId", data.getStringExtra("terminalId"));//终端号
                this.pay.other.put("merchantId", data.getStringExtra("merchantId"));//商户号
                this.pay.other.put("merchantName", data.getStringExtra("merchantName"));//商户名称
                this.pay.other.put("transactionNumber", data.getStringExtra("transactionNumber"));//交易单号
                this.pay.other.put("dateTime", data.getStringExtra("dateTime"));//交易单号

                holder.container_1.setVisibility(View.GONE);
                holder.container_2.setVisibility(View.VISIBLE);
                Message message = handler_succ.obtainMessage();
                message.obj = holder.successView;
                message.sendToTarget();
                submit();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, this.pay.name + "退款出错,", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, this.pay.name + "退款出错,", "退款接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, this.pay.name + "退款出错,", e.getMessage());
        }
    }

    //支付宝退款
    public void rebackAlipay(final Pay pay, final PayAdapter.Holder holder) {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "提示！";
        builder.defaultMessage = "请退还顾客" + pay.name + ":" + pay.amount;
        builder.defaultButtonY = "确定退款";
        builder.defaultButtonN = "取消退款";
        builder.clickYesTitle = "提示";
        builder.clickYesMessage = "你即将把" + pay.name + ":" + pay.amount + "退还顾客！";
        builder.clickYesButton = "确定";
        builder.clickNoTitle = "提示";
        builder.clickNoMessage = "你取消了退款请求";
        builder.clickNoButton = "确定";
        //2.具体退款
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
                    RebackPay.this.pay = pay;
                    RebackPay.this.holder = holder;
                    startThridService(2, "transName", "扫码撤销", "type", "02", "oldTransactionNumber", pay.rebackId);
                }
                dismiss();
            }
        };
    }

    //支付宝退款结果
    private void rebackAlipayResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {

                //如果原金额和退款金额不等，直接提示
                if ((new BigDecimal(this.pay.amount).multiply(new BigDecimal(100))).compareTo(new BigDecimal(data.getStringExtra("amount"))) != 0) {
                    DialogYN.Builder builder = new DialogYN.Builder();
                    builder.defaultTitle = "郑重警告！";
                    builder.defaultMessage = "原金额:" + new BigDecimal(this.pay.amount).stripTrailingZeros().toPlainString() +
                            ",退款金额：" + new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString() + ",退款金额与原付款金额不等！";
                    builder.defaultButtonY = "退款出错";
                    builder.defaultButtonN = "退款出错";
                    //2.具体退款
                    new DialogYN(this, builder);
                    return;
                }

                this.pay.reback = true;
                this.pay.other = new LinkedHashMap<>();
                this.pay.other.put("transType", data.getStringExtra("transType"));//交易金额
                this.pay.other.put("amount", data.getStringExtra("amount"));//交易金额
                this.pay.other.put("traceNo", data.getStringExtra("traceNo"));//流水号
                this.pay.other.put("referenceNo", data.getStringExtra("referenceNo"));//参考号
                this.pay.other.put("batchNo", data.getStringExtra("batchNo"));//批次号
                this.pay.other.put("terminalId", data.getStringExtra("terminalId"));//终端号
                this.pay.other.put("merchantId", data.getStringExtra("merchantId"));//商户号
                this.pay.other.put("merchantName", data.getStringExtra("merchantName"));//商户名称
                this.pay.other.put("transactionNumber", data.getStringExtra("transactionNumber"));//交易单号
                this.pay.other.put("dateTime", data.getStringExtra("dateTime"));//交易单号

                holder.container_1.setVisibility(View.GONE);
                holder.container_2.setVisibility(View.VISIBLE);
                Message message = handler_succ.obtainMessage();
                message.obj = holder.successView;
                message.sendToTarget();
                submit();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, this.pay.name + "退款出错,", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, this.pay.name + "退款出错,", "退款接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, this.pay.name + "退款出错,", e.getMessage());
        }
    }

    //扫码退款
    public void rebackScan(final Pay pay, final PayAdapter.Holder holder) {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "提示！";
        builder.defaultMessage = "请退还顾客" + pay.name + ":" + pay.amount;
        builder.defaultButtonY = "确定退款";
        builder.defaultButtonN = "取消退款";
        builder.clickYesTitle = "提示";
        builder.clickYesMessage = "你即将把" + pay.name + ":" + pay.amount + "退还顾客！";
        builder.clickYesButton = "确定";
        builder.clickNoTitle = "提示";
        builder.clickNoMessage = "你取消了退款请求";
        builder.clickNoButton = "确定";
        //2.具体退款
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
                    RebackPay.this.pay = pay;
                    RebackPay.this.holder = holder;
                    Log.i("RebackPay", "oldTransactionNumber=" + pay.rebackId);
                    startThridService(3, "transName", "扫码撤销", "type", "02", "oldTransactionNumber", pay.rebackId);
                }
                dismiss();
            }
        };
    }

    //扫码退款结果
    private void rebackScanResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                //如果原金额和退款金额不等，直接提示
                if ((new BigDecimal(this.pay.amount).multiply(new BigDecimal(100))).compareTo(new BigDecimal(data.getStringExtra("amount"))) != 0) {
                    DialogYN.Builder builder = new DialogYN.Builder();
                    builder.defaultTitle = "郑重警告！";
                    builder.defaultMessage = "原金额:" + new BigDecimal(this.pay.amount).stripTrailingZeros().toPlainString() +
                            ",退款金额：" + new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString() + ",退款金额与原付款金额不等！";
                    builder.defaultButtonY = "退款出错";
                    builder.defaultButtonN = "退款出错";
                    //2.具体退款
                    new DialogYN(this, builder);
                    return;
                }
                this.pay.reback = true;
                this.pay.other = new LinkedHashMap<>();
                this.pay.other.put("transType", data.getStringExtra("transType"));//交易金额
                this.pay.other.put("amount", data.getStringExtra("amount"));//交易金额
                this.pay.other.put("traceNo", data.getStringExtra("traceNo"));//流水号
                this.pay.other.put("referenceNo", data.getStringExtra("referenceNo"));//参考号
                this.pay.other.put("batchNo", data.getStringExtra("batchNo"));//批次号
                this.pay.other.put("terminalId", data.getStringExtra("terminalId"));//终端号
                this.pay.other.put("merchantId", data.getStringExtra("merchantId"));//商户号
                this.pay.other.put("merchantName", data.getStringExtra("merchantName"));//商户名称
                this.pay.other.put("transactionNumber", data.getStringExtra("transactionNumber"));//交易单号
                this.pay.other.put("dateTime", data.getStringExtra("dateTime"));//交易单号
                holder.container_1.setVisibility(View.GONE);
                holder.container_2.setVisibility(View.VISIBLE);
                Message message = handler_succ.obtainMessage();
                message.obj = holder.successView;
                message.sendToTarget();
                submit();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, this.pay.name + "退款出错,", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, this.pay.name + "退款出错,", "退款接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, this.pay.name + "退款出错,", e.getMessage());
        }
    }

    //扫码退款
    public void rebackUnion(final Pay pay, final PayAdapter.Holder holder) {
        DialogYN.Builder builder = new DialogYN.Builder();
        builder.defaultTitle = "提示！";
        builder.defaultMessage = "请退还顾客" + pay.name + ":" + pay.amount;
        builder.defaultButtonY = "确定退款";
        builder.defaultButtonN = "取消退款";
        builder.clickYesTitle = "提示";
        builder.clickYesMessage = "你即将把" + pay.name + ":" + pay.amount + "退还顾客！";
        builder.clickYesButton = "确定";
        builder.clickNoTitle = "提示";
        builder.clickNoMessage = "你取消了退款请求";
        builder.clickNoButton = "确定";
        //2.具体退款
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
                    RebackPay.this.pay = pay;
                    RebackPay.this.holder = holder;
                    startThridService(4, "transName", "消费撤销", "type", "01", "oldTrace", pay.rebackId);
                }
                dismiss();
            }
        };
    }

    //扫码退款结果
    private void rebackUnionResult(int resultCode, Intent data) {
        try {
            if (resultCode == Activity.RESULT_OK) {
                //如果原金额和退款金额不等，直接提示
                if ((new BigDecimal(this.pay.amount).multiply(new BigDecimal(100))).compareTo(new BigDecimal(data.getStringExtra("amount"))) != 0) {
                    DialogYN.Builder builder = new DialogYN.Builder();
                    builder.defaultTitle = "郑重警告！";
                    builder.defaultMessage = "原金额:" + new BigDecimal(this.pay.amount).stripTrailingZeros().toPlainString() +
                            ",退款金额：" + new BigDecimal(data.getStringExtra("amount")).divide(new BigDecimal(100)).stripTrailingZeros().toPlainString() + ",退款金额与原付款金额不等！";
                    builder.defaultButtonY = "退款出错";
                    builder.defaultButtonN = "退款出错";
                    //2.具体退款
                    new DialogYN(this, builder);
                    return;
                }
                this.pay.reback = true;
                this.pay.other = new LinkedHashMap<>();
                this.pay.other.put("cardNo", data.getStringExtra("cardNo"));//交易金额
                this.pay.other.put("transType", data.getStringExtra("transType"));//交易金额
                this.pay.other.put("amount", data.getStringExtra("amount"));//交易金额
                this.pay.other.put("traceNo", data.getStringExtra("traceNo"));//流水号
                this.pay.other.put("referenceNo", data.getStringExtra("referenceNo"));//参考号
                this.pay.other.put("batchNo", data.getStringExtra("batchNo"));//批次号
                this.pay.other.put("terminalId", data.getStringExtra("terminalId"));//终端号
                this.pay.other.put("merchantId", data.getStringExtra("merchantId"));//商户号
                this.pay.other.put("merchantName", data.getStringExtra("merchantName"));//商户名称
                this.pay.other.put("transactionNumber", data.getStringExtra("transactionNumber"));//交易单号
                this.pay.other.put("dateTime", data.getStringExtra("dateTime"));//交易单号

                holder.container_1.setVisibility(View.GONE);
                holder.container_2.setVisibility(View.VISIBLE);
                Message message = handler_succ.obtainMessage();
                message.obj = holder.successView;
                message.sendToTarget();
                submit();
            } else if (resultCode == Activity.RESULT_CANCELED) {
                new DialogOK(this, this.pay.name + "退款出错,", data.getStringExtra("reason"));
            } else {
                new DialogOK(this, this.pay.name + "退款出错,", "退款接口没有按照约定返回结果！");
            }
        } catch (Exception e) {
            new DialogOK(this, this.pay.name + "退款出错,", e.getMessage());
        }
    }

    //检查是否可以提交数据
    private void submit() {
        boolean result = true;
        for (int i = 0; i < this.payAdapter.list.size(); i++) {
            Pay pay = this.payAdapter.list.get(i);
            if (!pay.reback) {
                result = false;
                break;
            }
        }
        if (result) {
            String json1 = GsonUtils.getInstance().toJson(this.proc);
            String json2 = GsonUtils.getInstance().toJson(this.payAdapter.list);
            Intent intent = new Intent(this, RebackSubmit.class);
            intent.putExtra("json1", json1);
            intent.putExtra("json2", json2);
            intent.putExtra("returnMan", returnMan);
            this.startActivityForResult(intent, 5);
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
            new DialogOK(this, "提示", "本机尚未安装第三方接口服务！");
        }
    }


    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
    }
}
