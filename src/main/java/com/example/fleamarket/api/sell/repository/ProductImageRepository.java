package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.sell.entity.ProductImage;
import com.example.fleamarket.api.sell.entity.ProductImage_;
import com.example.fleamarket.api.sell.entity.Sell_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductImageRepository {
    private final Entityql entityql;
    private final NativeSql nativeSql;
    private static final ProductImage_ pi = new ProductImage_();
    private static final Sell_ s = new Sell_();

    public void save(ProductImage productImage) {
        this.entityql.insert(pi, productImage).execute();

    }

    public ProductImage getById(String id) {
        return this.entityql.from(pi)
            .leftJoin(s, on -> on.eq(pi.sellId, s.id))
            .associate(pi, s, ((productImage, sell) -> {
                productImage.setSell(sell);
            })).where(cond -> cond.eq(pi.id, id)).fetchOne();
    }

    public void update(ProductImage productImage) {
        this.entityql.update(pi, productImage).execute();
    }


    public void delete(String id) {
        this.nativeSql.delete(pi).where(cond -> cond.eq(pi.id, id)).execute();
    }
}
