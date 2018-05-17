package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.DeviceInfo;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.widget.AttentionView;
import com.uxun.pos.view.widget.CustomButtom;
import com.uxun.pos.view.widget.FailureView;
import com.uxun.pos.view.widget.SuccessView;

//系统配置
public class Setting extends Dialog {

    private SuccessView success;
    private FailureView failure;
    private AttentionView attention;
    private LinearLayout loading;

    private CustomButtom test;
    private CustomButtom save;

    private TextView msg;
    private EditText input;

    public Setting(Context context) {
        super(context, R.style.dialog);
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_setting);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
        this.initEvent();
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.932);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.382);
        this.getWindow().setAttributes(layoutParams);
    }

    //初始化图形
    private void initView() {
        success = this.findViewById(R.id.success);
        failure = this.findViewById(R.id.failure);
        attention = this.findViewById(R.id.attention);
        loading = this.findViewById(R.id.loading);
        msg = this.findViewById(R.id.msg);
        input = this.findViewById(R.id.input);
        test = this.findViewById(R.id.test);
        save = this.findViewById(R.id.save);
        String ip = NetworkConfig.getIP();
        if (!"".equals(ip)) {
            input.setText(ip);
            input.setSelection(ip.length());
        }
    }

    //初始化事件
    private void initEvent() {
        test.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                String ip = input.getText().toString();
                if (!"".equals(ip.trim())) {
                    //1.显示进度
                    msg.setText("正在测试IP地址,请稍后...");
                    loading.setVisibility(View.VISIBLE);
                    attention.setVisibility(View.GONE);
                    failure.setVisibility(View.GONE);
                    success.setVisibility(View.GONE);
                    //2.请求网络
                    VolleyUtils.post(NetworkConfig.getUrl(ip) + "/posInfo/regist", new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            if (msg.what == 0) {
                                testSucc();
                            } else {
                                testFail();
                            }
                        }
                    }, "id", DeviceInfo.HOST_ID);
                }
            }
        });
        save.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                String ip = input.getText().toString();
                if (!"".equals(ip.trim())) {
                    NetworkConfig.setIP(ip);
                    msg.setText("已经保存");
                    attention.setVisibility(View.GONE);
                    failure.setVisibility(View.GONE);
                    loading.setVisibility(View.GONE);
                    success.setVisibility(View.VISIBLE);
                    new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                            success.startAnimate();
                        }
                    }.obtainMessage().sendToTarget();
                }
            }
        });
    }

    //测试成功
    private void testSucc() {
        msg.setText("恭喜你，该IP可用!");
        success.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        attention.setVisibility(View.GONE);
        failure.setVisibility(View.GONE);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                success.startAnimate();
            }
        }.obtainMessage().sendToTarget();
    }

    //测试失败
    private void testFail() {
        msg.setText("对不起，该IP不可用！");
        failure.setVisibility(View.VISIBLE);
        loading.setVisibility(View.GONE);
        attention.setVisibility(View.GONE);
        success.setVisibility(View.GONE);
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                failure.startAnimate();
            }
        }.obtainMessage().sendToTarget();
    }

}
