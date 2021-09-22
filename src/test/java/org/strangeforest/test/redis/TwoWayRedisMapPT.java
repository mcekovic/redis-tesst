package org.strangeforest.test.redis;

import java.util.stream.*;

import org.junit.jupiter.api.*;

import org.junit.jupiter.params.*;
import org.junit.jupiter.params.provider.*;
import redis.clients.jedis.*;

import static org.assertj.core.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TwoWayRedisMapPT {

	private Jedis jedis;

	private static final int COUNT = 1000;

	@BeforeAll
	public void setUp() {
		jedis = new Jedis("localhost", 6379);
	}

	@ParameterizedTest
	@MethodSource("redisTwoWayMapSource")
	void perfTestRedisTwoWayMap(TwoWayMap map) {
		for (int i = 0; i < COUNT; i++)
			testRedisTwoWayMap(map);
	}

	private void testRedisTwoWayMap(TwoWayMap map) {
		map.put("A", "1", "2");
		map.put("B", "1", "2", "3");
		map.put("C", "1", "2", "3", "4");

		map.remove1("B");
		map.remove2("2");

		assertThat(map.get1("A")).containsExactlyInAnyOrder("1");
		assertThat(map.get1("B")).isEmpty();
		assertThat(map.get1("C")).containsExactlyInAnyOrder("1", "3", "4");
		assertThat(map.get2("1")).containsExactlyInAnyOrder("A", "C");
		assertThat(map.get2("2")).isEmpty();
		assertThat(map.get2("3")).containsExactlyInAnyOrder("C");
		assertThat(map.get2("4")).containsExactlyInAnyOrder("C");
	}

	private Stream<Arguments> redisTwoWayMapSource() {
		return Stream.of(
				Arguments.of(new RedisTwoWayMap(jedis)),
				Arguments.of(new PipedRedisTwoWayMap(jedis)),
				Arguments.of(new TxRedisTwoWayMap(jedis))
		);
	}
}
