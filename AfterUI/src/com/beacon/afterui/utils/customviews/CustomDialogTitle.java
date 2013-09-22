package com.beacon.afterui.utils.customviews;

import android.content.Context;
import android.text.Layout;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.TextView;

public class CustomDialogTitle extends TextView {
    
    public CustomDialogTitle(Context context, AttributeSet attrs,
            int defStyle) {
        super(context, attrs, defStyle);
    }

    public CustomDialogTitle(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomDialogTitle(Context context) {
        super(context);
    }
    
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        final Layout layout = getLayout();
        if (layout != null) {
            final int lineCount = layout.getLineCount();
            if (lineCount > 0) {
                final int ellipsisCount = layout.getEllipsisCount(lineCount - 1);
                if (ellipsisCount > 0) {
                    setSingleLine(false);
                    final int textSize = (int) this.getTextSize(); 
                    setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
                    setMaxLines(2);
                    super.onMeasure(widthMeasureSpec, heightMeasureSpec);    
                }
            }
        }
    }

}

