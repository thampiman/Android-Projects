/**
 * Author: Ajay Thampi
 * Date Created: 12 Jan 2011
 * Copyright (c) 2011 Thampiman Productions
 */
package com.crimsonsky.flashcards;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuInflater;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.content.ContentValues;
import android.database.Cursor;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.content.Intent;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import com.crimsonsky.flashcards.CardsData;

import static android.provider.BaseColumns._ID;
import static com.crimsonsky.flashcards.Constants.*;
import static com.crimsonsky.flashcards.Card.*;

@SuppressWarnings("unused")
public class MyCards extends ListActivity {
	private static final String TAG = "FlashCards";
	private static String[] FROM_META_INFO = { _ID, CARDS_TITLE, NUM_CARDS, };
	private static String META_INFO_ORDER_BY = DATE_MODIFIED + " DESC" ;
	private static Card selectedCard;
	private static List<Card> gCards;
	private static long gListId;
	
	private CardsData cardsData;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mycards);
		
		ProgressDialog dialog = ProgressDialog.show(this, "", 
                "Loading. Please wait...", true);
		
		// Register all items in the List View for the Context Menu
		registerForContextMenu(getListView());
		
		// Create data helper
		cardsData = new CardsData(this);
		
		// Get Cards
		Cursor cursor = getCards();
		if (cursor.getCount() == 0) // If no cards, create default set (temporary)
		{
			Log.d(TAG, "Adding cards!");
			addCards();
			cursor = getCards();
		}
		dialog.cancel();
		
		// Create list adapter
		List<Card> cards = getData(cursor);
		ListAdapter adapter = new CardsListAdapter(this, cards, android.R.layout.simple_list_item_2,
												   new String [] {Card.KEY_TITLE, Card.KEY_NUM_CARDS},
												   new int [] {android.R.id.text1, android.R.id.text2});
		this.setListAdapter(adapter);
	}
	
	private void addCards()
	{
		addCards("Barron's GRE Vocab 01", "5 cards");
		addCards("Barron's GRE Vocab 02", "5 cards");
		addCards("Barron's GRE Vocab 03", "5 cards");
	}
	
	private void addCards(String cardsTitle, String numCards) {
		SQLiteDatabase db = cardsData.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(CARDS_TITLE, cardsTitle);
		values.put(NUM_CARDS, numCards);
		values.put(DATE_MODIFIED, System.currentTimeMillis());
		long id = db.insertOrThrow(CARDS_META_INFO_TABLE_NAME, null, values);
		if (id > 0)
		{
			String sId = CARDS_TABLE + Long.toString(id);
			
			// Create table to store cards
			db.execSQL("CREATE TABLE " + sId + " (" + _ID
					+ " INTEGER PRIMARY KEY AUTOINCREMENT, " + QUESTION
					+ " TEXT NOT NULL, " + HINT
					+ " TEXT, " + ANSWER 
					+ " TEXT NOT NULL);");
			db.close();
			
			// Add individual cards
			addCards(sId, "Baleful (adjective)", "Context clue: \"a baleful glance\"", 
					 "Deadly; having a malign influence; ominous<p>Context sentence: a baleful glance</p>");
			addCards(sId, "Two synonyms for baleful", "Think cancer tumour that is harmful; also think something really bad that is about to happen", 
			 		 "Malignant, Ominous");
			addCards(sId, "Antipathy (noun)", "Context clue: \"Natural antipathy of cats and dogs\"", 
			 		 "Habitual aversion; intense dislike; natural repugnance; hatred<p>Anti means against; path means feeling</p>");
			addCards(sId, "Analogy: <p>SOLICITOUS : CONCERN :: HOSTILE : __________</p>", "Think of strong dislike or hatred",
					 "ANTIPATHY<p>Somone solicitous or caring demonstrates concern</p>");
			addCards(sId, "Macabre (adjective)", "Context clue: \"a macabre ending\"",
					 "gruesome and horrifying, ghastly; representing death<p>Pronounciation: muh-kah-bruh</p>");
		}
	}
	
	private void addCards(String sId, String question, String hint, String answer) {
		SQLiteDatabase db = cardsData.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(QUESTION, question);
		values.put(HINT, hint);
		values.put(ANSWER, answer);
		db.insertOrThrow(sId, null, values);
		db.close();
	}
	
	private Cursor getCards() {
		SQLiteDatabase db = cardsData.getReadableDatabase();
		Cursor cursor = db.query(CARDS_META_INFO_TABLE_NAME, FROM_META_INFO, null, null, null,
				null, META_INFO_ORDER_BY);
		startManagingCursor(cursor);
		return cursor;
	}
	
	private List<Card> getData(Cursor cursor) {
		List<Card> cards = new ArrayList<Card>();
		while (cursor.moveToNext()) {
			cards.add(new Card(cursor.getInt(0), cursor.getString(1), cursor.getString(2)));
		}
		return cards;
	}
	
	@Override
	protected void onListItemClick(ListView listView, View view, int position, long id) {
		CardsListAdapter cardsListAdapter = (CardsListAdapter)this.getListAdapter();
		List <Card> cards = cardsListAdapter.getCards();
		Card card = cards.get((int)id);
		Intent viewCardsIntent = new Intent(this, ViewCards.class);
		Bundle bundle = new Bundle();
		bundle.putString(KEY_ID, card.get(KEY_ID));
		bundle.putString(KEY_TITLE, card.get(KEY_TITLE));
		viewCardsIntent.putExtras(bundle);
		startActivity(viewCardsIntent);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
	    super.onCreateOptionsMenu(menu);
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.cards_menu_one, menu);
	    return true;
    }
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_cards_context_menu, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) {
			case R.id.my_cards_delete:
				deleteCards(info.id);
				return true;
			case R.id.my_cards_delete_all:
				deleteAll();
				return true;
			default:
				return super.onContextItemSelected(item);
		}
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu)
	{
	    super.onPrepareOptionsMenu(menu);

	    // Disable 'My Cards' menu item when on My Cards view
	    MenuItem item = menu.findItem(R.id.my_cards_menu);
	    item.setVisible(false);

	    return true;
	}
	
	private void deleteCards(long listId) {
		CardsListAdapter cardsListAdapter = (CardsListAdapter)this.getListAdapter();
		gCards = cardsListAdapter.getCards();
		selectedCard = gCards.get((int)listId);
		gListId = listId;
		String sAlert = "Are you sure you want to delete all cards from the \"" + 
							selectedCard.get(KEY_TITLE) + "\" set?";
		
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(sAlert)
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // Get context
		        	   Context context = getApplicationContext();
		        	   
		        	   // Delete individual cards
		        	   SQLiteDatabase db = cardsData.getWritableDatabase();
		        	   db.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE + selectedCard.get(KEY_ID));
		        	   
		        	   // Delete entry in meta_info table
		        	   db.execSQL("DELETE FROM " + CARDS_META_INFO_TABLE_NAME +
		        			   	  " WHERE " + _ID + "=" + selectedCard.get(KEY_ID));
		        	   db.close();
		        	   
		        	   // Update ListAdapter
		        	   gCards.remove((int)gListId);
		        	   ListAdapter adapter = new CardsListAdapter(context, gCards, android.R.layout.simple_list_item_2,
			   					   new String [] {Card.KEY_TITLE, Card.KEY_NUM_CARDS},
			   					   new int [] {android.R.id.text1, android.R.id.text2});
		        	   setListAdapter(adapter);
		        	   getListView().invalidate();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
	
	private void deleteAll() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage("Are you sure you want to delete all sets?")
		       .setCancelable(false)
		       .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		        	   // Get context
		        	   Context context = getApplicationContext();
		        	   
		        	   // Iterate through all cards and delete one by one
		        	   CardsListAdapter cardsListAdapter = (CardsListAdapter)getListAdapter();
		       		   List <Card> cards = cardsListAdapter.getCards();
		       		   Iterator<Card> iter = cards.iterator();
		       		   
		       		   while (iter.hasNext()) {
		       			   Card card = iter.next();
		       			   // Delete individual cards
		       			   SQLiteDatabase db = cardsData.getWritableDatabase();
			        	   db.execSQL("DROP TABLE IF EXISTS " + CARDS_TABLE + card.get(KEY_ID));
			        	   
			        	   // Delete entry in meta_info table
			        	   db.execSQL("DELETE FROM " + CARDS_META_INFO_TABLE_NAME +
			        			   	  " WHERE " + _ID + "=" + card.get(KEY_ID));
			        	   db.close();
		       		   }
		        	   
		        	   // Update ListAdapter
		       		   cards.clear();
		        	   ListAdapter adapter = new CardsListAdapter(context, cards, android.R.layout.simple_list_item_2,
			   					   new String [] {Card.KEY_TITLE, Card.KEY_NUM_CARDS},
			   					   new int [] {android.R.id.text1, android.R.id.text2});
		        	   setListAdapter(adapter);
		        	   getListView().invalidate();
		           }
		       })
		       .setNegativeButton("No", new DialogInterface.OnClickListener() {
		           public void onClick(DialogInterface dialog, int id) {
		                dialog.cancel();
		           }
		       });
		AlertDialog alert = builder.create();
		alert.show();
	}
}
