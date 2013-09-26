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

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

/**
 * The Class NotesList represents an {@link android.app.ListActivity}
 * that lists all the Notes that are saved in the DB.
 * The Notes are listed in order of most recent date
 * (but the order is set by the {@link NotesDb} class).
 * The {@link android.widget.ListAdapter} is a {@link android.widget.SimpleCursorAdapter}
 * returned to the activity by the DB. The list view is
 * defined in activity_notes_list.xml
 */
public class NotesList extends ListActivity {
	
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
		setContentView(R.layout.activity_notes_list);
	}
	
	/**
	 * List notes by setting the {@link android.widget.ListAdapter}
	 * to a {@link android.widget.SimpleCursorAdapter}, returned by the {@link NotesDb}.
	 */
	private void listNotes() {		
		setListAdapter(mDb.listNotes(this));
	}

	/**
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_list, menu);
		return true;
	}
	
	/**
	 * Checks to see which menu item on the {@link android.view.Menu}
	 * was clicked and opens the appropriate window.
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem menu) {
		switch(menu.getItemId()) {
		case R.id.action_add:
			openNew();
			return true;
		case R.id.action_cloud:
			openWordCloud();
			return true;
		case R.id.action_log:
			openLog();
			return true;
		default:
			return super.onOptionsItemSelected(menu);
		}
				
	}
	
	/**
	 * @see android.app.Activity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		listNotes();
		
	}
	
	/**
	 * Opens an instance of {@link NoteDetail} without passing
	 * an ID through the intent. This starts the {@link NoteDetail} window
	 * with the default information, equivalent to making a new note.
	 */
	private void openNew() {
		
		Intent intent = new Intent(this, NoteDetail.class);
		startActivity(intent);
		
	}

	/**
	 * Opens an instance of {@link WordCloud}
	 */
	private void openWordCloud() {
		Intent intent = new Intent(this, WordCloud.class);
		startActivity(intent);
		
	}

	/**
	 * Opens an instance of {@link WordLog}.
	 */
	private void openLog() {
		Intent intent = new Intent(this, WordLog.class);
		startActivity(intent);
		
	}

	/**
	 * Opens an instance of {@link NoteDetail} and passes
	 * the {@link android.database.SQLiteDatabase} ID of the
	 * {@link Note} selected. The new window will query the
	 * database to fill in the form with the saved information.
	 * This is equivalent to editing a Note.
	 * 
	 * @see android.app.ListActivity#onListItemClick(android.widget.ListView, android.view.View, int, long)
	 */
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, NoteDetail.class);
		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
		intent.putExtra("com.amfontai.cmput301asn1.id", cursor.getInt(cursor.getColumnIndex(NotesDb._ID)));
		startActivity(intent);
	}

}
