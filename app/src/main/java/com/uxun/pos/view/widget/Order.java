package com.uxun.pos.view.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;


//序号图形
public class Order extends TextView {

    private int w;
    private int h;

    private Paint paintMax = new Paint();
    private Paint paintMin = new Paint();

    private int minRadius;
    private int maxRadius;

    public Order(Context context) {
        this(context, null);
    }

    public Order(Context context, AttributeSet attrs) {
        super(context, attrs);
        paintMax.setAntiAlias(true);
        paintMax.setStyle(Paint.Style.FILL);
        paintMax.setColor(Color.BLACK);

        paintMin.setAntiAlias(true);
        paintMin.setStyle(Paint.Style.FILL);
        paintMin.setColor(Color.WHITE);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        maxRadius = w > h ? h / 2 : w / 2;
        minRadius = maxRadius - 4;
    }

    //设置大圆的颜色和小圆的颜色
    public void setColor(int maxCircleColor, int minCircleColor) {
        paintMax.setColor(maxCircleColor);
        paintMin.setColor(minCircleColor);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawCircle(w / 2, h / 2, maxRadius, paintMax);
        canvas.drawCircle(w / 2, h / 2, minRadius, paintMin);
        super.onDraw(canvas);
    }
}
