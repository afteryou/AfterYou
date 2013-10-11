package com.beacon.afterui.sliding.fragment;

import android.content.Context;

import com.beacon.afterui.sliding.customViews.ListItem;

public class Category extends ListItem {

	private String code;
	private String name;
	private String icon;
	private int type;

	public static final int TYPE_INTEREST = 0x00000001;
	public static final int TYPE_BRAND = 0x00000002;

	public int getType() {
		return type;
	}

	public void setType(int type) {
		if (type == TYPE_INTEREST || type == TYPE_BRAND) {
			this.type = type;
			return;
		}
		throw new IllegalArgumentException("unsupported type!");
	}

	public Category(Context ctx, String text) {
		super(ctx);
//		this.setText(R.id.slid_menu_item_text, text);
	}

	public Category(Context ctx) {
		super(ctx);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Override
	public void setTextViewId(int resId) {
		super.setTextViewId(resId);
		this.setText(resId, this.getName());
	}

}
