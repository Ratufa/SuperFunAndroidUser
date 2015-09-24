package com.superfunapp.fragments;

import java.util.ArrayList;

import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class PrivacyFragment extends Fragment {

	// TextView
	private TextView textActionTitle;

	private TextView privacyTv;

	private ProgressDialog progressDialog;

	private AlertDialogManager alertDialogManager;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.privacy_policy, container, false);
		privacyTv = (TextView) view.findViewById(R.id.privacyText);
		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.GONE);

		ImageView menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		menuImage.setVisibility(View.VISIBLE);
		ImageView back = (ImageView) getActivity().findViewById(R.id.cancelImage);
		back.setVisibility(View.VISIBLE);
		//ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		//menu.setVisibility(View.VISIBLE);
		
		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);

		textActionTitle.setTextSize(29f);

		alertDialogManager = new AlertDialogManager();
		Constants.mainActivityDisplay = false;

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.privacy);
			textActionTitle.setText("");
			privacyTv.setGravity(Gravity.RIGHT);
			Utility.setAxTMANALFont(privacyTv, getActivity());
			Utility.setTextViewWidth("ar", textActionTitle);

		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), "TIMES.TTF");
			privacyTv.setGravity(Gravity.LEFT | Gravity.CENTER);
			privacyTv.setTypeface(typeface);
			textActionTitle.setText(getResources().getString(R.string.privacy_menu));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		// Privacy_Policy API call
		if (NetworkAvailablity.chkStatus(getActivity()))
			if (Utility.privacyList.size() == 0)
				new Privacy_Policy().execute();
			else {
				if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
					privacyTv.setText(Utility.privacyList.get(0));

				} else {

					privacyTv.setText(Utility.privacyList.get(1));
				}

			}
		else
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

		// view.setLayoutParams(LinearLayout.)(0, 0, 0, 10);
		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.privacy_menu));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	// Privacy_Policy API implementation
	public class Privacy_Policy extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utility.privacyList = new ArrayList<>();
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

			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.PRIVACY_POLICY, ServiceHandler.POST);
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

					Utility.privacyList.add(jsonObject.optString("pp_arabic") + "");

					Utility.privacyList.add(jsonObject.optString("pp_english") + "");

					if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
						privacyTv.setText(jsonObject.optString("pp_arabic") + "");
						// Utility.privacyList.add(jsonObject.optString("pp_arabic") + "");
					} else {
						// Utility.privacyList.add(jsonObject.optString("pp_english") + "");
						privacyTv.setText(jsonObject.optString("pp_english") + "");
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}
			}
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
