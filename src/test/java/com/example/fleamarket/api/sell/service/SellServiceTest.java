package com.example.fleamarket.api.sell.service;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.sell.entity.*;
import com.example.fleamarket.api.sell.input.SellInput;
import com.example.fleamarket.api.util.BusinessDateGetter;
import com.example.fleamarket.api.util.IdGenerator;
import org.junit.jupiter.api.Test;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("data.sql")
@Transactional
class SellServiceTest extends AbstractPostgresContainerTest {
    @Autowired
    SellService sellService;
    @Autowired
    QueryDsl queryDsl;
    Sell_ s = new Sell_();
    ProductImageData_ pid = new ProductImageData_();

    @MockitoBean
    BusinessDateGetter businessDateGetter;

    @MockitoBean
    IdGenerator idGenerator;

    @Test
    void register() {
        doReturn("s99").when(idGenerator).generateId();
        LocalDateTime dateTime = LocalDateTime.of(2025, 2,2,10,10,10);
        doReturn(dateTime).when(businessDateGetter).getBusinessDateTime();
        SellInput sellInput = new SellInput();
        sellInput.setProductName("pname99");
        sellInput.setDescription("desc99");
        sellInput.setPrice(999);

        Sell sell = this.sellService.register(sellInput, "u01");

        Sell fromDb = this.queryDsl.from(s).where(cond -> cond.eq(s.id, "s99")).fetchOne();
        assertThat(fromDb.getProductName()).isEqualTo("pname99");
        assertThat(fromDb.getSellDateTime()).isEqualTo(dateTime);

    }

    @Test
    void registerProductImage() {
        doReturn("pi99").when(idGenerator).generateId();
        ProductImage productImage = this.sellService.registerProductImage("s01", "u01", "foo".getBytes());

        ProductImageData fromDb = this.queryDsl.from(pid).where(cond -> cond.eq(pid.productImageId, "pi99")).fetchOne();
        assertThat(fromDb.getData()).isEqualTo("foo".getBytes());
    }
    @Test
    void updateProductImage() {
        this.sellService.updateProductImage("pi01", "u01", "foo".getBytes());

        ProductImageData fromDb = this.queryDsl.from(pid).where(cond -> cond.eq(pid.productImageId, "pi01")).fetchOne();
        assertThat(fromDb.getData()).isEqualTo("foo".getBytes());
    }

}
