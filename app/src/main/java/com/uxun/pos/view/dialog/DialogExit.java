package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.SecurityUtils;
import com.uxun.pos.view.listener.ClickListener;

/**
 * Created by Administrator on 2018/4/20.
 */

public abstract class DialogExit extends Dialog {


    private TextView cancel;
    private TextView confirm;
    private TextView message;

    private EditText username;
    private EditText password;

    public DialogExit(Context context) {
        super(context, R.style.dialog);
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_auth);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
        this.initEvent();
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.932);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.618);
        this.getWindow().setAttributes(layoutParams);
    }

    //初始化图形
    private void initView() {
        message = this.findViewById(R.id.message);
        cancel = this.findViewById(R.id.cancel);
        confirm = this.findViewById(R.id.confirm);
        username = this.findViewById(R.id.username);
        password = this.findViewById(R.id.password);
        message.setText("请输入登录密码进行认证！");
        username.setText(ConstantLogin.loginData.user.UserId);
        username.setEnabled(false);
        this.password.requestFocus();
    }

    private void initEvent() {
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                dismiss();
            }
        });
        confirm.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if (!ConstantLogin.loginData.user.UserPass.equals(SecurityUtils.encode(password.getText().toString()))) {
                    password.setText("");
                    password.setHint("登录密码输入错误");
                } else {
                    result();
                }
            }
        });
    }

    public abstract void result();

}
