package com.amfontai.cmput301asn1;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import com.amfontai.cmput301asn1.NotesDb.NotesDbHelper;

public class NoteDetail extends Activity {
	
	NotesDbHelper mDb = new NotesDb().new NotesDbHelper(this);
	
	@SuppressLint("SimpleDateFormat")
	SimpleDateFormat mFormat = new SimpleDateFormat("yyyy-MM-dd");

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_note_detail);
		// Show the Up button in the action bar.
		setupActionBar();
		
		Intent intent = getIntent();
		
		int id = intent.getIntExtra("com.amfontai.cmput301asn1.id", -1);
		
		Button date = (Button) findViewById(R.id.date);
		
		if(-1 != id) {
			SQLiteDatabase db = mDb.getReadableDatabase();
			Cursor cursor;
			cursor = db.query(NotesDb.TABLE_NAME,
					new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_DATE, NotesDb.COLUMN_CONTENT},
					NotesDb._ID + " = ?",
					new String[] {String.valueOf(id)},
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
			date.setText(mFormat.format(new Date()));
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
