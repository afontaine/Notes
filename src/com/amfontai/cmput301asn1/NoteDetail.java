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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

/**
 * The Class NoteDetail represents a create/edit/delete window for
 * {@link Note}. It sends the note to the {@link NotesDb} to be
 * saved, or the ID of the note to be deleted.
 */
@SuppressLint("SimpleDateFormat")
public class NoteDetail extends Activity implements DatePickerDialog.OnDateSetListener {
	
	/**
	 * mDb is an instance of (@link NotesDb}, which controls
	 * access to the {@link android.database.SQLiteDatabase}.
	 */
	private NotesDb mDb = new NotesDb(NotesDb.WRITE, this);
	
	/**
	 * the {@link android.database.SQLiteDatabase} ID of the
	 * {@link Note} instance. -1 if the note is new.
	 */
	private int mId;
	
	/** 
	 * An instance of {@link Note} that 
	 * The activity shows.
	 */
	private Note mNote;

	/**
	 * On creation of the note, the {@link android.app.Activity}
	 * gets the {@link ndroid.content.Intent}
	 * passed to it by the parent window
	 * and looks for an {@link mId} number to seach {@link NotesDb} for.
	 * 
	 * If there is no ID in the intent, the ID defaults to -1,
	 * and the entries are filled with the default:
	 * 
	 * Subject: Blank,
	 * Date: The current date,
	 * Content: Blank
	 * 
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_detail);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		
		mId = intent.getIntExtra("com.amfontai.cmput301asn1.id", -1);
		
		Button date = (Button) findViewById(R.id.date);

		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		
		/* 
		 * If there is an ID in the intent, query the database and fill
		 * the fields with the saved data.
		 */
		if(-1 != mId) {
			mNote = mDb.getNoteById(mId);
			EditText subject = (EditText) findViewById(R.id.subject);
			subject.setText(mNote.getSubject());
			
			date.setText(mNote.getDate());
			EditText content = (EditText) findViewById(R.id.content);
			content.setText(mNote.getContent());
		}
		/* 
		 * If not, set the date to the current date and
		 * leave the other fields blank
		 */
		else {
			date.setText(format.format(new Date()));
			mNote = new Note();
		}
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
		getMenuInflater().inflate(R.menu.note_detail, menu);
		return true;
	}
	
	/**
	 * Saves the note before pausing the Activity
	 * 
	 * @see android.app.Activity#onPause()
	 */
	@Override
	public void onPause() {
		super.onPause();
		saveNote();
	}

	/**
	 * Reads in the selected menu item
	 * and performs the appropriate task.
	 * 
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
		case R.id.action_trash:
			deleteNote();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Save the {@link mNote} when the save button is
	 * pressed.
	 *
	 * @param v The View
	 */
	public void saveNote(View v) {
		finish();
	}
	
	/**
	 * Saves the {@link mNote} by
	 * setting the appropriate fields
	 * and passing it the {@link mId} and
	 * {@link mDb}, so it can be saved to the
	 * {@link android.database.SQLiteDatabase}
	 */
	public void saveNote() {
		mNote.setSubject(((EditText) findViewById(R.id.subject)).getText().toString());
		mNote.setDate(((Button) findViewById(R.id.date)).getText().toString());
		mNote.setContent(((EditText) findViewById(R.id.content)).getText().toString());
		mNote.saveNote(mId, mDb);
		
	}

	/**
	 * Delete the note by passing it the {@link mId} and {@link mDb}.
	 */
	private void deleteNote() {
		mNote.deleteNote(mId, mDb);
		
	}

	/**
	 * Sets the text of the button to the date selected in the
	 * date picker.
	 * 
	 * @see android.app.DatePickerDialog.OnDateSetListener#onDateSet(android.widget.DatePicker, int, int, int)
	 */
	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Button date = (Button) findViewById(R.id.date);
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		date.setText(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
		
	}
	
	/**
	 * Show date picker dialog.
	 *
	 * @param v the v
	 */
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	/**
	 * The Class DatePickerFragment brings up an
	 * android {@link android.app.DatePickerDialog}
	 */
	public static class DatePickerFragment extends DialogFragment {
		
		/**
		 * @see android.app.DialogFragment#onCreateDialog(android.os.Bundle)
		 */
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			
			Button date = (Button) ((NoteDetail) getActivity()).findViewById(R.id.date);
			
			
			Calendar c = Calendar.getInstance();
			
			try {
				c.setTime(format.parse((String) date.getText()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			return new DatePickerDialog(getActivity(),
					(NoteDetail) getActivity(), c.get(Calendar.YEAR),
					c.get(Calendar.MONTH), c.get(Calendar.DATE));
		}
		
	}

}
