package com.nokia.ices.apps.fusion.security.external;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ExternalLoginHolder {
	
	public static final Logger logger = LoggerFactory.getLogger(ExternalLoginHolder.class);
	
	
	private static Map<String, ExternalLoginEntity> externalLoginMap = Collections
			.synchronizedMap(new HashMap<String, ExternalLoginEntity>());

	public static Map<String, ExternalLoginEntity> getExternalLoginMap() {
		return externalLoginMap;
	}
	
	public static void putExternalLoginMap(String token, ExternalLoginEntity entity) {
		externalLoginMap.put(token, entity);
	}

	public static void setExternalLoginMap(Map<String, ExternalLoginEntity> externalLoginMap) {
		ExternalLoginHolder.externalLoginMap = externalLoginMap;
	}
	
	public static void removeToken(String token) {
		deleteExpiredToken();
		if(externalLoginMap.containsKey(token)){
			externalLoginMap.remove(token);
		}
		
	}
	
	
	public static ExternalLoginEntity checkToken(String token) {
		deleteExpiredToken();
		if (!externalLoginMap.containsKey(token)) {
			return new ExternalLoginEntity();
//			throw new RuntimeException("TOKEN_IS_EXPIRED");
		} else {
			externalLoginMap.get(token).setExpireTime(System.currentTimeMillis() + ExternalLoginEntity.expireInterval);
			return externalLoginMap.get(token);
		}
	}
	private static void deleteExpiredToken() {
		Long currentTime = System.currentTimeMillis();
		externalLoginMap.forEach((token, loginEntity) -> {
			logger.info(externalLoginMap.get(token).toString());
			if (currentTime >= loginEntity.getExpireTime()) {
				externalLoginMap.remove(token);
			}
		});
	}

}
