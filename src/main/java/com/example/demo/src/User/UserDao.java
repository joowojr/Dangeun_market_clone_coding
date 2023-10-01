package com.example.demo.src.User;


import com.example.demo.src.User.model.*;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class UserDao {

    private JdbcTemplate template;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    public GetUserRes getUserById(long userIdx){
        String getUserQuery = "SELECT UI.user_id, UI.nickname, UI.profile_img,UI.email, UI.redeal_rate, UI.response_rate, DATE (UI.created_at) AS signup_date, UI.manner_temp, " +
                "( SELECT region_name FROM Region R WHERE region_id = " +
                "( SELECT region_id FROM UserRegion WHERE  is_representive = 1 AND user_id = UI.user_id ))AS region, " +
                "COUNT(UB.badge_ls_id) AS 'badge_num', " +
                "COUNT(PD.board_id) AS 'pd_num' , UI.status, " +
                "COUNT(PR.review_id) AS '후기 개수' " +
                "FROM UserInfo UI " +
                "LEFT JOIN UserRegion UR ON UI.user_id = UR.user_id " +
                "LEFT JOIN (SELECT user_id,badge_ls_id FROM UserBadgeList UB WHERE status = 'ACTIVE') AS UB " +
                "ON UI.user_id = UB.user_id " +
                "LEFT JOIN (SELECT user_id,board_id FROM Board WHERE category_id=1 ) AS PD " +
                "ON UI.user_id = PD.user_id " +
                "LEFT JOIN (SELECT review_id, reviewee_id FROM ProductReview WHERE status='ACTIVE') AS PR " +
                "ON UI.user_id = PR.reviewee_id " +
                "WHERE UI.user_id = ? "+
                "GROUP BY UI.user_id;";;
        long getUserParam = userIdx;
        return this.template.queryForObject(getUserQuery,
                (rs, rowNum) ->  new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("profile_img"),
                        rs.getString("email"),
                        rs.getFloat("redeal_rate"),
                        rs.getFloat("response_rate"),
                        rs.getTimestamp("signup_date").toString(),
                        rs.getString("status"),
                        rs.getFloat("manner_temp"),
                        rs.getString("region"),
                        rs.getInt("badge_num"),
                        rs.getInt("pd_num")
                ),getUserParam);
    }
    public int getUsersTotal(){
        String getUserQuery = "SELECT COUNT(*) FROM (SELECT UI.user_id, UI.nickname, UI.profile_img,UI.email, UI.redeal_rate, UI.response_rate, DATE (UI.created_at) AS signup_date, UI.manner_temp, " +
                "( SELECT region_name FROM Region R WHERE region_id = " +
                "( SELECT region_id FROM UserRegion WHERE  is_representive = 1 AND user_id = UI.user_id ))AS region, " +
                "COUNT(UB.badge_ls_id) AS 'badge_num', " +
                "COUNT(PD.board_id) AS 'pd_num' , UI.status, " +
                "COUNT(PR.review_id) AS '후기 개수' " +
                "FROM UserInfo UI " +
                "LEFT JOIN UserRegion UR ON UI.user_id = UR.user_id " +
                "LEFT JOIN (SELECT user_id,badge_ls_id FROM UserBadgeList UB WHERE status = 'ACTIVE') AS UB " +
                "ON UI.user_id = UB.user_id " +
                "LEFT JOIN (SELECT user_id,board_id FROM Board WHERE category_id=1 ) AS PD " +
                "ON UI.user_id = PD.user_id " +
                "LEFT JOIN (SELECT review_id, reviewee_id FROM ProductReview WHERE status='ACTIVE') AS PR " +
                "ON UI.user_id = PR.reviewee_id " +
                "WHERE UI.status='ACTIVE'" +
                "GROUP BY UI.user_id) T ";
        return this.template.queryForObject(getUserQuery,int.class);
    }
    public List<GetUserRes> getUsers(int size, int start){
        String getUserQuery = "SELECT * FROM (SELECT UI.user_id, UI.nickname, UI.profile_img,UI.email, UI.redeal_rate, UI.response_rate, DATE (UI.created_at) AS signup_date, UI.manner_temp, " +
                "( SELECT region_name FROM Region R WHERE region_id = " +
                "( SELECT region_id FROM UserRegion WHERE  is_representive = 1 AND user_id = UI.user_id ))AS region, " +
                "COUNT(UB.badge_ls_id) AS 'badge_num', " +
                "COUNT(PD.board_id) AS 'pd_num' , UI.status, " +
                "COUNT(PR.review_id) AS '후기 개수' " +
                "FROM UserInfo UI " +
                "LEFT JOIN UserRegion UR ON UI.user_id = UR.user_id " +
                "LEFT JOIN (SELECT user_id,badge_ls_id FROM UserBadgeList UB WHERE status = 'ACTIVE') AS UB " +
                "ON UI.user_id = UB.user_id " +
                "LEFT JOIN (SELECT user_id,board_id FROM Board WHERE category_id=1 ) AS PD " +
                "ON UI.user_id = PD.user_id " +
                "LEFT JOIN (SELECT review_id, reviewee_id FROM ProductReview WHERE status='ACTIVE') AS PR " +
                "ON UI.user_id = PR.reviewee_id " +
                "WHERE UI.status='ACTIVE'"+
                "GROUP BY UI.user_id)T WHERE T.user_id LIMIT ? OFFSET ?";;
                Object [] params = {size,start-1};
        return this.template.query(getUserQuery,
                (rs, rowNum) ->  new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("profile_img"),
                        rs.getString("email"),
                        rs.getFloat("redeal_rate"),
                        rs.getFloat("response_rate"),
                        rs.getTimestamp("signup_date").toString(),
                        rs.getString("status"),
                        rs.getFloat("manner_temp"),
                        rs.getString("region"),
                        rs.getInt("badge_num"),
                        rs.getInt("pd_num")
                ),params);
    }

    public List<GetUserBadgeRes> getUserBadgeList(long userId) {
        long getUserParams = userId;
        String getUserQuery = "SELECT BL.badge_id,BL.badge_name, " +
                "CASE UB.is_representive " +
                "WHEN 0 THEN 'NO' " +
                "ELSE 'YES' " +
                "END AS 'is_representive', " +
                "BL.badge_icon, BL.content1, BL.content2 " +
                "FROM UserBadgeList UB " +
                "INNER JOIN BadgeList BL " +
                "ON UB.badge_id = BL.badge_id " +
                "WHERE UB.user_id= ? " +
                "AND UB.status = 'ACTIVE';";
        return  this.template.query(
                getUserQuery,
                (rs,rowNum)-> new GetUserBadgeRes(
                        rs.getLong("badge_id"),
                        rs.getString("badge_name"),
                        rs.getString("is_representive"),
                        rs.getString("badge_icon"),
                        rs.getString("content1"),
                        rs.getString("content2")
                ),getUserParams);
    }

    public long createUser(PostUserReq postUserReq){
        String createUserQuery = "INSERT INTO UserInfo (phone_num,nickname,email) VALUES (?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getPhoneNum(),postUserReq.getNickname(),postUserReq.getEmail()};
        this.template.update(createUserQuery, createUserParams);

        String lastInserIdQuery ="SELECT user_id FROM UserInfo ORDER BY user_id DESC LIMIT 1";
        return this.template.queryForObject(lastInserIdQuery,long.class);
    }

    public long getUserByPhoneNum(String phoneNum){
        String checkPhoneNumQuery = "SELECT user_id FROM UserInfo WHERE phone_num=?";
        String phoneNumParam = phoneNum;
        return this.template.queryForObject(checkPhoneNumQuery,long.class,phoneNumParam);
    }

    public long checkUserId(long userId){
        String checkUserQuery = "SELECT exists(SELECT user_id FROM UserInfo WHERE user_id=?)";
        long checkUserParam = userId;
        return this.template.queryForObject(checkUserQuery,int.class,checkUserParam);
    }

    public int checkPhoneNum(String phoneNum){
        String checkPhoneNumQuery = "SELECT exists(SELECT phone_num FROM UserInfo WHERE phone_num=?)";
        String phoneNumParam = phoneNum;
        return this.template.queryForObject(checkPhoneNumQuery,int.class,phoneNumParam);
    }
    public int checkNickname(String nickname){
        String checkNicknameQuery = "SELECT exists(SELECT nickname FROM UserInfo WHERE nickname=?)";
        String nicknameParam = nickname;
        return  this.template.queryForObject(checkNicknameQuery,int.class,nicknameParam);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "SELECT exists(SELECT email FROM UserInfo WHERE email=?)";
        String emailParam = email;
        return  this.template.queryForObject(checkEmailQuery,int.class,emailParam);
    }

    public int modifyNickname(@NotNull PatchUserReq patchUserReq){
        String modifyNicknameQuery = "update UserInfo set nickname = ? where user_id = ? ";
        Object[] modifyNicknameParams = new Object[]{patchUserReq.getNickname(), patchUserReq.getUserId()};

        return this.template.update(modifyNicknameQuery,modifyNicknameParams);
    }
    public int modifyProfileImg(PatchUserReq patchUserReq){
        String modifyProfileImgQuery = "update UserInfo set profile_img = ? where user_id = ? ";
        Object[] modifyProfileImgParams = new Object[]{patchUserReq.getProfileImg(), patchUserReq.getUserId()};

        return this.template.update(modifyProfileImgQuery,modifyProfileImgParams);
    }


    /*

    public User getPwd(PostLoginReq postLoginReq){
        String getPwdQuery = "select userIdx, password,email,userName,ID from UserInfo where ID = ?";
        String getPwdParams = postLoginReq.getId();

        return this.jdbcTemplate.queryForObject(getPwdQuery,
                (rs,rowNum)-> new User(
                        rs.getInt("userIdx"),
                        rs.getString("ID"),
                        rs.getString("userName"),
                        rs.getString("password"),
                        rs.getString("email")
                ),
                getPwdParams
                );

    }
*/

}
