package com.example.fleamarket.api.buy.service;

import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.exception.AlreadyBoughtException;
import com.example.fleamarket.api.buy.exception.BalanceShortageException;
import com.example.fleamarket.api.buy.exception.SellByMyselfException;
import com.example.fleamarket.api.buy.input.BuyInput;
import com.example.fleamarket.api.buy.repository.BuyRepository;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.repository.SellRepository;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.repository.UserRepository;
import com.example.fleamarket.api.util.BusinessDateGetter;
import com.example.fleamarket.api.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyService {

    private final SellRepository sellRepository;
    private final BuyRepository buyRepository;
    private final UserRepository userRepository;
    private final IdGenerator idGenerator;
    private final BusinessDateGetter businessDateGetter;


    public Buy buy(BuyInput buyInput, User user) {
        Sell sell = this.sellRepository.getById(buyInput.getSellId());
        if (sell.getUserId().equals(user.getId())) {
            throw new SellByMyselfException("自分で出品した商品なので購入できない");
        }
        if (sell.getStatus() != Sell.Status.NOW_SELLING) {
            throw new AlreadyBoughtException("すでに購入済み");
        }
        if (user.getBalance() < sell.getPrice()) {
            throw new BalanceShortageException("残高が足りない");
        }
        Buy buy = new Buy();
        buy.setId(this.idGenerator.generateId());
        buy.setBuyDateTime(this.businessDateGetter.getBusinessDateTime());
        buy.setUserId(user.getId());
        buy.setSellId(sell.getId());
        this.buyRepository.save(buy);

        sell.setStatus(Sell.Status.NEED_SHIPPING);
        this.sellRepository.update(sell);

        user.setBalance(user.getBalance() - sell.getPrice());
        this.userRepository.update(user);

        return buy;
    }



    public List<Buy> getByUserId(String userId) {
        return this.buyRepository.getByUserId(userId);
    }

    public Buy getByIdAndUserId(String id, String userId) {
        Buy buy = this.buyRepository.getById(id);
        if (!userId.equals(buy.getUserId())) {
            throw new RuntimeException("指定したUserIdが不正 buyId="+id + " userId="+userId);
        }
        return buy;
    }

    public Buy getBySellIdAndUserId(String sellId, String userId) {
        Buy buy = this.buyRepository.getBySellId(sellId);
        if (!userId.equals(buy.getUserId())) {
            throw new RuntimeException("指定したUserIdが不正 sellId="+sellId + " userId="+userId);
        }
        return buy;
    }



}
