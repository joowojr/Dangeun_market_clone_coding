package com.example.demo.src.Review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Review.Model.GetReviewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewProvider reviewProvider;
    private final ReviewService reviewService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
    }

    // Path Variable
    // 유저에 대한 리뷰 조회
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable long userId){
        try {
            List<GetReviewRes> getReviewRes = reviewProvider.gerReviews(userId);
            return new BaseResponse<>(getReviewRes);
        } catch (BaseException exception ) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
