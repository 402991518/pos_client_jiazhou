package com.uxun.pos.view.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.uxun.pos.R;

/**
 * @author Administrator 自定义 Toast
 */
@SuppressLint("InflateParams")
public class Toast {

	public final static int LENGTH_SHORT = android.widget.Toast.LENGTH_SHORT;

	public final static int LENGTH_LONG = android.widget.Toast.LENGTH_LONG;

	private android.widget.Toast toast;

	private Toast(Context context, CharSequence text, int duration) {
		View view = LayoutInflater.from(context).inflate(R.layout.toeast, null);
		TextView textView =  view.findViewById(R.id.message);
		textView.setText(text);
		toast = new android.widget.Toast(context);
		toast.setDuration(duration);
		toast.setView(view);
	}

	public static Toast makeText(Context context, CharSequence text, int duration) {
		return new Toast(context, text, duration);
	}

	public void show() {
		if (toast != null) {
			toast.show();
		}
	}

	public void setGravity(int gravity, int xOffset, int yOffset) {
		if (toast != null) {
			toast.setGravity(gravity, xOffset, yOffset);
		}
	}
}
