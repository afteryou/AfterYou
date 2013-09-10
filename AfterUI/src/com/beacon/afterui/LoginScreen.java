package com.beacon.afterui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class LoginScreen extends Activity {

    private ImageButton mLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
