package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.servercommunication.NetworkAvailablity;
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.AlertDialogManager;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.LostLogoutDialog;
import com.superfunapp.utils.Utility;

public class TicketPoints extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle, mobNumberTv, totalTicketTv,
			totalPointsTv, showCashierTv,ticketBtnTitle,pointBtnTitle;

	// Button
	private Button collectPoint, collectTicket;

	private Fragment fragment;

	private FragmentManager fm;

	private FragmentTransaction ft;

	private ProgressDialog progressDialog;

	private String total_tickets = "0", total_points = "0";

	// TextView
	private TextView buttonTicket;

	private TextView buttonCollect;

	private TextView buttonHistory;

	private TextView mobTitle;

	private TextView ticktTitle;

	private TextView pointTitle;

	private AlertDialogManager alertDialogManager;

	private ImageView backImage;

	private Activity mActivity;

	private LinearLayout scrollView;
	
	private LostLogoutDialog lostLogoutDialog; 

	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.ticket_points_new, container, false);
		lostLogoutDialog = new LostLogoutDialog();
		
		buttonCollect = (TextView) getActivity().findViewById(R.id.collectBtn);

		buttonHistory = (TextView) getActivity().findViewById(R.id.historyBtn);

		buttonTicket = (TextView) getActivity().findViewById(R.id.buyBtn);

		mobTitle = (TextView) view.findViewById(R.id.mobTitle);

		scrollView = (LinearLayout) view.findViewById(R.id.layoutScroll);

		ticktTitle = (TextView) view.findViewById(R.id.totTicktTitle);

		pointTitle = (TextView) view.findViewById(R.id.pointTitle);
		
		ticketBtnTitle = (TextView) view.findViewById(R.id.ticketBtnTitle);

		pointBtnTitle = (TextView) view.findViewById(R.id.pointBtnTitle);

		backImage = (ImageView) getActivity().findViewById(R.id.cancelImage);

		collectPoint = (Button) view.findViewById(R.id.collectPointBtn);

		collectTicket = (Button) view.findViewById(R.id.collectTicketBtn);

		backImage.setVisibility(View.INVISIBLE);

		textActionTitle = (TextView) getActivity().findViewById(
				R.id.screenTitle);

		showCashierTv = (TextView) view.findViewById(R.id.showCashierTv);

		Utility.setTextHelveticaBold(showCashierTv, getActivity());
		Utility.setTextHelveticaBold(pointTitle, getActivity());
		Utility.setTextHelveticaBold(ticktTitle, getActivity());
		Utility.setTextHelveticaBold(mobTitle, getActivity());
		Utility.setTextHelveticaBold(ticketBtnTitle, getActivity());
		Utility.setTextHelveticaBold(pointBtnTitle, getActivity());

		buttonTicket.setSelected(false);
		buttonHistory.setSelected(false);
		buttonCollect.setSelected(true);

		Constants.mainActivityDisplay = false;

		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
				Constants.USER_LANGAUGE).equals("ar")) {

			Utility.setTextGabbalandFont(showCashierTv, getActivity());
			Utility.setTextGabbalandFont(pointTitle, getActivity());
			Utility.setTextGabbalandFont(ticktTitle, getActivity());
			Utility.setTextGabbalandFont(mobTitle, getActivity());
			Utility.setTextGabbalandFont(ticketBtnTitle, getActivity());
			Utility.setTextGabbalandFont(pointBtnTitle, getActivity());

			Utility.setTextViewWidth("ar", textActionTitle);
			textActionTitle.setBackgroundResource(R.drawable.collect_title);
			textActionTitle.setText("");
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ticket_arabic_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.log_arabic_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.collect_arabic_selector));
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(
					R.string.Collecting_Tickets_Points));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			Utility.setTextHelveticaBold(showCashierTv, getActivity());
			Utility.setTextHelveticaBold(pointTitle, getActivity());
			Utility.setTextHelveticaBold(ticktTitle, getActivity());
			Utility.setTextHelveticaBold(mobTitle, getActivity());
			Utility.setTextHelveticaBold(ticketBtnTitle, getActivity());
			Utility.setTextHelveticaBold(pointBtnTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
			buttonTicket.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.ticket_english_selector));
			buttonHistory.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.log_english_selector));
			buttonCollect.setBackgroundDrawable(getResources().getDrawable(
					R.drawable.collect_english_selector));
		}
		textActionTitle.setTextSize(27f);
		ImageView menu = (ImageView) getActivity().findViewById(R.id.menuImage);

		menu.setVisibility(View.VISIBLE);

		alertDialogManager = new AlertDialogManager();

		mobNumberTv = (TextView) view.findViewById(R.id.mobNumber);
		totalTicketTv = (TextView) view.findViewById(R.id.totTickets);
		totalPointsTv = (TextView) view.findViewById(R.id.totPoints);

		

		Utility.setTextHelveticaBold(totalTicketTv, getActivity());
		Utility.setTextHelveticaBold(totalPointsTv, getActivity());
		Utility.setTextHelveticaBold(mobNumberTv, getActivity());

		Utility.setTextGabbalandFont(buttonCollect, getActivity());
		Utility.setTextGabbalandFont(buttonHistory, getActivity());
		Utility.setTextGabbalandFont(buttonTicket, getActivity());

		collectTicket.setOnClickListener(this);
		collectPoint.setOnClickListener(this);

		if (NetworkAvailablity.chkStatus(getActivity()))
			new Tickets_Points().execute();
		else
			alertDialogManager.showAlertDialog(getActivity(), getResources()
					.getString(R.string.net_error),
					getResources().getString(R.string.network_connection));

		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.VISIBLE);

		// setupActionBar();
		setHasOptionsMenu(true);
		
		collectTicket.setOnTouchListener(new OnTouchListener() {        
		    @SuppressLint("ClickableViewAccessibility")
			@Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		            	//collectTicket.setTextColor(Color.parseColor("#FFFFFF"));
		            	collectTicket.setSelected(true);
		            	if (!total_tickets.equals("0")) {
		    				if (NetworkAvailablity.chkStatus(getActivity()))
		    					new CollectTickets().execute();
		    				else
		    					alertDialogManager.showAlertDialog(
		    							getActivity(),
		    							getResources().getString(R.string.net_error),
		    							getResources().getString(
		    									R.string.network_connection));
		    			} else {

		    				alertDialogManager.showAlertDialog(getActivity(),
		    						getResources().getString(R.string.error),
		    						getResources().getString(R.string.not_enough_tickets));

		    			}

		                return true; // if you want to handle the touch event
		            case MotionEvent.ACTION_UP:
		            	//collectTicket.setTextColor(Color.parseColor("#E37C33"));
		            	collectTicket.setSelected(false);
		                return true; // if you want to handle the touch event
		            case MotionEvent.ACTION_OUTSIDE:
		            	
		    			return true;
		        }
		        return false;
		    }

		});
		
		
		collectPoint.setOnTouchListener(new OnTouchListener() {        
		    @SuppressLint("ClickableViewAccessibility")
			@Override
		    public boolean onTouch(View v, MotionEvent event) {
		        switch(event.getAction()) {
		            case MotionEvent.ACTION_DOWN:
		            	//collectPoint.setTextColor(Color.parseColor("#FFFFFF"));
		            	collectPoint.setSelected(true);
		            	//collectPoint.setBackgroundResource(R.drawable.buy_button_pressed);
		            	if (!total_points.equals("0")) {
		    				if (NetworkAvailablity.chkStatus(getActivity()))
		    					new CollectPoints().execute();
		    				else
		    					alertDialogManager.showAlertDialog(
		    							getActivity(),
		    							getResources().getString(R.string.net_error),
		    							getResources().getString(
		    									R.string.network_connection));
		    			} else {
		    				alertDialogManager.showAlertDialog(getActivity(),
		    						getResources().getString(R.string.error),
		    						getResources().getString(R.string.not_enough_points));

		    			}
		                return true; // if you want to handle the touch event
		            case MotionEvent.ACTION_UP:
		            	//collectPoint.setTextColor(Color.parseColor("#E37C33"));
		            	collectPoint.setSelected(false);
		            	 return true; // if you want to handle the touch event
		            case MotionEvent.ACTION_OUTSIDE:
		            	
		        }
		        return false;
		    }

		});
		return view;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Constants.OptionMenuOpen = true;
		Constants.mainActivityDisplay = false;
		Utility.changeLangauge(SharedPrefrnceSuperFun.getSharedPrefData(
				(HomeScreen) getActivity(), Constants.USER_LANGAUGE),
				getActivity());
	}

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity())
				.getSupportActionBar();
		Drawable transparentDrawable = new ColorDrawable(Color.TRANSPARENT);
		actionBar.setIcon(transparentDrawable);
		actionBar.setTitle("");
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(false);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout,
				null);
		textActionTitle = (TextView) getActivity().findViewById(
				R.id.screenTitle);
		textActionTitle.setText(getResources().getString(
				R.string.Collecting_Tickets_Points));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());
		// imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);
		// imageViewBack.setVisibility(View.INVISIBLE);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.new_header));
		
		

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.collectTicketBtn:

			if (!total_tickets.equals("0")) {
				if (NetworkAvailablity.chkStatus(getActivity()))
					new CollectTickets().execute();
				else
					alertDialogManager.showAlertDialog(
							getActivity(),
							getResources().getString(R.string.net_error),
							getResources().getString(
									R.string.network_connection));
			} else {

				alertDialogManager.showAlertDialog(getActivity(),
						getResources().getString(R.string.error),
						getResources().getString(R.string.not_enough_tickets));

			}

			break;
		case R.id.collectPointBtn:
			if (!total_points.equals("0")) {
				if (NetworkAvailablity.chkStatus(getActivity()))
					new CollectPoints().execute();
				else
					alertDialogManager.showAlertDialog(
							getActivity(),
							getResources().getString(R.string.net_error),
							getResources().getString(
									R.string.network_connection));
			} else {
				alertDialogManager.showAlertDialog(getActivity(),
						getResources().getString(R.string.error),
						getResources().getString(R.string.not_enough_points));

			}

			break;

		default:
			break;
		}
	}

	// Collect ticket API implementation
	public class CollectTickets extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(
					R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mob_no",
					SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
							Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility
					.getDeviceId(getActivity())));
			String json = serviceHandler.makeServiceCall(
					WebServiceDetails.WS_URL
							+ WebServiceDetails.COLLECT_TICKETS,
					ServiceHandler.POST, nameValuePairs);
			Log.e("COLLECT_TICKETS response", json + "--");
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
					if (jsonObject.has("data")) {
						if (jsonObject.optString("message").equals(
								"Ticket allready collect"))
							alertDialogManager.showAlertDialog(
									getActivity(),
									getResources().getString(R.string.message),
									getResources().getString(
											R.string.ticket_message));
						total_tickets="0";
						totalTicketTv.setText("0");

					} else {
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								getActivity(), Constants.CONFIRMATION_CODE,
								jsonObject.optString("confirmation_code"));
						fragment = new TicketsCollector();
						fm = ((HomeScreen) getActivity())
								.getSupportFragmentManager();
						ft = fm.beginTransaction();
						ft.replace(R.id.content_frame, fragment, "contact");
						ft.addToBackStack(fm.getClass().getName());
						ft.commit();
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.server_communication));
				}

			}

		}

	}

	// Collect points API implementation
	public class CollectPoints extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
			progressDialog.setMessage(getResources().getString(
					R.string.please_wait));
			progressDialog.setIndeterminate(true);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			progressDialog.setCancelable(false);
			if (!progressDialog.isShowing())
				progressDialog.show();
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mob_no",
					SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
							Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility
					.getDeviceId(mActivity)));
			String json = serviceHandler
					.makeServiceCall(WebServiceDetails.WS_URL
							+ WebServiceDetails.COLLECT_POINTS,
							ServiceHandler.POST, nameValuePairs);
			Log.e("COLLECT_POINTS response", json + "--");
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
					if (jsonObject.has("data")) {
						if (jsonObject.optString("message").equals(
								"Point allready collect"))
						total_points="0";	
						
						totalPointsTv.setText("0");
						
						alertDialogManager.showAlertDialog(getActivity(),
								getResources().getString(R.string.message),
								getResources()
										.getString(R.string.point_message));

					} else {
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								getActivity(), Constants.CONFIRMATION_CODE,
								jsonObject.optString("confirmation_code"));
						fragment = new PointsCollector();
						fm = ((HomeScreen) getActivity())
								.getSupportFragmentManager();
						ft = fm.beginTransaction();
						ft.replace(R.id.content_frame, fragment, "contact");
						ft.addToBackStack(fm.getClass().getName());
						ft.commit();
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					alertDialogManager.showAlertDialog(
							getActivity(),
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.server_communication));
				}

			}

		}

	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}

	// Ticket n points info API implementation
	public class Tickets_Points extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			progressDialog = new ProgressDialog(getActivity());
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
			nameValuePairs.add(new BasicNameValuePair("mob_no",
					SharedPrefrnceSuperFun.getSharedPrefData(mActivity,
							Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility
					.getDeviceId(mActivity)));
			String json = serviceHandler.makeServiceCall(
					WebServiceDetails.WS_URL
							+ WebServiceDetails.USER_TICKET_INFO,
					ServiceHandler.POST, nameValuePairs);
			Log.e("USER_TICKET_INFO response", json + "--");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			progressDialog.dismiss();
			mobNumberTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(
					mActivity, Constants.USER_MOB));
			if (result.length() != 0) {

				try {
					JSONObject jsonObject = new JSONObject(result);

					if (jsonObject.has("data")) {
						if (jsonObject.optString("data").equals(
								"data not found")) {
							totalTicketTv.setText(total_tickets + "");
							totalPointsTv.setText(total_points + "");
							scrollView.setVisibility(View.VISIBLE);
						} else if (jsonObject.optString("data").equals(
								"Please login again")) {

							scrollView.setVisibility(View.INVISIBLE);
							totalTicketTv.setText(total_tickets + "");
							totalPointsTv.setText(total_points + "");
							// show alert dialog
							lostLogoutDialog.showAlertDialog(
									getActivity(),
									getResources().getString(R.string.alert),
									getResources().getString(
											R.string.please_login_again));
						}
					} else {
						scrollView.setVisibility(View.VISIBLE);
						total_tickets = jsonObject.optString("tickets_bal");
						total_points = jsonObject.optString("points_bal");
						totalTicketTv.setText(total_tickets + "");
						totalPointsTv.setText(total_points + "");
						SharedPrefrnceSuperFun
								.setDataInSharedPrefrence(mActivity,
										Constants.TOTAL_POINTS, total_points);
						SharedPrefrnceSuperFun.setDataInSharedPrefrence(
								mActivity, Constants.TOTAL_TICKETS,
								total_tickets);
					}

				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					scrollView.setVisibility(View.VISIBLE);
					alertDialogManager.showAlertDialog(
							mActivity,
							getResources().getString(R.string.error),
							getResources().getString(
									R.string.server_communication));
				}

			}
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Constants.OptionMenuOpen = true;
	}

}
