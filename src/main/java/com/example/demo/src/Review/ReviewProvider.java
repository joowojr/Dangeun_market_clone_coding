package com.example.demo.src.Review;

import com.example.demo.config.BaseException;
import com.example.demo.src.Review.Model.GetReviewRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class ReviewProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewDao reviewDao;
    private final ReviewService reviewService;

    public ReviewProvider(ReviewDao reviewDao, ReviewService reviewService) {
        this.reviewDao = reviewDao;
        this.reviewService = reviewService;
    }


    public List<GetReviewRes> gerReviews(long userId) throws BaseException {
        try {
            List<GetReviewRes> getReviewRes = reviewDao.getReviews(userId);
            return getReviewRes;
        } catch (Exception exception) {
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
