package com.example.robogame;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.view.SurfaceHolder;

/**
 * Manages logics, physics and animation of the game between frames.
 * @author ssergey
 *
 */
public class GameThread extends Thread {
	/** 40ms between frames. */
	private static int FRAME_RATE = 40;
	/** For resources and app stuff. */
	private Context mContext;
	/**  For drawing on surface. */
	SurfaceHolder mSurfaceHolder;
	boolean mRun = false; 

	private int mCanvasW = 0;
	private int mCanvasH = 0;

	/**
	 * Main character. Manages its own animation, health, health bar,
	 * death events and animation, preserves location. 
	 */
	Robo mRobo;
	/**
	 * Collection of all the fires on map. 
	 * Manages fires' animation and location.
	 */
	private Fires mFires;

	/**
	 *  Wall objects - map borders (walls) and on-map walls (drawable walls).
	 *  Robo stops his x/y speed when hits a wall.
	 */
	private Wall mTopWall;
	private Wall mLeftWall;
	private Wall mRightWall;
	private Wall mBottomWall;
	LinkedList<Wall> mWalls;
	LinkedList<Wall> mDrawWalls;
	LinkedList<Wall> mAllWalls;

	// Scratch list for hit walls.
	LinkedList<Wall> mHitList;

	/** Ordered list of destination points. */
	Route mRoute;

	public GameThread(SurfaceHolder surfaceHolder, Context context)
			throws IOException, JSONException {
		mSurfaceHolder = surfaceHolder;
		mContext = context;

		mWalls = new LinkedList<Wall>();
		mDrawWalls = new LinkedList<Wall>();
		mAllWalls = new LinkedList<Wall>();
		
		mHitList = new LinkedList<Wall>();

		mRobo = new Robo(mContext);
		mFires = new Fires(mContext);

		mRoute = new Route();
	}

	/**
	 *  Callback called when surface size is changed.
	 * @param width - new canvas width.
	 * @param height - new canvas height.
	 */
	// See GameView::surfaceChanged.
	public void setSurfaceSize(int width, int height) {
		synchronized (mSurfaceHolder) {
			if (mCanvasW == 0) {
				mCanvasW = width;
				mCanvasH = height;
				initWalls();
			}
		}
	}

	public void onTouch(int x, int y) {
		mFires.add(x, y, mAllWalls);
	}

	/**
	 * Inits walls. Must be called when canvas size is set.
	 *
	 */
	private void initWalls() {
		// TODO: Make sure we init width and height.
		final int depth = 1000;
		//mSurfaceHolder.getSurfaceFrame();
		mTopWall = new Wall(0, -depth, mCanvasW, 0);
		mLeftWall = new Wall(-depth, 0, 0, mCanvasH);
		mRightWall = new Wall(mCanvasW, 0, mCanvasW + depth, mCanvasH);
		mBottomWall = new Wall(0, mCanvasH, mCanvasW, mCanvasH + depth);
		mWalls.add(mTopWall);
		mWalls.add(mLeftWall);
		mWalls.add(mRightWall);
		mWalls.add(mBottomWall);

		mDrawWalls.add(new Wall(400, 400, 100, 200));

		mAllWalls.addAll(mDrawWalls);
		mAllWalls.addAll(mWalls);
	}

	/**
	 * Draw Game Over message.
	 * @param c - Canvas to draw on.
	 */
	private void drawGameOver(Canvas c) {
		Paint paint = new Paint(); 
		paint.setColor(Color.RED); 
		paint.setTextSize(40); 
		c.drawText("Game Over", mCanvasW/3, mCanvasH/2, paint); 
	}

	void setRunning(boolean val) {
		mRun = val;
	}

	@Override
	public void run() {
		long startTime = 0;
		long endTime = 0;
		long sleepTime = 0;
		while (mRun) {
			Canvas c = null;
			startTime = System.currentTimeMillis();
			try {
				c = mSurfaceHolder.lockCanvas();
				if (c == null) {
					// Surface was destroyed. mRun should be false.
					continue;
				}
				synchronized (mSurfaceHolder) {
					updatePhysics();
					draw(c);
				}
			} finally {
				// Do this in a finally so that if an exception is thrown
				// during the above, we don't leave the Surface in an
				// inconsistent state.
				if (c != null) {
					mSurfaceHolder.unlockCanvasAndPost(c);
				}
			}
			endTime = System.currentTimeMillis();
			sleepTime = FRAME_RATE - (endTime - startTime);
			if (sleepTime > 0) {
				try {
					sleep(sleepTime);
				} catch (InterruptedException e) {}
			}
		}
	}

	/**
	 * Checks if robo collides with fires or walls, updates its health, 
	 * direction. Moves robo to next position.
	 */
	private void updatePhysics() {
		if (mRobo.isAlive()) {
			// Check if we hit some explosions, remove them and
			// update health.
			mRobo.incHealth(-mFires.collide(mRobo));
			chooseDestination();
		}
		// Update velocity, in case we hit something.
		// TODO: not using hitList currently.
		mHitList.clear();
		mRobo.checkStatic(mAllWalls, mHitList);
		mRobo.move();
	}

	void chooseDestination() {
		if (mRoute.size() == 0) {
			if (mFires.size() > 0 ) {
				final int fX = mFires.getX(0);
				final int fY = mFires.getY(0);
				mRoute.push(fX, fY);
			} else {
				// If no destinations, stop.
				mRobo.set_vx(0);
				mRobo.set_vy(0);
			}
			return;
		} 
		Point dst = mRoute.peek();
		if (mRobo.intersects(dst.x, dst.y)) {
			mRoute.pop();
			return;
		}

		mRobo.headTo(dst.x, dst.y);
	}

	private void draw(Canvas c) {
		c.drawColor(Color.BLACK);
		for (Wall wall : mDrawWalls) {
			wall.draw(c);
		}
		// Robo knows when it dies and draws itself appropriately.
		mRobo.draw(c);
		mFires.draw(c); 
		if (mRobo.isOver()) {
			drawGameOver(c);
		}
	}
}
