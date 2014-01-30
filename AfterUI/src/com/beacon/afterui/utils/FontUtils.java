package com.beacon.afterui.utils;

import java.util.Hashtable;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;

public class FontUtils {

    public static final String ITC_AVANT_GARDE_STD_BK = "ITCAvantGardeStd-Bk.otf";
    
    public static final String MYRIAD_PRO_REGULAR = "MyriadPro-Regular.otf";
    
    private static Hashtable<String, Typeface> mFontMap = new Hashtable<String, Typeface>();

    /**
     * Loads the desired font.
     * 
     * @param context
     *            This should be application context.
     * @param fontName
     * @return
     */
    public static synchronized Typeface loadTypeFace(final Context context,
            final String fontName) {

        if (TextUtils.isEmpty(fontName)) {
            throw new IllegalArgumentException("Font name can not be NULL!");
        }

        if (mFontMap.containsKey(fontName)) {
            return mFontMap.get(fontName);
        }

        if (context == null) {
            throw new IllegalArgumentException("Context name can not be NULL!");
        }

        Typeface typeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/" + fontName);

        if (typeFace != null) {
            mFontMap.put(fontName, typeFace);
        }

        return typeFace;
    }
}
