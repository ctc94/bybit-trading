package com.bybit.redis.config;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

@Service
public class RedisMessageSubscriber implements MessageListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(RedisMessageSubscriber.class);

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String msg = new String(message.getBody());
		//
		if (msg.indexOf("last_price") == -1)
			return;
		
		//LOGGER.info(msg);
		JSONObject jObject = new JSONObject(msg);
		
		//String topic = getValue(jObject,"topic");
		
		JSONObject obj = getUpdate(jObject);
		//LOGGER.info(obj.toString());
		LOGGER.info("last_price="+getValue(obj,"last_price"));
		LOGGER.info("last_tick_direction="+getValue(obj,"last_tick_direction"));
		LOGGER.info("timestamp_e6="+getValue(jObject,"timestamp_e6"));
		LOGGER.info("dateTime="+getDateStr(getValue(jObject,"timestamp_e6")));
	}
	
	private String getDateStr(String timestamp) {
		long time = Long.parseLong(timestamp)/1000;
		ZoneId zone = ZoneId.systemDefault();
		Instant instant = Instant.ofEpochMilli(time);
		ZonedDateTime zonedDateTime = instant.atZone(zone);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
		//System.out.println("Asia/Seoul : " + zonedDateTime33.format(formatter));
		return zonedDateTime.format(formatter);
		
	}
	
	private String getValue(JSONObject jObject,String key) {
		try {
			return jObject.get(key).toString();
		} catch (JSONException e) {
			return "";
		}

	}

	private String getTopic(JSONObject jObject) {

		try {
			String topic = jObject.getString("topic");
			return topic;
		} catch (JSONException e) {
			return "";
		}

	}

	/**
	 * instrument_info용 데이터가져오기
	 * 
	 * @param jObject
	 * @return
	 */
	private JSONObject getUpdate(JSONObject jObject) {

		try {
			JSONObject data = jObject.getJSONObject("data");
			JSONArray jArray = data.getJSONArray("update");
			JSONObject obj = jArray.getJSONObject(0);
			return obj;
		} catch (JSONException e) {
			return new JSONObject();
		}
	}

	/**
	 * klineV2용 데이터가져오기
	 * 
	 * @param jObject
	 * @return
	 */
	private JSONObject getData(JSONObject jObject) {

		try {
			JSONArray jArray = jObject.getJSONArray("data");
			JSONObject obj = jArray.getJSONObject(0);
			return obj;
		} catch (JSONException e) {
			return null;
		}
	}
}
