package com.beacon.afterui.views;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;

public class LoginScreen extends BaseActivity {

    private ImageButton mLoginButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);
        mLoginButton = (ImageButton) findViewById(R.id.login_btn);
        mLoginButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {

                Intent intent = new Intent(LoginScreen.this, CapturePictureActivity.class);
                startActivity(intent);

            }
        });
    }

}
