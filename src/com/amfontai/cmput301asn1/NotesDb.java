package com.amfontai.cmput301asn1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class NotesDb implements BaseColumns {
	
	public static final String TABLE_NAME = "Notes";
	public static final String COLUMN_SUBJECT = "Subject";
	public static final String COLUMN_DATE = "Date";
	public static final String COLUMN_CONTENT = "Content";
	
	public NotesDb () {}
	
	
	
	
	
	public class NotesDbHelper extends SQLiteOpenHelper {

		public static final int DATABASE_VERSION = 3;
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

		public Cursor getNotesByDate() {
			return getWritableDatabase().query(
					NotesDb.TABLE_NAME,
					new String[] {NotesDb._ID, NotesDb.COLUMN_SUBJECT, NotesDb.COLUMN_DATE},
					null,
					null,
					null,
					null,
					NotesDb.COLUMN_DATE + " DESC");
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

}
