package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.sell.entity.ProductImage_;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.entity.Sell_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.criteria.declaration.WhereDeclaration;
import org.seasar.doma.jdbc.criteria.statement.EntityQueryable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static org.seasar.doma.jdbc.criteria.expression.Expressions.count;

@Repository
@RequiredArgsConstructor
public class SellRepository {
    private final QueryDsl queryDsl;
    private static final Sell_ s = new Sell_();
    private static final ProductImage_ pi = new ProductImage_();

    public void save(Sell sell) {
        this.queryDsl.insert(s).single(sell).execute();
    }

    public List<Sell> getByUserId(String userId) {
        return join().where(cond -> {
            cond.eq(s.userId, userId);
        }).fetch();
    }


    public void update(Sell sell) {
        this.queryDsl.update(s).single(sell).execute();
    }

    public Page<Sell> getByKeywordAndExceptStatus(String keyword, List<Sell.Status> statuses, Pageable pageable) {
        Consumer<WhereDeclaration> where = cond -> {
            if (keyword != null && keyword.length() != 0) {
                cond.like(s.productName, "%" + keyword + "%");
            }
            if (statuses != null && statuses.size() > 0) {
                cond.notIn(s.status, statuses);
            }
        };
        Long total = this.queryDsl.from(s).where(where).select(count()).fetchOne();

        List<Sell> sells = join()
            .where(where)
            .offset((int) pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(cond -> {
                cond.desc(s.sellDateTime);
            })
            .fetch();

        return new PageImpl<>(sells, pageable, total);

    }

    EntityQueryable<Sell> join() {
        return this.queryDsl.from(s).leftJoin(pi, on -> on.eq(s.id, pi.sellId))
            .associate(s, pi, (sell, productImage) -> {
                if (sell.getProductImages() == null) {
                    sell.setProductImages(new ArrayList<>());
                }
                sell.getProductImages().add(productImage);
            })        ;
    }

    public Sell getById(String id) {
        return join()
            .where(cond -> {
            cond.eq(s.id, id);
        }).fetchOne();
    }
}
