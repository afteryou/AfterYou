
package com.beacon.afterui.utils.customviews;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnKeyListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.database.DataSetObserver;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;

import com.beacon.afterui.R;



public class DialogHelper {

    private DialogHelper() {
    }

    /**
     * Create an Error AlertDialog.Builder with disable 'Find Key' flag.
     *
     * @param ctx
     *            The context that build this dialog.
     * @param disableFindKey
     *            Whether disable 'Find Key'
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createMessageDialogBuilder(final Context ctx,
                                                                 boolean disableFindKey) {

        CustomDialog.Builder builder = new CustomDialog.Builder(ctx,
				new AfterYouDialogImpl.AfterYouBuilderImpl(ctx));

        if (disableFindKey) {
            builder.setOnKeyListener(new CustomOnKeyListener());
        }
        return builder;
    }

    /**
     * Create an AlertDialog.Builder with a 'OK' button and disable 'Find Key'.
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createBaseDialogBuilder(final Activity mActivity, int titleId) {

        return createBaseDialogBuilder(mActivity, titleId, false);
    }

    /**
     * Create an AlertDialog.Builder with a 'OK' button.
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param disableFindKey
     *            Whether disable 'Find Key'
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createBaseDialogBuilder(final Activity mActivity, int titleId,
                                                              boolean disableFindKey) {

        CustomDialog.Builder builder = new CustomDialog.Builder(mActivity,
				new AfterYouDialogImpl.AfterYouBuilderImpl(mActivity));
        builder.setTitle(titleId);
        builder.setInverseBackgroundForced(true);
//        builder.setIcon(R.drawable.ic_dialog_menu_generic);
        builder.setPositiveButton(R.string.IDS_OK, null);
        if (disableFindKey) {
            builder.setOnKeyListener(new CustomOnKeyListener());
        }

        return builder;
    }
    public static CustomDialog.Builder createBaseDialogBuilderWithOutOkButton(final Activity mActivity, int titleId,
            boolean disableFindKey) {

        CustomDialog.Builder builder = new CustomDialog.Builder(mActivity,
				new AfterYouDialogImpl.AfterYouBuilderImpl(mActivity));
			builder.setTitle(titleId);
			builder.setInverseBackgroundForced(true);
			if (disableFindKey) {
				builder.setOnKeyListener(new CustomOnKeyListener());
			}

			return builder;
	}
    /**
     * Create an AlertDialog.Builder with a 'OK' button.
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param onClickListener
     *            The OnClickListener for the 'OK' button
     * @param disableFindKey
     *            Whether disable 'Find Key'
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createBaseDialogBuilder(final Activity mActivity, int titleId,
                                                              OnClickListener onClickListener, boolean disableFindKey) {

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, disableFindKey);
        builder.setPositiveButton(R.string.IDS_OK, onClickListener);

        return builder;
    }

    /**
     * Create a SingleChoice AlertDialog.Builder with a 'OK' button.
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param dialogId
     *            The dialogId for the this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param itemsId
     *            The resource id of an array
     * @param checkedItem
     *            Specifies which item is checked. If -1 no items are checked.
     * @param onClickListener
     *            Notified when an item on the list is clicked. The dialog will
     *            not be dismissed when an item is clicked. It will only be
     *            dismissed if clicked on a button, if no buttons are supplied
     *            it's up to the user to dismiss the dialog.
     *
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createSingleChoiceDialogBuilder(final Activity mActivity, final int dialogId,
                                                                      int titleId, int itemsId, int checkedItem,
                                                                      OnClickListener onClickListener) {

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, onClickListener, false);
        builder.setSingleChoiceItems(itemsId, checkedItem, onClickListener);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mActivity.removeDialog(dialogId);
            }
        });

        return builder;
    }
    public static CustomDialog.Builder createSingleChoiceDialogBuilderWithoutOkButton(final Activity mActivity, final int dialogId,
            int titleId, int itemsId, int checkedItem,
            OnClickListener onClickListener) {

            CustomDialog.Builder builder = createBaseDialogBuilderWithOutOkButton(mActivity, titleId, false);
			builder.setSingleChoiceItems(itemsId, checkedItem, onClickListener);
			builder.setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						mActivity.removeDialog(dialogId);
					}
			});

	return builder;
	}
    /**
     * Create a MultiChoice AlertDialog.Builder with a 'OK' button.
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param dialogId
     *            The dialogId for the this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param itemsId
     *            The resource id of an array
     * @param checkedItems
     *            Specifies which items are checked. If -1 no items are checked.
     * @param onClickListener
     *            Notified when an item on the list is clicked. The dialog will
     *            not be dismissed when an item is clicked. It will only be
     *            dismissed if clicked on a button, if no buttons are supplied
     *            it's up to the user to dismiss the dialog.
     *
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createMultiChoiceDialogBuilder(final Activity mActivity, final int dialogId,
                                                                     int titleId, int itemsId,
                                                                     final boolean[] checkedItems,
                                                                     OnClickListener onClickListener) {

        OnMultiChoiceClickListener mListenter = new OnMultiChoiceClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
            }
        };

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, onClickListener, false);
        builder.setMultiChoiceItems(itemsId, checkedItems, mListenter);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mActivity.removeDialog(dialogId);
            }
        });

        return builder;
    }
    /**
     * Create an AlertDialog.Builder with a 'OK' button and a 'NO' button
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param messageId
     *            The string id that will be display on the dialog.
     * @param onClickListener
     *            The OnClickListener for the 'OK' button
     * @param mItemListener
     * 			  The OnMultiChoiceClickListener for item selection event
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createMultiChoiceDialogBuilder(final Activity mActivity,
    		final int dialogId,
            int titleId, int itemsId,
            final boolean[] checkedItems,
            OnClickListener onClickListener,
            OnMultiChoiceClickListener mItemListener,
            OnCancelListener cancelListener) {

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, onClickListener, false);
    	builder.setMultiChoiceItems(itemsId, checkedItems, mItemListener);
    	builder.setPositiveButton(R.string.done_txt, onClickListener);
    	if(cancelListener == null)
    		builder.setOnCancelListener(new OnCancelListener() {
    			@Override
    			public void onCancel(DialogInterface dialog) {
    				mActivity.removeDialog(dialogId);
    			}
    	});
    	else
    		builder.setOnCancelListener(cancelListener);
    	return builder;
    }
    /**
     * Create an AlertDialog.Builder with a 'OK' button and a 'NO' button
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param messageId
     *            The string id that will be display on the dialog.
     * @param onClickListener
     *            The OnClickListener for the 'OK' button
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createConfirmDialogBuilder(final Activity mActivity, int titleId, int messageId,
                                                                 OnClickListener onClickListener) {

        CustomDialog.Builder builder = createAlertDialogBuilder(mActivity, titleId, messageId, onClickListener);
        builder.setNegativeButton(R.string.IDS_NO, onClickListener);

        return builder;
    }

    /**
     * Create AlertDialog.Builder with a 'OK' button and can not cancel.
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param titleId
     *            The string id that will be used as title for this dialog.
     * @param messageId
     *            The string id that will be display on the dialog.
     * @param onClickListener
     *            The OnClickListener for the 'OK' button
     * @return AlertDialog.Builder
     */
    public static CustomDialog.Builder createAlertDialogBuilder(final Activity mActivity, int titleId, int messageId,
                                                               OnClickListener onClickListener) {

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, onClickListener, true);
        builder.setMessage(messageId);
        builder.setCancelable(false);

