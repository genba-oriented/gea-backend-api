package com.example.fleamarket.api.review.repository;

import com.example.fleamarket.api.review.entity.Review;
import com.example.fleamarket.api.review.entity.Review_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final QueryDsl queryDsl;

    private static final Review_ r = new Review_();

    public void save(Review review) {
        this.queryDsl.insert(r).single(review).execute();
    }

    public List<Review> getByUserId(String userId) {
        return this.queryDsl.from(r).where(cond -> cond.eq(r.revieweeUserId, userId)).fetch();
    }

}
