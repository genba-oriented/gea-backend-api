package com.example.fleamarket.api.user.repository;

import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.entity.User_;
import lombok.RequiredArgsConstructor;
import org.seasar.doma.jdbc.criteria.Entityql;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final Entityql entityql;
    private static final User_ u = new User_();

    public User getById(String id) {
        return this.entityql.from(u).where(cond -> {
            cond.eq(u.id, id);
        }).fetchOne();
    }


    public User getByIdpUserId(String idpUserId) {
        return this.entityql.from(u).where(cond -> {
            cond.eq(u.idpUserId, idpUserId);
        }).fetchOne();
    }

    public void save(User user) {
        this.entityql.insert(u, user).execute();
    }

    public void update(User user) {
        this.entityql.update(u, user).execute();
    }
}
