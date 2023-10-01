package com.example.demo.src.User;

import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.Board.BoardProvider;
import com.example.demo.src.Board.model.GetLikeBoardRes;
import com.example.demo.utils.Page;
import com.example.demo.utils.PageResponse;
import com.example.demo.utils.SmsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.User.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Random;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER_JWT;
import static com.example.demo.utils.ValidationRegex.isRegexPhoneNum;

@RestController
@RequestMapping("/users")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;
    @Autowired
    private final SmsService smsService;
    @Autowired
    private  final BoardProvider boardProvider;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService, SmsService smsService, BoardProvider boardProvider){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
        this.smsService = smsService;
        this.boardProvider = boardProvider;
    }
    /**
     * 인증번호 문자 API
     * [POST] /users/login/send-sms
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login/send-sms") String sendSms(@RequestParam String phoneNum){
        // 인증번호 생성
        Random rand  = new Random();
        String certNum = "";
        for(int i=0; i<4; i++) {
            String ran = Integer.toString(rand.nextInt(10));
            certNum+=ran;
        }
        //문자 전송, 인증번호 리턴
        this.smsService.sendSms(phoneNum,certNum);
        return certNum;
    }
    /**
     * 로그인 API
     * [POST] /users/login
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<PostLoginRes> login(@RequestParam String phoneNum){
        //전화번호 정규표현
        if(!isRegexPhoneNum(phoneNum)){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_PHONENUM);
        }
        try{
            PostLoginRes postLoginRes = userService.login(phoneNum);
            return new BaseResponse<>(postLoginRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 프로필 조회 API
     * [GET] /users/:userId
     * @return BaseResponse<GetUserRes>
     */
    @ResponseBody
    @GetMapping("/{userId}")
    public BaseResponse<GetUserRes> getUserById(@PathVariable(value = "userId") long userId){
        try{
            GetUserRes getUserRes = userProvider.getUserById(userId);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 목록 조회 API, 페이징 처리
     * [GET] /users
     * @return PageResponse<Page<List<GetUserRes>>>
     */
    @ResponseBody
    @GetMapping("")
    public PageResponse<Page<List<GetUserRes>>> getUsers(@RequestParam(required = false) Integer size, @RequestParam(required = false) Integer currentPage){
        int s=0; // size
        int cp=0; // currentPage
        if (size==null) {
            s = 10;
        }else s=size;
        if (currentPage==null){
            cp = 1;
        }else cp = currentPage;
        try{
            PageResponse<Page<List<GetUserRes>>> result = new PageResponse<>(userProvider.getUsers(s,cp));
            return result;
        } catch(BaseException exception){
            return new PageResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원 1명 배지목록 조회 API
     * [GET] /users/:userId/badges
     * @return BaseResponse<List<GetUserBadgeRes>>
     */
    @ResponseBody
    @GetMapping("/{userId}/badges")
    public BaseResponse<List<GetUserBadgeRes>> getUserBadgeList(@PathVariable long userId){
        try{
            List<GetUserBadgeRes> getUserRes = userProvider.getUserBadgeList(userId);
            return new BaseResponse<>(getUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 회원가입 API
     * [POST] /users
     * @return BaseResponse<PostUserRes>
     */
    @ResponseBody
    @PostMapping ("")
    public BaseResponse<PostUserRes> createUser(@RequestBody PostUserReq postUserReq){
        if(postUserReq.getPhoneNum()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_PHONENUM);
        }
        if(postUserReq.getNickname()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_NICKNAME);
        }
        //전화번호 정규표현
        if(!isRegexPhoneNum(postUserReq.getPhoneNum())){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_PHONENUM);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
       }
    }
    /**
     * 프로필 수정 API
     * [PATCH] /users/:userId
     * @return BaseResponse<String>
     */
    @ResponseBody
    @PatchMapping("/me/profile")
    public BaseResponse<PatchUserRes> modifyProfile(@PathVariable long userId, @RequestBody PatchUserReq user){
        try {
            //jwt에서 idx 추출.
            long userIdByJwt = jwtService.getUserId();
            //userIdx와 접근한 유저가 같은지 확인
            if(userId != userIdByJwt){
                return new BaseResponse<>(INVALID_USER_JWT);
            }
            //같다면 유저네임 변경
            PatchUserReq patchUserReq = new PatchUserReq(userId,user.getNickname(),user.getProfileImg());
            userService.modifyProfile(patchUserReq);
            PatchUserRes patchUserRes = new PatchUserRes(userId);
            return new BaseResponse<>(patchUserRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     * 유저 관심 목록 조회 API
     * [POST] users/:userId/like
     * @return BaseResponse<GetLikeBoardRes>
     */
    @ResponseBody
    @GetMapping("/{userId}/like")
    public BaseResponse<List<GetLikeBoardRes>> getLikeBoardList(@PathVariable long userId){
        try {
            List<GetLikeBoardRes> getLikeBoardRes = boardProvider.getLikePostResList(userId);
            return new BaseResponse<>(getLikeBoardRes);
        }
        catch (BaseException exception){
            return new BaseResponse<>(exception.getStatus());
        }
    }


}
