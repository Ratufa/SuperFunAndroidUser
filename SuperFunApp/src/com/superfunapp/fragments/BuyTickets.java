package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.ListBean;
import com.superfunapp.R;
import com.superfunapp.adapters.OfferListAdapter;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.LostLogoutDialog;
import com.superfunapp.utils.Utility;

public class BuyTickets extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle;
	
	private String amount = "", points = "0", tickets = "0";
	// ListView
	private ListView offerList;
	// Button
	private Button buyBtn;
	
	private ProgressDialog progressDialog;
	// Button
	private Button buttonTicket;
	
	private Button buttonCollect;
	
	private Button buttonHistory;
	
	private TextView chooseOffer;

	private AlertDialogManager alertDialogManager;
	
	private OfferListAdapter adapter;
	
	public static int selectedItem = -1;
	
	private ImageView backImage;
	
	private LostLogoutDialog logoutDialog;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		amount = "";
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("InflateParams")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.buy_tickets, container, false);
		offerList = (ListView) view.findViewById(R.id.offerList);
		View footer = getActivity().getLayoutInflater().inflate(R.layout.offer_list_footer, null);
		offerList.addFooterView(footer);
		View tabs = getActivity().findViewById(R.id.tabLayout);

		alertDialogManager = new AlertDialogManager();
		logoutDialog = new LostLogoutDialog();
		
		buttonCollect = (Button) getActivity().findViewById(R.id.collectBtn);
		buttonHistory = (Button) getActivity().findViewById(R.id.historyBtn);
		buttonTicket = (Button) getActivity().findViewById(R.id.buyBtn);
		chooseOffer = (TextView) view.findViewById(R.id.chooseOffer);

		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);

		textActionTitle.setTextSize(29f);
		buttonTicket.setSelected(true);
		buttonHistory.setSelected(false);
		buttonCollect.setSelected(false);
		
		Constants.mainActivityDisplay = true;

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
		
			Utility.setTextViewWidth("ar", textActionTitle);
			textActionTitle.setBackgroundResource(R.drawable.buy_ticket_title);
			textActionTitle.setText("");
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_arabic_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(R.drawable.log_arabic_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(R.drawable.collect_arabic_selector));
			Utility.setTextGabbalandFont(chooseOffer, getActivity());
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(R.string.ticekts_tab));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(R.drawable.ticket_english_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(R.drawable.log_english_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(R.drawable.collect_english_selector));
			Utility.setTextHelveticaBold(chooseOffer, getActivity());
		}

		backImage = (ImageView) getActivity().findViewById(R.id.cancelImage);
		backImage.setVisibility(View.INVISIBLE);
		ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);
		menu.setVisibility(View.VISIBLE);
		buyBtn = (Button) footer.findViewById(R.id.buyBtn);

	//	buyBtn.setOnClickListener(this);
		buyBtn.setOnTouchListener(new OnTouchListener() {        
		    @SuppressLint("ClickableViewAccessibility")
			@Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		            	buyBtn.setTextColor(Color.parseColor("#FFFFFF"));
		            	buyBtn.setBackgroundResource(R.drawable.buy_button_pressed);
		                return true; // if you want to handle the touch event
		            case MotionEvent.ACTION_UP:
		            	buyBtn.setTextColor(Color.parseColor("#E37C33"));
		            	buyBtn.setBackgroundResource(R.drawable.circle_dark);
		            	if (amount.length() != 0) {
		    				if (NetworkAvailablity.chkStatus(getActivity()))
		    					new BuyNow().execute(amount, points, tickets);
		    				else
		    					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));
		    			} else
		    				alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.message), getResources().getString(R.string.enter_amount));
		                return true; // if you want to handle the touch event
		            case MotionEvent.ACTION_OUTSIDE:
		            	buyBtn.setTextColor(Color.parseColor("#E37C33"));
		            	buyBtn.setBackgroundResource(R.drawable.circle_dark);
		            	if (amount.length() != 0) {
		    				if (NetworkAvailablity.chkStatus(getActivity()))
		    					new BuyNow().execute(amount, points, tickets);
		    				else
		    					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));
		    			} else
		    				alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.message), getResources().getString(R.string.enter_amount));
		    			return true;
		        }
		        return false;
		    }

		});

		Utility.setTextGabbalandFont(buyBtn, getActivity());

		if (NetworkAvailablity.chkStatus(getActivity()))
			if (Utility.offersList.size() == 0)
				new OfferList().execute();
			else {
				adapter = new OfferListAdapter(getActivity(), Utility.offersList);
				offerList.setAdapter(adapter);
			}
		else
			alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));

		offerList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {

				amount = Utility.offersList.get(pos).getAmount();
				points = Utility.offersList.get(pos).getPoint();
				tickets = Utility.offersList.get(pos).getTicket();
				adapter.changeBackgroundColor(pos);
			}

		});

		tabs.setVisibility(View.VISIBLE);

		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		//Constants.mainActivityDisplay = true;
		amount="";
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) getActivity(), Constants.USER_LANGAUGE), getActivity());
		Constants.OptionMenuOpen = true;
	}
	@Override
	public void onStart() {
		super.onStart();
		Constants.OptionMenuOpen = true;
	}
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		
	}

	/*@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
	//	menu.findItem(R.id.action_settings).setVisible(true);

	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

		switch (v.getId()) {
		case R.id.buyBtn:
			if (amount.length() != 0) {
				if (NetworkAvailablity.chkStatus(getActivity()))
					new BuyNow().execute(amount, points, tickets);
				else
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.net_error), getResources().getString(R.string.network_connection));
			} else
				alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.message), getResources().getString(R.string.enter_amount));

			break;

		default:
			break;
		}
	}
	
	// Offer List API implementation
	public class OfferList extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

			Utility.offersList = new ArrayList<ListBean>();
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
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.OFFER_LIST, ServiceHandler.POST);
			Log.e("OFFER_LIST response", json + "--");
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
					JSONObject jsonObject = new JSONObject(result);
					SharedPrefrnceSuperFun.setDataInSharedPrefrence(getActivity(), Constants.OFFER_RESPONSE, result);
					jsonArray = jsonObject.optJSONArray("detail");

					if (jsonArray != null && jsonArray.length() != 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							ListBean bean = new ListBean();

							bean.setAmount(jsonArray.optJSONObject(i).optString("amount"));
							bean.setPoint(jsonArray.optJSONObject(i).optString("point"));
							bean.setTicket(jsonArray.optJSONObject(i).optString("ticket"));

							Utility.offersList.add(bean);

						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}
				adapter = new OfferListAdapter(getActivity(), Utility.offersList);
				offerList.setAdapter(adapter);

			}

		}
	}

	// BuyNow API implementation
	public class BuyNow extends AsyncTask<String, String, String> {

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
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("Mobile", SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) getActivity(), Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("Quantity", params[0]));
			nameValuePairs.add(new BasicNameValuePair("points", params[1]));
			nameValuePairs.add(new BasicNameValuePair("tickets", params[2]));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(getActivity())));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.BUY_NOW, ServiceHandler.POST, nameValuePairs);
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

					String success = jsonObject.optString("data");
					if (success.equals("successful")) {

						JSONObject data = jsonObject.optJSONObject("payment");

						String webUrl = data.optString("PaymentURL");
						FragmentManager fm = ((HomeScreen) getActivity()).getSupportFragmentManager();
						Fragment fragment = new PaymentFragment();
						FragmentTransaction ft = fm.beginTransaction();
						Bundle bundle = new Bundle();
						bundle.putString("pay_url", webUrl);
						fragment.setArguments(bundle);
						ft.replace(R.id.content_frame, fragment);
						ft.addToBackStack(null);
						ft.commit();

					} else if(success.equals("Please login again")){
						logoutDialog.showAlertDialog(getActivity(), getResources().getString(R.string.alert), getResources().getString(R.string.please_login_again));
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}

			}
		}

	}

}
