
package com.app.afteryou.activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.app.afteryou.BaseActivity;
import com.app.afteryou.controller.GPSController;
import com.app.afteryou.preference.GPSFileExplorer;
import com.app.afteryou.preference.PreferenceEngine;
import com.app.afteryou.R;

public class GPSChooseActivity extends BaseActivity {
	@Override
	public void onCreate(Bundle bundle) {
		super.onCreate(bundle);
		setContentView(R.layout.gps_choose_view);

		mExplorer = new GPSFileExplorer(this, PreferenceEngine.getInstance().getGpsFileName());
		LinearLayout listParent = findViewByIdAutoCast(R.id.fb_list_parent);
		mExplorer.setGPSFileListener(new GPSFileExplorer.GPSFileListener() {
			@Override
			public void onGpsFileSelected(String fullFileName) {

				PreferenceEngine.getInstance().saveGpsFileName(fullFileName);
				GPSController.getInstance(GPSChooseActivity.this).initProvider();
				GPSChooseActivity.this.finish();
			}
		});
		// mExplorer.chooseOneCategoryAsDefault(null);
		listParent.addView(mExplorer, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT, 1.0f));
		mExplorer.requestFocus();
	}

	private GPSFileExplorer mExplorer;

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mExplorer.isShowingChildren()) {
				mExplorer.backToPrev();
				// mBoard.reset();
			} else {
				this.finish();
			}
		}
		return false;
	}
}