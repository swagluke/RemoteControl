package com.example.sparkgesture;

import java.util.Calendar;
import java.util.HashMap;

import android.app.Activity;
import android.content.Intent;
import android.gesture.GestureLibrary;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.canvas.Canvas1;
import com.canvas.R;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class MainActivity extends Activity implements
		GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener,
		OnItemSelectedListener, OnClickListener {

	public static int MOVE_LEFT = 0;
	public static int MOVE_UP = 1;
	public static int MOVE_DOWN = 2;
	public static int MOVE_RIGHT = 3;

	private static final int SWIPE_MIN_DISTANCE = 150;

	private static final int SWIPE_MAX_OFF_PATH = 100;

	private static final int SWIPE_THRESHOLD_VELOCITY = 100;
	private static final String DEBUG_TAG = "Gestures";
	private GestureDetectorCompat mDetector;
	private TextView instru;
	private HashMap<Integer, String> mIL = new HashMap<Integer, String>();

	private GestureLibrary gestureLib;
	public static Firebase ref;

	// Write data to Firebase

	// Called when the activity is first created.
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// Instantiate the gesture detector with the
		// application context and an implementation of
		// GestureDetector.OnGestureListener
		mDetector = new GestureDetectorCompat(this, this);
		// Set the gesture detector as the double tap
		// listener.
		mDetector.setOnDoubleTapListener(this);
		Spinner spinner = (Spinner) findViewById(R.id.spinner);
		// Create an ArrayAdapter using the string array and a default spinner
		// layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
				this, R.array.gestures, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		spinner.setOnItemSelectedListener(this);
		instru = (TextView) findViewById(R.id.textView1);
		mIL.put(6, "Single Tap Screen");
		mIL.put(0, "Double Tap Screen");
		mIL.put(1, "Long Press");
		mIL.put(2, "Swipe Left");
		mIL.put(3, "Swipe Right");
		mIL.put(4, "Swipe Up");
		mIL.put(5, "Swipe Down");

		// Create a reference to a Firebase location
		 ref = new Firebase("https://mousemove.firebaseio.com/");
		// Read data and react to changes
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

		// button listener for MouseActivity button
		Button mClickMouseActivity = (Button) findViewById(R.id.mouseActivityId);
		mClickMouseActivity.setOnClickListener(this);
		Button mHandWritting = (Button) findViewById(R.id.handwritting);
		mHandWritting.setOnClickListener(this);
		Button mJoyStcik = (Button) findViewById(R.id.joystick);
		mJoyStcik.setOnClickListener(this);

	}
	
	private long getTime() {
    	Calendar rightNow = Calendar.getInstance();

    	// offset to add since we're not UTC
    	long offset = rightNow.get(Calendar.ZONE_OFFSET) +
    	    rightNow.get(Calendar.DST_OFFSET);
    	long sinceMidnight = (rightNow.getTimeInMillis() + offset) %
    	    (24 * 60 * 60 * 1000);
    	return sinceMidnight;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mouseActivityId: {
			// start new intent -> go to mouse activity page
			Intent intent = new Intent(this, MouseActivity.class);
			startActivity(intent);
			break;
		}
		case R.id.handwritting: {
			// start new intent -> go to mouse activity page
			Intent intent = new Intent(this, Canvas1.class);
			startActivity(intent);
			break;
		}
		case R.id.joystick: {
			// start new intent -> go to mouse activity page
			Intent intent = new Intent(this, Joystick.class);
			startActivity(intent);
			break;
		}

		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		this.mDetector.onTouchEvent(event);
		// Be sure to call the superclass implementation

	    // tell the system that we handled the event and no further processing is required
	    return super.onTouchEvent(event);
	}

	@Override
	public boolean onDown(MotionEvent event) {
		// .d(DEBUG_TAG, "onDown: " + event.toString());
		return true;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// .d(DEBUG_TAG, "onFling: " + e1.toString() + e2.toString());
		float dX = e2.getX() - e1.getX();

		float dY = e1.getY() - e2.getY();

		if (Math.abs(dY) < SWIPE_MAX_OFF_PATH &&

		Math.abs(velocityX) >= SWIPE_THRESHOLD_VELOCITY &&

		Math.abs(dX) >= SWIPE_MIN_DISTANCE) {

			if (dX > 0) {
				ref.setValue("C" + 2 + "|" + getTime());
				Toast.makeText(getApplicationContext(), "Right Swipe",
						Toast.LENGTH_SHORT).show();

			} else {
				ref.setValue("C" + 3 + "|" + getTime());
				Toast.makeText(getApplicationContext(), "Left Swipe",
						Toast.LENGTH_SHORT).show();

			}

			return true;

		} else if (Math.abs(dX) < SWIPE_MAX_OFF_PATH &&

		Math.abs(velocityY) >= SWIPE_THRESHOLD_VELOCITY &&

		Math.abs(dY) >= SWIPE_MIN_DISTANCE) {

			if (dY > 0) {
				ref.setValue("C" + 4 + "|" + getTime());

				Toast.makeText(getApplicationContext(), "Up Swipe",
						Toast.LENGTH_SHORT).show();

			} else {
				ref.setValue("C" + 5 + "|" + getTime());
				Toast.makeText(getApplicationContext(), "Down Swipe",
						Toast.LENGTH_SHORT).show();

			}

			return true;

		}

		return false;

	}

	@Override
	public void onLongPress(MotionEvent event) {
		ref.setValue("C" + 1 + " " + "|" + getTime());
		Toast.makeText(getApplicationContext(), "Long Press",
				Toast.LENGTH_SHORT).show();
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// .d(DEBUG_TAG, "onScroll: " + e1.toString() + e2.toString());
		return true;
	}

	@Override
	public void onShowPress(MotionEvent event) {
		// .d(DEBUG_TAG, "onShowPress: " + event.toString());
	}

	@Override
	public boolean onSingleTapUp(MotionEvent event) {
		ref.setValue("C" + 6 + "|" + getTime());
		Toast.makeText(getApplicationContext(), "Single Tap",
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onDoubleTap(MotionEvent event) {
		ref.setValue("C" + 0 + "|" + getTime());
		Toast.makeText(getApplicationContext(), "Double Tap",
				Toast.LENGTH_SHORT).show();
		return true;
	}

	@Override
	public boolean onDoubleTapEvent(MotionEvent event) {
		// .d(DEBUG_TAG, "onDoubleTapEvent: " + event.toString());
		return true;
	}

	@Override
	public boolean onSingleTapConfirmed(MotionEvent event) {
		// .d(DEBUG_TAG, "onSingleTapConfirmed: " + event.toString());
		return true;
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String in = mIL.get(position);
		instru.setText(in);

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}

}