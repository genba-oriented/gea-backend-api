package com.example.fleamarket.api.sell.controller;

import com.example.fleamarket.api.sell.entity.ProductImage;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.input.SellInput;
import com.example.fleamarket.api.sell.service.SellService;
import com.example.fleamarket.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sell/sells")
@RequiredArgsConstructor
public class SellController {

    private final SellService sellService;

    @GetMapping
    public List<Sell> getAll(@AuthenticationPrincipal User user) {
        return this.sellService.getByUserId(user.getId());
    }

    @GetMapping("/{id}")
    public Sell getSell(@PathVariable String id, @AuthenticationPrincipal User user) {
        return this.sellService.getByIdAndUserId(id, user.getId());
    }


    @PostMapping
    public ResponseEntity<Void> register(@Validated @RequestBody SellInput sellInput, @AuthenticationPrincipal User user) {

        Sell sell = this.sellService.register(sellInput, user.getId());

        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(sell.getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }


    @PostMapping("/{sellId}/product-images")
    public ResponseEntity<Void> registerProductImage(@PathVariable String sellId, @RequestParam MultipartFile file, @AuthenticationPrincipal User user) {
        byte[] data = null;
        try {
            data = file.getBytes();
        } catch(IOException ex) {
            throw new RuntimeException("ファイルアップロード失敗", ex);
        }
        ProductImage productImage = this.sellService.registerProductImage(sellId, user.getId(), data);
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(productImage.getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }
    @PostMapping("/{sellId}/product-images/{id}") //PutにしたいがマルチパートのリクスとなのでPostにする
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateProductImage(@PathVariable String sellId, @PathVariable String id, @AuthenticationPrincipal User user, @RequestParam MultipartFile file) {
        byte[] data = null;
        try {
            data = file.getBytes();
        } catch(IOException ex) {
            throw new RuntimeException("ファイルアップロード失敗", ex);
        }
        this.sellService.updateProductImage(id, user.getId(), data);
    }

    @PutMapping("/{sellId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateSell(@PathVariable String sellId, @Validated @RequestBody SellInput sellInput, @AuthenticationPrincipal User user) {
        this.sellService.update(sellId, user.getId(), sellInput);
    }


    @GetMapping("/{sellId}/product-images/{id}")
    public byte[] getProductImage(@AuthenticationPrincipal User user, @PathVariable String sellId, @PathVariable String id) {
        return this.sellService.getProductImageDataByProductImageIdAndUserId(id, user.getId()).getData();
    }


    @DeleteMapping("/{sellId}/product-images/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProductImage(@AuthenticationPrincipal User user, @PathVariable String sellId, @PathVariable String id) {
        this.sellService.deleteProductImageByIdAndSellIdAndUserId(id, sellId, user.getId());
    }

    @PutMapping("/{sellId}/product-images/reorder")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void reorderProductImages(@RequestBody List<String> ids, @AuthenticationPrincipal User user) {
        this.sellService.reorderProductImages(ids, user.getId());
    }

}
