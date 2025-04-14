package com.example.fleamarket.api.review.repository;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.review.entity.Review;
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
@Import({DomaAutoConfiguration.class, ReviewRepository.class})
@Sql("data.sql")
class ReviewRepositoryTest extends AbstractPostgresContainerTest {
    @Autowired
    ReviewRepository reviewRepository;

    @Test
    void getByUserId() {
        List<Review> reviews = this.reviewRepository.getByUserId("u01");
        assertThat(reviews).hasSize(3);
    }


}
