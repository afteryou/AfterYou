package com.beacon.afterui.sliding.fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;

public class GiftsBuyFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction, OnItemClickListener {

    /** TAG */
    private static final String TAG = GiftsBuyFragment.class.getSimpleName();

    private ListView mListView;

    private GiftItem[] mGiftItems;

    public GiftsBuyFragment() {
        super();
    }

    public GiftsBuyFragment(final Context context) {
        super();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        TextView textView = new TextView(getActivity());
        textView.setText("This is so good!");

        View view = inflater.inflate(R.layout.gifts_buy_screen, null, false);

        mListView = (ListView) view.findViewById(R.id.gifts_buy_list);
        init();

        mListView.setOnItemClickListener(this);
        mListView.setAdapter(new GiftPointsAdapter());

        return view;
    }

    private void init() {
        // Initializes gift points and prices.

        mGiftItems = new GiftItem[9];
        GiftItem item = new GiftItem();
        item.dollarValue = "$5";
        item.points = "5,000 points";
        mGiftItems[0] = item;

        item = new GiftItem();
        item.dollarValue = "$10";
        item.points = "10,000 points";
        mGiftItems[1] = item;

        item = new GiftItem();
        item.dollarValue = "$15";
        item.points = "15,000 points";
        mGiftItems[2] = item;

        item = new GiftItem();
        item.dollarValue = "$20";
        item.points = "20,000 points";
        item.buy = "buy";
        mGiftItems[3] = item;

        item = new GiftItem();
        item.dollarValue = "$25";
        item.points = "25,000 points";
        mGiftItems[4] = item;

        item = new GiftItem();
        item.dollarValue = "$30";
        item.points = "30,000 points";
        mGiftItems[5] = item;

        item = new GiftItem();
        item.dollarValue = "$35";
        item.points = "35,000 points";
        item.buy = "buy";
        mGiftItems[6] = item;

        item = new GiftItem();
        item.dollarValue = "$40";
        item.points = "40,000 points";
        mGiftItems[7] = item;

        item = new GiftItem();
        item.dollarValue = "$50";
        item.points = "50,000 points";
        mGiftItems[8] = item;
    }

    private class GiftItem {
        String dollarValue;
        String points;
        String buy;
    }

    private class GiftPointsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mGiftItems.length;
        }

        @Override
        public Object getItem(int position) {
            return mGiftItems[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = getActivity().getLayoutInflater().inflate(
                        R.layout.gift_buy_item, null, true);
            }

            GiftItem item = mGiftItems[position];

            TextView dollarTextView = (TextView) convertView
                    .findViewById(R.id.dollar_amt_text);
            dollarTextView.setText(item.dollarValue);

            TextView pointsTextView = (TextView) convertView
                    .findViewById(R.id.points_text_id);
            pointsTextView.setText(item.points);

            return convertView;
        }
    }

    @Override
    public void doSearch(int type, SearchParams params) {

    }

    @Override
    public void onFragmentPause() {

    }

    @Override
    public void onFragmentResume() {

    }

    @Override
    public boolean onBack() {
        return false;
    }

    public class AnimState implements Parcelable {
        int centerLeft = 0;
        int centerTop = 0;
        float centerScaleX = 0.0f;
        float centerScaleY = 0.0f;
        float centerAngle = 0.0f;

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.centerLeft);
            dest.writeInt(this.centerTop);
            dest.writeFloat(this.centerScaleX);
            dest.writeFloat(this.centerScaleY);
            dest.writeFloat(this.centerAngle);
        }

        public final Parcelable.Creator<AnimState> CREATOR = new Parcelable.Creator<AnimState>() {

            @Override
            public AnimState createFromParcel(Parcel source) {
                AnimState state = new AnimState();
                if (source != null) {
                    state.centerLeft = source.readInt();
                    state.centerTop = source.readInt();
                    state.centerScaleX = source.readFloat();
                    state.centerScaleY = source.readFloat();
                    state.centerAngle = source.readFloat();
                }
                return state;
            }

            @Override
            public AnimState[] newArray(int size) {
                return new AnimState[size];
            }
        };
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long arg3) {
        Log.d(TAG, "CLicked on " + position);
    }
}
