package com.example.robogame;

import java.io.IOException;

import org.json.JSONException;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

public class GameView extends SurfaceView implements SurfaceHolder.Callback{
	// TODO: In progress.
	private GameThread mThread;

	public GameView(Context context, AttributeSet attrs) 
			throws IOException, JSONException  {
		super(context, attrs);

		// register our interest in hearing about changes to our surface
		SurfaceHolder holder = getHolder();
		holder.addCallback(this);

		// create thread only; it's started in surfaceCreated()
		mThread = new GameThread(holder, context);
		// On touch we add a fire.
		setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				mThread.onTouch((int) arg1.getX(), (int) arg1.getY());
				return true;
			}
		});
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		mThread.setSurfaceSize(width, height);	
	}

	@Override
	public void surfaceCreated(SurfaceHolder arg0) {
		mThread.setRunning(true);
		mThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder arg0) {
		boolean retry = true;
		mThread.setRunning(false);
		while (retry) {
			try {
				mThread.join();
				retry = false;
			} catch (InterruptedException e) {}
		}

	}
}
