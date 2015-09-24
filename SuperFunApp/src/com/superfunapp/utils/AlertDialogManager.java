package com.superfunapp.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.fragments.TicketPoints;

public class AlertDialogManager {

	private Fragment fragment;

	private FragmentManager fm;

	private FragmentTransaction ft;

	private Context ctx;
	
	//private String fragmentName;

	public void showAlertDialog(Context context, String title, String message) {

		ctx = context;
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);

		//this.fragmentName = fragmentNm;
		// Setting Dialog Title
		alertDialog.setTitle(title);

		// Setting Dialog Message
		alertDialog.setMessage(message);

		// Setting OK Button
		alertDialog.setPositiveButton(context.getResources().getString(R.string.ok),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//changeFragment(fragmentName);
						dialog.dismiss();
					}
				});

		// Showing Alert Message
		
		alertDialog.show();
	}

	public void changeFragment(String fragName) {

		if (fragName.equals("tickets_points")) {
			
			fragment = new TicketPoints();
			fm = ((HomeScreen) ctx).getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.addToBackStack(fm.getClass().getName());
			ft.commit();
		}

	}
}
