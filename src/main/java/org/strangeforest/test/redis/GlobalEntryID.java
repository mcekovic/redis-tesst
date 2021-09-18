package org.strangeforest.test.redis;

import java.io.*;

import redis.clients.jedis.*;

public final class GlobalEntryID extends StreamEntryID {

	public static final StreamEntryID MIN_ENTRY = new StreamEntryID() {
		@Serial private static final long serialVersionUID = 1L;
		public String toString() {
			return "-";
		}
	};

	public static final StreamEntryID MAX_ENTRY = new StreamEntryID() {
		@Serial private static final long serialVersionUID = 1L;
		public String toString() {
			return "+";
		}
	};
}
