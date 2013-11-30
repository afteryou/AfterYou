package com.beacon.afterui.sliding.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.views.ProfileSettingsActivity;

/**
 * For showing left sliding menu behind main view.
 * 
 * @author spoddar
 * 
 */
public class SettingMenuItem extends BaseActivity {
	public static final String TAG = SettingMenuItem.class.toString();

	private ListView mListView;

	private Typeface typeFaceSemiBold;

	private static final int PROFILE_SETTING = 0;
	private static final int PRIVACY_SETTING = 1;
	private static final int BLOCKING = 2;
	private static final int NOTIFICATION = 3;
	private Context mContext;
	private int[] mImages;
	private String[] mSettingItemTxt;
	private TextView mCancelBtn;
	
	private static final String IMAGE = "icon";
	private static final String TEXT = "text";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_fragment);
		setBehindLeftContentView(R.layout.setting_fragment);
		setBehindRightContentView(R.layout.setting_fragment);
		TextView clickItem = (TextView) findViewById(R.id.setting_txt);
		mCancelBtn = (TextView) findViewById(R.id.cancel_btn);

		mContext = this;
		String clickText = getIntent().getExtras().getString("setting");
		clickItem.setText(clickText);
		mListView = (ListView) findViewById(R.id.setting_menu_list);
		mSettingItemTxt = getResources().getStringArray(
				R.array.setting_item_txt);
		mImages = new int[] { R.drawable.profile_setting_img,
				R.drawable.privacy_setting_img, R.drawable.blocking_img,
				R.drawable.notifications_img };
		typeFaceSemiBold = Typeface.createFromAsset(this.getAssets(),
				"fonts/MyriadPro-Semibold.otf");
		Typeface typeFaceRegular = Typeface.createFromAsset(this.getAssets(),
				"fonts/MyriadPro-Regular.otf");
		mListView.setAdapter(new ListAddapter());
		mListView.setOnItemClickListener(setting_item_listener);

	}
	
	// When using fragment

//	@Override
//	public View onCreateView(LayoutInflater inflater, ViewGroup container,
//			Bundle savedInstanceState) {
//		mSettingItemTxt = getResources().getStringArray(
//				R.array.setting_item_txt);
//		mImages = new int[] { R.drawable.profile_setting_img,
//				R.drawable.privacy_setting_img, R.drawable.blocking_img,
//				R.drawable.notifications_img };
//		List<HashMap<String, String>> mList = new ArrayList<HashMap<String, String>>();
//		// mRootView = (ViewGroup) inflater.inflate(R.layout.sliding_menu,
//		// null);
//
//		// font myriadPro semibold
//		typeFaceSemiBold = Typeface.createFromAsset(getActivity().getAssets(),
//				"fonts/MyriadPro-Semibold.otf");
//		// font myriadPro regular
//		Typeface typeFaceRegular = Typeface.createFromAsset(getActivity()
//				.getAssets(), "fonts/MyriadPro-Regular.otf");
//		View view = inflater.inflate(R.layout.setting_fragment, null);
//		View viewText = inflater.inflate(R.layout.setting_menu_item, null);
//		mListView = (ListView) view.findViewById(R.id.setting_menu_list);
//		TextView dashText = (TextView) viewText
//				.findViewById(R.id.setting_item_txt);
//
//
//		String[] from = { IMAGE, TEXT };
//		int[] to = { R.id.setting_img, R.id.setting_item_txt };
//		dashText.setTypeface(typeFaceSemiBold);
//
//		for (int i = 0; i < mSettingItemTxt.length; i++) {
//			HashMap<String, String> map = new HashMap<String, String>();
//			map.put(IMAGE, String.valueOf(mImages[i]));
//			map.put(TEXT, mSettingItemTxt[i]);
//			mList.add(map);
//		}
//		SimpleAdapter addapter = new SimpleAdapter(getActivity(), mList,
//				R.layout.setting_menu_item, from, to) {
//			@Override
//			public void setViewText(TextView v, String text) {
//				v.setTypeface(typeFaceSemiBold);
//				v.setText(text);
//			}
//		};
//		mListView.setAdapter(addapter);
//		mListView.setOnItemClickListener(leftDrawerListner);
//		return view;
//	}

	private class ListAddapter extends BaseAdapter {

		@Override
		public int getCount() {
			return mSettingItemTxt.length;
		}

		@Override
		public Object getItem(int position) {
			return mSettingItemTxt[position];
		}

		@Override
		public long getItemId(int id) {
			// TODO Auto-generated method stub
			return id;
		}

		@Override
		public View getView(int position, View view, ViewGroup parent) {
			LayoutInflater mInflater = (LayoutInflater) mContext
					.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
			ViewHolder holder = null;
			if (view == null) {
				view = mInflater.inflate(R.layout.setting_menu_item, null);
				holder = new ViewHolder();
				holder.txtTitle = (TextView) view
						.findViewById(R.id.setting_item_txt);
				holder.imageView = (ImageView) view
						.findViewById(R.id.setting_img);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}
			holder.txtTitle.setText(mSettingItemTxt[position]);
			holder.imageView.setImageResource(mImages[position]);
			return view;
		}

		private class ViewHolder {
			ImageView imageView;
			TextView txtTitle;
		}

	}

	private OnItemClickListener setting_item_listener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {

			Intent intent = null;
			switch (position) {
			case PROFILE_SETTING:

				intent = new Intent(SettingMenuItem.this,
						ProfileSettingsActivity.class);
				startActivity(intent);
				break;

			case PRIVACY_SETTING:
				break;

			case BLOCKING:

				break;

			case NOTIFICATION:

				break;

			}

		}
	};

}
