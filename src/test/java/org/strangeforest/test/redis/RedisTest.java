package org.strangeforest.test.redis;

import java.lang.annotation.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.*;
import org.springframework.test.context.*;

import static org.junit.jupiter.api.TestInstance.*;

@ExtendWith(RedisExtension.class)
@ContextConfiguration(initializers = RedisConfigInitializer.class)
@TestInstance(Lifecycle.PER_CLASS)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface RedisTest {}
