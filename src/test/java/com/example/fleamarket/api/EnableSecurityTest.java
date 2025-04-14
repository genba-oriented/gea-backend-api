package com.example.fleamarket.api;

import com.example.fleamarket.api.config.SecurityConfig;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@TestPropertySource(properties = "spring.autoconfigure.exclude=")
@Import( SecurityConfig.class )
public @interface EnableSecurityTest {
}
