package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.listener.ClickListener;

//银联末笔查询结果显示
public class UnionQuery extends Dialog {

    private Intent data;

    public UnionQuery(Context context, Intent data) {
        super(context, R.style.dialog);
        this.data = data;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_union_query);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.876f);
        this.getWindow().setAttributes(layoutParams);
    }

    //初始化图形
    private void initView() {
        setText(this.findViewById(R.id.transType), "transType");
        setText(this.findViewById(R.id.amount), "amount");
        setText(this.findViewById(R.id.traceNo), "traceNo");
        setText(this.findViewById(R.id.referenceNo), "referenceNo");
        setText(this.findViewById(R.id.batchNo), "batchNo");
        setText(this.findViewById(R.id.terminalId), "terminalId");
        setText(this.findViewById(R.id.merchantId), "merchantId");
        setText(this.findViewById(R.id.merchantName), "merchantName");
        setText(this.findViewById(R.id.transactionNumber), "transactionNumber");
        setText(this.findViewById(R.id.dateTime), "dateTime");
        this.findViewById(R.id.ok).setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                dismiss();
            }
        });
    }

    private void setText(View textView, String key) {
        if (data != null) {
            String value = data.getStringExtra(key);
            if (value != null) {
                ((TextView) textView).setText(value);
            }
        }
    }
}
