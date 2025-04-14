package com.example.fleamarket.api;

import com.example.fleamarket.api.config.AuthenticatedUserAuthentication;
import com.example.fleamarket.api.user.entity.User;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithUserFactory implements WithSecurityContextFactory<WithUser> {

    @Override
    public SecurityContext createSecurityContext(WithUser annotation) {
        User user = new User();
        user.setId(annotation.id());
        user.setName(annotation.name());
        user.setEmail(annotation.email());
        user.setIdpUserId(annotation.idpUserId());
        user.setBalance(annotation.balance());
        user.setShippingAddress(annotation.shippingAddress());
        user.setActivated(annotation.activated());

        AuthenticatedUserAuthentication token = new AuthenticatedUserAuthentication(user);
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(token);
        return context;
    }
}
