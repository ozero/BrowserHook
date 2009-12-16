/*
 * Copyright (C) 2008 Google Inc.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.example.browserhook;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Converter {

	public static final String KEY_TITLE = "title";
	public static final String KEY_URL = "url";
	public static final String KEY_APP = "app";
	public static final String KEY_ACTIVITY = "activity";
	public static final String KEY_ROWID = "_id";

	private static final String TAG = "Converter";
	private DatabaseHelper mDbHelper;
	private SQLiteDatabase mDb;// DBへの接続オブジェクト

	private static final String DATABASE_NAME = "data";
	private static final String DATABASE_TABLE = "item";
	private static final int DATABASE_VERSION = 2;

	/**
	 * Database creation sql statement
	 */
	private static final String DATABASE_CREATE = "create table "
			+ DATABASE_TABLE + " (" + "_id integer primary key autoincrement, "
			+ "title text not null, url text not null, "
			+ "app text not null, activity text not null);";

	// 呼び出し元Activityへの参照をContextとして持ち回りできるように
	private final Context mCtx;

	// DBアクセスのラッパークラス（SQLiteクラスをベースに
	private static class DatabaseHelper extends SQLiteOpenHelper {

		// コンストラクタ。使用するDBファイルなどを指定
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		// 初めて作成された場合はこのcreate文を実行
		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(DATABASE_CREATE);
			//あと初期データの投入
			String iv[][] = {
					{ "direct[dolphin]", "",
							"com.mgeek.android.DolphinBrowser",
							"com.mgeek.android.DolphinBrowser.BrowserActivity" },
					{ "pc2m+dolphin", "http://rg0020.ddo.jp/p/?",
							"com.mgeek.android.DolphinBrowser",
							"com.mgeek.android.DolphinBrowser.BrowserActivity" },
					{ "bing+dolphin", "http://d2c.infogin.com/ja-jp/lnk000/=",
							"com.mgeek.android.DolphinBrowser",
							"com.mgeek.android.DolphinBrowser.BrowserActivity" }
			};
			for(String iv2[] : iv){
				ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_TITLE, iv2[0]);
				initialValues.put(KEY_URL, iv2[1]);
				initialValues.put(KEY_APP, iv2[2]);
				initialValues.put(KEY_ACTIVITY, iv2[3]);
				db.insert(DATABASE_TABLE, null, initialValues);
			}
			return;
		}

		@Override
		// スケーマのアップグレード処理に付けたし
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
					+ newVersion + ", which will destroy all old data");
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE);
			onCreate(db);
		}
	}

	/**
	 * コンストラクタ - takes the context to allow the database to be opened/created
	 * 
	 * @param ctx
	 *            the Context within which to work
	 */
	public Converter(Context ctx) {
		// mCtxプロパティに呼び出し元Activityへの参照をコンテクストとして持っておく
		this.mCtx = ctx;
	}

	/**
	 * Open the notes database. If it cannot be opened, try to create a new
	 * instance of the database. If it cannot be created, throw an exception to
	 * signal the failure
	 * 
	 * @return this (self reference, allowing this to be chained in an
	 *         initialization call)
	 * @throws SQLException
	 *             if the database could be neither opened or created
	 */
	public Converter open() throws SQLException {
		// DBヘルパインスタンスを持つ
		mDbHelper = new DatabaseHelper(mCtx);
		// DBヘルパよりDB接続を受け取り、このクラスにプロパティとして持っておく
		mDb = mDbHelper.getWritableDatabase();
		return this;
	}

	public void close() {
		mDbHelper.close();
	}

	/**
	 * Create a new note using the title and body provided. If the note is
	 * successfully created return the new rowId for that note, otherwise return
	 * a -1 to indicate failure.
	 * 
	 * @param title
	 *            the title of the note
	 * @param body
	 *            the body of the note
	 * @return rowId or -1 if failed
	 */
	public long createItem(String title, String url, String app, String activity) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_TITLE, title);
		initialValues.put(KEY_URL, url);
		initialValues.put(KEY_APP, app);
		initialValues.put(KEY_ACTIVITY, activity);

		return mDb.insert(DATABASE_TABLE, null, initialValues);
	}

	/**
	 * Delete the note with the given rowId
	 * 
	 * @param rowId
	 *            id of note to delete
	 * @return true if deleted, false otherwise
	 */
	public boolean deleteItem(long rowId) {

		return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
	}

	/**
	 * Return a Cursor over the list of all notes in the database
	 * 
	 * @return Cursor over all notes
	 */
	public Cursor fetchAllItems() {

		return mDb.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_URL, KEY_APP, KEY_ACTIVITY }, null, null, null, null, null);
	}

	/**
	 * Return a Cursor positioned at the note that matches the given rowId
	 * 
	 * @param rowId
	 *            id of note to retrieve
	 * @return Cursor positioned to matching note, if found
	 * @throws SQLException
	 *             if note could not be found/retrieved
	 */
	public Cursor fetchItem(long rowId) throws SQLException {

		Cursor mCursor =

		mDb.query(true, DATABASE_TABLE, new String[] { KEY_ROWID, KEY_TITLE,
				KEY_URL, KEY_APP, KEY_ACTIVITY }, KEY_ROWID + "=" + rowId,
				null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

	}

	/**
	 * Update the note using the details provided. The note to be updated is
	 * specified using the rowId, and it is altered to use the title and body
	 * values passed in
	 * 
	 * @param rowId
	 *            id of note to update
	 * @param title
	 *            value to set note title to
	 * @param body
	 *            value to set note body to
	 * @return true if the note was successfully updated, false otherwise
	 */
	public boolean updateItem(long rowId, String title, String url, String app,
			String activity) {
		ContentValues args = new ContentValues();
		args.put(KEY_TITLE, title);
		args.put(KEY_URL, url);
		args.put(KEY_APP, app);
		args.put(KEY_ACTIVITY, activity);

		return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
	}
}
