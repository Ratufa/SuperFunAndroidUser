package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
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
import com.superfunapp.adapters.HowItAdapter;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class HowItFragment extends Fragment {

	// TextView
	private TextView textActionTitle;
	// ImageView
	// private ImageView imageViewBack;
	//private ArrayList<HashMap<String, String>> arHashMaps;
	// ListView
	private ListView howList;

	private AlertDialogManager alertDialogManager;

	private ProgressDialog progressDialog;

	private ImageView menuImage;

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.how_it_works, container, false);
		menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		menuImage.setVisibility(View.VISIBLE);
		ImageView back = (ImageView) getActivity().findViewById(R.id.cancelImage);
		back.setVisibility(View.VISIBLE);
		//ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		//menu.setVisibility(View.VISIBLE);
		
		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.How_to_use_the_App));
		
		Constants.mainActivityDisplay = false;

		textActionTitle.setTextSize(29f);
		
		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.how_to);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		alertDialogManager = new AlertDialogManager();
		howList = (ListView) view.findViewById(R.id.listView1);
		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.GONE);

		if (NetworkAvailablity.chkStatus(getActivity())) {
			
			
			 if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("en")) {
				if (Utility.howToEngList.size() == 0)
					new HowIt().execute();
				else{
					HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToEngList);
					howList.setAdapter(adapter);
				}
			}else if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
				if (Utility.howToListArabic.size() == 0)
					new HowIt().execute();
				else{
					HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToListArabic);
					howList.setAdapter(adapter);
				}
			}
			
//			if (Utility.howToListArabic.size() == 0 && Utility.howToEngList.size() == 0)
//				new HowIt().execute();
//			else{
//				
//				if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("en")) {
//					HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToEngList);
//					if(Utility.howToEngList.size()>0)
//					howList.setAdapter(adapter);
//					else
//						alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error),getResources().getString(R.string.no_data));
//				}else{
//					
//					HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToListArabic);
//					if(Utility.howToListArabic.size()>0)
//					howList.setAdapter(adapter);
//					else
//						alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error),getResources().getString(R.string.no_data));
//				}
//				
//			}
				
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
		textActionTitle.setText(getResources().getString(R.string.How_to_use_the_App));

		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	// Offer List API implementation
	public class HowIt extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			Utility.howToListArabic = new ArrayList<HashMap<String, String>>();
			Utility.howToEngList = new ArrayList<HashMap<String, String>>();
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
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar"))
				nameValuePairs.add(new BasicNameValuePair("type", "arabic"));
			else
				nameValuePairs.add(new BasicNameValuePair("type", "english"));

			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.HOW_IT_WORKS, ServiceHandler.POST, nameValuePairs);
			Log.e("howitworks response", json + "--");
			return json;
		}

		
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();

//			
			if (!Utility.isNullOrEmpty(result)) {
				try {
					JSONArray jsonArray = new JSONArray(result);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.HOW_TO_RESPONSE, result);

					if (jsonArray != null) {
						for (int i = 0; i < jsonArray.length(); i++) {

							HashMap<String, String> map = new HashMap<String, String>();
							map.put("key_image", jsonArray.optJSONObject(i).optString("img_url"));
							
							
							if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
								Utility.howToListArabic.add(map);
							}else{
								//howList.setAdapter(new HowItAdapter(getActivity(),Utility.howToEngList));
								Utility.howToEngList.add(map);
							}
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}
				//Log.e("arHashMaps response", arHashMaps + "--");
				
				
				if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("en")) {
					HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToEngList);
					if(Utility.howToEngList.size()>0)
					howList.setAdapter(adapter);
					else
						alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error),getResources().getString(R.string.no_data));
				}else{
					//howList.setAdapter(new HowItAdapter(getActivity(),Utility.howToEngList));
					HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToListArabic);
					if(Utility.howToListArabic.size()>0)
					howList.setAdapter(adapter);
					else
						alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error),getResources().getString(R.string.no_data));
				}
				
//				HowItAdapter adapter = new HowItAdapter(getActivity(), Utility.howToList);
//				if(Utility.howToList.size()>0)
//				howList.setAdapter(adapter);
//				else
//					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error),getResources().getString(R.string.no_data));

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
