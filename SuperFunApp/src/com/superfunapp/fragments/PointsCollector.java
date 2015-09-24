package com.superfunapp.fragments;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.superfunapp.servercommunication.ServiceHandler;
import com.superfunapp.servercommunication.WebServiceDetails;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class PointsCollector extends Fragment implements OnClickListener {

	// TextView
	private TextView textActionTitle;

	private TextView secondsTv, showText;

	private TextView pointsTv, pointTitle;

	private TextView ticketsTV, ticketTitle;

	private TextView mobTv, mobTitle;

	private TextView codeTv, codeTitle;

	// Button
	private Button doneBtn;

	private Handler customHandler = new Handler();

	public static int counter = 30;

	private Fragment fragment;

	private FragmentManager fm;

	private FragmentTransaction ft;

	private ProgressDialog progressDialog;

	//private AlertDialogManager alertDialogManager;
	
	private Activity mActivity;
	
	private boolean showDialog = false;
	
	@SuppressWarnings("deprecation")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.points_collector, container, false);
		//alertDialogManager = new AlertDialogManager();
		progressDialog = new ProgressDialog(getActivity());
		progressDialog.setMessage(getResources().getString(R.string.please_wait));
		progressDialog.setIndeterminate(true);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progressDialog.setCancelable(false);

		secondsTv = (TextView) view.findViewById(R.id.secondsTv);
		pointsTv = (TextView) view.findViewById(R.id.collectorPoints);
		ticketsTV = (TextView) view.findViewById(R.id.collectorTickets);
		mobTv = (TextView) view.findViewById(R.id.collectorMob);
		codeTv = (TextView) view.findViewById(R.id.collectorCode);

		showText = (TextView) view.findViewById(R.id.showPage);
		ticketTitle = (TextView) view.findViewById(R.id.totalTickText);
		pointTitle = (TextView) view.findViewById(R.id.totalPoinText);
		mobTitle = (TextView) view.findViewById(R.id.mobText);
		codeTitle = (TextView) view.findViewById(R.id.confirmText);

		Constants.mainActivityDisplay = false;

		ImageView menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		menuImage.setVisibility(View.INVISIBLE);

		ImageView backImage = (ImageView) getActivity().findViewById(R.id.cancelImage);
		backImage.setVisibility(View.VISIBLE);

		textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);

		textActionTitle.setTextSize(29f);
		
		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("ar")) {
			textActionTitle.setBackgroundResource(R.drawable.point_title);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
			Utility.setTextGabbalandFont(ticketTitle, getActivity());
			Utility.setTextGabbalandFont(pointTitle, getActivity());
			Utility.setTextGabbalandFont(mobTitle, getActivity());
			Utility.setTextGabbalandFont(codeTitle, getActivity());
			Utility.setTextGabbalandFont(showText, getActivity());
		} else {
			Utility.setTextViewWidth("en", textActionTitle);
			Utility.setTextHelveticaBold(showText, getActivity());
			Utility.setTextHelveticaBold(ticketTitle, getActivity());
			Utility.setTextHelveticaBold(pointTitle, getActivity());
			Utility.setTextHelveticaBold(mobTitle, getActivity());
			Utility.setTextHelveticaBold(codeTitle, getActivity());
			textActionTitle.setText(getResources().getString(R.string.points_collector));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);
		}

		
		Utility.setTextHelveticaBold(mobTv, getActivity());
		Utility.setTextHelveticaBold(pointsTv, getActivity());
		Utility.setTextHelveticaBold(ticketsTV, getActivity());
		Utility.setTextHelveticaBold(codeTv, getActivity());

		mobTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_MOB));
		pointsTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.TOTAL_POINTS));
		ticketsTV.setText(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.TOTAL_TICKETS));
		codeTv.setText(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.CONFIRMATION_CODE));

		doneBtn = (Button) view.findViewById(R.id.pointDoneBtn);
		doneBtn.setOnClickListener(this);
		backImage.setOnClickListener(this);

		customHandler.postDelayed(updateTimerThread, 1000);
		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.GONE);
		// setupActionBar();
		// setHasOptionsMenu(true);
		return view;
	}

	/*@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_settings).setVisible(false);
	}*/
	
	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		mActivity = activity;
	}

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity()).getSupportActionBar();
		actionBar.setIcon(R.drawable.close);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout, null);
		textActionTitle = (TextView) mCustomView.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.points_collector));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(R.drawable.new_header));

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.cancelImage:
			counter = 30;
			if (updateTimerThread != null)
				customHandler.removeCallbacks(updateTimerThread);
			//new CheckStatus().cancel(true);
		    new ResetCode().execute();
			
