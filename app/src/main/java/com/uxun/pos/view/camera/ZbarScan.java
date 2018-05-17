package com.uxun.pos.view.camera;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.root.RootActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zbar.ZBarView;


public class ZbarScan extends RootActivity implements QRCodeView.Delegate {

    protected TextView msg;
    protected TextView light;
    protected TextView cancel;
    protected ZBarView zbarView;

    private boolean openLight = false;

    private int code = -1;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        zbarView.setDelegate(this);
        this.code = this.getIntent().getIntExtra("code", code);
        initEvent();
    }

    private void initEvent() {
        cancel.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                finish();
            }
        });
        light.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                if (openLight) {
                    zbarView.closeFlashlight();
                } else {
                    zbarView.openFlashlight();
                }
                openLight = !openLight;
                if (openLight) {
                    light.setText("关闭闪光灯");
                } else {
                    light.setText("打开闪光灯");
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        zbarView.startCamera();
        zbarView.showScanRect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        zbarView.startSpot();
    }

    @Override
    protected void onStop() {
        zbarView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zbarView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (result != null) {
            Intent intent = new Intent();
            intent.putExtra("message", result);
            this.setResult(code, intent);
            this.finish();
        } else {
            zbarView.startSpot();
        }
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        msg.setText("扫描出错...");
        zbarView.startSpot();
    }

    @Override
    protected void clickEnter() {
    }

    @Override
    protected void clickEscape() {
    }
}
