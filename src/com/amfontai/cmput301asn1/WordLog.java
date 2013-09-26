/*
 *  This is a part of amfontai Notes
 *  Copyright (C) 2013 Andrew Fontaine
 *
 *  amfontai Notes program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  amfontai Notes program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package com.amfontai.cmput301asn1;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v4.app.NavUtils;

/**
 * This is the WordLog activity, which shows
 * all statistics relating to the notes in
 * the database. It contains 3 text views,
 * defined in activity_word_cloud.xml. The
 * actual counts, however, are generated
 * when the activity is created
 */
public class WordLog extends Activity {

	/**
	 * mDb is an instance of (@link NotesDb}, which controls
	 * access to the {@link android.database.SQLiteDatabase}.
	 */
	private NotesDb mDb = new NotesDb(NotesDb.READ, this);
	
	/**
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_word_log);
		// Show the Up button in the action bar.
		setupActionBar();
		// Here we get the views from the layout file
		TextView characters = (TextView) findViewById(R.id.characters);
		TextView words = (TextView) findViewById(R.id.words);
		TextView logs = (TextView) findViewById(R.id.logs);
		TextView topHundred = (TextView) findViewById(R.id.top);
		/* Here we query the DB for the statistics and 
		 * add them to the displayed string
		 */
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

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.word_log, menu);
		return true;
	}

	/**
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
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
