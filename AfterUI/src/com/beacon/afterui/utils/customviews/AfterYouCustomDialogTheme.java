package com.beacon.afterui.utils.customviews;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.StateSet;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.log.AfterUIlog;

public class AfterYouCustomDialogTheme {

	public static CustomDialog.Builder newBuilder(Context context) {
		return new CustomDialog.Builder(context,
				new AfterYouDialogImpl.AfterYouBuilderImpl(context));
	}

	public ErrorDialog newErrorDialog(Context context,
			final OnClickListener clickListener, String msg) {
		return new ErrorDialog(new AfterYouDialogImpl(context), context,
				R.style.Theme_CustomDialog, clickListener, msg);
	}

	public ErrorDialog newErrorDialog(Context context,
			final OnClickListener clickListener, int msgId) {
		return new ErrorDialog(new AfterYouDialogImpl(context), context,
				R.style.Theme_CustomDialog, clickListener, msgId);
	}

//	public CustomProgressDialog newCustomProgressDialog(Context context) {
//		return new AfterYouProgressDialog(new AfterYouDialogImpl(context),
//				context, R.style.Theme_CustomDialog);
//	}

	public CustomerDatePickDialog newCustomDatePickDialog(Context context,
			OnDateSetListener callBack, int year, int monthOfYear,
			int dayOfMonth) {
		return new CustomerDatePickDialog(new AfterYouDialogImpl(context),
				context, R.style.Theme_CustomDialog, callBack, year,
				monthOfYear, dayOfMonth);
	}

	public void setTitleText(Context ctx, TextView tv, String text) {
		Typeface tfMed = Typeface.createFromAsset(ctx.getAssets(),
				"MyriadPro-BoldCondIt.otf");
		tv.setTypeface(tfMed);
		tv.setText(text.toUpperCase());
		tv.setIncludeFontPadding(false);
	}

	public void setTitleText(Context ctx, TextView tv, int textId) {
		Typeface tfMed = Typeface.createFromAsset(ctx.getAssets(),
				"MyriadPro-BoldCondIt.otf");
		tv.setTypeface(tfMed);
		tv.setText(ctx.getResources().getString(textId).toUpperCase());
		tv.setIncludeFontPadding(false);
	}

	public static volatile float MenuTextSize = -1;

