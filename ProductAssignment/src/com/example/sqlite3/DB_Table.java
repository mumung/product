package com.example.sqlite3;

/**
 * DB_Table for creating table and fileds
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


public class DB_Table {

	static final String TABLE_NAME = "products";
	static final String ID = "_id";
	static final String FIELDS = "fields";
	static final String NAME = "name";
	static final String DESCRIPTION = "description";
	static final String PRODUCT_PHOTO_URL = "product_photo_url";
	static final String REGULAR_PRICE = "regular_price";
	static final String SALE_PRICE = "sale_price";
	static final String COLORS = "colors";
	static final String STORES = "stores";
	
	/**
	 * to create table on the database depended on model
	 */ 
	static public void onCreate(SQLiteDatabase db) {

		StringBuilder SB = new StringBuilder();

		SB.append("CREATE TABLE " + DB_Table.TABLE_NAME + " (");
		SB.append(DB_Table.ID + " integer primary key autoincrement, ");
		SB.append(DB_Table.FIELDS + " text not null, ");
		SB.append(DB_Table.NAME + " text not null, ");
		SB.append(DB_Table.DESCRIPTION + " text, ");
		SB.append(DB_Table.PRODUCT_PHOTO_URL + " text, ");
		SB.append(DB_Table.REGULAR_PRICE + " float, ");
		SB.append(DB_Table.SALE_PRICE + " float, ");
		SB.append(DB_Table.COLORS + " text, ");
		SB.append(DB_Table.STORES + " text);");

		try {
			db.execSQL(SB.toString());
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * to upgrade from the old version database to the new version database
	 */
	static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS " + DB_Table.TABLE_NAME);
		DB_Table.onCreate(db);
	}

}
