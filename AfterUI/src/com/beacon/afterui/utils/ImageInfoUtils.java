package com.beacon.afterui.utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

public class ImageInfoUtils {

	private static final String GIF = "GIF";
	private static final String PNG = "PNG";
	private static final String JPG = "JPG";
	private static final String BMP = "BMP";

	private static final int HEADER_SIZE = 512;

	public static ImageInfo getImageInfo(byte[] header) {
		ImageInfo model = null;
		String picextendname;
		picextendname = null;
		byte[] content = header;
		try {
			int k;
			k = content.length;
			Integer kk;
			kk = null;
			String picsize;
			picsize = null;
			if (k >= 1024) {
				// bigger than fact pic file sizes
				k = k / 1024 + 1;
				kk = Integer.valueOf(k);
				picsize = kk.toString() + "K";
			} else if (k > 0) {
				kk = Integer.valueOf(k);
				picsize = kk.toString();
			}
			model = new ImageInfo();
			model.setHeader(header);
			model.setSize(picsize);
		} catch (Exception e) {
			content = new byte[0];
			e.printStackTrace();
		}
		picextendname = getFileExtendName(content);
		int picwidth, picheight, color;
		String piccolor;
		picwidth = 0;
		picheight = 0;
		color = 0;
		piccolor = null;
		if (picextendname.equals(GIF)) {
			// picwidth position
			picwidth = getFileAttribute(content, 7, 2, picextendname);
			// picheight position
			picheight = getFileAttribute(content, 9, 2, picextendname);
			// piccolor position
			color = getFileAttribute(content, 10, 1, picextendname);
			color = color % 8 + 1;
			piccolor = getPicColor(color);
		}
		if (picextendname.equals(JPG)) {
			picwidth = getFileAttribute(content, 166, 2, picextendname);
			picheight = getFileAttribute(content, 164, 2, picextendname);
			color = getFileAttribute(content, 167, 1, picextendname);
			color = color * 8;
			if ((picwidth == 0) || (picheight == 0)) {
				picwidth = getFileAttribute(content, 197, 2, picextendname);
				picheight = getFileAttribute(content, 195, 2, picextendname);
				color = getFileAttribute(content, 198, 1, picextendname);
				color = color * 8;
			}
			piccolor = getPicColor(color);
		}
		if (picextendname.equals(BMP)) {
			picwidth = getFileAttribute(content, 19, 2, picextendname);
			picheight = getFileAttribute(content, 23, 2, picextendname);
			color = getFileAttribute(content, 28, 1, picextendname);
			piccolor = getPicColor(color);
		}
		if (picextendname.equals(PNG)) {
			picwidth = getFileAttribute(content, 19, 2, picextendname);
			picheight = getFileAttribute(content, 23, 2, picextendname);
			// usually is "16M"??
			piccolor = "16M";
		}
		model.setExtension(picextendname);
		model.setWidth(picwidth);
		model.setHeight(picheight);
		model.setColor(piccolor);
		return model;
	}

