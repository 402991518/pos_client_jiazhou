package com.uxun.pos.view.listener;

import android.view.View;

import java.util.Calendar;

//防抖操作
public abstract class ClickListener implements View.OnClickListener {


    public static final int MIN_CLICK_DELAY_TIME = 300;

    private long lastClickTime = 0;

    @Override
    public void onClick(View view) {

        long currentTime = Calendar.getInstance().getTimeInMillis();
        if (currentTime - lastClickTime > MIN_CLICK_DELAY_TIME) {
            lastClickTime = currentTime;
            click(view);
        }
    }

    protected abstract void click(View view);

}
