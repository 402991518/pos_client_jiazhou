package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.utils.VolleyUtils;


//网络请求
public abstract class DialogNet extends Dialog {

    private String message;
    private String url;
    private String[] params;

    /**
     * @param context
     * @param message 弹出网络请求时的信息提示框
     * @param url     网络请求的URL
     * @param params  网络请求的参数
     */
    public DialogNet(Context context, String message, String url, String... params) {
        super(context, R.style.dialog);
        this.message = message;
        this.url = url;
        this.params = params;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_net);
        this.setCanceledOnTouchOutside(false);
        this.initDialogSize();
        ((TextView) this.findViewById(R.id.message)).setText(message);
        VolleyUtils.post(url, new Handler() {
            @Override
            public void handleMessage(Message msg) {
                dismiss();
                onNetRequestResult(msg);
            }
        }, params);
    }

    // 回退按钮绑定事件
    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    //设定弹出窗口的大小
    private void initDialogSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.932);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.198);
        this.getWindow().setAttributes(layoutParams);
    }

    public abstract void onNetRequestResult(Message message);

}
