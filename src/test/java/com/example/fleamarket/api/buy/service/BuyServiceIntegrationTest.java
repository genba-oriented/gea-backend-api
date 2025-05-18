package com.example.fleamarket.api.buy.service;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.entity.Buy_;
import com.example.fleamarket.api.buy.input.BuyInput;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.entity.User_;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("data.sql")
@Transactional
class BuyServiceIntegrationTest extends AbstractPostgresContainerTest {

    @Autowired
    BuyService buyService;

    @Autowired
    QueryDsl queryDsl;

    static final User_ u = new User_();
    static final Buy_ b = new Buy_();

    @Test
    void buy() {
        BuyInput buyInput = new BuyInput();
        buyInput.setSellId("s05");
        User user = new User();
        user.setId("u01");
        user.setBalance(1500);
        Buy buy = this.buyService.buy(buyInput, user);

        User fromDb = this.queryDsl.from(u).where(cond -> cond.eq(u.id, "u01")).fetchOne();
        assertThat(fromDb.getBalance()).isEqualTo(500);

        Buy buyFromDb = this.queryDsl.from(b).where(cond -> cond.eq(b.id, buy.getId())).fetchOne();
        assertThat(buyFromDb.getSellId()).isEqualTo(("s05"));
    }
}
