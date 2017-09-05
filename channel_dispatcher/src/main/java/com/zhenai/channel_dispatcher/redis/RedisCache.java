package com.zhenai.channel_dispatcher.redis;

import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.Cache;
import org.springframework.cache.support.SimpleValueWrapper;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisCache implements Cache {
	
	private static final String HASH = "abt";

	private RedisTemplate<String, Object> redisTemplate;  
	private String name;  

	public RedisTemplate<String, Object> getRedisTemplate() {  
		return redisTemplate;  
	}  

	public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {  
		this.redisTemplate = redisTemplate;  
	}  

	public void setName(String name) {  
		this.name = name;  
	}  

	@Override  
	public String getName() {  
		return this.name;  
	}  

	@Override  
	public Object getNativeCache() {  
		return this.redisTemplate;  
	}  

	@Override  
	public ValueWrapper get(Object key) { 
		final String keyf = getKeyf(key);
		if (StringUtils.isEmpty(keyf)) {
			return null;
		}
		Object object = redisTemplate.opsForHash().get(HASH, keyf);
		return (object != null ? new SimpleValueWrapper(object) : null);  
	}

	private String getKeyf(Object key) {
		return String.valueOf(key);
	}  

	@Override  
	public void put(Object key, Object value) {  
		final String keyf = getKeyf(key);
		if (StringUtils.isEmpty(keyf)) {
			return;
		}
		final Object valuef = value;  
		boolean containsKey = redisTemplate.hasKey(HASH);
		redisTemplate.opsForHash().put(HASH, keyf, valuef);
		if (!containsKey) {
			redisTemplate.expire(HASH, 4, TimeUnit.HOURS);
		}
	}  

	@Override  
	public void evict(Object key) {    
		final String keyf = getKeyf(key);
		if (StringUtils.isEmpty(keyf)) {
			return;
		}
		redisTemplate.opsForHash().delete(HASH, keyf);
	}  

	@Override  
	public void clear() {
		redisTemplate.delete(HASH);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(Object key, Class<T> clazz) {
		final String keyf = getKeyf(key);
		if (StringUtils.isEmpty(keyf)) {
			return null;
		}
		Object object = redisTemplate.opsForHash().get(HASH, keyf);
		return (T) object;  
	}

	@Override
	public ValueWrapper putIfAbsent(Object key, Object value) {
		final String keyf = getKeyf(key);
		if (StringUtils.isEmpty(keyf)) {
			return null;
		}
		final Object valuef = value;  
		boolean success = redisTemplate.opsForHash().putIfAbsent(HASH, keyf, valuef);
		if (success) {
			return null;
		} else {
			return get(keyf);
		}
	}  
}  