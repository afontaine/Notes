package com.amfontai.cmput301asn1;


import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import com.amfontai.cmput301asn1.NotesDb.NotesDbHelper;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class WordLog extends Activity {

	NotesDbHelper mDb = new NotesDb().new NotesDbHelper(this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_log);
		// Show the Up button in the action bar.
		setupActionBar();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		TextView characters = (TextView) findViewById(R.id.characters);
		TextView words = (TextView) findViewById(R.id.words);
		TextView logs = (TextView) findViewById(R.id.logs);
		//TextView topHundred = (TextView) findViewById(R.id.top);
		
		characters.setText(characters.getText() + Long.toString(totalCharacters()));
		words.setText(words.getText() + Long.toString(totalWords()));
		logs.setText(logs.getText() + Long.toString(totalEntries()));
		
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

	}
	
	private long totalEntries() {
		return DatabaseUtils.queryNumEntries(mDb.getReadableDatabase(), NotesDb.TABLE_NAME);
	}
	
	private long totalCharacters() {
		Cursor all = mDb.getReadableDatabase().query(NotesDb.TABLE_NAME, 
				new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_CONTENT},
				null,null, null, null, null);
		String results = "";
		all.moveToFirst();
		do {
			results += all.getString(all.getColumnIndex(NotesDb.COLUMN_SUBJECT))
					+ all.getString(all.getColumnIndex(NotesDb.COLUMN_CONTENT));
		} while(all.moveToNext());
		
		all.close();
		
		return results.length();
	}
	
	private String[] getAllWords() {
		Cursor all = mDb.getReadableDatabase().query(NotesDb.TABLE_NAME, 
				new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_CONTENT},
				null,null, null, null, null);
		String results = "";
		all.moveToFirst();
		
		do {
			results += " " + all.getString(all.getColumnIndex(NotesDb.COLUMN_SUBJECT))
					+ " " + all.getString(all.getColumnIndex(NotesDb.COLUMN_CONTENT));
		} while(all.moveToNext());
		
		all.close();
		
		results = results.trim();
		String[] words = results.split("\\W+");
		return words;
	}
	
	private long totalWords() {
		return getAllWords().length;
	}
	
	@SuppressLint("DefaultLocale")
	private ArrayList<String> topHundred() {
		String[] words = getAllWords();
		
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		
		for(String word : words) {
			if(wordMap.containsKey(word))
				wordMap.put(word.toLowerCase(), wordMap.get(word) + 1);
			else
				wordMap.put(word.toLowerCase(), 1);
		}
		
		return recursiveSort(wordMap, new ArrayList<String>());
	}
	
	private ArrayList<String> recursiveSort(HashMap<String, Integer> wordMap, ArrayList<String> results) {
		if(results.size() > 99) 
			return results;
		String top = "";
		int highest = 0;
		ArrayList<String> keys = new ArrayList<String>(wordMap.keySet());
		
		for(String word : keys) {
			if(wordMap.get(word).intValue() > highest) {
				highest = wordMap.get(word).intValue();
				top = word;
			}
		}
		results.add(top);
		
		wordMap.remove(top);
		
		return recursiveSort(wordMap, results);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_log, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// This ID represents the Home or Up button. In the case of this
			// activity, the Up button is shown. Use NavUtils to allow users
			// to navigate up one level in the application structure. For
			// more details, see the Navigation pattern on Android Design:
			//
			// http://developer.android.com/design/patterns/navigation.html#up-vs-back
			//
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

}
