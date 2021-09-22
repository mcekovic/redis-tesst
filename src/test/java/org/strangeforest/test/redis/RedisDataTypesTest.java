package org.strangeforest.test.redis;

import java.time.*;
import java.util.*;

import org.junit.jupiter.api.*;
import redis.clients.jedis.*;

import static org.assertj.core.api.Assertions.*;
import static org.strangeforest.test.redis.GlobalEntryID.*;
import static org.testcontainers.shaded.org.awaitility.Awaitility.*;

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

		jedis.xadd("user-stream/1", NEW_ENTRY, Map.of("name", "Pera", "age", "30"));
		jedis.xadd("user-stream/1", NEW_ENTRY, Map.of("name", "Zika", "age", "40"));

		var streamInfo = jedis.xinfoStream("user-stream/1");
		System.out.println(streamInfo.getLength());
		System.out.println(streamInfo.getFirstEntry());
		System.out.println(streamInfo.getLastEntry());

		System.out.println(jedis.xrange("user-stream/1", MIN_ENTRY, MAX_ENTRY));

		assertThat(jedis.xread(1, 200L, Map.entry("user-stream/1", LAST_ENTRY))).isEmpty();

		new Thread(() -> {
			try {
				Thread.sleep(200L);
			}
			catch (InterruptedException ignored) {}

			new Jedis("localhost", 6379)
				.xadd("user-stream/1", NEW_ENTRY, Map.of("name", "Mika", "age", "50"));
		}).start();

		await().atMost(Duration.ofMillis(1000L)).until(() -> {
			var entries = jedis.xread(1, 1000L, Map.entry("user-stream/1", LAST_ENTRY));
			System.out.println(entries);
			return !entries.isEmpty();
		});
	}
}
