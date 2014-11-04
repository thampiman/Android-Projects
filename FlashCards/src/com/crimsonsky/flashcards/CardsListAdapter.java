/**
 * Author: Ajay Thampi
 * Date Created: 12 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import android.widget.SimpleAdapter;
import android.content.Context;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class CardsListAdapter extends SimpleAdapter {
	private List<Card> cards;
	
	public CardsListAdapter(Context context, List<? extends Map<String, String>> cards,
							int resource, String [] from, int [] to) {
		super(context, cards, resource, from, to);
		this.cards = (List<Card>)cards;
	}
	
	public List<Card> getCards() {
		return cards;
	}
}
