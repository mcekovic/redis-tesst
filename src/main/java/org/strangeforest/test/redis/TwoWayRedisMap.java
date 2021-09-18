package org.strangeforest.test.redis;

import java.util.*;

import org.springframework.data.redis.connection.*;

public class TwoWayRedisMap {

	private final StringRedisConnection redis;

	public TwoWayRedisMap(StringRedisConnection redis) {
		this.redis = redis;
	}

	public Set<String> get1(String key1) {
		return redis.sMembers(redisKey1(key1));
	}

	public Set<String> get2(String key2) {
		return redis.sMembers(redisKey2(key2));
	}

	public void put(String key1, String... keys2) {
		redis.sAdd(redisKey1(key1), keys2);
		for (var key2 : keys2)
			redis.sAdd(redisKey2(key2), key1);
	}

	public void remove1(String key1) {
		var redisKey1 = redisKey1(key1);
		for (var key2 : redis.sMembers(redisKey1))
			redis.sRem(redisKey2(key2), key1);
		redis.del(redisKey1);
	}

	public void remove2(String key2) {
		var redisKey2 = redisKey2(key2);
		for (var key1 : redis.sMembers(redisKey2))
			redis.sRem(redisKey1(key1), key2);
		redis.del(redisKey2);
	}

	private String redisKey1(String key1) {
		return "map/1/" + key1;
	}

	private String redisKey2(String key2) {
		return "map/2/" + key2;
	}
}
