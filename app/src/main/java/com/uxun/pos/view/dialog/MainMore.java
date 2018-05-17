package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.Key;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.widget.CustomButtom;

import java.util.List;


public abstract class MainMore extends Dialog {

    public static final String key_1 = "单品折扣";//====
    public static final String key_2 = "整单折扣";//====
    public static final String key_3 = "单品取消";//====
    public static final String key_4 = "整单取消";//====
    public static final String key_5 = "重打小票";//====
    public static final String key_0 = "退  货";//===

    public static final String key_6 = "银联入口";
    public static final String key_9 = "修改密码";
    public static final String key_7 = "锁定系统";
    public static final String key_8 = "退出系统";


    private ClickListener clickListener = new ClickListener() {
        @Override
        protected void click(View view) {
            dismiss();
            onMoreButtonClick(view.getTag().toString());
        }
    };

    private int h;
    private int w;
    private int gap;
    private LinearLayout container;
    private LayoutInflater layoutInflater = (LayoutInflater) Application.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    public MainMore(Context context) {
        super(context, R.style.dialog);
        this.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_main_more);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
    }


    private void initView() {
        this.container = this.findViewById(R.id.container);
        ScrollView scrollView = (ScrollView) this.container.getParent();
        ViewGroup.MarginLayoutParams containerMarginLayoutParams = (ViewGroup.MarginLayoutParams) scrollView.getLayoutParams();
        gap = containerMarginLayoutParams.topMargin + containerMarginLayoutParams.bottomMargin;
        List<Key> keys = ConstantLogin.loginData.keys;
        for (int i = 0; i < keys.size(); i++) {
            Key key = keys.get(i);
            createButton(key.FuncNo);
        }
        createButton(key_6);
        createButton(key_9);
        createButton(key_7);
        createButton(key_8);
    }


    private void createButton(String text) {
        View view = layoutInflater.inflate(R.layout.item_main_more, null);
        CustomButtom customButtom = view.findViewById(R.id.customButtom);
        customButtom.setText(text);
        customButtom.setTag(text);
        customButtom.setOnClickListener(clickListener);
        CardView.LayoutParams layoutParams = new CardView.LayoutParams(w, (h - gap) / 8);
        view.setLayoutParams(layoutParams);
        container.addView(view);
    }

    //初始化窗口大小
    private void initSize() {
        Window window = getWindow();
        window.setGravity(Gravity.LEFT);
        window.setWindowAnimations(R.style.DialogOutAndInStyle);
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        w = layoutParams.width = (int) (SystemConfig.screen_width * 0.382f);
        h = layoutParams.height = (int) (SystemConfig.screen_height * 0.932f);
        layoutParams.alpha = 0.8f;
        layoutParams.x = 10;
        this.getWindow().setAttributes(layoutParams);
    }

    public abstract void onMoreButtonClick(String text);
}
