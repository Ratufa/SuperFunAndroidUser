package com.superfunapp.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class GiftListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> giftList;
	private LayoutInflater inflater;

	public GiftListAdapter(Context ctx,
			ArrayList<HashMap<String, String>> dataList) {

		this.giftList = dataList;
		this.context = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return giftList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return giftList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = inflater.inflate(R.layout.new_gift_list_item, null);

			viewHolder.giftName = (TextView) convertView
					.findViewById(R.id.giftNameTv);
			viewHolder.giftPoints = (TextView) convertView
					.findViewById(R.id.giftPoints);
			viewHolder.giftImage = (ImageView) convertView
					.findViewById(R.id.giftImage);
			viewHolder.giftTitle = (TextView) convertView
					.findViewById(R.id.giftTitle);
			
			Utility.setTextHelveticaBold(viewHolder.giftName, context);
			Utility.setTextHelveticaBold(viewHolder.giftTitle, context);
			Utility.setTextHelveticaBold(viewHolder.giftPoints, context);
			
			if (SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) context,
					Constants.USER_LANGAUGE).equals("ar")){
				viewHolder.giftName.setText(giftList.get(position).get(
						"gift_name_ar"));
				Utility.setTextGabbalandFont(viewHolder.giftName, context);
				Utility.setTextGabbalandFont(viewHolder.giftTitle, context);
			}
			else{
				viewHolder.giftName.setText(giftList.get(position).get(
						"gift_name_en"));
			}

			viewHolder.giftPoints.setText(giftList.get(position).get(
					"gift_points"));
			Glide.with(context).load(giftList.get(position).get("gift_image"))
					.into(viewHolder.giftImage);

			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	public static class ViewHolder {

		public TextView giftName;
		public TextView giftTitle;
		public TextView giftPoints;
		private ImageView giftImage;
	}

}
