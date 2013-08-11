package com.example.robogame;

import java.io.IOException;

import org.json.JSONException;

import com.example.robogame.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;

public class DeathExpSheet {

	private final int NUM_DESTROY = 10;

	public DeathExpSheet(Context context) throws IOException, JSONException {
		mSprite = new AtlasSprite(context, R.raw.deathatlas, 
				R.raw.deathatlas_js);
	}

	public void draw(Canvas c, Rect rect, double angle) {
		if (mFid < mSprite.size()) {
			mSprite.draw(c, rect.left, rect.top, angle);
			mFid++;
		}
	}

	public boolean isDestroyed() {
		return mFid >= NUM_DESTROY;
	}

	public boolean isOver() {
		return mFid == mSprite.size();
	}

	private AtlasSprite mSprite;
	private int mFid = 0;
}
