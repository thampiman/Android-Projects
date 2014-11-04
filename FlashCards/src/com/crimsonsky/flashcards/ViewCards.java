/**
 * Author: Ajay Thampi
 * Date Created: 30 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.graphics.Typeface;
import android.view.ViewGroup;
import android.view.MotionEvent;
import android.view.Gravity;
import android.util.Log;
import android.content.res.Configuration;
import android.text.Html;
import android.view.animation.AnimationUtils;
import android.view.View;
import static com.crimsonsky.flashcards.Card.*;
import static com.crimsonsky.flashcards.Constants.*;

@SuppressWarnings("unused")
public class ViewCards extends Activity {
	private static final String TAG = "FlashCards"; 
	private static String[] FROM_CARDS = { QUESTION, HINT, ANSWER, };
	private static String CARDS_ORDER_BY = "Random()";
	private final int THRESHOLD = 40;
	
	private MyViewFlipper flipper; 
	private float oldTouchValue;
	
	private CardsData cardsData;
	private String cardSetTitle;
	private String cardSetId;
	 
	@Override 
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
	    setContentView(R.layout.viewcards); 
	     
	    Bundle bundle = this.getIntent().getExtras();
	    cardSetId = bundle.getString(KEY_ID);
	    cardSetTitle = bundle.getString(KEY_TITLE);
	    
	    Typeface font = Typeface.createFromAsset(getAssets(), "Timeless.ttf");
	    flipper = (MyViewFlipper)findViewById(R.id.details);
	    
	    // Create data helper
		cardsData = new CardsData(this);
		
	    // Load cards from database
	    SQLiteDatabase db = cardsData.getReadableDatabase();
	    String cardsTable = CARDS_TABLE + cardSetId;
		Cursor cursor = db.query(cardsTable, FROM_CARDS, null, null, null,
								 null, CARDS_ORDER_BY);
		startManagingCursor(cursor);
		
		int count = 1;
		int viewId = 0;
		
		while (cursor.moveToNext()) {
			TextView qnView = new TextView(this);
			qnView.setText(Html.fromHtml("<p><u>Question " + Integer.toString(count) + "</u>:</br></p>" + cursor.getString(0))); 
			qnView.setTypeface(font);  
			qnView.setBackgroundColor(getResources().getColor(R.color.question));
			qnView.setTextColor(getResources().getColor(R.color.question_text));
			qnView.setGravity(Gravity.CENTER);
			qnView.setTextSize(20);
			qnView.setId(viewId);
	        flipper.addView(qnView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
	                                                         ViewGroup.LayoutParams.FILL_PARENT));
	        viewId++;
	        
	        TextView ansView = new TextView(this);
	        ansView.setText(Html.fromHtml("<p><u>Answer " + Integer.toString(count) + "</u>:</br></p>" + cursor.getString(2))); 
	        ansView.setTypeface(font);  
	        ansView.setBackgroundColor(getResources().getColor(R.color.answer));
	        ansView.setTextColor(getResources().getColor(R.color.answer_text));
	        ansView.setGravity(Gravity.CENTER);
	        ansView.setTextSize(20);
	        ansView.setId(viewId);
	        flipper.addView(ansView, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT, 
                    										ViewGroup.LayoutParams.FILL_PARENT));
	        viewId++;
	        
	        count++;
		}
		db.close();
		
		this.setTitle(cardSetTitle);
	} 
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
	  super.onConfigurationChanged(newConfig);
	}
	 
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				oldTouchValue = event.getX();
		 		break;
		 	case MotionEvent.ACTION_UP:
		 		float currentX = event.getX();
		 		
                if (currentX >= (getWindowManager().getDefaultDisplay().getWidth() - THRESHOLD)) {
                	showNextCard();
                } else if (currentX <= THRESHOLD) {
                	showPreviousCard();
                } else if ((oldTouchValue > currentX) && (oldTouchValue - currentX > THRESHOLD)) {
                	showNextCard();
                } else if ((oldTouchValue < currentX) && (currentX - oldTouchValue > THRESHOLD)) {
                	showPreviousCard();
                }
                break;
		 	default:
                	break;
		}
		return true;
	}
	
	private void showNextCard() {
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
    	flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
        flipper.showNext();
	}
	
	private void showPreviousCard() {
		flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
    	flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
    	flipper.showPrevious();
	}
} 