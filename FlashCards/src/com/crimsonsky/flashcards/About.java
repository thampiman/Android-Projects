/**
 * Author: Ajay Thampi
 * Date Created: 10 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import com.crimsonsky.flashcards.R;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.text.method.LinkMovementMethod;

public class About extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.about);
		
		// Code that lets you click on an <a href> link on a TextView
		TextView aboutContentView = (TextView) findViewById(R.id.about_content);
		aboutContentView.setMovementMethod(LinkMovementMethod.getInstance());
	}
}
