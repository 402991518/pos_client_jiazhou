package com.uxun.pos.view.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.BounceInterpolator;


public class SuccessView extends View {


    private static final int PAINT_WIDTH = 4;

    private int w;
    private int h;

    private Path path1;
    private Path path2;
    private RectF rectF;
    private Paint paint;

    public SuccessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.parseColor("#67B845"));
        paint.setStrokeWidth(PAINT_WIDTH);
        paint.setStyle(Paint.Style.STROKE);
        path1 = new Path();
        path2 = new Path();
        rectF = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        this.w = w;
        this.h = h;
        initPath();
    }

    private void initPath() {
        rectF.left = 0 + PAINT_WIDTH;
        rectF.top = 0 + PAINT_WIDTH;
        rectF.right = w - PAINT_WIDTH;
        rectF.bottom = h - PAINT_WIDTH;
        path1.addArc(rectF, 0, 360);
        path2.moveTo(w / 2, h / 4);
        path2.lineTo(w / 2, h / 2);
        path2.lineTo(w, h / 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawPath(path1, paint);
        canvas.translate(-w / 16, h / 8);
        canvas.rotate(-45, w / 2, h / 2);
        canvas.drawPath(path2, paint);
    }

    public void startAnimate() {
        path1.reset();
        path2.reset();
        ValueAnimator valueAnimator1 = ValueAnimator.ofInt(0, -360);
        valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int delta = (int) animation.getAnimatedValue();
                path1.reset();
                path1.addArc(rectF, 0, delta);
                invalidate();
            }
        });
        ValueAnimator valueAnimator2 = ValueAnimator.ofInt(0, h / 2 + w / 2, h / 2 + w / 4, h / 2 + w / 2);
        valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int delta = (int) animation.getAnimatedValue();
                path2.reset();
                if (delta < h / 2) {
                    path2.moveTo(w / 2, 0);
                    path2.lineTo(w / 2, delta);
                } else {
                    path2.moveTo(w / 2, ((h / 4.0f) / (w / 2.0f)) * (delta - h / 2.0f));
                    path2.lineTo(w / 2, h / 2);
                    path2.lineTo(w / 2 + (delta - h / 2), h / 2);
                }
                invalidate();
            }
        });
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playSequentially(valueAnimator1, valueAnimator2);
        valueAnimator1.setDuration(200);
        animatorSet.start();
    }
}
