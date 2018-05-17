package com.uxun.pos.view;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.utils.SecurityUtils;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.KeyBoardService;
import com.uxun.pos.view.widget.htextview.HTextView;
import com.uxun.pos.view.widget.htextview.HTextViewType;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

//锁屏
public class Lock extends RootActivity {

    // 轮播广告文字
    private static final String[] AD_TEXTS = new String[]{

            "龙.腾.科.技.",

            "深圳市优讯龙腾科技有限公司",

            "SHENZHEN LONGTEN TECHNOLOGY CO.LTD",

            "龙腾科技 专注领域 专注行业 成就企业互联",

            "龙腾 十余年的专注与爱",

            "龙腾 不断创新的软件服务",

            "龙腾 不做流水线 用心量身",

            "龙腾 真正贴身的服务于售后",

    };

    // 轮播广告文字的颜色
    private static final int[] AD_COLORS = new int[]{
            Color.rgb(254, 67, 101),
            Color.rgb(29, 191, 151),
            Color.rgb(35, 235, 185),
            Color.rgb(219, 69, 32)
    };


    private static class AnimateHandler extends Handler {

        private Lock lock;

        public AnimateHandler(Lock lock) {
            this.lock = lock;
        }

        @Override
        public void handleMessage(Message msg) {
            lock.changeAnimate();
        }
    }

    private AnimateHandler animateHandler;
    private Timer timer = new Timer(true);
    protected HTextView htextView;
    protected Chronometer chronometer;
    protected EditText input;
    protected TextView msg;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        animateHandler = new AnimateHandler(this);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                animateHandler.obtainMessage().sendToTarget();
            }
        }, 0l, 3000 * 1 * 1l);
        chronometer.start();
        new KeyBoardService(this.getWindow());
        msg.setText(ConstantLogin.loginData.user.UserName + ",你已经离开了：");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }

    // 改变动画
    private void changeAnimate() {
        Random random = new Random();
        htextView.setAnimateType(HTextViewType.randomHTextViewType());
        htextView.setTextColor(AD_COLORS[random.nextInt(AD_COLORS.length)]);
        htextView.animateText(AD_TEXTS[random.nextInt(AD_TEXTS.length)]);
    }

    @Override
    protected void clickEnter() {
        String userpass = input.getText().toString();
        if (SecurityUtils.encode(userpass).equals(ConstantLogin.loginData.user.UserPass)) {
            this.finish();
        } else {
            input.setText("");
            input.setHint("对不起，你输入的密码有误！");
        }
    }

    @Override
    protected void clickEscape() {

    }

}
