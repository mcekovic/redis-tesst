package org.strangeforest.test.redis;

import java.util.*;

public interface TwoWayMap {

	Set<String> get1(String key1);
	Set<String> get2(String key2);
	void put(String key1, String... keys2);
	void remove1(String key1);
	void remove2(String key2);
}
