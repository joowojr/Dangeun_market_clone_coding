package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.Board.BoardDao;
import com.example.demo.src.Comment.model.GetCommentRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class CommentProvider {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommentDao commentDao;
    private final BoardDao boardDao;

    public CommentProvider(CommentDao commentDao, BoardDao boardDao) {
        this.commentDao = commentDao;
        this.boardDao = boardDao;
    }

    @Transactional(readOnly = true)
    public List<GetCommentRes> getCommentsByUser(long userId) throws BaseException {
        if (boardDao.checkProductId(userId)==0){
            throw  new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            List<GetCommentRes> getCommentRes = commentDao.getCommentsByUser(userId);
            return getCommentRes;
        }
        catch (Exception exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public List<GetCommentRes> getCommentsByBoard(long boardId) throws BaseException {
        if (boardDao.checkProductId(boardId)==0){
            throw  new BaseException(PRODUCTS_EMPTY_BOARD_ID);
        }
        try {
            List<GetCommentRes> getCommentRes = commentDao.getCommentsByBoard(boardId);
            return getCommentRes;
        }
        catch (Exception exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }


}
