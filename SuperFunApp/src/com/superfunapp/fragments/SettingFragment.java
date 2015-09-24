package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.Toast;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.StartScreenActivity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class SettingFragment extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle;

	// Button
	private Button logoutBtn;

	private Button submitBtn;

	private ProgressDialog progressDialog;

	private RadioGroup langRadioGroup;

	private RadioButton englishButton, arabicButton;

	private TextView chooseLang;

	private AlertDialogManager alertDialogManager;

	private String userLanguage = "en";

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.settings, container, false);
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(
				(HomeScreen) getActivity(), Constants.USER_LANGAUGE),
				(HomeScreen) getActivity());
		alertDialogManager = new AlertDialogManager();

		logoutBtn = (Button) view.findViewById(R.id.logoutBtn);
		if (SharedPrefrnceSuperFun
				.getSharedPrefData(getActivity(), "entry_way")
				.equalsIgnoreCase("true")) {
			logoutBtn.setVisibility(View.VISIBLE);
		} else {
			logoutBtn.setVisibility(View.GONE);
		}
		Constants.mainActivityDisplay = false;
		submitBtn = (Button) view.findViewById(R.id.submitLanguage);

		langRadioGroup = (RadioGroup) view.findViewById(R.id.langRadioGroup);

		textActionTitle = (TextView) getActivity().findViewById(
				R.id.screenTitle);
		chooseLang = (TextView) view.findViewById(R.id.chooseLangText);
		textActionTitle.setText(getResources()
				.getString(R.string.settings_menu));
		textActionTitle.setTextSize(29f);

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
				Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.settings);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
			// Utility.setAxTMANALFont(chooseLang, getActivity());
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		ImageView menuImage = (ImageView) getActivity().findViewById(
				R.id.menuImage);
		menuImage.setVisibility(View.VISIBLE);

		ImageView back = (ImageView) getActivity().findViewById(
				R.id.cancelImage);
		back.setVisibility(View.VISIBLE);

		// ImageView menu = (ImageView)
		// getActivity().findViewById(R.id.menuImage);
		// menu.setVisibility(View.VISIBLE);

		englishButton = (RadioButton) view.findViewById(R.id.englishLangBtn);
		arabicButton = (RadioButton) view.findViewById(R.id.arabicLangBtn);

		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.GONE);

		logoutBtn.setOnClickListener(this);
		// submitBtn.setOnClickListener(this);

		submitBtn.setOnTouchListener(new OnTouchListener() {
			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					submitBtn.setTextColor(Color.parseColor("#ECE2A2"));
					submitBtn.setBackgroundResource(R.drawable.select_button);
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_UP:
					submitBtn.setTextColor(Color.parseColor("#FFFFFF"));
					submitBtn.setBackgroundResource(R.drawable.unselect_button);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(
							getActivity(), Constants.USER_LANGAUGE,
							userLanguage);
					Utility.changeLangauge(userLanguage, getActivity());
					if (userLanguage == "en") {
						Utility.setTextViewWidth("en", textActionTitle);
						textActionTitle.setText(getResources().getString(
								R.string.settings_menu));
						Utility.setTextGabbalandFont(textActionTitle,
								getActivity());
						// Utility.setTextGabbalandFont(chooseLang,
						// getActivity());
						textActionTitle.setBackgroundDrawable(null);
						logoutBtn.setText(getResources().getString(
								R.string.log_out_new));
						Utility.setTextMogaFont(logoutBtn, getActivity());

						submitBtn.setText(getResources().getString(
								R.string.submit));
						chooseLang.setText(getResources().getString(
								R.string.choose_language));
					}
					if (userLanguage == "ar") {
						Utility.setTextViewWidth("ar", textActionTitle);
						textActionTitle
								.setBackgroundResource(R.drawable.settings);
						textActionTitle.setText("");
						logoutBtn.setText(getResources().getString(
								R.string.log_out_new));
						Utility.setMogaMaddySolemanFont(logoutBtn,
								getActivity());
						submitBtn.setText(getResources().getString(
								R.string.submit));
						chooseLang.setText(getResources().getString(
								R.string.choose_language));
						Utility.setMogaMaddySolemanFont(chooseLang,
								getActivity());
					}
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_OUTSIDE:
					submitBtn.setTextColor(Color.parseColor("#FFFFFF"));
					submitBtn.setBackgroundResource(R.drawable.unselect_button);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(
							getActivity(), Constants.USER_LANGAUGE,
							userLanguage);
					Utility.changeLangauge(userLanguage, getActivity());
					if (userLanguage == "en") {
						Utility.setTextViewWidth("en", textActionTitle);
						textActionTitle.setText(getResources().getString(
								R.string.settings_menu));
						Utility.setTextGabbalandFont(textActionTitle,
								getActivity());
						// Utility.setTextGabbalandFont(chooseLang,
						// getActivity());
						textActionTitle.setBackgroundDrawable(null);
						logoutBtn.setText(getResources().getString(
								R.string.log_out_new));
						Utility.setTextMogaFont(logoutBtn, getActivity());

						submitBtn.setText(getResources().getString(
								R.string.submit));
						chooseLang.setText(getResources().getString(
								R.string.choose_language));
					}
					if (userLanguage == "ar") {
						Utility.setTextViewWidth("ar", textActionTitle);
						textActionTitle
								.setBackgroundResource(R.drawable.settings);
						textActionTitle.setText("");
						logoutBtn.setText(getResources().getString(
								R.string.log_out_new));
						Utility.setMogaMaddySolemanFont(logoutBtn,
								getActivity());
						submitBtn.setText(getResources().getString(
								R.string.submit));
						chooseLang.setText(getResources().getString(
								R.string.choose_language));
						Utility.setMogaMaddySolemanFont(chooseLang,
								getActivity());
					}
					return true;
				}
				return false;
			}

		});

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
				Constants.USER_LANGAUGE) != null) {
			if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
					Constants.USER_LANGAUGE).equals("ar")) {
				arabicButton.setChecked(true);
				englishButton.setChecked(false);
			} else {
				englishButton.setChecked(true);
				arabicButton.setChecked(false);
			}
		}

		langRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						// TODO Auto-generated method stub

						if (checkedId == R.id.englishLangBtn) {
							userLanguage = "en";

							// textActionTitle.setText(getResources().getString(
							// R.string.settings_menu));
							// Utility.setTextGabbalandFont(textActionTitle,
							// getActivity());
							// textActionTitle.setBackgroundDrawable(null);
							// logoutBtn.setText(getResources().getString(
							// R.string.log_out_new));
							// Utility.setTextMogaFont(logoutBtn,
							// getActivity());
							// chooseLang.setText(getResources().getString(
							// R.string.choose_language));
						}
						if (checkedId == R.id.arabicLangBtn) {
							userLanguage = "ar";
							// textActionTitle.setBackgroundResource(R.drawable.settings);
							// textActionTitle.setText("");
							// logoutBtn.setText(getResources().getString(
							// R.string.log_out_new));
							// Utility.setMogaMaddySolemanFont(logoutBtn,
							// getActivity());
							//
							// chooseLang.setText(getResources().getString(
							// R.string.choose_language));
						}

					}
				});

		return view;
	}

	/*
	 * @Override public void onPrepareOptionsMenu(Menu menu) { // TODO
	 * Auto-generated method stub super.onPrepareOptionsMenu(menu);
	 * menu.findItem(R.id.action_settings).setVisible(false); }
	 */

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity())
				.getSupportActionBar();
		actionBar.setIcon(R.drawable.close);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout,
				null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources()
				.getString(R.string.settings_menu));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.new_header));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {

		case R.id.logoutBtn:
			showAlert();
			break;
		case R.id.submitLanguage:
			/*
			 * SharedPrefrnceSuperFun.setDataInSharedPrefrence( getActivity(),
			 * Constants.USER_LANGAUGE, userLanguage);
			 * Utility.changeLangauge(userLanguage, getActivity()); if
			 * (userLanguage == "en") { Utility.setTextViewWidth("en",
			 * textActionTitle);
			 * textActionTitle.setText(getResources().getString(
			 * R.string.settings_menu));
			 * Utility.setTextGabbalandFont(textActionTitle, getActivity());
			 * //Utility.setTextGabbalandFont(chooseLang, getActivity());
			 * textActionTitle.setBackgroundDrawable(null);
			 * logoutBtn.setText(getResources().getString(
			 * R.string.log_out_new)); Utility.setTextMogaFont(logoutBtn,
			 * getActivity());
			 * 
			 * submitBtn.setText(getResources().getString(R.string.submit));
			 * chooseLang.setText(getResources().getString(
			 * R.string.choose_language)); } if (userLanguage == "ar") {
			 * Utility.setTextViewWidth("ar", textActionTitle);
			 * textActionTitle.setBackgroundResource(R.drawable.settings);
			 * textActionTitle.setText("");
			 * logoutBtn.setText(getResources().getString(
			 * R.string.log_out_new));
			 * Utility.setMogaMaddySolemanFont(logoutBtn, getActivity());
			 * submitBtn.setText(getResources().getString(R.string.submit));
			 * chooseLang.setText(getResources().getString(
			 * R.string.choose_language));
			 * Utility.setMogaMaddySolemanFont(chooseLang, getActivity()); }
			 */
			break;

		default:
			break;
		}
	}

	public class ChangePassword extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(
					R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_id",
					SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
							Constants.USER_ID)));
			nameValuePairs.add(new BasicNameValuePair("old_pass", params[0]));
			nameValuePairs.add(new BasicNameValuePair("new_pass", params[1]));

			String json = serviceHandler.makeServiceCall(
					WebServiceDetails.WS_URL
							+ WebServiceDetails.CHANGE_PASSWORD,
					ServiceHandler.POST, nameValuePairs);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();

			if (result.length() != 0) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.optString("status").equals("success")) {
						alertDialogManager.showAlertDialog(
								getActivity(),
								getResources().getString(R.string.message),
								getResources().getString(
										R.string.password_changed));
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.server_communication));
				}

			}

		}

	}

	// Verify Pin API implementation
	public class LogOut extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(
					R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mob_no",
					SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
							Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility
					.getDeviceId(getActivity())));
			String json = serviceHandler.makeServiceCall(
					WebServiceDetails.WS_URL + WebServiceDetails.LOG_OUT,
					ServiceHandler.POST, nameValuePairs);

			Log.e("params>>", nameValuePairs + "--" + json);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			Toast.makeText(getActivity(),
					getResources().getString(R.string.account_deleted),
					Toast.LENGTH_LONG).show();
			SharedPrefrnceSuperFun.deletePrefrenceData(getActivity());
			Intent intent = new Intent(getActivity(), StartScreenActivity.class);
			startActivity(intent);
			getActivity().overridePendingTransition(R.anim.trans_left_in,
					R.anim.trans_left_out);
			getActivity().finish();

		}

	}

	public void showAlert() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.message));

		// set dialog message
		alertDialogBuilder
				.setMessage(
						getResources().getString(R.string.delete_account_msg))
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.yes),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {

								new LogOut().execute();

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

	@Override
	public void onStart() {
		super.onStart();
		Constants.OptionMenuOpen = true;
	}

	@Override
	public void onResume() {
		super.onResume();
		Constants.mainActivityDisplay = false;
		Constants.OptionMenuOpen = true;
	}
}
