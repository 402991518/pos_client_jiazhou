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
import com.uxun.pos.view.adapter.WareAdapter;
import com.uxun.pos.view.utils.KeyBoardService;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by Administrator on 2018/3/20.
 */

public abstract class RebateAll extends Dialog {

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


    private TextView rebate;
    private TextView totalSale;
    private TextView totalReal;

    private EditText inputTotalReal;
    private EditText inputTotalRebate;

    private WareAdapter wareAdapter;

    public RebateAll(Context context, WareAdapter wareAdapter) {
        super(context, R.style.dialog);
        this.wareAdapter = wareAdapter;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_rebate_all);
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
        rebate = this.findViewById(R.id.rebate);
        totalSale = this.findViewById(R.id.totalSale);
        totalReal = this.findViewById(R.id.totalReal);
        inputTotalReal = this.findViewById(R.id.inputTotalReal);
        inputTotalRebate = this.findViewById(R.id.inputTotalRebate);
        inputTotalReal.setShowSoftInputOnFocus(false);
        inputTotalRebate.setShowSoftInputOnFocus(false);
        initInputHint();
    }

    private void initInputHint() {
        inputTotalRebate.setText("");
        inputTotalReal.setText("");
        inputTotalRebate.setHint("请输入整单折扣");
        inputTotalReal.setHint("请输入整单实价");
    }


    //初始化图形
    private void initView() {
        totalSale.setText(wareAdapter.getTotalSale().toString());
        totalReal.setText(wareAdapter.getTotalReal().toString());
        rebate.setText(wareAdapter.getTotalSale().subtract(wareAdapter.getTotalReal()).toString());
    }

    // 初始事件
    private void initEvent() {
        this.inputTotalReal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    initInputHint();
                }
            }
        });
        this.inputTotalRebate.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    initInputHint();
                }
            }
        });
        this.inputTotalReal.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (inputTotalReal.hasFocus()) {
                    inputTotalRebate.setText("");
                    String totalRealText = s.toString();
                    // 用户输入实价同步更新折扣
                    if ("".equals(totalRealText.trim())) {
                        inputTotalRebate.setHint("请输入整单折扣");
                    } else {
                        try {
                            BigDecimal totalRealDecimal = new BigDecimal(totalRealText);
                            totalRealDecimal = totalRealDecimal.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
                            String totalRebateText = totalRealDecimal.divide(wareAdapter.getTotalSale(), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100)).toString();
                            inputTotalRebate.setHint(totalRebateText);
                        } catch (Exception e) {
                            inputTotalRebate.setHint("请输入整单折扣");
                        }
                    }
                }
            }
        });
        this.inputTotalRebate.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                if (inputTotalRebate.hasFocus()) {
                    inputTotalReal.setText("");
                    String totalRebateText = s.toString();
                    // 用户输入折扣同步更新实价
                    if ("".equals(totalRebateText.trim())) {
                        inputTotalReal.setHint("请输入整单折扣");
                    } else {
                        try {
                            BigDecimal totalRebateDecimal = new BigDecimal(totalRebateText);
                            totalRebateDecimal = totalRebateDecimal.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
                            String totalRealText = wareAdapter.getTotalSale().multiply(totalRebateDecimal).divide(new BigDecimal(100), 2, RoundingMode.HALF_UP).toString();
                            inputTotalReal.setHint(totalRealText);
                        } catch (Exception e) {
                            inputTotalReal.setHint("请输入整单折扣");
                        }
                    }
                }
            }
        });
    }

    // 设置折扣
    private void setTotalRebate() {
        //1.折率校验
        String rebateText;
        BigDecimal totalRebate;
        try {
            rebateText = inputTotalRebate.getText().toString();
            totalRebate = new BigDecimal(rebateText);
        } catch (Exception e) {
            inputTotalRebate.setText("");
            inputTotalRebate.setHint("整单折扣输入错误");
            return;
        }
        totalRebate = totalRebate.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (totalRebate.compareTo(new BigDecimal(0)) < 0) {
            inputTotalRebate.setText("");
            inputTotalRebate.setHint("整单折扣不能小于0");
            return;
        }
        if (totalRebate.compareTo(new BigDecimal(100)) > 0) {
            inputTotalRebate.setText("");
            inputTotalRebate.setHint("整单折扣不能超过100");
            return;
        }
        //2.修改折扣
        int count = wareAdapter.getCount();
        for (int i = 0; i < count; i++) {
            Ware ware = (Ware) wareAdapter.getItem(i);
            ware.setRebate(totalRebate);
        }
        OnRebateResult();
        dismiss();
    }

    // 设置实价
    private void setTotalReal() {
        //1.整单实价校验
        String realPriceText;
        BigDecimal totalReal;
        try {
            realPriceText = inputTotalReal.getText().toString();
            totalReal = new BigDecimal(realPriceText);
        } catch (Exception e) {
            inputTotalReal.setText("");
            inputTotalReal.setHint("整单实价输入错误");
            return;
        }
        totalReal = totalReal.divide(new BigDecimal(1), 2, RoundingMode.HALF_UP);
        if (totalReal.compareTo(new BigDecimal(0)) < 0) {
            inputTotalReal.setText("");
            inputTotalReal.setHint("整单实价不能小于0");
            return;
        }
        BigDecimal totalSale = wareAdapter.getTotalSale();
        if (totalReal.compareTo(totalSale) > 0) {
            inputTotalReal.setText("");
            inputTotalReal.setHint("整单实价不能超过售价！");
            return;
        }
        //2.整单实价修改
        BigDecimal temp_rebate = totalReal.divide(totalSale, 2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        int count = wareAdapter.getCount();
        for (int i = 0; i < count; i++) {
            Ware ware = (Ware) wareAdapter.getItem(i);
            ware.setRebate(temp_rebate);
        }
        OnRebateResult();
        dismiss();
    }

    private void clickOK() {
        if (this.inputTotalReal.hasFocus()) {
            this.setTotalReal();
        } else if (this.inputTotalRebate.hasFocus()) {
            this.setTotalRebate();
        }
    }

    public abstract void OnRebateResult();

}
