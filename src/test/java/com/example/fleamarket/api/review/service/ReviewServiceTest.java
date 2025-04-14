package com.example.fleamarket.api.review.service;

import com.example.fleamarket.api.AbstractPostgresContainerTest;
import com.example.fleamarket.api.review.dto.RatedUserDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Sql("data.sql")
@Transactional
class ReviewServiceTest extends AbstractPostgresContainerTest {
    @Autowired
    ReviewService reviewService;

    @Test
    void getAverageScore() {
        RatedUserDto dto = this.reviewService.getRatedUser("u01");
        assertThat(dto.getAverageScore()).isEqualTo(3.7);
        assertThat(dto.getReviewCount()).isEqualTo(3);

        dto = this.reviewService.getRatedUser("u99");
        assertThat(dto).isNull();
    }

}
