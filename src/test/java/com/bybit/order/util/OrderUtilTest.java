package com.bybit.order.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.TreeMap;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

@SpringBootTest
public class OrderUtilTest {

	private static final Logger log = LoggerFactory.getLogger(OrderUtilTest.class);
	
	@Value("${api.url}")
	private String apiUrl;

	@Test
	public void testGetApiKey() {
		
		String apiKey = OrderUtil.getApiKey();
		assertEquals("Q8P5vdot8RRonx9Wka", apiKey);
	}
	
	@Test
	public void testGetSecret() {		
		String secret = OrderUtil.getSecret();
		assertEquals("VyG6wQk2AYZqxdhFkPWmBcZsT5nyajxCy2sh", secret);
	}
	
	@Test
	public void testReadFile() {
		log.info(FileUtil.readFile(".api").toString());
	}
	
	@Test
	public void testGenQueryString() throws UnknownHostException {
		
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
        map.put("order_type", "Market");//[Limit,Market]
        map.put("qty", "100");
        map.put("side", "Buy");//[Sell,Buy]
        //map.put("price", "61150");
        map.put("time_in_force", "GoodTillCancel");
        map.put("timestamp", OrderUtil.toEpochMilli()+"");
        map.put("api_key", OrderUtil.getApiKey());
		
		String queryStr = OrderUtil.genQueryString(map, OrderUtil.getSecret());
		log.info(queryStr);
		
	}
	
	@Test
	public void testOrderCreate() {
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
        map.put("order_type", "Market");//[Limit,Market]
        map.put("qty", "200");
        map.put("side", "Sell");//[Buy,Sell]
        //map.put("price", "60450");        
        
        Map<String, String> resMap = OrderUtil.orderCreate(apiUrl,map);
        log.info(resMap.toString());
	}
	
	@Test
	public void testGetOrder() {
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
		map.put("order_status", "New");
        
        Map<String, Object> resMap = OrderUtil.getOrder(apiUrl,map);
        log.info(resMap.toString());
	}
	
	@Test
	public void testGetOrderCancel() {
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
		map.put("order_id", "9854aa57-b64f-4610-8628-5c80d374f63f");
        
        Map<String, String> resMap = OrderUtil.getOrderCancel(apiUrl,map);
        log.info(resMap.toString());
	}
	
	@Test
	public void testGetOrderCancelAll() {
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
        
        Map<String, String> resMap = OrderUtil.getOrderCancelAll(apiUrl,map);
        log.info(resMap.toString());
	}
	
	@Test
	public void testGetMyPosition() {
		TreeMap<String,String> map = OrderUtil.getTreeMap();
		map.put("symbol", "BTCUSD");
        
        Map<String, Object> resMap = OrderUtil.getMyPosition(apiUrl,map);
        log.info(resMap.toString());
	}

}
