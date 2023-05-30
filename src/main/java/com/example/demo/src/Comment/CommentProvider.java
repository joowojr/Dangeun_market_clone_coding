package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.Comment.model.GetCommentRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
@Service
public class CommentProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    public final CommentDao commentDao;

    public CommentProvider(CommentDao commentDao) {
        this.commentDao = commentDao;
    }

    @Transactional(readOnly = true)
    public List<GetCommentRes> getComments(long postId) throws BaseException {
        try {
            List<GetCommentRes> getCommentRes = commentDao.getComments(postId);
            return getCommentRes;
        }
        catch (Exception exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
