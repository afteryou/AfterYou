package com.app.afteryou.customview;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListViewAdapter extends BaseAdapter {

	private final Object mLock = new Object();
	private List<ListItem> mData;
	private Context mContext;
	private Map<ListItem, Integer> mDataIds = new HashMap<ListItem, Integer>();
	private int mItemLayoutId;
	private int mTextViewId;

	public ListViewAdapter(Context ctx, int itemLayoutRes, int textViewId, List<? extends ListItem> data) {
		mContext = ctx;
		mData = new ArrayList<ListItem>(data);
		for (ListItem item : data) {
			item.setContentView(itemLayoutRes);
			item.setTextViewId(textViewId);
		}
		mItemLayoutId = itemLayoutRes;
		mTextViewId = textViewId;
	}

	public ListViewAdapter(Context ctx, int itemLayoutRes, int textViewId) {
		mContext = ctx;
		mData = new ArrayList<ListItem>();
		mItemLayoutId = itemLayoutRes;
		mTextViewId = textViewId;
	}

	public ListViewAdapter(Context ctx, int itemLayoutRes, int textViewId, String[] texts) {
		mContext = ctx;
		mData = new ArrayList<ListItem>();
		mItemLayoutId = itemLayoutRes;
		mTextViewId = textViewId;
		if(texts == null){
			return;
		}
		for (String s : texts) {
			ListItem item = new ListItem(ctx.getApplicationContext());
			item.setContentView(itemLayoutRes);
			item.setText(textViewId, s);
			mData.add(item);
		}
		
	}

	public Context getContext() {
		return mContext;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return mData.get(position).getView();
	}

	public void add(List<? extends ListItem> items) {
		synchronized (mLock) {
			for (ListItem item : items) {
				add(item);
			}
		}
	}

	public void add(ListItem item) {
		synchronized (mLock) {
			mData.add(mData.size(), item);
			item.setContentView(mItemLayoutId);
			item.setTextViewId(mTextViewId);
			notifyDataSetChanged();
		}
	}

	public void remove(String itemText) {
		if (itemText == null) {
			return;
		}
		synchronized (mLock) {
			for (ListItem item : mData) {
				if (item.getText().equals(itemText)) {
					mData.remove(item);
					break;
				}
			}
		}
	}

	public void remove(ListItem item) {
		synchronized (mLock) {
			mData.remove(item);
		}
	}

	public void remove(int position) {
		synchronized (mLock) {
			mData.remove(position);
			notifyDataSetChanged();
		}
	}

	public void clear() {
		synchronized (mLock) {
			mData.clear();
			mDataIds.clear();
			notifyDataSetChanged();
		}
	}

	public void insert(ListItem object, int index) {
		synchronized (mLock) {
			if (mData != null) {
				mData.add(index, object);
			}
			notifyDataSetChanged();
		}
	}

	@Override
	public synchronized int getCount() {
		synchronized (mLock) {
			return mData.size();
		}
	}

	public List<ListItem> getAll() {
		return new ArrayList<ListItem>(mData);
	}

	@Override
	public ListItem getItem(int position) {
		return mData.get(position);
	}

	@SuppressWarnings("unchecked")
	public <T> T getItemAutoCast(int position) {
		return (T) mData.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public List<View> getAllViews() {
		List<View> result = new ArrayList<View>();
		for (ListItem item : getAll()) {
			result.add(item.getView());
		}
		return result;
	}

}
