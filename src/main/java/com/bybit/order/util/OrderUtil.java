package com.bybit.order.util;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OrderUtil {

	static List<String> ret = null;
	static {
		ret = FileUtil.readFile(".api");
	}

	public static String getApiKey() {
		String line = ret.get(0);
		String key[] = line.split(":");
		return key[0];
	}

	public static String getSecret() {
		String line = ret.get(0);
		String key[] = line.split(":");
		return key[1];
	}
	
	public static long toEpochMilli() {
		return Instant.now().toEpochMilli();
	}

	public static String getQueryString(TreeMap<String, String> params, String secret) {
		Set<String> keySet = params.keySet();
		Iterator<String> iter = keySet.iterator();
		StringBuilder sb = new StringBuilder();
		while (iter.hasNext()) {
			String key = iter.next();
			sb.append(key + "=" + params.get(key));
			sb.append("&");
		}
		sb.deleteCharAt(sb.length() - 1);

		return sb + "&sign=" + getSign(secret, sb.toString());
	}

	public static TreeMap<String, String> getTreeMap() {
		return new TreeMap<String, String>(new Comparator<String>() {
			public int compare(String obj1, String obj2) {
				// sort in alphabet order
				return obj1.compareTo(obj2);
			}
		});
	}

	public static String getSign(String secret, String sb) {
		if("".equals(secret)) return "";
		return bytesToHex(getByte(secret, sb));
	}
	
	public static Map<String, String> orderCreate(String apiUrl,TreeMap<String,String> map) {
		
		map.put("time_in_force", "GoodTillCancel");
        map.put("timestamp", toEpochMilli()+"");
        map.put("api_key", getApiKey());
		
		String queryStr = getQueryString(map, getSecret());
		
		//return postNewCall(apiUrl+"/private/linear/order/create",queryStr);
		return postNewCall(apiUrl+"/private/linear/order/create",queryStr);
	}
	
	public static Map<String, Object> getOrder(String apiUrl,TreeMap<String,String> map) {
		map.put("timestamp", toEpochMilli()+"");
        map.put("api_key", getApiKey());
		
		String queryStr = getQueryString(map, getSecret());
		System.out.println(queryStr);
		//return Map.of();
		return getNewCall(apiUrl+"/private/linear/order/list",queryStr);
	}
	
	public static Map<String, String> getOrderCancel(String apiUrl,TreeMap<String,String> map) {
		map.put("timestamp", toEpochMilli()+"");
        map.put("api_key", getApiKey());
		
		String queryStr = getQueryString(map, getSecret());
		System.out.println(queryStr);
		//return Map.of();
		return postNewCall(apiUrl+"/private/linear/order/cancel",queryStr);
	}
	
	public static Map<String, String> getOrderCancelAll(String apiUrl,TreeMap<String,String> map) {
		map.put("timestamp", toEpochMilli()+"");
        map.put("api_key", getApiKey());
		
		String queryStr = getQueryString(map, getSecret());
		System.out.println(queryStr);
		//return Map.of();
		return postNewCall(apiUrl+"/private/linear/order/cancelAll",queryStr);
	}
	
	public static Map<String, Object> getMyPosition(String apiUrl,TreeMap<String,String> map) {
		map.put("timestamp", toEpochMilli()+"");
        map.put("api_key", getApiKey());
		
		String queryStr = getQueryString(map, getSecret());
		System.out.println(queryStr);
		//return Map.of();
		return getNewCall(apiUrl+"/private/linear/position/list",queryStr);
	}
	
	/*
	 * 공통 호출 함수
	 */
	private static Map<String, String> postNewCall(String fullUrl,String queryStr) {
		OkHttpClient client = new OkHttpClient();
        RequestBody body=new FormBody.Builder().build();
        
        Request request = new Request.Builder()
                .post(body)
                .url(fullUrl+"?"+queryStr)
                .build();
        Call call = client.newCall(request);
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            Response response = call.execute(); 
            Map<String, String> resMap = mapper.readValue(response.body().string(),Map.class);
            return resMap;
        }catch (IOException e){
            e.printStackTrace();
        }
		return Map.of();
	}
	
	/*
	 * 공통 호출 함수
	 */
	public static Map<String, Object> getNewCall(String fullUrl,String queryStr) {
		OkHttpClient client = new OkHttpClient();
        //RequestBody body=new FormBody.Builder().build();
        
        Request request = new Request.Builder()
        		.get()
                .url(fullUrl+"?"+queryStr)
                .build();
        Call call = client.newCall(request);
        
        ObjectMapper mapper = new ObjectMapper();
        try {
            Response response = call.execute(); 
            Map<String, Object> resMap = mapper.readValue(response.body().string(),Map.class);
            return resMap;
        }catch (IOException e){
            e.printStackTrace();
        }
		return Map.of();
	}

	private static byte[] getByte(String secret, String sb) {
		Mac sha256_HMAC = null;
		try {
			sha256_HMAC = Mac.getInstance("HmacSHA256");
			SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
			sha256_HMAC.init(secret_key);
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		} catch (InvalidKeyException e1) {
			throw new RuntimeException(e1);
		}
		return sha256_HMAC.doFinal(sb.getBytes());
	}

	private static String bytesToHex(byte[] hash) {
		StringBuffer hexString = new StringBuffer();
		for (int i = 0; i < hash.length; i++) {
			String hex = Integer.toHexString(0xff & hash[i]);
			if (hex.length() == 1)
				hexString.append('0');
			hexString.append(hex);
		}
		return hexString.toString();
	}
}
