package com.example.robogame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class RobotSpriteSheet extends SpriteSheet {
	
	public RobotSpriteSheet(BitmapDrawable[] frames) {
		super(frames);
	}

	@Override
	public void draw(Canvas c, Rect rect, double angle) {
		tick();
		c.save();
		// Rotate the guy. 180 is for walking forward instead of backward.
		c.rotate((float) angle + 180, rect.centerX(), rect.centerY());
		c.drawBitmap(mFrames[mFid].getBitmap(), rect.left, rect.top,  null);
		c.restore();
	}
}
