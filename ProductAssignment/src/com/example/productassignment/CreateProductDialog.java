package com.example.productassignment;

/**
 * Alert dialog for creating and updating a product
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.StringTokenizer;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.model.Product;

public class CreateProductDialog extends DialogFragment {

	
    /*
     * Declare variable and initialize them
     */
	final String TAG = "CreateProduct";
	
	LayoutInflater inflater;
	View view;
	
	TextView title;
	
    EditText name, fields, regular, imageURL, sale, colors, stores, description;

	AlertDialog.Builder builder;

	Context context;
	Product product = null;
	
	// to represent color name for colors
	String[] colorNames = {	"White", "Black", "Red", "Blue", "Green", "Yellow", "Gray"};
	int index;

	public CreateProductDialog(Context context) {
		this.context = context;
		index = 1;
	}

	public CreateProductDialog(Context context, Product product) {
		this.context = context;
		this.product = product;
		index = 2;
	}
	
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		if(index == 1)  	// depend on a context
			view = inflater.inflate(R.layout.create_update, ((MainActivity) context).main_root, false);
		else
			view = inflater.inflate(R.layout.create_update, ((DetailedProductActivity) context).show_root, false);

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		
		title = (TextView) view.findViewById(R.id.title);
		
    	name 	= (EditText) view.findViewById(R.id.nameEditText);
    	fields 	= (EditText) view.findViewById(R.id.fieldsEditText);
    	regular = (EditText) view.findViewById(R.id.regularEditText);
    	sale 	= (EditText) view.findViewById(R.id.saleEditText);
    	colors 	= (EditText) view.findViewById(R.id.colorEditText);
    	stores 	= (EditText) view.findViewById(R.id.storeEditText);
    	imageURL = (EditText) view.findViewById(R.id.imageEditText);
    	description = (EditText) view.findViewById(R.id.descriptionEditText);		
		
		if(index!= 1)
			initializeUpdate();

		builder.setCancelable(false).setPositiveButton("Create/Update", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				
				switch(index){
					case 1: createProduct();
						break;
					case 2: updateProduct();
						break;
				}
				Log.d(TAG, "Create or Update");
			}
		}).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {
				Log.d(TAG, "Cancel");
				dialog.dismiss();
			}
		});

		AlertDialog dialog = builder.create();

		dialog.getWindow().setGravity(Gravity.CENTER);

		return dialog;
	}


	/**
     * to save a new product to database
     */
	private void createProduct(){
		
		product = new Product();
		
		getNewData();
		
		((MainActivity) context).insertProduct(product);
	}

	
	/**
	 * to update a new product to database
	 */
	private void updateProduct(){
		
		getNewData();
		
		((DetailedProductActivity) context).doUpdate(product);
		
	}	
	
	
	/**
	 * to get data from the components of the UI
	 */
	private void getNewData(){
		
		String temp;
		
		product.setName((name.getText().toString()).trim());
		product.setFields((fields.getText().toString()).trim());
		
		temp = (regular.getText().toString()).trim();
		if(temp != null){
			if(temp.equals("") || temp.equals("null") )
				product.setRegular_price(0);
			else
				product.setRegular_price(Double.parseDouble(temp));
				
		}
			
		
		temp = (sale.getText().toString()).trim();
		if(temp != null){
			if(temp.equals("") || temp.equals("null"))
				product.setSale_price(0);
			else
				product.setSale_price(Double.parseDouble(temp));
				
		}
		
		product.setProduct_photo_url((imageURL.getText().toString()).trim());
		product.setDescription((description.getText().toString()).trim());
		
		temp = (colors.getText().toString()).trim();
		product.setColors(convertIntArray(temp));
		
		temp = (stores.getText().toString()).trim();
		product.setStores(convertDictoinary(temp));
		
		Log.d(TAG + " create a product", product.toString());
		
	}
	
	
	/**
	 * to convert from input to int array
	 */
	private int[] convertIntArray(String color){
		
		int[] results = null;
		ArrayList<Integer> tempColor = new ArrayList<Integer>();
		
		
//		Log.d(TAG + " String Color", color);
		
		
		StringTokenizer token = new StringTokenizer(color, ",");
		
		while(token.hasMoreElements()){
			
			String uncheckedColor = ((token.nextToken().toString()).trim()).toLowerCase(Locale.US);
			
			for (int i = 0; i < colorNames.length; i++) {
				
				if( uncheckedColor.equals((colorNames[i]).toLowerCase(Locale.US))){
					
					tempColor.add(i);
					break;
				}
				
			}
		}
		
		if(tempColor.size() > 0){
			
			results = new int[tempColor.size()];
			
			for (int i = 0; i < tempColor.size(); i++) 
				results[i] = tempColor.get(i);
		} 
		
//		Log.d(TAG + " color!!!", Arrays.toString(results));

		return results;
	}

	
	/**
	 * to convert from input to Dictionary object
	 */
	private Dictionary<Integer, String> convertDictoinary(String store ){
		
		Integer key = 1;
		String value;
		
		Dictionary<Integer, String> results = new Hashtable<Integer, String>();
	
		StringTokenizer token = new StringTokenizer(store, ",");
		
		while(token.hasMoreTokens()){
			
			value = (token.nextToken().toString()).trim();
			
			if(value != null){
				results.put(key, value);
				key++;
			}
				
		}			

//		Log.d(TAG + "stories", stores.toString());
		
		return results;
	}	
	

	/**
	 * to setup for updating the product
	 */
	private void initializeUpdate(){

		title.setText("Update the product");
		
    	name.setText(product.getName());
    	fields.setText(product.getFields());
    	regular.setText(product.getRegular_price() + "");
    	sale.setText(product.getSale_price() + "");
    	imageURL.setText(product.getProduct_photo_url());
    	description.setText(product.getDescription());
    	colors.setText(showColors(product.getColors()));
    	stores.setText(showStores(product.getStores()));
	}

	
	/**
	 * to return colors text from colors array
	 */
	private String showColors(int[] intArray ){
		
		StringBuilder sb;
		
		if(intArray != null){
			
			if(intArray.length > 0){
				
				sb = new StringBuilder();

				for (int i = 0; i < intArray.length; i++) {
					sb.append(colorNames[intArray[i]]);
					
					if(i < intArray.length - 1)
						sb.append(", ");
				}
			
				return sb.toString();
			}
		}
		
		return null;
	}

	
	/**
	 * to return stores text from dictionary
	 */
	private String showStores(Dictionary<Integer, String> dic ){
		
		StringBuilder sb = null;
		
		Log.d(TAG + " dic size", dic.size() +"");
				
		if(dic.size() > 0){
			Enumeration<Integer> storesKey = dic.keys();
			
			int name;
		
			while(storesKey.hasMoreElements()){
				
				if(sb == null)
					sb = new StringBuilder();
				else
					sb.append(", ");
				
				name = (Integer) storesKey.nextElement();
				sb.append((dic.get(name)).trim());
	
			}
			
			Log.d(TAG + "dic", sb.toString());
			
			return sb.toString();
		}
		
		return "";
	}	
	
	@Override
	public void dismiss() {

		super.dismiss();
	}

}
