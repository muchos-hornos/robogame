package com.example.robogame;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class Wall {
	private static int CR = 0;
	private static int CG = 255;
	private static int CB = 0;
	
	public Wall(int x, int y, int w, int h) {
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setARGB(255, CR, CG, CB);
		mRect = new Rect(x, y, x + w, y + h);
	}
	
	Rect rect() { return mRect; }

	public void draw(Canvas c) {
		c.drawRect(mRect, mPaint);
	}
	private Paint mPaint;
	private Rect mRect;
}
