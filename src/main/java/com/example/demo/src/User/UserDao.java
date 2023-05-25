package com.example.demo.src.User;


import com.example.demo.src.User.model.*;
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
        String getUserQuery = "SELECT U.user_id, U.nickname, U.profile_img,U.email, U.redeal_rate, U.response_rate, DATE (U.created_at) AS signup_date, U.manner_temp, " +
                "( SELECT region_name FROM Region R WHERE region_id = " +
                "( SELECT region_id FROM UserRegion WHERE  is_representive = 1 AND user_id = U.user_id ))AS region, " +
                "COUNT(UB.badge_ls_id) AS 'badge_num', " +
                "COUNT(PD.post_id) AS 'pd_num' , U.status, " +
                "COUNT(PR.review_id) AS '후기 개수' " +
                "FROM UserInfo U " +
                "LEFT JOIN UserRegion UR ON U.user_id = UR.user_id " +
                "LEFT JOIN (SELECT user_id,badge_ls_id FROM UserBadgeList UB WHERE status = 'ACTIVE') AS UB " +
                "ON U.user_id = UB.user_id " +
                "LEFT JOIN (SELECT user_id,post_id FROM Post WHERE category_id=1 ) AS PD " +
                "ON U.user_id = PD.user_id " +
                "LEFT JOIN (SELECT review_id, reviewee_id FROM ProductReview WHERE status='ACTIVE') AS PR " +
                "ON U.user_id = PR.reviewee_id " +
                "WHERE U.user_id = ? "+
                "GROUP BY U.user_id;";;
        long getUserParams = userIdx;
        return this.template.queryForObject(getUserQuery,
                (rs, rowNum) ->  new GetUserRes(
                        rs.getInt("user_id"),
                        rs.getString("nickname"),
                        rs.getString("profile_img"),
                        rs.getString("email"),
                        rs.getFloat("redeal_rate"),
                        rs.getFloat("response_rate"),
                        rs.getTimestamp("signup_date").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getFloat("manner_temp"),
                        rs.getString("region"),
                        rs.getInt("badge_num"),
                        rs.getInt("pd_num")
                ),getUserParams);
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

    /*public List<GetUserRes> getUsers(){
        String getUsersQuery = "select * from UserInfo";
        return this.jdbcTemplate.query(getUsersQuery,
                (rs,rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password"))
                );
    }

    public List<GetUserRes> getUsersByEmail(String email){
        String getUsersByEmailQuery = "select * from UserInfo where email =?";
        String getUsersByEmailParams = email;
        return this.jdbcTemplate.query(getUsersByEmailQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUsersByEmailParams);
    }

    public GetUserRes getUser(int userIdx){
        String getUserQuery = "select * from UserInfo where userIdx = ?";
        int getUserParams = userIdx;
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum) -> new GetUserRes(
                        rs.getInt("userIdx"),
                        rs.getString("userName"),
                        rs.getString("ID"),
                        rs.getString("Email"),
                        rs.getString("password")),
                getUserParams);
    }
    

    public int createUser(PostUserReq postUserReq){
        String createUserQuery = "insert into UserInfo (userName, ID, password, email) VALUES (?,?,?,?)";
        Object[] createUserParams = new Object[]{postUserReq.getUserName(), postUserReq.getId(), postUserReq.getPassword(), postUserReq.getEmail()};
        this.jdbcTemplate.update(createUserQuery, createUserParams);

        String lastInserIdQuery = "select last_insert_id()";
        return this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
    }

    public int checkEmail(String email){
        String checkEmailQuery = "select exists(select email from UserInfo where email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);

    }

    public int modifyUserName(PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update UserInfo set userName = ? where userIdx = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserIdx()};

        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

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
