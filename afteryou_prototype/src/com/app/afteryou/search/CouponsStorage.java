
package com.app.afteryou.search;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import com.nbi.coupons.data.StoreCoupons;

//Represents coupon storage
public class CouponsStorage {

	//Listener interface to handle coupon add and delete actions 
	public static interface OnCouponsStorageChangedListener {
		public void onCouponAdded(CouponInfo coupon, int position);
		public void onCouponDeleted(int position);
	}

	// IO Error constants
	public static final int NO_ERROR = 0;
	public static final int NOT_ENOUGH_MEMORY = -1;
	public static final int OTHER_IO_ERROR = -2;

	//Maximum coupon storage size 
	private static final int MAX_STORAGE_SIZE = 50;
	//Storage filename
	private static final String STORAGE_FILENAME = "SavedCoupons";

	//Represents coupon item to store in cache
	private static class CouponCacheItem {
		public String couponId;
		public int couponSize;

		public CouponCacheItem() {
			couponId = "";
			couponSize = 0;
		}

		public CouponCacheItem(StoreCoupons storeCoupons, int couponSize) {
			this.couponId = storeCoupons.getCoupon(0).getCouponId();
			this.couponSize = couponSize;
		}

		//Checks whether cached coupon is matched to defined stored coupon
		public boolean matches(StoreCoupons storeCoupons) {
			return (couponId.equals(storeCoupons.getCoupon(0).getCouponId()));
		}
	}

	private static CouponsStorage self = null;

	private ArrayList<CouponCacheItem> couponsCache = new ArrayList<CouponCacheItem>();
	private Context appContext;
	private OnCouponsStorageChangedListener storageListener = null;

	// Must be called once at application startup
	public static void init(Context context) {
		self = new CouponsStorage(context);
	}

	public static void release() {
		self = null;
	}

	static CouponsStorage getInstance() {
		return self;
	}

