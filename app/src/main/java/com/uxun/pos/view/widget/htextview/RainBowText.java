package com.uxun.pos.view.widget.htextview;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Shader;

public class RainBowText extends HText {

	private int mTextWidth;
	private LinearGradient mLinearGradient;
	private Matrix mMatrix;
	private float mTranslate;
	private int dx;

	private int[] colors = new int[] {

	Color.rgb(254, 67, 101),

	Color.rgb(252, 157, 154),

	Color.rgb(249, 205, 173),

	Color.rgb(200, 200, 169),

	Color.rgb(131, 175, 155),

	};

	@Override
	protected void initVariables() {
		mMatrix = new Matrix();
		dx = DisplayUtils.dp2Px(7);
	}

	public void setColors(int colors[]) {
		this.colors = colors;
	}

	@Override
	protected void animateStart(CharSequence text) {
		mHTextView.invalidate();
	}

	@Override
	protected void animatePrepare(CharSequence text) {
		mTextWidth = (int) mPaint.measureText(mText, 0, mText.length());
		mTextWidth = Math.max(DisplayUtils.dp2Px(100), mTextWidth);
		if (mTextWidth > 0) {
			mLinearGradient = new LinearGradient(0, 0, mTextWidth, 0, colors, null, Shader.TileMode.MIRROR);
			mPaint.setShader(mLinearGradient);
		}
	}

	@Override
	protected void drawFrame(Canvas canvas) {
		if (mMatrix != null && mLinearGradient != null) {
			mTranslate += dx;
			mMatrix.setTranslate(mTranslate, 0);
			mLinearGradient.setLocalMatrix(mMatrix);
			canvas.drawText(mText, 0, mText.length(), startX, startY, mPaint);
			mHTextView.postInvalidateDelayed(100);
		}
	}
}
