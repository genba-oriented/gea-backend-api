package com.example.fleamarket.api.user.entity;

import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Table;

@Entity(metamodel = @Metamodel)
@Table(quote = true)
@Data
public class User {
    @Id
    private String id;
    private String name;
    private String email;
    private String idpUserId;
    private Integer balance;
    private String shippingAddress;
    private Boolean activated;
}
