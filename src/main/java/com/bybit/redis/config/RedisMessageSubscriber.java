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
		String topic = new String(pattern);
		//log.info(new String(pattern));
		String msg = new String(message.getBody());
		
		if("instrument_info.100ms.BTCUSDT".equals(topic)) {
			getLastPrice_BTCUSDT(msg);
		}
		
		if("candle.5.BTCUSDT".equals(topic)) {
			getKline_5_BTCUSDT(msg);
		}
		
		if("candle.30.BTCUSDT".equals(topic)) {
			getKline_30_BTCUSDT(msg);
		}
		
		if("candle.60.BTCUSDT".equals(topic)) {
			getKline_60_BTCUSDT(msg);
		}
		
		if("candle.240.BTCUSDT".equals(topic)) {
			getKline_240_BTCUSDT(msg);
		}
		
		if("candle.D.BTCUSDT".equals(topic)) {
			getKline_D_BTCUSDT(msg);
		}
		
		
	}
	private void getKline_D_BTCUSDT(String msg) {
		// TODO Auto-generated method stub
		
	}
	private void getKline_240_BTCUSDT(String msg) {
		// TODO Auto-generated method stub
		
	}
	private void getKline_60_BTCUSDT(String msg) {
		// TODO Auto-generated method stub
		
	}
	private void getKline_30_BTCUSDT(String msg) {
		// TODO Auto-generated method stub
		
	}
	//@Async
	private void getKline_5_BTCUSDT(String msg) {
		
		if(OrderInfo.BSTUSD.KlineMap_5 == null) return;
		
		JSONObject jObject = new JSONObject(msg);
		JSONObject obj = getData(jObject);
		if(obj == null) return;
		//log.info(obj.toString());
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
	private void getLastPrice_BTCUSDT(String msg) {
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
		} catch (Exception e) {
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
