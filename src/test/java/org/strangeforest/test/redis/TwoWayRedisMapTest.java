package org.strangeforest.test.redis;

import java.util.*;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.boot.test.context.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.core.*;

@RedisTest
@SpringBootTest
class TwoWayRedisMapTest {

	@Autowired
	private RedisTemplate<String, String> template;

	@Test
	void testTwoWayRedisMap() {
		template.execute((RedisCallback<Void>) redisConnection -> {
			var map = new TwoWayRedisMap((StringRedisConnection)redisConnection);
			map.put("A", "1", "2");
			map.put("B", "1", "2", "3");
			map.put("C", "1", "2", "3", "4");

			map.remove1("B");
			map.remove2("2");

			Assertions.assertThat(map.get1("A")).containsExactlyInAnyOrder("1");
			Assertions.assertThat(map.get1("B")).isEmpty();
			Assertions.assertThat(map.get1("C")).containsExactlyInAnyOrder("1", "3", "4");
			Assertions.assertThat(map.get2("1")).containsExactlyInAnyOrder("A", "C");
			Assertions.assertThat(map.get2("2")).isEmpty();
			Assertions.assertThat(map.get2("3")).containsExactlyInAnyOrder("C");
			Assertions.assertThat(map.get2("4")).containsExactlyInAnyOrder("C");
			return null;
		});
	}
}
