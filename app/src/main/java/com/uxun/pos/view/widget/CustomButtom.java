package com.uxun.pos.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.uxun.pos.R;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.SystemConfig;

import java.util.Arrays;

public class CustomButtom extends View {

    private static int BUTTON_COLOR_STATE_UP = ContextCompat.getColor(Application.getContext(), R.color.default_theme_color);
    private static int BUTTON_COLOR_STATE_DOWN = ContextCompat.getColor(Application.getContext(), R.color.default_bg_color);

    private int w;
    private int h;

    private int delta;

    private Paint paintText;
    private Paint paintIcon;

    private int colorBg;
    private int colorText;
    private String text;
    private Bitmap icon;

    private int colorBgReal;


    public CustomButtom(Context context, String text, Bitmap icon, int colorBg, int colorText) {
        super(context);
        this.text = text;
        this.icon = icon;
        this.colorBg = colorBg;
        this.colorText = colorText;
        this.initPaint();
        setClickable(true);
    }

    public CustomButtom(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initResource(context, attrs);
        this.initPaint();
        setClickable(true);
    }

    private void initResource(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomButtom);
        //1.获取文字
        this.text = typedArray.getString(R.styleable.CustomButtom_text);
        //2.获取图标
        Drawable drawable = typedArray.getDrawable(R.styleable.CustomButtom_icon);
        if (drawable != null) {
            try {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
                icon = bitmapDrawable.getBitmap();
            } catch (Exception e) {
            }
        }
        //获取背景色
        colorBg = typedArray.getColor(R.styleable.CustomButtom_colorBg, BUTTON_COLOR_STATE_UP);
        colorText = typedArray.getColor(R.styleable.CustomButtom_colorText, Color.BLACK);
    }

    private void initPaint() {
        colorBgReal = colorBg;
        paintText = new Paint();
        paintText.setAntiAlias(true);
        paintText.setTextAlign(Paint.Align.CENTER);
        paintText.setColor(colorText);
        paintText.setTextSize(SystemConfig.font_size * SystemConfig.density);
        Paint.FontMetricsInt fontMetrics = paintText.getFontMetricsInt();
        delta = Math.abs(fontMetrics.ascent) - (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent)) / 2;

        paintIcon = new Paint();
        paintIcon.setAntiAlias(true);
        float[] matrix = new float[20];
        Arrays.fill(matrix, 0);
        matrix[0] = matrix[6] = matrix[12] = matrix[18] = 1;
        paintIcon.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(matrix)));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        if (icon != null) {
            float scale_w = w / icon.getWidth() + 0.5f;
            float scale_h;
            if (text != null) {
                scale_h = 0.75f * h / icon.getHeight() + 0.5f;
            } else {
                scale_h = 1.00f * h / icon.getHeight() + 0.5f;
            }
            float scale = scale_w > scale_h ? scale_h : scale_w;
            if (scale < 1) {
                int bitmap_w = (int) (icon.getWidth() * scale + 0.5f);
                int bitmap_h = (int) (icon.getHeight() * scale + 0.5f);
                icon = Bitmap.createScaledBitmap(icon, bitmap_w, bitmap_h, true);
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawColor(colorBgReal);
        if (icon != null && text != null) {
            canvas.drawBitmap(icon, (w - icon.getWidth()) / 2, (0.75f * h - icon.getHeight()) / 2, paintIcon);
            canvas.drawText(text, w / 2, 0.75f * h + delta, paintText);
        } else if (icon != null && text == null) {
            canvas.drawBitmap(icon, (w - icon.getWidth()) / 2, (h - icon.getHeight()) / 2, paintIcon);
        } else if (icon == null && text != null) {
            canvas.drawText(text, w / 2, 0.50f * h + delta, paintText);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                colorBgReal = BUTTON_COLOR_STATE_DOWN;
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                colorBgReal = colorBg;
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setText(String text) {
        this.text = text;
        invalidate();
    }

    public String getText() {
        return text;
    }
}