        return builder;
    }

    public static CustomDialog.Builder createMultiChoiceDialogBuilder(final Activity mActivity, final int dialogId,
                                                                     int titleId, ArrayAdapter<?> mArrayAdapter,
                                                                     OnClickListener onClickListener) {

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, onClickListener, false);
        builder.setAdapter(mArrayAdapter, onClickListener);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mActivity.removeDialog(dialogId);
            }
        });

        return builder;
    }

    public static CustomDialog.Builder createMultiChoiceDialogBuilder(final Activity mActivity, final int dialogId,
                                                                     int titleId, ArrayAdapter<?> mArrayAdapter,
                                                                     int dialogViewId, OnClickListener onClickListener) {

        CustomDialog.Builder builder = createBaseDialogBuilder(mActivity, titleId, onClickListener, false);

        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(dialogViewId, null);
        builder.setView(dialogView);
        builder.setAdapter(mArrayAdapter, onClickListener);
        builder.setOnCancelListener(new OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                mActivity.removeDialog(dialogId);
            }
        });

        return builder;
    }

    /**
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param messageId
     *            The string id that will be display on the dialog.
     * @param onCancelListener
     *            The OnCancelListener for this dialog. If null the dialog will
     *            be no-cancelable.
     * @return ProgressDialog that find key has been disable.
     */
    public static CustomProgressDialog createProgessDialog(final Activity mActivity, int messageId,
                                                     OnCancelListener onCancelListener) {
        return createProgessDialog(mActivity, messageId, null, onCancelListener, true);
    }

    /**
     *
     * @param mActivity
     *            The activity that owner this dialog.
     * @param messageId
     *            The string id that will be display on the dialog.
     * @param onKeyListener
     *            The OnKeyListener for this dialog. If null no onKeyListener
     *            will be set to the dialog.
     * @param onCancelListener
     *            The OnCancelListener for this dialog. If null the dialog will
     *            be no-cancelable.
     * @param disableFindKey
     *            Whether disable 'Find Key'
     * @return ProgressDialog
     */
    public static CustomProgressDialog createProgessDialog(final Activity mActivity, int messageId,
                                                     OnKeyListener onKeyListener, OnCancelListener onCancelListener,
                                                     boolean disableFindKey) {

        CustomProgressDialog progressDialog = new AfterYouProgressDialog(new AfterYouDialogImpl(mActivity), mActivity,
				R.style.Theme_CustomDialog);
        progressDialog.setMessage(mActivity.getString(messageId));
        progressDialog.setOnCancelListener(onCancelListener);
        progressDialog.setCanceledOnTouchOutside(false);

        if (disableFindKey) {
            progressDialog.setOnKeyListener(new CustomOnKeyListener(onKeyListener));
        } else {
            progressDialog.setOnKeyListener(onKeyListener);
        }

        return progressDialog;
    }

    /**
     * Create an ProgressDialog with disable 'Find Key'.
     *
     * @param ctx
     *            The context that build this dialog.
     * @param onKeyListener
     *            The OnKeyListener for this dialog. If null no onKeyListener
     *            will be set to the dialog.
     * @return ProgressDialog that find key has been disable.
     */
    public static CustomProgressDialog createProgessDialog(final Context ctx, OnKeyListener onKeyListener) {

        CustomProgressDialog progressDialog = new AfterYouProgressDialog(new AfterYouDialogImpl(ctx), ctx,
				R.style.Theme_CustomDialog);
    	progressDialog.setOnKeyListener(new CustomOnKeyListener(onKeyListener));
    	progressDialog.setCanceledOnTouchOutside(false);
        return progressDialog;
    }


    /* The current CustomOnKeyListener only disable 'Find Key' */
    public static class CustomOnKeyListener implements OnKeyListener {

        private OnKeyListener mOnKeyListener;

        public CustomOnKeyListener() {
        }

        public CustomOnKeyListener(OnKeyListener mOnKeyListener) {
            this.mOnKeyListener = mOnKeyListener;
        }

        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_SEARCH && event.getRepeatCount() == 0) {
                return true;
            }
            if (keyCode == KeyEvent.KEYCODE_MENU && event.isLongPress()
    	            && !event.isCanceled()) {
    			return true;
    		}
            if (mOnKeyListener == null) {
                return false;
            }

            return mOnKeyListener.onKey(dialog, keyCode, event);
        }
    }

    public static class CustomDialogAdapter implements ListAdapter {

        ListAdapter mListAdapter;
        boolean enabledIitems[];

        public CustomDialogAdapter(ListAdapter mListAdapter, boolean enabledIitems[]) {
            this.mListAdapter = mListAdapter;
            this.enabledIitems = enabledIitems;
        }

        @Override
        public void unregisterDataSetObserver(DataSetObserver observer) {
            mListAdapter.unregisterDataSetObserver(observer);
        }

        @Override
        public void registerDataSetObserver(DataSetObserver observer) {
            mListAdapter.registerDataSetObserver(observer);
        }

        @Override
        public boolean isEmpty() {
            return mListAdapter.isEmpty();
        }

        @Override
        public boolean hasStableIds() {
            return mListAdapter.hasStableIds();
        }

        @Override
        public int getViewTypeCount() {
            return mListAdapter.getViewTypeCount();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mListAdapter.getView(position, convertView, parent);
            view.setEnabled(isEnabled(position));
            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return mListAdapter.getItemViewType(position);
        }

        @Override
        public long getItemId(int position) {
            return mListAdapter.getItemId(position);
        }

        @Override
        public Object getItem(int position) {
            return mListAdapter.getItem(position);
        }

        @Override
        public int getCount() {
            return mListAdapter.getCount();
        }

        @Override
        public boolean isEnabled(int position) {
            return enabledIitems[position];
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }
    }
}
