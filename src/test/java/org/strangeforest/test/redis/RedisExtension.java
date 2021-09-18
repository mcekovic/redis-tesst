package org.strangeforest.test.redis;

import java.util.*;

import org.junit.jupiter.api.extension.*;
import org.testcontainers.containers.*;

public class RedisExtension implements TestInstancePostProcessor {

	private static SharedGenericContainer container;

	private static final int REDIS_PORT = 6379;

	public static synchronized SharedGenericContainer getContainer() {
		if (container == null) {
			container = new SharedGenericContainer("redis")
				.withExposedPorts(REDIS_PORT);
			container.start();
		}
		return container;
	}

	public static int getPort() {
		return getContainer().getMappedPort(REDIS_PORT);
	}

	private static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(RedisExtension.class);

	@Override public void postProcessTestInstance(Object o, ExtensionContext context) {
		var store = context.getRoot().getStore(NAMESPACE);
		store.getOrComputeIfAbsent("RedisContainer", k -> getContainer());
	}

	static class SharedGenericContainer extends GenericContainer<SharedGenericContainer> implements ExtensionContext.Store.CloseableResource {

		public SharedGenericContainer(String dockerImageName) {
			super(dockerImageName);
		}

		@Override public void close() {
			super.close();
		}
	}
}
