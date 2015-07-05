package com.example.sparkgesture;

import java.util.Calendar;

import com.MobileAnarchy.Android.Widgets.Joystick.JoystickMovedListener;
import com.MobileAnarchy.Android.Widgets.Joystick.JoystickView;
import com.canvas.R;
import com.canvas.R.id;
import com.canvas.R.layout;
import com.canvas.R.menu;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

public class Joystick extends Activity {
	private JoystickView mJs;
	private Firebase ref = new Firebase("https://mousemove.firebaseio.com/");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_joystick);

		ref.addValueEventListener(new ValueEventListener() {

			@Override
			public void onDataChange(DataSnapshot snap) {
				// System.out.println(snap.getName() + " -> " +
				// snap.getValue());
			}

			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub

			}
		});

		mJs = (com.MobileAnarchy.Android.Widgets.Joystick.JoystickView) findViewById(R.id.dualjoystickView);
		mJs.setOnJostickMovedListener(_listenerLeft);

		if (savedInstanceState == null) {
			getFragmentManager().beginTransaction()
					.add(R.id.container, new PlaceholderFragment()).commit();
		}

	}

	private JoystickMovedListener _listenerLeft = new JoystickMovedListener() {

		private boolean mCenterL;

		public void OnMoved(int pan, int tilt) {
			double mRadiusL = Math.sqrt((pan * pan) + (tilt * tilt));
			// mAngleL = Math.atan2(pan, tilt);
			double mAngleL = Math.atan2(-pan, -tilt);
			// System.out.println(String.format("( r%.0f, %.0f\u00B0 )",
			// Math.min(mRadiusL, 10), mAngleL * 180 / Math.PI));
			int angle = (int) (mAngleL * 180 / Math.PI);

			Calendar rightNow = Calendar.getInstance();
			long offset = rightNow.get(Calendar.ZONE_OFFSET)
					+ rightNow.get(Calendar.DST_OFFSET);
			long sinceMidnight = (rightNow.getTimeInMillis() + offset)
					% (24 * 60 * 60 * 1000);

			if (angle == 0) {

				ref.setValue("C" + 4 + "|" + sinceMidnight);
			}
			if (-90 < angle && angle < 0) {
				// up
				ref.setValue("C" + 4 + "|" + sinceMidnight);
				// right
				ref.setValue("C" + 2 + "|" + sinceMidnight);
			}
			if (angle == -90) {
				ref.setValue("C" + 2 + "|" + sinceMidnight);
			}
			if (-179 < angle && angle < -90) {
				// up
				ref.setValue("C" + 5 + "|" + sinceMidnight);
				// right
				ref.setValue("C" + 2 + "|" + sinceMidnight);
			}
			
			if(angle==180){
				//down
				ref.setValue("C" + 5 + "|" + sinceMidnight);
			}
			
			if (90<angle && angle<180){
				//left 
				ref.setValue("C" + 3 + "|" + sinceMidnight);
				//down
				ref.setValue("C" + 5 + "|" + sinceMidnight);
				
			}
			
			if (angle == 90) {
				ref.setValue("C" + 3 + "|" + sinceMidnight);
			}
			
			
			if (0 < angle && angle < 90) {
				// up
				ref.setValue("C" + 4 + "|" + sinceMidnight);
				// left
				ref.setValue("C" + 3 + "|" + sinceMidnight);
			}
			
			mCenterL = false;

		}

		public void OnReleased() {
			//
		}

		public void OnReturnedToCenter() {
			// mRadiusL = mAngleL = 0;
			// UpdateMethod();
			// mCenterL = true;
			System.out.println("center");
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.joystick, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_joystick,
					container, false);
			return rootView;
		}
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
