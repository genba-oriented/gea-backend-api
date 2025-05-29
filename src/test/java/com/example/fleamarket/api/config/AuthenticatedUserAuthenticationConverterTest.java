package com.example.fleamarket.api.config;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.EnableSecurityTest;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.entity.User_;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

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
}
