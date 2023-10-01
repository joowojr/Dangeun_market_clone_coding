package com.example.demo.src.Board;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Board.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

@RestController
@RequestMapping("/boards")
public class BoardController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final BoardProvider boardProvider;
    @Autowired
    private final BoardService boardService;
    @Autowired
    private final BoardDao boardDao;

    public BoardController(BoardProvider boardProvider, BoardService boardService, BoardDao boardDao) {
        this.boardProvider = boardProvider;
        this.boardService = boardService;
        this.boardDao = boardDao;
    }

    /**
     * 중고 게시물 작성 API
     * [POST] /boards/products
     * @return BaseResponse<PostProductRes>
     */
    @ResponseBody
    @PostMapping("/products")
    public BaseResponse<PostProductRes> writeProduct(@RequestBody PostProductReq postProductReq){
        if (postProductReq.getTitle()==null || postProductReq.getTitle().isBlank()){
            return new BaseResponse<>(POST_BOARDS_EMPTY_TITLE);
        }
        if (postProductReq.getContent()==null || postProductReq.getContent().isBlank()){
            return new BaseResponse<>(POST_BOARDS_EMPTY_CONTENT);
        }
        if (postProductReq.getImages()==null || postProductReq.getImages().isEmpty()){
            return new BaseResponse<>(POST_BOARDS_EMPTY_IMAGE);
        }
        try {
            PostProductRes postProductRes = boardService.writeProduct(postProductReq);
            return new BaseResponse<>(postProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고 게시물 1개 조회 API
     * [POST] /boards/products/:boardId
     * @return BaseResponse<GetProductRes>
     */
    @ResponseBody
    @GetMapping("/products/{boardId}")
    public BaseResponse<GetProductRes> getProduct(@PathVariable long boardId){
        try {
            GetProductRes getProductRes = boardProvider.getProduct(boardId);
            return new BaseResponse<>(getProductRes);

        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 중고 게시물 목록 조회 API
     * [POST] /boards/products
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @GetMapping("/boards/products")
    public BaseResponse<List<GetProductRes>> getProductList(){
        try {
            List<GetProductRes> getProductRes = boardProvider.getProductList();
            return new BaseResponse<>(getProductRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }
    /**
     * 중고 게시물 삭제 API
     * [POST] /boards/products/:boardId
     * @return BaseResponse<List<GetProductRes>>
     */
    @ResponseBody
    @DeleteMapping("/products/{boardId}")
    public BaseResponse<DeleteBoardRes> deleteProduct(@PathVariable Long boardId){
        if (boardId==null){
            return new BaseResponse<>(PRODUCTS_EMPTY_BOARD_ID);
        }
        try {
            DeleteBoardRes deleteBoardRes = boardService.deleteProduct(boardId);
            return new BaseResponse<>(deleteBoardRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }

    /**
     * 관심 API
     * [POST] /boards/:boardId/likes
     * @return BaseResponse<PostLikeBoardRes>
     */
    @ResponseBody
    @PostMapping("/{boardId}/likes")
    public BaseResponse<PostLikeBoardRes> likeBoard(@PathVariable long boardId,
                                                        @RequestParam long userId){
        try {
            PostLikeBoardRes postLikeBoardRes = boardService.likeBoard(boardId,userId);
            return new BaseResponse<>(postLikeBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }

    }

    /**
     * 관심 취소 API
     * [POST] /boards/:boardId/likes
     * @return BaseResponse<DeleteLikeBoardRes>
     */
    @ResponseBody
    @DeleteMapping("/{boardId}/likes")
    public BaseResponse<DeleteLikeBoardRes> unlike(@PathVariable long boardId,
                                                   @RequestParam long userId) {
        try {
            DeleteLikeBoardRes deleteLikeBoardRes = boardService.unlikeBoard(boardId,userId);
            return new BaseResponse<>(deleteLikeBoardRes);
        } catch (BaseException exception) {
            return new BaseResponse<>(exception.getStatus());
        }
    }

}
