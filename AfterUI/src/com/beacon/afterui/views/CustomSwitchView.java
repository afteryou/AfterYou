package com.beacon.afterui.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout;

import com.beacon.afterui.R;

public class CustomSwitchView extends RelativeLayout implements OnClickListener {

    /** TAG */
    private static final String TAG = CustomSwitchView.class.getSimpleName();

    private Drawable mSelectedDrawable;

    private ImageView mLeftView;

    private ImageView mRightView;

    private static final int LEFT_VIEW_ID = 123456;

    private static final int RIGHT_VIEW_ID = 81249179;

    private int mSelectedId;

    public CustomSwitchView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
        addViews();
    }

    public CustomSwitchView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
        addViews();
    }

    public CustomSwitchView(Context context) {
        super(context);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(
                attrs, R.styleable.CustomSwitch, 0, 0);

        try {
            mSelectedDrawable = getResources().getDrawable(
                    R.drawable.switch_btn);
        } finally {
            typedArray.recycle();
        }
    }

    private void addViews() {
        mLeftView = new ImageView(getContext());
        mLeftView.setId(LEFT_VIEW_ID);
        mLeftView.setBackgroundDrawable(mSelectedDrawable);
        mLeftView.setImageResource(R.drawable.male_txt);
        mLeftView.setScaleType(ScaleType.CENTER);
        mLeftView.setOnClickListener(this);
        mSelectedId = LEFT_VIEW_ID;

        mRightView = new ImageView(getContext());
        mRightView.setId(RIGHT_VIEW_ID);
        mRightView.setImageResource(R.drawable.female_txt);
        mRightView.setScaleType(ScaleType.CENTER);
        mRightView.setOnClickListener(this);

        RelativeLayout.LayoutParams paramsLeft = new RelativeLayout.LayoutParams(
                mSelectedDrawable.getIntrinsicWidth(),
                mSelectedDrawable.getIntrinsicHeight());
        paramsLeft.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        addView(mLeftView, paramsLeft);

        RelativeLayout.LayoutParams paramsRight = new RelativeLayout.LayoutParams(
                mSelectedDrawable.getIntrinsicWidth(),
                mSelectedDrawable.getIntrinsicHeight());
        paramsRight.addRule(RelativeLayout.RIGHT_OF, mLeftView.getId());
        addView(mRightView, paramsRight);
    }

    @Override
    public void onClick(View v) {

        int selectedId = -1;

        switch (v.getId()) {
        case LEFT_VIEW_ID:
            selectedId = LEFT_VIEW_ID;
            break;

        case RIGHT_VIEW_ID:
            selectedId = RIGHT_VIEW_ID;
            break;
        }

        if (selectedId != mSelectedId) {
            updateViews(selectedId);
        }
    }

    private void updateViews(int selectedId) {
        mSelectedId = selectedId;
        switch (mSelectedId) {
        case LEFT_VIEW_ID:
            mLeftView.setBackgroundDrawable(mSelectedDrawable);
            mRightView.setBackgroundDrawable(null);
            break;

        case RIGHT_VIEW_ID:
            mLeftView.setBackgroundDrawable(null);
            mRightView.setBackgroundDrawable(mSelectedDrawable);
            break;

        default:
            break;
        }
    }

    public int getSelectedItem() {
        return mSelectedId == LEFT_VIEW_ID ? 0 : 1;
    }
}
