package com.example.robogame;

import com.example.myfirstapp.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = 
			"com.example.myfirstapp.MESSAGE";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is 
//    	// present.
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }
    
    /** Called when the user clicks the Start Game button */
    public void  startGame(View view) {
    	Intent intent = new Intent(this, GameActivity.class);
    	startActivity(intent);
    }
    
}
