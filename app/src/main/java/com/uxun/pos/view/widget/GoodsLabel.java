package com.uxun.pos.view.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.View;

import com.uxun.pos.R;
import com.uxun.pos.domain.dto.GrdGoods;
import com.uxun.pos.global.Application;
import com.uxun.pos.global.SystemConfig;

// icon:id:name=4:2:4
public class GoodsLabel extends View {

    private static int GAP = 12;
    private static int BUTTON_COLOR_STATE_UP = Color.parseColor("#FFFFFF");
    private static int BUTTON_COLOR_STATE_DOWN = ContextCompat.getColor(Application.getContext(), R.color.default_bg_color);

    private int w;
    private int h;

    private Bitmap icon;//图标
    private String order;//序号
    private Bitmap itemno;//店内码
    private Bitmap itemname;//名称

    private GrdGoods goods;
    private Bitmap iconSRC;

    private Paint paintBg;
    private TextPaint textPaint;

    public GoodsLabel(Context context, int total, int index, GrdGoods goods) {
        super(context);
        this.goods = goods;
        this.order = "(" + index + "/" + total + ")";
        this.initPaint();
        this.setClickable(true);
    }

    private void initPaint() {
        paintBg = new Paint();
        paintBg.setAntiAlias(true);
        paintBg.setColor(BUTTON_COLOR_STATE_UP);
        if (goods != null) {
            iconSRC = BitmapFactory.decodeResource(getResources(), R.mipmap.sale);
            textPaint = new TextPaint();
            textPaint.setAntiAlias(true);
            textPaint.setTextSize(SystemConfig.font_size * SystemConfig.density);
            this.setTag(goods.BARCODE);
        } else {
            iconSRC = BitmapFactory.decodeResource(getResources(), R.mipmap.forbidden);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.h = h;
        this.w = w;
        if (goods != null) {
            icon = createIcon();
            itemno = createText(goods.BARCODE, textPaint, w);
            itemname = createText(goods.ITEMSUBNAME, textPaint, w);
        } else {
            icon = compressBitmap(iconSRC, w, h);
        }
    }

    //创建ICON的Bitmap
    private Bitmap createIcon() {
        //1.图标缩放
        int icon_height = (int) (0.3f * h);
        Bitmap temp = compressBitmap(iconSRC, w, icon_height);
        //2.字体基线的偏移量
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int delta = Math.abs(fontMetrics.ascent) - (Math.abs(fontMetrics.ascent) + Math.abs(fontMetrics.descent)) / 2;
        //3.序号文字的宽度
        Rect rect = new Rect();
        textPaint.getTextBounds(order, 0, order.length(), rect);
        int orderTextWidth = rect.width();//序号文本的宽度
        int iconPaddingLeft = (w - (temp.getWidth() + GAP + orderTextWidth)) / 2;//图标距离左边界的距离
        //4.创建ICON的Bitmap
        Bitmap icon = Bitmap.createBitmap(w, icon_height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(icon);
        canvas.drawBitmap(temp, iconPaddingLeft, (icon_height - temp.getHeight()) / 2, null);
        canvas.drawText(order, iconPaddingLeft + GAP + temp.getWidth(), icon_height / 2 + delta, textPaint);

        return icon;
    }

    //创建文本的Bitmap
    private Bitmap createText(String text, TextPaint textPaint, int textWidth) {
        StaticLayout staticLayout = new StaticLayout(text == null ? "" : text, textPaint, textWidth, Layout.Alignment.ALIGN_CENTER, 1.0f, 0.0f, true);
        Bitmap bitmap = Bitmap.createBitmap(staticLayout.getWidth(), staticLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        staticLayout.draw(canvas);
        return bitmap;
    }

    //压缩图形纵横比
    private Bitmap compressBitmap(Bitmap src, int w, int h) {
        float scale_w = w / iconSRC.getWidth() + 0.5f;
        float scale_h = h / iconSRC.getHeight() + 0.5f;
        float scale = scale_w > scale_h ? scale_h : scale_w;
        if (scale < 1) {
            int bitmap_w = (int) (src.getWidth() * scale + 0.5f);
            int bitmap_h = (int) (src.getHeight() * scale + 0.5f);
            return Bitmap.createScaledBitmap(src, bitmap_w, bitmap_h, true);
        }
        return src;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRoundRect(0, 0, w, h, GAP, GAP, paintBg);
        if (goods != null) {
            //1.绘制ICON和序号
            canvas.drawBitmap(icon, 0, 0, null);
            //2.绘制店内码
            canvas.drawBitmap(itemno, (w - itemno.getWidth()) / 2, 0.3f * h, null);
            //3.绘制名称
            canvas.drawBitmap(itemname, (w - itemname.getWidth()) / 2, 0.5f * h, null);
        } else {
            canvas.drawBitmap(icon, (w - icon.getWidth()) / 2, (h - icon.getHeight()) / 2, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                paintBg.setColor(BUTTON_COLOR_STATE_DOWN);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                paintBg.setColor(BUTTON_COLOR_STATE_UP);
                break;
        }
        invalidate();
        return super.onTouchEvent(event);
    }
}
