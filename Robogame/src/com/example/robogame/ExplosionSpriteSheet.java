package com.example.robogame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class ExplosionSpriteSheet extends SpriteSheet {
	public ExplosionSpriteSheet(BitmapDrawable[] frames) {
		super(frames);
		mTPF = 2;
	}

	@Override
	public void draw(Canvas c, Rect rect, double angle) {
		// TODO Auto-generated method stub

	}

}
