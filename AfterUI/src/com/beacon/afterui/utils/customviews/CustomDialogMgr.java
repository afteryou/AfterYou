package com.beacon.afterui.utils.customviews;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.CursorAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.utils.Utilities;

public class CustomDialogMgr {

    private final Context mContext;
    private DialogInterface mDialogInterface;
    private Window mWindow;
    public final static int BIT_BUTTON_POSITIVE = 1;
    public final static int BIT_BUTTON_NEGATIVE = 2;
    public final static int BIT_BUTTON_NEUTRAL = 4;

    private CharSequence mTitle;

    private CharSequence mMessage;

    private ListView mListView;

    private View mView;

    private int mViewSpacingLeft;

    private int mViewSpacingTop;

    private int mViewSpacingRight;

    private int mViewSpacingBottom;

    private int whichButtons;

    private boolean mViewSpacingSpecified = false;

    private Button mButtonPositive;

    private CharSequence mButtonPositiveText;

    private Message mButtonPositiveMessage;

    private Button mButtonNegative;

    private CharSequence mButtonNegativeText;

    private Message mButtonNegativeMessage;

    private Button mButtonNeutral;

    private CharSequence mButtonNeutralText;

    private Message mButtonNeutralMessage;

    private ScrollView mScrollView;

    private int mIconId = -1;

    private Drawable mIcon;

    private ImageView mIconView;

    private TextView mTitleView;

    private TextView mMessageView;

    private int mMessageGravity = -1;

    private View mCustomTitleView;

    private boolean mForceInverseBackground;

    private ListAdapter mAdapter;

    private int mCheckedItem = -1;

    private Handler mHandler;

    private boolean mIsLowerCaseTitle = false;

    private boolean mIsCustomPadding = false;

    private boolean mIsCancelable = true;

    private int autoDismissStrategy = CustomDialog.AUTO_DIMISS_WHEN_BUTTON_CLICK;

    View.OnClickListener mButtonHandler = new View.OnClickListener() {
        public void onClick(View v) {
            Message m = null;
            int needDismiss = 0;
            if (v == mButtonPositive && mButtonPositiveMessage != null) {
                needDismiss = CustomDialog.NOT_DIMISS_WHEN_POSITIVE_BUTTON_CLICK
                        & autoDismissStrategy;
                m = Message.obtain(mButtonPositiveMessage);
            } else if (v == mButtonNegative && mButtonNegativeMessage != null) {
                needDismiss = CustomDialog.NOT_DIMISS_WHEN_NEGATIVE_BUTTON_CLICK
                        & autoDismissStrategy;
                m = Message.obtain(mButtonNegativeMessage);
            } else if (v == mButtonNeutral && mButtonNeutralMessage != null) {
                needDismiss = CustomDialog.NOT_DIMISS_WHEN_NEUTRAL_BUTTON_CLICK
                        & autoDismissStrategy;
                m = Message.obtain(mButtonNeutralMessage);
            }
            if (m != null) {
                m.sendToTarget();
            }
            // Post a message so we dismiss after the above handlers are
            // executed

            // add a flag, if you don't want dialog auto dismiss after you
            // select positive button, you can make this flag "false"
            if (isNeedDismiss(needDismiss)) {
                mHandler.obtainMessage(ButtonHandler.MSG_DISMISS_DIALOG,
                        mDialogInterface).sendToTarget();
            }
        }
    };

    private boolean isNeedDismiss(int needDismiss) {
        if (autoDismissStrategy == CustomDialog.AUTO_DIMISS_WHEN_BUTTON_CLICK) {
            return true;
        }
        if (needDismiss == 0) {
            return true;
        }
        return false;
    }

    private static final class ButtonHandler extends Handler {
        // Button clicks have Message.what as the BUTTON{1,2,3} constant
        private static final int MSG_DISMISS_DIALOG = 1;

        private WeakReference<DialogInterface> mDialog;

        public ButtonHandler(DialogInterface dialog) {
            mDialog = new WeakReference<DialogInterface>(dialog);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {

            case DialogInterface.BUTTON_POSITIVE:
            case DialogInterface.BUTTON_NEGATIVE:
            case DialogInterface.BUTTON_NEUTRAL:
                ((DialogInterface.OnClickListener) msg.obj).onClick(
                        mDialog.get(), msg.what);
                break;

            case MSG_DISMISS_DIALOG:
                ((DialogInterface) msg.obj).dismiss();
            }
        }
    }

