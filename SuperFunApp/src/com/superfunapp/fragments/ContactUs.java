package com.superfunapp.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class ContactUs extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle;

	private TextView emailTitle;

	private TextView phoneTitle;

	private TextView webTitle;

	//private TextView whtsAppTitle;

	private Button textContactPhone;

	private Button textContactEmail;

	private Button textContactWhts;

	private Button textContactWeb;

	private AlertDialogManager alertDialogManager;

	private ProgressDialog progressDialog;

	private String whts, phone, web, email;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.contact_us, container, false);

		alertDialogManager = new AlertDialogManager();
		textContactEmail = (Button) view.findViewById(R.id.contactEmail);
		textContactPhone = (Button) view.findViewById(R.id.contactPhone);
		textContactWeb = (Button) view.findViewById(R.id.contactWeb);
		textContactWhts = (Button) view.findViewById(R.id.contactWhatsApp);
		//ImageView menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		//menuImage.setVisibility(View.INVISIBLE);
		ImageView back = (ImageView) getActivity().findViewById(R.id.cancelImage);
		back.setVisibility(View.VISIBLE);
		ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		menu.setVisibility(View.VISIBLE);
		
		Constants.mainActivityDisplay = false;
		
		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);
		textActionTitle.setTextSize(29f);
		// textActionTitle.setShadowLayer(2.5f, 3.5f, 5.5f, Color.BLACK);
		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		webTitle = (TextView) view.findViewById(R.id.webText);
		phoneTitle = (TextView) view.findViewById(R.id.hotLineText);
		//whtsAppTitle = (TextView) view.findViewById(R.id.whatsText);
		emailTitle = (TextView) view.findViewById(R.id.emailText);

		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.GONE);

		Utility.setTextEurostile(textContactEmail, getActivity());
		Utility.setTextHelveticaBold(textContactPhone, getActivity());
		Utility.setTextEurostile(textContactWeb, getActivity());
		Utility.setTextHelveticaBold(textContactWhts, getActivity());

		textContactEmail.setOnClickListener(this);
		textContactPhone.setOnClickListener(this);
		textContactWeb.setOnClickListener(this);
		textContactWhts.setOnClickListener(this);

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.contact_us_new);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
			Utility.setTextGabbalandFont(emailTitle, getActivity());
			Utility.setTextGabbalandFont(phoneTitle, getActivity());
			//Utility.setTextGabbalandFont(whtsAppTitle, getActivity());
			Utility.setTextGabbalandFont(webTitle, getActivity());
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(R.string.contact_menu));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		// ContactUs API call
		if (NetworkAvailablity.chkStatus(getActivity())) {
			if (Utility.contactUsList.size() == 0)
				new ContactUsAPI().execute();
			else {
				textContactEmail.setText(Utility.contactUsList.get(2) + "");
				textContactPhone.setText(Utility.contactUsList.get(1) + "");
				textContactWeb.setText(Utility.contactUsList.get(3) + "");
				textContactWhts.setText(Utility.contactUsList.get(0) + "");
			}
		} else
			alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));

		return view;
	}

	/*@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_settings).setVisible(false);
	}*/

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity()).getSupportActionBar();
		actionBar.setIcon(R.drawable.close);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);
		//ImageView view = (ImageView) getActivity().findViewById(android.R.id.home);
		//view.setPadding(0, 0, 0, 10);
		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.contact_menu));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());
		// imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);
		// imageViewBack.setVisibility(View.GONE);
		// imageViewBack.setOnClickListener(this);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	public class ContactUsAPI extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utility.contactUsList = new ArrayList<>();
			progressDialog = new ProgressDialog(getActivity());
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

			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.CONTACT_US, ServiceHandler.POST);
			Log.e("CONTACT_US response", json + "--");
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
					if (result != null || !result.equals(null) || result.length() != 0) {
						whts = jsonObject.optString("whatsapp");
						phone = jsonObject.optString("phone");
						email = jsonObject.optString("email");
						web = jsonObject.optString("website");

						Utility.contactUsList.add(whts);
						Utility.contactUsList.add(phone);
						Utility.contactUsList.add(email);
						Utility.contactUsList.add(web);

						textContactEmail.setText(email + "");
						textContactPhone.setText(phone + "");
						textContactWeb.setText(web + "");
						textContactWhts.setText(whts + "");
						
						System.out.println("----email---"+email);
						System.out.println("----phone---"+phone);
						System.out.println("----web---"+web);
						System.out.println("----whts---"+whts);
					}
				} catch (Exception e) {

					e.printStackTrace();
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

			}

		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.contactEmail:
			Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null));
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
			emailIntent.putExtra(Intent.EXTRA_TEXT, "");
			startActivity(Intent.createChooser(emailIntent, "Send email..."));
			break;
		case R.id.contactPhone:
			Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
			startActivity(intent);
			break;
		case R.id.contactWeb:
			Intent i = new Intent(Intent.ACTION_VIEW);
			i.setData(Uri.parse("http://"+web));
			startActivity(i);
			break;
		case R.id.contactWhatsApp:
			/*
			 * Intent sendIntent = new Intent(); sendIntent.setAction(Intent.ACTION_SEND); sendIntent.putExtra(Intent.EXTRA_TEXT, ""); sendIntent.setType("text/plain"); sendIntent.setPackage("com.whatsapp"); startActivity(sendIntent);
			 */

			// Uri mUri = Uri.parse("smsto:+"+whts);
			// Intent mIntent = new Intent(Intent.ACTION_SENDTO, mUri);
			// mIntent.setPackage("com.whatsapp");
			// mIntent.putExtra("sms_body", "The text goes here");
			// mIntent.putExtra("chat",true);
			// startActivity(mIntent);
			break;

		default:
			break;
		}

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