	private CouponsStorage(Context context) {
		appContext = context;

		DataInputStream inputStream = null;
		try {
			// Try to open storage file for reading
			inputStream = new DataInputStream(new BufferedInputStream(appContext.openFileInput(STORAGE_FILENAME)));
			// Read coupons cache from storage
			CouponCacheItem cacheItem;
			while ((cacheItem = readCouponCacheItemFromStream(inputStream)) != null) {
				couponsCache.add(cacheItem);
			}
		} catch (FileNotFoundException e) {
			// Storage file does not exist, create it and leave the coupons cache empty
			FileOutputStream outputStream = null;
			try {
				outputStream = appContext.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE);
			} catch (FileNotFoundException ex) {
				// This should never happens
			} finally {
				try {
					if (outputStream != null)
						outputStream.close();
				} catch (IOException ex) {}
			}
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException ex) {}
		}
	}

	public void setOnCouponsStorageChangedListener(OnCouponsStorageChangedListener listener) {
		storageListener = listener;
	}

	// Returns number of coupons in storage
	public int numCoupons() {
		return couponsCache.size();
	}

	// This method is called by SavedCouponsListView to store only coupon names
	// to decrease memory usage
	public ArrayList<String> loadCouponNames() {
		ArrayList<String> couponNames = new ArrayList<String>();
		DataInputStream inputStream = null;
		try {
			inputStream = new DataInputStream(new BufferedInputStream(appContext.openFileInput(STORAGE_FILENAME)));
			StoreCoupons coupon;
			int couponCount = numCoupons();
			for (int index = 0; index < couponCount; index++) {
				if ((coupon = readCouponFromStream(inputStream)) != null) {
					couponNames.add(coupon.getCoupon(0).getTitle());
				}
			}
		} catch (IOException e) {
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (IOException e) {}
		}

		return couponNames;
	}

	// Loads coupon from storage at specified position. Returns null if requested position
	// does not exist or in case of any IO exception
	public CouponInfo loadCoupon(int position) {
		StoreCoupons coupon = null;
		if (position >= 0 && position < numCoupons()) {
			DataInputStream inputStream = null;
			try {
				inputStream = new DataInputStream(new BufferedInputStream(appContext.openFileInput(STORAGE_FILENAME)));
				// switch input stream cursor to coupon data at requested position
				inputStream.skipBytes(getCouponStorageOffset(position));
				coupon = readCouponFromStream(inputStream);
			} catch (IOException e) {
			} finally {
				try {
					if (inputStream != null)
						inputStream.close();
				} catch (IOException e) {}
			}
		}

		return ((coupon != null) ? new CouponInfo(coupon) : null);
	}

	// Saves coupon to storage. If coupon is already presented in storage,
	// then this method just updates its position
	public int saveCoupon(CouponInfo couponInfo) {
		StoreCoupons coupon = couponInfo.getStoreCoupons();
		// Firstly, delete coupon from storage at old position if it exists
		int error = deleteCoupon(findCouponPosition(coupon));
		if (error == NO_ERROR) {
			// Then, append coupon to the end of storage file
			error = appendCoupon(coupon);
		}
		return error;
	}

	// Deletes coupon at specified position from storage
	public int deleteCoupon(int position) {
		int error = NO_ERROR;
		if (position >= 0 && position < numCoupons()) {
			int beforeSize = getCouponStorageOffset(position);
			int afterSize = getCouponStorageOffset(numCoupons()) - getCouponStorageSize(position) - beforeSize;
			FileInputStream inputStream = null;
			DataOutputStream outputStream = null;
			try {
				// Firstly, read "before" and "after" data from storage
				inputStream = appContext.openFileInput(STORAGE_FILENAME);
				byte[] beforeData = null;
				byte[] afterData = null;
				if (beforeSize > 0) {
					beforeData = new byte[beforeSize];
					inputStream.read(beforeData);
				}
				if (afterSize > 0) {
					afterData = new byte[afterSize];
					inputStream.skip(getCouponStorageSize(position));
					inputStream.read(afterData);
				}
				inputStream.close();
				inputStream = null;

				outputStream = new DataOutputStream(appContext.openFileOutput(STORAGE_FILENAME, Context.MODE_PRIVATE));
				// Write "before" data if it exists
				if (beforeData != null)
					outputStream.write(beforeData);
				// Write "after" data if it exists
				if (afterData != null)
					outputStream.write(afterData);
				outputStream.close();
				outputStream = null;

				// Remove coupon from cache
				couponsCache.remove(position);
				// Notify storage listener
				if (storageListener != null)
					storageListener.onCouponDeleted(position);
			} catch (IOException e) {
				error = OTHER_IO_ERROR;
				try {
					if (inputStream != null)
						inputStream.close();
					if (outputStream != null)
						outputStream.close();
				} catch (IOException ex) {}
			}
		}
		return error;
	}

	// Deletes coupon from storage if it exists
	public int deleteCoupon(CouponInfo coupon) {
		return deleteCoupon(findCouponPosition(coupon.getStoreCoupons()));
	}

	//Reads coupon cache item object from data input stream
	private CouponCacheItem readCouponCacheItemFromStream(DataInputStream stream) {
		CouponCacheItem cacheItem;
		try {
			// Create coupon cache item
			cacheItem = new CouponCacheItem();
			// Read coupon size
			cacheItem.couponSize = stream.readInt();
			byte[] byteArray = new byte[cacheItem.couponSize];
			// Read coupon data
			stream.read(byteArray);
			StoreCoupons storeCoupons = new StoreCoupons(byteArray);
			// Read coupon unique id
			cacheItem.couponId = storeCoupons.getCoupon(0).getCouponId();
		} catch (IOException e) {
			cacheItem = null;
		}

		return cacheItem;
	}

	//Reads coupon object from data input stream
	private StoreCoupons readCouponFromStream(DataInputStream stream) {
		StoreCoupons coupon = null;
		try {
			// Read coupon size
			int couponSize = stream.readInt();
			byte[] byteArray = new byte[couponSize];
			// Read coupon data
			stream.read(byteArray);
			// Build coupon from data
			coupon = new StoreCoupons(byteArray);
		} catch (IOException e) {}

		return coupon;
	}

	//Returns coupon storage size
	private int getCouponStorageSize(int position) {
		return (/* coupon size int (4 bytes) */4 + couponsCache.get(position).couponSize);
	}

	//Returns coupons storage offset position to load/delete operations
	private int getCouponStorageOffset(int position) {
		int offset = 0;
		for (int index = 0; index < position; index++) {
			offset += getCouponStorageSize(index);
		}
		return offset;
	}

	private int findCouponPosition(StoreCoupons coupon) {
		int couponCount = numCoupons();
		for (int position = 0; position < couponCount; position++) {
			if (couponsCache.get(position).matches(coupon)) {
				return position;
			}
		}
		return (-1); // not found
	}

	//Checks whether coupon storage has free space
	private static boolean hasFreeSpace(int size) {
		StatFs stat = new StatFs(Environment.getDataDirectory().getPath());
		return (((size - 1) / stat.getBlockSize()) + 1) <= stat.getAvailableBlocks();
	}

	//Appends coupon to the end of storage file 
	private int appendCoupon(StoreCoupons coupon) {
		// Delete the oldest coupon from storage if number of coupons exceeds storage limit
		if (numCoupons() == MAX_STORAGE_SIZE) {
			if (deleteCoupon(0) != NO_ERROR)
				return OTHER_IO_ERROR;
		}
		int error = NO_ERROR;
		byte[] byteArray = coupon.toByteArray();
		//Check conversion was successful
		if(byteArray==null){
			error=OTHER_IO_ERROR;
		}
		// Check for free space
		if (!hasFreeSpace(byteArray.length + 4)) {
			error = NOT_ENOUGH_MEMORY;
		} else {
			// Now we can safely append coupon to the end of storage file
			DataOutputStream outputStream = null;
			try {
				outputStream = new DataOutputStream(appContext.openFileOutput(STORAGE_FILENAME, Context.MODE_APPEND));
				// Write coupon size
				outputStream.writeInt(byteArray.length);
				// Write coupon data
				outputStream.write(byteArray);
				// Append coupon to cache
				couponsCache.add(new CouponCacheItem(coupon, byteArray.length));
				// Notify storage listener
				if (storageListener != null)
					storageListener.onCouponAdded(new CouponInfo(coupon), numCoupons() - 1);
			} catch (IOException e) {
				error = OTHER_IO_ERROR;
			} finally {
				try {
					if (outputStream != null)
						outputStream.close();
				} catch (IOException e) {}
			}
		}
		return error;
	}
}
