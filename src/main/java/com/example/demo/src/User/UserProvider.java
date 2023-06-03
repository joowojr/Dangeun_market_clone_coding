package com.example.demo.src.User;


import com.example.demo.config.BaseException;
import com.example.demo.src.User.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }


    @Transactional(readOnly = true)
    // 유저 1명 프로필 정보 조회
    public GetUserRes getUserById(long userId) throws BaseException{
        if (userDao.checkUserId(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try{
            GetUserRes getUserRes = userDao.getUserById(userId);
            return getUserRes;
        }
        catch (Exception exception) {
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional
    public Page<List<GetUserRes>> getUsers(int size, int currentPage) throws BaseException {
        try {
            Page<List<GetUserRes>> result = new Page<>(currentPage,size,userDao.getUsersTotal());
            result.setData(userDao.getUsers(size,result.getStart()));
            return result;
        }catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }

    }
    @Transactional(readOnly = true)
    public List<GetUserBadgeRes> getUserBadgeList(long userId) throws BaseException{
        if (userDao.checkUserId(userId)==0){
            throw new BaseException(USERS_EMPTY_USER_ID);
        }
        try {
            return userDao.getUserBadgeList(userId);
        }
        catch (Exception exception){
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public int checkPhoneNum(String phoneNum) throws BaseException{
        try{
            return userDao.checkPhoneNum(phoneNum);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    @Transactional(readOnly = true)
    public int checkNickname(String nickname) throws BaseException{
        try{
            return userDao.checkNickname(nickname);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    @Transactional(readOnly = true)
    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

/*    public List<GetUserRes> getUsers() throws BaseException{
        try{
            List<GetUserRes> getUserRes = userDao.getUsers();
            return getUserRes;
        }
        catch (Exception exception) {
            // Logger를 이용하여 에러를 로그에 기록한다
            logger.error("Error!", exception);
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetUserRes> getUsersByEmail(String email) throws BaseException{
        try{
            List<GetUserRes> getUsersRes = userDao.getUsersByEmail(email);
            return getUsersRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }


    public GetUserRes getUser(int userIdx) throws BaseException {
        try {
            GetUserRes getUserRes = userDao.getUser(userIdx);
            return getUserRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkEmail(String email) throws BaseException{
        try{
            return userDao.checkEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostLoginRes logIn(PostLoginReq postLoginReq) throws BaseException{
        User User = userDao.getPwd(postLoginReq);
        String encryptPwd;
        try {
            encryptPwd=new SHA256().encrypt(postLoginReq.getPassword());
        } catch (Exception ignored) {
            throw new BaseException(PASSWORD_DECRYPTION_ERROR);
        }

        if(User.getPassword().equals(encryptPwd)){
            int userIdx = User.getUserIdx();
            String jwt = jwtService.createJwt(userIdx);
            return new PostLoginRes(userIdx,jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }*/

}
