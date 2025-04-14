package com.example.fleamarket.api.buy.service;

import com.example.fleamarket.api.buy.dto.BuyerDto;
import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.repository.BuyRepository;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class BuyForSellerService {
    private final BuyRepository buyRepository;
    private final UserRepository userRepository;

    public BuyerDto getBuyerBySellIdAndSellerId(String sellId, String sellerUserId) {
        Buy buy = this.buyRepository.getBySellId(sellId);
        if (!sellerUserId.equals(buy.getSell().getUserId())) {
            throw new RuntimeException("指定したuserIdが不正 sellId="+sellId + " sellerUserId="+sellerUserId);
        }
        BuyerDto dto = new BuyerDto();
        dto.setUserId(buy.getUserId());
        User user = this.userRepository.getById(buy.getUserId());
        dto.setUserName(user.getName());
        dto.setShippingAddress(user.getShippingAddress());
        dto.setBuyDateTime(buy.getBuyDateTime());
        return dto;
    }

}
