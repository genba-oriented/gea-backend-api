package com.example.fleamarket.api.sell.service;

import com.example.fleamarket.api.sell.entity.ProductImage;
import com.example.fleamarket.api.sell.entity.ProductImageData;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.input.SellInput;
import com.example.fleamarket.api.sell.repository.ProductImageDataRepository;
import com.example.fleamarket.api.sell.repository.ProductImageRepository;
import com.example.fleamarket.api.sell.repository.SellRepository;
import com.example.fleamarket.api.util.BusinessDateGetter;
import com.example.fleamarket.api.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SellService {

    private final SellRepository sellRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductImageDataRepository productImageDataRepository;
    private final IdGenerator idGenerator;
    private final BusinessDateGetter businessDateGetter;

    public Sell register(SellInput sellInput, String userId) {
        Sell sell = new Sell();
        sell.setId(idGenerator.generateId());
        sell.setProductName(sellInput.getProductName());
        sell.setDescription(sellInput.getDescription());
        sell.setPrice(sellInput.getPrice());
        sell.setUserId(userId);
        sell.setStatus(Sell.Status.NOW_SELLING);
        sell.setSellDateTime(this.businessDateGetter.getBusinessDateTime());
        this.sellRepository.save(sell);
        return sell;
    }


    public ProductImage registerProductImage(String sellId, String userId, byte[] data) {
        Sell sell = getByIdAndUserId(sellId, userId);
        ProductImage productImage = new ProductImage();
        productImage.setId(idGenerator.generateId());
        productImage.setSellId(sellId);
        this.productImageRepository.save(productImage);
        ProductImageData productImageData = new ProductImageData();
        productImageData.setId(idGenerator.generateId());
        productImageData.setProductImageId(productImage.getId());
        productImageData.setData(data);
        this.productImageDataRepository.save(productImageData);
        return productImage;
    }

    public List<Sell> getByUserId(String userId) {
        return this.sellRepository.getByUserId(userId);
    }

    public void update(String sellId, String userId, SellInput sellInput) {
        Sell sell = getByIdAndUserId(sellId, userId);
        sell.setProductName(sellInput.getProductName());
        sell.setDescription(sellInput.getDescription());
        sell.setPrice(sellInput.getPrice());
        sell.setEditDateTime(this.businessDateGetter.getBusinessDateTime());
        this.sellRepository.update(sell);
    }

    public void updateProductImage(String productImageId, String userId, byte[] data) {
        ProductImageData productImageData = getProductImageDataByProductImageIdAndUserId(productImageId, userId);
        productImageData.setData(data);
        this.productImageDataRepository.update(productImageData);

    }


    // @PostAuthorize("returnObject.userId == principal.id") //内部メソッドとして利用できないのは微妙
    public Sell getByIdAndUserId(String sellId, String userId) {
        Sell sell = this.sellRepository.getById(sellId);
        if (sell == null || !userId.equals(sell.getUserId())) {
            throw new RuntimeException("該当する出品データがありません sellId=" + sellId + " userId=" + userId);
        }
        return sell;
    }

    public ProductImageData getProductImageDataByProductImageIdAndUserId(String productImageId, String userId) {
        ProductImage productImage = this.productImageRepository.getById(productImageId);
        if (!userId.equals(productImage.getSell().getUserId())) {
            throw new RuntimeException("ユーザIDが異なります login userId=" + userId + " image's userId=" + productImage.getSell().getUserId());
        }
        ProductImageData data = this.productImageDataRepository.getByProductImageId(productImageId);
        return data;
    }

    public void deleteProductImageByIdAndSellIdAndUserId(String id, String sellId, String userId) {
        ProductImage productImage = this.productImageRepository.getById(id);
        if (!userId.equals(productImage.getSell().getUserId())) {
            throw new RuntimeException("ユーザIDが異なります sellId=" + sellId + " login userId=" + userId + " image's userId=" + productImage.getSell().getUserId());
        }
        this.productImageDataRepository.deleteByProductImageId(id);
        this.productImageRepository.delete(productImage.getId());
    }

    public void reorderProductImages(List<String> ids, String userId) {
        for (int i=0; i<ids.size(); i++) {
            ProductImage productImage = this.productImageRepository.getById(ids.get(i));
            if (!userId.equals(productImage.getSell().getUserId())) {
                throw new RuntimeException("ユーザIDが異なります sellId=" + " login userId=" + userId + " image's userId=" + productImage.getSell().getUserId());
            }
            productImage.setOrder(i);
            this.productImageRepository.update(productImage);
        }
    }
}
