package com.example.robogame;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.myfirstapp.R;

public class Fires {
	public Fires(Context context) throws IOException, JSONException {
		mSprite = new AtlasSprite(context, R.raw.fireatlas, 
				R.raw.fireatlas_js);

		mXs = new LinkedList<Integer>();
		mYs = new LinkedList<Integer>();
		mFids = new LinkedList<Integer>();

		mWidth = mSprite.maxWidth();
		mHeight = mSprite.maxHeight();
		mRect = new Rect(0, 0, mWidth, mHeight);
	}

	void add(int x, int y, LinkedList<Wall> walls) {
		// Do not add fires on walls.
		for (Wall wall : walls) {
			mRect.offsetTo(x, y);
			if (Rect.intersects(mRect, wall.rect())) {
				return;
			}
			mXs.add(x);
			mYs.add(y);
			mFids.add(0);
		}
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
			mSprite.drawFrame(c, mXs.get(i), mYs.get(i), 0, fid);
			fid = (fid + 1) % mSprite.size(); 
			mFids.set(i, fid); 
		}
	}

	// Explosions' frames.
	AtlasSprite mSprite;
	// Explosions (x,y)'s. 
	// TODO: Implement a pool of Point,ExpSprite objects.
	private LinkedList<Integer> mXs;
	private LinkedList<Integer> mYs;
	private LinkedList<Integer> mFids;

	// Explosions' size.
	private int mWidth;
	private int mHeight;
	private Rect mRect;
}
