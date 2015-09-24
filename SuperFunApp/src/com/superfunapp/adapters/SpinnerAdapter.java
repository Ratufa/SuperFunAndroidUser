package com.superfunapp.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.superfunapp.R;

public class SpinnerAdapter extends ArrayAdapter<String> {
	
	private TextView text;
	//private Context context;
	private LayoutInflater layoutInflater;
	private List<String> list;

	public SpinnerAdapter(Context context, List<String> data) {
		super(context, R.layout.row_spinner);
		//this.context = context;
		layoutInflater = LayoutInflater.from(context);
		this.list = data;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public String getItem(int position) {

		return list.get(position);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);

	};

	public View getCustomView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
			convertView = layoutInflater.inflate(com.superfunapp.R.layout.row_spinner, parent, false);
		}

		text = (TextView) convertView.findViewById(R.id.text1);

		text.setText(getItem(position));

		return convertView;
	}

}
