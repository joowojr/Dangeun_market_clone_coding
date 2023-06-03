package com.example.demo.src.Kakao;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.User.UserService;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.utils.ValidationRegex.isRegexPhoneNum;

@RestController
@RequestMapping("")
public class KakaoController {

    @Autowired
    private final KakaoService kakaoService;
    @Autowired
    private final UserService userService;

    public KakaoController(KakaoService kakaoService, UserService userService) {
        this.kakaoService = kakaoService;
        this.userService = userService;
    }

    @ResponseBody
    @GetMapping("")
    public void kakaoCallback(@RequestParam String code) {
        System.out.println(code);
    }

    /**
     * 카카오 액세스 토큰 API
     * [GET] /users/login/kakao
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @GetMapping ("/users/kakao")
    public void getKaKaoAccessToken(@RequestParam String code) {
        kakaoService.getKaKaoAccessToken(code);
    }

    /**
     * 카카오 회원가입 API
     * [GET] /users/kakao
     *
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping("/users/kakao")
    public BaseResponse<PostUserRes> createKakaoUser(@RequestBody PostUserReq postUserReq){
        if(postUserReq.getPhoneNum()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_PHONENUM);
        }
        if(postUserReq.getNickname()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_NICKNAME);
        }
        if (postUserReq.getEmail()==null){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_EMPTY_EMAIL);
        }
        //전화번호 정규표현
        if(!isRegexPhoneNum(postUserReq.getPhoneNum())){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_PHONENUM);
        }
        //이메일 정규표현
        if (!isRegexPhoneNum(postUserReq.getEmail())){
            return new BaseResponse<>(BaseResponseStatus.POST_USERS_INVALID_EMAIL);
        }
        try{
            PostUserRes postUserRes = userService.createUser(postUserReq);
            return new BaseResponse<>(postUserRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }




}
