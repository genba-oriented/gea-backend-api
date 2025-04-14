package com.example.fleamarket.api.review.service;

import com.example.fleamarket.api.buy.entity.Buy;
import com.example.fleamarket.api.buy.repository.BuyRepository;
import com.example.fleamarket.api.buy.service.BuyService;
import com.example.fleamarket.api.review.dto.RatedUserDto;
import com.example.fleamarket.api.review.entity.Review;
import com.example.fleamarket.api.review.exception.AlreadyReviewedException;
import com.example.fleamarket.api.review.input.ReviewInput;
import com.example.fleamarket.api.review.repository.ReviewRepository;
import com.example.fleamarket.api.sell.entity.Sell;
import com.example.fleamarket.api.sell.repository.SellRepository;
import com.example.fleamarket.api.sell.service.SellService;
import com.example.fleamarket.api.user.entity.User;
import com.example.fleamarket.api.user.repository.UserRepository;
import com.example.fleamarket.api.util.IdGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BuyService buyService;
    private final BuyRepository buyRepository;
    private final UserRepository userRepository;
    private final SellService sellService;
    private final SellRepository sellRepository;
    private final IdGenerator idGenerator;

    public Review reviewAsBuyer(ReviewInput reviewInput, User user) {
        Buy buy = this.buyService.getBySellIdAndUserId(reviewInput.getSellId(), user.getId());
        Review review = new Review();
        review.setId(idGenerator.generateId());
        review.setSellId(reviewInput.getSellId());
        review.setAsBuyer(true);
        review.setRevieweeUserId(buy.getSell().getUserId());
        review.setReviewerUserId(user.getId());
        review.setScore(reviewInput.getResult() == ReviewInput.Result.GOOD ? 5 : 1);
        review.setComment(reviewInput.getComment());
        try {
            this.reviewRepository.save(review);
        } catch (DuplicateKeyException ex) {
            throw new AlreadyReviewedException("すでにレビューされています");
        }

        Sell sell = buy.getSell();
        sell.setStatus(Sell.Status.NEED_REVIEW_BY_SELLER);
        this.sellRepository.update(sell);

        return review;
    }

    public Review reviewAsSeller(ReviewInput reviewInput, User user) {
        Sell sell = this.sellService.getByIdAndUserId(reviewInput.getSellId(), user.getId());
        Buy buy = this.buyRepository.getBySellId(reviewInput.getSellId());
        Review review = new Review();
        review.setId(idGenerator.generateId());
        review.setSellId(reviewInput.getSellId());
        review.setAsBuyer(false);
        review.setRevieweeUserId(buy.getUserId());
        review.setReviewerUserId(user.getId());
        review.setScore(reviewInput.getResult() == ReviewInput.Result.GOOD ? 5 : 1);
        review.setComment(reviewInput.getComment());

        try {
            this.reviewRepository.save(review);
        } catch (DuplicateKeyException ex) {
            throw new AlreadyReviewedException("すでにレビューされています");
        }

        sell.setStatus(Sell.Status.COMPLETED);
        this.sellRepository.update(sell);

        return review;
    }

    public RatedUserDto getRatedUser(String userId) {
        List<Review> reviews = this.reviewRepository.getByUserId(userId);
        Double average = reviews.stream().mapToDouble(Review::getScore).average().orElse(Double.NaN);
        if (average.equals(Double.NaN)) {
            average = null;
        } else {
            average = ((double)Math.round(average * 10))/10;
        }
        User user = this.userRepository.getById(userId);
        if (user == null) {
            return null;
        }
        RatedUserDto dto = new RatedUserDto();
        dto.setUserId(userId);
        dto.setUserName(user.getName());
        dto.setAverageScore(average);
        dto.setReviewCount(reviews.size());
        return dto;
    }
}
