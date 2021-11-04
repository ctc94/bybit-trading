package com.bybit.redis.config;

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
		//LOGGER.info("redis topic = " + new String(pattern));
		JSONObject jObject = new JSONObject(new String(message.getBody()));		
		String topic = getTopic(jObject);
		LOGGER.info("websocket topic = " + topic);
		JSONObject obj = getData(jObject);
		LOGGER.info(obj.toString());
	}
	
	private String getTopic(JSONObject jObject) {
		
		try {
			String topic = jObject.getString("topic");
			return topic;
		}catch (JSONException e) {
			return "";
		}
		
	}
	
	private JSONObject getData(JSONObject jObject) {
		
		try {
			JSONArray jArray = jObject.getJSONArray("data");
			JSONObject obj = jArray.getJSONObject(0);
			return obj;
		}catch (JSONException e) {
			return null;
		}
	}
}


