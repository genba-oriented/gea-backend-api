package com.example.fleamarket.api.sell.repository;

import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.example.fleamarket.api.sell.entity.ProductImageData_;
import com.example.fleamarket.api.sell.entity.ProductImage_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.QueryDsl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ProductImageDataRepository {
    private final QueryDsl queryDsl;
    private static final ProductImageData_ d = new ProductImageData_();
    private static final ProductImage_ pi = new ProductImage_();


    public void save(ProductImageData productImageData) {
        this.queryDsl.insert(d).single(productImageData).execute();
    }

    public void update(ProductImageData productImageData) {
        this.queryDsl.update(d).single(productImageData).execute();
    }

    public ProductImageData getByProductImageId(String productImageId) {
        return this.queryDsl.from(d).leftJoin(pi, on -> on.eq(d.productImageId, pi.id))
            .associate(d, pi, ((data, productImage) -> {
                data.setProductImage(productImage);
            }))
            .where(cond -> cond.eq(d.productImageId, productImageId)).fetchOne();
    }

    public void deleteByProductImageId(String id) {
        this.queryDsl.delete(d).where(cond -> cond.eq(d.productImageId, id)).execute();
    }
}
