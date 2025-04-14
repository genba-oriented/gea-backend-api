package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.example.fleamarket.api.sell.entity.ProductImageData_;
import org.junit.jupiter.api.Test;
import org.seasar.doma.boot.autoconfigure.DomaAutoConfiguration;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;


@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({DomaAutoConfiguration.class, ProductImageDataRepository.class})
@Sql("data.sql")
class ProductImageDataRepositoryTest extends AbstractPostgresContainerTest {
    @Autowired
    ProductImageDataRepository productImageDataRepository;


    @Autowired
    Entityql entityql;
    static final ProductImageData_ pid = new ProductImageData_();

    @Test
    void save() {
        ProductImageData data = new ProductImageData();
        data.setId("pid99");
        data.setProductImageId("pi01");
        data.setData("foo".getBytes());
        this.productImageDataRepository.save(data);

        ProductImageData fromDb = this.entityql.from(pid).where(cond -> cond.eq(pid.id, "pid99")).fetchOne();
        assertThat(fromDb.getData()).isEqualTo("foo".getBytes());
    }

}
