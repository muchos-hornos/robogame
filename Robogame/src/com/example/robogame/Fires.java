package com.example.robogame;

import java.util.LinkedList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;

import com.example.myfirstapp.R;

public class Fires {
	public Fires(Context context) {
		// TODO: Atlas.
		eFrames = new BitmapDrawable[]{ 
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire01),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire02),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire03),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire04),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire05),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire06),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire07),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire08),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire09),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire10),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire11),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire12),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire13),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire14),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire15),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire16),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire17),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire18),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire19),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire20),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire21),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire22),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire23),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire24),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.fire25)};

		mXs = new LinkedList<Integer>();
		mYs = new LinkedList<Integer>();
		mFids = new LinkedList<Integer>();

		mWidth = eFrames[2].getBitmap().getWidth();
		mHeight = eFrames[2].getBitmap().getHeight();
		for (BitmapDrawable d : eFrames) {
			int w = d.getBitmap().getWidth();
			int h = d.getBitmap().getHeight();
			if (mWidth < w) {
				mWidth = w;
			}
			if (mHeight < h) {
				mHeight = h;
			}
		}
	}

	void add(int x, int y, int canvasW, int canvasH) {
		int dstX = x;
		int dstY = y;
		if (x + mWidth > canvasW) {
			dstX = canvasW - mWidth;
		}
		if (y + mHeight > canvasH) {
			dstY = canvasH - mHeight;
		}
		mXs.add(dstX);
		mYs.add(dstY);
		mFids.add(0);
	}

	void remove(int idx) {
		mXs.remove(idx);
		mYs.remove(idx);
		mFids.remove(idx);
	}

	// Returns number of collisions.
	public int collide(Rect guy) {
		int i = 0;
		int count = 0;
		while (i < mXs.size()) {
			int x = mXs.get(i);
			int y = mYs.get(i);
			if (guy.intersects(x, y, x + mWidth, y + mHeight)) {
				remove(i);
				count++;
			} else {
				i++;
			}
		}
		return count;
	}

	public int size() {
		return mXs.size();
	}

	public int getX(int idx) {
		return mXs.get(idx);
	}

	public int getY(int idx) {
		return mYs.get(idx);
	}

	public void draw(Canvas c) {
		int i = 0;
		for (i = 0; i < mXs.size(); i++) {
			int fid = mFids.get(i);
			BitmapDrawable expl = eFrames[fid];
			c.drawBitmap(expl.getBitmap(), mXs.get(i), mYs.get(i), null);
			fid = (fid + 1) % eFrames.length; 
			mFids.set(i, fid); 
		}
	}

	// Explosions' frames.
	BitmapDrawable[] eFrames;
	// Explosions (x,y)'s. 
	// TODO: Implement a pool of Point,ExpSprite objects.
	private LinkedList<Integer> mXs;
	private LinkedList<Integer> mYs;
	private LinkedList<Integer> mFids;

	// Explosions' size.
	private int mWidth;
	private int mHeight;
}