	public void setMenuBackground(final Activity act) {
		try {
			act.getLayoutInflater().setFactory(new Factory() {

				public View onCreateView(final String name,
						final Context context, final AttributeSet attrs) {

					if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
							|| name.equalsIgnoreCase("com.android.internal.view.menu.ListMenuItemView")) {

						try {
							final LayoutInflater f = act.getLayoutInflater();
							final View[] view = new View[1];
							try {
								view[0] = f.createView(name, null, attrs);
							} catch (InflateException e) {
								fixForAndroid23Menu(name, attrs, f, view);
							}
							if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")
									&& MenuTextSize < 0)
								try {
									MenuTextSize = ((TextView) view[0])
											.getTextSize();
								} catch (Exception e) {
									MenuTextSize = ((TextView) view[0]
											.findViewById(android.R.id.title))
											.getTextSize();
								}

							new Handler().post(new Runnable() {
								public void run() {

									if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
										TextView tv = (TextView) view[0]
												.findViewById(android.R.id.title);
										if (tv.getText().equals(
												act.getResources().getString(
														R.string.IDS_MORE))) {
											Drawable draw = act
													.getResources()
													.getDrawable(
															R.drawable.menu_more);
											try {
												Class menuItemView = Class
														.forName("com.android.internal.view.menu.IconMenuItemView");
												Method method = menuItemView
														.getMethod("setIcon",
																Drawable.class);
												method.invoke(view[0], draw);
											} catch (Exception e) {
												AfterUIlog.printStackTrace(e);
											}
										}
									}

									CustomStateListDrawable states;
									try {
										states = new CustomStateListDrawable(
												context, (TextView) view[0]);
									} catch (Exception e) {
										states = new CustomStateListDrawable(
												context,
												(TextView) view[0]
														.findViewById(android.R.id.title));
									}
									view[0].setBackgroundDrawable(states);

									if (name.equalsIgnoreCase("com.android.internal.view.menu.ListMenuItemView")) {
										try {
											((TextView) view[0]).setTextSize(
													TypedValue.COMPLEX_UNIT_PX,
													MenuTextSize);
										} catch (Exception e) {
											((TextView) view[0]
													.findViewById(android.R.id.title))
													.setTextSize(
															TypedValue.COMPLEX_UNIT_PX,
															MenuTextSize);
										}
									}
								}
							});
							return view[0];
						} catch (InflateException e) {
						} catch (ClassNotFoundException e) {

						}
					}
					return null;
				}
			});
		} catch (IllegalStateException e) {
		}
	}

	private static void fixForAndroid23Menu(final String name,
			final android.util.AttributeSet attrs, final LayoutInflater f,
			final View[] view) {
		try {
			f.inflate(new XmlPullParser() {

				public int next() throws XmlPullParserException, IOException {
					try {
						view[0] = (View) f.createView(name, null, attrs);
					} catch (InflateException e) {
					} catch (ClassNotFoundException e) {
					}
					throw new XmlPullParserException("exit");
				}

				public void defineEntityReplacementText(String arg0, String arg1)
						throws XmlPullParserException {
				}

				public int getAttributeCount() {
					return 0;
				}

				public String getAttributeName(int arg0) {
					return null;
				}

				public String getAttributeNamespace(int arg0) {
					return null;
				}

				public String getAttributePrefix(int arg0) {
					return null;
				}

				public String getAttributeType(int arg0) {
					return null;
				}

				public String getAttributeValue(int arg0) {
					return null;
				}

				public String getAttributeValue(String arg0, String arg1) {
					return null;
				}

				public int getColumnNumber() {
					return 0;
				}

				public int getDepth() {
					return 0;
				}

				public int getEventType() throws XmlPullParserException {
					return 0;
				}

				public boolean getFeature(String arg0) {
					return false;
				}

				public String getInputEncoding() {
					return null;
				}

				public int getLineNumber() {
					return 0;
				}

				public String getName() {
					return null;
				}

				public String getNamespace() {
					return null;
				}

				public String getNamespace(String arg0) {
					return null;
				}

				public int getNamespaceCount(int arg0)
						throws XmlPullParserException {
					return 0;
				}

				public String getNamespacePrefix(int arg0)
						throws XmlPullParserException {
					return null;
				}

				public String getNamespaceUri(int arg0)
						throws XmlPullParserException {
					return null;
				}

				public String getPositionDescription() {
					return null;
				}

				public String getPrefix() {
					return null;
				}

				public Object getProperty(String arg0) {
					return null;
				}

				public String getText() {
					return null;
				}

				public char[] getTextCharacters(int[] arg0) {
					return null;
				}

				public boolean isAttributeDefault(int arg0) {
					return false;
				}

				public boolean isEmptyElementTag()
						throws XmlPullParserException {
					return false;
				}

				public boolean isWhitespace() throws XmlPullParserException {
					return false;
				}

				public int nextTag() throws XmlPullParserException, IOException {
					return 0;
				}

				public String nextText() throws XmlPullParserException,
						IOException {
					return null;
				}

				public int nextToken() throws XmlPullParserException,
						IOException {
					return 0;
				}

				public void require(int arg0, String arg1, String arg2)
						throws XmlPullParserException, IOException {
				}

				public void setFeature(String arg0, boolean arg1)
						throws XmlPullParserException {
				}

				public void setInput(Reader arg0) throws XmlPullParserException {
				}

				public void setProperty(String arg0, Object arg1)
						throws XmlPullParserException {
				}

				public void setInput(InputStream inputStream,
						String inputEncoding) throws XmlPullParserException {
				}
			}, null, false);
		} catch (InflateException e1) {
		}
	}

	private static class CustomStateListDrawable extends StateListDrawable {
		final TextView mTv;
		final Context mContext;

		public CustomStateListDrawable(Context context, TextView tv) {
			mTv = tv;
			mContext = context;

			int stateFocused = android.R.attr.state_focused;
			int statePressed = android.R.attr.state_pressed;
			int stateSelected = android.R.attr.state_selected;
			addState(new int[] { statePressed }, context.getResources()
					.getDrawable(R.drawable.red_gradient));
			addState(new int[] { stateSelected }, context.getResources()
					.getDrawable(R.drawable.red_gradient_hover));
			addState(new int[] { stateFocused }, context.getResources()
					.getDrawable(R.drawable.red_gradient_hover));
			addState(
					new int[] { -statePressed, -stateSelected, -stateFocused },
					context.getResources().getDrawable(
							R.drawable.gray_gradient_list));
			addState(
					StateSet.WILD_CARD,
					context.getResources().getDrawable(
							R.drawable.gray_gradient_list));
		}

		public boolean selectDrawable(int idx) {
			if (mTv != null) {
				// set enabled state of view based on the enabled state of the
				// MenuItem to make disabled state more apparent for ICS
				// moving this here so that it will update each time the menu is
				// displayed, in case the enabled state of the underlying item
				// has changed
				try {
					Class menuItemView = Class
							.forName("com.android.internal.view.menu.IconMenuItemView");
					Method method = menuItemView.getMethod("getItemData",
							new Class<?>[0]);
					Object object = method.invoke(mTv, new Object[0]);
					if (object instanceof MenuItem) {
						mTv.setEnabled(((MenuItem) object).isEnabled());
					}
				} catch (Exception e) {
					AfterUIlog.printStackTrace(e);
				}
				if (mTv.isEnabled()) {
					if (idx == 3) {
						mTv.setTextColor(mContext.getResources().getColor(
								R.color.eden_dark));
					} else {
						mTv.setTextColor(mContext.getResources().getColor(
								R.color.app_theme_color));
					}
				}
			}
			return super.selectDrawable(idx);
		}
	}

	public OnCreateContextMenuListener adaptContextMenuListener(
			final Context ctx, final OnCreateContextMenuListener createListenr) {

		return new OnCreateContextMenuListener() {

			public void onCreateContextMenu(final ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				createListenr.onCreateContextMenu(menu, v, menuInfo);
				if (menu.size() == 0) {
					// do not displaying context menu if it is
					// "Getting more results..." item
					return;
				}

				Handler handler = new Handler();

				List<String> items = new ArrayList<String>();

				for (int i = 0; i < menu.size(); i++) {
					items.add((String) menu.getItem(i).getTitle());
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(ctx,
						R.layout.custom_alert_dialog_custom_list_view_item,
						items);

				createListDialog(ctx, adapter, menu);
				handler.post(new Runnable() {

					public void run() {
						menu.close();
					}
				});
			}
		};
	}

	private void createListDialog(final Context ctx,
			ArrayAdapter<String> adapter, final ContextMenu menu) {
		final CustomDialog dialog = AfterYouCustomDialogTheme.newBuilder(ctx)
				.setAdapter(adapter, new OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						((Activity) ctx).onContextItemSelected(menu
								.getItem(which));
						dialog.cancel();
					}
				}).create();
		dialog.setCancelable(true);
		dialog.show();
	}


	public int getListViewDividerID() {
		return R.drawable.divider_horizontal_dim_dark;
	}

	public void setStartupAnimation(Animation a, ImageView icon, ImageView logo) {
		a.setDuration(2000);
		icon.startAnimation(a);
		logo.startAnimation(a);
	}


	public int getRadioButtonDrawable() {
		return R.drawable.radio_btn;
	}


	public int getAppLayoutBackgoundColor() {
		return Color.WHITE;
	}

}