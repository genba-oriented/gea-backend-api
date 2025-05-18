package com.example.fleamarket.api.buy.repository;

import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.entity.Buy_;
import com.example.fleamarket.api.sell.entity.ProductImage_;
import com.example.fleamarket.api.sell.entity.Sell_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.seasar.doma.jdbc.criteria.statement.EntityQueryable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class BuyRepository {
    private final QueryDsl queryDsl;
    private static final Buy_ b = new Buy_();
    private static final Sell_ s = new Sell_();
    private static final ProductImage_ pi = new ProductImage_();

    public void save(Buy buy) {
        this.queryDsl.insert(b).single(buy).execute();
    }


    public List<Buy> getByUserId(String userId) {
        return join()
            .where(cond -> cond.eq(b.userId, userId))
            .orderBy(cond -> cond.desc(b.buyDateTime))
            .fetch();
    }

    private EntityQueryable<Buy> join() {
        return this.queryDsl.from(b)
            .leftJoin(s, on -> on.eq(b.sellId, s.id))
            .associate(b, s, ((buy, sell) -> {
                buy.setSell(sell);
            }))
            .leftJoin(pi, on -> on.eq(s.id, pi.sellId))
            .associate(s, pi, ((sell, productImage) -> {
                if (sell.getProductImages() == null) {
                    sell.setProductImages(new ArrayList<>());
                }
                sell.getProductImages().add(productImage);
            }))
            ;
    }

    public Buy getById(String id) {
        return join().where(cond -> {
            cond.eq(b.id, id);
        }).fetchOne();
    }

    public Buy getBySellId(String sellId) {
        return join().where(cond -> {
            cond.eq(b.sellId, sellId);
        }).fetchOne();
    }


}
