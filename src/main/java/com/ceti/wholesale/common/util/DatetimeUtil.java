package com.ceti.wholesale.common.util;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class DatetimeUtil {
	
	public static Instant convertLongStringToInstant(String time) {
		try {
			return Instant.ofEpochSecond(Long.valueOf(time));
		}catch(Exception e) {
			return null;
		}
	}
	
	public static String formatLongToString(String zoneId, Instant time) {
		try {
	        DateTimeFormatter formatterNo = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	        LocalDateTime today = LocalDateTime.ofInstant(time , ZoneId.of(zoneId));
	        return today.format(formatterNo);
	    }catch(Exception e) {
			return null;
		}
	}
}
