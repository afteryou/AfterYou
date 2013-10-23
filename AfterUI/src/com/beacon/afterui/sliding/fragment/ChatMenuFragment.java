package com.beacon.afterui.sliding.fragment;

import java.util.List;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.ServiceConnection;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.Transformation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.activity.BaseActivity;
import com.beacon.afterui.chat.ChatManagerService;
import com.beacon.afterui.chat.ChatManagerService.ChatManagerImpl;
import com.beacon.afterui.chat.LoginListener;
import com.beacon.afterui.chat.RosterListAdapter;
import com.beacon.afterui.chat.RosterListener;
import com.beacon.afterui.constants.AppConstants;
import com.beacon.afterui.provider.AfterYouMetadata.RosterTable;
import com.beacon.afterui.provider.PreferenceEngine;
import com.beacon.afterui.sliding.SlidingActivity;
import com.beacon.afterui.sliding.customViews.ListViewAdapter;

/**
 * For showing left sliding menu behind main view.
 * 
 * @author spoddar
 * 
 */
public class ChatMenuFragment extends Fragment implements OnItemClickListener,
        OnItemLongClickListener, OnClickListener, OnLongClickListener,
        LoginListener, RosterListener, LoaderCallbacks<Cursor> {
    public static final String TAG = ChatMenuFragment.class.toString();
    private static final int ANIMATION_DURATION = 300;
    private static final float ANIMATION_X_TRANSLATION = 70.0f;
    private static final int TRANSLATION_X_RIGHT = 0x00000001;
    private static final int TRANSLATION_X_LEFT = 0x00000002;
    private static final int TRANSLATION_Y_TOP = 0x00000004;
    private static final int ANIMATION_DELETE = 0x00000008;

    private ListView mFavoriteMatchesList;
    private ListViewAdapter mListAdapter;
    private boolean mIsDeleteMode = false;
    private CloseSlidingMenuAdapter mCloseSlidingMenuAdapter = new CloseSlidingMenuAdapter();

    private Typeface typeFaceSemiBold;

    private Bitmap mUserThumbBitmap;
    private EditText mSearchEditText;
    private ChatManagerService mChatManager;

    private Handler mHandler;

    private RosterListAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mHandler = new Handler();

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        bindService();

        getLoaderManager().initLoader(0, null, this);
    }

    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        getLoaderManager().restartLoader(0, null, this);
    }

    private void bindService() {
        // Bind to LocalService
        Intent intent = new Intent(getActivity(), ChatManagerService.class);
        getActivity().bindService(intent, mServicConnection,
                Context.BIND_AUTO_CREATE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {

        // font myriadPro semibold
        typeFaceSemiBold = Typeface.createFromAsset(getActivity().getAssets(),
                "fonts/MyriadPro-Semibold.otf");
        // font myriadPro regular
        Typeface typeFaceRegular = Typeface.createFromAsset(getActivity()
                .getAssets(), "fonts/MyriadPro-Regular.otf");

        View view = inflater.inflate(R.layout.chat_view, null);

        mFavoriteMatchesList = (ListView) view
                .findViewById(R.id.favorite_match_list);
        mFavoriteMatchesList.setOnItemClickListener(this);
        mSearchEditText = (EditText) view.findViewById(R.id.search_txt_right);
        TextView create_group_chat_txt = (TextView) view
                .findViewById(R.id.create_group_chat_txt);
        TextView edit_txt = (TextView) view.findViewById(R.id.edit_txt);
        TextView favorite_txt = (TextView) view.findViewById(R.id.favorite_txt);
        TextView group_chat_txt = (TextView) view
                .findViewById(R.id.group_chat_txt);

        group_chat_txt.setTypeface(typeFaceSemiBold);
        create_group_chat_txt.setTypeface(typeFaceSemiBold);
        favorite_txt.setTypeface(typeFaceSemiBold);
        edit_txt.setTypeface(typeFaceSemiBold);
        mSearchEditText.setTypeface(typeFaceRegular);

        mAdapter = new RosterListAdapter(getActivity(), null,
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mFavoriteMatchesList.setAdapter(mAdapter);

        return view;
    }

    private boolean isRuntimePostJellyBean() {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, final View view,
            final int position, long id) {
        if (position >= mAdapter.getCount()) {
            return;
        }

        Log.d(TAG, "onItemClick() : " + position + " id : " + id);
        // Let's start the chat by sending the ID.

        Intent chatIntent = new Intent(getActivity(), ChatScreenFragment.class);
        chatIntent.putExtra(AppConstants.ROSTER_ID, id);
        startActivity(chatIntent);

        // List<View> views = mListAdapter.getAllViews();
        // views.remove(position);
        // animateListItems(TRANSLATION_X_LEFT, views);
    }

    private void setHasTransientState(View view, boolean b) {
        if (isRuntimePostJellyBean()) {
            view.setHasTransientState(b);
        } else {
            ViewCompat.setHasTransientState(view, b);
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view,
            int position, long id) {
        mIsDeleteMode = !mIsDeleteMode;
        switchDeleteMode(mIsDeleteMode);
        return true;
    }

    private void switchDeleteMode(boolean delMode) {
        if (!delMode) {
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                ViewGroup vg = (ViewGroup) mListAdapter.getItem(i).getView();
            }
        } else {
            for (int i = 0; i < mListAdapter.getCount(); i++) {
                ViewGroup vg = (ViewGroup) mListAdapter.getItem(i).getView();
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
    }

    @Override
    public boolean onLongClick(View v) {
        int id = v.getId();
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ((BaseActivity) this.getActivity()).showAbove();
    }

    private void animateListItems(int type, List<View> views) {
        animateListItems(type, views, mCloseSlidingMenuAdapter);
    }

    private void animateListItems(final int type, final List<View> views,
            final AnimatorListenerAdapter adapter) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                mFavoriteMatchesList.invalidateViews();
                final ViewTreeObserver observer = mFavoriteMatchesList
                        .getViewTreeObserver();
                for (View v : views) {
                    setHasTransientState(v, true);
                }
                observer.addOnPreDrawListener(new OnPreDrawListener() {

                    public boolean onPreDraw() {
                        observer.removeOnPreDrawListener(this);
                        for (View view : views) {
                            translationItem(type, view, adapter);
                            setHasTransientState(view, false);
                        }
                        return true;
                    }
                });
            }
        };
        getActivity().runOnUiThread(runnable);
    }

    private void translationItem(int type, View target,
            final AnimatorListenerAdapter adapter) {
        target.setPivotX(0.0f);
        target.setPivotY(0.0f);
        if (isRuntimePostJellyBean()) {
            ViewPropertyAnimator animator = target.animate();
            animator.setDuration(ANIMATION_DURATION);
            animator.setListener(adapter);
            switch (type) {
            case TRANSLATION_X_RIGHT:
                animator.translationX(ANIMATION_X_TRANSLATION);
                break;
            case TRANSLATION_Y_TOP:
                break;
            case TRANSLATION_X_LEFT:
                animator.translationX(0.0f);
                break;
            }
        } else {
            ListItemAnimation anim = new ListItemAnimation(type, target);
            anim.setAnimationListener(new AnimationAdapter() {
                public void onAnimationEnd(Animation animation) {
                    adapter.onAnimationEnd(null);
                }
            });
            anim.start();
        }
    }

    private class CloseSlidingMenuAdapter extends AnimatorListenerAdapter {
        public void onAnimationEnd(Animator animation) {
            Activity activity = getActivity();
            if (activity != null) {
                mIsDeleteMode = false;
                switchDeleteMode(false);
                ((SlidingActivity) activity).getSlidingMenu().showAbove();
            }
        }
    }

    public boolean onBack() {
        // attachAllCategories();
        return true;
    }

    private class ListItemAnimation extends Animation {
        private int mTransType;
        private View mTarget;

        public ListItemAnimation(int type, View v) {
            mTransType = type;
            mTarget = v;
            Rect targetRect = new Rect();
            v.getGlobalVisibleRect(targetRect);
            Rect listRect = new Rect();
            mFavoriteMatchesList.getGlobalVisibleRect(listRect);
            if (!listRect.contains(targetRect)) {
                end();
            } else {
                v.setAnimation(this);
            }
            setDuration(ANIMATION_DURATION);
        }

        private void end() {
            switch (mTransType) {
            case TRANSLATION_X_RIGHT:
                mTarget.setTranslationX(ANIMATION_X_TRANSLATION);
                break;
            case TRANSLATION_X_LEFT:
                mTarget.setTranslationX(0.0f);
                break;
            case ANIMATION_DELETE:
                mTarget.setTranslationX(-mTarget.getWidth());
                break;
            case TRANSLATION_Y_TOP:
                mTarget.setTranslationY(0.0f);
            }
        }

        @Override
        protected void applyTransformation(float interpolatedTime,
                Transformation t) {
            switch (mTransType) {
            case TRANSLATION_X_RIGHT:
                mTarget.setTranslationX(ANIMATION_X_TRANSLATION
                        * interpolatedTime);
                break;
            case TRANSLATION_X_LEFT:
                mTarget.setTranslationX(ANIMATION_X_TRANSLATION
                        - ANIMATION_X_TRANSLATION * interpolatedTime);
                break;
            case ANIMATION_DELETE:
                mTarget.setTranslationX(-mTarget.getWidth() * interpolatedTime);
                break;
            case TRANSLATION_Y_TOP:
                mTarget.setTranslationY(mTarget.getHeight()
                        - mTarget.getHeight() * interpolatedTime);
            }
        }

        @Override
        public void start() {
            super.start();
            mTarget.requestFocusFromTouch();
        }

        @Override
        public boolean willChangeBounds() {
            return true;
        }
    }

    private class AnimationAdapter implements AnimationListener {
        public void onAnimationStart(Animation animation) {
        }

        public void onAnimationEnd(Animation animation) {
        }

        public void onAnimationRepeat(Animation animation) {
        }
    }

    private OnMenuClickListener mOnMenuClickListener;

    public void setOnMenuClickListener(OnMenuClickListener onMenuClickListener) {
        mOnMenuClickListener = onMenuClickListener;
    }

    public static interface OnMenuClickListener {
        public void onMenuClick(Category category);

        public void onMutilMenuClick(List<Category> categories);
    }

    @Override
    public void onDestroy() {
        getActivity().unbindService(mServicConnection);

        if (mUserThumbBitmap != null) {
            mUserThumbBitmap.recycle();
            mUserThumbBitmap = null;
        }

        super.onDestroy();
    }

    private ServiceConnection mServicConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            ChatManagerImpl impl = (ChatManagerImpl) service;
            mChatManager = impl.getService();

            Log.d(TAG, "Service connected, login started!");
            PreferenceEngine prefEngine = PreferenceEngine
                    .getInstance(getActivity());

            // Get username and password.
            String userName = prefEngine.getUserName();
            final String password = prefEngine.getPassword();

            final String lastLoggedIn = prefEngine.getLastLoggedIn();

            if (!userName.equals(lastLoggedIn)) {
                // Clean DB.
                ContentResolver resolver = getActivity().getContentResolver();
                resolver.delete(RosterTable.CONTENT_URI, null, null);

                prefEngine.setLastLoggedIn(userName);
            }

            int index = userName.indexOf("@");
            userName = userName.substring(0, index);

            mChatManager.login(userName, password, ChatMenuFragment.this,
                    mHandler);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mChatManager = null;
        }
    };

    @Override
    public void onLoginSuccess() {
        Log.d(TAG, "onLoginSuccess");

        // On login success, let's fetch new rooster.
        mChatManager.updateRosterInDb(ChatMenuFragment.this, mHandler);
    }

    @Override
    public void onLoginFailed(int errorCode) {
        Log.d(TAG, "onLoginFailed : " + errorCode);
    }

    @Override
    public void onRosterFailed() {
        Log.e(TAG, "Roster list not received from server!");
    }

    @Override
    public void onRosterDownloaded() {
        Log.i(TAG, "Roster is loaded!");
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Log.d(TAG, "onCreateLoader()");
        return new CursorLoader(getActivity(), RosterTable.CONTENT_URI, null,
                null, null, RosterTable.NAME + " ASC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        Log.d(TAG, "onLoadFinished : "
                + (cursor == null ? "Cursor is NULL : " : cursor.getCount()));
        mAdapter.swapCursor(cursor);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        Log.d(TAG, "onLoaderReset()");
        mAdapter.swapCursor(null);
        mAdapter.notifyDataSetChanged();
    }
}
