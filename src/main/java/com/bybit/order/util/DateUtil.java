package com.bybit.order.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	public static String getDateStr(String timestamp) {
		String format = "yyyy-MM-dd HH:mm:ss.SSS";
		return getDateStr(format,timestamp);
	}
	
	public static String getDateStr(String format,String timestamp) {
		long time = Long.parseLong(timestamp);
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = Instant.ofEpochMilli(time);
		ZonedDateTime zonedDateTime = instant.atZone(zone);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		//System.out.println("Asia/Seoul : " + zonedDateTime33.format(formatter));
		return zonedDateTime.format(formatter);
	}
	
	public static String getDefaultDateStr(String timestamp) {
		String format = "yyyyMMddHHmmss";
		return getDateStr(format,timestamp);
	}
}
