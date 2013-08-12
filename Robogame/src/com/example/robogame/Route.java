package com.example.robogame;

import android.graphics.Point;
import android.util.Log;

/** Ordered list of points. 
 * Fixed size, when full throws oldest point on add.
 *  */ 
public class Route {
	private static final int POOL_SIZE = 100;
	public Route() {
		// Initialize pool.
		mPoints = new Point[POOL_SIZE];
		for (int i = 0; i < mPoints.length; i++) {
			mPoints[i] = new Point();
		}
	}
	
	public void push(int x, int y) {
		mPoints[mWriteIndex % mPoints.length].x = x;
		mPoints[mWriteIndex % mPoints.length].y = y;
		mWriteIndex++;
		Log.d("ROUTE", "New point to x: " + x + " y: " + y + " size: " + size());
	}
	
	public Point pop() {
		if (mWriteIndex > 0) {
			mWriteIndex = (mWriteIndex - 1) % mPoints.length;
			return mPoints[mWriteIndex];
			 
		}
		return null;
	}
	
	public Point peek() {
		if (mWriteIndex > 0) {
			return mPoints[(mWriteIndex - 1) % mPoints.length];			 
		}
		return null;
	}
	
	public int size() {
		if (mWriteIndex > 0) {
			return mWriteIndex % mPoints.length;
		}
		return 0;
	}

	private Point[] mPoints;
	private int mWriteIndex = 0;
}
