package com.example.demo.src.Board;
import com.example.demo.config.BaseException;
import com.example.demo.src.Board.model.GetLikeBoardRes;
import com.example.demo.src.Board.model.GetProductRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
@Service
public class BoardProvider {
    private final BoardDao boardDAO;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    public BoardProvider(BoardDao boardDAO) {
        this.boardDAO = boardDAO;
    }

    // 포스트 1개 조회
    @Transactional(readOnly = true)
    public GetProductRes getProduct(long boardId) throws BaseException {
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
    // 관심 목록 포스트 조회
    @Transactional(readOnly = true)
    public List<GetLikeBoardRes> getLikePostResList(long postId)throws BaseException{
        try {
            List<GetLikeBoardRes> getLikeBoardRes = boardDAO.getLikePostList(postId);
            return getLikeBoardRes;
        } catch (Exception exception) {
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
