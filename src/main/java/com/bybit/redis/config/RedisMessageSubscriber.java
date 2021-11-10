package com.bybit.redis.config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Service;

import com.bybit.data.util.DataUtil;
import com.bybit.order.OrderInfo;
import com.bybit.order.util.DateUtil;

@Service
public class RedisMessageSubscriber implements MessageListener {

	private static final Logger log = LoggerFactory.getLogger(RedisMessageSubscriber.class);
	private static String before_open_time = "";

	@Override
	public void onMessage(Message message, byte[] pattern) {
		String msg = new String(message.getBody());
		
		getKline_5_BTCUSD(msg);
		getLastPrice_BTCUSD(msg);
		
	}
	//@Async
	private void getKline_5_BTCUSD(String msg) {
		if (msg.indexOf("klineV2.5.BTCUSD") == -1)
			return;
		
		if(OrderInfo.BSTUSD.KlineMap_5 == null) return;
		
		JSONObject jObject = new JSONObject(msg);
		JSONObject obj = getData(jObject);
		String start = getValue(obj,"start");
		
		if("".equals(before_open_time)) {
			before_open_time = start;
		}
		
		if(!start.equals(before_open_time)) {
			//to-do: 이동평균선 구하기,R값구하기
			DataUtil.getEMA(OrderInfo.BSTUSD.KlineMap_5,30,start);
			
			before_open_time = start;
			
			log.info("before_open_time =>"+ before_open_time);
			log.info("OrderInfo.BSTUSD.KlineMap_5 size : " +(OrderInfo.BSTUSD.KlineMap_5.keySet().size()+1));
			
		}
		
		obj.put("open_time", obj.get("start"));
		obj.put("date_time", DateUtil.getDefaultDateStr(start,1000));
		OrderInfo.BSTUSD.KlineMap_5.put(start,obj.toMap());
		
		//log.info(obj.toString());
		
	}

	//@Async
	private void getLastPrice_BTCUSD(String msg) {
		if (msg.indexOf("last_price") == -1)
			return;
		
		//LOGGER.info(msg);
		JSONObject jObject = new JSONObject(msg);
		
		JSONObject obj = getUpdate(jObject);
		OrderInfo.BSTUSD.lastPrice = getValue(obj,"last_price");
		//log.info("last_price="+getValue(obj,"last_price"));
//		log.info("last_tick_direction="+getValue(obj,"last_tick_direction"));
//		log.info("timestamp_e6="+getValue(jObject,"timestamp_e6"));
//		log.info("dateTime="+getDateStr(getValue(jObject,"timestamp_e6")));
		
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
