package com.superfunapp.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.htmltextview.HtmlTextView;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class HistoryAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> historyList;
	private LayoutInflater inflater;

	public HistoryAdapter(Context ctx, ArrayList<HashMap<String, String>> dataList) {
		this.historyList = dataList;
		this.context = ctx;
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return historyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return historyList.get(position);
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
		final ViewHolder viewHolder;

		if (convertView == null) {

			viewHolder = new ViewHolder();

			convertView = inflater.inflate(R.layout.log_book_item2, null);

			viewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.headerLayout);
			viewHolder.name = (TextView) convertView.findViewById(R.id.itemNameTv);
			viewHolder.date = (TextView) convertView.findViewById(R.id.itemDateTv);
			viewHolder.type = (TextView) convertView.findViewById(R.id.itemTypeTv);
			viewHolder.address = (HtmlTextView) convertView.findViewById(R.id.itemAddress);
			viewHolder.arrowImage = (ImageView) convertView
					.findViewById(R.id.indicatorImg);
			viewHolder.totalBranch = (TextView) convertView.findViewById(R.id.itemValueTv);
			viewHolder.layout.setTag(position);

			if (SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) context, 
					Constants.USER_LANGAUGE).equals("ar")) {

				viewHolder.name.setText(historyList.get(position).get("op_name_arabic"));
				viewHolder.address.setHtmlFromString(historyList.get(position).get("op_loc_arabic"), 
						new HtmlTextView.LocalImageGetter());
				viewHolder.date.setText(Utility.changeDate(historyList.get(position).get("op_date")) 
						+ " - " + Utility.Convert24to12(historyList.get(position).get("op_time")));
				viewHolder.type.setText(historyList.get(position).get("op_type_arabic"));
				viewHolder.totalBranch.setText(historyList.get(position).get("op_value"));

				Utility.setTextEurostile(viewHolder.date, context);
				Utility.setTextGabbalandFont(viewHolder.address, context);
				Utility.setTextGabbalandFont(viewHolder.name, context);
				Utility.setTextHelveticaBold(viewHolder.totalBranch, context);
				Utility.setTextGabbalandFont(viewHolder.type, context);

			} else {

				Utility.setTextEurostile(viewHolder.date, context);
				Utility.setTextHelveticaBold(viewHolder.address, context);
				Utility.setTextHelveticaBold(viewHolder.totalBranch, context);
				Utility.setTextHelveticaBold(viewHolder.type, context);
				Utility.setTextHelveticaBold(viewHolder.name, context);

				viewHolder.name.setText(historyList.get(position).get("op_name"));				
				viewHolder.address.setHtmlFromString(historyList.get(position).get("op_loc"),
						new HtmlTextView.LocalImageGetter());
				System.out.println("-------addresssssss--"+historyList.get(position).get("op_loc"));
				viewHolder.date.setText(Utility.changeDate(historyList.get(position).get("op_date")) 
						+ " - " + Utility.Convert24to12(historyList.get(position).get("op_time")));
				viewHolder.type.setText(historyList.get(position).get("op_type"));
				viewHolder.totalBranch.setText(historyList.get(position).get("op_value"));
			}
			viewHolder.layout.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//int pos = (Integer) viewHolder.layout.getTag();

					if (viewHolder.address.getVisibility() == View.VISIBLE) {
						viewHolder.address.setVisibility(View.GONE);
						viewHolder.arrowImage
						.setImageResource(R.drawable.arrowdown);
					} else {
						viewHolder.address.setVisibility(View.VISIBLE);
						viewHolder.arrowImage
						.setImageResource(R.drawable.arrowup);
					}
				}
			});
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		return convertView;
	}

	public static class ViewHolder {

		public TextView name;
		public TextView type;
		public TextView date;
		public HtmlTextView address;
		public TextView totalBranch;
		public ImageView arrowImage;
		public LinearLayout layout;
	}

}
