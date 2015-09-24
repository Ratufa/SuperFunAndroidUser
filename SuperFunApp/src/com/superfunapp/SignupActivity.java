package com.superfunapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class SignupActivity extends Activity implements OnClickListener {

	// EditText
	private EditText editTextMobile;

	private TextView title;

	private TextView signUpVerifyText;

	// Button
	private Button signupButton;

	private Button termsBtn;

	private Button privacyBtn;

	// Intent
	private Intent intent;

	private ProgressDialog progressDialog;

	public static Activity signUp;

	private AlertDialogManager alertDialogManager;

	private ImageView back;

	private String mobNumberStr;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE), this);
		setContentView(R.layout.signup_screen);
		alertDialogManager = new AlertDialogManager();
		signUp = this;
		editTextMobile = (EditText) findViewById(R.id.signupMobEt);
		signupButton = (Button) findViewById(R.id.signUpBtn);
		termsBtn = (Button) findViewById(R.id.termsBtn);
		privacyBtn = (Button) findViewById(R.id.privacyBtn);
		title = (TextView) findViewById(R.id.signupText);
		signUpVerifyText = (TextView) findViewById(R.id.signUpVerify);
		back = (ImageView) findViewById(R.id.signup_close);

		Utility.setTextMogaFont(signupButton, this);

		if (SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE).equals("ar")) {
			LayoutParams paramsExample = new LayoutParams(200, LinearLayout.LayoutParams.WRAP_CONTENT);

			title.setLayoutParams(paramsExample);
			title.setBackgroundDrawable(getResources().getDrawable(R.drawable.welcome_new));
			title.setText("");
			Utility.setTextGabbalandFont(signUpVerifyText, this);
		} else {
			LayoutParams paramsExample = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

			title.setLayoutParams(paramsExample);
			title.setText(getResources().getString(R.string.welcome_string));
			Utility.setTextGabbalandFont(title, this);
			Utility.setTextHelveticaBold(signUpVerifyText, this);
			title.setBackgroundDrawable(null);
		}

		termsBtn.setPaintFlags(termsBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		privacyBtn.setPaintFlags(privacyBtn.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

		termsBtn.setOnClickListener(this);
		privacyBtn.setOnClickListener(this);
		signupButton.setOnClickListener(this);
		back.setOnClickListener(this);

		signupButton.setOnTouchListener(new OnTouchListener() {        
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch(event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					signupButton.setTextColor(Color.parseColor("#ECE2A2"));
					signupButton.setBackgroundResource(R.drawable.select_button);
					return true; // if you want to handle the touch event
					
				case MotionEvent.ACTION_UP:
					signupButton.setTextColor(Color.parseColor("#FFFFFF"));
					signupButton.setBackgroundResource(R.drawable.unselect_button);
					mobNumberStr = editTextMobile.getText().toString().trim();

					if (mobNumberStr.length() != 0) {
						if (mobNumberStr.length() > 8 || mobNumberStr.length() < 8) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_length_valid));
						}
						else if (!mobNumberStr.matches("(9|6|5).*")) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_start_valid));
						} else if (mobNumberStr.contains("+")) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.international_codes));
						} else {
							SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_MOB, mobNumberStr);
							showAlertDialog();
						}
					} else {
						if (mobNumberStr.length() == 0) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_not_specified));
						}
					}
					return true; // if you want to handle the touch event
				case MotionEvent.ACTION_OUTSIDE:
					signupButton.setTextColor(Color.parseColor("#FFFFFF"));
					signupButton.setBackgroundResource(R.drawable.unselect_button);
					mobNumberStr = editTextMobile.getText().toString().trim();
					if (mobNumberStr.length() != 0) {
						if (mobNumberStr.length() > 8 || mobNumberStr.length() < 8) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_length_valid));
						}
						else if (!mobNumberStr.matches("(9|6|5).*")) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_start_valid));
						} else if (mobNumberStr.contains("+")) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.international_codes));
						} else {
							SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_MOB, mobNumberStr);
							showAlertDialog();
						}
					} else {
						if (mobNumberStr.length() == 0) {
							alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_not_specified));
						}
					}
					return true;
				}
				return false;
			}

		});

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.termsBtn:
			intent = new Intent(this, HomeScreen.class);
			intent.putExtra("terms", true);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

			break;
		case R.id.privacyBtn:
			intent = new Intent(this, HomeScreen.class);
			intent.putExtra("privacy", true);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);

			break;
		case R.id.signup_close:
			Intent intent = new Intent(SignupActivity.this, StartScreenActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
			finish();
			break;
		case R.id.signUpBtn:

			mobNumberStr = editTextMobile.getText().toString().trim();

			if (mobNumberStr.length() != 0) {

				if (mobNumberStr.length() > 8 || mobNumberStr.length() < 8) {
					alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_length_valid));

				}

				else if (!mobNumberStr.matches("(9|6|5).*")) {

					alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_start_valid));

				} else if (mobNumberStr.contains("+")) {

					alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.international_codes));

				} else {
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_MOB, mobNumberStr);

					showAlertDialog();

				}

			} else {
				if (mobNumberStr.length() == 0) {
					alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_not_specified));
				}
			}

			break;

		default:
			break;
		}
	}

	public class SignUp extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressDialog = new ProgressDialog(SignupActivity.this);
			progressDialog.setMessage(getResources().getString(R.string.please_wait));
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
			nameValuePairs.add(new BasicNameValuePair("mob_no", params[0]));
			nameValuePairs.add(new BasicNameValuePair("device_type", "android"));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(SignupActivity.this)));
			nameValuePairs.add(new BasicNameValuePair("uu_id", Utility.getDeviceId(SignupActivity.this)));
			nameValuePairs.add(new BasicNameValuePair("simno", Utility.getSimNumber(SignupActivity.this)));
			nameValuePairs.add(new BasicNameValuePair("ipaddrss", Utility.getLocalIpAddress()));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.SIGN_UP, ServiceHandler.POST, nameValuePairs);
			Log.e("sign up response", json + "--");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {

				JSONObject jsonObject = null;
				try {

					jsonObject = new JSONObject(result);
					JSONObject data = null;
					String status = jsonObject.optString("status");
					data = jsonObject.optJSONObject("details");

					if (status.equals("success")) {
						intent = new Intent(SignupActivity.this, MobileVerification.class);
						startActivity(intent);
						overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
						finish();
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_ID, data.optString("id"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_MOB, data.optString("mob_no"));
					}

					else if (status.equals("Already Registered")) {
						intent = new Intent(SignupActivity.this, MobileVerification.class);
						startActivity(intent);
						overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
						finish();
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_ID, data.optString("id"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(SignupActivity.this, Constants.USER_MOB, data.optString("mob_no"));

					} else if (status.equals("Not Verified Yet")) {
						alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error),getResources().getString(R.string.inactive_account));
					}else if (status.equals("Already Login")) {

						alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error),getResources().getString(R.string.log_out_msg));
					}else{
						intent = new Intent(SignupActivity.this, MobileVerification.class);
						startActivity(intent);
						overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
						finish();
					}

				} catch (JSONException e) {

					e.printStackTrace();
					alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

			}


		}
	}

	@Override
	public void onBackPressed() {
		//super.onBackPressed();
	//	showExitAlert();
		intent = new Intent(SignupActivity.this, StartScreenActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
		//overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
		finish();
	}

	public void showAlert() {

		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.verify_mobile);
		dialog.setTitle(getResources().getString(R.string.app_name));
		// set the custom dialog components - text, image and button
		final EditText et = (EditText) dialog.findViewById(R.id.mobNumberEt);
		Button btnEdit = (Button) dialog.findViewById(R.id.editMob);
		Button btnYes = (Button) dialog.findViewById(R.id.confirmMob);
		et.setText(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_MOB) + "");
		// if button is clicked, close the custom dialog
		btnYes.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		btnEdit.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

			}
		});

		dialog.show();
	}

	public void showAlertDialog() {

		AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

		//this.fragmentName = fragmentNm;
		// Setting Dialog Title
		alertDialog.setTitle(getResources().getString(R.string.app_name));

		// Setting Dialog Message
		alertDialog.setMessage(getResources().getString(R.string.mob_correct_text));

		// Setting OK Button
		alertDialog.setPositiveButton(getResources().getString(R.string.yes),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//changeFragment(fragmentName);
				if (NetworkAvailablity.chkStatus(SignupActivity.this))
					new SignUp().execute(mobNumberStr);
				else
					alertDialogManager.showAlertDialog(SignupActivity.this, getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));

			}
		});

		alertDialog.setNegativeButton(getResources().getString(R.string.no),
				new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//changeFragment(fragmentName);
				dialog.dismiss();
			}
		});

		// Showing Alert Message
		alertDialog.show();
	}

	public void showExitAlert() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.message));

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
