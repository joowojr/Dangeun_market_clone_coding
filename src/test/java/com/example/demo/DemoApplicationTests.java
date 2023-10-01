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
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class) //Junit5
@SpringBootTest
@ActiveProfiles("test") // 괄호 안에 실행 환경을 명시해준다.
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
    void kakao(){
        kakaoService.getKakaoAccessToken("PKNf4Qku9t45VIZ_jpcEtBVrcjQGau7lmMXAaGSYG-WnDhSuqGw9uyA7oSSHb2AzC9XyVQoqJQ8AAAGIhp4NnQ");
    }
    @Test
    void createKakaoUser() throws BaseException {
        kakaoController.getKaKaoUserInfo("jQKbCdTMcB--xu4zctUPLDUInMdiol-LWsM2NfzznQZxES9PAG3IQmSgXbx1f4LuKRvJ6go9dNoAAAGIhqTI3w");
    }


}
