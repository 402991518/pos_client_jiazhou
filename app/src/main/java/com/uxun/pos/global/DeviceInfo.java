package com.uxun.pos.global;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;

import com.uxun.pos.global.Application;

/**
 * Created by Administrator on 2017/11/28.
 * 设备物理信息
 */

public class DeviceInfo {

    public static final String HOST_ID;// 本机唯一序列号
    public static final String HOST_IP;// 本机IP地址
    public static final String HOST_NAME;// 本机名称

    // 获取本机参数
    static {
        Application application = Application.getContext();
        WifiManager wifiManager = (WifiManager) application.getSystemService(Context.WIFI_SERVICE);
        int ip = wifiManager.getConnectionInfo().getIpAddress();
        HOST_IP = (ip & 0xFF) + "." + ((ip >> 8) & 0xFF) + "." + ((ip >> 16) & 0xFF) + "." + (ip >> 24 & 0xFF);
        HOST_NAME = Build.MODEL;
        HOST_ID = Settings.Secure.getString(application.getContentResolver(), Settings.Secure.ANDROID_ID);
    }

}
