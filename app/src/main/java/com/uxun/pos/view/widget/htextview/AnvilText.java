package com.uxun.pos.view.widget.htextview;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.animation.BounceInterpolator;

public class AnvilText extends HText {

	private Paint bitmapPaint;
	private float ANIMA_DURATION = 800;
	private int mTextHeight = 0;
	private float progress;

	@Override
	protected void initVariables() {
		bitmapPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		bitmapPaint.setColor(Color.WHITE);
		bitmapPaint.setStyle(Paint.Style.FILL);
	}

	@Override
	protected void animateStart(CharSequence text) {
		ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1).setDuration((long) ANIMA_DURATION);
		valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				progress = (Float) animation.getAnimatedValue();
				mHTextView.invalidate();
			}
		});
		valueAnimator.start();
	}

	@Override
	protected void animatePrepare(CharSequence text) {
		Rect bounds = new Rect();
		mPaint.getTextBounds(mText.toString(), 0, mText.length(), bounds);
		mTextHeight = bounds.height();
	}

	@Override
	protected void drawFrame(Canvas canvas) {
		float offset = startX;
		float oldOffset = oldStartX;
		int maxLength = Math.max(mText.length(), mOldText.length());
		float percent = progress;
		for (int i = 0; i < maxLength; i++) {
			// draw old text
			if (i < mOldText.length()) {
				mOldPaint.setTextSize(mTextSize);
				int move = CharacterUtils.needMove(i, differentList);
				if (move != -1) {
					mOldPaint.setAlpha(255);
					float p = percent * 2f;
					p = p > 1 ? 1 : p;
					float distX = CharacterUtils.getOffset(i, move, p, startX, oldStartX, gaps, oldGaps);
					canvas.drawText(mOldText.charAt(i) + "", 0, 1, distX, startY, mOldPaint);
				} else {
					float p = percent * 2f;
					p = p > 1 ? 1 : p;
					mOldPaint.setAlpha((int) ((1 - p) * 255));
					canvas.drawText(mOldText.charAt(i) + "", 0, 1, oldOffset, startY, mOldPaint);
				}
				oldOffset += oldGaps[i];
			}
			// draw new text
			if (i < mText.length()) {
				if (!CharacterUtils.stayHere(i, differentList)) {
					float interpolation = new BounceInterpolator().getInterpolation(percent);
					mPaint.setAlpha(255);
					mPaint.setTextSize(mTextSize);
					float y = startY - (1 - interpolation) * mTextHeight * 2;
					float width = mPaint.measureText(mText.charAt(i) + "");
					canvas.drawText(mText.charAt(i) + "", 0, 1, offset + (gaps[i] - width) / 2, y, mPaint);
				}
				offset += gaps[i];
			}
		}

	}

}
