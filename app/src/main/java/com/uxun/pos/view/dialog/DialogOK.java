package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.listener.ClickListener;

//确定提示框
public class DialogOK extends Dialog {

    private String title;
    private String message;

    public DialogOK(Context context, String title, String message) {
        super(context, R.style.dialog);
        this.title = (title == null ? "提示" : title.trim());
        this.message = (message == null ? "没有提示信息" : message.trim());
        this.show();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_ok);
        this.setCanceledOnTouchOutside(false);
        this.initSize();
        this.initView();
    }

    //取消回退按钮
    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
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
        ((TextView) this.findViewById(R.id.title)).setText(title);
        ((TextView) this.findViewById(R.id.title)).setTextSize(SystemConfig.font_size * 1.2f);
        ((TextView) this.findViewById(R.id.message)).setText(message);
        this.findViewById(R.id.ok).setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                dismiss();
            }
        });
    }
}
