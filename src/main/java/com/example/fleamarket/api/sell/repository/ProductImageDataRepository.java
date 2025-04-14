package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.example.fleamarket.api.sell.entity.ProductImageData_;
import com.example.fleamarket.api.sell.entity.ProductImage_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.seasar.doma.jdbc.criteria.NativeSql;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductImageDataRepository {
    private final Entityql entityql;
    private final NativeSql nativeSql;
    private static final ProductImageData_ d = new ProductImageData_();
    private static final ProductImage_ pi = new ProductImage_();


    public void save(ProductImageData productImageData) {
        this.entityql.insert(d, productImageData).execute();
    }

    public void update(ProductImageData productImageData) {
        this.entityql.update(d, productImageData).execute();
    }

    public ProductImageData getByProductImageId(String productImageId) {
        return this.entityql.from(d).leftJoin(pi, on -> on.eq(d.productImageId, pi.id))
            .associate(d, pi, ((data, productImage) -> {
                data.setProductImage(productImage);
            }))
            .where(cond -> cond.eq(d.productImageId, productImageId)).fetchOne();
    }

    public void deleteByProductImageId(String id) {
        this.nativeSql.delete(d).where(cond -> cond.eq(d.productImageId, id)).execute();
    }
}
