package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.adapters.CompanyListAdapter;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class CompanyList extends Fragment {

	// ExpandableListView
	private ListView purchaseHistoryView;
	// TextView
	private TextView textActionTitle;
	
	private AlertDialogManager alertDialogManager;

	private ProgressDialog progressDialog;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.participating_companies, container, false);
		alertDialogManager = new AlertDialogManager();
		purchaseHistoryView = (ListView) view.findViewById(R.id.companyList);
		View tabs = getActivity().findViewById(R.id.tabLayout);
		ImageView menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		menuImage.setVisibility(View.VISIBLE);
		ImageView back = (ImageView) getActivity().findViewById(R.id.cancelImage);
		back.setVisibility(View.VISIBLE);
		//ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		//menu.setVisibility(View.VISIBLE);
		View header = getActivity().getLayoutInflater().inflate(R.layout.list_header, null);
		purchaseHistoryView.addHeaderView(header);

		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);
		textActionTitle.setTextSize(29f);
		
		Constants.mainActivityDisplay = false;
		
		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.locations);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(R.string.fun_fair_comps));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		tabs.setVisibility(View.GONE);

		// Company List API call
		if (NetworkAvailablity.chkStatus(getActivity())) {
			if (Utility.headerList.size() == 0)
				new CompaniesList().execute();
			else
				purchaseHistoryView.setAdapter(new CompanyListAdapter(getActivity(), Utility.headerList));
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

	// Setting up Action bar
	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity()).getSupportActionBar();
		actionBar.setIcon(R.drawable.close);
		actionBar.setTitle("");
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.fun_fair_comps));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	// Participating companies list API implementation
	public class CompaniesList extends AsyncTask<String, String, String> {

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
			Utility.headerList = new ArrayList<HashMap<String, String>>();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.COMPANY_LIST, ServiceHandler.POST);
			Log.e("COMPANY_LIST response", json + "--");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {

				try {

					JSONArray jsonArray = new JSONArray(result);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.COMPANY_RESPONSE, result);
					for (int i = 0; i < jsonArray.length(); i++) {

						HashMap<String, String> headerMap = new HashMap<String, String>();
						headerMap.put("company_english_name", jsonArray.optJSONObject(i).optString("company_name"));
						headerMap.put("company_logo", jsonArray.optJSONObject(i).optString("company_logo"));
						headerMap.put("company_arabic_name", jsonArray.optJSONObject(i).optString("arabic_name"));
						headerMap.put("company_address_eng", jsonArray.optJSONObject(i).optString("branch_list_english"));
						headerMap.put("company_address_ar", jsonArray.optJSONObject(i).optString("branch_list_arabic"));
						headerMap.put("company_branch_count", jsonArray.optJSONObject(i).optString("Number of branch"));
						Utility.headerList.add(headerMap);

					}
					if (Utility.headerList.size() > 0) {
						purchaseHistoryView.setAdapter(new CompanyListAdapter(getActivity(), Utility.headerList));

					} else {
						purchaseHistoryView.setAdapter(null);
						alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_fun_fare));
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
