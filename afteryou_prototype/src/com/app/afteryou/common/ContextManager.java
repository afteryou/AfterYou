
package com.app.afteryou.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.app.afteryou.R;

public class ContextManager extends Activity {
	private static final String PREFERENCES_NAME = ContextManager.class.getName();
	private static final String CARRIER_PREFERENCE_NAME = "carrier";

	private static final String[] CARRIERS = {"Verizon", "Non-Verizon"};
	private static final int VERIZON_CARRIER_INDEX = 0;
	private static final int NON_VERIZON_CARRIER_INDEX = 1;

	private Credentials mCredentials;
	private String[] mAvailableCredentials;

	private Spinner mCredentialsSpinner;
	private Spinner mCarrierSpinner;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		setContentView(R.layout.context_manager);

		mCredentials = Credentials.instance();
		mAvailableCredentials = mCredentials.getCrendentialNames();

		mCredentialsSpinner = (Spinner)findViewById(R.id.credential_spinner);
		mCredentialsSpinner.setAdapter(createSpinnerAdapter(mAvailableCredentials));
		mCredentialsSpinner.setSelection(getIndex(mAvailableCredentials, mCredentials.getName(), 0));

		mCarrierSpinner = (Spinner)findViewById(R.id.carrier_spinner);
		mCarrierSpinner.setAdapter(createSpinnerAdapter(CARRIERS));
		mCarrierSpinner.setSelection(getIndex(CARRIERS, mCredentials.getName(), NON_VERIZON_CARRIER_INDEX));

		Button saveButton = (Button)findViewById(R.id.save_button);
		saveButton.setOnClickListener(new SaveButtonListeter());
    }

	private ArrayAdapter<String> createSpinnerAdapter(String[] array) {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
			this,
    		android.R.layout.simple_spinner_item,
    		array);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        return adapter;
	}

	private void onSaveButtonClicked() {
		AlertDialog dialog = new AlertDialog.Builder(this).setMessage(R.string.restart_required).create();

		RestartConfirmationDialogListeter listener = new RestartConfirmationDialogListeter();
		dialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(android.R.string.ok), listener);
		dialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(android.R.string.cancel), listener);

		dialog.show();
	}

	private void save() {
		String selectedCredential = mAvailableCredentials[mCredentialsSpinner.getSelectedItemPosition()];
		mCredentials.saveName(selectedCredential);

		String selectedCarrier = CARRIERS[mCarrierSpinner.getSelectedItemPosition()];
		saveValue(this, CARRIER_PREFERENCE_NAME, selectedCarrier);

		setResult(Activity.RESULT_OK);
		finish();
	}

	private class SaveButtonListeter implements OnClickListener {
		@Override
		public void onClick(View view) {
			onSaveButtonClicked();
		}
	}

	private class RestartConfirmationDialogListeter implements android.content.DialogInterface.OnClickListener {
		@Override
		public void onClick(DialogInterface dialog, int which) {
			if(which == DialogInterface.BUTTON_POSITIVE) {
				save();
			}
		}
	}

	private static int getIndex(String[] array, String string, int defaultIndex) {
    	for (int i = 0; i < array.length; i++) {
    		if (string.equalsIgnoreCase(array[i])) {
    			return i;
    		}
    	}
    	return defaultIndex;
    }

	private static void saveValue(Context context, String name, String value) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		preferences.edit().putString(name, value).commit();
	}

	private static String getSavedValue(Context context, String name, String defaultValue) {
		SharedPreferences preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
		return preferences.getString(name, defaultValue);
	}

	public static String getSavedCarrier(Context context) {
		return getSavedValue(context, CARRIER_PREFERENCE_NAME, CARRIERS[VERIZON_CARRIER_INDEX]);
	}
}
