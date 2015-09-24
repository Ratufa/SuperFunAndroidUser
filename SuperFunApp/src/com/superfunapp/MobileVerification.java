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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class MobileVerification extends Activity implements OnClickListener {

	// TextView
	private TextView timerTextView, title, verifyText, timerTitle, secondTitle;

	private TextView mobNumber;

	// EditText
	private EditText editTextMobile;

	// Button
	private Button confirmBtn, correctBtn;

	private ProgressDialog progressDialog;

	private Handler customHandler = new Handler();

	private static int timerCount = 180;

	private AlertDialogManager alertDialogManager;

	private ImageView back;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE), this);
		setContentView(R.layout.mobile_verification);
		alertDialogManager = new AlertDialogManager();

		editTextMobile = (EditText) findViewById(R.id.verifyMobEt);
		timerTextView = (TextView) findViewById(R.id.verifyTimerText);
		mobNumber = (TextView) findViewById(R.id.verifyMobText);
		verifyText = (TextView) findViewById(R.id.verify_text);
		timerTitle = (TextView) findViewById(R.id.timerTitle);
		secondTitle = (TextView) findViewById(R.id.secondTitle);
		confirmBtn = (Button) findViewById(R.id.confirmMobBtn);
		correctBtn = (Button) findViewById(R.id.numCorrectBtn);
		title = (TextView) findViewById(R.id.titleMobile);
		back = (ImageView) findViewById(R.id.mobile_close);

		if (SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_LANGAUGE).equals("ar")) {
			title.setBackgroundResource(R.drawable.sms_title);
			title.setText("");
			Utility.setTextGabbalandFont(verifyText, this);
			Utility.setTextGabbalandFont(timerTitle, this);
			Utility.setTextGabbalandFont(secondTitle, this);
		} else {
			Utility.setTextGabbalandFont(title, this);
			title.setBackgroundDrawable(null);
			title.setText(getResources().getString(R.string.sms_screen_name));
			Utility.setTextMogaFont(confirmBtn, this);
			Utility.setTextHelveticaBold(mobNumber, this);
			Utility.setTextHelveticaBold(timerTextView, this);
			Utility.setTextMogaFont(correctBtn, this);
		}

		customHandler.postDelayed(updateTimerThread, 1000);
		// on click listener
		confirmBtn.setOnClickListener(this);
		correctBtn.setOnClickListener(this);
		back.setOnClickListener(this);

		mobNumber.setText(SharedPrefrnceSuperFun.getSharedPrefData(this, Constants.USER_MOB));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.numCorrectBtn:
			showAlert();
			break;

		case R.id.mobile_close:
			timerCount = 180;
			if (updateTimerThread != null)
				customHandler.removeCallbacks(updateTimerThread);

			Intent intent = new Intent(MobileVerification.this, SignupActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
			finish();
			break;

		case R.id.confirmMobBtn:
			String pinNumStr = editTextMobile.getText().toString().trim();

			if (pinNumStr.length() > 0) {

				if (NetworkAvailablity.chkStatus(this))
					new VerifyPin().execute(pinNumStr);
				else
					alertDialogManager.showAlertDialog(this, getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));

			} else {
				alertDialogManager.showAlertDialog(this, getResources().getString(R.string.error), getResources().getString(R.string.enter_pin));
			}

			break;

		default:
			break;
		}
	}

	// Send Pin Again API implementation
	public class SendAgain extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MobileVerification.this);
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
			nameValuePairs.add(new BasicNameValuePair("mob_no", SharedPrefrnceSuperFun.getSharedPrefData(MobileVerification.this, Constants.USER_MOB)));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.PIN_SEND, ServiceHandler.POST, nameValuePairs);
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {
				JSONObject jsonObject;
				try {
					jsonObject = new JSONObject(result);
					String data = jsonObject.optString("data");

					if (data.equals("message not sent")) {
						alertDialogManager.showAlertDialog(MobileVerification.this, "Error", getResources().getString(R.string.msg_not_sent));

					} else {
						alertDialogManager.showAlertDialog(MobileVerification.this, "Message", getResources().getString(R.string.pin_send));

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(MobileVerification.this, "Error", getResources().getString(R.string.server_communication));

				}

			}

		}

	}

	// Verify Pin API implementation
	public class VerifyPin extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MobileVerification.this);
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
			nameValuePairs.add(new BasicNameValuePair("mob_no", SharedPrefrnceSuperFun.getSharedPrefData(MobileVerification.this, Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("user_id", SharedPrefrnceSuperFun.getSharedPrefData(MobileVerification.this, Constants.USER_ID)));
			nameValuePairs.add(new BasicNameValuePair("pin", params[0]));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(MobileVerification.this)));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.VERIFY_PIN, ServiceHandler.POST, nameValuePairs);

			Log.e("params>>", nameValuePairs + "--" + json);
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

					if (jsonObject.optString("status").equals("failed")) {

						alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.invalid_pin));

					} else if (jsonObject.optString("status").equals("not verify")) {
						alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.invalid_pin));
					} else {
						// SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_MOB, jsonObject.optJSONObject("details").optString("mob_no"));

						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_STATUS, jsonObject.optString("status"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_MOB, jsonObject.optJSONObject("details").optString("mob_no"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_POINTS, jsonObject.optJSONObject("details").optString("points"));

						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_TICKETS, jsonObject.optJSONObject("details").optString("tickets"));
						// SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_LANGAUGE, jsonObject.optJSONObject("details").optString("pref_language"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_ID, jsonObject.optJSONObject("details").optString("id"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_PASSWORD, jsonObject.optJSONObject("details").optString("password"));

						if (SharedPrefrnceSuperFun.getSharedPrefData(MobileVerification.this, Constants.USER_STATUS).equals("verify")) {
							timerCount = 180;
							timerTextView.setText("0");
							customHandler.removeCallbacks(updateTimerThread);
							StartScreenActivity.startScreen.finish();
							SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, "is_logged_in", "true");
							alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.message), getResources().getString(R.string.login_successful));

							SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, "entry_way", "true");
							
							Intent intent = new Intent(MobileVerification.this, HomeScreen.class);
							startActivity(intent);
							overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
							finish();
						} else {
							alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.invalid_pin));
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

			}

		}

	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			timerCount--;

			if (timerCount >= 0) {
				customHandler.postDelayed(this, 1000);
				timerTextView.setText(timerCount + "");

			} else {
				timerCount = 180;
				timerTextView.setText("0");
				customHandler.removeCallbacks(updateTimerThread);
				Intent intent = new Intent(MobileVerification.this, SignupActivity.class);
				startActivity(intent);
				overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
				finish();
			}

		}

	};

	@Override
	public void onBackPressed() {
		// super.onBackPressed();
		showExitAlert();
		/*
		 * timerCount = 120; if (updateTimerThread != null) customHandler.removeCallbacks(updateTimerThread);
		 * 
		 * Intent intent = new Intent(MobileVerification.this, SignupActivity.class); startActivity(intent); overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out); finish();
		 */
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

				if (et.getText().toString().trim().length() != 0) {
					if (!et.getText().toString().trim().equals(SharedPrefrnceSuperFun.getSharedPrefData(MobileVerification.this, Constants.USER_MOB)))

						new EditNumber().execute(et.getText().toString().trim());
					else
						alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.num_same));
				} else
					alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.mob_not_specified));
			}
		});

		dialog.show();
	}

	public class EditNumber extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(MobileVerification.this);
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
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(MobileVerification.this)));
			nameValuePairs.add(new BasicNameValuePair("uu_id", Utility.getDeviceId(MobileVerification.this)));
			nameValuePairs.add(new BasicNameValuePair("simno", Utility.getSimNumber(MobileVerification.this)));
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
					String status = jsonObject.optString("status");
					if (status.equals("success")) {
						JSONObject data = jsonObject.optJSONObject("details");

						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_ID, data.optString("user_id"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(MobileVerification.this, Constants.USER_MOB, data.optString("mob_no"));

					} else if (status.equals("Already Registered")) {

						/*
						 * alertDialogManager.showAlertDialog( MobileVerification.this, "Error", getResources().getString( R.string.number_registered));
						 */

					} else if (status.equals("Not Verified Yet")) {
						/*
						 * alertDialogManager.showAlertDialog( MobileVerification.this, "Error", getResources().getString(R.string.invalid_num));
						 */

					}

				} catch (JSONException e) {

					e.printStackTrace();
					alertDialogManager.showAlertDialog(MobileVerification.this, getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

			}

		}
	}

	public void showExitAlert() {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.message));

		// set dialog message
		alertDialogBuilder.setMessage(getResources().getString(R.string.Exit_Super_Fun_App)).setCancelable(false).setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				// if this button is clicked, close
				// current activity
				timerCount = 180;
				if (updateTimerThread != null)
					customHandler.removeCallbacks(updateTimerThread);

				finish();

			}
		}).setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
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
