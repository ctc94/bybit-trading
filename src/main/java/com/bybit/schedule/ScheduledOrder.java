package com.bybit.schedule;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bybit.data.util.DataUtil;
import com.bybit.order.OrderInfo;
import com.bybit.order.util.DateUtil;
import com.bybit.order.util.OrderUtil;;

@Service
public class ScheduledOrder {
	private static final Logger log = LoggerFactory.getLogger(ScheduledOrder.class);
	@Value("${api.url}")
	private String apiUrl;

	@Scheduled(fixedRate = 2000)
	public void getLastPrice() {
		//log.info("lastPrice =>" + OrderInfo.BSTUSD.lastPrice);
	}
	
	@Scheduled(fixedRate = Long.MAX_VALUE)
	public void getKline() {
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

	//@Scheduled(fixedRate = 60000)
	public void getMyPosition() {
		//if (OrderInfo.BSTUSD.myPositionMap == null) {
			TreeMap<String, String> map = OrderUtil.getTreeMap();
			map.put("symbol", "BTCUSD");

			Map<String, Object> resMap = OrderUtil.getMyPosition(apiUrl, map);
			OrderInfo.BSTUSD.myPositionMap = (Map<String, Object>) resMap.get("result");
		//}
		log.info(OrderInfo.BSTUSD.myPositionMap.toString());
		log.info("entry_price=>"+OrderInfo.BSTUSD.myPositionMap.get("entry_price"));
		log.info("size=>"+OrderInfo.BSTUSD.myPositionMap.get("size"));
		
		if(((Integer)OrderInfo.BSTUSD.myPositionMap.get("size")).intValue() == 0) return;
		//log.info(OrderInfo.BSTUSD.myPositionMap.get("unrealised_pnl").toString());
		
		Double unrealised_pnl = (Double)OrderInfo.BSTUSD.myPositionMap.get("unrealised_pnl");
		
		if(OrderInfo.BSTUSD.lastPrice == null) return;
		
		float lastPrice = Float.parseFloat(OrderInfo.BSTUSD.lastPrice);
		float wallet_balance = Float.parseFloat((String)OrderInfo.BSTUSD.myPositionMap.get("wallet_balance"));
		
		log.info("unrealised_pnl=>"+unrealised_pnl);
		log.info("wallet_balance=>"+wallet_balance);
		log.info("usd=>"+(unrealised_pnl * lastPrice) + "");
		log.info("totalusd=>"+(wallet_balance * lastPrice) + "");

	}

}
