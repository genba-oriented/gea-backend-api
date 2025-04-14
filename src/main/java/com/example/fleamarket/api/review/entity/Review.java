package com.example.fleamarket.api.review.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;

@Entity(metamodel = @Metamodel)
@Data
public class Review {
    @Id
    private String id;
    private String sellId;
    private Boolean asBuyer;
    private String revieweeUserId;
    private String reviewerUserId;
    private Integer score;
    private String comment;

}
