package com.bybit.data.util;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bybit.order.OrderInfo;
import com.bybit.order.util.OrderUtil;
import com.bybit.redis.config.RedisMessageSubscriber;

public class DataUtil {
	private static final Logger log = LoggerFactory.getLogger(DataUtil.class);
	
	public static Map<String, Object> getKline(String apiUrl, TreeMap<String, String> map) {
		
		String queryStr = OrderUtil.getQueryString(map, "");
		return OrderUtil.getNewCall(apiUrl+"/v2/public/kline/list",queryStr);
	}

	public static void getEMA(Map<String, Map<String, Object>> map,int emaSize,String open_time) {
		
		float sum = 0;
		int size = map.keySet().size();
		int skipsize = size - emaSize;
		int count = 0;
		for (String key : map.keySet()) {
			
			count++;
			
			if(count <= skipsize) continue;
			
			Map<String, Object> vm = map.get(key);
			float close = Float.parseFloat(vm.get("close").toString());
			float open = Float.parseFloat(vm.get("open").toString());
			sum += (close+open)/2;
		}
		
		float avg = sum/emaSize;
		
		log.info("sum ==>"+ sum);
		log.info("avg ==>"+ avg);
		
		if(OrderInfo.BSTUSD.KlineMap_5_EMA == null) {
			OrderInfo.BSTUSD.KlineMap_5_EMA = new TreeMap<String, String>();
		}
		OrderInfo.BSTUSD.KlineMap_5_EMA.put(open_time, String.valueOf(avg));
		log.info(OrderInfo.BSTUSD.KlineMap_5_EMA.toString());
	}

}
