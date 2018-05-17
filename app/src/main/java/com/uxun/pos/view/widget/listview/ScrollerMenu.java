package com.uxun.pos.view.widget.listview;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScrollerMenu extends LinearLayout {

    //回调接口
    public interface OnMenuClickListener {
        void onClick();
    }

    //菜单创建辅助类
    public static class Builder {
        private String text = "自定义菜单";
        private int textColor = Color.BLACK;
        private int bgColor = Color.rgb(247, 133, 63);
        private int with = 200;
        private OnMenuClickListener onMenuClickListener;

        public Builder(String text, int textColor, int bgColor, int with, OnMenuClickListener onMenuClickListener) {
            this.text = text;
            this.textColor = textColor;
            this.bgColor = bgColor;
            this.with = with;
            this.onMenuClickListener = onMenuClickListener;
        }
    }

    private Builder[] builders;

    public ScrollerMenu(Context context, Builder... builders) {
        super(context);
        this.builders = builders;
        createMenus();
    }

    private void createMenus() {
        if (builders != null) {
            for (int i = 0; i < builders.length; i++) {
                final Builder builder = builders[i];
                TextView textView = new TextView(getContext());
                textView.setBackgroundColor(builder.bgColor);
                textView.setText(builder.text);
                textView.setTextColor(builder.textColor);
                textView.setLayoutParams(new LayoutParams(builder.with, LayoutParams.MATCH_PARENT));
                if (builder.onMenuClickListener != null) {
                    textView.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.onMenuClickListener.onClick();
                        }
                    });

                }
                addView(textView);
            }
        }
    }
}
