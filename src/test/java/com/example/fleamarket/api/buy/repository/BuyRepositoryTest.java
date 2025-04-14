package com.example.fleamarket.api.buy.repository;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.sell.entity.Sell;
import org.junit.jupiter.api.Test;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DomaAutoConfiguration.class, BuyRepository.class})
@Sql("data.sql")
class BuyRepositoryTest extends AbstractPostgresContainerTest {
    @Autowired
    BuyRepository buyRepository;

    @Test
    void getByUserId() {
        List<Buy> buys = this.buyRepository.getByUserId("u51");
        assertThat(buys).hasSize(3);
        assertThat(buys.get(0).getId()).isEqualTo("b02");
        assertThat(buys.get(0).getSell().getStatus()).isEqualTo(Sell.Status.NEED_SHIPPING);
    }

    @Test
    void getById() {
        Buy buy = this.buyRepository.getById("b01");
        assertThat(buy.getSell().getStatus()).isEqualTo(Sell.Status.COMPLETED);
    }
}
