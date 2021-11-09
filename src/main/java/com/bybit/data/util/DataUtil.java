package com.bybit.data.util;

import java.util.Map;
import java.util.TreeMap;

import com.bybit.order.util.OrderUtil;

public class DataUtil {

	public static Map<String, Object> getKline(String apiUrl, TreeMap<String, String> map) {
		
		String queryStr = OrderUtil.getQueryString(map, "");
		System.out.println(queryStr);
		//return Map.of();
		return OrderUtil.getNewCall(apiUrl+"/v2/public/kline/list",queryStr);
	}

}
