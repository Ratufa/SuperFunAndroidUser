package com.superfunapp.adapters;

import java.util.ArrayList;
import java.util.HashMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.superfunapp.HomeScreen;
import com.superfunapp.R;
import com.superfunapp.sharedPrefrns.SharedPrefrnceSuperFun;
import com.superfunapp.utils.Constants;
import com.superfunapp.utils.Utility;

public class CompanyListAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> companyList;
	private LayoutInflater inflater;

	public CompanyListAdapter(Context ctx,
			ArrayList<HashMap<String, String>> dataList) {

		this.companyList = dataList;
		this.context = ctx;
		inflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return companyList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return companyList.get(position);
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

			convertView = inflater
					.inflate(R.layout.companies_item_parent_new, null);

			viewHolder.layout = (LinearLayout) convertView
					.findViewById(R.id.headerLayout);
			viewHolder.name = (TextView) convertView
					.findViewById(R.id.branchName);
			viewHolder.address = (TextView) convertView
					.findViewById(R.id.branchAddress);
			viewHolder.image = (ImageView) convertView
					.findViewById(R.id.branchImage);
			viewHolder.arrowImage = (ImageView) convertView
					.findViewById(R.id.indicatorImg);
			viewHolder.totalBranch = (TextView) convertView
					.findViewById(R.id.totalBranches);
			viewHolder.branchTitle = (TextView) convertView
					.findViewById(R.id.branchTitle);
			viewHolder.layout.setTag(position);
			
			//Utility.setTextMogaFont(viewHolder.name, context);

			if (SharedPrefrnceSuperFun.getSharedPrefData((HomeScreen) context,
					Constants.USER_LANGAUGE).equals("ar")) {
				viewHolder.name.setText(companyList.get(position).get(
						"company_arabic_name"));
				viewHolder.address.setText(Html.fromHtml(companyList.get(
						position).get("company_address_ar")));
				Utility.setTextGabbalandFont(viewHolder.name, context);
				Utility.setTextGabbalandFont(viewHolder.branchTitle, context);
			} else {
				viewHolder.name.setText(companyList.get(position).get(
						"company_english_name"));
				viewHolder.address.setText(Html.fromHtml(companyList.get(
						position).get("company_address_eng")));
			}

			Glide.with(context)
					.load(companyList.get(position).get("company_logo"))
					.into(viewHolder.image);
			viewHolder.totalBranch.setText(companyList.get(position).get(
					"company_branch_count"));
			
			Utility.setTextEurostile(viewHolder.address, context);

			viewHolder.layout.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					int pos = (Integer) viewHolder.layout.getTag();
					if (!companyList.get(pos).get("company_branch_count")
							.equals("0")) {
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
		public TextView branchTitle;
		public TextView address;
		public TextView totalBranch;
		public ImageView image;
		public ImageView arrowImage;
		public LinearLayout layout;
	}

}
