package com.beacon.afterui.utils;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Album {

	public String id;
	public String name;
	public long coverID;
	public int count = 1;
	public Bitmap thumb;

	@Override
	public String toString() {

		return "Name : " + name;
	}

}
