/**
 * Author: Ajay Thampi
 * Date Created: 29 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import android.provider.BaseColumns;

public interface Constants extends BaseColumns {
	public static final String CARDS_META_INFO_TABLE_NAME = "cards_meta_info";
	
	// Columns in the cards_meta_info table
	public static final String CARDS_TITLE = "cards_title";
	public static final String NUM_CARDS = "num_cards";
	public static final String DATE_MODIFIED = "date_modified";
	
	// Columns in each cards table
	public static final String CARDS_TABLE = "Cards_";
	public static final String QUESTION = "question";
	public static final String HINT = "hint";
	public static final String ANSWER = "answer";
}
