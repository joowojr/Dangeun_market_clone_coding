package com.example.demo.src.Comment;

import com.example.demo.src.Comment.model.GetCommentRes;
import com.example.demo.src.Comment.model.PostCommentReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;
@Repository
public class CommentDao {
    private JdbcTemplate template;

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    public List<GetCommentRes> getComments(long postId){
        long getCommentsParam = postId;
        String getCommentsQuery =
                "SELECT C.comment_id, C.parent_id, C.content, U.nickname,U.profile_img, C.place, C.image_url, C.created_at, " +
                "(SELECT R.region_name FROM Region R WHERE R.region_id =(SELECT UR.region_id FROM UserRegion UR WHERE UR.user_id = U.user_id AND UR.is_representive=1)) AS region, " +
                "COUNT(CL.like_id) AS 'like_num' " +
                "FROM Comment C " +
                "LEFT JOIN UserInfo U " +
                "ON C.user_id = U.user_id " +
                "LEFT JOIN (SELECT like_id,comment_id FROM CommentLike WHERE status='ACTIVE') AS CL " +
                "ON C.comment_id = CL.comment_id " +
                "WHERE C.board_id=? " +
                "AND C.status='ACTIVE' " +
                "GROUP BY C.comment_id;";
        return this.template.query(getCommentsQuery,
                (rs, rowNum) -> new GetCommentRes(
                        rs.getLong("comment_id"),
                        rs.getLong("parent_id"),
                        rs.getString("content"),
                        rs.getString("nickname"),
                        rs.getString("profile_img"),
                        rs.getString("place"),
                        rs.getString("image_url"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("region"),
                        rs.getInt("like_num")
                ),getCommentsParam);
    }
    // 댓글 작성
    public long writeComment(PostCommentReq postCommentReq){
        String writeCommentQuery="INSERT INTO Comment(board_id,user_id,parent_id,content,place,image_url) " +
                "VALUE(?,?,?,?,?,?)";
        Object[] writeCommentParams
                ={postCommentReq.getPostId(),postCommentReq.getUserId(),postCommentReq.getParentId(),postCommentReq.getContent(),postCommentReq.getPlace(),postCommentReq.getImgUrl()};
        this.template.update(writeCommentQuery, writeCommentParams);

        String lastInsertIdQuery = "SELECT comment_id FROM Comment ORDER BY comment_id DESC LIMIT 1";
        return this.template.queryForObject(lastInsertIdQuery,long.class);
    }

    //댓글 삭제
    public long deleteComment(long commentId){
        long deleteCommentParam = commentId;
        String deleteCommentQuery = "DELETE FROM Comment WHERE comment_id=?";
        this.template.update(deleteCommentQuery,deleteCommentParam);
        return commentId;
    }

}
