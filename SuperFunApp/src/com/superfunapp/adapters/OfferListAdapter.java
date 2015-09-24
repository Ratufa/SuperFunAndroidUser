package com.superfunapp.adapters;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.ListBean;
import com.superfunapp.R;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class OfferListAdapter extends BaseAdapter {

	private ArrayList<ListBean> giftList;
	private LayoutInflater inflater;
	private int index = -1;
	private Context context;

	public OfferListAdapter(Context ctx, ArrayList<ListBean> dataList) {

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
	public ListBean getItem(int position) {
		// TODO Auto-generated method stub
		return giftList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		final ViewHolder viewHolder;

	//	if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = inflater.inflate(R.layout.buy_tickets_item, null);

			viewHolder.listItemLayout = (LinearLayout) convertView
					.findViewById(R.id.listItemLayout);

			viewHolder.offerPrice = (TextView) convertView
					.findViewById(R.id.priceValue);
			viewHolder.offerPoint = (TextView) convertView
					.findViewById(R.id.giftValue);

			viewHolder.offerTicket = (TextView) convertView
					.findViewById(R.id.ticketsValue);

			viewHolder.imageView = (ImageView) convertView
					.findViewById(R.id.indicatorImg);

			viewHolder.view1 = convertView.findViewById(R.id.divide1);

			viewHolder.view2 = convertView.findViewById(R.id.divide2);

			viewHolder.priceTitle = (TextView) convertView
					.findViewById(R.id.priceText);
			viewHolder.giftTitle = (TextView) convertView
					.findViewById(R.id.giftText);
			viewHolder.ticketTitle = (TextView) convertView
					.findViewById(R.id.ticketText);
			
			if (SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) context, 
					Constants.USER_LANGAUGE).equals("ar")) {
				//Utility.setTextGabbalandFont(viewHolder.offerPrice, context);
				//Utility.setTextGabbalandFont(viewHolder.offerPoint, context);
				//Utility.setTextGabbalandFont(viewHolder.offerTicket, context);
				Utility.setTextGabbalandFont(viewHolder.priceTitle, context);
				Utility.setTextGabbalandFont(viewHolder.giftTitle, context);
				Utility.setTextGabbalandFont(viewHolder.ticketTitle, context);
			}
			else{
				Utility.setTextHelveticaBold(viewHolder.offerPrice, context);
				Utility.setTextHelveticaBold(viewHolder.offerPoint, context);
				Utility.setTextHelveticaBold(viewHolder.offerTicket, context);
				Utility.setTextHelveticaBold(viewHolder.priceTitle, context);
				Utility.setTextHelveticaBold(viewHolder.giftTitle, context);
				Utility.setTextHelveticaBold(viewHolder.ticketTitle, context);
			}
		
			viewHolder.offerPrice.setText(giftList.get(position).getAmount());
			viewHolder.offerPoint.setText(giftList.get(position).getPoint());
			viewHolder.offerTicket.setText(giftList.get(position).getTicket());

			viewHolder.listItemLayout.setTag(position);

			if (index == position) {

				viewHolder.offerPrice.setTextColor(Color.parseColor("#000000"));
				viewHolder.offerPoint.setTextColor(Color.parseColor("#000000"));
				viewHolder.offerTicket
						.setTextColor(Color.parseColor("#000000"));
				viewHolder.priceTitle.setTextColor(Color.parseColor("#FFFFFF"));
				viewHolder.ticketTitle
						.setTextColor(Color.parseColor("#FFFFFF"));
				viewHolder.giftTitle.setTextColor(Color.parseColor("#FFFFFF"));
				viewHolder.view2.setBackgroundResource(R.drawable.divideline1);
				viewHolder.view1.setBackgroundResource(R.drawable.divideline1);
				viewHolder.imageView.setImageResource(R.drawable.circle1);
				viewHolder.listItemLayout
						.setBackgroundResource(R.drawable.selector_list_item);
			}

		return convertView;
	}

	public static class ViewHolder {

		public TextView offerPrice;
		public TextView offerTicket;
		public TextView offerPoint;
		public LinearLayout listItemLayout;
		public ImageView imageView;
		public TextView giftTitle;
		public TextView priceTitle;
		public TextView ticketTitle;
		public View view1;
		public View view2;
	}

	public  void changeBackgroundColor(int index) {
		this.index = index;
		notifyDataSetChanged();
	}

}
