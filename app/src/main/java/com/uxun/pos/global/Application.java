package com.uxun.pos.global;

public class Application extends android.app.Application {

    private static Application context;

    // 应用创建的时候调用
    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }

    public static Application getContext() {
        return context;
    }

    public static String getVersionName() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            return "";
        }
    }

    public static String getVersionCode() {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode + "";
        } catch (Exception e) {
            return "";
        }
    }
}
