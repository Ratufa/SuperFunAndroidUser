package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.adapters.HistoryAdapter;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.LostLogoutDialog;
import com.superfunapp.utils.Utility;

public class PurchaseHistory extends Fragment {

	// ExpandableListView
	private ListView purchaseHistoryView;

	// TextView
	private TextView textActionTitle;

	private ProgressDialog progressDialog;

	// Button
	private Button buttonTicket;

	private Button buttonCollect;

	private Button buttonHistory;

	private AlertDialogManager alertDialogManager;
	
	private LostLogoutDialog lostLogoutDialog;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.purchase_history, container, false);

		alertDialogManager = new AlertDialogManager();
		
		lostLogoutDialog = new LostLogoutDialog(); 
		
		buttonCollect = (Button) getActivity().findViewById(R.id.collectBtn);
		
		buttonHistory = (Button) getActivity().findViewById(R.id.historyBtn);
		
		buttonTicket = (Button) getActivity().findViewById(R.id.buyBtn);

		ImageView cancelImage = (ImageView) getActivity().findViewById(R.id.cancelImage);
		
		ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		
		menu.setVisibility(View.VISIBLE);

		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);
		
		textActionTitle.setTextSize(29f);

		buttonTicket.setSelected(false);
		buttonHistory.setSelected(true);
		buttonCollect.setSelected(false);

		Constants.mainActivityDisplay = false;

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.log_title);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_arabic_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(R.drawable.log_arabic_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(R.drawable.collect_arabic_selector));
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(R.string.log_box));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_english_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(R.drawable.log_english_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(R.drawable.collect_english_selector));
		}

		cancelImage.setVisibility(View.INVISIBLE);

		Utility.setTextGabbalandFont(buttonCollect, getActivity());
		Utility.setTextGabbalandFont(buttonHistory, getActivity());
		Utility.setTextGabbalandFont(buttonTicket, getActivity());

		purchaseHistoryView = (ListView) view.findViewById(R.id.purchaseHistoryList);
		View header = getActivity().getLayoutInflater().inflate(R.layout.list_header, null);
		purchaseHistoryView.addHeaderView(header);

		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.VISIBLE);

		if (NetworkAvailablity.chkStatus(getActivity())) {
			///if (Utility.purchaseHistoryList.size() == 0)
			new PurchaseHistoryAPI().execute();
			/*else
				purchaseHistoryView.setAdapter(new HistoryAdapter(getActivity(), Utility.purchaseHistoryList));*/
		} else
			alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));

		return view;
	}

	/*@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_settings).setVisible(true);
	}*/

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Constants.mainActivityDisplay = false;
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) getActivity(), Constants.USER_LANGAUGE), getActivity());
		Constants.OptionMenuOpen = true;
	}

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity()).getSupportActionBar();
		Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
		actionBar.setIcon(transparentDrawable);
		actionBar.setTitle("");
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.log_box));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	public class PurchaseHistoryAPI extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			progressDialog.show();
			Utility.purchaseHistoryList = new ArrayList<HashMap<String, String>>();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mob_no", SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) getActivity(), Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(getActivity())));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.USER_HISTORY, ServiceHandler.POST, nameValuePairs);
			Log.e("sign up response", json + "--");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {

				JSONObject jsonArray = null;
				try {
					jsonArray = new JSONObject(result);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.HISTORY_RESPONSE, result);
					if (jsonArray != null) {
						//	for (int i = jsonArray.length()-1; i >= 0; i++) {
						if(!jsonArray.has("data")){
							
							JSONArray jsonArray2 = jsonArray.optJSONArray("details");
							for (int i = 0; i < jsonArray2.length(); i++) {
								HashMap<String, String> map = new HashMap<String, String>();
								
								map.put("op_name", jsonArray2.optJSONObject(i).optString("operation_name"));
								map.put("op_type", jsonArray2.optJSONObject(i).optString("operation_type"));
								map.put("op_value", jsonArray2.optJSONObject(i).optString("operation_value"));
								map.put("op_loc", jsonArray2.optJSONObject(i).optString("operation_location"));
								map.put("op_name_arabic", jsonArray2.optJSONObject(i).optString("operation_name_arabic"));
								map.put("op_type_arabic", jsonArray2.optJSONObject(i).optString("operation_type_arabic"));
								map.put("op_loc_arabic", jsonArray2.optJSONObject(i).optString("operation_location_arabic"));

								map.put("op_time", jsonArray2.optJSONObject(i).optString("date").split(" ")[1]);
								map.put("op_date", jsonArray2.optJSONObject(i).optString("date").split(" ")[0]);
								Utility.purchaseHistoryList.add(map);
								
							}
							if (Utility.purchaseHistoryList.size() > 0)
								purchaseHistoryView.setAdapter(new HistoryAdapter(getActivity(), Utility.purchaseHistoryList));
							else {
								purchaseHistoryView.setAdapter(null);
								alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_purchase));
							}
						} else if(jsonArray.has("data")){
							lostLogoutDialog.showAlertDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.please_login_again));
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					//alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

				/*if (Utility.purchaseHistoryList.size() > 0)
					purchaseHistoryView.setAdapter(new HistoryAdapter(getActivity(), Utility.purchaseHistoryList));
				else {
					purchaseHistoryView.setAdapter(null);
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_purchase));
				}*/
			}
		}
	}
	@Override
	public void onStart() {
		super.onStart();
		Constants.OptionMenuOpen = true;
		Constants.mainActivityDisplay = false;
	}

}
