package org.strangeforest.test.redis;

import java.util.*;

import redis.clients.jedis.*;

public class TxTwoWayRedisMap {

	private final Jedis jedis;

	public TxTwoWayRedisMap(Jedis Jedis) {
		this.jedis = Jedis;
	}

	public Set<String> get1(String key1) {
		return jedis.smembers(redisKey1(key1));
	}

	public Set<String> get2(String key2) {
		return jedis.smembers(redisKey2(key2));
	}

	public void put(String key1, String... keys2) {
		var tx = jedis.multi();
		tx.sadd(redisKey1(key1), keys2);
		for (var key2 : keys2)
			tx.sadd(redisKey2(key2), key1);
		tx.exec();
	}

	public void remove1(String key1) {
		var redisKey1 = redisKey1(key1);
		var keys2 = jedis.smembers(redisKey1);
		var tx = jedis.multi();
		for (var key2 : keys2)
			tx.srem(redisKey2(key2), key1);
		tx.del(redisKey1);
		tx.exec();
	}

	public void remove2(String key2) {
		var redisKey2 = redisKey2(key2);
		var keys1 = jedis.smembers(redisKey2);
		var tx = jedis.multi();
		for (var key1 : keys1)
			tx.srem(redisKey1(key1), key2);
		tx.del(redisKey2);
		tx.exec();
	}

	private String redisKey1(String key1) {
		return "map/1/" + key1;
	}

	private String redisKey2(String key2) {
		return "map/2/" + key2;
	}
}
