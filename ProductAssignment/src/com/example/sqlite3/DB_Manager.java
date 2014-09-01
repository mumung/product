package com.example.sqlite3;

/**
 * DB_Manager for connecting between the database and a activity
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import java.util.ArrayList;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.model.Product;

public class DB_Manager {

	Context context;
	DB_OpenHelper db_OpenHelper;
	SQLiteDatabase db;
	DB_DAO db_DAO;

	public DB_Manager(Context context) {

		this.context = context;
		db_OpenHelper = new DB_OpenHelper(context);
		db = db_OpenHelper.getWritableDatabase();
		db_DAO = new DB_DAO(db);
	}

	public void close() {

		db.close();
	}

	public long insertProduct(Product product) {

		return db_DAO.insertProduct(product);
	}

	public boolean updateProduct(Product product) {

		return db_DAO.updateProduct(product);
	}

	public boolean deleteProduct(Product product) {

		return db_DAO.deleteProduct(product);
	}

	public boolean deleteAllProducts() {

		return db_DAO.deleteAllProducts();
	}

	public Product selectProduct(long id) {

		return db_DAO.selectProduct(id);
	}
	
	public ArrayList<Product> getAllProducts() {

		return db_DAO.getAllProducts();
	}

	public int getProductCount() {

		return this.db_DAO.getProductCount();
	}

}