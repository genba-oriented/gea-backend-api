package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.entity.Sell_;
import org.junit.jupiter.api.Test;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({ DomaAutoConfiguration.class, SellRepository.class })
@Sql("data.sql")
class SellRepositoryTest extends AbstractPostgresContainerTest {

    @Autowired
    SellRepository sellRepository;
    @Autowired
    QueryDsl queryDsl;
    Sell_ s = new Sell_();


    @Test
    void save() {
        Sell sell = new Sell();
        sell.setId("s99");
        sell.setProductName("pname99");
        sell.setDescription("desc99");
        sell.setSellDateTime(LocalDateTime.of(2025, 2, 2, 10, 10, 10));
        this.sellRepository.save(sell);

        Sell fromDb = this.queryDsl.from(s).where(cond -> cond.eq(s.id, "s99")).fetchOne();
        assertThat(fromDb.getProductName()).isEqualTo("pname99");
    }

    @Test
    void getByUserId() {
        List<Sell> sells = this.sellRepository.getByUserId("u01");
        assertThat(sells).hasSize(2);
        assertThat(sells.get(0).getProductImages()).hasSize(2);
    }

    @Test
    void getByKeyword() {
        PageRequest pageable = PageRequest.of(0, 3);
        Page<Sell> page = this.sellRepository.getByKeywordAndExceptStatus("name", null, pageable);
        assertThat(page.getTotalElements()).isEqualTo(8);
        assertThat(page.getNumberOfElements()).isEqualTo(3);
        assertThat(page.getContent().get(0).getId()).isEqualTo("s09");

        pageable = PageRequest.of(1, 3);
        page = this.sellRepository.getByKeywordAndExceptStatus("name", null, pageable);
        assertThat(page.getContent().get(0).getId()).isEqualTo("s05");
    }
    @Test
    void getByKeywordByNull() {
        PageRequest pageable = PageRequest.of(0, 3);
        Page<Sell> page = this.sellRepository.getByKeywordAndExceptStatus(null, null, pageable);
        assertThat(page.getContent().get(0).getId()).isEqualTo("s10");

    }


}