    public CustomDialogMgr(Context context) {
        mContext = context;
    }

    public void setDialogInterface(DialogInterface di) {
        mDialogInterface = di;
        mHandler = new ButtonHandler(di);
    }

    public void setWindow(Window window) {
        mWindow = window;
    }

    public CustomDialogMgr(Context context, DialogInterface di, Window window) {
        mContext = context;
        mWindow = window;
    }

    static boolean canTextInput(View v) {
        if (v.onCheckIsTextEditor()) {
            return true;
        }

        if (!(v instanceof ViewGroup)) {
            return false;
        }

        ViewGroup vg = (ViewGroup) v;
        int i = vg.getChildCount();
        while (i > 0) {
            i--;
            v = vg.getChildAt(i);
            if (canTextInput(v)) {
                return true;
            }
        }

        return false;
    }

    public void installContent() {
        /* We use a custom title so never request a window title */
        mWindow.requestFeature(Window.FEATURE_NO_TITLE);

        if (mView == null || !canTextInput(mView)) {
            mWindow.setFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM,
                    WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        }

        View v = View.inflate(mContext, R.layout.custom_alert_dialog, null);
        int widht = mWindow.getWindowManager().getDefaultDisplay().getWidth();
        int height = mWindow.getWindowManager().getDefaultDisplay().getHeight();
        int dialog_width = widht > height ? widht : height;
        v.setMinimumWidth(dialog_width - Utilities.dipToPix(mContext, 24));
        mWindow.setContentView(v);
        // mWindow.getDecorView()
        // mWindow.setContentView(R.layout.custom_alert_dialog);pef

        // Set font for button.
        Typeface typeFaceRegular = Typeface.createFromAsset(
                mContext.getAssets(), "fonts/MyriadPro-Regular.otf");

        Button button1 = (Button) v.findViewById(R.id.button1);
        Button button2 = (Button) v.findViewById(R.id.button2);
        Button button3 = (Button) v.findViewById(R.id.button3);
        button1.setTypeface(typeFaceRegular);
        button2.setTypeface(typeFaceRegular);
        button3.setTypeface(typeFaceRegular);

        CustomDialogTitle alertTitle = (CustomDialogTitle) v
                .findViewById(R.id.alertTitle);
        alertTitle.setTypeface(typeFaceRegular);

        setupView();
    }

    public void setTitle(CharSequence title) {
        if (!mIsLowerCaseTitle)
            title = title.toString().toUpperCase();
        mTitle = title;
        if (mTitleView != null) {
            mTitleView.setText(title);
        }
    }

    public void setCustomTitle(View customTitleView) {
        mCustomTitleView = customTitleView;
    }

    public void setMessage(CharSequence message) {
        mMessage = message;
        if (mMessageView != null) {
            mMessageView.setText(message);
        }
    }

    public void setMessageGravity(int gravity) {
        mMessageGravity = gravity;
        if (mMessageView != null) {
            mMessageView.setGravity(gravity);
        }
    }

    public void setView(View view) {
        mView = view;
        mViewSpacingSpecified = false;
    }

    public void setView(View view, int viewSpacingLeft, int viewSpacingTop,
            int viewSpacingRight, int viewSpacingBottom) {
        mView = view;
        mViewSpacingSpecified = true;
        mViewSpacingLeft = viewSpacingLeft;
        mViewSpacingTop = viewSpacingTop;
        mViewSpacingRight = viewSpacingRight;
        mViewSpacingBottom = viewSpacingBottom;
    }

    public void setButton(int whichButton, CharSequence text,
            DialogInterface.OnClickListener listener, Message msg) {

        if (msg == null && listener != null) {
            msg = mHandler.obtainMessage(whichButton, listener);
        }

        switch (whichButton) {

        case DialogInterface.BUTTON_POSITIVE:
            mButtonPositiveText = text;
            mButtonPositiveMessage = msg;
            break;

        case DialogInterface.BUTTON_NEGATIVE:
            mButtonNegativeText = text;
            mButtonNegativeMessage = msg;
            break;

        case DialogInterface.BUTTON_NEUTRAL:
            mButtonNeutralText = text;
            mButtonNeutralMessage = msg;
            break;

        default:
            throw new IllegalArgumentException("Button does not exist");
        }
    }

