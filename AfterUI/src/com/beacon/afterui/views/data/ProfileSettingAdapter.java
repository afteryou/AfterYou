package com.beacon.afterui.views.data;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.beacon.afterui.R;
import com.beacon.afterui.sliding.fragment.ProfileSettingSideBar.ProfileSettingModal;
import com.beacon.afterui.utils.FontUtils;

public class ProfileSettingAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private Typeface ITCAvantGardeStdBkFont;
    private List<ProfileSettingModal> mList;
    private static final String PASSWORD = "password";

    public ProfileSettingAdapter(Context context, List<ProfileSettingModal> list) {

        mContext = context;
        mList = list;
        mLayoutInflater = LayoutInflater.from(mContext);
        ITCAvantGardeStdBkFont = FontUtils.loadTypeFace(mContext,
                FontUtils.ITC_AVANT_GARDE_STD_BK);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = mLayoutInflater.inflate(
                    R.layout.profile_setting_sidebar_item, null);

            holder.profile_label = (TextView) view.findViewById(R.id.head_txt);
            holder.profile_label.setTypeface(ITCAvantGardeStdBkFont);

            holder.sub_txt = (TextView) view.findViewById(R.id.sub_txt);
            holder.sub_txt.setTypeface(ITCAvantGardeStdBkFont);

            holder.direction_img = (ImageView) view
                    .findViewById(R.id.direction_img);
            holder.star_img = (ImageView) view.findViewById(R.id.star_img);

            view.setTag(holder);

        } else {

            holder = (ViewHolder) view.getTag();

        }
        ProfileSettingModal modal = (ProfileSettingModal) getItem(position);
        holder.profile_label.setText(modal.label);
        holder.sub_txt.setText(modal.info);
        if (!modal.isRequired) {
            holder.star_img.setVisibility(View.GONE);
            holder.direction_img.setImageResource(R.drawable.cross_btn);
        } else {
            holder.star_img.setVisibility(View.VISIBLE);
            holder.direction_img
                    .setImageResource(R.drawable.right_direction_img);
        }
        return view;
    }

    private class ViewHolder {

        private TextView profile_label;
        private TextView sub_txt;
        private ImageView direction_img;
        private ImageView star_img;

    }

}