	public static ImageInfo getImageInfo(String picpath) {

		try {
			return getImageInfo(readFromFile(picpath));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public static ImageInfo getImageInfofromUrl(String urlString) {
		HttpURLConnection urlConnection = null;
		InputStream in = null;
		try {
			final URL url = new URL(urlString);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = urlConnection.getInputStream();
			byte[] buf = new byte[HEADER_SIZE];
			in.read(buf);
			return getImageInfo(buf);
		} catch (final IOException e) {
			Log.e("ImageInfoUtils", "Error in getImageInfo - " + e);
		} finally {
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
			if (in != null) {
				try {
					in.close();
				} catch (final IOException e) {
					Log.e("ImageInfoUtils", "Error in getImageInfo - " + e);
				}
			}
		}
		return null;
	}

	private static byte[] readFromFile(String fileName) throws IOException {
		FileInputStream fin = null;
		try {
			fin = new FileInputStream(fileName);
			byte[] buf = new byte[HEADER_SIZE];
			fin.read(buf);
			fin.close();
			return buf;
		} finally {
			fin.close();
		}
	}

	private static String getFileExtendName(byte[] byte1) {
		String strFileExtendName;
		strFileExtendName = null;
		// header bytes contains GIF87a or GIF89a?
		if ((byte1[0] == 71) && (byte1[1] == 73) && (byte1[2] == 70)
				&& (byte1[3] == 56) && ((byte1[4] == 55) || (byte1[4] == 57))
				&& (byte1[5] == 97)) {
			strFileExtendName = GIF;
		}
		// header bytes contains JFIF?
		if ((byte1[6] == 74) && (byte1[7] == 70) && (byte1[8] == 73)
				&& (byte1[9] == 70)) {
			strFileExtendName = JPG;
		}
		// header bytes contains BM?
		if ((byte1[0] == 66) && (byte1[1] == 77)) {
			strFileExtendName = BMP;
		}
		// header bytes contains PNG?
		if ((byte1[1] == 80) && (byte1[2] == 78) && (byte1[3] == 71)) {
			strFileExtendName = PNG;
		}
		return strFileExtendName;
	}

	private static int getFileAttribute(byte[] byte2, int n, int m,
			String fileextendname) {
		int j, FileAttributeValue;
		j = 0;
		FileAttributeValue = 0;
		String str, str1;
		str = "";
		str1 = "";
		for (int k = 0; k < m; k++) {
			if (byte2[n - k] < 0) {
				j = byte2[n - k];
				j = j + 256;
			} else {
				j = byte2[n - k];
			}
			str1 = Integer.toHexString(j);
			if (str1.length() < 2) {
				str1 = "0" + str1;
			}
			if (fileextendname.equalsIgnoreCase("JPG")
					|| fileextendname.equalsIgnoreCase("PNG")) {
				str = str1 + str;
			} else {
				str = str + str1;
			}
		}
		FileAttributeValue = HexToDec(str);
		return FileAttributeValue;
	}

	private static int HexToDec(String cadhex) {
		int n, i, j, k, decimal;
		String CADHEX1;
		n = 0;
		i = 0;
		j = 0;
		k = 0;
		decimal = 0;
		CADHEX1 = null;
		n = cadhex.length();
		CADHEX1 = cadhex.trim().toUpperCase();
		while (i < n) {
			j = CADHEX1.charAt(i);
			if ((j >= 48) && (j < 65)) {
				j = j - 48;
			}
			if (j >= 65) {
				j = j - 55;
			}
			i = i + 1;
			k = 1;
			for (int m = 0; m < (n - i); m++) {
				k = 16 * k;
			}
			decimal = j * k + decimal;
		}
		return decimal;
	}

	private static String getPicColor(int color) {
		int k;
		k = 1;
		String piccolor;
		piccolor = null;
		for (int m = 0; m < color; m++) {
			k = 2 * k;
		}
		Integer kk;
		kk = null;
		if (k >= 1048576) {
			k = k / 1048576;
			kk = Integer.valueOf(k);
			piccolor = kk.toString() + "M";
		} else if (k >= 1024) {
			k = k / 1024;
			kk = Integer.valueOf(k);
			piccolor = kk.toString() + "K";
		} else if (k > 0) {
			kk = Integer.valueOf(k);
			piccolor = kk.toString();
		}
		return piccolor;
	}

	public static void main(String[] args) {
		// ImageInfo model = getImageInfo("d:/test.png");
		ImageInfo model = getImageInfofromUrl("http://www.gayot.com/images/hotels/reviews/LAHOT021490.jpg");
		System.out.println("picextendname is:" + model.getExtension());
		System.out.println("picwidth is:" + model.getWidth());
		System.out.println("picheight is:" + model.getHeight());
		System.out.println("piccolor:" + model.getColor());
		System.out.println("picsize:" + model.getSize());
	}

	public static class ImageInfo {

		private String extension = null;
		private int width = 0;
		private int height = 0;
		private String color = null;
		private String size = null;
		private byte[] header;

		public byte[] getHeader() {
			return header;
		}

		public void setHeader(byte[] header) {
			this.header = header;
		}

		public ImageInfo() {
		}

		public String getExtension() {
			return extension;
		}

		public void setExtension(String extension) {
			this.extension = extension;
		}

		public int getWidth() {
			return width;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public int getHeight() {
			return height;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public String getColor() {
			return color;
		}

		public void setColor(String color) {
			this.color = color;
		}

		public String getSize() {
			return size;
		}

		public void setSize(String size) {
			this.size = size;
		}

	}

	public static List<Album> getAlbumList(Context context) {

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

		Cursor cursor = context.getContentResolver().query(uri, projection,
				null, null, null);

		ArrayList<String> ids = new ArrayList<String>();
		ArrayList<Album> mAlbumsList = new ArrayList<Album>();
		mAlbumsList.clear();
		if (cursor != null && cursor.moveToFirst()) {
			do {
				Album album = new Album();

				int columnIndex = cursor
						.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
				album.id = cursor.getString(columnIndex);

				if (!ids.contains(album.id)) {
					columnIndex = cursor
							.getColumnIndex(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
					album.name = cursor.getString(columnIndex);

					columnIndex = cursor
							.getColumnIndex(MediaStore.Images.Media._ID);
					album.coverID = cursor.getLong(columnIndex);

					album.thumb = MediaStore.Images.Thumbnails.getThumbnail(
							context.getContentResolver(), album.coverID,
							MediaStore.Images.Thumbnails.MICRO_KIND, null);

					mAlbumsList.add(album);
					ids.add(album.id);
					mAlbumsList.get(ids.indexOf(album.id)).count++;
				} else {
					mAlbumsList.get(ids.indexOf(album.id)).count++;
				}
			}while (cursor.moveToNext());
			cursor.close();

		}
		return mAlbumsList;
	}
	
	public static List<Photo> getPhotoList( final Context context, final String bucketId ){
		List<Photo> photoList = new ArrayList<Photo>();
		
		ContentResolver resolver = context.getContentResolver();
		
		String where = MediaStore.Images.Media.BUCKET_ID + "=?";
		String whereArgs[] = {bucketId};
		
		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.Media._ID,
				MediaStore.Images.Media.BUCKET_ID,
				MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

		Cursor cursor = resolver.query(uri, projection,
				where, whereArgs, null);
		
		if( cursor != null && cursor.moveToFirst() ){
			do {
				Photo photo = new Photo();
				int columnIndex = cursor
						.getColumnIndex(MediaStore.Images.Media.BUCKET_ID);
				photo.id = cursor.getString(columnIndex); 

				columnIndex = cursor
						.getColumnIndex(MediaStore.Images.Media._ID);
				photo.coverId = cursor.getLong(columnIndex);

				photo.thumb = MediaStore.Images.Thumbnails.getThumbnail(
						resolver, photo.coverId,
						MediaStore.Images.Thumbnails.MICRO_KIND, null);

				photoList.add(photo);
				
			}while(cursor.moveToNext());
		}
		
		return photoList;
	}

	public static String getPhotoPath(final Context context, final String id) {
		List<Photo> photoList = new ArrayList<Photo>();
		ContentResolver resolver = context.getContentResolver();
		Log.d("ImageInfoUtils :", "ID : " + id);

		String where = MediaStore.Images.Media._ID + "=?";
		String whereArgs[] = { id };

		Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
		String[] projection = { MediaStore.Images.Media.DATA };

		Cursor cursor = resolver.query(uri, projection, where, whereArgs, null);

		Log.d("ImageInfoUtils :", cursor.toString());
		Log.d("cursor.getCount()) :", cursor.getCount() + "");
		String path = null;
		if (cursor != null && cursor.moveToFirst()) {
			do {
				int columnIndex = cursor
						.getColumnIndex(MediaStore.Images.Media.DATA);
				path = cursor.getString(columnIndex);
				Log.d("ImageInfoUtils :", "Path :" + path);

			} while (cursor.moveToNext());
		}

		return path;
	}
}