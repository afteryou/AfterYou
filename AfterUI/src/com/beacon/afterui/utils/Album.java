package com.beacon.afterui.utils;

import android.graphics.Bitmap;

public class Album {

    public String id;
    public String name;
    public long coverID;
    public int count;
    public Bitmap thumb;

    @Override
    public String toString() {
        return "Name : " + name;
    }
}
