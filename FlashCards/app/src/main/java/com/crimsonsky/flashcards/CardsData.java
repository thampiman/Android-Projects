/**
 * Author: Ajay Thampi
 * Date Created: 29 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import static android.provider.BaseColumns._ID;
import static com.crimsonsky.flashcards.Constants.*;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CardsData extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "cards.db";
	private static final int DATABASE_VERSION = 1;
	
	/** Create a helper object for the Cards database **/
	public CardsData(Context ctx) {
		super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + CARDS_META_INFO_TABLE_NAME);
		onCreate(db);
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE " + CARDS_META_INFO_TABLE_NAME + " (" + _ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + CARDS_TITLE
					+ " TEXT NOT NULL, " + NUM_CARDS
					+ " TEXT NOT NULL, " + DATE_MODIFIED 
					+ " INTEGER);");
	}
}
