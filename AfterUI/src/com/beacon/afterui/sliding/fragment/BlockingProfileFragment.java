package com.beacon.afterui.sliding.fragment;

import java.util.ArrayList;
import java.util.zip.Inflater;

import android.app.Fragment;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;

public class BlockingProfileFragment extends Fragment implements
		FragmentLifecycle, OnClickListener, OnItemClickListener {

	private ListView mBlockProfileUserList;

	private Typeface mITCAvantGardeStdBkFont;

	private ArrayList<String> mBlockUserNameList = new ArrayList<String>();

	private EditText mProfileNameBox;

	private View listview_divider_blocking_profile;

	private BlockListAdapter mBlockUserAdapter = new BlockListAdapter();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.blocking_profile_fragment, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);

		TextView cancel_btn_blocking_profile = (TextView) view
				.findViewById(R.id.cancel_btn_blocking_profile);
		cancel_btn_blocking_profile.setTypeface(mITCAvantGardeStdBkFont);
		cancel_btn_blocking_profile.setOnClickListener(this);

		TextView setting_txt_blocking_profile = (TextView) view
				.findViewById(R.id.setting_txt_blocking_profile);
		setting_txt_blocking_profile.setTypeface(mITCAvantGardeStdBkFont);

		TextView privacy_info_blocking_profile = (TextView) view
				.findViewById(R.id.privacy_info_blocking_profile);
		privacy_info_blocking_profile.setTypeface(mITCAvantGardeStdBkFont);

		TextView block_btn = (TextView) view.findViewById(R.id.block_btn);
		block_btn.setTypeface(mITCAvantGardeStdBkFont);
		block_btn.setOnClickListener(this);

		mProfileNameBox = (EditText) view.findViewById(R.id.profile_name_box);
		mProfileNameBox.setTypeface(mITCAvantGardeStdBkFont);

		listview_divider_blocking_profile = view
				.findViewById(R.id.listview_divider_blocking_profile);

		mBlockProfileUserList = (ListView) view
				.findViewById(R.id.blocked_profile_user_list);
		mBlockProfileUserList.setOnItemClickListener(this);
		return view;
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {

		mBlockUserNameList.remove(position);
		mBlockUserAdapter.notifyDataSetChanged();

	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.cancel_btn_blocking_profile:

			getActivity().onBackPressed();

			break;

		case R.id.block_btn:

			String name = mProfileNameBox.getText().toString().trim();
			if (!(name.length() == 0)) {
				mBlockUserNameList.add(name);
				mBlockProfileUserList.setAdapter(mBlockUserAdapter);
				mBlockUserAdapter.notifyDataSetChanged();
				mProfileNameBox.setText("");
				listview_divider_blocking_profile.setVisibility(View.VISIBLE);
			} else {
				Toast.makeText(getActivity(), "Please enter the profile name",
						Toast.LENGTH_SHORT).show();
			}

			break;

		}

	}

	@Override
	public void onFragmentPause() {

	}

	@Override
	public void onFragmentResume() {

	}

	@Override
	public boolean onBack() {
		return false;
	}

	class BlockListAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mBlockUserNameList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {

			ViewHolder holder;

			if (view == null) {
				holder = new ViewHolder();

				LayoutInflater inflater = getActivity().getLayoutInflater();
				view = inflater
						.inflate(R.layout.blocking_profile_adapter, null);
				holder.block_user_name = (TextView) view
						.findViewById(R.id.block_user_name);
				holder.block_user_name.setTypeface(mITCAvantGardeStdBkFont);
				holder.unblock_txt = (TextView) view
						.findViewById(R.id.unblock_txt);
				holder.unblock_txt.setTypeface(mITCAvantGardeStdBkFont);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.block_user_name.setText(mBlockUserNameList.get(position));

			return view;
		}

		private class ViewHolder {

			TextView block_user_name;
			TextView unblock_txt;

		}
	}

}
