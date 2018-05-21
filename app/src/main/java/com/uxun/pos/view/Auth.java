
package com.uxun.pos.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.global.DeviceInfo;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.camera.ZbarScan;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;

import java.util.Map;

public class Auth extends RootActivity {


    private TextView scan;
    private TextView cancel;
    private TextView confirm;
    private TextView message;

    private EditText username;
    private EditText password;

    private int code;
    private String tip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initDate();
        initEvent();
    }

    //初始化数据
    private void initDate() {
        this.code = getIntent().getIntExtra("code", -1);
        this.tip = this.getIntent().getStringExtra("tip");
        this.message.setText(tip);
    }

    private void initEvent() {
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                clickEscape();
            }
        });
        confirm.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                String username = Auth.this.username.getText().toString();
                String password = Auth.this.password.getText().toString();
                if ("".equals(username)) {
                    Auth.this.username.requestFocus();
                    return;
                }
                if ("".equals(password)) {
                    Auth.this.password.requestFocus();
                    return;
                }
                query(username, password);
            }
        });
        scan.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                startActivityForResult(new Intent(Auth.this, ZbarScan.class), 1);
            }
        });
    }


    //查询
    private void query(String username, String password) {
        confirm.setClickable(false);
        message.setText("正在查询用户权限,请稍后...");
        this.username.setText("");
        this.password.setText("");
        VolleyUtils.post(NetworkConfig.getUrl() + "user/valid", new Handler() {
            @Override
            public void handleMessage(Message msg) {
                confirm.setClickable(true);
                try {
                    if (msg.what == 0) {
                        Map<String, Object> content = GsonUtils.getInstance().fromJson(msg.obj.toString(), Map.class);
                        String code = (String) content.get("code");
                        if ("1".equals(code)) {
                            message.setText("查询用户权限出错," + content.get("message") + "");
                        } else if ("2".equals(code)) {
                            message.setText("查询用户权限出错," + content.get("message"));
                        } else if ("0".equals(code)) {
                            processResult(GsonUtils.getInstance().toJson(content.get("data")));
                        }
                    } else {
                        message.setText("查询用户权限出错," + msg.obj.toString());
                    }
                } catch (Exception e) {
                    message.setText("查询用户权限出错," + e.getMessage());
                }
            }
        }, "posid", DeviceInfo.HOST_ID, "username", username, "password", password);
    }

    //处理结果
    private void processResult(String json) {
        Intent intent = new Intent();
        intent.putExtra("json", json);
        this.setResult(code, intent);
        this.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            String ups = data.getStringExtra("message");
            if (ups != null) {
                query("", ups);
            }
        }
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
        finish();
    }
}
