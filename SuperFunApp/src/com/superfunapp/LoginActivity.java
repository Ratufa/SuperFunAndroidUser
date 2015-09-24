package com.superfunapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class LoginActivity extends Activity implements OnClickListener {

	// EditText
	private EditText editTextMobile;
	
	private TextView titleLogin;

	// Button
	private Button forgotButton;
	
	private Button loginBtn;

	private ProgressDialog progressDialog;

	private AlertDialogManager alertDialogManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(this,
				Constants.USER_LANGAUGE), this);
		setContentView(R.layout.login_screen);
		alertDialogManager = new AlertDialogManager();
		editTextMobile = (EditText) findViewById(R.id.loginMobEt);
		forgotButton = (Button) findViewById(R.id.forgotBtn);
		loginBtn = (Button) findViewById(R.id.loginBtn);
		titleLogin = (TextView) findViewById(R.id.loginTitle);

		Utility.setTextMogaFont(loginBtn, this);
		Utility.setTextMogaFont(forgotButton, this);
		Utility.setTextGabbalandFont(titleLogin, this);
		loginBtn.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.loginBtn:
			String mobNumberStr = editTextMobile.getText().toString().trim();

			if (mobNumberStr.length() != 0) {

				if (mobNumberStr.length() > 7) {
					alertDialogManager.showAlertDialog(this, getResources()
							.getString(R.string.error), getResources()
							.getString(R.string.mob_length_valid));
				} else {
					if (NetworkAvailablity.chkStatus(this))
						new Login().execute(mobNumberStr);
					else
						alertDialogManager.showAlertDialog(this, getResources()
								.getString(R.string.net_error), getResources()
								.getString(R.string.network_connection));
				}

			} else {
				if (mobNumberStr.length() == 0) {
					alertDialogManager.showAlertDialog(this, getResources()
							.getString(R.string.error), getResources()
							.getString(R.string.mob_not_specified));

				}
			}
			break;

		default:
			break;
		}
	}

	public class Login extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(LoginActivity.this);
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
			nameValuePairs.add(new BasicNameValuePair("mob_no", params[0]));

			nameValuePairs.add(new BasicNameValuePair("device_id", Utility
					.getDeviceId(LoginActivity.this)));
			nameValuePairs
					.add(new BasicNameValuePair("device_type", "android"));
			String json = serviceHandler.makeServiceCall(
					WebServiceDetails.WS_URL + WebServiceDetails.LOGIN,
					ServiceHandler.POST, nameValuePairs);
			Log.e("login response", json + "");
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

						Toast.makeText(
								LoginActivity.this,
								getResources().getString(
										R.string.invalid_username_pwd),
								Toast.LENGTH_LONG).show();
					} else {
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_MOB,
								jsonObject.optJSONObject("details").optString(
										"mob_no"));

						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this, "is_logged_in", "true");
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_MOB,
								jsonObject.optJSONObject("details").optString(
										"mob_no"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_POINTS,
								jsonObject.optJSONObject("details").optString(
										"points"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_STATUS,
								jsonObject.optJSONObject("details").optString(
										"status"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_TICKETS,
								jsonObject.optJSONObject("details").optString(
										"tickets"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_LANGAUGE,
								jsonObject.optJSONObject("details").optString(
										"pref_language"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_ID,
								jsonObject.optJSONObject("details").optString(
										"id"));
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								LoginActivity.this,
								Constants.USER_PASSWORD,
								jsonObject.optJSONObject("details").optString(
										"password"));
						StartScreenActivity.startScreen.finish();
						if (SharedPrefrnceSuperFun.getSharedPrefData(
								LoginActivity.this, Constants.USER_STATUS)
								.equals("0")) {
							alertDialogManager.showAlertDialog(
									LoginActivity.this,
									getResources().getString(R.string.error),
									getResources().getString(
											R.string.verify_number));

							Intent intent = new Intent(LoginActivity.this,
									MobileVerification.class);
							startActivity(intent);
							overridePendingTransition(R.anim.trans_left_in,
									R.anim.trans_left_out);
							finish();
						} else {
							Toast.makeText(
									LoginActivity.this,
									getResources().getString(
											R.string.login_successful),
									Toast.LENGTH_LONG).show();
							Intent intent = new Intent(LoginActivity.this,
									HomeScreen.class);
							startActivity(intent);
							overridePendingTransition(R.anim.trans_left_in,
									R.anim.trans_left_out);
							finish();
						}

					}

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(
							LoginActivity.this,
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.server_communication));
				}

			}

		}

	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.trans_right_in, R.anim.trans_right_out);
	}

}
