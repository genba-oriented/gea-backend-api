package com.example.fleamarket.api.sell.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Transient;

@Entity(metamodel = @Metamodel)
@Data
public class ProductImageData {
    @Id
    private String id;
    private String productImageId;
    private byte[] data;
    @Transient
    private ProductImage productImage;

}
