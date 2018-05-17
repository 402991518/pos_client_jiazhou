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
import com.uxun.pos.view.widget.htextview.HTextView;
import com.uxun.pos.view.widget.htextview.HTextViewType;

import java.math.BigDecimal;
import java.math.RoundingMode;

//移动支付
public abstract class PayMove extends Dialog {


    private String payname;
    private BigDecimal left;

    private TextView title;
    private HTextView msg;
    private EditText input;

    public PayMove(Context context, BigDecimal left, String payname) {
        super(context, R.style.dialog);
        this.left = left;
        this.payname = payname;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_pay_move);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
        new KeyBoardService(this.getWindow());
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.786f);
        this.getWindow().setAttributes(layoutParams);
    }

    private void initView() {
        title = this.findViewById(R.id.title);
        msg = this.findViewById(R.id.msg);
        input = this.findViewById(R.id.input);
        title.setText(payname);
        input.setText(left.toString());
        input.setSelection(left.toString().length());
        msg.setAnimateType(HTextViewType.ANVIL);
        msg.animateText("");
        msg.animateText("尚需付款：" + left.toString());
    }

    @Override
    public final boolean dispatchKeyEvent(KeyEvent event) {
        if ((KeyEvent.KEYCODE_ENTER == event.getKeyCode() || KeyEvent.KEYCODE_NUMPAD_ENTER == event.getKeyCode()) && event.getAction() == KeyEvent.ACTION_DOWN) {
            clickOK();
            return true;
        } else {
            return super.dispatchKeyEvent(event);
        }
    }

    private void clickOK() {
        //1.输入校验
        String inputText = this.input.getText().toString();
        if (inputText == null || "".equals(inputText.trim())) {
            this.input.setText("");
            this.input.setHint("输入金额无效");
            this.input.requestFocus();
            return;
        }
        //2.转换数值校验
        BigDecimal decimalIput;
        try {
            decimalIput = new BigDecimal(inputText);
        } catch (Exception e) {
            this.input.setText("");
            this.input.setHint("输入金额无效");
            this.input.requestFocus();
            return;
        }
        //3.舍入校验
        decimalIput = decimalIput.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (decimalIput.compareTo(new BigDecimal(0)) <= 0) {
            this.input.setText("");
            this.input.setHint("输入金额无效");
            this.input.requestFocus();
            return;
        }
        //4.与剩余金额校验
        if (decimalIput.compareTo(left) > 0) {
            this.input.setText("");
            this.input.setHint("付款金额不能超过剩余付款额");
            this.input.requestFocus();
            return;
        }

        processPayCashResult(decimalIput);
        dismiss();
    }

    public abstract void processPayCashResult(BigDecimal amount);
}
