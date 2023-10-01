package com.example.demo.src.Review;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Review.Model.GetReviewRes;
import com.example.demo.src.Review.Model.PatchReviewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
public class ReviewController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewProvider reviewProvider;
    private final ReviewService reviewService;

    public ReviewController(ReviewProvider reviewProvider, ReviewService reviewService) {
        this.reviewProvider = reviewProvider;
        this.reviewService = reviewService;
    }

    /**
     * 유저에 대한 리뷰 조회 API
     * [POST] /reviews/:reviewId
     * @return BaseResponse<GetProductRes>
     */
    @ResponseBody
    @GetMapping("/reviews/{revieweeId}")
    public BaseResponse<List<GetReviewRes>> getReviews(@PathVariable long revieweeId){
        try {
            List<GetReviewRes> getReviewRes = reviewProvider.gerReviews(revieweeId);
            return new BaseResponse<>(getReviewRes);
        } catch (BaseException exception ) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 리뷰 수정 API
     * [PATCH] /boards/products/:boardId
     * @return BaseResponse<GetProductRes>
     */
    @ResponseBody
    @PatchMapping("/reviews/{reviewId}")
    public BaseResponse<PatchReviewRes> modifyReview(@PathVariable long reviewId, @RequestBody String content){
        try {
            PatchReviewRes patchReviewRes = new PatchReviewRes(reviewService.modifyReview(reviewId,content));
            return new BaseResponse<>(patchReviewRes);
        } catch (BaseException exception ) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
