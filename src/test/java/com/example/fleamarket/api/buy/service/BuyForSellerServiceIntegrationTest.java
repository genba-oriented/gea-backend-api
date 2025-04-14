package com.example.fleamarket.api.buy.service;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.buy.dto.BuyerDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("data.sql")
@Transactional
class BuyForSellerServiceIntegrationTest extends AbstractPostgresContainerTest {
    @Autowired
    BuyForSellerService buyForSellerService;

    @Test
    void getBySellIdAndSellerId() {
        BuyerDto buyerDto = this.buyForSellerService.getBuyerBySellIdAndSellerId("s01", "u01");
        assertThat(buyerDto.getUserId()).isEqualTo("u51");
        assertThat(buyerDto.getShippingAddress()).isEqualTo("address03");

        assertThatThrownBy(() -> {
            this.buyForSellerService.getBuyerBySellIdAndSellerId("s01", "u99");
        }).isInstanceOf(RuntimeException.class);

    }



}
