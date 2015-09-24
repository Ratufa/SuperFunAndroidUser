package com.superfunapp;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class ForgotPassword extends Activity implements OnClickListener {

	// EditText
	private EditText editTextMobile;

	// Button
	private Button submitButton;

	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(this,
				Constants.USER_LANGAUGE), this);
		setContentView(R.layout.forget_password);
		editTextMobile = (EditText) findViewById(R.id.forgotMobEt);
		submitButton = (Button) findViewById(R.id.sumbitForgotBtn);
		
		// on click listener
		submitButton.setOnClickListener(this);

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.sumbitForgotBtn:
			String mobNumStr = editTextMobile.getText().toString().trim();

			if (mobNumStr.length() > 0) {
				/*
				 * if (mobNumStr.indexOf("9") != 0 || mobNumStr.indexOf("5") !=
				 * 0 || mobNumStr.indexOf("6") != 0) { Toast.makeText(this,
				 * getResources().getString(R.string.mob_start_valid),
				 * Toast.LENGTH_LONG).show(); } else
				 */if (mobNumStr.length() > 8) {
					Toast.makeText(
							this,
							getResources().getString(R.string.mob_length_valid),
							Toast.LENGTH_LONG).show();
				} else if (mobNumStr.contains("+")) {
					Toast.makeText(
							this,
							getResources().getString(
									R.string.international_codes),
							Toast.LENGTH_LONG).show();
				} else {
					if (NetworkAvailablity.chkStatus(this))
						new ForgotPasswordAPI().execute(mobNumStr);
					else
						Toast.makeText(
								this,
								getResources().getString(
										R.string.network_connection),
								Toast.LENGTH_LONG).show();
				}

			} else {
				Toast.makeText(this,
						getResources().getString(R.string.mob_not_specified),
						Toast.LENGTH_LONG).show();
			}
			break;

		default:
			break;
		}
	}

	// ForgotPassword API implementation
	public class ForgotPasswordAPI extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(ForgotPassword.this);
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
			String json = serviceHandler.makeServiceCall(
					WebServiceDetails.WS_URL
							+ WebServiceDetails.FORGOT_PASSWORD,
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
					if (jsonObject.optString("data").equals("success")) {
						Toast.makeText(
								ForgotPassword.this,
								getResources()
										.getString(R.string.password_sent),
								Toast.LENGTH_LONG).show();
					} else {
						Toast.makeText(
								ForgotPassword.this,
								getResources()
										.getString(R.string.pass_not_sent),
								Toast.LENGTH_LONG).show();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Toast.makeText(
							ForgotPassword.this,
							getResources().getString(
									R.string.server_communication),
							Toast.LENGTH_LONG).show();
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
