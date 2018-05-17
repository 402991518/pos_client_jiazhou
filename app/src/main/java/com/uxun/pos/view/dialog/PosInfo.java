package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.constant.ConstantLogin;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.listener.ClickListener;

//POS机详细信息
public class PosInfo extends Dialog {

    public PosInfo(Context context) {
        super(context, R.style.dialog);
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_pos_info);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.618f);
        this.getWindow().setAttributes(layoutParams);
    }

    //初始化图形
    private void initView() {
        ((TextView) this.findViewById(R.id.posid)).setText(ConstantLogin.loginData.device.PosNo + "");
        ((TextView) this.findViewById(R.id.posname)).setText(ConstantLogin.loginData.device.Descr + "");
        ((TextView) this.findViewById(R.id.grdcode)).setText(ConstantLogin.loginData.device.GrdCode + "");
        ((TextView) this.findViewById(R.id.userid)).setText(ConstantLogin.loginData.user.UserId + "");
        ((TextView) this.findViewById(R.id.username)).setText(ConstantLogin.loginData.user.UserName + "");
        this.findViewById(R.id.ok).setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                dismiss();
            }
        });
    }
}
