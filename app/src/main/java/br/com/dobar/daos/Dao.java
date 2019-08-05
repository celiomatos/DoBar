package br.com.dobar.daos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class Dao {
	private DatabaseHelper helper;
	private SQLiteDatabase db;

	/**
	 * 
	 * @param context
	 */
	public Dao(Context context) {
		helper = new DatabaseHelper(context);
	}

	/**
	 * 
	 * @return
	 */
	public SQLiteDatabase getDb() {
		if (db == null) {
			db = helper.getWritableDatabase();
		}
		return db;
	}

	/**
	 * 
	 */
	public void close() {
		helper.close();
		db = null;
	}
}
