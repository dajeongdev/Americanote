package com.coffee.americanote.review.service;

import com.coffee.americanote.cafe.domain.entity.Cafe;
import com.coffee.americanote.cafe.repository.CafeRepository;
import com.coffee.americanote.review.domain.ReviewRequest;
import com.coffee.americanote.review.domain.entity.Review;
import com.coffee.americanote.review.repository.ReviewRepository;
import com.coffee.americanote.user.domain.entity.User;
import com.coffee.americanote.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final CafeRepository cafeRepository;

    @Transactional
    public void saveReview(List<ReviewRequest> reviewRequests) {
        List<User> users = userRepository.findAll();
        List<Review> reviews = new ArrayList<>();
        for (ReviewRequest request : reviewRequests) {
            int userIdx = new Random().nextInt(users.size()) + 1;
            User user = users.get(userIdx);
            Cafe cafe = cafeRepository.findById(request.cafeId()).get();
            Review review = Review.builder()
                    .cafe(cafe)
                    .user(user)
                    .star(request.star())
                    .content(request.contents())
                    .build();
            reviews.add(review);
        }
        reviewRepository.saveAll(reviews);
    }
}
