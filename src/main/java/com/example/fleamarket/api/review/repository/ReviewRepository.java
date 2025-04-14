package com.example.fleamarket.api.review.repository;

import com.example.fleamarket.api.review.entity.Review;
import com.example.fleamarket.api.review.entity.Review_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ReviewRepository {
    private final Entityql entityql;
    private final NativeSql nativeSql;

    private static final Review_ r = new Review_();

    public void save(Review review) {
        this.entityql.insert(r, review).execute();
    }

    public List<Review> getByUserId(String userId) {
        return this.entityql.from(r).where(cond -> cond.eq(r.revieweeUserId, userId)).fetch();
    }

}
