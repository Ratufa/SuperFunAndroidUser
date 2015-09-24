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

import com.squareup.picasso.Picasso;
import com.superfunapp.R;

public class HowItAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<HashMap<String, String>> giftList;
	private LayoutInflater inflater;

	// private ImageLoaderBig imageLoaderBig;
	public HowItAdapter(Context ctx, ArrayList<HashMap<String, String>> dataList) {

		this.giftList = dataList;
		this.context = ctx;
		// imageLoaderBig = new ImageLoaderBig(context);
		inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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

	@SuppressLint({ "InflateParams", "ViewHolder" })
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub

		convertView = inflater.inflate(R.layout.how_it_adapter, null);

		ImageView giftImage = (ImageView) convertView.findViewById(R.id.howItImage);

		Picasso.with(context).load(giftList.get(position).get("key_image")).into(giftImage);

		return convertView;
	}

}
