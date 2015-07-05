package com.example.sparkgesture;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.Toast;

import com.canvas.R;
import com.firebase.client.Firebase;


public class MouseActivity extends Activity  {

	private Firebase ref;
	// Write data to Firebase

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mouse);
		
		ref = com.example.sparkgesture.MainActivity.ref;
	}
	
	public boolean onTouchEvent(MotionEvent event) {
	    int eventaction = event.getAction();

	    switch (eventaction) {
	        case MotionEvent.ACTION_DOWN: 
	            // finger touches the screen
	            break;

	        case MotionEvent.ACTION_MOVE:
	        	Calendar rightNow = Calendar.getInstance();

	        	// offset to add since we're not UTC
	        	long offset = rightNow.get(Calendar.ZONE_OFFSET) +
	        	    rightNow.get(Calendar.DST_OFFSET);
	        	long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
	        	    (24 * 60 * 60 * 1000);
	        	
	            // finger moves on the screen
				DisplayMetrics displaymetrics = new DisplayMetrics();
				getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
				int height = displaymetrics.heightPixels;
				int width = displaymetrics.widthPixels;
				//String format = s.format(new Date());
				String hash = createHashString("A", 0, event.getRawX(), event.getRawY(), width, height, "Z", sinceMidnight);
				ref.setValue(hash);
	            break;

	        case MotionEvent.ACTION_UP:   
	            // finger leaves the screen
	            break;
	    }

	    // tell the system that we handled the event and no further processing is required
	    return true; 
	}
	
	// temporary method, should be public and made accessible by main AND mouse activities
	private String createHashString(String method, int action, float mousePosX, float mousePosY, int sizeX, int sizeY, String keyboard, long time) {
		return "" + method + "|" + action + "|" + mousePosX + "|" + mousePosY + "|" + sizeX + "|" + sizeY + "|" + keyboard + "|" + time;
	}
}