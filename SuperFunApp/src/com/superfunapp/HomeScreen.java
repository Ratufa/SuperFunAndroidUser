package com.superfunapp;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings.SettingNotFoundException;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.superfunapp.fragments.BuyTickets;
import com.superfunapp.fragments.CompanyList;
import com.superfunapp.fragments.ContactUs;
import com.superfunapp.fragments.GiftsList;
import com.superfunapp.fragments.HowItFragment;
import com.superfunapp.fragments.PointsCollector;
import com.superfunapp.fragments.PrivacyFragment;
import com.superfunapp.fragments.PurchaseHistory;
import com.superfunapp.fragments.SettingFragment;
import com.superfunapp.fragments.TermFragment;
import com.superfunapp.fragments.TicketPoints;
import com.superfunapp.fragments.TicketsCollector;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class HomeScreen extends ActionBarActivity implements OnClickListener {
	
	// Button
	private Button buttonTicket;
	private Button buttonCollect;
	private Button buttonHistory;
	private DisplayMetrics dimension;
	private int width = 0, count;
	private Fragment fragment;
	private FragmentManager fm;
	private FragmentTransaction ft;
	private PopupWindow popupShareInvite;
	private ImageView backImage;
	public static Activity activity;
	private ImageView menuImage;
	private boolean fromCompany = false, fromHow = false;

	private int brightness = 255;
	//Content resolver used as a handle to the system's settings
	private ContentResolver cResolver;
	//Window object, that will store a reference to the current window
	private Window window;

	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        activity = this;

		if (SharedPrefrnceSuperFun.getSharedPrefData(this,
				Constants.USER_LANGAUGE) != null) {
		} else {
			SharedPrefrnceSuperFun.setDataInSharedPrefrence(this,
					Constants.USER_LANGAUGE, "ar");//sahu
		}
		//SharedPrefrnceSuperFun.setDataInSharedPrefrence(this, Constants.USER_MOB, "66600805");
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(this,
				Constants.USER_LANGAUGE), this);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.main_screen);
		Utility.setThreadPolicy();

		fm = getSupportFragmentManager();
		buttonCollect = (Button) findViewById(R.id.collectBtn);
		buttonHistory = (Button) findViewById(R.id.historyBtn);
		buttonTicket = (Button) findViewById(R.id.buyBtn);

		backImage = (ImageView) findViewById(R.id.cancelImage);
		menuImage = (ImageView) findViewById(R.id.menuImage);

		buttonTicket.setSelected(true);
		buttonHistory.setSelected(false);
		buttonCollect.setSelected(false);

		if (SharedPrefrnceSuperFun.getSharedPrefData(this,
				Constants.USER_LANGAUGE).equals("ar")) {
			Utility.setMogaMaddySolemanFont(buttonCollect, this);
			Utility.setMogaMaddySolemanFont(buttonHistory, this);
			Utility.setMogaMaddySolemanFont(buttonTicket, this);
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ticket_arabic_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.log_arabic_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.collect_arabic_selector));
		} else {
			Utility.setTextGabbalandFont(buttonHistory, this);
			Utility.setTextGabbalandFont(buttonCollect, this);
			Utility.setTextGabbalandFont(buttonTicket, this);
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ticket_english_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.log_english_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.collect_english_selector));
		}

		dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		width = dimension.widthPixels;

		if (getIntent().getExtras() != null) {

			if (getIntent().getBooleanExtra("how_it", false)) {
				fromHow = true;
				fragment = new HowItFragment();
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.addToBackStack(fm.getClass().getName());
				ft.commit();
			} else if (getIntent().getBooleanExtra("company", false)) {
				fromCompany = true;
				fragment = new CompanyList();
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.addToBackStack(fm.getClass().getName());
				ft.commit();
			} else if (getIntent().getBooleanExtra("terms", false)) {
				fragment = new TermFragment();
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.addToBackStack(fm.getClass().getName());
				ft.commit();
			} else if (getIntent().getBooleanExtra("privacy", false)) {
				fragment = new PrivacyFragment();
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.addToBackStack(fm.getClass().getName());
				ft.commit();
			}

		} else {

			if (savedInstanceState == null) {
				// On home screen load fragment view
				fragment = new BuyTickets();
				FragmentTransaction ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment, "check");
				ft.addToBackStack(fm.getClass().getName());
				ft.commit();
			}
		}

		buttonCollect.setOnClickListener(this);
		buttonHistory.setOnClickListener(this);
		buttonTicket.setOnClickListener(this);
		backImage.setOnClickListener(this);
		menuImage.setOnClickListener(this);
		
		
		//Get the content resolver
        cResolver = getContentResolver();
        //Get the current window
        window = getWindow();
        try 
        {
        	//Get the current system brightness
        	brightness = android.provider.Settings.System.getInt(cResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS);
        	//Set the system brightness using the brightness variable value
        	android.provider.Settings.System.putInt(cResolver, android.provider.Settings.System.SCREEN_BRIGHTNESS, brightness);
			//Get the current window attributes
			LayoutParams layoutpars = window.getAttributes();
			//Set the brightness of this window
			layoutpars.screenBrightness = brightness / (float)100;
			//Apply attribute changes to this window
			window.setAttributes(layoutpars);
		} 
        catch (SettingNotFoundException e) 
		{
        	//Throw an error case it couldn't be retrieved
			Log.e("Error", "Cannot access system brightness");
			e.printStackTrace();
		}

	}

	public void setUpActionBar() {
		getSupportActionBar().setIcon(R.drawable.close);
		getSupportActionBar().setDisplayHomeAsUpEnabled(false);
		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayShowHomeEnabled(true);
		getSupportActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.header));
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		
		if(Constants.OptionMenuOpen == true){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
		}
		return super.onCreateOptionsMenu(menu);
	}*/

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		menu.clear();
		if(Constants.OptionMenuOpen == true){
			MenuInflater inflater = getMenuInflater();
			inflater.inflate(R.menu.main, menu);
		}
		// if nav drawer is opened, hide the action items
		//	menu.findItem(R.id.action_settings).setVisible(true);
		//	popupWindowDialog();
		return super.onPrepareOptionsMenu(menu);
	}

	/*@Override
	public boolean onCreatePanelMenu(int featureId, Menu menu) {
	    popupWindowDialog();
	    return false;
	}*/
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle presses on the action bar items
		switch (item.getItemId()) {
		case R.id.action_settings:
			if(Constants.OptionMenuOpen == true)
				popupWindowDialog();
			return true;
		case android.R.id.home:

			count = fm.getBackStackEntryCount();
			if (count == 1) {
				super.onBackPressed();
			} else {
				showAlert();
			}
			return true;

		default:
			//popupWindowDialog();
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.buyBtn:
			//			buttonHistory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			//			buttonTicket.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
			//					R.drawable.page_indicator);
			//			buttonCollect.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			buttonTicket.setSelected(true);
			buttonHistory.setSelected(false);
			buttonCollect.setSelected(false);

			fragment = new BuyTickets();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.cancelImage:
			/*count = fm.getBackStackEntryCount();
			if (count == 1) {
				super.onBackPressed();
			} else if (fromCompany || fromHow) {
				super.onBackPressed();
			} else {
				showAlert();
			}*/
			if(SharedPrefrnceSuperFun.getSharedPrefData(HomeScreen.this, "entry_way").equalsIgnoreCase("true")){
				fragment = new BuyTickets();
				fm = getSupportFragmentManager();
				ft = fm.beginTransaction();
				ft.replace(R.id.content_frame, fragment);
				ft.addToBackStack(fm.getClass().getName());
				ft.commit();
			}else{
				finish();
			}
			
			break;
		case R.id.menuImage:
			popupWindowDialog();//sahu
			break;
		case R.id.historyBtn:

			//			buttonHistory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
			//					R.drawable.page_indicator);
			//			buttonTicket.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			//			buttonCollect.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			buttonTicket.setSelected(false);
			buttonHistory.setSelected(true);
			buttonCollect.setSelected(false);

			fragment = new PurchaseHistory();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.collectBtn:
			//			buttonHistory.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			//			buttonTicket.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
			//			buttonCollect.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0,
			//					R.drawable.page_indicator);
			buttonTicket.setSelected(false);
			buttonHistory.setSelected(false);
			buttonCollect.setSelected(true);

			fragment = new TicketPoints();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.settingsBtn:
			popupShareInvite.dismiss();
			fragment = new SettingFragment();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.participatingBtn:
			popupShareInvite.dismiss();
			fragment = new CompanyList();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.howItWorksBtn:
			popupShareInvite.dismiss();
			fragment = new HowItFragment();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.termsBtn:
			popupShareInvite.dismiss();
			fragment = new TermFragment();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.privacyBtn:
			popupShareInvite.dismiss();
			fragment = new PrivacyFragment();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.contactBtn:
			popupShareInvite.dismiss();
			fragment = new ContactUs();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;
		case R.id.giftsListBtn:
			popupShareInvite.dismiss();
			fragment = new GiftsList();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			//ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			break;

		default:
			break;
		}
	}

	@SuppressLint("InflateParams")
	private void popupWindowDialog() {

		popupShareInvite = new PopupWindow(HomeScreen.this);
		View layout = getLayoutInflater().inflate(R.layout.popup_window, null);

		View action = findViewById(R.id.mainLayout);

		popupShareInvite.setContentView(layout);

		popupShareInvite.setHeight(dimension.heightPixels * 2 / 3);
		popupShareInvite.setWidth(width * 2 / 3);

		popupShareInvite.setOutsideTouchable(true);
		popupShareInvite.setFocusable(true);
		popupShareInvite.setBackgroundDrawable(new ColorDrawable(
				android.graphics.Color.TRANSPARENT));
		popupShareInvite.showAtLocation(action, Gravity.RIGHT | Gravity.TOP, 0,
				dimension.heightPixels / 8);
		// Show anchored to button
		// popupShareInvite.setBackgroundDrawable(getResources().getDrawable(R.drawable.pop_list));
		// popupShareInvite.showAsDropDown(menuImage);

		TextView settingBtn = (TextView) layout.findViewById(R.id.settingsBtn);
		TextView howItBtn = (TextView) layout.findViewById(R.id.howItWorksBtn);
		TextView partBtn = (TextView) layout
				.findViewById(R.id.participatingBtn);
		TextView giftBtn = (TextView) layout.findViewById(R.id.giftsListBtn);
		TextView termsBtn = (TextView) layout.findViewById(R.id.termsBtn);
		TextView privacyBtn = (TextView) layout.findViewById(R.id.privacyBtn);
		TextView contactBtn = (TextView) layout.findViewById(R.id.contactBtn);

		settingBtn.setOnClickListener(this);
		howItBtn.setOnClickListener(this);
		partBtn.setOnClickListener(this);
		giftBtn.setOnClickListener(this);
		termsBtn.setOnClickListener(this);
		privacyBtn.setOnClickListener(this);
		contactBtn.setOnClickListener(this);

	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		TicketsCollector.counter=30;
		PointsCollector.counter=30;
		
		if(SharedPrefrnceSuperFun.getSharedPrefData(HomeScreen.this, "entry_way").equalsIgnoreCase("true")){
			fragment = new BuyTickets();
			fm = getSupportFragmentManager();
			ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			ft.addToBackStack(fm.getClass().getName());
			ft.commit();
			
			count = fm.getBackStackEntryCount();
			Log.e("count>>>", count + ">>>");
			if (fromCompany || fromHow) {
				super.onBackPressed();
				Intent in = new Intent(HomeScreen.this, StartScreenActivity.class);
				startActivity(in);
				finish();
			} else {
				if(Constants.mainActivityDisplay == true)
				   showAlert();
			}
		}else{
			finish();
		}
		
		/*count = fm.getBackStackEntryCount();
		Log.e("count>>>", count + ">>>");
		if (fromCompany || fromHow) {
			super.onBackPressed();
			Intent in = new Intent(HomeScreen.this, StartScreenActivity.class);
			startActivity(in);
			finish();
		} else {
			showAlert();
		}*/
	}

	public void showAlert() {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		// set title
		alertDialogBuilder.setTitle("Super Fun");
		// set dialog message
		alertDialogBuilder
		.setMessage(
				getResources().getString(R.string.Exit_Super_Fun_App))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, close
						// current activity
						overridePendingTransition(
								R.anim.trans_right_in,
								R.anim.trans_right_out);
						finish();

					}
				})
				.setNegativeButton(getResources().getString(R.string.no),
						new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						// if this button is clicked, just close
						// the dialog box and do nothing
						dialog.cancel();
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	
}
