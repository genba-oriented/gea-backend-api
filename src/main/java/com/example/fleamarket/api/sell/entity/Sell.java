package com.example.fleamarket.api.sell.entity;

import com.example.fleamarket.api.user.entity.User;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Transient;

import java.time.LocalDateTime;
import java.util.List;

@Entity(metamodel = @Metamodel)
@Data
public class Sell {
    @Id
    private String id;
    private String userId;
    @Transient
    private User user;
    private String productName;
    private String description;
    private Integer price;
    private LocalDateTime sellDateTime;
    private LocalDateTime editDateTime;
    private LocalDateTime shippedDateTime;
    private LocalDateTime completedDateTime;
    private Status status;
    @Transient
    private List<ProductImage> productImages;

    public enum Status {
        NOW_SELLING,
        NEED_SHIPPING,
        NEED_REVIEW_BY_BUYER,

        NEED_REVIEW_BY_SELLER,
        COMPLETED
    }

}

