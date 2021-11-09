package com.bybit.data.util;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.bybit.order.OrderInfo;
import com.bybit.order.util.DateUtil;
import com.bybit.order.util.OrderUtil;

@SpringBootTest
public class DataUtilTest {

	private static final Logger log = LoggerFactory.getLogger(DataUtilTest.class);
	
	@Value("${api.url}")
	private String apiUrl;
	
	
	@Test
	public void testgetKline() {
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
        map.put("interval", "5");
        int fromSeconde = 5*30*60;
        log.info("fromSeconde ==>"+fromSeconde + "");
        log.info("from ==>"+((OrderUtil.toEpochMilli()/1000) - fromSeconde) + "");
        log.info(DateUtil.getDefaultDateStr(((OrderUtil.toEpochMilli()/1000) - fromSeconde)*1000 + ""));
        log.info("to ==>"+((OrderUtil.toEpochMilli()/1000)) + "");
        log.info(DateUtil.getDefaultDateStr(OrderUtil.toEpochMilli() + ""));
        
        map.put("from", ((OrderUtil.toEpochMilli()/1000) - fromSeconde)+"");
        map.put("limit", "35");
        
        Map<String, Object> resMap = DataUtil.getKline(apiUrl,map);
        //log.info(resMap.toString());
        
        List<Map<String, Object>> list = (List<Map<String, Object>>)resMap.get("result");
        Map<String, Map<String, Object>> KlineMap_5 = new TreeMap<String, Map<String, Object>>();
        for (Map<String, Object> m : list) {
        	String key = m.get("open_time").toString();
        	m.put("date_time", DateUtil.getDefaultDateStr(key,1000));
        	KlineMap_5.put(key, m);
			log.info(m.toString());
		}
        
        OrderInfo.BSTUSD.KlineMap_5 = KlineMap_5;
        
        
	}

}
