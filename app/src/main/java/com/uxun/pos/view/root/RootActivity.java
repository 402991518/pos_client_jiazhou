package com.uxun.pos.view.root;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.utils.ActivityUtils;

public abstract class RootActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initWindow();
        ActivityUtils.bindLayout(this);
        ActivityUtils.bindWidget(this, RootActivity.class);
        this.initMargin();
        this.initWidget(this.getWindow().getDecorView());
    }

    // 回退按钮绑定事件
    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            this.clickEscape();
            return true;
        }
        return false;
    }

    // 自定义回车(enter)和退出(escape)键盘事件
    @Override
    public final boolean dispatchKeyEvent(KeyEvent event) {
        if ((KeyEvent.KEYCODE_ENTER == event.getKeyCode() || KeyEvent.KEYCODE_NUMPAD_ENTER == event.getKeyCode()) && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.clickEnter();
            return true;
        } else if (KeyEvent.KEYCODE_ESCAPE == event.getKeyCode() && event.getAction() == KeyEvent.ACTION_DOWN) {
            this.clickEscape();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    // 点击界面外区域绑定事件
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        LayoutParams layoutParams = this.getWindow().getAttributes();
        if (x > 0 && x < layoutParams.width && y > 0 && y < layoutParams.height) {
            return super.onTouchEvent(event);
        } else {
            return super.onTouchEvent(event);
        }
    }

    // 初始化窗口属性
    private void initWindow() {
        Window window = this.getWindow();
        // 1.基本窗口设置
        window.setWindowAnimations(android.R.style.Animation_Activity);// 动画效果
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);// 设置横屏
        window.setSoftInputMode(LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);// 默认不弹出输入法
        // 2.设置窗口亮度,设置背景窗体变暗
        window.setFlags(LayoutParams.FLAG_KEEP_SCREEN_ON, LayoutParams.FLAG_KEEP_SCREEN_ON);// 始终点亮																														// 点亮屏幕
        window.setFlags(LayoutParams.FLAG_DIM_BEHIND, LayoutParams.FLAG_DIM_BEHIND);// 设置背景窗体变暗
        LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = 1.00f;// 透明度
        layoutParams.dimAmount = 0.618f; // 背景亮度
        layoutParams.screenBrightness = 1.00f;// 前景亮度
        window.setAttributes(layoutParams);
    }

    // 设置根布局的属性
    private void initMargin() {
        ViewGroup decorView = (ViewGroup) this.getWindow().getDecorView();
        ViewGroup linearLayout = (ViewGroup) decorView.getChildAt(0);
        ViewGroup frameLayout = (ViewGroup) linearLayout.getChildAt(1);
        // 1. 客户端根Layout(XML布局文件中根布局即为customRootLayout)
        ViewGroup customRootLayout = (ViewGroup) frameLayout.getChildAt(0);
        // 2. 设置客户端根Layout的Margin
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) customRootLayout.getLayoutParams();
        int margin = 0;
        marginLayoutParams.setMargins(margin, margin, margin, margin);
    }

    //初始化组件
    private void initWidget(View view) {
        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                initWidget(viewGroup.getChildAt(i));
            }
        } else {
            if (view instanceof TextView) {
                final TextView textView = (TextView) view;
                textView.setTextSize(SystemConfig.font_size);
            }
            if (view instanceof EditText) {
                EditText editText = (EditText) view;
                // editText的Tag保存的是初始hint的内容
                editText.setTag(editText.getHint());
                editText.setShowSoftInputOnFocus(true);
            }
        }
    }

    // 启动Activity并返回结果
    @Override
    public final void startActivityForResult(Intent intent, int requestCode) {
        intent.putExtra("code", requestCode);
        super.startActivityForResult(intent, requestCode);
    }

    // 用户点击Enter键
    protected abstract void clickEnter();

    // 用户点击Escape键
    protected abstract void clickEscape();

}