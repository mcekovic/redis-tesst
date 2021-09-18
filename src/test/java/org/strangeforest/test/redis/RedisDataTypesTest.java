package org.strangeforest.test.redis;

import java.util.*;

import org.junit.jupiter.api.*;

import redis.clients.jedis.*;
import redis.clients.jedis.params.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RedisDataTypesTest {

	private Jedis jedis;

	@BeforeAll
	public void setUp() {
		jedis = new Jedis("localhost", 6379);
	}

	@Test
	void testHash() {
		jedis.hmset("user/1", Map.of("name", "Pera", "age", "30"));

		System.out.println(jedis.hmget("user/1", "name", "age"));
	}

	@Test
	void testStream() {
		jedis.xtrim("user-stream/1", 0L, false);

		jedis.xadd("user-stream/1", Map.of("name", "Pera", "age", "30"), XAddParams.xAddParams());

		System.out.println(jedis.xrange("user-stream/1", FIRST_ENTRY, LAST_ENTRY));

		System.out.println(jedis.xinfoStream("user-stream/1").getFirstEntry());
		System.out.println(jedis.xinfoStream("user-stream/1").getLastEntry());
		System.out.println(jedis.xinfoStream("user-stream/1").getLength());
	}

	public static final StreamEntryID FIRST_ENTRY = new StreamEntryID() {
		public String toString() {
			return "-";
		}
	};

	public static final StreamEntryID LAST_ENTRY = new StreamEntryID() {
		public String toString() {
			return "+";
		}
	};
}
