package com.beacon.afterui.views;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.ImageInfoUtils;

public class FullImageActivity extends Activity implements OnClickListener {
    private static final String ID = "id";
    private static final String PATH = "path";
    private ImageView mFullSizeImage;
    private String mImagePath;
    private ImageButton mDoneBtn;
    private ImageButton mCancelBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.full_image_activity);
        mFullSizeImage = (ImageView) findViewById(R.id.photo_of_user_full_image);
        mDoneBtn = (ImageButton) findViewById(R.id.done_btn_full_image_activity);
        mCancelBtn = (ImageButton) findViewById(R.id.cancel_btn_full_image_activity);
        Long mId = getIntent().getLongExtra(ID, -1);
        int postion = getIntent().getIntExtra("POSITION", -1);
        Log.d("FullImageActivity", "ID :" + mId);
        Log.d("FullImageActivity", "POSITION" + postion);
        mImagePath = ImageInfoUtils.getPhotoPath(FullImageActivity.this,
                mId.toString());
        Log.d("Image Path", mImagePath);
        Uri uri = Uri.parse(mImagePath);
        mFullSizeImage.setImageURI(uri);
        mDoneBtn.setOnClickListener(this);
        mCancelBtn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
        
        case R.id.done_btn_full_image_activity:
            Intent intent = new Intent();
            intent.putExtra(PATH, mImagePath);
            setResult(RESULT_OK, intent);
            finish();
            break;

        case R.id.cancel_btn_full_image_activity:
            setResult(RESULT_CANCELED);
            finish();
            break;

        }

    }

}
