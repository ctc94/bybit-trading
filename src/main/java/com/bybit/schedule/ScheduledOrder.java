package com.bybit.schedule;

import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.bybit.order.OrderInfo;
import com.bybit.order.util.OrderUtil;;

@Service
public class ScheduledOrder {
	private static final Logger log = LoggerFactory.getLogger(ScheduledOrder.class);
	@Value("${api.url}")
	private String apiUrl;

	@Scheduled(fixedRate = 2000)
	public void getLastPrice() {
		log.info("lastPrice =>" + OrderInfo.BSTUSD.lastPrice);
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
