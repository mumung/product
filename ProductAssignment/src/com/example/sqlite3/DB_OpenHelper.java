package com.example.sqlite3;

/**
 * DB_OpenHelper for helping about creating a database and updating database version
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_OpenHelper extends SQLiteOpenHelper {

	static final String DATABASE_NAME = "products.db";
	static final int DATABASE_VERSION = 1;

	DB_OpenHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onOpen(SQLiteDatabase db) {

		super.onOpen(db);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		DB_Table.onCreate(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		DB_Table.onUpgrade(db, oldVersion, newVersion);
	}

}