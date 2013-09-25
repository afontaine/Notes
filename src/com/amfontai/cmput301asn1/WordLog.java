package com.amfontai.cmput301asn1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

public class WordLog extends Activity {

	NotesDb mDb = new NotesDb(NotesDb.READ, this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_log);
		// Show the Up button in the action bar.
		setupActionBar();
		
		TextView characters = (TextView) findViewById(R.id.characters);
		TextView words = (TextView) findViewById(R.id.words);
		TextView logs = (TextView) findViewById(R.id.logs);
		TextView topHundred = (TextView) findViewById(R.id.top);
		
		characters.setText(characters.getText() + " " + Long.toString(mDb.totalCharacters()));
		words.setText(words.getText() + " " + Long.toString(mDb.totalWords()));
		logs.setText(logs.getText() + " " + Long.toString(mDb.totalEntries()));
		for(String word : mDb.topHundred())
			topHundred.setText(topHundred.getText() + " " + word);
	}

	/**
	 * Set up the {@link android.app.ActionBar}.
	 */
	private void setupActionBar() {

		getActionBar().setDisplayHomeAsUpEnabled(true);

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
