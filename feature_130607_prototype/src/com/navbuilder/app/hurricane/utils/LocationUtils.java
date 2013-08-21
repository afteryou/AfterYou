package com.navbuilder.app.hurricane.utils;

import java.text.DecimalFormat;
import java.util.Locale;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;

import com.navbuilder.app.hurricane.log.Nimlog;
import com.navbuilder.nb.data.Location;
import com.navbuilder.util.StringUtil;
import com.nbi.common.data.MapLocation;

public class LocationUtils {

	private static final String EMPTY = "";
	private static final String BLANK = " ";
	private static final String COMMASPACE = ", ";
	private static final String SPACEANDSPACE = "  ";
	private final static String COMMA = ",";
	public static final float MILE2METER = 1609.344f;
	private static Vector<int[]> addressFormat = new Vector<int[]>();
	static {
		addressFormat.add(new int[] { 2 });
		addressFormat.add(new int[] { 0, 9, 1 });
		addressFormat.add(new int[] { 3, 8, 5, 9, 6, 8, 7 });
	}

	/**
	 * Formats a distance as a string with optional units and rounding.
	 * 
	 * @param dist
	 *            the distance.
	 * @param showUnits
	 *            indicates whether to include units.
	 * @param decLimit
	 *            formats the distance as whole numbers if dist is above this
	 *            limit.
	 * @param round
	 *            indicates whether to round the distance.
	 * @param metric
	 *            indicates whether to use metric units.
	 * @param shortUnitNames
	 *            if true then units are short ("m", "ft", etc.)
	 * @return the distance as a string.
	 */
	public static String formatDistance(double dist, boolean showUnits, double decLimit,
			boolean round, boolean metric, boolean shortUnitNames) {
		StringBuffer buffer = new StringBuffer();

		boolean smallUnits = false;
		double tempDist = dist;

		if (metric) {
			dist = tempDist / 1000.0; // Meters to KM
		} else {
			dist = tempDist / MILE2METER; // Meters to Miles
		}
		String result = null;
		/**
		 * 1 feet = 0.000189393939 mile 950 feet = 0.17992 mile
		 */
		if ((dist < 0.17992 && showUnits && !metric)/* mi */
				|| (dist < 0.995 && showUnits && metric)/* km->m */) {
			smallUnits = true;

			if (metric) {
				dist = tempDist;

				if (round) {
					dist = 10 * (int) ((dist + 5) / 10);
				}
			} else {
				dist = tempDist * 3.2808; // feet

				if (round) {
					dist = 50 * (int) ((dist + 25) / 50);
				}
			}
			result = String.valueOf((int) dist);
		} else {

			if (dist < decLimit) {
				result = formatFloat(dist, 1);
			} else {
				result = String.valueOf((int) dist);
			}
		}
		buffer.append(result);

		StringBuffer sb = new StringBuffer(BLANK);
		if (showUnits) {
			/* if (shortUnitNames) { */
			String unit = metric ? (smallUnits ? sb.append("m").toString() : sb.append("km").toString())
					: (smallUnits ? sb.append("ft").toString() : sb.append("mi").toString());

			buffer.append(unit);

		}
		return buffer.toString();
	}

	private static String formatFloat(double value, int precision) {
		// fix bug 88623, 88727, 88638, make sure the result has the precision
		DecimalFormat df = new DecimalFormat();
		df.setMaximumFractionDigits(precision);
		df.setMinimumFractionDigits(precision);
		return df.format(value);
	}

	public static String formatLocation(MapLocation mapLocation, boolean isSingleLine) {
		// String countryCode = "USA";
		if (mapLocation == null) {
			return EMPTY;
		}
		if (addressFormat == null) {
			return parseDefaultAddressFromLocation(mapLocation, null);
		}
		StringBuilder sbRet = new StringBuilder();
		StringBuilder sbLine;
		int lineSize = addressFormat.size();
		for (int i = 0; i < lineSize; i++) {
			int[] lineFormats = addressFormat.get(i);
			sbLine = new StringBuilder();
			for (int j = 0; j < lineFormats.length; j++) {
				if (mapLocation.getCrossStreet() != null && mapLocation.getCrossStreet().length() > 0
						&& lineFormats[j] == STREET_NUMBER) {
					continue;
				}
				String str = getAddressSeg(mapLocation, lineFormats[j]);
				if (!(COMMA.equals(str.trim()) && EMPTY.equals(sbLine.toString().trim()))) {
					sbLine.append(str);
				}
			}
			if (!sbLine.toString().trim().equals(COMMA)) {
				if (sbLine.toString().trim().length() > 0) {
					if (i != (lineSize - 1)) {
						if (isSingleLine) {
							sbLine.append(COMMASPACE);
						} else {
							sbLine.append("\n");
						}
					}
					sbRet.append(sbLine);
				}
			}
		}
		String str = sbRet.toString().trim();
		if (str.startsWith(COMMA)) {
			str = str.substring(1);
		}
		while (str.endsWith(COMMA)) {
			str = str.substring(0, str.length() - 1).trim();
		}

		str = str.replace("\n\n", "\n");

		return str;
	}

