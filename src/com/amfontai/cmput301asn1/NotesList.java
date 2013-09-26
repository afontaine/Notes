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

public class NotesList extends ListActivity {
	
	private NotesDb mDb = new NotesDb(NotesDb.READ, this);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_notes_list);
	}
	
	private void listNotes() {		
		setListAdapter(mDb.listNotes(this));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.notes_list, menu);
		return true;
	}
	
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
	
	@Override
	protected void onResume() {
		super.onResume();
		listNotes();
		
	}
	
	private void openNew() {
		
		Intent intent = new Intent(this, NoteDetail.class);
		startActivity(intent);
		
	}

	private void openWordCloud() {
		Intent intent = new Intent(this, WordCloud.class);
		startActivity(intent);
		
	}

	private void openLog() {
		Intent intent = new Intent(this, WordLog.class);
		startActivity(intent);
		
	}

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		Intent intent = new Intent(this, NoteDetail.class);
		Cursor cursor = (Cursor) getListView().getItemAtPosition(position);
		intent.putExtra("com.amfontai.cmput301asn1.id", cursor.getInt(cursor.getColumnIndex(NotesDb._ID)));
		startActivity(intent);
	}

}
