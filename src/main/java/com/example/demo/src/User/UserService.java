package com.example.demo.src.User;



import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.User.model.PatchUserReq;
import com.example.demo.src.User.model.PostUserReq;
import com.example.demo.src.User.model.PostUserRes;
import com.example.demo.utils.JwtService;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    //POST
    @Transactional
    public PostUserRes createUser(@NotNull PostUserReq postUserReq) throws BaseException {
        // 전화번호 중복
        if (userProvider.checkPhoneNum(postUserReq.getPhoneNum())==1){
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_PHONENUM);
        }
        // 닉네임 중복
        if (userProvider.checkNickname(postUserReq.getNickname())==1){
            throw new BaseException(BaseResponseStatus.POST_USERS_EXISTS_NICKNAME);
        }
        try{
            long userId = userDao.createUser(postUserReq);
            //jwt 발급.
            String jwt = jwtService.createJwt(userId);
            return new PostUserRes(jwt,userId);
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional
    public void modifyProfile(PatchUserReq patchUserReq) throws BaseException {
        try{
            //닉네임 수정
            int nicknameResult = userDao.modifyNickname(patchUserReq);
            if(nicknameResult == 0){
                throw new BaseException(MODIFY_FAIL_NICKNAME);
            }
            //프로필 사진 수정
            int profileImgResult = userDao.modifyProfileImg(patchUserReq);
            if(profileImgResult == 0){
                throw new BaseException(MODIFY_FAIL_PROFILEIMG);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
