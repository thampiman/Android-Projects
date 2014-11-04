/**
 * Author: Ajay Thampi
 * Date Created: 30 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ViewFlipper;

/**
 * Workaround solution for crash in ViewFlipper when orientation is changed in quick succession.
 * Based on thread: http://code.google.com/p/android/issues/detail?id=6191
 */
public class MyViewFlipper extends ViewFlipper {
	public MyViewFlipper(Context context) {
        super(context);
    }

    public MyViewFlipper(Context context, AttributeSet attrs) {
    	super(context, attrs);
    }
    
    @Override
    protected void onDetachedFromWindow() {
    	try{
    		super.onDetachedFromWindow();
    	}catch(Exception e) {
    	}
    }
}