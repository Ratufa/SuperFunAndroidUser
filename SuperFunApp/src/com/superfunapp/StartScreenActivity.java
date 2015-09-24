package com.superfunapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.superfunapp.adapters.SpinnerAdapter;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class StartScreenActivity extends Activity implements OnClickListener {
	// Button
	private Button howItBtn;
	private Button participatingCompBtn;
	private Button changeLangauageBtn;
	private Button loginBtn;
	private Button signUpBtn;
	private Intent intent;
	public static Activity startScreen;
	private Spinner spinLanguage;
	private TextView dontHaveText;
	private TextView titleTv;
	//private String[] languages = null;
	private List<String> spinLanguageList;
	static int pos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		if (SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE) != null) {

		} else {
			SharedPrefrnceSuperFun.setDataInSharedPrefrence(this, Constants.USER_LANGAUGE, "ar");//sahu
		}

		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE), this);

		if (SharedPrefrnceSuperFun.getSharedPrefData(this, "is_logged_in") != null) {
			if (SharedPrefrnceSuperFun.getSharedPrefData(this, "is_logged_in").equals("true")) {
				intent = new Intent(this, HomeScreen.class);
				startActivity(intent);
				overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
				finish();
			}
		} else {

			spinLanguageList = new ArrayList<String>();

			//		spinLanguageList.add(getResources().getString(R.string.change_lang));
			spinLanguageList.add(getResources().getString(R.string.arabic));
			spinLanguageList.add(getResources().getString(R.string.english));

			setContentView(R.layout.start_screen);
			startScreen = this;
			howItBtn = (Button) findViewById(R.id.howItBtn);
			participatingCompBtn = (Button) findViewById(R.id.participatingCompBtn);
			changeLangauageBtn = (Button) findViewById(R.id.changeLangBtn);
			loginBtn = (Button) findViewById(R.id.loginBtn);
			signUpBtn = (Button) findViewById(R.id.signUpBtn);
			dontHaveText = (TextView) findViewById(R.id.dontHaveText);
			titleTv = (TextView) findViewById(R.id.titleText);

			if(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE).equalsIgnoreCase("ar")){
				Utility.changeLangauge("ar", startScreen);
				System.out.println("----1111arrrrrr---"+SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE));
				spinLanguageList = new ArrayList<String>();

				//			spinLanguageList.add(getResources().getString(R.string.change_lang));
				spinLanguageList.add(getResources().getString(R.string.arabic));
				spinLanguageList.add(getResources().getString(R.string.english));

				//	spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));

				howItBtn.setText(getResources().getString(R.string.how_to));
				Utility.setTextGabbalandFont(dontHaveText, StartScreenActivity.this);
				participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
				signUpBtn.setText(getResources().getString(R.string.sign_up));
				loginBtn.setText(getResources().getString(R.string.login_string));
				changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
				dontHaveText.setText(getResources().getString(R.string.dont_have_new));
				titleTv.setText(getResources().getString(R.string.app_name));
			}else{
				System.out.println("----1111ennnnn---"+SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE));
				Utility.changeLangauge("en", startScreen);
				howItBtn.setText(getResources().getString(R.string.how_to));
				Utility.setTextHelveticaBold(dontHaveText, StartScreenActivity.this);

				spinLanguageList = new ArrayList<String>();

				//			spinLanguageList.add(getResources().getString(R.string.change_lang));
				spinLanguageList.add(getResources().getString(R.string.arabic));
				spinLanguageList.add(getResources().getString(R.string.english));

				//		spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));
				participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
				signUpBtn.setText(getResources().getString(R.string.sign_up));
				loginBtn.setText(getResources().getString(R.string.login_string));
				changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
				dontHaveText.setText(getResources().getString(R.string.dont_have_new));
				titleTv.setText(getResources().getString(R.string.app_name));
			}

			Utility.setTextMogaFont(loginBtn, this);
			Utility.setTextMogaFont(signUpBtn, this);
			Utility.setTextGabbalandFont(titleTv, this);
			Utility.setTextGabbalandFont(changeLangauageBtn, this);
			Utility.setTextGabbalandFont(howItBtn, this);
			Utility.setTextGabbalandFont(participatingCompBtn, this);
			spinLanguage = (Spinner) findViewById(R.id.spinLangauge);
			spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));

			if (SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE) != null) {
				if(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE).equalsIgnoreCase("ar")){
					spinLanguage.setSelection(0);
				}else{
					spinLanguage.setSelection(1);
				}
			} else {
				SharedPrefrnceSuperFun.setDataInSharedPrefrence(this, Constants.USER_LANGAUGE, "ar");//sahu
			}

			spinLanguage.setOnItemSelectedListener(new OnItemSelectedListener() {

				@Override
				public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
					// TODO Auto-generated method stub
					//changeLangauageBtn.setText(spinLanguageList.get(position) + "");
					if(position == 0){
						System.out.println("-----0000");
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "ar");
						Utility.changeLangauge("ar", startScreen);
						howItBtn.setText(getResources().getString(R.string.how_to));
						Utility.setTextHelveticaBold(dontHaveText, StartScreenActivity.this);
						/*			SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "en");
					Utility.changeLangauge("en", startScreen);
					howItBtn.setText(getResources().getString(R.string.how_to));
					Utility.setTextHelveticaBold(dontHaveText, StartScreenActivity.this);

					spinLanguageList = new ArrayList<String>();

		//			spinLanguageList.add(getResources().getString(R.string.change_lang));
					spinLanguageList.add(getResources().getString(R.string.english));
					spinLanguageList.add(getResources().getString(R.string.arabic));

					spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));*/

						participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
						signUpBtn.setText(getResources().getString(R.string.sign_up));
						loginBtn.setText(getResources().getString(R.string.login_string));
						changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
						dontHaveText.setText(getResources().getString(R.string.dont_have_new));
						titleTv.setText(getResources().getString(R.string.app_name));
					}
					if(position == 1){
						System.out.println("-----1111");
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "en");
						Utility.changeLangauge("en", startScreen);
						/*SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "ar");
						Utility.changeLangauge("ar", startScreen);

						spinLanguageList = new ArrayList<String>();

			//			spinLanguageList.add(getResources().getString(R.string.change_lang));
						spinLanguageList.add(getResources().getString(R.string.english));
						spinLanguageList.add(getResources().getString(R.string.arabic));

						spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));*/

						howItBtn.setText(getResources().getString(R.string.how_to));
						Utility.setTextGabbalandFont(dontHaveText, StartScreenActivity.this);
						participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
						signUpBtn.setText(getResources().getString(R.string.sign_up));
						loginBtn.setText(getResources().getString(R.string.login_string));
						changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
						dontHaveText.setText(getResources().getString(R.string.dont_have_new));
						titleTv.setText(getResources().getString(R.string.app_name));
					}
					if(position == 2)
						System.out.println("-----22222");
					/*if (pos == 1) {
						System.out.println("-----111111");
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "en");
						Utility.changeLangauge("en", startScreen);
						howItBtn.setText(getResources().getString(R.string.how_to));
						Utility.setTextHelveticaBold(dontHaveText, StartScreenActivity.this);

						spinLanguageList = new ArrayList<String>();

			//			spinLanguageList.add(getResources().getString(R.string.change_lang));
						spinLanguageList.add(getResources().getString(R.string.english));
						spinLanguageList.add(getResources().getString(R.string.arabic));

						spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));

						participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
						signUpBtn.setText(getResources().getString(R.string.sign_up));
						loginBtn.setText(getResources().getString(R.string.login_string));
						changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
						dontHaveText.setText(getResources().getString(R.string.dont_have_new));
						titleTv.setText(getResources().getString(R.string.app_name));
					}
					if (position == 2) {
						System.out.println("-----222222");
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "ar");
						Utility.changeLangauge("ar", startScreen);

						spinLanguageList = new ArrayList<String>();

			//			spinLanguageList.add(getResources().getString(R.string.change_lang));
						spinLanguageList.add(getResources().getString(R.string.english));
						spinLanguageList.add(getResources().getString(R.string.arabic));

						spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));

						howItBtn.setText(getResources().getString(R.string.how_to));
						Utility.setTextGabbalandFont(dontHaveText, StartScreenActivity.this);
						participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
						signUpBtn.setText(getResources().getString(R.string.sign_up));
						loginBtn.setText(getResources().getString(R.string.login_string));
						changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
						dontHaveText.setText(getResources().getString(R.string.dont_have_new));
						titleTv.setText(getResources().getString(R.string.app_name));
					}*/
				}

				@Override
				public void onNothingSelected(AdapterView<?> parent) {
					// TODO Auto-generated method stub

				}
			});

			howItBtn.setOnClickListener(this);
			participatingCompBtn.setOnClickListener(this);
			changeLangauageBtn.setOnClickListener(this);
			loginBtn.setOnClickListener(this);
			//signUpBtn.setOnClickListener(this);

			signUpBtn.setOnTouchListener(new OnTouchListener() {        
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					switch(event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						signUpBtn.setTextColor(Color.parseColor("#ECE2A2"));
						signUpBtn.setBackgroundResource(R.drawable.select_button);
						return true; // if you want to handle the touch event
					case MotionEvent.ACTION_UP:
						signUpBtn.setTextColor(Color.parseColor("#FFFFFF"));
						signUpBtn.setBackgroundResource(R.drawable.unselect_button);
						Intent intent = new Intent(StartScreenActivity.this, SignupActivity.class);
						startActivity(intent);
						overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
						finish();
						return true; // if you want to handle the touch event
					case MotionEvent.ACTION_OUTSIDE:
						signUpBtn.setTextColor(Color.parseColor("#FFFFFF"));
						signUpBtn.setBackgroundResource(R.drawable.unselect_button);
						Intent intent2 = new Intent(StartScreenActivity.this, SignupActivity.class);
						startActivity(intent2);
						overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
						finish();
						return true;
					}
					return false;
				}
			});
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.howItBtn:
			SharedPrefrnceSuperFun.setDataInSharedPrefrence(StartScreenActivity.this, "entry_way", "false");
			intent = new Intent(this, HomeScreen.class);
			intent.putExtra("how_it", true);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
			//finish();
			break;
		case R.id.participatingCompBtn:
			SharedPrefrnceSuperFun.setDataInSharedPrefrence(StartScreenActivity.this, "entry_way", "false");
			intent = new Intent(this, HomeScreen.class);
			intent.putExtra("company", true);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
			//finish();
			break;
		case R.id.changeLangBtn:
			spinLanguage.performClick();
			break;

		case R.id.signUpBtn:

			/*intent = new Intent(this, SignupActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
			finish();*/
			break;
		case R.id.loginBtn:
			intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
			break;
		default:
			break;
		}
	}

	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		// super.onBackPressed();
		showAlert();
	}

	public void showAlert() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle("Super Fun");

		// set dialog message
		alertDialogBuilder.setMessage(getResources().getString(R.string.Exit_Super_Fun_App)).setCancelable(false).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
				finish();

			}
		}).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {

				dialog.cancel();
			}
		});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	@Override
	protected void onResume() {
		super.onResume();

		howItBtn = (Button) findViewById(R.id.howItBtn);
		participatingCompBtn = (Button) findViewById(R.id.participatingCompBtn);
		changeLangauageBtn = (Button) findViewById(R.id.changeLangBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		signUpBtn = (Button) findViewById(R.id.signUpBtn);
		dontHaveText = (TextView) findViewById(R.id.dontHaveText);
		titleTv = (TextView) findViewById(R.id.titleText);
		spinLanguage = (Spinner) findViewById(R.id.spinLangauge);
		

		/*if (SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE) != null) {
			if(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE).equalsIgnoreCase("ar")){
				spinLanguage.setSelection(0);
				Utility.changeLangauge("ar", startScreen);
			}else{
				spinLanguage.setSelection(1);
				Utility.changeLangauge("en", startScreen);
			}
		} else {
			SharedPrefrnceSuperFun.setDataInSharedPrefrence(this, Constants.USER_LANGAUGE, "ar");//sahu
		}*/

		if(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE).equalsIgnoreCase("ar")){
			try {
				SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "ar");
				Utility.changeLangauge("ar", startScreen);
				System.out.println("----2222arrrrrr---");
				/*spinLanguageList = new ArrayList<String>();

	//			spinLanguageList.add(getResources().getString(R.string.change_lang));
				spinLanguageList.add(getResources().getString(R.string.arabic));
				spinLanguageList.add(getResources().getString(R.string.english));

				spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));*/

				howItBtn.setText(getResources().getString(R.string.how_to));
				Utility.setTextGabbalandFont(dontHaveText, StartScreenActivity.this);
				participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
				signUpBtn.setText(getResources().getString(R.string.sign_up));
				loginBtn.setText(getResources().getString(R.string.login_string));
				changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
				dontHaveText.setText(getResources().getString(R.string.dont_have_new));
				titleTv.setText(getResources().getString(R.string.app_name));
			} catch (Exception e) {
				// TODO: handle exception
			}

		} else{
			try {
				SharedPrefrnceSuperFun.setDataInSharedPrefrence(startScreen, Constants.USER_LANGAUGE, "en");
				System.out.println("----2222ennnnnn---");
				Utility.changeLangauge("en", startScreen);
				howItBtn.setText(getResources().getString(R.string.how_to));
				Utility.setTextHelveticaBold(dontHaveText, StartScreenActivity.this);

				/*spinLanguageList = new ArrayList<String>();

		//	    spinLanguageList.add(getResources().getString(R.string.change_lang));
				spinLanguageList.add(getResources().getString(R.string.arabic));
				spinLanguageList.add(getResources().getString(R.string.english));

				spinLanguage.setAdapter(new SpinnerAdapter(getApplicationContext(), spinLanguageList));*/

				participatingCompBtn.setText(getResources().getString(R.string.Fun_Fair_Centers));
				signUpBtn.setText(getResources().getString(R.string.sign_up));
				loginBtn.setText(getResources().getString(R.string.login_string));
				changeLangauageBtn.setText(getResources().getString(R.string.change_lang));
				dontHaveText.setText(getResources().getString(R.string.dont_have_new));
				titleTv.setText(getResources().getString(R.string.app_name));
			} catch (Exception e) {
				// TODO: handle exception
			}
		}
	}
}
