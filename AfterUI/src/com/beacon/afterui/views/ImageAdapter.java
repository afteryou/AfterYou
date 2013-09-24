package com.beacon.afterui.views;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.beacon.afterui.utils.Photo;

public class ImageAdapter extends BaseAdapter {

    private Context mContext;

    private List<Photo> mPhotoList;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public void setPhotoList(List<Photo> photoList) {
        mPhotoList = photoList;
    }

    @Override
    public int getCount() {
        return mPhotoList == null ? 0 : mPhotoList.size();
    }

    @Override
    public Object getItem(int position) {
        return mPhotoList.get(position);
    }

    @Override
    public long getItemId(int id) {
        return id;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ImageView photoImage = null;
        if (convertView == null) {
            photoImage = new ImageView(mContext);
        } else {
            photoImage = (ImageView) convertView;
        }

        Photo photo = (Photo) getItem(position);
        photoImage.setImageBitmap(photo.thumb);
        photoImage.setScaleType(ImageView.ScaleType.CENTER);
        return photoImage;
    }

}
