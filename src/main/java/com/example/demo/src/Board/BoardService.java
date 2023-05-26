package com.example.demo.src.Board;

import com.example.demo.config.BaseException;
import com.example.demo.src.Board.model.PostLikeBoardRes;
import com.example.demo.src.Board.model.PostProductReq;
import com.example.demo.src.Board.model.PostProductRes;
import com.example.demo.src.Board.model.PatchLikeBoardRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;

@Service
public class BoardService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final BoardDao boardDAO;
    private final BoardProvider boardProvider;
    public BoardService(BoardDao boardDAO, BoardProvider boardProvider) {
        this.boardDAO = boardDAO;
        this.boardProvider = boardProvider;
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

    //관심 누르기
    public PostLikeBoardRes likeBoard (long boardId,long userId) throws BaseException{
        try {
            if(boardDAO.checkLike(boardId,userId)==1){
                //데이터 있으면
                if(boardDAO.getLikeStatus(boardId,userId)=="INACTIVE"){
                    // 이미 취소 눌러진 경우
                   boardDAO.likeBoard(boardId,userId);
                    System.out.println("성공");
                   return new PostLikeBoardRes(boardId,userId);
                }
                System.out.println("라이크 에러");
                throw new BaseException(LIKE_BOARD_ERROR);
            } // 데이터 없으면
            else boardDAO.likeBoard(boardId,userId);
            return new PostLikeBoardRes(boardId,userId);
        }
        catch (Exception exception) {
            System.out.println("체크"+boardDAO.checkLike(boardId,userId)+"상태"+boardDAO.getLikeStatus(boardId,userId));
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 관심 취소
    public PatchLikeBoardRes unlikeBoard(long boardId, long userId) throws BaseException {
        try {
            if(boardDAO.checkLike(boardId,userId)==1){
                //데이터 있으면
                if(boardDAO.getLikeStatus(boardId,userId)=="INACTIVE"){
                    // 이미 취소 눌러진 경우
                    throw new BaseException(UNLIKE_BOARD_ERROR);
                }
                boardDAO.unlikeBoard(boardId,userId);
                return new PatchLikeBoardRes(boardId,userId);
            }  // 데이터 없으면
            else throw new BaseException(UNLIKE_BOARD_ERROR);
        }
        catch (Exception exception){
            System.out.println("체크"+boardDAO.checkLike(boardId,userId)+"상태"+boardDAO.getLikeStatus(boardId,userId));
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
