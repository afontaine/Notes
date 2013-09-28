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

import android.content.ContentValues;
import android.database.Cursor;

/**
 * The Class Note.
 */
public class Note {
	
	/** The subject of the Note. */
	private String mSubject;
	
	/** The date of the Note. */
	private String mDate;
	
	/** The content of the Noe. */
	private String mContent;
	
	/**
	 * Instantiates a new note.
	 *
	 * @param cursor A cursor pointing to a row in an SQLiteDatabase
	 */
	Note(Cursor cursor) {
		mSubject = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_SUBJECT));
		mDate = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_DATE));
		mContent = cursor.getString(cursor.getColumnIndex(NotesDb.COLUMN_CONTENT));
	}
	
	/**
	 * Instantiates a new note.
	 *
	 * @param subject the subject
	 * @param date the date
	 * @param content the content
	 */
	Note(String subject, String date, String content) {
		this.mSubject = subject;
		this.mDate = date;
		this.mContent = content;
	}
	
	/**
	 * Instantiates a new note.
	 * Leaves everything blank ("new note")
	 */
	Note() {}

	/**
	 * Gets the subject.
	 *
	 * @return the subject
	 */
	public String getSubject() {
		return mSubject;
	}

	/**
	 * Sets the subject.
	 *
	 * @param subject the subject to set
	 */
	public void setSubject(String subject) {
		this.mSubject = subject;
	}

	/**
	 * Gets the date.
	 *
	 * @return the date
	 */
	public String getDate() {
		return mDate;
	}

	/**
	 * Sets the date.
	 *
	 * @param date the date to set
	 */
	public void setDate(String date) {
		this.mDate = date;
	}

	/**
	 * Gets the content.
	 *
	 * @return the content
	 */
	public String getContent() {
		return mContent;
	}

	/**
	 * Sets the content.
	 *
	 * @param content the content to set
	 */
	public void setContent(String content) {
		this.mContent = content;
	}

	/**
	 * Save note.
	 *
	 * @param id The row ID of the current Note. If -1, it is a new note.
	 * @param notesDb the NotesDb object to put the note in
	 * @return the long Either the number of rows affected, or the ID of the new row.
	 */
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

	/**
	 * Delete note.
	 *
	 * @param id The row ID of the current Note. If -1, returns -1, as nothing happened.
	 * @param notesDb the NotesDb object to remove the note from.
	 * @return the number of rows affected.
	 */
	public int deleteNote(int id, NotesDb notesDb) {
		if(-1 != id) {
			return notesDb.getDB().delete(NotesDb.TABLE_NAME, NotesDb._ID + " = ?", new String[] {Integer.toString(id)});
		}
		return -1;
	}
}