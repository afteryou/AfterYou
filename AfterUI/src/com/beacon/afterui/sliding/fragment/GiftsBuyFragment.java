package com.beacon.afterui.sliding.fragment;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.beacon.afterui.constants.CommonConstants;
import com.beacon.afterui.inappbilling.util.IabHelper;
import com.beacon.afterui.inappbilling.util.IabHelper.OnIabSetupFinishedListener;
import com.beacon.afterui.inappbilling.util.IabResult;
import com.beacon.afterui.inappbilling.util.InAppBillingConstants;
import com.beacon.afterui.inappbilling.util.Inventory;
import com.beacon.afterui.inappbilling.util.Purchase;
import com.beacon.afterui.utils.customviews.AfterYouDialogImpl;
import com.beacon.afterui.utils.customviews.ErrorDialog;

public class GiftsBuyFragment extends Fragment implements FragmentLifecycle,
        ISearchFunction, OnItemClickListener {

    /** TAG */
    private static final String TAG = GiftsBuyFragment.class.getSimpleName();

    private ListView mListView;

    private GiftItem[] mGiftItems;

    private IabHelper mHelper;

    public static final int RC_REQUEST = 10001;

    private GiftItem mGiftItem;

    public GiftsBuyFragment() {
        super();
    }

    public GiftsBuyFragment(final Context context) {
        super();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initInAppBilling();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gifts_buy_screen, null, false);

        mListView = (ListView) view.findViewById(R.id.gifts_buy_list);
        init();

        mListView.setOnItemClickListener(this);
        mListView.setAdapter(new GiftPointsAdapter());

        return view;
    }

    private void initInAppBilling() {

        disposeInAppHelper();

        mHelper = new IabHelper(getActivity(), InAppBillingConstants.APP_KEY);

        // enable debug logging (for a production application, you should set
        // this to false).
        mHelper.enableDebugLogging(true);

        mHelper.startSetup(mOnIabSetupFinishedListener);
    }

    private OnIabSetupFinishedListener mOnIabSetupFinishedListener = new OnIabSetupFinishedListener() {

        @Override
        public void onIabSetupFinished(IabResult result) {
            Log.d(TAG, "Setup finished.");

            if (!result.isSuccess()) {
                // Oh noes, there was a problem.
                // complain("Problem setting up in-app billing: " + result);
                Log.e(TAG, "Something went wrong, try again!");
                return;
            }

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null)
                return;

            // IAB is fully set up. Now, let's get an inventory of stuff we own.
            Log.d(TAG, "Setup successful. Querying inventory.");
            mHelper.queryInventoryAsync(mGotInventoryListener);
        }
    };

    private IabHelper.QueryInventoryFinishedListener mGotInventoryListener = new IabHelper.QueryInventoryFinishedListener() {

        @Override
        public void onQueryInventoryFinished(IabResult result,
                Inventory inventory) {
            Log.d(TAG, "Query inventory finished.");

            // Have we been disposed of in the meantime? If so, quit.
            if (mHelper == null)
                return;

            // Is it a failure?
            if (result.isFailure()) {
                Log.e(TAG, "Failed to query inventory: " + result);
                return;
            }

            Log.d(TAG, "Query inventory was successful.");

            // Check for gas delivery -- if we own gas, we should fill up the
            // tank immediately
            Purchase gasPurchase = inventory
                    .getPurchase(InAppBillingConstants.FIVE_K);
            if (gasPurchase != null && verifyDeveloperPayload(gasPurchase)) {
                Log.d(TAG,
                        "We own this item, and we are consuming it now --->.");
                mHelper.consumeAsync(
                        inventory.getPurchase(InAppBillingConstants.FIVE_K),
                        mConsumeFinishedListener);
                return;
            }

        }
    };

    private void init() {
        // Initializes gift points and prices.

        mGiftItems = new GiftItem[9];
        GiftItem item = new GiftItem();
        item.dollarValue = "$5";
        item.points = "5,000 points";
        item.pointsIntegerValue = 5000;
        mGiftItems[0] = item;

        item = new GiftItem();
        item.dollarValue = "$10";
        item.points = "10,000 points";
        item.pointsIntegerValue = 10000;
        mGiftItems[1] = item;

        item = new GiftItem();
        item.dollarValue = "$15";
        item.points = "15,000 points";
        item.pointsIntegerValue = 15000;
        mGiftItems[2] = item;

        item = new GiftItem();
        item.dollarValue = "$20";
        item.points = "20,000 points";
        item.buy = "buy";
        item.pointsIntegerValue = 20000;
        mGiftItems[3] = item;

        item = new GiftItem();
        item.dollarValue = "$25";
        item.points = "25,000 points";
        item.pointsIntegerValue = 25000;
        mGiftItems[4] = item;

        item = new GiftItem();
        item.dollarValue = "$30";
        item.points = "30,000 points";
        item.pointsIntegerValue = 30000;
        mGiftItems[5] = item;

        item = new GiftItem();
        item.dollarValue = "$35";
        item.points = "35,000 points";
        item.pointsIntegerValue = 35000;
        item.buy = "buy";
        mGiftItems[6] = item;

        item = new GiftItem();
        item.dollarValue = "$40";
        item.points = "40,000 points";
        item.pointsIntegerValue = 40000;
        mGiftItems[7] = item;

        item = new GiftItem();
        item.dollarValue = "$50";
        item.points = "50,000 points";
        item.pointsIntegerValue = 50000;
        mGiftItems[8] = item;
    }

    private class GiftItem {
        String dollarValue;
        String points;
        String buy;
        int pointsIntegerValue;
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

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct.
         * It will be the same one that you sent when initiating the purchase.
         * 
         * WARNING: Locally generating a random string when starting a purchase
         * and verifying it here might seem like a good approach, but this will
         * fail in the case where the user purchases an item on one device and
         * then uses your app on a different device, because on the other device
         * you will not have access to the random string you originally
         * generated.
         * 
         * So a good developer payload has these characteristics:
         * 
         * 1. If two different users purchase an item, the payload is different
         * between them, so that one user's purchase can't be replayed to
         * another user.
         * 
         * 2. The payload must be such that you can verify it even when the app
         * wasn't the one who initiated the purchase flow (so that items
         * purchased by the user on one device work on other devices owned by
         * the user).
         * 
         * Using your own server to store and verify developer payloads across
         * app installations is recommended.
         */

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
            long arg3) {
        Log.d(TAG, "Clicked on " + position);

        if (mGiftItems == null || mGiftItems.length <= 0) {
            return;
        }
        mGiftItem = mGiftItems[position];

        String payload = "";
        // mHelper.launchPurchaseFlow(getActivity(),
        // InAppBillingConstants.FIVE_K,
        // IabHelper.ITEM_TYPE_SUBS, RC_REQUEST,
        // mPurchaseFinishedListener, payload);
        try {
            mHelper.launchPurchaseFlow(getActivity(),
                    InAppBillingConstants.FIVE_K, RC_REQUEST,
                    mPurchaseFinishedListener, payload);
        } catch (IllegalStateException e) {
            mGiftItem = null;
            Log.e(TAG,
                    "Something is wrong with InApp billing, can't launch it!");
            showErrorDialog();
        }
        // Fragment detail = new GiftListFragment();
        // Bundle bundle = new Bundle();
        // bundle.putInt(CommonConstants.BundleKey.POINTS_KEY,
        // giftItem.pointsIntegerValue);
        // FragmentHelper.gotoFragment(getActivity(), GiftsBuyFragment.this,
        // detail, bundle);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult(" + requestCode + "," + resultCode + ","
                + data);
        if (mHelper == null)
            return;

        // Pass on the activity result to the helper for handling
        if (!mHelper.handleActivityResult(requestCode, resultCode, data)) {
            // not handled, so handle it ourselves (here's where you'd
            // perform any handling of activity results not related to in-app
            // billing...
            super.onActivityResult(requestCode, resultCode, data);
        } else {
            Log.d(TAG, "onActivityResult handled by IABUtil.");
        }
    }

    /** Callback for when a purchase is finished. */
    private IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {

        @Override
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: "
                    + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null)
                return;

            if (result.isFailure()) {
                Log.e(TAG, "Error purchasing: " + result);
                showErrorInPurchasing();
                return;
            }

            if (!verifyDeveloperPayload(purchase)) {
                Log.e(TAG,
                        "Error purchasing. Authenticity verification failed.");
                // setWaitScreen(false);
                return;
            }

            Log.d(TAG, "Purchase successful : " + purchase.getSku());

            if (purchase.getSku().equals(InAppBillingConstants.FIVE_K)) {
                Log.d(TAG, "Starting " + InAppBillingConstants.FIVE_K
                        + " consumption!");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    private void showErrorInPurchasing() {
        ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(
                getActivity()), getActivity(), R.style.Theme_CustomDialog,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, getResources().getString(R.string.error_in_in_app_billing));
        errDialog.show();
    }

    private void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(getActivity());
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }

    private void showErrorDialog() {
        ErrorDialog errDialog = new ErrorDialog(new AfterYouDialogImpl(
                getActivity()), getActivity(), R.style.Theme_CustomDialog,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }, getResources().getString(
                        R.string.in_app_billing_general_error));
        errDialog.show();
    }

    // Called when consumption is complete
    private IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase
                    + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null)
                return;

            // We know this is the "gas" sku because it's the only one we
            // consume,
            // so we don't check which sku was consumed. If you have more than
            // one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in
                // our
                // game world's logic, which in our case means filling the gas
                // tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
                // mTank = mTank == TANK_MAX ? TANK_MAX : mTank + 1;
                // saveData();
                // alert("InApp purchase is done, Congrats!");
                launchSendGiftScreen();
            } else {
                Log.e(TAG, "Error while consuming: " + result);
                showErrorInPurchasing();
            }
            // updateUi();
            // setWaitScreen(false);
            disposeInAppHelper();
            Log.d(TAG, "End consumption flow.");

            initInAppBilling();
        }
    };

    private void launchSendGiftScreen() {
        Fragment detail = new SendGiftFragment();
        Bundle bundle = new Bundle();
        FragmentHelper.gotoFragment(getActivity(), GiftsBuyFragment.this,
                detail, bundle);
    }

    @Override
    public void onDestroy() {
        // very important:
        Log.d(TAG, "Destroying helper.");
        disposeInAppHelper();

        super.onDestroy();
    }

    private void disposeInAppHelper() {
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }
    }
}
