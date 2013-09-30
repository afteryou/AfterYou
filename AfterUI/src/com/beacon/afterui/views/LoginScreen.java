package com.beacon.afterui.views;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.utils.Utilities;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class LoginScreen extends BaseActivity implements OnClickListener,OnFocusChangeListener {

    /** TAG */
    private static final String TAG = LoginScreen.class.getSimpleName();

    private ImageButton mLoginButton;

    private ImageView mCrossEmailBtn;

    private ImageView mCrossPasswordBtn;

    private EditText mEmailText;
    private EditText mPasswordText;

    private static final boolean isTest = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        mLoginButton = (ImageButton) findViewById(R.id.sign_in_btn_login_screen);
        mCrossEmailBtn = (ImageView) findViewById(R.id.cross_btn_email_login_screen);
        mCrossPasswordBtn = (ImageView) findViewById(R.id.cross_btn_password_login_screen);
        mEmailText = (EditText) findViewById(R.id.email_text);
        mPasswordText = (EditText) findViewById(R.id.password_text);

        if (isTest) {
            // TODO remove this, it is for testing
            mEmailText.setText("abc@def.com");
            mPasswordText.setText("wieehj");
        }

        mCrossEmailBtn.setOnClickListener(this);
        mCrossPasswordBtn.setOnClickListener(this);
        mLoginButton.setOnClickListener(this);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        case R.id.sign_in_btn_login_screen:
            handleSignInButton();
            break;

        case R.id.cross_btn_email_login_screen:
            mEmailText.setText("");
            break;
            
        case R.id.cross_btn_password_login_screen:
            mPasswordText.setText("");
            break;

        }
    }

    private void handleSignInButton() {

        // check user name not NULL.
        if (mEmailText.getText().length() <= 0
                || mPasswordText.getText().length() <= 0) {

            // Show dialog and return
            showErrorDialog(R.string.username_password_empty_desc);
            return;
        }

        if (!Utilities.isValidEmail(mEmailText.getText())) {
            // Show dialog and return
            showErrorDialog(R.string.invalid_email);
            return;
        }

        Intent intent = new Intent(LoginScreen.this,
                CapturePictureActivity.class);
        try {
            startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e(TAG, " Activity not found : " + e.getMessage());
        }
    }

    private void showErrorDialog(int stringResId) {
        ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(this),
                this, R.style.Theme_CustomDialog,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, getResources().getString(stringResId));
        errDialog.show();
    }

	@Override
	public void onFocusChange(View v, boolean hasFocus) {
		switch (v.getId()) {

		case R.id.email_text:
			if (hasFocus) {
				mEmailText.setSelection(0);
				mEmailText.setHint("");
				mEmailText.setCursorVisible(true);
			}
			break;
		case R.id.password_text:
			if (hasFocus) {
				mPasswordText.setSelection(0);
				mPasswordText.setHint("");
				mPasswordText.setCursorVisible(true);
			}
			break;
		}
		
	}
}
