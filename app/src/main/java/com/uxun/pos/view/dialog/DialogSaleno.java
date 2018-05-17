package com.uxun.pos.view.dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.utils.KeyBoardService;

public abstract class DialogSaleno extends Dialog {

    private String tip;
    private TextView msg;
    private EditText saleno;

    public DialogSaleno(Context context, String tip) {
        super(context, R.style.dialog);
        this.tip = tip;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_saleno);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        saleno = this.findViewById(R.id.saleno);
        saleno.setShowSoftInputOnFocus(false);
        msg = this.findViewById(R.id.msg);
        msg.setText(tip);
        new KeyBoardService(this.getWindow());
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
            dismiss();
            processInput(saleno.getText().toString());
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    public abstract void processInput(String input);

}
