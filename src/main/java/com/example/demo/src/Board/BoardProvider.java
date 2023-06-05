package com.example.demo.src.Board;
import com.example.demo.config.BaseException;
import com.example.demo.src.Board.model.GetLikeBoardRes;
import com.example.demo.src.Board.model.GetProductRes;
import com.example.demo.src.User.UserDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BoardProvider {
    private final BoardDao boardDAO;
    private final UserDao userDao;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BoardProvider(BoardDao boardDAO, UserDao userDao) {
        this.boardDAO = boardDAO;
        this.userDao = userDao;
    }

    // 포스트 1개 조회
    @Transactional(readOnly = true)
    public GetProductRes getProduct(long boardId) throws BaseException {
        if (boardDAO.checkProductId(boardId)==0){
            throw  new BaseException(PRODUCTS_EMPTY_BOARD_ID);
        }
        try {
            GetProductRes getProductRes = boardDAO.getProduct(boardId);
            return getProductRes;
        }
        catch (Exception exception) {
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //중고 게시물 여러개 조회
    @Transactional(readOnly = true)
    public List<GetProductRes> getProductList() throws BaseException{
        try {
            List<GetProductRes> getProductRes = boardDAO.getProductList();
            return getProductRes;
        }
        catch (Exception exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    //중고 게시물 개수 조회
    // 관심 목록 포스트 조회
    @Transactional(readOnly = true)
    public List<GetLikeBoardRes> getLikePostResList(long userId)throws BaseException{
        if (userDao.checkUserId(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            List<GetLikeBoardRes> getLikeBoardRes = boardDAO.getLikePostList(userId);
            return getLikeBoardRes;
        } catch (Exception exception) {
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
