package com.example.demo;

import com.example.demo.config.BaseException;
import com.example.demo.src.Board.BoardController;
import com.example.demo.src.Board.BoardDao;
import com.example.demo.src.Board.BoardService;
import com.example.demo.src.Board.model.PostBoardImageReq;
import com.example.demo.src.Board.model.PostProductReq;
import com.example.demo.src.Comment.CommentDao;
import com.example.demo.src.Comment.CommentService;
import com.example.demo.src.Comment.model.PostCommentReq;
import com.example.demo.src.User.UserDao;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.utils.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class DemoApplicationTests {
    @Autowired
    CommentService commentService;
    @Autowired
    CommentDao commentDao;
    @Autowired
    BoardDao boardDAO;
    @Autowired
    BoardService boardService;
    @Autowired
    BoardController boardController;
    @Autowired
    UserDao userDao;
    @Autowired
    JwtService jwtService;
    @Test
    void writecomment() throws BaseException {
        System.out.println(
                commentService.writeComment(new PostCommentReq(1,1, null,"test",null,null)));
    }

    @Test
    void writeProduct() {
        List<PostBoardImageReq> list= new ArrayList<PostBoardImageReq>();
        list.add(new PostBoardImageReq("IMG",1));
        System.out.println(boardDAO.writeProduct(new PostProductReq(1,1,1,"title","content",10000, list,1,1,1,"place")));
    }

    @Test
    void like(){
        System.out.println(boardDAO.checkLike(1,1));
        boardController.likeBoard(1,1);
    }

}
