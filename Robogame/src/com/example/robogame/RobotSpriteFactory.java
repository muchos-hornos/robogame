package com.example.robogame;

import com.example.myfirstapp.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

public class RobotSpriteFactory {

	public RobotSpriteFactory(Context context) {
		mFrames = new BitmapDrawable[]{ 
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_0),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_1),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_2),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_3),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_4),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_5),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_6),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_7),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_8),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_9),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_10),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_11),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_12),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_13),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_14),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_15),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_16),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_17),
				(BitmapDrawable)context.getResources().getDrawable(R.drawable.r_18)};
	}
	
	public RobotSpriteSheet getNew() {
		return new RobotSpriteSheet(mFrames);
	}

	private BitmapDrawable[] mFrames;
}
