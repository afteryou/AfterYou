package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.FontUtils;
import com.beacon.afterui.views.MainActivity;

public class LogOutFragment extends Fragment implements FragmentLifecycle,
		OnClickListener {

	private Typeface mITCAvantGardeStdBkFont;

	public LogOutFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.log_out_lay, null);
		mITCAvantGardeStdBkFont = FontUtils.loadTypeFace(getActivity()
				.getApplicationContext(), FontUtils.ITC_AVANT_GARDE_STD_BK);
		TextView msg_txt = (TextView) view.findViewById(R.id.msg_txt);
		msg_txt.setTypeface(mITCAvantGardeStdBkFont);

		TextView logout_btn = (TextView) view.findViewById(R.id.logout_btn);
		logout_btn.setTypeface(mITCAvantGardeStdBkFont);
		logout_btn.setOnClickListener(this);

		TextView cancel_btn = (TextView) view.findViewById(R.id.cancel_btn);
		cancel_btn.setTypeface(mITCAvantGardeStdBkFont);
		cancel_btn.setOnClickListener(this);

		return view;
	}

	@Override
	public void onClick(View view) {

		switch (view.getId()) {

		case R.id.logout_btn:
			Toast.makeText(getActivity(), "Log out btn", Toast.LENGTH_SHORT)
					.show();
			exitApp();
			break;
		case R.id.cancel_btn:
			getActivity().onBackPressed();
			break;
		}

	}
	
	private void exitApp() {
		MainActivity activity  = (MainActivity)getActivity();
		activity.exitApp();
		
	}

	@Override
	public void onFragmentPause() {

	}

	@Override
	public void onFragmentResume() {

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	@Override
	public void onPause() {
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onStart() {
		super.onStart();
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	@Override
	public boolean onBack() {
		return false;
	}
}
