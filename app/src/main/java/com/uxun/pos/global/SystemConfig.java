package com.uxun.pos.global;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

//系统设置
public class SystemConfig {

    private static final String key_font_size = "fontSize";
    private static final String key_screen_orientation = "screenOrientation";

    private static final int font_size_min = 12;//系统支持最小字体
    private static final int font_size_max = 32;//系统支持最大字体
    private static final int font_size_default = 12;//系统默认字体大小

    private static final int screen_orientation_portrait = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;//竖屏
    private static final int screen_orientation_landscape = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;//横屏
    private static final int screen_orientation_default = screen_orientation_portrait;//默认系统屏幕为横屏

    public static final int font_size;//字体大小
    public static final int screen_orientation;//屏幕方向
    public static final float density;//屏幕密度
    public static final int screen_width;//屏幕宽度
    public static final int screen_height;//屏幕高度

    static {
        screen_orientation = getScreenOrientation();
        font_size = getFontSize();
        screen_width = getScreenWidth();
        screen_height = getScreenHeight();
        density = getDensity();
    }

    //设置屏幕方向
    public static void setScreenOrientation(int screenOrientation) {
        SharedPreferences sharedPreferences = Application.getContext().getSharedPreferences(SystemConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        if (screenOrientation == screen_orientation_portrait || screenOrientation == screen_orientation_landscape) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key_screen_orientation, screenOrientation);
            editor.commit();
        }
    }

    //设置字体大小
    public static void setFontSize(int fontSize) {
        SharedPreferences sharedPreferences = Application.getContext().getSharedPreferences(SystemConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        if (fontSize >= font_size_min && fontSize <= font_size_max) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(key_font_size, fontSize);
            editor.commit();
        }
    }

    //获取屏幕方向
    private static int getScreenOrientation() {
        SharedPreferences sharedPreferences = Application.getContext().getSharedPreferences(SystemConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        int screenOrientation = sharedPreferences.getInt(key_screen_orientation, screen_orientation_default);
        if (screenOrientation != screen_orientation_portrait && screenOrientation != screen_orientation_landscape) {
            screenOrientation = screen_orientation_default;
        }
        return screenOrientation;
    }

    //获取字体大小
    private static int getFontSize() {
        SharedPreferences sharedPreferences = Application.getContext().getSharedPreferences(SystemConfig.class.getSimpleName(), Context.MODE_PRIVATE);
        int fontSize = sharedPreferences.getInt(key_font_size, font_size_default);
        if (fontSize < font_size_min || fontSize > font_size_max) {
            fontSize = font_size_default;
        }
        return fontSize;
    }

    //获取屏幕宽度
    private static int getScreenWidth() {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        return displayMetrics.widthPixels;
    }

    //获取屏幕高度
    private static int getScreenHeight() {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        return displayMetrics.heightPixels;
    }

    //获取屏幕密度
    private static float getDensity() {
        DisplayMetrics displayMetrics = getDisplayMetrics();
        return displayMetrics.density;
    }

    //获取显示信息
    private static DisplayMetrics getDisplayMetrics() {
        WindowManager windowManager = (WindowManager) Application.getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);
        return displayMetrics;
    }
}
