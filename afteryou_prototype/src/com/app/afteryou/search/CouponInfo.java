
package com.app.afteryou.search;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;

import android.app.Activity;

import com.nbi.common.NBIContext;
import com.nbi.coupons.data.Coupon;
import com.nbi.coupons.data.DiscountType;
import com.nbi.coupons.data.StoreCoupons;

//Presents coupon info 
public class CouponInfo extends PlaceInfo {
	private StoreCoupons storeCoupons;

	public CouponInfo(StoreCoupons storeCoupons) {
		super(storeCoupons.getStore().getRetailerPlace());
		this.storeCoupons = storeCoupons;
	}

	public CouponInfo(byte[] data) throws IOException {
		this(new StoreCoupons(data));
	}

	//Returns coupon name
	public String getName() {
		return storeCoupons.getCoupon(0).getTitle();
	}

	//Returns coupon description
	public String getDescription() {
		return storeCoupons.getCoupon(0).getDescription();
	}

	//Returns retailer name
	public String getRetailerName() {
		return storeCoupons.getStore().getRetailerName();
	}

	//Returns coupon expiration date
	public String getExpirationDate() {
		Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT-00:00"));
		calendar.setTime(storeCoupons.getCoupon(0).getExpirationDate());
		StringBuffer date = new StringBuffer();
		date.append(calendar.get(Calendar.MONTH) + 1)
			.append('/')
			.append(calendar.get(Calendar.DAY_OF_MONTH))
			.append('/')
			.append(calendar.get(Calendar.YEAR));

		return date.toString();
	}

	//Checks whether coupon is expired
	boolean hasExpired() {
		long expTime = storeCoupons.getCoupon(0).getExpirationDate().getTime();
		long current = Calendar.getInstance().getTime().getTime();
		return expTime < current;
	}

	//Returns coupon ID
	public String getCouponId() {
		return storeCoupons.getCoupon(0).getCouponId();
	}

	//Returns coupon discount 
    public String getDiscount() {
        StringBuffer discount = new StringBuffer();
        Coupon coupon = storeCoupons.getCoupon(0);
        int type = coupon.getDiscountType();
        switch (type) {
        case DiscountType.DISCOUNT_TYPE_PERCENTAGE_OFF:
            discount.append(Double.toString(coupon.getDiscountValue()));
            discount.append("%\noff!");
            break;
        case DiscountType.DISCOUNT_TYPE_AMOUNT_OFF:
            double buy_value = coupon.getBuyValue();
            double list_value = coupon.getListValue();            
            int percent_off = (int)(((list_value - buy_value)/list_value)*100);
            discount.append(Integer.toString(percent_off));
            discount.append("%\noff!");            
            break;
        }
        return discount.toString();
    }

    //Returns coupon value
    public String getValue() {
        StringBuffer value = new StringBuffer();
        Coupon coupon = storeCoupons.getCoupon(0);
        value.append("$");
        value.append(Double.toString(coupon.getListValue()));
        return value.toString();
    }

    //Returns coupon thumbnail image URL
	public String getThumbImageUrl() {
		String url = storeCoupons.getCoupon(0).getImageUrls().getLargeThumbUrl();
		if (url != null && url.equals(Coupon.UNDEFINED_IMAGE_ID)) {
			url = null;
		}
		return url;
	}

	//Returns coupon image URL
	public String getImageUrl() {
		String url = storeCoupons.getCoupon(0).getImageUrls().getImageUrl();
		if (url != null && url.equals(Coupon.UNDEFINED_IMAGE_ID)) {
			url = null;
		}
		return url;
	}

	//Check whether coupon has place info
	public boolean hasPlace() {
		return (getPlace() != null && getAddress() != null && getAddress().length() > 0);
	}

	public void launchDealURL(NBIContext nbiContext, boolean built_in, Activity ac) {
		storeCoupons.getCoupon(0).launchDealUrl(nbiContext, built_in, ac);
	}

	public byte[] toByteArray() {
		return storeCoupons.toByteArray();
	}

	//Returns coupon store info
	public StoreCoupons getStoreCoupons() {
		return storeCoupons;
	}
}