//			fragment = new TicketPoints();
//			m = ((HomeScreen) getActivity()).getSupportFragmentManager();
//			ft = fm.beginTransaction();
//			ft.replace(R.id.content_frame, fragment);
//			// ft.addToBackStack(fm.getClass().getName());
//			ft.commit();
			break;

		default:
			break;
		}
	}

	private Runnable updateTimerThread = new Runnable() {

		public void run() {

			counter--;
			if (counter >= 0) {
				new CheckStatus().execute();
				secondsTv.setText(counter + "");
				customHandler.postDelayed(this, 1000);
			} else {
				secondsTv.setText("0");
				counter = 30;
				// progressDialog.dismiss();
				new CheckStatus().cancel(true);
				customHandler.removeCallbacks(updateTimerThread);
			}

		}

	};
	


	public class CheckStatus extends AsyncTask<String, String, String> {

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();

		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			ServiceHandler serviceHandler = new ServiceHandler();
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("mob_no", SharedPrefrnceSuperFun.getSharedPrefData(mActivity, Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("confirmation_code", SharedPrefrnceSuperFun.getSharedPrefData(mActivity, Constants.CONFIRMATION_CODE)));
			nameValuePairs.add(new BasicNameValuePair("operation", "collect_point"));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(mActivity)));
			if(counter==-1){
				counter++;
			}
			nameValuePairs.add(new BasicNameValuePair("timer_status", String.valueOf(counter)));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.TIMER_TEMP, ServiceHandler.POST, nameValuePairs);
			Log.e("timer temp json`", json + "");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			if (result.length() != 0) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.optString("data").equals("success")) {
						if(!showDialog){
						if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("en")) {
							showAlert(getResources().getString(R.string.points_collected).concat(" ").concat(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.TOTAL_POINTS)).concat("\n\n").concat(getResources().getString(R.string.branch_name)).concat(" ").concat(jsonObject.optString("detail")).concat("\n\n").concat(getResources().getString(R.string.location_name)).concat(" ").concat(jsonObject.optString("location_english")));
						} else {
							showAlert(getResources().getString(R.string.points_collected).concat(" ").concat(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.TOTAL_POINTS)).concat("\n\n").concat(getResources().getString(R.string.branch_name)).concat(" ").concat(jsonObject.optString("detail_arabic")).concat("\n\n").concat(getResources().getString(R.string.location_name)).concat(" ").concat(jsonObject.optString("location_arabic")));
						}
										
						}
						showDialog = true; 
						customHandler.removeCallbacks(updateTimerThread);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}
			}
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();			
			if(progressDialog.isShowing())
				progressDialog.dismiss();
			
			if (updateTimerThread != null)
				customHandler.removeCallbacks(updateTimerThread);

			//if(!mActivity.isFinishing() && mActivity!=null){
			Fragment fragment = new TicketPoints();
			FragmentManager fm = getFragmentManager();//((HomeScreen) getActivity()).getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			ft.replace(R.id.content_frame, fragment);
			// ft.addToBackStack(fm.getClass().getName());
			ft.commitAllowingStateLoss();
			//}
		}

	}
	
	public class ResetCode extends AsyncTask<String, String, String> {

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
			nameValuePairs.add(new BasicNameValuePair("mob_no", SharedPrefrnceSuperFun.getSharedPrefData(mActivity, Constants.USER_MOB)));
			nameValuePairs.add(new BasicNameValuePair("confirmation_code", SharedPrefrnceSuperFun.getSharedPrefData(mActivity, Constants.CONFIRMATION_CODE)));
			nameValuePairs.add(new BasicNameValuePair("operation", "collect_point"));
			nameValuePairs.add(new BasicNameValuePair("device_id", Utility.getDeviceId(mActivity)));
			nameValuePairs.add(new BasicNameValuePair("timer_status","0"));
			String json = serviceHandler.makeServiceCall(WebServiceDetails.WS_URL + WebServiceDetails.TIMER_TEMP, ServiceHandler.POST, nameValuePairs);
			Log.e("timer temp json`", json + "");
			return json;
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
		
			new CheckStatus().cancel(true);
			
			if (result.length() != 0) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					if (jsonObject.optString("data").equals("success")) {
						if(!showDialog){
							if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.USER_LANGAUGE).equals("en")) {
								showAlert(getResources().getString(R.string.points_collected).concat(" ").concat(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.TOTAL_POINTS)).concat("\n\n").concat(getResources().getString(R.string.branch_name)).concat(" ").concat(jsonObject.optString("detail")).concat("\n\n").concat(getResources().getString(R.string.location_name)).concat(" ").concat(jsonObject.optString("location_english")));
							} else {
								showAlert(getResources().getString(R.string.points_collected).concat(" ").concat(SharedPrefrnceSuperFun.getSharedPrefData(getActivity(), Constants.TOTAL_POINTS)).concat("\n\n").concat(getResources().getString(R.string.branch_name)).concat(" ").concat(jsonObject.optString("detail_arabic")).concat("\n\n").concat(getResources().getString(R.string.location_name)).concat(" ").concat(jsonObject.optString("location_arabic")));
							}
						}
						showDialog = true; 
						customHandler.removeCallbacks(updateTimerThread);

					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					//alertDialogManager.showAlertDialog(getActivity(), getResources().getString(R.string.error), getResources().getString(R.string.server_communication));
				}
			}
		}

		

	}
	
	
	public void showAlert(String message) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				getActivity());

		// set title
		alertDialogBuilder.setTitle(getResources().getString(R.string.message));

		// set dialog message
		alertDialogBuilder
				.setMessage(message)
				.setCancelable(false)
				.setPositiveButton(getResources().getString(R.string.ok),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								//new LogOut().execute();
								fragment = new TicketPoints();
								fm = ((HomeScreen) getActivity()).getSupportFragmentManager();
								ft = fm.beginTransaction();
								ft.replace(R.id.content_frame, fragment);
								//ft.addToBackStack(fm.getClass().getName());
								ft.commit();
							}
						});
				

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();
	}
	
//	@Override
//	public void onDetach() {
//		// TODO Auto-generated method stub
//		super.onDetach();
//		//new CheckStatus().cancel(true);
//		customHandler.removeCallbacks(updateTimerThread);
//
//		fragment = new TicketPoints();
//		fm = ((HomeScreen) getActivity()).getSupportFragmentManager();
//		ft = fm.beginTransaction();
//		ft.replace(R.id.content_frame, fragment);
//		// ft.addToBackStack(fm.getClass().getName());
//		ft.commit();
//	}
	
	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		super.onDestroyView();
		//new CheckStatus().cancel(true);
		customHandler.removeCallbacks(updateTimerThread);
		fragment = new TicketPoints();
		fm = ((HomeScreen) getActivity()).getSupportFragmentManager();
		ft = fm.beginTransaction();
		ft.replace(R.id.content_frame, fragment);
		// ft.addToBackStack(fm.getClass().getName());
		ft.commitAllowingStateLoss();
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Constants.OptionMenuOpen = false;
	}
	
	@Override
	public void onResume() {
		super.onResume();
		Constants.mainActivityDisplay = false;
		Constants.OptionMenuOpen = false;
	}

}
