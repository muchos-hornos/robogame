package com.example.robogame;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public abstract class SpriteSheet {

	public SpriteSheet(BitmapDrawable[] frames) {
		mFid = 0;
		mTicks = 0;
		mTPF = 1;
		mWidth = frames[0].getBitmap().getWidth();
		mHeight = frames[0].getBitmap().getHeight();
		mFrames = frames;
	}
	
	void set_animate(boolean value) {
		mAnimate = value;
	}

	void tick() {
		if (mAnimate) {
			mTicks++;
			mFid ++;//= mTicks / mTPF;
			mFid = mFid % mFrames.length;
		}
	}
	
	public int width() {
		return mWidth;
	}

	public int height() {
		return mHeight;
	}
	
	public abstract void draw(Canvas c, Rect rect, double angle);

	protected BitmapDrawable[] mFrames;
	// Frame index.
	protected int mFid;
	protected int mTicks;
	// Ticks per frame.
	protected int mTPF;
	protected int mWidth;
	protected int mHeight;
	private boolean mAnimate = true;
}
