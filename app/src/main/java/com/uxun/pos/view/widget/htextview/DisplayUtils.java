package com.uxun.pos.view.widget.htextview;

import android.content.Context;
import android.content.res.Resources;

public final class DisplayUtils {

	private static final float DENSITY = Resources.getSystem().getDisplayMetrics().density;

	public static int dp2Px(int dp) {
		return Math.round(dp * DENSITY);
	}

	public static int dp2Px(Context ctx, float dpValue) {
		final float density = ctx.getResources().getDisplayMetrics().density;
		return (int) (dpValue * density + 0.5f);
	}
}
