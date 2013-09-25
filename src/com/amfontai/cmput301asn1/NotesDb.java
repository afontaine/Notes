package com.amfontai.cmput301asn1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.mcavallo.opencloud.Cloud;
import org.mcavallo.opencloud.formatters.HTMLFormatter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.widget.SimpleCursorAdapter;

public class NotesDb implements BaseColumns {
	
	public static char READ = 'r';
	public static char WRITE = 'w';
	public static int HTML = 1;
	public static int CSS = 0;
	
	private static final int MAX_WORDS_CLOUD = 100;
	private static final double MAX_TEXT_SIZE = 38.0;
	private static final String TABLE_NAME = "Notes";
	private static final String COLUMN_SUBJECT = "Subject";
	private static final String COLUMN_DATE = "Date";
	private static final String COLUMN_CONTENT = "Content";
	
	private NotesDbHelper mDbHelper;
	private char RW;
	
	public NotesDb (char RW, Context context) {
			mDbHelper  = new NotesDbHelper(context);	
			this.RW = RW;
	}
	
	private SQLiteDatabase getDB() {
		if(RW == READ)
			return mDbHelper.getReadableDatabase();
		else if(RW == WRITE)
			return mDbHelper.getWritableDatabase();
		else
			return null;
	}

	public Cursor getNotesByDate() {
		return getDB().query(
				NotesDb.TABLE_NAME,
				new String[] {NotesDb._ID, NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_DATE},
				null,
				null,
				null,
				null,
				NotesDb.COLUMN_DATE + " DESC");
	}
	
	public Note getNoteById(int id) {
		Cursor cursor;
		cursor = getDB().query(NotesDb.TABLE_NAME,
				new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_DATE, NotesDb.COLUMN_CONTENT},
				NotesDb._ID + " = ?",
				new String[] {String.valueOf(id)},
				null,
				null,
				null,
				"1");
		if (cursor.moveToFirst())
			return new Note(cursor);
		else
			return null;
	}
	
	public long saveNote(int id, Note note) {
		
		ContentValues content = new ContentValues();
		content.put(NotesDb.COLUMN_SUBJECT, note.subject);
		content.put(NotesDb.COLUMN_DATE, note.date);
		content.put(NotesDb.COLUMN_CONTENT, note.content);
		if(-1 != id)
			return getDB().update(NotesDb.TABLE_NAME, content, NotesDb._ID + " = ?", new String[] {Integer.toString(id)});
		else
			return getDB().insert(NotesDb.TABLE_NAME, null, content);
	}
	
	public int deleteNote(int id) {
		if(-1 != id) {
			return getDB().delete(NotesDb.TABLE_NAME, NotesDb._ID + " = ?", new String[] {Integer.toString(id)});
		}
		return -1;
	}
	
	public List<String> getAllWords() {
		Cursor all = getDB().query(NotesDb.TABLE_NAME, 
				new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_CONTENT},
				null,null, null, null, null);
		String results = "";
		if(all.moveToFirst()) {
			do {
				results += " " + all.getString(all.getColumnIndex(NotesDb.COLUMN_SUBJECT))
						+ " " + all.getString(all.getColumnIndex(NotesDb.COLUMN_CONTENT));
			} while(all.moveToNext());
		
			all.close();
			
			results = results.trim();
			String[] words = results.split("\\W+");
			return Arrays.asList(words);
		}
		else {
			return new ArrayList<String>();
		}
	}
	
	public String getWordCloud() {
		Cloud wordCloud = new Cloud();
		wordCloud.setMaxWeight(MAX_TEXT_SIZE);
		wordCloud.setMinWeight(12);
		for(String word : getAllWords())
			wordCloud.addText(word);
		wordCloud.setMaxTagsToDisplay(MAX_WORDS_CLOUD);
		
		HTMLFormatter html = new HTMLFormatter();
		html.setHtmlTemplateTag("<span style=\"font-size: %tag-weight%px\">%tag-name% </span>\n");
		return (!wordCloud.tags().isEmpty()) ? html.html(wordCloud) : "<span>Add some notes to make a word cloud!</span>";
	}
	
	public long totalEntries() {
		return DatabaseUtils.queryNumEntries(getDB(), NotesDb.TABLE_NAME);
	}
	
	public long totalCharacters() {
		Cursor all = getDB().query(NotesDb.TABLE_NAME, 
				new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_CONTENT},
				null,null, null, null, null);
		String results = "";
		if(all.moveToFirst())
			do {
				results += all.getString(all.getColumnIndex(NotesDb.COLUMN_SUBJECT))
						+ all.getString(all.getColumnIndex(NotesDb.COLUMN_CONTENT));
			} while(all.moveToNext());
		
		all.close();
		
		return results.length();
	}
	
	public long totalWords() {
		return getAllWords().size();
	}
	
	public ArrayList<String> topHundred() {
		List<String> words = getAllWords();
		
		HashMap<String, Integer> wordMap = new HashMap<String, Integer>();
		
		for(String word : words) {
			if(wordMap.containsKey(word))
				wordMap.put(word, wordMap.get(word) + 1);
			else
				wordMap.put(word, 1);
		}
		
		return recursiveSort(wordMap, new ArrayList<String>());
	}
	
	private ArrayList<String> recursiveSort(HashMap<String, Integer> wordMap, ArrayList<String> results) {
		if(results.size() > 99 || wordMap.isEmpty()) 
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
	
	public SimpleCursorAdapter listNotes(Context context) {
		return new SimpleCursorAdapter(
				context,
				android.R.layout.two_line_list_item,
				getNotesByDate(),
				new String[] {NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_DATE},
				new int[] {android.R.id.text1, android.R.id.text2},
				0);
	}

	private class NotesDbHelper extends SQLiteOpenHelper {

		public static final int DATABASE_VERSION = 4;
		public static final String DATABASE_NAME = "Notes.db";
		
		private static final String TEXT_TYPE = " TEXT";
		private static final String COMMA_SEP = ", ";
		private static final String SQL_CREATE_TABLE = 
				"CREATE TABLE " + NotesDb.TABLE_NAME + " ("
				+ NotesDb._ID + " INTEGER PRIMARY KEY" + COMMA_SEP
				+ NotesDb.COLUMN_SUBJECT + TEXT_TYPE + COMMA_SEP
				+ NotesDb.COLUMN_DATE + TEXT_TYPE + COMMA_SEP
				+ NotesDb.COLUMN_CONTENT + TEXT_TYPE + " )";
		private static final String SQL_DELETE_TABLE = 
				"DROP TABLE IF EXISTS " + NotesDb.TABLE_NAME;
				
		public NotesDbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase arg0) {
			arg0.execSQL(SQL_CREATE_TABLE);
			ContentValues c = new ContentValues();
			c.put(COLUMN_SUBJECT, "test");
			c.put(COLUMN_DATE, "2013-09-17");
			c.put(COLUMN_CONTENT, "Hello!");
			arg0.insert(TABLE_NAME, null, c);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL(SQL_DELETE_TABLE);
			db.execSQL(SQL_CREATE_TABLE);
		}
		
		public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onUpgrade(db, oldVersion, newVersion);
		}	
	}
	
	public class Note {
		public String subject;
		public String date;
		public String content;
		
		Note(Cursor cursor) {
			subject = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_SUBJECT));
			date = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_DATE));
			content = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_CONTENT));
		}
		
		Note(String subject, String date, String content) {
			this.subject = subject;
			this.date = date;
			this.content = content;
		}
	}
}
