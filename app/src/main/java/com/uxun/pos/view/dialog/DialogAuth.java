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
import com.uxun.pos.domain.dto.User;
import com.uxun.pos.global.DeviceInfo;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.GsonUtils;
import com.uxun.pos.utils.VolleyUtils;
import com.uxun.pos.view.listener.ClickListener;

import java.util.Map;

//认证
public abstract class DialogAuth extends Dialog {

    private String tip;

    private TextView cancel;
    private TextView confirm;
    private TextView message;

    private EditText username;
    private EditText password;

    public DialogAuth(Context context, String tip) {
        super(context, R.style.dialog);
        this.tip = tip;
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
        message.setText(tip);
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
                String username = DialogAuth.this.username.getText().toString();
                String password = DialogAuth.this.password.getText().toString();
                if ("".equals(username)) {
                    DialogAuth.this.username.requestFocus();
                    return;
                }
                if ("".equals(password)) {
                    DialogAuth.this.password.requestFocus();
                    return;
                }
                query(username, password);
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
                            parse(GsonUtils.getInstance().toJson(content.get("data")));
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

    //解析
    private void parse(String json) {
        User user = GsonUtils.getInstance().fromJson(json, User.class);
        processAuth(user);
        dismiss();
    }

    public abstract void processAuth(User user);
}
