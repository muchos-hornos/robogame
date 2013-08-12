package com.example.robogame;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

public class Robo {
	private static int INIT_X = 200;
	private static int INIT_Y = 200;

	public Robo(Context context) throws IOException, JSONException {
		mRect = new Rect();
		mScratch = new Rect();
		mAngle = 0;
		mSprite = new AtlasSprite(context, R.raw.robo, R.raw.robo_js);
		mDSprite = new DeathExpSheet(context);
		mRect.top = 200;
		mRect.left = 200;
		mRect.right = mRect.left + mSprite.maxWidth();
		mRect.bottom = mRect.top + mSprite.maxHeight();
		mBar = new HealthBar(mHealth);
		mBombPlayer = MediaPlayer.create(context, R.raw.exp1);
	}

	// Change the guy's velocity to point to the given direction,
	// while leaving velocity's abs value the same.
	public void headTo(int newX, int newY) {
		int dX = newX - mRect.left;
		int dY = newY - mRect.top;
		double absDV = Math.sqrt(dX*dX + dY*dY);
		mVX = (int) (mABSV * (dX / absDV));
		mVY = (int) (mABSV * (dY / absDV));
		updateAngle();
	}

	// Rotate guy's sprite angle.
	private void updateAngle() {
		if (mVX == 0 && mVY == 0) {
			return;
		}
		mAngle = Math.toDegrees(Math.acos(mVX / mABSV));
		if (mVY < 0) {
			mAngle *= -1;
		}
	}

	int vx() { return mVX; }
	void set_vx(int v) { mVX = v; }

	int vy() { return mVY; }
	void set_vy(int v) { mVY = v; }

	void updateV(int vx, int vy) {
		mVX = vx;
		mVY = vy;
	}

	/** Checks if robo hit walls and zeroes appropriate v on hit.
	 * If no hit does nothing.
	 */
	void checkStatic(LinkedList<Wall> walls, LinkedList<Wall> hits) {
		for (Wall wall : walls) {
			mScratch.set(mRect);
			if (mScratch.intersect(wall.rect())) {
				if (mScratch.width() < mScratch.height()) {
					// Only zero the VX if we are moving towards the wall.
					if ((mScratch.centerX() - mRect.centerX()) * mVX > 0) {
						set_vx(0);
						hits.add(wall);
					}
				} else {
					// Only zero the VY if we are moving towards the wall.
					if((mScratch.centerY() - mRect.centerY()) * mVY > 0) {
						set_vy(0);
						hits.add(wall);
					}
				}
			}
		}
	}
	
	void getHits(LinkedList<Wall> walls, LinkedList<Wall> hits) {
		for (Wall wall : walls) {
			mScratch.set(mRect);
			if (mScratch.intersect(wall.rect())) {
				hits.add(wall);
			}
		}
	}

	public void move() {
		mRect.offset(mVX, mVY);
	}

	public boolean isOver() {
		return (mHealth <= 0 && mDSprite.isOver());
	}

	public void draw(Canvas c) {
		updateAngle();
		if (mHealth <= 0) {
			// Draw death animation.
			if (!mDSprite.isDestroyed()) {
				// For the first couple of frames still draw the robo.
				mSprite.draw(c, mRect.left, mRect.top, mAngle);
			}
			// Draw the explosion.
			mDSprite.draw(c, mRect, 0);
			// TODO: Find a better place for this.
			if (!mHasPlayed) {
				mBombPlayer.start();
				mHasPlayed = true;
			}
		} else {
			mSprite.draw(c, mRect.left, mRect.top, mAngle);
			mBar.draw(c, mHealth);
		}
	}

	public Rect rect() {
		return mRect;
	}

	public boolean intersects(int x, int y) {
		mScratch.left = x;
		mScratch.top = y;
		// To avoid tunelling.
		mScratch.right = x + 40;
		mScratch.bottom = y + 40;
		return Rect.intersects(mRect, mScratch);
	}

	public boolean isAlive() {
		return mHealth > 0;
	}

	public void incHealth(int by) {
		mHealth += by;
		if (mHealth < 0) {
			mHealth = 0;
		}
	}


	Rect mRect;
	Rect mScratch;
	AtlasSprite mSprite;
	private DeathExpSheet mDSprite;
	private double mAngle;
	// Velocity of the guy.
	private int mVX = 0;
	private int mVY = 0;
	private int mHealth = 400;
	private HealthBar mBar;
	// Abs value of the guys velocity.
	private final double mABSV = 15;

	/**  For audio.*/
	private MediaPlayer mBombPlayer;
	private boolean  mHasPlayed = false;
}
