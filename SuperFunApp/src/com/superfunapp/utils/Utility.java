package com.superfunapp.utils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import com.superfunapp.ListBean;

public class Utility {

	public static ArrayList<HashMap<String, String>> headerList = new ArrayList<>();

	public static ArrayList<HashMap<String, String>> giftList = new ArrayList<>();

	public static ArrayList<HashMap<String, String>> purchaseHistoryList = new ArrayList<>();

	public static ArrayList<HashMap<String, String>> howToListArabic = new ArrayList<>();

	public static ArrayList<String> contactUsList = new ArrayList<>();

	public static ArrayList<String> privacyList = new ArrayList<>();

	public static ArrayList<String> termsList = new ArrayList<>();

	public static ArrayList<ListBean> offersList = new ArrayList<>();
	
	public static ArrayList<HashMap<String, String>> howToEngList = new ArrayList<>();

	public static String getSimNumber(Context ctx) {

		TelephonyManager telemamanger = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		String getSimSerialNumber = telemamanger.getSimSerialNumber();

		return getSimSerialNumber;

	}

	@SuppressWarnings("deprecation")
	public static String getLocalIpAddress() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						return Formatter.formatIpAddress(inetAddress.hashCode());
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}
		return null;
	}

	public static String Convert24to12(String time) {
		String convertedTime = "";
		try {
			SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
			SimpleDateFormat parseFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
			Date date = parseFormat.parse(time);
			convertedTime = displayFormat.format(date);
			// System.out.println("convertedTime : " + convertedTime);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return convertedTime;
		// Output will be 10:23 PM
	}

	public static void setTextViewWidth(String language, TextView tv) {

//		if (language.equals("ar")) {
//
//			LayoutParams paramsExample = new LayoutParams(380, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//			tv.setLayoutParams(paramsExample);
//		} else {
//			LayoutParams paramsExample = new LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//
//			tv.setLayoutParams(paramsExample);
//		}

	}

	public static String changeDate(String time) {
		String convertedTime = "";
		try {
			SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
			SimpleDateFormat parseFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
			Date date = parseFormat.parse(time);
			convertedTime = displayFormat.format(date);
			// System.out.println("convertedTime : " + convertedTime);
		} catch (final ParseException e) {
			e.printStackTrace();
		}
		return convertedTime;

	}

	public static void changeLangauge(String langName, Context ctx) {
		Locale locale = null;
		if (langName.equals("ar")) {

			locale = new Locale("ar");
			Locale.setDefault(locale);
		} else {
			locale = new Locale("en");
			Locale.setDefault(locale);
		}

		Configuration config = new Configuration();
		config.locale = locale;
		ctx.getResources().updateConfiguration(config, ctx.getResources().getDisplayMetrics());
	}

	public static String getDeviceId(Context ctx) {

		String device_uuid = Secure.getString(ctx.getContentResolver(), Secure.ANDROID_ID);

		return device_uuid;
	}

	public static void setTextGabbalandFont(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Gabbaland.ttf");
		view.setTypeface(typeface);

	}

	public static void setTextEurostile(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "EUROSTILE BOLD.TTF");
		view.setTypeface(typeface);

	}

	public static void setTextHelveticaBold(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Helvetica-Bold.otf");
		view.setTypeface(typeface);

	}

	public static void setButtonGabbalandFont(Button view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "Gabbaland.ttf");
		view.setTypeface(typeface);

	}

	public static void setImpactFont(Button view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "IMPACT.TTF");
		view.setTypeface(typeface);

	}

	public static void setMogaMaddySolemanFont(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "moga_1.otf");
		view.setTypeface(typeface, Typeface.BOLD);

	}

	public static void setAxTShareFont(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "AXtShareQ Regular.ttf");
		view.setTypeface(typeface);

	}

	public static void setAxTMANALFont(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "AXTMANALBLACK.TTF");
		view.setTypeface(typeface);

	}

	public static void setTextMogaFont(TextView view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "moga.ttf");
		view.setTypeface(typeface, Typeface.BOLD);

	}

	public static void setButtonMogaFont(Button view, Context ctx) {

		Typeface typeface = Typeface.createFromAsset(ctx.getAssets(), "moga.ttf");
		view.setTypeface(typeface);

	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD)
	public static void setThreadPolicy() {
		try {
			if (android.os.Build.VERSION.SDK_INT > 9) {
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

	}
	
	
	public static boolean isNullOrEmpty(String s) {
        return s == null || s.length() == 0 || s.equalsIgnoreCase("null");
    }
}
