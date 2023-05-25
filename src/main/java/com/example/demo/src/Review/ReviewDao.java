package com.example.demo.src.Review;

import com.example.demo.src.Review.Model.GetReviewRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class ReviewDao {
    private JdbcTemplate template;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    //리뷰 목록 조회
    public List<GetReviewRes> getReviews(long userId) {
        String sql = "SELECT PR.review_id, CASE  PR.review_type " +
                "WHEN 'C' THEN 'CONSUMER' " +
                "ELSE 'SELLER' " +
                "END AS 'review_type', " +
                "UI.nickname, UI.profile_img, BL.badge_icon, " +
                "(SELECT UR.region_id FROM UserRegion UR WHERE  UR.user_id=UI.user_id AND UR.is_representive=1 AND UR.status = 'ACTIVE') AS region, " +
                " PR.content, PR.created_at " +
                "FROM ProductReview PR " +
                "INNER JOIN UserInfo UI " +
                "ON PR.reviewer_id = UI.user_id " +
                "INNER JOIN UserBadgeList UB " +
                "ON PR.reviewer_id = UB.user_id " +
                "INNER JOIN BadgeList BL " +
                "ON UB.badge_id = BL.badge_id \n" +
                "WHERE PR.reviewee_id=? " +
                "AND UI.status='ACTIVE' AND UB.is_representive=1 ";

        List<GetReviewRes> result = template.query(sql,
                (rs, rowNum) ->  new GetReviewRes(
                        rs.getLong("review_id"),
                        rs.getString("review_type"),
                        rs.getString("nickname"),
                        rs.getString("profile_img"),
                        rs.getString("badge_icon"),
                        rs.getString("region"),
                        rs.getString("content"),
                        rs.getTimestamp("created_at").toLocalDateTime()

                ),userId);
        return result;
    }
}
