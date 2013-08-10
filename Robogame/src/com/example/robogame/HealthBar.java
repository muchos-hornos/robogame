package com.example.robogame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

public class HealthBar {
	public static int HEIGHT = 10;
	public static int X = 4;
	public static int Y = 4;

	public HealthBar(int width) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setARGB(255, 0, 255, 0);
		mRect = new RectF(X, Y, X + width, Y + HEIGHT);
	}

	public void draw(Canvas c, int w) {
		mRect.right = mRect.left + w;
		c.drawRect(mRect, mPaint);
	}

	private Paint mPaint;
	private RectF mRect;
}