    public void setIcon(int resId) {
        mIconId = resId;
        if (mIconView != null) {
            if (resId > 0) {
                mIconView.setImageResource(mIconId);
            } else if (resId == 0) {
                mIconView.setVisibility(View.GONE);
            }
        }
    }

    public void setIcon(Drawable icon) {
        mIcon = icon;
        if ((mIconView != null) && (mIcon != null)) {
            mIconView.setImageDrawable(icon);
        }
    }

    public void setInverseBackgroundForced(boolean forceInverseBackground) {
        mForceInverseBackground = forceInverseBackground;
    }

    public ListView getListView() {
        return mListView;
    }

    public boolean getCancelable() {
        return mIsCancelable;
    }

    public Button getButton(int whichButton) {
        switch (whichButton) {
        case DialogInterface.BUTTON_POSITIVE:
            return mButtonPositive;
        case DialogInterface.BUTTON_NEGATIVE:
            return mButtonNegative;
        case DialogInterface.BUTTON_NEUTRAL:
            return mButtonNeutral;
        default:
            return null;
        }
    }

    public int getButtonNumber() {
        return whichButtons;
    }

    public View.OnClickListener getButtonHandler() {
        return mButtonHandler;
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    @SuppressWarnings({ "UnusedDeclaration" })
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        return mScrollView != null && mScrollView.executeKeyEvent(event);
    }

    /*
     * private void setMinimumWidth(View v) {
     * v.setMinimumWidth(mWindow.getWindowManager()
     * .getDefaultDisplay().getWidth() - Utilities.dipToPix(mContext, 24)); }
     */

    private void setupView() {
        LinearLayout contentPanel = (LinearLayout) mWindow
                .findViewById(R.id.contentPanel);
        setupContent(contentPanel);

        boolean hasButtons = setupButtons();

        LinearLayout topPanel = (LinearLayout) mWindow
                .findViewById(R.id.topPanel);

        int[] attr = new int[] { android.R.attr.topDark,
                android.R.attr.centerDark, android.R.attr.bottomDark,
                android.R.attr.fullBright, android.R.attr.topBright,
                android.R.attr.centerBright, android.R.attr.bottomBright,
                android.R.attr.bottomMedium };

        TypedArray a = mContext.obtainStyledAttributes(attr);
        boolean hasTitle = setupTitle(topPanel);

        View buttonPanel = mWindow.findViewById(R.id.buttonPanel);
        if (!hasButtons) {
            buttonPanel.setVisibility(View.GONE);
        }

        FrameLayout customPanel = null;
        if (mView != null) {
            customPanel = (FrameLayout) mWindow.findViewById(R.id.customPanel);
            FrameLayout custom = (FrameLayout) mWindow
                    .findViewById(R.id.custom);
            custom.addView(mView, new LayoutParams(MATCH_PARENT, MATCH_PARENT));
            if (mViewSpacingSpecified) {
                custom.setPadding(mViewSpacingLeft, mViewSpacingTop,
                        mViewSpacingRight, mViewSpacingBottom);
            }
            if (mListView != null) {
                ((LinearLayout.LayoutParams) customPanel.getLayoutParams()).weight = 0;
            }
            /* setMinimumWidth(customPanel); */
        } else {
            mWindow.findViewById(R.id.customPanel).setVisibility(View.GONE);
        }

        setBackground(topPanel, contentPanel, customPanel, hasButtons, a,
                hasTitle, buttonPanel);
        a.recycle();
    }

