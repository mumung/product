package com.example.productassignment;

/**
 * DetailedProductActivity for updating and showing a detail information of a product
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Enumeration;

import android.app.Activity;
import android.app.FragmentManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Product;

public class DetailedProductActivity extends Activity {

	final String TAG = "DetailedProduct";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detailed_product);
		
		initialize();
	}

	
	/*
	 * Declare variable and initialize them
	 */
	RelativeLayout show_root;
	
    Button update, delete;
    TextView name, fields, regular, sale, colors, stores, description;
    ImageView image;
	
	Bitmap bitmap = null;
    
	FragmentManager fragmentManager;
	
	Handler handler;
	
    // to represent each property of a product
    String[] titles = {	"Name: ", "Fields: ", "Regular price: $ ", "Sale Price: $ ", 
			"Colors: ", "Stores: ", "Descriotion: "};
    
    // to represent color name for colors
    String[] colorNames = {	"White", "Black", "Red", "Blue", "Green", "Yellow", "Gray"};
    
    ArrayList<Product> products = null;
    int index;
	boolean isUpdate;
	float start, end, touchLength;
	
	private void initialize(){
		
		show_root = (RelativeLayout) findViewById(R.id.show_root);
		
		products = getIntent().getParcelableArrayListExtra("products");
//		Log.d(TAG + " get data", products.toString());
		
		index = getIntent().getIntExtra("index", -1);
		Log.d(TAG + " get index", index + "");
		
    	update = (Button) findViewById(R.id.update);
    	update.setOnClickListener(new ButtonClickListener());
    	
    	delete = (Button) findViewById(R.id.delete);
    	delete.setOnClickListener(new ButtonClickListener());
    	
    	name 	= (TextView) findViewById(R.id.name);
    	fields 	= (TextView) findViewById(R.id.fields);
    	regular = (TextView) findViewById(R.id.regular);
    	sale 	= (TextView) findViewById(R.id.sale);
    	colors 	= (TextView) findViewById(R.id.colors);
    	stores 	= (TextView) findViewById(R.id.stores);
    	description = (TextView) findViewById(R.id.description);
    	
    	image = (ImageView) findViewById(R.id.image);
    	image.setOnClickListener(new ButtonClickListener());
    	image.setScaleType(ImageView.ScaleType.FIT_CENTER);
    	
    	if(products != null && index > -1)
    		setShowInformation(index);
		
		fragmentManager = getFragmentManager();
		
		handler = new Handler();
    	
	}
	

    /**
     * to set the values of the product to views
     */
	private void setShowInformation(int index){
		
		Product product = products.get(index);
		
    	name.setText(	titles[0] + product.getName());
    	fields.setText(	titles[1] + product.getFields());
    	regular.setText(titles[2] + product.getRegular_price());
    	sale.setText(	titles[3] + product.getSale_price());
    	colors.setText(	titles[4] + showColors(product.getColors()));
    	stores.setText(	titles[5] + showStores(product.getStores()));
    	description.setText(titles[6] + product.getDescription());
    	
    	String imageUrl = product.getProduct_photo_url();
    	
    	if( imageUrl != null){
    		new ImageWork(DetailedProductActivity.this).execute(
    				(product.getProduct_photo_url()).trim());
    	}
		
	};
	

    /**
     * to return colors text from colors array
     */
	private String showColors(int[] intArray ){
		
		StringBuilder sb = new StringBuilder();
		if(intArray != null){
			for (int i = 0; i < intArray.length; i++) {
				sb.append(colorNames[intArray[i]]);
				
				if(i < intArray.length - 1)
					sb.append(", ");
			}	
			
			return sb.toString();
		}

		return null;
	}

	
    /**
     * to return stores text from dictionary
     */
	private String showStores(Dictionary<Integer, String> dic ){
		
		StringBuilder sb = null;
		
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
		
		if(sb == null)
			sb = new StringBuilder("");
		
		return sb.toString();
	}
	

    /**
     * to setup a image on image UI
     */
	protected void setImage(Bitmap bitmap){
		
		this.bitmap = bitmap;
		
		if(bitmap != null)
			image.setImageBitmap(bitmap);
		else
			image.setImageResource(R.drawable.ic_launcher);
	}

	
    /**
     * to control buttons in context
     */
    private class ButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			
				case R.id.update: updateProduct();
					break;

				case R.id.delete: deleteProduct();
					break;

				case R.id.image: enlargeImage();
					break;
			}
		}
    }	
	
	
    /**
     * to delete a product in the database
     */
	private void deleteProduct(){
		
		boolean isDelete = ((MainActivity) MainActivity.context).delete(products.get(index), index);
		
		if(index > 0){
			products.remove(index);
			index--;
		}
		
		show_root.setVisibility(View.INVISIBLE);
		
		if(isDelete)
			Toast.makeText(DetailedProductActivity.this, "Deleted the product!", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(DetailedProductActivity.this, "Error during removing the product!", Toast.LENGTH_SHORT).show();
	}
	
	
    /**
     * to make a dialog to update a product
     */
	private void updateProduct(){
		
		CreateProductDialog createProduct = new CreateProductDialog(this, products.get(index));
		createProduct.show(fragmentManager, "Update the product");

		Log.d(TAG, "Clicked");
	};
	
	

    /**
     * to update a product in the database
     */
	protected void doUpdate(Product product){
		
		isUpdate = ((MainActivity) MainActivity.context).updateProduct(product);
		
		int id;
		
		if(isUpdate){
			
			id = product.get_id();
			
			for (int i = 0; i < products.size(); i++) {
				if((products.get(i)).get_id() == id){
					id = i;
					break;
				}
			}
			
			products.set(id, product);
			
			setShowInformation(index);
		}
		
		handler.postDelayed(new Runnable() { // to control time
			
			@Override
			public void run() {

				if(isUpdate)
					Toast.makeText(DetailedProductActivity.this, "Updated the product!", Toast.LENGTH_SHORT).show();
				else
					Toast.makeText(DetailedProductActivity.this, "Error during updating the product!", Toast.LENGTH_SHORT).show();
			}
		}, 1000);
	}

	
    /**
     * to show the original image
     */
	private void enlargeImage(){
		
		FullImageDialog fullImage = new FullImageDialog(this, bitmap);
		fullImage.show(fragmentManager, "Full image");

		Log.d(TAG, "Clicked");
	};
	
	
    /**
     * to change UI component when a user' finger moves on the device's screen
     */
	private void changeProduct() {

		if (touchLength > 0) {

			if (index == 0)
				index = products.size() - 1;
			else
				index--;

		} else {

			if (index == products.size() - 1)
				index = 0;
			else
				index++;
		}

		if( index > -1)
			setShowInformation(index);
		else
			Toast.makeText(this, "No more data!", Toast.LENGTH_SHORT).show();
	}

	
	@Override
	public boolean dispatchTouchEvent(MotionEvent motionevent) {

		switch (motionevent.getAction()) {

			case MotionEvent.ACTION_DOWN:
				
				start = motionevent.getX();
				break;

			case MotionEvent.ACTION_UP:

				end = motionevent.getX();
				touchLength = end - start;
				
				Log.d(TAG + " touch event", touchLength+"");
				
				if (Math.abs(touchLength) > 50){
					show_root.setVisibility(View.VISIBLE);
					changeProduct();
				}
				break;
		}

		return super.dispatchTouchEvent(motionevent);
	}	
}
