package com.example.productassignment;

/**
 * FullImageDialog for showing a raw image
 * @version 1.0 1 Sept 2014
 * @author Kye Sung Park 
 */

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;


public class FullImageDialog extends DialogFragment {

    /*
     * Declare variable and initialize them
     */
	final String TAG = "FullImageDialog";
	
	LayoutInflater inflater;
	View view;
	ImageView image;

	AlertDialog.Builder builder;

	Context context;
	Bitmap bitmap;

	public FullImageDialog(Context context, Bitmap bitmap) {
		this.context = context;
		this.bitmap = bitmap;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {

		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		view = inflater.inflate(R.layout.full_image, ((DetailedProductActivity) context).show_root, false);

		builder = new AlertDialog.Builder(getActivity());
		builder.setView(view);
		
		image = (ImageView) view.findViewById(R.id.fullimage);
		image.setImageBitmap(bitmap);

		builder.setCancelable(false).setPositiveButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int id) {

				Log.d(TAG, "dissmiss full image dialog");
			}
		});

		AlertDialog dialog = builder.create();

		dialog.getWindow().setGravity(Gravity.CENTER);

		return dialog;
	}

	@Override
	public void dismiss() {

		super.dismiss();
	}

}
