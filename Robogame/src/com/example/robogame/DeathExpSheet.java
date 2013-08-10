package com.example.robogame;

import com.example.myfirstapp.R;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

public class DeathExpSheet {
	
	private final int NUM_DESTROY = 10;

	public DeathExpSheet(Context context) {
		mSheet = ((BitmapDrawable)context.getResources().
				getDrawable(R.drawable.ds)).getBitmap();
		mWidth = mSheet.getWidth() / mNumX;
		mHeight = mSheet.getHeight() / mNumY;
		mNumFrames = mNumX * mNumY;
		mSrc = new Rect(0, 0, mWidth, mHeight);
		mDst = new Rect(mSrc);
		//mNumY = 2;
	}

	public void draw(Canvas c, Rect rect, double angle) {
		if (mFid < mNumFrames) {
			mDst.offsetTo(rect.left, rect.top);
			mSrc.offsetTo(mWidth * (mFid % mNumX), mHeight * (mFid / mNumX));
			c.drawBitmap(mSheet, mSrc, mDst, null);
				mFid++;
		}
	}
	
	public boolean isDestroyed() {
		return mFid >= NUM_DESTROY;
	}
	
	public boolean isDead() {
		return mFid >= mNumFrames;
	}
	
	protected Bitmap mSheet;
	private int mNumFrames;
	private int mNumX = 5;
	private int mNumY = 4;
	private int mWidth;
	private int mHeight;
	private int mFid = 0;
	private Rect mSrc;
	private Rect mDst;

}
