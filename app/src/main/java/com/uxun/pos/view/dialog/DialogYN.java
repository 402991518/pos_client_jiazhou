package com.uxun.pos.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.uxun.pos.R;
import com.uxun.pos.global.SystemConfig;
import com.uxun.pos.view.widget.AttentionView;
import com.uxun.pos.view.widget.CustomButtom;
import com.uxun.pos.view.widget.FailureView;
import com.uxun.pos.view.widget.SuccessView;


public class DialogYN extends Dialog {

    public static class Builder {
        public String defaultTitle = "警告！";
        public String defaultMessage = "你确定要删除选中商品吗？";
        public String defaultButtonY = "确定删除";
        public String defaultButtonN = "不删除";

        public String clickYesTitle = "提示";
        public String clickYesMessage = "你已经删除了商品！";
        public String clickYesButton = "确定";

        public String clickNoTitle = "提示";
        public String clickNoMessage = "你取消了删除操作！";
        public String clickNoButton = "确定";
    }

    private Handler handlerSuccess = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            success.startAnimate();
        }
    };
    private Handler handlerFailure = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            failure.startAnimate();
        }
    };

    private SuccessView success;
    private FailureView failure;
    private AttentionView attention;

    private TextView title;
    private TextView message;


    private LinearLayout container1;
    private LinearLayout container2;

    private CustomButtom y;
    private CustomButtom n;
    private CustomButtom o;

    private Builder builder;

    public DialogYN(Context context, Builder builder) {
        super(context, R.style.dialog);
        this.builder = builder;
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.dialog_yn);
        this.setCanceledOnTouchOutside(false);
        this.initDialogSize();
        this.bindWidget();
        this.initView();
        this.registListener();
    }

    // 回退按钮绑定事件
    @Override
    public final boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return false;
    }

    //设定弹出窗口的大小
    private void initDialogSize() {
        WindowManager.LayoutParams layoutParams = this.getWindow().getAttributes();
        layoutParams.width = (int) (SystemConfig.screen_width * 0.932);
        layoutParams.height = (int) (SystemConfig.screen_height * 0.382);
        this.getWindow().setAttributes(layoutParams);
    }

    //绑定组件
    private void bindWidget() {
        success = this.findViewById(R.id.success);
        failure = this.findViewById(R.id.failure);
        attention = this.findViewById(R.id.attention);
        title = this.findViewById(R.id.title);
        message = this.findViewById(R.id.message);
        container1 = this.findViewById(R.id.container1);
        container2 = this.findViewById(R.id.container2);
        y = this.findViewById(R.id.y);
        n = this.findViewById(R.id.n);
        o = this.findViewById(R.id.o);
    }


    //初始化显示
    private void initView() {
        this.title.setTextSize(SystemConfig.font_size * 1.2f);
        this.title.setText(this.builder.defaultTitle);
        this.message.setText(this.builder.defaultMessage);
        this.y.setText(this.builder.defaultButtonY);
        this.n.setText(this.builder.defaultButtonN);
    }

    //初始化监听
    private void registListener() {
        y.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickY();
            }
        });
        n.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickN();
            }
        });
        o.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                clickO(success.getVisibility() == View.VISIBLE);
            }
        });
    }

    //显示确定的动画
    public final void showAnimateY() {
        success.setVisibility(View.VISIBLE);
        container2.setVisibility(View.VISIBLE);
        attention.setVisibility(View.GONE);
        container1.setVisibility(View.GONE);
        title.setText(builder.clickYesTitle);
        message.setText(builder.clickYesMessage);
        o.setText(builder.clickYesButton);
        handlerSuccess.obtainMessage().sendToTarget();
    }

    //显示取消的动画
    public final void showAnimateN() {
        failure.setVisibility(View.VISIBLE);
        container2.setVisibility(View.VISIBLE);
        attention.setVisibility(View.GONE);
        container1.setVisibility(View.GONE);
        title.setText(builder.clickNoTitle);
        message.setText(builder.clickNoMessage);
        o.setText(builder.clickNoButton);
        handlerFailure.obtainMessage().sendToTarget();
    }

    public void clickY() {

    }

    public void clickN() {

    }

    public void clickO(boolean ok) {
    }
}
