package com.superfunapp.fragments;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class PaymentFragment extends Fragment {

	// TextView
	private WebView payView;

	@SuppressWarnings("deprecation")
	@SuppressLint("SetJavaScriptEnabled")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater
				.inflate(R.layout.payment_gateway, container, false);
		payView = (WebView) view.findViewById(R.id.paymentWebView);
		//alertDialogManager = new AlertDialogManager();
		View tabs = getActivity().findViewById(R.id.tabLayout);
		tabs.setVisibility(View.GONE);
		
		ImageView menuImage = (ImageView) getActivity().findViewById(R.id.menuImage);
		menuImage.setVisibility(View.INVISIBLE);
		
		ImageView back = (ImageView) getActivity().findViewById(
				R.id.cancelImage);
		back.setVisibility(View.VISIBLE);

		TextView textActionTitle = (TextView) getActivity().findViewById(R.id.screenTitle);
		
		Constants.mainActivityDisplay = false;
		//Constants.OptionMenuOpen = false;
		
		textActionTitle.setTextSize(29f);
		if (SharedPrefrnceSuperFun.getSharedPrefData(getActivity(),
				Constants.USER_LANGAUGE).equals("ar")) {	
			textActionTitle.setBackgroundResource(R.drawable.payment_title);
			textActionTitle.setText("");
			Utility.setTextViewWidth("ar", textActionTitle);
		} else{
			Utility.setTextViewWidth("en", textActionTitle);
			textActionTitle.setText(getResources().getString(
					R.string.Payment_Gateway));
			Utility.setTextGabbalandFont(textActionTitle, getActivity());
			textActionTitle.setBackgroundDrawable(null);	
		}
		
		payView.getSettings().setJavaScriptEnabled(true);
		payView.setWebViewClient(new MyWebViewClient());
		
		Bundle bundle = this.getArguments();
		payView.loadUrl(bundle.getString("pay_url","")+"");

		return view;
	}

	class MyWebViewClient extends WebViewClient {
		
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			view.loadUrl(url);
			
			return true;
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			//progressDialog.dismiss();
			Log.e("payment url in finished>>>>>>>", url+"---");
			super.onPageFinished(view, url);
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			//progressDialog.show();
			Log.e("payment url in started>>>>>>>", url+"---");
			super.onPageStarted(view, url, favicon);
		}
	}

	/*@Override
	public void onPrepareOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onPrepareOptionsMenu(menu);
		menu.findItem(R.id.action_settings).setVisible(false);
	}*/

	@SuppressLint("InflateParams")
	private void setupActionBar() {

		ActionBar actionBar = ((HomeScreen) getActivity())
				.getSupportActionBar();
		actionBar.setIcon(R.drawable.close);
		actionBar.setDisplayHomeAsUpEnabled(false);
		actionBar.setHomeButtonEnabled(true);
		actionBar.setDisplayShowHomeEnabled(true);

		LayoutInflater mInflater = LayoutInflater.from(getActivity());

		View mCustomView = mInflater.inflate(R.layout.custom_action_layout,
				null);
		TextView textActionTitle = (TextView) mCustomView
				.findViewById(R.id.screenTitle);
		textActionTitle.setText(getResources().getString(R.string.Payment_Gateway));
		Utility.setTextGabbalandFont(textActionTitle, getActivity());
		//imageViewBack = (ImageView) mCustomView.findViewById(R.id.backBtn);
		//imageViewBack.setVisibility(View.GONE);

		actionBar.setCustomView(mCustomView);
		actionBar.setDisplayShowCustomEnabled(true);

		actionBar.setBackgroundDrawable(getResources().getDrawable(
				R.drawable.new_header));

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
