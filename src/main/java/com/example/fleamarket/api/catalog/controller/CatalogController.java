package com.example.fleamarket.api.catalog.controller;

import com.example.fleamarket.api.catalog.dto.SellDto;
import com.example.fleamarket.api.catalog.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/catalog")
@RequiredArgsConstructor
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/sells")
    public Page<SellDto> getSells(@RequestParam(required = false) String keyword, Pageable pageable) {
        return this.catalogService.getSells(keyword, pageable);
    }


    @GetMapping("/sells/{sellId}/product-images/{productImageId}")
    public byte[] getProductImage(@PathVariable String sellId, @PathVariable String productImageId) {
        return this.catalogService.getProductImageDataByProductImageId(productImageId).getData();
    }


    @GetMapping("/sells/{id}")
    public SellDto getSell(@PathVariable String id) {
        return this.catalogService.getById(id);
    }

}
