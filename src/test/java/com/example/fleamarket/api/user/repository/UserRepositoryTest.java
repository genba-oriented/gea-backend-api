package com.example.fleamarket.api.user.repository;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.user.entity.User;
import org.junit.jupiter.api.Test;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ DomaAutoConfiguration.class, UserRepository.class })
@Sql("data.sql")
class UserRepositoryTest extends AbstractPostgresContainerTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void getByIdpUserId() {
        User user = this.userRepository.getByIdpUserId("idp01");
        assertThat(user.getName()).isEqualTo("uname01");
    }

}
