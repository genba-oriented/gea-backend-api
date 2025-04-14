package com.example.fleamarket.api.catalog.service;

import com.example.fleamarket.api.catalog.dto.SellDto;
import com.example.fleamarket.api.sell.entity.ProductImage;
import com.example.fleamarket.api.sell.entity.Sell;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CatalogServiceTest {
    @InjectMocks
    CatalogService catalogService;

    @Test
    void convertToDto() {
        Sell sell = new Sell();
        sell.setProductImages(List.of(new ProductImage()));
        sell.setStatus(Sell.Status.NOW_SELLING);
        SellDto sellDto = this.catalogService.convertToDto(sell);
        assertThat(sellDto.getSold()).isFalse();

        sell.setStatus(Sell.Status.NEED_REVIEW_BY_BUYER);
        sellDto = this.catalogService.convertToDto(sell);
        assertThat(sellDto.getSold()).isTrue();

    }

}
