package com.example.demo.src.Board;
import com.example.demo.config.BaseException;
import com.example.demo.src.Board.model.GetLikeBoardRes;
import com.example.demo.src.Board.model.GetBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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
    public GetBoardRes getPostByPostId(long postId) throws BaseException {
        try {
            GetBoardRes getBoardRes = boardDAO.getBoard(postId);
            return getBoardRes;
        }
        catch (Exception exception) {
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //포스트 여러개 조회
    public List<GetBoardRes> getBoards() throws BaseException{
        try {
            List<GetBoardRes> getBoardRes = boardDAO.getBoards();
            return getBoardRes;
        }
        catch (Exception exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    //포스트 여러개 조회
    //카테고리로 조회
    public List<GetBoardRes> getBoardsByCategory(Integer categoryId) throws BaseException{
        try {
            List<GetBoardRes> getBoardRes = boardDAO.getBoardsByCategory(categoryId);
            return getBoardRes;
        }
        catch (Exception exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 관심 목록 포스트 조회
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
