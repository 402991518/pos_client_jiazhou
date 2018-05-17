package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.domain.bo.Ware;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.utils.KeyBoardService;

import java.math.BigDecimal;
import java.math.RoundingMode;

public abstract class Rebate extends Dialog {

    // 适配TextWatcherAdapter
    private class TextWatcherAdapter implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    }


    private TextView msg;
    private TextView rebate;
    private TextView salePrice;
    private TextView realPrice;

    private EditText inputReal;
    private EditText inputRebate;

    private Ware ware;

    public Rebate(Context context, Ware ware) {
        super(context, R.style.dialog);
        this.ware = ware;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_rebate);
        this.setCanceledOnTouchOutside(true);
        this.initSize();
        this.bindView();
        new KeyBoardService(this.getWindow());
        this.initView();
        this.initEvent();
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

    //初始化窗口大小
    private void initSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.986f);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.812f);
        this.getWindow().setAttributes(layoutParams);
    }

    //初始化图形
    private void bindView() {
        msg = this.findViewById(R.id.msg);
        rebate = this.findViewById(R.id.rebate);
        salePrice = this.findViewById(R.id.salePrice);
        realPrice = this.findViewById(R.id.realPrice);
        inputReal = this.findViewById(R.id.inputReal);
        inputRebate = this.findViewById(R.id.inputRebate);
        inputReal.setShowSoftInputOnFocus(false);
        inputRebate.setShowSoftInputOnFocus(false);
        initInputHint();
    }

    private void initInputHint() {
        inputRebate.setText("");
        inputReal.setText("");
        inputRebate.setHint("请输入商品折扣...");
        inputReal.setHint("请输入商品实价...");
    }


    //初始化图形
    private void initView() {
        msg.setText(ware.getGoods().ITEMNO + "/" + ware.getGoods().ITEMNAME);
        salePrice.setText(ware.getSalePrice().toString());
        realPrice.setText(ware.getRealPrice().toString());
        rebate.setText(ware.getRebate().toString());
    }

    // 初始事件
    private void initEvent() {
        this.inputReal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    initInputHint();
                }
            }
        });
        this.inputRebate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    initInputHint();
                }
            }
        });
        this.inputReal.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (inputReal.hasFocus()) {
                    inputRebate.setText("");
                    String price = s.toString();
                    // 用户输入实价同步更新折扣
                    if ("".equals(price.trim())) {
                        inputRebate.setHint("请输入商品折扣...");
                    } else {
                        try {
                            BigDecimal realPrice = new BigDecimal(price);
                            realPrice = realPrice.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
                            String rebate = realPrice.divide(ware.getSalePrice(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString();
                            inputRebate.setHint(rebate);
                        } catch (Exception e) {
                            inputRebate.setHint("请输入商品折扣...");
                        }
                    }
                }
            }
        });
        this.inputRebate.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (inputRebate.hasFocus()) {
                    inputReal.setText("");
                    String rebateText = s.toString();
                    // 用户输入折扣同步更新实价
                    if ("".equals(rebateText.trim())) {
                        inputReal.setHint("请输入商品实价...");
                    } else {
                        try {
                            BigDecimal decimalRebate = new BigDecimal(rebateText);
                            decimalRebate = decimalRebate.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
                            String realPrice = ware.getSalePrice().multiply(decimalRebate).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
                            inputReal.setHint(realPrice);
                        } catch (Exception e) {
                            inputReal.setHint("请输入商品实价...");
                        }
                    }
                }
            }
        });
    }


    // 设置折扣
    private void setRebate() {
        String rebateText;
        BigDecimal rebate;
        try {
            rebateText = inputRebate.getText().toString();
            rebate = new BigDecimal(rebateText);
        } catch (Exception e) {
            inputRebate.setText("");
            inputRebate.setHint("商品折扣输入错误");
            return;
        }
        rebate = rebate.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (rebate.compareTo(new BigDecimal(0)) < 0) {
            inputRebate.setText("");
            inputRebate.setHint("商品折扣不能小于0");
            return;
        }
        if (rebate.compareTo(new BigDecimal(100)) > 0) {
            inputRebate.setText("");
            inputRebate.setHint("商品折扣不能超过100");
            return;
        }
        ware.setRebate(rebate);
        OnRebateResult();
        dismiss();
    }

    // 设置实价
    private void setPrice() {
        String realPriceText;
        BigDecimal realPrice;
        try {
            realPriceText = inputReal.getText().toString();
            realPrice = new BigDecimal(realPriceText);
        } catch (Exception e) {
            inputReal.setText("");
            inputReal.setHint("商品实价输入错误");
            return;
        }
        realPrice = realPrice.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (realPrice.compareTo(new BigDecimal(0)) < 0) {
            inputReal.setText("");
            inputReal.setHint("商品实价不能小于0");
            return;
        }
        BigDecimal salePrice = ware.getGoods().SALEPRICE;
        if (realPrice.compareTo(salePrice) > 0) {
            inputReal.setText("");
            inputReal.setHint("商品实价不能超过售价！");
            return;
        }

        ware.setRealPrice(realPrice);
        OnRebateResult();
        dismiss();
    }

    private void clickOK() {
        if (this.inputReal.hasFocus()) {
            this.setPrice();
        } else if (this.inputRebate.hasFocus()) {
            this.setRebate();
        }
    }

    public abstract void OnRebateResult();

}
