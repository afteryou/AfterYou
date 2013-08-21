/*
 * (C) Copyright 2012 by TeleCommunication Systems, Inc.
 *
 * The information contained herein is confidential, proprietary
 * to TeleCommunication Systems, Inc., and considered a trade secret
 * as defined in section 499C of the penal code of the State of
 * California. Use of this information by anyone other than
 * authorized employees of TeleCommunication Systems is granted only
 * under a written non-disclosure agreement, expressly prescribing
 * the scope and manner of such use.
 *
 */

package com.navbuilder.app.hurricane.debug;

import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.navbuider.app.hurricane.uiflow.HurricaneAppController;
import com.navbuilder.app.hurricane.BaseActivity;
import com.navbuilder.app.hurricane.R;
import com.navbuilder.app.hurricane.preference.PreferenceEngine;

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
				HurricaneAppController.getInstance().updateLocationProvider(fullFileName);
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