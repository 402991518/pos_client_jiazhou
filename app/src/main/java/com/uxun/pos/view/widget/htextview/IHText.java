package com.uxun.pos.view.widget.htextview;

import android.graphics.Canvas;
import android.util.AttributeSet;


public interface IHText {
	
	void init(HTextView hTextView, AttributeSet attrs, int defStyle);
	void animateText(CharSequence text);
	void onDraw(Canvas canvas);
	void reset(CharSequence text);
	
}
