package com.example.robogame;

import java.io.IOException;

import org.json.JSONException;

import com.example.myfirstapp.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
public class AnimatedView extends ImageView{
	private Context mContext;
	private int mWidth;
	private int mHeight;
	// (x,y) of the guy.
	Rect gRect;
	AtlasSprite gSprite;
	private double angle;
	// Velocity of the guy.
	private int xVelocity = 15;
	private int yVelocity = 0;
	// Abs value of the guys velocity.
	private double absV = Math.sqrt(xVelocity*xVelocity + yVelocity*yVelocity);
	private Handler h;
	// 40 ms between frames.
	private final int FRAME_RATE = 40;
	private Fires mFires;

	private DeathExpSheet mDSprite;


	/** Paint to draw the lines on screen. */
	private int mHealth = 100;
	private final int HEALTH_BAR_X = 4;
	private final int HEALTH_BAR_Y = 4;
	private final int HEALTH_BAR_H = 10;
	private Paint mLinePaint;
	private RectF mScratchRect;
	
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
		// Init the guy.
		// TODO: A class for it?
		gRect = new Rect();
		angle = 0;
		gSprite = new AtlasSprite(mContext, R.raw.robo, R.raw.robo_js);
		gRect.top = 20;
		gRect.left = 20;
		gRect.right = gRect.left + gSprite.maxWidth();
		gRect.bottom = gRect.top + gSprite.maxHeight();

		mFires = new Fires(mContext);

		mDSprite = new DeathExpSheet(mContext);

		// Initialize paints for speedometer
		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setARGB(255, 0, 255, 0);
		mScratchRect = new RectF(0, 0, 0, 0);
		
		mBombPlayer = MediaPlayer.create(mContext, R.raw.exp1);

		// On touch we add an explosion.
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mFires.add((int) arg1.getX(), (int) arg1.getY(), mWidth, mHeight);
				return true;
			}
		});
	}

	// Change the guy's velocity to point to the given direction,
	// while leaving velocity's abs value the same.
	private void changeV(int newX, int newY) {
		int dX = newX - gRect.left;
		int dY = newY - gRect.top;
		double absDV = Math.sqrt(dX*dX + dY*dY);
		xVelocity = (int) (absV * (dX / absDV));
		yVelocity = (int) (absV * (dY / absDV));
	}

	private void UpdateAngle() {
		if (xVelocity == 0 && yVelocity == 0) {
			return;
		}
		angle = Math.toDegrees(Math.acos(xVelocity / absV));
		if (yVelocity < 0) {
			angle *= -1;
		}
	}

	private void drawGameOverMsg(Canvas c) {
		// Draw the explosions.
		mFires.draw(c);
		if (!mDSprite.isDestroyed()) {
			gRect.offset(xVelocity, yVelocity);
			UpdateAngle();
			gSprite.draw(c, gRect.left, gRect.top, angle);
		}
		mDSprite.draw(c, gRect, angle);

		if (mDSprite.isOver()) {
			Paint paint = new Paint(); 
			paint.setColor(Color.RED); 
			paint.setTextSize(40); 
			c.drawText("Game Over", this.getWidth()/3, this.getHeight()/2, paint); 
		}
		h.postDelayed(r, FRAME_RATE);
	}

	protected void onDraw(Canvas c) {
		mWidth = this.getWidth();
		mHeight = this.getHeight();
		if (mHealth <= 0) {
			drawGameOverMsg(c);
			if (!mHasPlayed) {
				mBombPlayer.start();
				mHasPlayed = true;
			}
			return;
		}
		// Check if we hit some explosions and remove them.
		mHealth -= mFires.collide(gRect);

		// Change direction to the next explosion.
		if (mFires.size() > 0) {
			changeV(mFires.getX(0), mFires.getY(0));
		} else {
			xVelocity = 0;
			yVelocity = 0;
		}

		// If we hit the wall - change direction to the opposite.
		if ((gRect.right >= this.getWidth() && xVelocity > 0) || 
				(gRect.left <= 0 && xVelocity < 0)) {
			xVelocity = 0;
		}
		if ((gRect.bottom >= this.getHeight() && yVelocity > 0) || 
				(gRect.top <= 0 && yVelocity < 0)) {
			yVelocity = 0;
		}
		gRect.offset(xVelocity, yVelocity);
		UpdateAngle();
		gSprite.draw(c, gRect.left, gRect.top, angle);

		// Draw the explosions.
		mFires.draw(c);

		// Draw the health bar.
		mScratchRect.set(HEALTH_BAR_X, HEALTH_BAR_Y, 
				HEALTH_BAR_X + mHealth, HEALTH_BAR_H + 10);
		c.drawRect(mScratchRect, mLinePaint);

		// Schedule the next frame.
		h.postDelayed(r, FRAME_RATE);
	}
}
