package com.beacon.afterui.utils;

public class SetHead {

	private static String mHeading;
	private static String mConditionHead;

	public static void setText(String heading) {
		mHeading = heading;
	}

	public static String getText() {

		return mHeading;
	}

	public static void setConditionHead(String heading) {
		mConditionHead = heading;
	}

	public static String getConditionHead() {

		return mConditionHead;

	}
}
