package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.adapter.WareAdapter;
import com.uxun.pos.view.listener.ClickListener;

import java.math.BigDecimal;


public class CartInfo extends Dialog {

    private WareAdapter wareAdapter;

    public CartInfo(Context context, WareAdapter wareAdapter) {
        super(context, R.style.dialog);
        this.wareAdapter = wareAdapter;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_cart_info);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.618f);
        this.getWindow().setAttributes(layoutParams);
    }

    //初始化图形
    private void initView() {
        ((TextView) this.findViewById(R.id.saleno)).setText(ConstantLogin.loginData.saleno + "");
        if (wareAdapter != null) {
            BigDecimal totalSale = wareAdapter.getTotalSale();
            BigDecimal totalReal = wareAdapter.getTotalReal();
            ((TextView) this.findViewById(R.id.totalSale)).setText(totalSale.toString());
            ((TextView) this.findViewById(R.id.totalReal)).setText(totalReal.toString());
            ((TextView) this.findViewById(R.id.totalRebate)).setText(totalSale.subtract(totalReal).toString());
        }
        this.findViewById(R.id.ok).setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                dismiss();
            }
        });
    }
}
