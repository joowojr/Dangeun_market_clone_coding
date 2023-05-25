package com.example.demo.src.Board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Board.model.GetLikeBoardRes;
import com.example.demo.src.Board.model.GetBoardRes;
import com.example.demo.src.Board.model.PostProductReq;
import com.example.demo.src.Board.model.PostProductRes;
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
    @PostMapping("/posts")
    public BaseResponse<PostProductRes> writeProduct(@RequestBody PostProductReq postProductReq){
        try {
            PostProductRes postProductRes = boardService.writeProduct(postProductReq);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // Path Variable
    // 포스트 1개 조회
    @ResponseBody
    @GetMapping("/posts/{postId}")
    public BaseResponse<GetBoardRes> getPostByPostId(@PathVariable long postId){
        try {
            GetBoardRes getBoardRes = boardProvider.getPostByPostId(postId);
            return new BaseResponse<>(getBoardRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    // Query string
    // 포스트 조회
    // 카테고리로 조회
    @ResponseBody
    @GetMapping("/posts")
    public BaseResponse<List<GetBoardRes>> getPosts(@RequestParam(required = false) Integer categoryId){
        try {
            if (categoryId==null){
                List<GetBoardRes> getBoardRes = boardProvider.getBoards();
                return new BaseResponse<>(getBoardRes);
            }
            List<GetBoardRes> getBoardRes = boardProvider.getBoardsByCategory(categoryId);
            return new BaseResponse<>(getBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }


    // Path Variable
    // 회원 관심 목록 조회
    @ResponseBody
    @GetMapping("users/{userId}/like")
    public BaseResponse<List<GetLikeBoardRes>> getLikePostList(@PathVariable long userId){
        try {
            List<GetLikeBoardRes> getLikeBoardRes = boardProvider.getLikePostResList(userId);
            return new BaseResponse<>(getLikeBoardRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
