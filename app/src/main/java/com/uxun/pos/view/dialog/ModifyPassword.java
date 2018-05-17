package com.uxun.pos.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.SecurityUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.listener.ClickListener;

import java.util.Map;

public class ModifyPassword extends Dialog {

    private EditText oldPassword;
    private EditText newPassword1;
    private EditText newPassword2;

    private TextView msg;
    private TextView modify;


    public ModifyPassword(Context context) {
        super(context, R.style.dialog);
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_modify_password);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
        this.initEvent();
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.618f);
        this.getWindow().setAttributes(layoutParams);
    }

    private void initView() {
        oldPassword = this.findViewById(R.id.oldPassword);
        newPassword1 = this.findViewById(R.id.newPassword1);
        newPassword2 = this.findViewById(R.id.newPassword2);
        msg = this.findViewById(R.id.msg);
        modify = this.findViewById(R.id.modify);
    }

    private void initEvent() {
        this.modify.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                modify();
            }
        });
    }

    private void modify() {
        final String oldPassword = this.oldPassword.getText().toString();

        if ("".equals(oldPassword)) {
            this.oldPassword.requestFocus();
            this.oldPassword.setHint("请输入原始登录密码...");
            return;
        }

        if (!SecurityUtils.encode(oldPassword).equals(ConstantLogin.loginData.user.UserPass)) {
            this.oldPassword.requestFocus();
            this.oldPassword.setText("");
            this.oldPassword.setHint("原始密码错误...");
            return;
        }

        final String newPassword1 = this.newPassword1.getText().toString();
        if (newPassword1.equals("")) {
            this.newPassword1.requestFocus();
            this.newPassword1.setHint("请输入新的登录密码...");
            return;
        }

        String newPassword2 = this.newPassword2.getText().toString();
        if (newPassword2.equals("")) {
            this.newPassword2.setHint("请确认新的登录密码...");
            this.newPassword2.requestFocus();
            return;
        }

        if (!newPassword1.equals(newPassword2)) {
            this.newPassword2.setText("");
            this.newPassword2.setHint("确认新密码错误...");
            this.newPassword2.requestFocus();
            return;
        }

        this.msg.setText("正在修改用户登录密码，请稍后...");
        this.modify.setClickable(false);

        VolleyUtils.post(NetworkConfig.getUrl() + "user/chpwd", new Handler() {
            @Override
            public void handleMessage(Message msg) {
                modify.setClickable(true);
                ModifyPassword.this.oldPassword.setText("");
                ModifyPassword.this.newPassword1.setText("");
                ModifyPassword.this.newPassword2.setText("");
                try {
                    if (msg.what == 0) {
                        Map<String, Object> content = GsonUtils.getInstance().fromJson(msg.obj.toString(), Map.class);
                        String code = (String) content.get("code");
                        ModifyPassword.this.msg.setText(content.get("message") + "");
                        if ("0".equals(code)) {
                            ConstantLogin.loginData.user.UserPass = SecurityUtils.encode(newPassword1);
                        }
                    } else {
                        ModifyPassword.this.msg.setText("修改密码出错！" + msg.obj.toString());
                    }

                } catch (Exception e) {
                    ModifyPassword.this.msg.setText("修改密码出错！" + e.getMessage());
                }
            }
        }, "posid", ConstantLogin.loginData.device.IpAddress, "username", ConstantLogin.loginData.user.UserId, "password", oldPassword, "newpassword", newPassword1);
    }


}
