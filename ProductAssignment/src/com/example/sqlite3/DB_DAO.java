package com.example.sqlite3;

/**
 * DB_DAO for inserting, updating, deleting and selecting a model on the database
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.StringTokenizer;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.model.Product;

public class DB_DAO {

    /*
     * Declare variable and initialize them
     */
	private SQLiteDatabase db;
	
	final String TAG = "DB_DAO";

	public DB_DAO(SQLiteDatabase db) {

		this.db = db;
	}

	
	/**
	 * to record a data to the database and return id number of the row
	 */
	public long insertProduct(Product product) {

		ContentValues values = new ContentValues();
		values.put(DB_Table.FIELDS, product.getFields());
		values.put(DB_Table.NAME, product.getName());
		values.put(DB_Table.DESCRIPTION, product.getDescription());
		values.put(DB_Table.PRODUCT_PHOTO_URL, product.getProduct_photo_url());
		values.put(DB_Table.REGULAR_PRICE, product.getRegular_price());
		values.put(DB_Table.SALE_PRICE, product.getSale_price());
		values.put(DB_Table.COLORS, Arrays.toString(product.getColors()));
		values.put(DB_Table.STORES, product.getStores().toString());

		Log.d(TAG + "insert", product.toString());

		return db.insert(DB_Table.TABLE_NAME, null, values);
	}

	
	/**
	 * to upgrade a data with special id
	 */	
	public boolean updateProduct(Product product) {
		
		ContentValues values = new ContentValues();
		values.put(DB_Table.FIELDS, product.getFields());
		values.put(DB_Table.NAME, product.getName());
		values.put(DB_Table.DESCRIPTION, product.getDescription());
		values.put(DB_Table.PRODUCT_PHOTO_URL, product.getProduct_photo_url());
		values.put(DB_Table.REGULAR_PRICE, product.getRegular_price());
		values.put(DB_Table.SALE_PRICE, product.getSale_price());
		values.put(DB_Table.COLORS, Arrays.toString(product.getColors()));
		values.put(DB_Table.STORES, product.getStores().toString());

		boolean isUpdate = db.update(DB_Table.TABLE_NAME, values, DB_Table.ID + "=?",
				new String[] { product.get_id() + "" }) > 0;
				
		Log.d(TAG + "updateProduct", isUpdate + "");
		
		return isUpdate;
	}

	
	/**
	 * to delete a data with special id
	 */	
	public boolean deleteProduct(Product product) {
		
		boolean isDelete = db.delete(DB_Table.TABLE_NAME, DB_Table.ID + "=?",
				new String[] { product.get_id() + "" }) > 0;
				
//		Log.d(TAG + "deleteProduct()", product.get_id() + "");
		return isDelete;
	}

	
	/**
	 * to delete all data in the table
	 */	
	public boolean deleteAllProducts() {

		boolean isDeleteAll = db.delete(DB_Table.TABLE_NAME, DB_Table.ID, null) > 0;
		
//		Log.d(TAG + "deleteAllProducts()", isDeleteAll + "");
		return isDeleteAll;
	}

	
	/**
	 * to get the number of all data in the table
	 */	
	public int getProductCount() {

		String sql = "SELECT  * FROM " + DB_Table.TABLE_NAME + ";";
		Cursor cursor = db.rawQuery(sql, null);

		int result = cursor.getCount();
		cursor.close();

//		Log.d(TAG + "getProductCount()", result + "");
		return result;
	}
	
	
	/**
	 * to return a data with the same id
	 */	
	public Product selectProduct(long id){
		
		Product product = null;
		
		Cursor cursor = db.query(
				DB_Table.TABLE_NAME, 
				new String[] { DB_Table.ID, DB_Table.FIELDS, DB_Table.NAME, DB_Table.DESCRIPTION,
						DB_Table.PRODUCT_PHOTO_URL, DB_Table.REGULAR_PRICE, DB_Table.SALE_PRICE,
						DB_Table.COLORS,DB_Table.STORES}, 
				DB_Table.ID +"="+ id,
				null, null, null, null, null);
		
		if(cursor != null && cursor.moveToFirst())			
			product = this.buildProductFromCursor(cursor);			
	
		cursor.close();
		
		if(!cursor.isClosed()){
			cursor.close();
		}
		
//		Log.d(TAG + "selectProduct()", product.toString());
		
		return product;
	}

	
	/**
	 * to return all data in the table
	 */	
	public ArrayList<Product> getAllProducts() {

		ArrayList<Product> result = new ArrayList<Product>();

		Cursor cursor = db.query(
				DB_Table.TABLE_NAME, 
				new String[] { DB_Table.ID, DB_Table.FIELDS, DB_Table.NAME, DB_Table.DESCRIPTION,
						DB_Table.PRODUCT_PHOTO_URL, DB_Table.REGULAR_PRICE, DB_Table.SALE_PRICE,
						DB_Table.COLORS,DB_Table.STORES}, 
				null, null, null, null, null);
		
		if (cursor != null) {

			cursor.moveToFirst();

			do {
				Product product = this.buildProductFromCursor(cursor);

				if (product != null) {
					result.add(product);
				}
			} while (cursor.moveToNext());

			if (!cursor.isClosed()) {

				cursor.close();
			}

		}

//		 Log.d(TAG + "getAllProducts()", result.toString());

		return result;
	}

	
	/**
	 * to convert data from cursor to model
	 */	
	private Product buildProductFromCursor(Cursor cursor) {

		Product product = null;

		if (cursor != null) {
			product = new Product();
			product.set_id(cursor.getInt(0));
			product.setFields(cursor.getString(1));
			product.setName(cursor.getString(2));
			product.setDescription(cursor.getString(3));
			product.setProduct_photo_url(cursor.getString(4));
			product.setRegular_price(cursor.getDouble(5));
			product.setSale_price(cursor.getDouble(6));
			
//			Log.d(TAG + " colors", (cursor.getString(7)).toString() );
			product.setColors(convertToIntArrayDB((cursor.getString(7)).toString()));
			
//			Log.d(TAG + " store", (cursor.getString(8)).toString() );
			product.setStores(convertToDictionaryDB((cursor.getString(8)).toString()));

		}
		return product;
	}
	
	
	
	/**
	 *  to convert from text to int array
	 */
	private int[] convertToIntArrayDB(String colorsValue){
		
		ArrayList<Integer> numbers = null;
		int[] results = null;
		
		if (colorsValue != null) { 
			
			numbers = new ArrayList<Integer>();
			String colorsText = colorsValue.replace("[","");
			colorsText = colorsText.replace("]","");
			colorsText = (colorsText.replace("null","")).trim();
			
			StringTokenizer token = new StringTokenizer(colorsText, ",");
			
			while(token.hasMoreTokens()){
				String temp = (token.nextToken().toString()).trim();
				
				if(temp != null){
					
					if((!temp.equals("null")))
						
						try {
							numbers.add(Integer.parseInt(temp));
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
			}
			
			results = new int[numbers.size()];
			
			for (int i = 0; i < numbers.size(); i++) 
				results[i] = numbers.get(i);
		}
		
		return results;
	}
	
	
	/**
	 * to convert from text to Dictionary object
	 */
	private Dictionary<Integer, String> convertToDictionaryDB(String storesValue ){
		
		Integer key;
		String value;
		
		Dictionary<Integer, String> results = null;
				
		if(storesValue != null)	{
			
			results = new Hashtable<Integer, String>();
			
			String storesText = storesValue.replaceAll("[{|}|\"]", "");
			
			Log.d(TAG, storesText );
			
			StringTokenizer token = new StringTokenizer(storesText, ",");
			StringTokenizer item;
			
			while(token.hasMoreTokens()){
				
				item = new StringTokenizer(token.nextToken().toString(), "=");
				
				key = Integer.parseInt((item.nextToken().toString()).trim());
				value = (item.nextToken().toString()).trim();
				
				if(key != null && value != null)
					results.put(key, value);
			}
			
		}	
		
		return results;
	}

}
