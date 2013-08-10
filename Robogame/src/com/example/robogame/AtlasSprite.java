package com.example.robogame;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;

public class AtlasSprite {
	public AtlasSprite(Context context, int imgRid, int jsonRid) 
			throws IOException, JSONException {
		mBitmap = ReadRawToBitMap(context, imgRid);
		mFrames = GetFrames(context, jsonRid); 
		mDstRect = new Rect();

		mMaxWidth = 0;
		mMaxHeight = 0;
		for (Rect frame : mFrames) {
			if (frame.width() > mMaxWidth) {
				mMaxWidth = frame.width();
			}
			if (frame.height() > mMaxHeight) {
				mMaxHeight = frame.height();
			}
		}
	}

	private static String FRAMES = "frames";
	private static String RECT = "frame";
	
	static Bitmap ReadRawToBitMap(Context context, int rid) {
		InputStream stream = context.getResources().openRawResource(rid);
		return BitmapFactory.decodeStream(stream);
	}

	static String ReadRawToStr(Context context, int rid) throws IOException {
		InputStream stream = context.getResources().openRawResource(rid);
		BufferedReader reader = 
				new BufferedReader(new InputStreamReader(stream));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null) {
			sb.append(line + "\n");
		}
		stream.close();

		return sb.toString();
	}

	static public Rect[] GetFrames(Context context, int rid) 
			throws IOException, JSONException {
		String content = ReadRawToStr(context, rid);
		JSONObject root = new JSONObject(content);
		JSONArray jFrames = root.getJSONArray(FRAMES);
		Rect[] frames = new Rect[jFrames.length()];
		int i = 0;
		for (i = 0; i < jFrames.length(); i++) {
			JSONObject frame = jFrames.getJSONObject(i);
			JSONObject jRect = frame.getJSONObject(RECT);
			int left = jRect.getInt("x");
			int top = jRect.getInt("y");
			int right = left + jRect.getInt("w");
			int bottom = top + jRect.getInt("h");
			frames[i] = new Rect(left, top, right, bottom);
		}
		return frames;
	}

	public void draw(Canvas c, int x, int y, double angle) {
		drawFrame(c, x, y, angle, mFid);
		mFid = (mFid + 1) % mFrames.length;
	}

	public void drawFrame(Canvas c, int x, int y, double angle, int fid) {
		Rect src = mFrames[fid % mFrames.length];
		mDstRect.set(src);
		mDstRect.offsetTo(x, y);
		c.drawBitmap(mBitmap, src, mDstRect, null);
	}
	
	public int size() {
		return mFrames.length;
	}
	
	public int maxWidth() {
		return mMaxWidth;
	}
	
	public int maxHeight() {
		return mMaxHeight;
	}

	private Bitmap mBitmap;
	private Rect[] mFrames;
	private int mFid = 0;
	private Rect mDstRect;
	private int mMaxWidth;
	private int mMaxHeight;
}
