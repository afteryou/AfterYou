package com.beacon.afterui.views.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.beacon.afterui.R;

public class ViewVotesAddapter extends BaseAdapter {

	private LayoutInflater mInflater;

	public ViewVotesAddapter(Context context) {
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return 20;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int id) {
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		if (view == null) {

			view = mInflater.inflate(R.layout.voting_006_voted_hot, null);

		}

		return view;
	}

}
