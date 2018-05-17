package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.NetworkConfig;
import com.uxun.pos.global.SystemConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

//程序更新界面
public abstract class Updater extends Dialog {

    private TextView message;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    message.setText(msg.obj.toString());
                    break;
                case 1:
                    dismiss();
                    onDownloadComplete();
                    break;
                case 2:
                    dismiss();
                    onDownloadError();
                    break;
            }

        }
    };

    public Updater(Context context) {
        super(context, R.style.dialog);
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_updater);
        this.setCanceledOnTouchOutside(false);
        this.initDialogSize();
        this.message = this.findViewById(R.id.message);
        this.message.setText("正在下载更新包，请稍后...");
        startDownLoad();
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

    //开始下载数据
    private void startDownLoad() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(NetworkConfig.getUrl() + "version/download").openConnection();
                    if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                        // 1.初始化
                        long length = httpURLConnection.getContentLength();
                        InputStream inputStream = httpURLConnection.getInputStream();
                        FileOutputStream fileOutputStream = new FileOutputStream(new File(Environment.getExternalStorageDirectory(), "pos_client.apk"));
                        //2.写数据
                        byte[] buffer = new byte[1024];
                        int len;
                        int process = 1;
                        while ((len = inputStream.read(buffer)) != -1) {
                            fileOutputStream.write(buffer, 0, len);
                            process += len;
                            Message message = handler.obtainMessage();
                            message.what = 0;
                            message.obj = "正在下载更新包，请稍后:" + String.format("%.2f", (process * 1.0f / length) * 100) + "%";
                            message.sendToTarget();
                        }
                        Message message = handler.obtainMessage();
                        message.what = 1;
                        message.sendToTarget();
                    }
                } catch (Exception e) {
                    Message message = handler.obtainMessage();
                    message.what = 2;
                    message.sendToTarget();
                }
            }
        }).start();
    }

    //下载完成
    public abstract void onDownloadComplete();

    //下载出错
    public abstract void onDownloadError();
}
