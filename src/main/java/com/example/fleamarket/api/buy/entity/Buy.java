package com.example.fleamarket.api.buy.entity;

import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.user.entity.User;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.Id;
import org.seasar.doma.Metamodel;
import org.seasar.doma.Transient;

import java.time.LocalDateTime;

@Entity(metamodel = @Metamodel)
@Data
public class Buy {
    @Id
    private String id;
    private String sellId;
    @Transient
    private Sell sell;
    private String userId;
    @Transient
    private User user;
    private LocalDateTime buyDateTime;
}
