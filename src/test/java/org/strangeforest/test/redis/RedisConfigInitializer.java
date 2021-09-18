package org.strangeforest.test.redis;

import org.springframework.boot.test.util.*;
import org.springframework.context.*;

public class RedisConfigInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

	@Override public void initialize(ConfigurableApplicationContext context) {
		TestPropertyValues.of(
			"spring.redis.port=" + RedisExtension.getPort()
		).applyTo(context.getEnvironment());
	}
}
