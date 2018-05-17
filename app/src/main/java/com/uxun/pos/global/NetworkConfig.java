package com.uxun.pos.global;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Administrator on 2017/11/28.
 * 网络配置信息
 */
public class NetworkConfig {

    public static final String RPC_PORT = "3721"; // POS服务器端口号
    public static final String RPC_NAME = "pos_server"; // POS服务端应用名称


    private static SharedPreferences sharedPreferences = Application.getContext().getSharedPreferences(NetworkConfig.class.getSimpleName(), Context.MODE_PRIVATE);

    //设置字体大小
    public static void setIP(String ip) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("ip", ip);
        editor.commit();
    }

    //获取字体大小
    public static String getIP() {
        return sharedPreferences.getString("ip", "");
    }

    public static String getUrl() {
         return "http://" + getIP() + ":" + RPC_PORT + "/" + RPC_NAME + "/";
    }

    public static String getUrl(String ip) {
        return "http://" + ip + ":" + RPC_PORT + "/" + RPC_NAME + "/";
    }

}
