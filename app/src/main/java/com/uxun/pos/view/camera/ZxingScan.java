package com.uxun.pos.view.camera;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class ZxingScan extends RootActivity implements QRCodeView.Delegate {

    protected TextView msg;
    protected TextView cancel;
    protected ZXingView zxingview;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zxingview.setDelegate(this);
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        zxingview.startCamera();
        zxingview.showScanRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zxingview.startSpot();
    }

    @Override
    protected void onStop() {
        zxingview.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zxingview.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        msg.setText("扫描结果：" + result);
        zxingview.startSpot();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        msg.setText("扫描出错...");
        zxingview.startSpot();
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
    }
}
