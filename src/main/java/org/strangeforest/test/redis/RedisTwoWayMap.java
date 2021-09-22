package org.strangeforest.test.redis;

import java.util.*;

import redis.clients.jedis.*;

public class RedisTwoWayMap implements TwoWayMap {

	private final Jedis jedis;

	public RedisTwoWayMap(Jedis Jedis) {
		this.jedis = Jedis;
	}

	@Override public Set<String> get1(String key1) {
		return jedis.smembers(redisKey1(key1));
	}

	@Override public Set<String> get2(String key2) {
		return jedis.smembers(redisKey2(key2));
	}

	@Override public void put(String key1, String... keys2) {
		jedis.sadd(redisKey1(key1), keys2);
		for (var key2 : keys2)
			jedis.sadd(redisKey2(key2), key1);
	}

	@Override public void remove1(String key1) {
		var redisKey1 = redisKey1(key1);
		var keys2 = jedis.smembers(redisKey1);
		for (var key2 : keys2)
			jedis.srem(redisKey2(key2), key1);
		jedis.del(redisKey1);
	}

	@Override public void remove2(String key2) {
		var redisKey2 = redisKey2(key2);
		var keys1 = jedis.smembers(redisKey2);
		for (var key1 : keys1)
			jedis.srem(redisKey1(key1), key2);
		jedis.del(redisKey2);
	}

	private String redisKey1(String key1) {
		return "map/1/" + key1;
	}

	private String redisKey2(String key2) {
		return "map/2/" + key2;
	}
}
