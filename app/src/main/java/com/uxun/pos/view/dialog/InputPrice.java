package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.dto.Goods;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.utils.KeyBoardService;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Administrator on 2018/3/12.
 */

public abstract class InputPrice extends Dialog {

    private class TextWatcherAdapter implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            realPrice.setText(s.toString());
        }
    }

    private Goods goods;

    private TextView message;
    private EditText salePrice;
    private EditText realPrice;


    public InputPrice(Context context, Goods goods) {
        super(context, R.style.dialog);
        this.goods = goods;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_inputprice);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.initView();
        new KeyBoardService(this.getWindow());
    }

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.618f);
        this.getWindow().setAttributes(layoutParams);
    }

    private void initView() {
        message = this.findViewById(R.id.message);
        message.setText(goods.BARCODE + "/" + goods.ITEMNAME);
        salePrice = this.findViewById(R.id.salePrice);
        realPrice = this.findViewById(R.id.realPrice);
        salePrice.setShowSoftInputOnFocus(false);
        realPrice.setShowSoftInputOnFocus(false);
        salePrice.setTextSize(SystemConfig.font_size);
        realPrice.setTextSize(SystemConfig.font_size);
        message.setTextSize(SystemConfig.font_size);
        salePrice.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                realPrice.setText(s.toString());
            }
        });
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
        //1
        String textSale = this.salePrice.getText().toString();
        if (textSale == null || "".equals(textSale.trim())) {
            this.salePrice.setText("");
            this.salePrice.setHint("请输入商品售价...");
            this.salePrice.requestFocus();
            return;
        }
        //2
        BigDecimal decimalSale;
        try {
            decimalSale = new BigDecimal(textSale);
        } catch (Exception e) {
            this.salePrice.setText("");
            this.salePrice.setHint("输入商品售价无效...");
            this.salePrice.requestFocus();
            return;
        }
        decimalSale = decimalSale.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (decimalSale.compareTo(new BigDecimal(0)) < 0) {
            this.salePrice.setText("");
            this.salePrice.setHint("输入商品售价无效...");
            this.salePrice.requestFocus();
            return;
        }
        //2
        String textReal = this.realPrice.getText().toString();
        if (textReal == null || "".equals(textReal.trim())) {
            this.realPrice.setText("");
            this.realPrice.setHint("请输入商品实价...");
            this.realPrice.requestFocus();
            return;
        }
        //4
        BigDecimal decimalReal;
        try {
            decimalReal = new BigDecimal(textReal);
        } catch (Exception e) {
            this.realPrice.setText("");
            this.realPrice.setHint("输入商品实价无效...");
            this.realPrice.requestFocus();
            return;
        }
        decimalReal = decimalReal.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (decimalReal.compareTo(new BigDecimal(0)) < 0) {
            this.realPrice.setText("");
            this.realPrice.setHint("输入商品实价无效...");
            this.realPrice.requestFocus();
            return;
        }
        //5.
        this.salePrice.setText(decimalSale.toString());
        this.realPrice.setText(decimalReal.toString());
        if (decimalReal.compareTo(decimalSale) > 0) {
            this.realPrice.setText("");
            this.realPrice.setHint("商品实价不能超过售价...");
            this.realPrice.requestFocus();
            return;
        }

        processGoods(decimalSale, decimalReal);
    }

    public abstract void processGoods(BigDecimal salePrice, BigDecimal realPrice);
}