package com.uxun.pos.view.utils;

import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.EditText;

import com.uxun.pos.R;
import com.uxun.pos.view.widget.CustomButtom;

public class KeyBoardService {

    private static final KeyEvent KEY_EVENT_0 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_0);
    private static final KeyEvent KEY_EVENT_1 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_1);
    private static final KeyEvent KEY_EVENT_2 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_2);
    private static final KeyEvent KEY_EVENT_3 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_3);
    private static final KeyEvent KEY_EVENT_4 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_4);
    private static final KeyEvent KEY_EVENT_5 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_5);
    private static final KeyEvent KEY_EVENT_6 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_6);
    private static final KeyEvent KEY_EVENT_7 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_7);
    private static final KeyEvent KEY_EVENT_8 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_8);
    private static final KeyEvent KEY_EVENT_9 = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_9);
    private static final KeyEvent KEY_EVENT_P = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_PERIOD);
    private static final KeyEvent KEY_EVENT_B = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_DEL);
    private static final KeyEvent KEY_EVENT_E = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER);

    private CustomButtom key_0;
    private CustomButtom key_1;
    private CustomButtom key_2;
    private CustomButtom key_3;
    private CustomButtom key_4;
    private CustomButtom key_5;
    private CustomButtom key_6;
    private CustomButtom key_7;
    private CustomButtom key_8;
    private CustomButtom key_9;
    private CustomButtom key_p;
    private CustomButtom key_b;
    private CustomButtom key_c;
    private CustomButtom key_o;

    private Window window;
    private View.OnClickListener onClickListener;

    public KeyBoardService(Window window) {
        this.window = window;
        this.findView();
        this.initListener();
        this.initEvent();
    }

    private void findView() {
        this.key_0 = window.findViewById(R.id.key_0);
        this.key_1 = window.findViewById(R.id.key_1);
        this.key_2 = window.findViewById(R.id.key_2);
        this.key_3 = window.findViewById(R.id.key_3);
        this.key_4 = window.findViewById(R.id.key_4);
        this.key_5 = window.findViewById(R.id.key_5);
        this.key_6 = window.findViewById(R.id.key_6);
        this.key_7 = window.findViewById(R.id.key_7);
        this.key_8 = window.findViewById(R.id.key_8);
        this.key_9 = window.findViewById(R.id.key_9);
        this.key_p = window.findViewById(R.id.key_p);
        this.key_b = window.findViewById(R.id.key_b);
        this.key_c = window.findViewById(R.id.key_c);
        this.key_o = window.findViewById(R.id.key_o);
    }

    private void initListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Window.Callback callback = window.getCallback();
                if (view == key_0) {
                    callback.dispatchKeyEvent(KEY_EVENT_0);
                } else if (view == key_1) {
                    callback.dispatchKeyEvent(KEY_EVENT_1);
                } else if (view == key_2) {
                    callback.dispatchKeyEvent(KEY_EVENT_2);
                } else if (view == key_3) {
                    callback.dispatchKeyEvent(KEY_EVENT_3);
                } else if (view == key_4) {
                    callback.dispatchKeyEvent(KEY_EVENT_4);
                } else if (view == key_5) {
                    callback.dispatchKeyEvent(KEY_EVENT_5);
                } else if (view == key_6) {
                    callback.dispatchKeyEvent(KEY_EVENT_6);
                } else if (view == key_7) {
                    callback.dispatchKeyEvent(KEY_EVENT_7);
                } else if (view == key_8) {
                    callback.dispatchKeyEvent(KEY_EVENT_8);
                } else if (view == key_9) {
                    callback.dispatchKeyEvent(KEY_EVENT_9);
                } else if (view == key_p) {
                    callback.dispatchKeyEvent(KEY_EVENT_P);
                } else if (view == key_b) {
                    callback.dispatchKeyEvent(KEY_EVENT_B);
                } else if (view == key_o) {
                    callback.dispatchKeyEvent(KEY_EVENT_E);
                }
            }
        };
    }

    private void initEvent() {
        this.key_0.setOnClickListener(onClickListener);
        this.key_1.setOnClickListener(onClickListener);
        this.key_2.setOnClickListener(onClickListener);
        this.key_3.setOnClickListener(onClickListener);
        this.key_4.setOnClickListener(onClickListener);
        this.key_5.setOnClickListener(onClickListener);
        this.key_6.setOnClickListener(onClickListener);
        this.key_7.setOnClickListener(onClickListener);
        this.key_8.setOnClickListener(onClickListener);
        this.key_9.setOnClickListener(onClickListener);
        this.key_p.setOnClickListener(onClickListener);
        this.key_b.setOnClickListener(onClickListener);
        this.key_o.setOnClickListener(onClickListener);
        this.key_c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = window.getCurrentFocus();
                if (view != null) {
                    if (view instanceof EditText) {
                        EditText editText = (EditText) view;
                        editText.setText("");
                    }
                }
            }
        });
    }
}
