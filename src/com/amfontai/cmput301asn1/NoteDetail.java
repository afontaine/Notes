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
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.amfontai.cmput301asn1.NotesDb.NotesDbHelper;

@SuppressLint("SimpleDateFormat")
public class NoteDetail extends Activity implements DatePickerDialog.OnDateSetListener {
	
	NotesDbHelper mDb = new NotesDb().new NotesDbHelper(this);
	int mId;

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
		
		if(-1 != mId) {
			SQLiteDatabase db = mDb.getReadableDatabase();
			Cursor cursor;
			cursor = db.query(NotesDb.TABLE_NAME,
					new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_DATE, NotesDb.COLUMN_CONTENT},
					NotesDb._ID + " = ?",
					new String[] {String.valueOf(mId)},
					null,
					null,
					null,
					"1");
			
			cursor.moveToFirst();
			EditText subject = (EditText) findViewById(R.id.subject);
			subject.setText(cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_SUBJECT)));
			
			date.setText(cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_DATE)));
			EditText content = (EditText) findViewById(R.id.content);
			content.setText(cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_CONTENT)));
		}
		else
			date.setText(format.format(new Date()));
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
		getMenuInflater().inflate(R.menu.note_detail, menu);
		return true;
	}
	
	@Override
	public void onPause() {
		super.onPause();
		saveNote();
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
		case R.id.action_trash:
			deleteNote();
			NavUtils.navigateUpFromSameTask(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	
	public void saveNote() {
		SQLiteDatabase db = mDb.getWritableDatabase();
		ContentValues content = new ContentValues();
		content.put(NotesDb.COLUMN_SUBJECT, ((EditText) findViewById(R.id.subject)).getText().toString());
		content.put(NotesDb.COLUMN_DATE, ((Button) findViewById(R.id.date)).getText().toString());
		content.put(NotesDb.COLUMN_CONTENT, ((EditText) findViewById(R.id.content)).getText().toString());
		if(-1 != mId)
			db.update(NotesDb.TABLE_NAME, content, NotesDb._ID + " = ?", new String[] {Integer.toString(mId)});
		else
			db.insert(NotesDb.TABLE_NAME, null, content);
	}

	private void deleteNote() {
		if(-1 != mId) {
			SQLiteDatabase db = mDb.getWritableDatabase();
			db.delete(NotesDb.TABLE_NAME, NotesDb._ID + " = ?", new String[] {Integer.toString(mId)});
		}
		
	}

	@Override
	public void onDateSet(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		Button date = (Button) findViewById(R.id.date);
		Calendar c = Calendar.getInstance();
		c.set(year, monthOfYear, dayOfMonth);
		date.setText(new SimpleDateFormat("yyyy-MM-dd").format(c.getTime()));
		
	}
	
	public void showDatePickerDialog(View v) {
		DialogFragment newFragment = new DatePickerFragment();
		newFragment.show(getFragmentManager(), "datePicker");
	}
	
	public static class DatePickerFragment extends DialogFragment {
		
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
			
			return new DatePickerDialog(getActivity(), (NoteDetail) getActivity(), c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE));
		}
		
	}

}
