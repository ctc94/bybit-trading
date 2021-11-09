package com.bybit.order.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateUtil {
	public static String getDateStr(String timestamp) {
		String format = "yyyy-MM-dd HH:mm:ss.SSS";
		return getDateStr(format,timestamp,1);
	}
	
	public static String getDateStr(String format,String timestamp,int multi) {
		long time = Long.parseLong(timestamp)*multi;
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = Instant.ofEpochMilli(time);
		ZonedDateTime zonedDateTime = instant.atZone(zone);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
		//System.out.println("Asia/Seoul : " + zonedDateTime33.format(formatter));
		return zonedDateTime.format(formatter);
	}
	
	public static String getDefaultDateStr(String timestamp) {
		String format = "yyyyMMddHHmmss";
		return getDateStr(format,timestamp,1);
	}
	public static String getDefaultDateStr(String timestamp,int multi) {
		String format = "yyyyMMddHHmmss";
		return getDateStr(format,timestamp,multi);
	}
}
