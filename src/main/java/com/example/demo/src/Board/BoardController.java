package com.example.demo.src.Board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Board.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping()
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BoardProvider boardProvider;
    @Autowired
    private final BoardService boardService;

    public BoardController(BoardProvider boardProvider, BoardService boardService) {
        this.boardProvider = boardProvider;
        this.boardService = boardService;
    }

    // Request Body
    // 중고 상품 게시물 작성
    @ResponseBody
    @PostMapping("/boards/products")
    public BaseResponse<PostProductRes> writeProduct(@RequestBody PostProductReq postProductReq){
        try {
            PostProductRes postProductRes = boardService.writeProduct(postProductReq);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // Path Variable
    // 중고 상품게시물 1개 조회
    @ResponseBody
    @GetMapping("/boards/products/{boardId}")
    public BaseResponse<GetBoardRes> getProduct(@PathVariable long boardId){
        try {
            GetBoardRes getBoardRes = boardProvider.getProduct(boardId);
            return new BaseResponse<>(getBoardRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // Query string
    // 중고 상품 게시물 조회
    @ResponseBody
    @GetMapping("/boards/products")
    public BaseResponse<List<GetBoardRes>> getProductList(){
        try {

            List<GetBoardRes> getBoardRes = boardProvider.getProductList();
            return new BaseResponse<>(getBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // Path Variable
    // 회원 관심 목록 조회
    @ResponseBody
    @GetMapping("users/{userId}/like")
    public BaseResponse<List<GetLikeBoardRes>> getLikeBoardList(@PathVariable long userId){
        try {
            List<GetLikeBoardRes> getLikeBoardRes = boardProvider.getLikePostResList(userId);
            return new BaseResponse<>(getLikeBoardRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // PathVariable & RequestParam
    // 관심 누르기
    @ResponseBody
    @PostMapping("boards/{boardId}/like")
    public BaseResponse<PostLikeBoardRes> putLikeBoard(@PathVariable long boardId,
                                                        @RequestParam long userId){
        try {
            PostLikeBoardRes postLikeBoardRes = boardService.likeBoard(boardId,userId);
            return new BaseResponse<>(postLikeBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }
    // PathVariable & RequestParam
    // 관심 취소
    @ResponseBody
    @PatchMapping("boards/{boardId}/like")
    public BaseResponse<PatchLikeBoardRes> unlikeBoard(@PathVariable long boardId,
                                                        @RequestParam long userId){
        try {
            PatchLikeBoardRes patchLikeBoardRes = boardService.unlikeBoard(boardId,userId);
            return new BaseResponse<>(patchLikeBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

}
