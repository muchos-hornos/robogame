package com.example.robogame;

import java.io.IOException;
import java.util.LinkedList;

import org.json.JSONException;

import com.example.myfirstapp.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
public class AnimatedView extends ImageView{
	private Context mContext;
	private Wall mTopWall;
	private Wall mLeftWall;
	private Wall mRightWall;
	private Wall mBottomWall;
	LinkedList<Wall> mWalls;
	LinkedList<Wall> mDrawWalls;
	LinkedList<Wall> mAllWalls;
	private Handler h;
	// 40 ms between frames.
	private final int FRAME_RATE = 40;

	Robo mRobo;
	private Fires mFires;

	private MediaPlayer mBombPlayer;
	private boolean  mHasPlayed = false;

	// Invalidates the ImageView.
	private Runnable r = new Runnable() {
		@Override
		public void run() {
			invalidate();
		}
	};


	public AnimatedView(Context context, AttributeSet attrs) 
			throws IOException, JSONException  {
		super(context, attrs);
		mContext = context;
		h = new Handler();

		mWalls = new LinkedList<Wall>();
		mDrawWalls = new LinkedList<Wall>();
		mAllWalls = new LinkedList<Wall>();

		mRobo = new Robo(context);

		mFires = new Fires(mContext);

		mBombPlayer = MediaPlayer.create(mContext, R.raw.exp1);

		// On touch we add a fire.
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mFires.add((int) arg1.getX(), (int) arg1.getY(), 
						mAllWalls);
				return true;
			}
		});
	}

	private void initWalls() {
		final int depth = 1000;
		mTopWall = new Wall(0, -depth, this.getWidth(), 0);
		mLeftWall = new Wall(-depth, 0, 0, this.getHeight());
		mRightWall = new Wall(this.getWidth(), 0,
				this.getWidth() + depth, this.getHeight());
		mBottomWall = new Wall(0, this.getHeight(), 
				this.getWidth(), this.getHeight() + depth);
		mWalls.add(mTopWall);
		mWalls.add(mLeftWall);
		mWalls.add(mRightWall);
		mWalls.add(mBottomWall);
		
		mDrawWalls.add(new Wall(400, 400, 100, 200));
		
		mAllWalls.addAll(mDrawWalls);
		mAllWalls.addAll(mWalls);
	}

	private void drawGameOver(Canvas c) {
		Paint paint = new Paint(); 
		paint.setColor(Color.RED); 
		paint.setTextSize(40); 
		c.drawText("Game Over", this.getWidth()/3, this.getHeight()/2, paint); 
	}

	private void updatePhysics() {
		if (mRobo.isAlive()) {
			// Check if we hit some explosions, remove them and
			// update health.
			mRobo.incHealth(-mFires.collide(mRobo.rect()));

			if (mFires.size() > 0) {
				// Change direction to the next fire.
				mRobo.headTo(mFires.getX(0), mFires.getY(0));
			} else {
				// If no fires, stop.
				mRobo.set_vx(0);
				mRobo.set_vy(0);
			}
		}
		// Update velocity, in case we hit something.
		mRobo.checkStatic(mAllWalls);
		mRobo.move();
	}

	protected void onDraw(Canvas c) {
		if (mWalls.size() == 0) {
			initWalls();
		}
		updatePhysics();
		for (Wall wall : mDrawWalls) {
			wall.draw(c);
		}
		mRobo.draw(c);
		mFires.draw(c);
		if (!mRobo.isAlive()) {
			if (mRobo.isOver()) {
				drawGameOver(c);
			}
			if (!mHasPlayed) {
				mBombPlayer.start();
				mHasPlayed = true;
			}
		} 
		// Schedule the next frame.
		h.postDelayed(r, FRAME_RATE);
	}
}
