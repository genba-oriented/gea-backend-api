package com.example.fleamarket.api.config;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.EnableSecurityTest;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.entity.User_;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.criteria.expression.Expressions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EnableSecurityTest
@Sql("data.sql")
@Transactional
class AuthenticatedUserAuthenticationConverterTest extends AbstractPostgresContainerTest {

    @Autowired
    AuthenticatedUserAuthenticationConverter converter;

    @Autowired
    QueryDsl queryDsl;

    static final User_ u = new User_();

    @Test
    void convert() {
        Jwt jwt = Jwt.withTokenValue("dummy").header("dummy", "dummy").subject("idp01").build();
        AuthenticatedUserAuthentication token = this.converter.convert(jwt);
        assertThat(token.getPrincipal().getIdpUserId()).isEqualTo("idp01");
    }


    @Test
    void convert_noUser() {
        Jwt jwt = Jwt.withTokenValue("dummy").header("dummy", "dummy").subject("idp99").build();
        AuthenticatedUserAuthentication token = this.converter.convert(jwt);
        assertThat(token.getPrincipal()).isNotNull();

        User fromDb = this.queryDsl.from(u).where(cond -> cond.eq(u.idpUserId, "idp99")).fetchOne();
        assertThat(fromDb.getIdpUserId()).isEqualTo("idp99");

    }


    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @Sql(value = "/clear.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void convert_concurrentRegister() throws Exception {
        Jwt jwt = Jwt.withTokenValue("dummy").header("dummy", "dummy").subject("idp99").build();
        Callable<AuthenticatedUserAuthentication> callable = () -> {
            AuthenticatedUserAuthentication authentication = this.converter.convert(jwt);
            try {Thread.sleep(100);}catch(Exception ex){}
            return authentication;
        };
        ExecutorService executor = Executors.newFixedThreadPool(2);
        List<Future<AuthenticatedUserAuthentication>> futures = executor.invokeAll(List.of(callable, callable));
        for (Future<AuthenticatedUserAuthentication> future: futures) {
            assertThat(future.get().getPrincipal()).isNotNull();
        }
        long count = this.queryDsl.from(u).where(cond -> cond.eq(u.idpUserId, "idp99")).select(Expressions.count()).fetchOne();
        assertThat(count).isEqualTo(1L);
    }
}
