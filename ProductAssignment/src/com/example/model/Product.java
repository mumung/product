package com.example.model;

/**
 * Model class for Product
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import java.util.Arrays;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable{

    /*
     * Declare variable and initialize them
     */
	int _id;
	String fields, name, description, product_photo_url;
	double regular_price, sale_price;
	int[] colors;
	Dictionary <Integer, String> stores;
	
	final String TAG = "Product";

	
	/**
	 *  default constructor
	 */
	public Product( ) {
		
		stores = new Hashtable<Integer, String>();
	}	

	
	/**
	 *  constructor with all fields
	 */
	public Product(int id, String fields, String name, String description,
			String product_photo_url, double regular_price, double sale_price,
			int[] colors, Dictionary<Integer, String> stores) {
		super();
		this._id = id;
		this.fields = fields;
		this.name = name;
		this.description = description;
		this.product_photo_url = product_photo_url;
		this.regular_price = regular_price;
		this.sale_price = sale_price;
		this.colors = colors;
		this.stores = stores;
	}

	
	/**
	 * constructor to convert from Json object to Class fields
	 */
	public Product(JSONObject object) throws JSONException {
		super();
		this.fields = object.getString("fields");;
		this.name = object.getString("name");
		this.description = object.getString("description");
		this.product_photo_url = object.getString("product_photo_url");
		this.regular_price = Double.parseDouble(object.getString("regular_price"));
		this.sale_price = Double.parseDouble(object.getString("sale_price"));
		
		// for int array
		JSONArray colorsJson = object.getJSONArray("colors");
		convertToIntArray(colorsJson);
//		Log.d(TAG + "colors", colorsJson.toString() );		
		
		// for Dictionary
		JSONObject storesJoson = object.getJSONObject("stores");
		stores = convertToDictionary(storesJoson.toString());
//		Log.d(TAG + "stores", storesJoson.toString() );
	}

	
	/**
	 * to convert from JSONArray to int array
	 */
	private void convertToIntArray(JSONArray colorsJson){
		
		if (colorsJson != null) { 
			   int size = colorsJson.length();
			   colors = new int[size];
			   for (int i=0; i<size; i++){ 
				   
				   try {
					   colors[i] = (colorsJson.getInt(i));
				   } catch (JSONException e) {
						e.printStackTrace();
				   }
			   } 
		}
		
//		Log.d(TAG + "colors", colors[0]+" "+colors[1]);
	}
	
	
	/**
	 * to convert from JSONOject to Dictionary object
	 */
	private Dictionary<Integer, String> convertToDictionary(String storesJoson ){
		
		Integer key;
		String value;
		
		Dictionary<Integer, String> results = new Hashtable<Integer, String>();
		
		String storesText = storesJoson.replaceAll("[{|}|\"]", "");
		
		StringTokenizer token = new StringTokenizer(storesText, ",");
		StringTokenizer item;
		
		while(token.hasMoreTokens()){
			
			item = new StringTokenizer(token.nextToken().toString(), ":");
			
			key = Integer.parseInt((item.nextToken().toString()).trim());
			value = (item.nextToken().toString()).trim();
			
			if(key != null && value != null)
				results.put(key, value);
		}			

//		Log.d(TAG + "stories", stores.toString());
		
		return results;
	}
	
	
	/*
	 *  setter method for all fields
	 */
	public void set_id(int id) {
		this._id = id;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setProduct_photo_url(String product_photo_url) {
		this.product_photo_url = product_photo_url;
	}

	public void setRegular_price(double regular_price) {
		this.regular_price = regular_price;
	}

	public void setSale_price(double sale_price) {
		this.sale_price = sale_price;
	}
	
	public void setColors(int[] colors) {
		this.colors = colors;
	}

	public void setStores(Dictionary<Integer, String> stores) {
		this.stores = stores;
	}

	
	
	/*
	 *  getter method for all fields
	 */
	public int get_id() {
		return _id;
	}

	public String getFields() {
		return fields;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getProduct_photo_url() {
		return product_photo_url;
	}

	public double getRegular_price() {
		return regular_price;
	}

	public double getSale_price() {
		return sale_price;
	}

	public int[] getColors() {
		return colors;
	}

	public Dictionary<Integer, String> getStores() {
		return stores;
	}

	/*
	 * toString Method
	 */
	@Override
	public String toString() {
		return "Product [id=" + _id + ", fields=" + fields + ", name=" + name
				+ ", description=" + description + ", product_photo_url="
				+ product_photo_url + ", regular_price=" + regular_price
				+ ", sale_price=" + sale_price + ", colors="
				+ Arrays.toString(colors) + ", stores=" + stores + "]";
	}
	
	
	/*
	 * to implement Parcelable 
	 */
	@Override
	public void writeToParcel(Parcel out, int flags) {
		
		out.writeInt(_id);
        out.writeString(fields);
        out.writeString(name);
        out.writeString(description);
        out.writeString(product_photo_url);
        out.writeDouble(regular_price);
        out.writeDouble(sale_price);
        out.writeIntArray(colors);
        out.writeString(stores.toString());
		
	}
	
	
    public static final Parcelable.Creator<Product> CREATOR
		= new Parcelable.Creator<Product>() {

    	public Product createFromParcel(Parcel in) {
    		return new Product(in);
    	}
	
    	public Product[] newArray(int size) {
    		return new Product[size];
    	}
    };
    
	private Product(Parcel in) {
	
		this._id = in.readInt();
		this.fields = in.readString();
		this.name = in.readString();
		this.description = in.readString();
		this.product_photo_url = in.readString();
		this.regular_price = in.readDouble();
		this.sale_price = in.readDouble();
		this.colors = in.createIntArray();
		
		String storesText = (in.readString()).replace("=", ":");
		stores = new Hashtable<Integer, String>();
		this.stores = convertToDictionary(storesText);
//		Log.d(TAG + " stores", storesText);
		
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

}
