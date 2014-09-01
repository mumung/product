package com.example.productassignment;

/**
 * ImageWork for downloading image data from Internet
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

public class ImageWork extends AsyncTask<String, Void, Bitmap> {

    /*
     * Declare variable and initialize them
     */
	private Context context;

	private ProgressDialog progressDialog;

	String imgUrl;

	public ImageWork(Context context) {

		super();
		this.context = context;
	}

	@Override
	protected Bitmap doInBackground(String... params) {

		imgUrl = params[0];

		Bitmap image = null;
		
		try {
			URL url = new URL(imgUrl);
			image = BitmapFactory.decodeStream(url.openStream());
		}
		catch (MalformedURLException e) {

			e.printStackTrace();
		}
		catch (IOException e) {

			e.printStackTrace();
		}

		return image;
	}

	@Override
	protected void onPreExecute() {

		super.onPreExecute();

		progressDialog = new ProgressDialog(context);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Downloading a image..");
		progressDialog.show();

	}

	@Override
	protected void onPostExecute(Bitmap result) {
		
		progressDialog.dismiss();
		((DetailedProductActivity) context).setImage(result);
		
	}
}