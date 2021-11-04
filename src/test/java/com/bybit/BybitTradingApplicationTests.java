package com.bybit;

import java.sql.Timestamp;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class BybitTradingApplicationTests {

	@Test
	void contextLoads() {
		long time = Long.parseLong("1636005300")*1000;
		ZoneId zone = ZoneId.systemDefault();
		System.out.println(zone);
		Instant instant = Instant.ofEpochMilli(time);
		ZonedDateTime zonedDateTime33 = instant.atZone(ZoneId.of("Asia/Seoul"));
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		System.out.println("Asia/Seoul : " + zonedDateTime33.format(formatter));

		ZonedDateTime zonedDateTime333 = instant.atZone(ZoneId.of("UTC"));		
		String formattedString = zonedDateTime333.format(formatter);
		System.out.println("UTC : " + formattedString);
		
		
		LocalDateTime localDateTime33 = LocalDateTime.ofInstant(instant, ZoneId.of("Asia/Seoul"));
		//ZonedDateTime zonedDateTime33 = ZonedDateTime.of(localDateTime33, ZoneId.of("UTC"));
		System.out.println(localDateTime33);
		
		
		ZonedDateTime utcDateTime = ZonedDateTime.now(ZoneId.of("UTC"));
		ZonedDateTime korDateTime = ZonedDateTime.now(ZoneId.of("Asia/Seoul"));
		Instant kornow = korDateTime.toInstant();
		System.out.println(kornow.toEpochMilli());
		
		Timestamp timestamputc = Timestamp.valueOf(utcDateTime.toLocalDateTime().atZone(ZoneId.of("Asia/Seoul")).toLocalDateTime());
        System.out.println(timestamputc);
		
		
		Instant utcnow = utcDateTime.toInstant();
        
		System.out.println(instant.toEpochMilli());
        System.out.println(utcnow.toEpochMilli());
        
		
        System.out.println(utcDateTime);
        System.out.println(korDateTime);
        
		
				
		Timestamp timestamp = Timestamp.from(instant);
        System.out.println(timestamp);
        
        
        Timestamp timestamp1 = Timestamp.from(Instant.ofEpochMilli(time));
        System.out.println(timestamp1);
        
        LocalDateTime noTimeZoneLocalDateTime = timestamp.toLocalDateTime();
        System.out.println(noTimeZoneLocalDateTime);
        ZonedDateTime zonedDateTime =
                noTimeZoneLocalDateTime.atZone(ZoneId.of("Asia/Seoul"));
            System.out.println(zonedDateTime);
            
            ZonedDateTime zonedDateTime1 =
                    noTimeZoneLocalDateTime.atZone(ZoneId.of("UTC"));
                System.out.println(zonedDateTime1);
	}

}
