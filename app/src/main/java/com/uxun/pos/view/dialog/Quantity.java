package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;

import com.uxun.pos.R;
import com.uxun.pos.domain.bo.Ware;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.listener.ClickListener;
import com.uxun.pos.view.utils.KeyBoardService;
import com.uxun.pos.view.widget.CustomButtom;

import java.math.BigDecimal;

//录入数量
public abstract class Quantity extends Dialog {

    private Ware ware;

    private CustomButtom add;
    private CustomButtom del;
    private EditText input;


    public Quantity(Context context, Ware ware) {
        super(context, R.style.dialog);
        this.ware = ware;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_quantity);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
        this.initEvent();
        new KeyBoardService(this.getWindow());
    }

    @Override
    protected void onStop() {
        super.onStop();
        onDialogDissmiss();
    }

    private void initView() {
        add = this.findViewById(R.id.add);
        del = this.findViewById(R.id.del);
        input = this.findViewById(R.id.input);
        input.setShowSoftInputOnFocus(true);
        String text = ware.getQuantity().toString();
        input.setText(text);
        input.setSelection(text.length());
    }

    private void initEvent() {
        this.add.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                String text = input.getText().toString();
                if (text == null || "".equals(text.trim())) {
                    input.setText("1");
                    input.setSelection(1);
                    return;
                }
                BigDecimal decimal;
                try {
                    decimal = new BigDecimal(text);
                } catch (Exception e) {
                    input.setText("1");
                    input.setSelection(1);
                    return;
                }

                int value = decimal.intValue() + 1;
                if (value < 1) {
                    input.setText("1");
                    input.setSelection(1);
                    return;
                }

                String result = value + "";
                input.setText(result);
                input.setSelection(result.length());
            }
        });

        this.del.setOnClickListener(new ClickListener() {
            @Override
            protected void click(View view) {
                String text = input.getText().toString();
                if (text == null || "".equals(text.trim())) {
                    input.setText("1");
                    input.setSelection(1);
                    return;
                }
                BigDecimal decimal;
                try {
                    decimal = new BigDecimal(text);
                } catch (Exception e) {
                    input.setText("1");
                    input.setSelection(1);
                    return;
                }

                int value = decimal.intValue() - 1;
                if (value < 1) {
                    input.setText("1");
                    input.setSelection(1);
                    return;
                }

                String result = value + "";
                input.setText(result);
                input.setSelection(result.length());
            }
        });
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.618f);
        this.getWindow().setAttributes(layoutParams);
    }


    @Override
    public final boolean dispatchKeyEvent(KeyEvent event) {
        if ((KeyEvent.KEYCODE_ENTER == event.getKeyCode() || KeyEvent.KEYCODE_NUMPAD_ENTER == event.getKeyCode()) && event.getAction() == KeyEvent.ACTION_DOWN) {
            clickEnter();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }


    private void clickEnter() {
        String text = input.getText().toString();
        if (text == null || "".equals(text.trim())) {
            input.setText("1");
            input.setSelection(1);
            return;
        }
        BigDecimal decimal;
        try {
            decimal = new BigDecimal(text);
        } catch (Exception e) {
            input.setText("1");
            input.setSelection(1);
            return;
        }

        int value = decimal.intValue();
        if (value < 1) {
            input.setText("1");
            input.setSelection(1);
            return;
        }

        onDialogResult(new BigDecimal(value));
        dismiss();
    }

    public abstract void onDialogResult(BigDecimal quatity);

    public abstract void onDialogDissmiss();
}
