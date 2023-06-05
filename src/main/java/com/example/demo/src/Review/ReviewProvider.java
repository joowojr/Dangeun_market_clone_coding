package com.example.demo.src.Review;

import com.example.demo.config.BaseException;
import com.example.demo.src.Review.Model.GetReviewRes;
import com.example.demo.src.User.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.USERS_EMPTY_USER_ID;

@Service
public class ReviewProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ReviewDao reviewDao;
    private final UserDao userDao;

    public ReviewProvider(ReviewDao reviewDao, UserDao userDao) {
        this.reviewDao = reviewDao;
        this.userDao = userDao;
    }

    @Transactional(readOnly = true)
    public List<GetReviewRes> gerReviews(long userId) throws BaseException {
        if (userDao.checkUserId(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            List<GetReviewRes> getReviewRes = reviewDao.getReviews(userId);
            return getReviewRes;
        } catch (Exception exception) {
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
