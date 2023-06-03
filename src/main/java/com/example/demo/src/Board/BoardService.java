package com.example.demo.src.Board;

import com.example.demo.config.BaseException;
import com.example.demo.src.Board.model.*;
import com.example.demo.src.User.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardDao boardDAO;
    private final BoardProvider boardProvider;
    private final UserDao userDao;
    public BoardService(BoardDao boardDAO, BoardProvider boardProvider, UserDao userDao) {
        this.boardDAO = boardDAO;
        this.boardProvider = boardProvider;
        this.userDao = userDao;
    }
    // 중고 상품 게시물 작성
    public PostProductRes writeProduct(PostProductReq postProductReq) throws BaseException {
        try {
                long boardId = boardDAO.writeProduct(postProductReq);
                return new PostProductRes(boardId);
            }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional
    // 중고 상품 게시물 삭제
    public DeleteBoardRes deleteProduct(long boardId) throws BaseException{
        if (boardDAO.checkBoardId(boardId)==0){
            throw  new BaseException(BOARDS_EMPTY_BOARD_ID);
        }
        try {
            boardDAO.deleteProduct(boardId);
            boardDAO.deleteBoard(boardId);
            return new DeleteBoardRes(boardId);
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //관심 누르기
    @Transactional
    public PostLikeBoardRes likeBoard (long boardId,long userId) throws BaseException{
        if (boardDAO.checkBoardId(boardId)==0){
            throw  new BaseException(BOARDS_EMPTY_BOARD_ID);
        }
        if (userDao.checkUserId(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        if (boardDAO.checkLike(1,1)==1){
            throw new BaseException(LIKE_BOARD_ERROR);
        }
        try {
                boardDAO.likeBoard(boardId, userId);
                return new PostLikeBoardRes(boardId, userId);

        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //관심 취소
    @Transactional
    public DeleteLikeBoardRes unlikeBoard(long boardId,long userId) throws BaseException{
        if (boardDAO.checkBoardId(boardId)==0){
            throw  new BaseException(BOARDS_EMPTY_BOARD_ID);
        }
        if (userDao.checkUserId(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        if (boardDAO.checkLike(1,1)==0){
            throw new BaseException(UNLIKE_BOARD_ERROR);
        }
        try {
            boardDAO.unlikeBoard(boardId, userId);
            return new DeleteLikeBoardRes(boardId, userId);
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
