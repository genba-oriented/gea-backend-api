package com.example.fleamarket.api;

import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserFactory.class)
public @interface WithUser {
    String id() default "";

    String name() default  "";
    String email() default  "";
    String idpUserId() default  "";
    int balance() default  0;
    String shippingAddress() default  "";

    boolean activated() default true;
}
