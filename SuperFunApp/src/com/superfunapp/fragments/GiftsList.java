package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;

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
import com.superfunapp.adapters.GiftListAdapter;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class GiftsList extends Fragment {

	// TextView
	private TextView textActionTitle;

	private AlertDialogManager alertDialogManager;
	
	private ProgressDialog progressDialog;

	private ListView listView;

	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.gift_list, container, false);
		alertDialogManager = new AlertDialogManager();
		View tabs = getActivity().findViewById(R.id.tabLayout);
		listView = (ListView) view.findViewById(R.id.giftList);
		tabs.setVisibility(View.GONE);

		View header = getActivity().getLayoutInflater().inflate(R.layout.list_header, null);
		listView.addHeaderView(header);

		ImageView menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		menuImage.setVisibility(View.VISIBLE);

		ImageView cancel = (ImageView) getActivity().findViewById(R.id.cancelImage);
		cancel.setVisibility(View.VISIBLE);
		
		//ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		//menu.setVisibility(View.VISIBLE);
		
		Constants.mainActivityDisplay = false;
		
		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);
		textActionTitle.setTextSize(29f);
		
		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.superfun_giftslist);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(R.string.gift_mennu));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		// Gift List API call
		if (NetworkAvailablity.chkStatus(getActivity())) {
			if (Utility.giftList.size() == 0)
				new GiftList().execute();
			else
				listView.setAdapter(new GiftListAdapter(getActivity(), Utility.giftList));
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
		actionBar.setTitle("");
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.gift_mennu));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());
		// imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);
		// imageViewBack.setVisibility(View.GONE);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	// Gift List API implementation

	public class GiftList extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utility.giftList = new ArrayList<HashMap<String, String>>();
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
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.GIFTS_LIST, ServiceHandler.POST);
			Log.e("GIFTS_LIST response", json + "--");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			if (result.length() != 0) {

				JSONArray jsonArray = null;
				try {
					jsonArray = new JSONArray(result);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.GIFT_RESPONSE, result);
					if (jsonArray != null) {
						for (int i = 0; i < jsonArray.length(); i++) {

							HashMap<String, String> map = new HashMap<String, String>();

							map.put("gift_name_en", jsonArray.optJSONObject(i).optString("english_name"));
							map.put("gift_name_ar", jsonArray.optJSONObject(i).optString("arabic_name"));
							map.put("gift_points", jsonArray.optJSONObject(i).optString("worth_points"));
							map.put("gift_image", jsonArray.optJSONObject(i).optString("image"));

							Utility.giftList.add(map);
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

				if (Utility.giftList.size() > 0) {
					listView.setAdapter(new GiftListAdapter(getActivity(), Utility.giftList));
				} else {
					listView.setAdapter(null);
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.no_gifts));
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
