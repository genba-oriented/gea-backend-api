package com.example.fleamarket.api.shipping.service;

import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.repository.SellRepository;
import com.example.fleamarket.api.sell.service.SellService;
import com.example.fleamarket.api.shipping.exception.AlreadyShippedException;
import com.example.fleamarket.api.util.BusinessDateGetter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ShippingService {

    private final SellService sellService;
    private final SellRepository sellRepository;
    private final BusinessDateGetter businessDateGetter;

    public void shipped(String sellId, String userId) {
        Sell sell = this.sellService.getByIdAndUserId(sellId, userId);
        if (sell.getStatus() != Sell.Status.NEED_SHIPPING) {
            throw new AlreadyShippedException("すでに発送済みです");
        }
        sell.setShippedDateTime(this.businessDateGetter.getBusinessDateTime());
        sell.setStatus(Sell.Status.NEED_REVIEW_BY_BUYER);
        this.sellRepository.update(sell);
    }
}
