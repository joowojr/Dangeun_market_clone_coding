package com.example.demo.src.Review;

import com.example.demo.config.BaseException;
import com.example.demo.src.User.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class ReviewService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewDao reviewDao;
    private final ReviewProvider reviewProvider;
    private final UserDao userDao;

    public ReviewService(ReviewDao reviewDao, ReviewProvider reviewProvider, UserDao userDao) {
        this.reviewDao = reviewDao;
        this.reviewProvider = reviewProvider;
        this.userDao = userDao;
    }

    @Transactional
    public long modifyReview(long reviewId, String content) throws BaseException {
        if (reviewDao.checkReviewId(reviewId)==0){
            throw  new BaseException(REVIEWS_EMPTY_REVIEW_ID);
        }
        try {
            return reviewDao.modifyReview(reviewId,content);
        }
        catch (Exception exception){
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
