package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.Comment.model.PostCommentReq;
import com.example.demo.src.Comment.model.PostCommentRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.COMMENT_EMPTY_COMMENT_ID;
import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class CommentService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CommentDao commentDao;
    private final CommentProvider commentProvider;

    public CommentService(CommentDao commentDao, CommentProvider commentProvider) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
    }

    @Transactional
    public PostCommentRes writeComment(PostCommentReq postUserReq) throws BaseException {
        long commentId = commentDao.writeComment(postUserReq);
        return new PostCommentRes(commentId,postUserReq.getPostId());
    }

    @Transactional
    public long deleteComment(long commentId) throws BaseException{
        if (commentDao.checkCommentId(commentId)==0){
            throw  new BaseException(COMMENT_EMPTY_COMMENT_ID);
        }
        try {
            return commentDao.deleteComment(commentId);
        }
        catch (Exception exception){
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
