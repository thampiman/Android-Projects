/**
 * Author: Ajay Thampi
 * Date Created: 10 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class FlashCards extends Activity implements OnClickListener {
	private static final String TAG = "FlashCards";
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Typeface font = Typeface.createFromAsset(getAssets(), "Timeless.ttf");
        
        TextView mainTextView = (TextView) findViewById(R.id.main_text_view);    
        mainTextView.setTypeface(font);  

        // Setting up click listeners for all the buttons
        View myCardsButton = findViewById(R.id.my_cards_button);
        myCardsButton.setOnClickListener(this);
        View settingsButton = findViewById(R.id.settings_button);
        settingsButton.setOnClickListener(this);
    }
    
    public void onClick(View v) {
    	switch (v.getId()) {
	    	case R.id.my_cards_button:
	    		Log.d(TAG, "My Cards Button Tapped");
		    	startActivity(new Intent(this, MyCards.class));
		    	break;
	    	case R.id.settings_button:
	    		Log.d(TAG, "Settings Button Tapped");
	    		break;
	    	default:
	    		Log.d(TAG, "Unrecognized Button");
	    		break;
    	}
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.opening_screen_one, menu);
	    return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
		    case R.id.about:
			    startActivity(new Intent(this, About.class));
			    return true;
	    }
	    return false;
    }
}
