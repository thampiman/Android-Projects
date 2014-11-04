/**
 * Author: Ajay Thampi
 * Date Created: 12 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import java.util.HashMap;

public class Card extends HashMap <String, String> {
	public static String KEY_ID = "id";
	public static String KEY_TITLE = "title";
	public static String KEY_NUM_CARDS = "numCards";
	private int nId;
	private String sTitle;
	private String sNumCards;
	
	public Card(int nId, String sTitle, String sNumCards)
	{
		this.nId = nId;
		this.sTitle = sTitle;
		this.sNumCards = sNumCards;
	}
	
	@Override
	public String get(Object k) {
		String key = (String)k;
		if (KEY_ID.equals(key))
			return Integer.toString(nId);
		if (KEY_TITLE.equals(key))
			return sTitle;
		if (KEY_NUM_CARDS.equals(key)) 
			return sNumCards;
		return null;
	}
}
