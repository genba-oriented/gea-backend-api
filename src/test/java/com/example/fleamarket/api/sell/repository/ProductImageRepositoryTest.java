package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.sell.entity.ProductImage;
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
@Import({ DomaAutoConfiguration.class, ProductImageRepository.class })
@Sql("data.sql")
class ProductImageRepositoryTest extends AbstractPostgresContainerTest {
    @Autowired
    ProductImageRepository productImageRepository;

    @Test
    void save() {
        ProductImage productImage = new ProductImage();
        productImage.setId("pi99");
        productImage.setSellId("s01");
        productImage.setOrder(0);
        this.productImageRepository.save(productImage);

        ProductImage fromDb = this.productImageRepository.getById("pi99");
        assertThat(new String(fromDb.getSellId())).isEqualTo("s01");
    }

}