    private boolean setupTitle(LinearLayout topPanel) {
        boolean hasTitle = true;

        if (mCustomTitleView != null) {
            // Add the custom title view directly to the topPanel layout
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT);

            topPanel.addView(mCustomTitleView, lp);

            // Hide the title template
            View titleTemplate = mWindow.findViewById(R.id.title_template);
            titleTemplate.setVisibility(View.GONE);
        } else {
            final boolean hasTextTitle = !TextUtils.isEmpty(mTitle);

            mIconView = (ImageView) mWindow.findViewById(R.id.icon);
            if (hasTextTitle) {
                /* Display the title if a title is supplied, else hide it */
                mTitleView = (TextView) mWindow.findViewById(R.id.alertTitle);
                if (mIsLowerCaseTitle) {
                    /* New style for the title of the dialog. */
                    mTitleView.setGravity(Gravity.LEFT);
                    View divider = mWindow.findViewById(R.id.titleDivider);
                    divider.setVisibility(View.VISIBLE);
                } else {
                    mTitleView.setGravity(Gravity.CENTER);
                    mTitle = mTitle.toString().toUpperCase();
                }
                mTitleView.setText(mTitle);

                /*
                 * Do this last so that if the user has supplied any icons we
                 * use them instead of the default ones. If the user has
                 * specified 0 then make it disappear.
                 */
                if (mIconId > 0) {
                    mIconView.setImageResource(mIconId);
                } else if (mIcon != null) {
                    mIconView.setImageDrawable(mIcon);
                } else if (mIconId == 0) {

                    /*
                     * Apply the padding from the icon to ensure the title is
                     * aligned correctly.
                     */
                    mTitleView.setPadding(mIconView.getPaddingLeft(),
                            mIconView.getPaddingTop(),
                            mIconView.getPaddingRight(),
                            mIconView.getPaddingBottom());
                    mIconView.setVisibility(View.GONE);
                }
                if (mIsLowerCaseTitle) {
                    ((LinearLayout) mTitleView.getParent())
                            .setBackgroundResource(R.color.brown_background);
                }
            } else {

                // Hide the title template
                View topPanelToGone = mWindow.findViewById(R.id.topPanel);
                topPanelToGone.setVisibility(View.GONE);
                mIconView.setVisibility(View.GONE);
                hasTitle = false;
            }
        }
        return hasTitle;
    }

    private void setupContent(LinearLayout contentPanel) {
        mScrollView = (ScrollView) mWindow.findViewById(R.id.scrollView);
        mScrollView.setFocusable(false);

        // Special case for users that only want to display a String
        mMessageView = (TextView) mWindow.findViewById(R.id.message);
        if (mMessageView == null) {
            return;
        }

        if (mMessage != null) {
            mMessageView.setText(mMessage);
            if (mIsCustomPadding) {
                LinearLayout mes = ((LinearLayout) mWindow
                        .findViewById(R.id.linearLayout4));
                mes.setPadding(mes.getPaddingLeft(), 0, mes.getPaddingRight(),
                        mes.getPaddingBottom());
            }
            if (mMessageGravity != -1) {
                mMessageView.setGravity(mMessageGravity);
            }
        } else {
            mMessageView.setVisibility(View.GONE);
            mScrollView.removeView(mMessageView);

            if (mListView != null) {
                contentPanel.removeView(mWindow.findViewById(R.id.scrollView));
                contentPanel.addView(mListView, new LinearLayout.LayoutParams(
                        MATCH_PARENT, MATCH_PARENT));
                contentPanel.setLayoutParams(new LinearLayout.LayoutParams(
                        MATCH_PARENT, 0, 1.0f));
            } else {
                contentPanel.setVisibility(View.GONE);
            }
        }
    }

    private boolean setupButtons() {
        mButtonPositive = (Button) mWindow.findViewById(R.id.button1);
        mButtonPositive.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonPositiveText)) {
            mButtonPositive.setVisibility(View.GONE);
        } else {
            mButtonPositive.setText(mButtonPositiveText);
            mButtonPositive.setVisibility(View.VISIBLE);
            whichButtons = whichButtons | BIT_BUTTON_POSITIVE;
        }

        mButtonNegative = (Button) mWindow.findViewById(R.id.button2);
        mButtonNegative.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonNegativeText)) {
            mButtonNegative.setVisibility(View.GONE);
        } else {
            mButtonNegative.setText(mButtonNegativeText);
            mButtonNegative.setVisibility(View.VISIBLE);

            whichButtons = whichButtons | BIT_BUTTON_NEGATIVE;
        }

        mButtonNeutral = (Button) mWindow.findViewById(R.id.button3);
        mButtonNeutral.setOnClickListener(mButtonHandler);

        if (TextUtils.isEmpty(mButtonNeutralText)) {
            mButtonNeutral.setVisibility(View.GONE);
        } else {
            mButtonNeutral.setText(mButtonNeutralText);
            mButtonNeutral.setVisibility(View.VISIBLE);

            whichButtons = whichButtons | BIT_BUTTON_NEUTRAL;
        }

        /*
         * If we only have 1 button it should be centered on the layout and
         * expand to fill 50% of the available space.
         */
        if (whichButtons == BIT_BUTTON_POSITIVE) {
            centerButton(mButtonPositive);
        } else if (whichButtons == BIT_BUTTON_NEGATIVE) {
            centerButton(mButtonNeutral);
        } else if (whichButtons == BIT_BUTTON_NEUTRAL) {
            centerButton(mButtonNeutral);
        }

        return whichButtons != 0;
    }

    private void centerButton(Button button) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) button
                .getLayoutParams();
        params.gravity = Gravity.CENTER_HORIZONTAL;
        params.weight = 0.5f;
        button.setLayoutParams(params);
        View leftSpacer = mWindow.findViewById(R.id.leftSpacer);
        leftSpacer.setVisibility(View.VISIBLE);
        View rightSpacer = mWindow.findViewById(R.id.rightSpacer);
        rightSpacer.setVisibility(View.VISIBLE);
    }

    private void setBackground(LinearLayout topPanel,
            LinearLayout contentPanel, View customPanel, boolean hasButtons,
            TypedArray a, boolean hasTitle, View buttonPanel) {

        /*
         * Get all the different background required int fullDark =
         * a.getResourceId( android.R.attr.fullDark, R.drawable.dialog_body);
         * 
         * int topDark = a.getResourceId( android.R.attr.topDark,
         * R.drawable.dialog_title); int centerDark = a.getResourceId(
         * android.R.attr.centerDark, R.drawable.dialog_body); int bottomDark =
         * a.getResourceId( android.R.attr.bottomDark, R.drawable.dialog_body);
         * int fullBright = a.getResourceId( android.R.attr.fullBright,
         * R.drawable.dialog_body); int topBright = a.getResourceId(
         * android.R.attr.topBright, R.drawable.dialog_body); int centerBright =
         * a.getResourceId( android.R.attr.centerBright,
         * R.drawable.dialog_body); int bottomBright = a.getResourceId(
         * android.R.attr.bottomBright, R.drawable.dialog_body); int
         * bottomMedium = a.getResourceId( android.R.attr.bottomMedium,
         * R.drawable.dialog_body);
         */

        /* Get all the different background required */
        int fullDark = R.drawable.dialog_body;
        int topDark = R.drawable.dialog_title;
        int centerDark = R.drawable.cal_dialog_body;
        int bottomDark = R.drawable.cal_dialog_body;
        int fullBright = R.drawable.dialog_body;
        int topBright = R.drawable.dialog_body;
        int centerBright = R.drawable.dialog_body;
        int bottomBright = R.drawable.dialog_body;
        int bottomMedium = R.drawable.dialog_body;
        /*
         * We now set the background of all of the sections of the alert. First
         * collect together each section that is being displayed along with
         * whether it is on a light or dark background, then run through them
         * setting their backgrounds. This is complicated because we need to
         * correctly use the full, top, middle, and bottom graphics depending on
         * how many views they are and where they appear.
         */

        View[] views = new View[4];
        boolean[] light = new boolean[4];
        View lastView = null;
        boolean lastLight = false;

        int pos = 0;
        if (hasTitle) {
            views[pos] = topPanel;
            light[pos] = false;
            pos++;
        }

        /*
         * The contentPanel displays either a custom text message or a ListView.
         * If it's text we should use the dark background for ListView we should
         * use the light background. If neither are there the contentPanel will
         * be hidden so set it as null.
         */
        views[pos] = (contentPanel.getVisibility() == View.GONE) ? null
                : contentPanel;
        light[pos] = mListView != null;
        pos++;
        if (customPanel != null) {
            views[pos] = customPanel;
            light[pos] = mForceInverseBackground;
            pos++;
        }
        if (hasButtons) {
            views[pos] = buttonPanel;
            light[pos] = true;
        }

        boolean setView = false;
        for (pos = 0; pos < views.length; pos++) {
            View v = views[pos];
            if (v == null) {
                continue;
            }
            if (lastView != null) {
                if (!setView) {
                    lastView.setBackgroundResource(lastLight ? topBright
                            : topDark);
                } else {
                    lastView.setBackgroundResource(lastLight ? centerBright
                            : centerDark);
                }
                setView = true;
            }
            lastView = v;
            lastLight = light[pos];
        }

        if (lastView != null) {
            if (setView) {

                /*
                 * ListViews will use the Bright background but buttons use the
                 * Medium background.
                 */
                lastView.setBackgroundResource(lastLight ? (hasButtons ? bottomMedium
                        : bottomBright)
                        : bottomDark);
            } else {
                lastView.setBackgroundResource(lastLight ? fullBright
                        : fullDark);
            }
        }

        if ((mListView != null) && (mAdapter != null)) {
            mListView.setAdapter(mAdapter);
            if (mCheckedItem > -1) {
                mListView.setItemChecked(mCheckedItem, true);
                mListView.setSelection(mCheckedItem);
            }
        }
    }

    public static class AlertParams {
        public final Context mContext;
        public final LayoutInflater mInflater;

        public int mIconId = 0;
        public Drawable mIcon;
        public CharSequence mTitle;
        public View mCustomTitleView;
        public CharSequence mMessage;
        public int mMessageGravity = -1;
        public CharSequence mPositiveButtonText;
        public DialogInterface.OnClickListener mPositiveButtonListener;
        public CharSequence mNegativeButtonText;
        public DialogInterface.OnClickListener mNegativeButtonListener;
        public CharSequence mNeutralButtonText;
        public DialogInterface.OnClickListener mNeutralButtonListener;
        public boolean mCancelable;
        public boolean mAutoDismissable;
        public DialogInterface.OnCancelListener mOnCancelListener;
        public DialogInterface.OnKeyListener mOnKeyListener;
        public CharSequence[] mItems;
        public ListAdapter mAdapter;
        public DialogInterface.OnClickListener mOnClickListener;
        public View mView;
        public int mViewSpacingLeft;
        public int mViewSpacingTop;
        public int mViewSpacingRight;
        public int mViewSpacingBottom;
        public boolean mViewSpacingSpecified = false;
        public boolean[] mCheckedItems;
        public boolean mIsMultiChoice;
        public boolean mIsSingleChoice;
        public int mCheckedItem = -1;
        public DialogInterface.OnMultiChoiceClickListener mOnCheckboxClickListener;
        public Cursor mCursor;
        public String mLabelColumn;
        public String mIsCheckedColumn;
        public boolean mForceInverseBackground;
        public AdapterView.OnItemSelectedListener mOnItemSelectedListener;
        public OnPrepareListViewListener mOnPrepareListViewListener;
        public boolean mRecycleOnMeasure = true;
        public boolean mIsLowerCaseTitle = false;
        public boolean mIsCustomPadding = false;
        public int autoDismissStrategy = CustomDialog.AUTO_DIMISS_WHEN_BUTTON_CLICK;
        public Runnable autoDismissRunnable;

        public interface OnPrepareListViewListener {
            void onPrepareListView(ListView listView);
        }

        public AlertParams(Context context) {
            mContext = context;
            mCancelable = true;
            mInflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        public void apply(CustomDialogMgr dialog) {
            // For a list, the client can either supply an array of items or an
            // adapter or a cursor
            if ((mItems != null) || (mCursor != null) || (mAdapter != null)) {
                createListView(dialog);
                this.mIsLowerCaseTitle = true;
            }
            if (mIsLowerCaseTitle) {
                dialog.mIsLowerCaseTitle = true;
            }
            if (mCustomTitleView != null) {
                dialog.setCustomTitle(mCustomTitleView);
            } else {
                if (mTitle != null) {
                    dialog.setTitle(mTitle);
                }
                if (mIcon != null) {
                    dialog.setIcon(mIcon);
                }
                if (mIconId >= 0) {
                    dialog.setIcon(mIconId);
                }
            }
            if (mMessage != null) {
                dialog.setMessage(mMessage);
            }
            if (mMessageGravity != -1) {
                dialog.setMessageGravity(mMessageGravity);
            }
            if (mPositiveButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_POSITIVE,
                        mPositiveButtonText, mPositiveButtonListener, null);
            }
            if (mNegativeButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE,
                        mNegativeButtonText, mNegativeButtonListener, null);
            }
            if (mNeutralButtonText != null) {
                dialog.setButton(DialogInterface.BUTTON_NEUTRAL,
                        mNeutralButtonText, mNeutralButtonListener, null);
            }
            if (mForceInverseBackground) {
                dialog.setInverseBackgroundForced(true);
            }
            if (mView != null) {
                if (mViewSpacingSpecified) {
                    dialog.setView(mView, mViewSpacingLeft, mViewSpacingTop,
                            mViewSpacingRight, mViewSpacingBottom);
                } else {
                    dialog.setView(mView);
                }
            }
            if (mIsCustomPadding) {
                dialog.mIsCustomPadding = true;
            }

            dialog.mIsCancelable = mCancelable;
            dialog.autoDismissStrategy = autoDismissStrategy;
        }

        public DialogInterface.OnClickListener getOnButtonClickListener() {
            if (mPositiveButtonListener != null
                    && (mNegativeButtonListener == null && mNeutralButtonListener == null)) {
                return mPositiveButtonListener;
            } else if (mNegativeButtonListener != null
                    && (mPositiveButtonListener == null && mNeutralButtonListener == null)) {
                return mNegativeButtonListener;
            } else if (mNeutralButtonListener != null
                    && (mPositiveButtonListener == null && mNegativeButtonListener == null)) {
                return mNeutralButtonListener;
            }
            return null;
        }

        private void createListView(final CustomDialogMgr dialog) {
            final ListView listView = (ListView) mInflater.inflate(
                    R.layout.custom_alert_dialog_custom_list_view, null);
            listView.setDivider(mContext.getResources().getDrawable(
                    R.drawable.divider_listview));
            listView.setDividerHeight(2);

            ListAdapter adapter;

            if (mIsMultiChoice) {
                if (mCursor == null) {
                    adapter = new ArrayAdapter<CharSequence>(
                            mContext,
                            R.layout.custom_alert_dialog_custom_list_view_multichoice,
                            R.id.text1, mItems) {
                        @Override
                        public View getView(int position, View convertView,
                                ViewGroup parent) {
                            View view = super.getView(position, convertView,
                                    parent);
                            if (mCheckedItems != null) {
                                boolean isItemChecked = mCheckedItems[position];
                                if (isItemChecked) {
                                    listView.setItemChecked(position, true);
                                }
                            }
                            return view;
                        }
                    };
                } else {
                    adapter = new CursorAdapter(mContext, mCursor, false) {
                        private final int mLabelIndex;
                        private final int mIsCheckedIndex;

                        {
                            final Cursor cursor = getCursor();
                            mLabelIndex = cursor
                                    .getColumnIndexOrThrow(mLabelColumn);
                            mIsCheckedIndex = cursor
                                    .getColumnIndexOrThrow(mIsCheckedColumn);
                        }

                        @Override
                        public void bindView(View view, Context context,
                                Cursor cursor) {
                            CheckedTextView text = (CheckedTextView) view
                                    .findViewById(R.id.text1);
                            text.setText(cursor.getString(mLabelIndex));
                            listView.setItemChecked(cursor.getPosition(),
                                    cursor.getInt(mIsCheckedIndex) == 1);
                        }

                        @Override
                        public View newView(Context context, Cursor cursor,
                                ViewGroup parent) {
                            return mInflater
                                    .inflate(
                                            R.layout.custom_alert_dialog_custom_list_view_multichoice,
                                            parent, false);
                        }

                    };
                }
            } else {
                int layout = mIsSingleChoice ? R.layout.custom_alert_dialog_custom_list_view_singlechoice
                        : R.layout.custom_alert_dialog_custom_list_view_item;
                if (mCursor == null) {
                    adapter = (mAdapter != null) ? mAdapter
                            : new ArrayAdapter<CharSequence>(mContext, layout,
                                    R.id.text1, mItems);
                } else {
                    adapter = new SimpleCursorAdapter(mContext, layout,
                            mCursor, new String[] { mLabelColumn },
                            new int[] { R.id.text1 });
                }
            }

            if (mOnPrepareListViewListener != null) {
                mOnPrepareListViewListener.onPrepareListView(listView);
            }

            dialog.mAdapter = adapter;
            dialog.mCheckedItem = mCheckedItem;

            if (mOnClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v,
                            int position, long id) {
                        mOnClickListener.onClick(dialog.mDialogInterface,
                                position);
                        if (mIsSingleChoice) {
                            dialog.mDialogInterface.dismiss();
                        }
                    }
                });
            } else if (mOnCheckboxClickListener != null) {
                listView.setOnItemClickListener(new OnItemClickListener() {
                    public void onItemClick(AdapterView parent, View v,
                            int position, long id) {
                        if (mCheckedItems != null) {
                            mCheckedItems[position] = listView
                                    .isItemChecked(position);
                        }
                        mOnCheckboxClickListener.onClick(
                                dialog.mDialogInterface, position,
                                listView.isItemChecked(position));
                    }
                });
            }

            // Attach a given OnItemSelectedListener to the ListView
            if (mOnItemSelectedListener != null) {
                listView.setOnItemSelectedListener(mOnItemSelectedListener);
            }

            if (mIsSingleChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
            } else if (mIsMultiChoice) {
                listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
            }
            dialog.mListView = listView;

        }
    }

}