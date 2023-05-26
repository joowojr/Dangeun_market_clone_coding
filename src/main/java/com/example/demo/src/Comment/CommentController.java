package com.example.demo.src.Comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.Comment.model.DeleteCommentRes;
import com.example.demo.src.Comment.model.GetCommentRes;
import com.example.demo.src.Comment.model.PostCommentReq;
import com.example.demo.src.Comment.model.PostCommentRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/living")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    public CommentController(CommentProvider commentProvider, CommentService commentService) {
        this.commentProvider = commentProvider;
        this.commentService = commentService;
    }
    // Path variable
    // 게시물 1개 댓글 조회
    @ResponseBody
    @GetMapping("/{boardId}/comments")
    public BaseResponse<List<GetCommentRes>> getComments(@PathVariable long boardId){
        try {
            List<GetCommentRes> getCommentRes = commentProvider.getComments(boardId);
            return new BaseResponse<>(getCommentRes);
        }
        catch (BaseException exception){
            // Logger를 이용하여 에러를 로그에 기록한다
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    // Request Body
    // 댓글 작성
    @ResponseBody
    @PostMapping("/{boardId}/comments")
    public BaseResponse<PostCommentRes> writeComment(@RequestBody PostCommentReq postCommentReq){
        try{
            PostCommentRes postCommentRes = commentService.writeComment(postCommentReq);
            return new BaseResponse<>(postCommentRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    //Path Variable
    // 댓글 삭제
    @ResponseBody
    @DeleteMapping("/{boardId}/comments/{commentId}")
    public BaseResponse<DeleteCommentRes> deleteComment(@PathVariable long boardId, @PathVariable long commentId){
        try {
            DeleteCommentRes deleteCommentRes = new DeleteCommentRes(commentService.deleteComment(commentId), boardId);
            return new BaseResponse<>(deleteCommentRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

}
