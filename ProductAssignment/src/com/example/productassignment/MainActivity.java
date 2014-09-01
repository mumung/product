package com.example.productassignment;

/**
 * Main Activity for creating and saving a product and showing a list of products
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import java.util.ArrayList;

import org.json.JSONException;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.model.Product;
import com.example.sqlite3.DB_Manager;


public class MainActivity extends Activity {

	final String TAG = "Main";
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        initialize();

    }
 
    
    /*
     * Declare variable and initialize them
     */
    public static Context context;
    
    RelativeLayout main_root;
    
    Button create, show;
    ListView productListView;
    
    ArrayList<Product> products;
    ArrayAdapter<Product> adapter;

    int currentPosition;
    
    DB_Manager DM;
    
	FragmentManager fragmentManager;
	
	Handler handler;
    
    private void initialize(){
    	
    	context = this;
    	
    	main_root = (RelativeLayout) findViewById(R.id.main_root);
    	
    	create = (Button) findViewById(R.id.create);
    	create.setOnClickListener(new ButtonClickListener());
    	
    	show = (Button) findViewById(R.id.show);
    	show.setOnClickListener(new ButtonClickListener());
    	
    	productListView = (ListView) findViewById(R.id.product_list);
    	
    	products  = new ArrayList<Product>(); 

    	DM = new DB_Manager(this);
    	
    	if(DM.getProductCount() == 0 )    		
    		mock_dataSetup();	//for setting mock data to database

    	products = DM.getAllProducts();
		showProductsList();
		Log.d(TAG + " products", products.toString());
    	
		fragmentManager = getFragmentManager();
		
		handler = new Handler();
    }

    
    /**
     * to setup UI components when the database has one more record.
     */
    private void showProductsList(){
    	adapter = new ArrayAdapter<Product>(this, android.R.layout.simple_list_item_single_choice, products){
			
			TextView item;
			Product product;

			@Override
			public View getView(int position, View convertView, ViewGroup parent) {

				View view = super.getView(position, convertView, parent);
				
				product = products.get(position) ;
				
				item = (TextView) view.findViewById(android.R.id.text1);
				item.setTextColor(Color.parseColor("#D0D0D0"));
				item.setText(product.getFields() + "\n" + product.getName());
			
				return view;
			}
		};
		
    	productListView.setAdapter(adapter);
    	productListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
    	productListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				currentPosition = position;
				Log.d(TAG + " index", currentPosition + "");
				
			}
		});

    }
  
    
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		DM.close();
	}
	
	
    /**
     * to insert a product to the database.
     */
	protected int insertProduct(Product product){

		int id = (int) DM.insertProduct(product);
		product.set_id(id);
		products.add(product);
		adapter.notifyDataSetChanged();
		
		handler.postDelayed(new Runnable() {
			
			@Override
			public void run() {
				Toast.makeText(MainActivity.this, "Created the product!", Toast.LENGTH_SHORT).show();
			}
		}, 1000);

		return id;
	}

	
    /**
     * to insert a product to the database.
     */
	protected boolean updateProduct(Product product){
		
		boolean isUpdate = DM.updateProduct(product);
		boolean isContain = false;
		
		int id = product.get_id();
		
		for (int i = 0; i < products.size(); i++) {
			if((products.get(i)).get_id() == id){
				id = i;
				isContain = true;
				break;
			}
		}
		
		if(isContain){
			products.set(id,product);
			adapter.notifyDataSetChanged();
		}
		
		return isUpdate;
	}

	
    /**
     * to get a product from the database.
     */
	protected Product selectProduct(long id){
		
		return DM.selectProduct(id);
	}	
	
	
    /**
     * to delete a product in the database.
     */
	protected boolean delete(Product product, int index){
		
		if(index > -1){
			products.remove(index);
			adapter.notifyDataSetChanged();
			
			if(currentPosition != 0)
				currentPosition--;
			
			productListView.setSelection(currentPosition);
		}
		
		return DM.deleteProduct(product);
	}

	
    /**
     * to control buttons in context
     */
    private class ButtonClickListener implements View.OnClickListener{

		@Override
		public void onClick(View v) {
			
			switch(v.getId()){
			
				case R.id.create: createProduct();
					break;
					
				case R.id.show:
					Intent intent = new Intent(MainActivity.this, DetailedProductActivity.class);
					intent.putExtra("products", products);
					intent.putExtra("index", currentPosition);
					startActivity(intent);
					break;
			}
		}
    }
 
    
    /**
     * to make a dialog for creating a product
     */
    private void createProduct(){
    	
		CreateProductDialog createProduct = new CreateProductDialog(this);
		createProduct.show(fragmentManager, "Create a product");

		Log.d(TAG, "Clicked");
    };
    
    
    /**
     * to setup mock_data
     */
    private void mock_dataSetup(){
    	
    	// mock_data 1
    	StringBuilder sb = new StringBuilder();
    	sb.append("[{\"fields\":\"Fileds A\",\"name\":\"Name B\",\"description\":\"Description C\",");
    	sb.append("\"regular_price\":\"10000.50\",\"sale_price\":\"15000.00\",");
    	sb.append("\"product_photo_url\":\"http://4.bp.blogspot.com/-jQY5In8MXkk/TxElCgmBnTI/AAAAAAAAAS0/EQe8oD1J4yE/s400/free_car_wallpapers+6.jpg\",");
    	
    	sb.append("\"colors\":[1,2],\"stores\":{\"1\":\"Kmart\", \"2\":\"Target\"}},");
    	
    	// mock_data 2
    	sb.append("{\"fields\":\"Fileds D\",\"name\":\"Name E\",\"description\":\"Description F\",");
    	sb.append("\"regular_price\":\"20000.50\",\"sale_price\":\"25000.00\",");
    	sb.append("\"product_photo_url\":\"http://1.bp.blogspot.com/-r_nEnykqinQ/TxEk7syF8wI/AAAAAAAAASM/ok-3d8RESAs/s400/free_car_wallpapers+1.jpg\",");
    	sb.append("\"colors\":[3,4],\"stores\":{\"1\":\"Walmart\", \"2\":\"Toms Hardware\"}},");
    	
    	// mock_data 3
    	sb.append("{\"fields\":\"Fileds G\",\"name\":\"Name H\",\"description\":\"Description I\",");
    	sb.append("\"regular_price\":\"30000.00\",\"sale_price\":\"45000.00\",");
    	sb.append("\"product_photo_url\":\"http://1.bp.blogspot.com/-NYjliAYgrGw/TxEk88X7K5I/AAAAAAAAASU/wICkvYLuXfo/s400/free_car_wallpapers+2.jpg\",");
    	sb.append("\"colors\":[5,6],\"stores\":{\"1\":\"G Mart\", \"2\":\"Fresh Mart\"}},");


    	// mock_data 4
    	sb.append("{\"fields\":\"Fileds J\",\"name\":\"Name K\",\"description\":\"Description L\",");
    	sb.append("\"regular_price\":\"15000.00\",\"sale_price\":\"20000.00\",");
    	sb.append("\"product_photo_url\":\"http://3.bp.blogspot.com/-gOMPYd4ZoMI/TxEk6THRCWI/AAAAAAAAASE/WvnBiSYKx60/s400/free_car_wallpapers.jpg\",");
    	sb.append("\"colors\":[2,5],\"stores\":{\"1\":\"Target\", \"2\":\"G Mart\"}},"); 	

    	// mock_data 5
    	sb.append("{\"fields\":\"Fileds M\",\"name\":\"Name N\",\"description\":\"Description O\",");
    	sb.append("\"regular_price\":\"25000.0\",\"sale_price\":\"30000.0\",");
    	sb.append("\"product_photo_url\":\"http://4.bp.blogspot.com/-I3SbYtt1FEU/TxEk-9pooqI/AAAAAAAAASc/zSSAfZ2k3Pg/s400/free_car_wallpapers+3.jpg\",");
    	sb.append("\"colors\":[1,4],\"stores\":{\"1\":\"Kmart\", \"2\":\"Fresh Mart\"}}]"); 	
    	
    	
    	ArrayList<Product> products_mock_data = new ArrayList<Product>();
		
		try {
			
			products_mock_data = ProductUtil.ProductsJSONParser.parseProducts(sb.toString());
			
		} catch (JSONException e) {

			e.printStackTrace();
		}
		
		for(Product product: products_mock_data){
			insertMockDataProduct(product);
		}
			
		
//		Log.d(TAG + "mock_data", sb.toString() );		
//		Log.d(TAG + "Json to Prodcut", products_mock_data.toString());
    	
    }
    
    /**
     * to insert a mock product to the database.
     */
	protected void insertMockDataProduct(Product product){

		DM.insertProduct(product);

	}
}




