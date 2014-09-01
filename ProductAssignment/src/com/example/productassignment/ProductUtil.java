package com.example.productassignment;

/**
 * ProductUtil for parsing JSON sting
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.model.Product;

public class ProductUtil {
	
	static public class ProductsJSONParser{		
		
		static ArrayList<Product> parseProducts(String jsonString) throws JSONException{
			
			ArrayList<Product> products = new ArrayList<Product>();	
			
			JSONArray productsJSONArray = new JSONArray(jsonString);
			
			for(int i=0; i < productsJSONArray.length(); i++){
				
				JSONObject productJSONObject = productsJSONArray.getJSONObject(i);
				
				Product tweet = new Product(productJSONObject);
				
				products.add(tweet);
			}
			
			return products;
		}
	}
	

}
