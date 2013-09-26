package com.amfontai.cmput301asn1;

import android.content.ContentValues;
import android.database.Cursor;

public class Note {
	private String mSubject;
	private String mDate;
	private String mContent;
	
	Note(Cursor cursor) {
		mSubject = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_SUBJECT));
		mDate = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_DATE));
		mContent = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_CONTENT));
	}
	
	Note(String subject, String date, String content) {
		this.mSubject = subject;
		this.mDate = date;
		this.mContent = content;
	}
	
	Note() {}

	/**
	 * @return the subject
	 */
	public String getSubject() {
		return mSubject;
	}

	/**
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.mSubject = subject;
	}

	/**
	 * @return the date
	 */
	public String getDate() {
		return mDate;
	}

	/**
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.mDate = date;
	}

	/**
	 * @return the content
	 */
	public String getContent() {
		return mContent;
	}

	/**
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.mContent = content;
	}

	public long saveNote(int id, NotesDb notesDb) {
		
		ContentValues content = new ContentValues();
		content.put(NotesDb.COLUMN_SUBJECT, mSubject);
		content.put(NotesDb.COLUMN_DATE, mDate);
		content.put(NotesDb.COLUMN_CONTENT, mContent);
		if(-1 != id)
			return notesDb.getDB().update(NotesDb.TABLE_NAME, content, NotesDb._ID + " = ?", new String[] {Integer.toString(id)});
		else
			return notesDb.getDB().insert(NotesDb.TABLE_NAME, null, content);
	}

	public int deleteNote(int id, NotesDb notesDb) {
		if(-1 != id) {
			return notesDb.getDB().delete(NotesDb.TABLE_NAME, NotesDb._ID + " = ?", new String[] {Integer.toString(id)});
		}
		return -1;
	}
}