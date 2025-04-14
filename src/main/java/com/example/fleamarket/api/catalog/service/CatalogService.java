package com.example.fleamarket.api.catalog.service;

import com.example.fleamarket.api.catalog.dto.SellDto;
import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.repository.ProductImageDataRepository;
import com.example.fleamarket.api.sell.repository.ProductImageRepository;
import com.example.fleamarket.api.sell.repository.SellRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CatalogService {
    private final SellRepository sellRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductImageDataRepository productImageDataRepository;

    public Page<SellDto> getSells(String keyword, Pageable pageable) {
        Page<Sell> sellsPage =  this.sellRepository.getByKeywordAndExceptStatus(keyword, List.of(), pageable);
        return new PageImpl<>(
            convertToDtos(sellsPage.getContent()),
            pageable,
            sellsPage.getTotalElements());


    }

    List<SellDto> convertToDtos(List<Sell> sells) {
        List<SellDto> dtos = new ArrayList<>();
        for (Sell sell : sells) {
            dtos.add(convertToDto(sell));
        }
        return dtos;
    }

    SellDto convertToDto(Sell sell) {
        if (sell == null) {
            return null;
        }
        SellDto dto = new SellDto();
        dto.setId(sell.getId());
        dto.setUserId(sell.getUserId());
        dto.setProductName(sell.getProductName());
        dto.setDescription(sell.getDescription());
        dto.setPrice(sell.getPrice());
        dto.setSellDateTime(sell.getSellDateTime());
        if (sell.getStatus() == Sell.Status.NOW_SELLING) {
            dto.setSold(false);
        } else {
            dto.setSold(true);
        }
        dto.setProductImageIds(sell.getProductImages().stream().map(pi -> pi.getId()).collect(Collectors.toList()));
        return dto;
    }

    public ProductImageData getProductImageDataByProductImageId(String productImageId) {
        return this.productImageDataRepository.getByProductImageId(productImageId);
    }

    public SellDto getById(String id) {
        return convertToDto(this.sellRepository.getById(id));
    }

}
