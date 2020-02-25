package com.skt.alerts;

import java.util.Calendar;
import java.util.Date;

public class CommonUtil {

	public String getSimpleAddress(String location) {
		// TODO Auto-generated method stub
		String simpleAddress = "";
		String[] addressArray = location.split(",");
		for (int l = 0; l < 3; l++) {
			if (addressArray[l] != null)
				simpleAddress += addressArray[l] + ", ";
		}
		simpleAddress = simpleAddress.substring(0, simpleAddress.length() - 2);
		return simpleAddress;
	}

	public static boolean isSameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(date1);
		Calendar cal2 = Calendar.getInstance();
		cal2.setTime(date2);
		return isSameDay(cal1, cal2);
	}

	public static boolean isSameDay(Calendar cal1, Calendar cal2) {
		if (cal1 == null || cal2 == null) {
			throw new IllegalArgumentException("The dates must not be null");
		}
		return (cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
				&& cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) && cal1
					.get(Calendar.DAY_OF_YEAR) == cal2
				.get(Calendar.DAY_OF_YEAR));
	}

	public static long getTimeValueFromDate(Date date) {
		long MILLIS_PER_DAY = 24 * 60 * 60 * 1000;
		long studentLastEventTime = date.getTime() % MILLIS_PER_DAY;
		return studentLastEventTime;
	}

	public static Date timeWithGrace(Date tripStart, int mins) {
		long ONE_MINUTE_IN_MILLIS = 60000;// millisecs
		long t = tripStart.getTime();
		Date dateAfterGrace = new Date(t + (mins * ONE_MINUTE_IN_MILLIS));
		return dateAfterGrace;
	}

	public static boolean isTimeInRange(Date timeStart, Date timeEnd,
			Date timeUnderTest) {

		if (timeStart.before(timeUnderTest) && timeUnderTest.before(timeEnd)) {
			return true;
		} else
			return false;
	}

	public static float distanceBetweenTwoPoints(float lat1, float lng1,
			float lat2, float lng2) {
		double earthRadius = 6371000; // meters
		double dLat = Math.toRadians(lat2 - lat1);
		double dLng = Math.toRadians(lng2 - lng1);
		double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(dLng / 2)
				* Math.sin(dLng / 2);
		double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		float dist = (float) (earthRadius * c);

		return dist;
	}
}