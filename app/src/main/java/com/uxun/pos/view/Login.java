package com.uxun.pos.view;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.domain.dto.LoginData;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.DeviceInfo;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.dialog.DialogOK;
import com.uxun.pos.view.dialog.Setting;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;
import com.uxun.pos.view.utils.NetParserUtils;

public class Login extends RootActivity {

    protected EditText username;
    protected EditText password;
    protected TextView appInfo;
    protected TextView message;
    protected LinearLayout container;
    protected ImageView image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((TextView) this.findViewById(R.id.hotline)).setTextSize(11);
        ((TextView) this.findViewById(R.id.copyright)).setTextSize(11);
        appInfo.setTextSize(11);
        appInfo.setText("版本号:" + Application.getVersionCode() + " 版本名称:" + Application.getVersionName());
        image.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                new DialogOK(Login.this, "本机唯一ID", DeviceInfo.HOST_ID);
            }
        });
    }

    //登录
    public void login(View view) {
        if (this.container.getVisibility() == View.INVISIBLE) {
            //1.检查用户名和密码
            final String username = this.username.getText().toString();
            if ("".equals(username)) {
                this.username.requestFocus();
                return;
            }
            final String password = this.password.getText().toString();
            if ("".equals(password)) {
                this.password.requestFocus();
                return;
            }
            this.container.setVisibility(View.VISIBLE);
            this.message.setText("正在登录，请稍后...");
            VolleyUtils.post(NetworkConfig.getUrl() + "user/login", new Handler() {
                @Override
                public void handleMessage(Message msg) {
                    container.setVisibility(View.INVISIBLE);
                    NetParserUtils.parseReponse(Login.this, msg, new NetParserUtils.ResponseParser() {
                        @Override
                        public void parse(String json) {
                            ConstantLogin.loginData = GsonUtils.getInstance().fromJson(json, LoginData.class);
                            Log.i("Login", "ConstantLogin.loginData = " + ConstantLogin.loginData.zhaokeyi);
                            Login.this.password.setText("");
                            startActivity(new Intent(Login.this, Main.class));
                        }
                    });
                }
            }, "posid", DeviceInfo.HOST_ID, "username", username, "password", password);
        }
    }


    //设置
    public void set(View view) {
        if (this.container.getVisibility() == View.INVISIBLE) {
            new Setting(this);
        }
    }

    //退出
    public void logout(View view) {
        if (this.container.getVisibility() == View.INVISIBLE) {
            finish();
        }
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
    }
}
