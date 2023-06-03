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
import com.example.demo.src.Kakao.KakaoController;
import com.example.demo.src.Kakao.KakaoService;
import com.example.demo.src.User.UserDao;
import com.example.demo.src.User.UserProvider;
import com.example.demo.src.User.UserService;
import com.example.demo.utils.JwtService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
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
    @Autowired
    KakaoController kakaoController;
    @Autowired
    KakaoService kakaoService;
    @Autowired
    UserProvider userProvider;
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

    @Test
    void kakaotest(){
        kakaoController.kakaoCallback("d");
    }

    @Test
    void kakao(){
        kakaoService.getKaKaoAccessToken("4aOJCDNq3gI1wPPPpYzj-QlBbUbtoQ8SiGkTFVfkYT69PFX5ZDAWA2c2Wl1GixnoCmWuhAo9dNoAAAGIdDvB9w");
    }
    @Test
    void createKakaoUser() throws BaseException {
        kakaoService.getKakaoUserInfo("lfyXuPO6uOnlcAYz_LlqW7sZ1duFT-rqhubwEMMKCj11GQAAAYh0QXLg");
    }


}
