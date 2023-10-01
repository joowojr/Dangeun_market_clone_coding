package com.example.demo.src.Kakao;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.User.UserService;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.config.BaseResponseStatus.KAKAO_CODE_EMPTY;
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

    /**
     * 카카오 유저 정보 받는 API
     * [GET] /users/signup/kakao
     * @return BaseResponse<PostLoginRes>
     */
    @ResponseBody
    @PostMapping ("/users/signup/kakao")
    public BaseResponse<String> getKaKaoUserInfo(@RequestParam String code) {
        if (code==null)return new BaseResponse<>(KAKAO_CODE_EMPTY);
        try {
            String token = kakaoService.getKakaoAccessToken(code);
            return new BaseResponse<>(kakaoService.getKakaoUserInfo(token));
        }
        catch (BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 카카오 로그인 API
     * [GET] /users/login/kakao
     * @return BaseResponse<PostLoginRes>
     */



}
