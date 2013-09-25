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
import com.amfontai.cmput301asn1.NotesDb.Note;

@SuppressLint("SimpleDateFormat")
public class NoteDetail extends Activity implements DatePickerDialog.OnDateSetListener {
	
	private NotesDb mDb = new NotesDb(NotesDb.WRITE, this);
	private int mId;

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
			Note note = mDb.getNoteById(mId);
			EditText subject = (EditText) findViewById(R.id.subject);
			subject.setText(note.subject);
			
			date.setText(note.date);
			EditText content = (EditText) findViewById(R.id.content);
			content.setText(note.content);
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
	
	public void saveNote(View v) {
		finish();
	}
	
	public void saveNote() {
		mDb.saveNote(mId, mDb.new Note(((EditText) findViewById(R.id.subject)).getText().toString(),
				((Button) findViewById(R.id.date)).getText().toString(),
				((EditText) findViewById(R.id.content)).getText().toString()));
	}

	private void deleteNote() {
		mDb.deleteNote(mId);
		
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
