package com.example.fleamarket.api.sell.entity;

import lombok.Data;
import org.seasar.doma.*;

@Entity(metamodel = @Metamodel)
@Data
public class ProductImage {
    @Id
    private String id;
    private String sellId;
    @Column(quote = true)
    private Integer order;
    @Transient
    private Sell sell;
}