	private final static int STREET_NUMBER = 0;
	private final static int STREET_NAME = 1;
	private final static int AREANAME = 2;
	private final static int CITY = 3;
	private final static int COUNTY = 4;
	private final static int STATE = 5;
	private final static int POSTAL = 6;
	private final static int COUNTRY = 7;
	private final static int COMMA_INT = 8;
	private final static int SPACE = 9;

	public static String getAddressSeg(MapLocation mapLocation, int lineFormat) {
		StringBuilder sbLine = new StringBuilder();
		switch (lineFormat) {
		case STREET_NUMBER:
			sbLine.append(mapLocation.getAddress());
			break;
		case STREET_NAME:
			sbLine.append(mapLocation.getStreet());
			if (mapLocation.getCrossStreet() != null && mapLocation.getCrossStreet().length() > 0) {
				sbLine.append(SPACEANDSPACE);
				sbLine.append(mapLocation.getCrossStreet());
				// sbLine.append(", ");
			}
			break;
		case AREANAME:
			sbLine.append(mapLocation.getAreaName());
			break;
		case CITY:
			sbLine.append(mapLocation.getCity());
			break;
		case COUNTY:
			sbLine.append(mapLocation.getCounty());
			break;
		case STATE:
			sbLine.append(mapLocation.getState());
			break;
		case POSTAL:
			sbLine.append(mapLocation.getPostalCode());
			break;
		case COUNTRY:

			break;
		case COMMA_INT:
			sbLine.append(COMMASPACE);
			break;
		case SPACE:
			sbLine.append(BLANK);
			break;
		default:
			Nimlog.e("Utilities", "formatLocation(String countryCode, Location location) wrong format: ");
		}
		return sbLine.toString();
	}

	private static String parseDefaultAddressFromLocation(MapLocation mapLocation, Locale locale) {

		if (locale == null) {
			locale = Locale.US;
		}
		if (mapLocation == null) {
			return EMPTY;
		}
		if (mapLocation.getType() == Location.AIRPORT) {
			return parseAirportFromLocation(mapLocation);
		}
		StringBuffer strRet = new StringBuffer();
		strRet.append(mapLocation.getAddress() == null ? EMPTY : mapLocation.getAddress());
		strRet.append(BLANK).append(mapLocation.getStreet() == null ? EMPTY : mapLocation.getStreet());

		strRet.append(mapLocation.getCrossStreet() != null && mapLocation.getCrossStreet().trim().length() > 0 ? SPACEANDSPACE
				+ mapLocation.getCrossStreet()
				: EMPTY);

		strRet.append("\n");
		strRet.append(mapLocation.getCity() == null ? EMPTY : mapLocation.getCity());
		strRet.append(COMMASPACE).append(mapLocation.getState() == null ? EMPTY : mapLocation.getState());
		strRet.append(BLANK).append(mapLocation.getPostalCode() == null ? EMPTY : mapLocation.getPostalCode());

		Pattern pattern = Pattern.compile("[^\n,]{1}.+\n?.+");
		Matcher matcher = pattern.matcher(strRet.toString().trim());
		if (matcher.find()) {
			return matcher.group();
		}
		return mapLocation.getAreaName();
	}

	private static String parseAirportFromLocation(MapLocation mapLocation) {
		StringBuilder strRet = new StringBuilder();
		String strAeraName = mapLocation.getAreaName();
		String StrAirport = mapLocation.getAirport();
		String strCity = mapLocation.getCity();
		String strState = mapLocation.getState();
		String strCountry = mapLocation.getCountry();

		if (strAeraName != null) {
			strRet.append(strAeraName);
		}
		if (StrAirport != null && StrAirport.length() > 0 && !StringUtil.stringEmpty(strAeraName)) {
			strRet.append(" (").append(StrAirport).append(")").append("\n");
		}
		if (strCity != null && strCity.length() > 0) {
			strRet.append(strCity);
		}
		if (strState != null && strState.length() > 0) {
			strRet.append(COMMASPACE).append(strState);
		}
		if (strCountry != null && strCountry.length() > 0) {
			strRet.append(COMMASPACE).append(strCountry);
		}
		return strRet.toString();
	}
}
