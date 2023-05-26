package com.example.demo.src.Board;

import com.example.demo.config.BaseException;
import com.example.demo.src.Board.model.PostProductReq;
import com.example.demo.src.Board.model.PostProductRes;
import com.example.demo.src.Board.model.PatchLikeBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardDao boardDAO;
    private final BoardProvider boardProvider;
    public BoardService(BoardDao boardDAO, BoardProvider boardProvider) {
        this.boardDAO = boardDAO;
        this.boardProvider = boardProvider;
    }

    public PostProductRes writeProduct(PostProductReq postProductReq) throws BaseException {
        try {
                long boardId = boardDAO.writeProduct(postProductReq);
                return new PostProductRes(boardId);
            }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 관심 누르기
    public PatchLikeBoardRes likeBoard(long boardId, long userId) throws BaseException {
        try {
            return boardDAO.likeBoard(boardId,userId);
        }
        catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
