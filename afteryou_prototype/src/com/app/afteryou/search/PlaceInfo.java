
package com.app.afteryou.search;

import android.content.Context;

import com.app.afteryou.R;
import com.app.afteryou.R.string;
import com.nbi.common.data.MapLocation;
import com.nbi.common.data.POI;
import com.nbi.common.data.Place;

//Holds each result from a search query
public class PlaceInfo {
	private POI poi;
	private Place place;
	private int pinID;

	private static final double METERSINMILE = 1609.344;

	public PlaceInfo(Place place) {
		this.place = place;
	}

	public PlaceInfo(POI poi) {
		this(poi.getPlace());
		this.poi = poi;
	}

	public void setPinID(int pinID) {
		this.pinID = pinID;
	}
	public int getPinID() {
		return pinID;
	}
	public Place getPlace() {
		return place;
	}

    String getName() {
    if (place.getName() == null || place.getName().length()==0)
        return "Address";
    else
        return place.getName();
    }
	String getAreaName() {
		return place.getLocation().getAreaName();
	}
	public String getAddress() {
		StringBuilder address = new StringBuilder(256);

		MapLocation location = place.getLocation();
		String street = location.getStreet();
		if(location.getType() == MapLocation.INTERSECTION) {
			address.append("Intersection at ");
			address.append(street);
			address.append(" and ");
			address.append(location.getCrossStreet());

		} else {
			address.append(location.getAddress());
			if(address.length() > 0) {
				address.append(' ');
			}
			address.append(street);
		}

		return address.toString();
	}
	public String getCity() {
		return place.getLocation().getCity();
	}
	String getState() {
		return place.getLocation().getState();
	}
	String getZip() {
		return place.getLocation().getPostalCode();
	}
	String getCountry() {
		return place.getLocation().getCountry();
	}
	double getLatitude() {
		return place.getLocation().getLatitude();
	}
	double getLongitude() {
		return place.getLocation().getLongitude();
	}
    public String getPhoneNumber() {
        String phoneNumber = null;
        if (place.getPhoneNumberCount() > 0)
            phoneNumber = place.getPhoneNumber(0).getArea() + place.getPhoneNumber(0).getNumber();
        return phoneNumber;
    }
	String getDistance(Context context) {
		if (poi == null) {
			return "";
		}

		double distance = poi.getDistance();
		if (distance == 0) {
			return "";
		}
		/* The distance is stored in meters and convert to miles.
		 It is better to use the system defined values (English v Metric) */
		double miles = distance / METERSINMILE;
		String formatString = context.getString(R.string.miles);
		String mileString = String.format(formatString, miles);
		return mileString;
	}
	// this returns the address in a single line format
	String getOneLineAddress() {
		String ret ="";
		if(getAddress().length() > 0)
		{
			ret += getAddress() + ", ";
		}
		if(getCity().length() > 0)
		{
			ret += getCity() + ", ";
		}
		if(getState().length() > 0)
		{
			ret += getState();
		}
		return ret;
	}
    // this returns the city, state, and zip
    public String getCityStateZip() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCity());
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(getState()).append(" ").append(getZip());
        return sb.toString();
    }
	// this returns the city and state
	String getCityState() {
        StringBuilder sb = new StringBuilder();
        sb.append(getCity());
        if (sb.length() > 0) {
            sb.append(", ");
        }
        sb.append(getState());
        return sb.toString();
	}
	boolean hasPhoneNumber() {
		return (place != null && place.getPhoneNumberCount() > 0);
	}

	// Returns place thumbnail image URL
	String getThumbImageUrl() {
		String url = null;

		if (poi != null) {
			url = poi.getThumbnailImageUrl();
		}

		return url;
	}

	// Returns place image URL
	String getImageUrl() {
		String url = null;

		if (poi != null) {
			url = poi.getImageUrl();
		}

		return url;
	}
}
