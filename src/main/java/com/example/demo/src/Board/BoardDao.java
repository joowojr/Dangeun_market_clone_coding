package com.example.demo.src.Board;

import com.example.demo.src.Board.model.GetBoardImageRes;
import com.example.demo.src.Board.model.GetLikeBoardRes;
import com.example.demo.src.Board.model.GetBoardRes;
import com.example.demo.src.Board.model.PostProductReq;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class BoardDao {
    private JdbcTemplate template;
    @Autowired
    public void setDataSource(DataSource dataSource){
        this.template = new JdbcTemplate(dataSource);
    }

    // 중고 상품게시물 작성
    public long writeProduct(PostProductReq postProductReq){
        String boardQuery = "INSERT INTO Board(user_id,category_id,region_id,title,content,price) VALUE(?,?,?,?,?,?);";
        Object[] boardParams ={postProductReq.getUserId(),postProductReq.getCategoryId(),postProductReq.getRegionId(),postProductReq.getTitle(),postProductReq.getContent(),postProductReq.getPrice()};
        this.template.update(boardQuery,boardParams);
        String lastInsertIdQuery = "SELECT board_id FROM Board ORDER BY board_id DESC LIMIT 1";
        long boardId= this.template.queryForObject(lastInsertIdQuery,long.class);

        String productQuery = "INSERT INTO Product(board_id,pd_category_id,price_offer,donation,place) VALUE(?,?,?,?,?);";
        Object[] productParams  ={boardId,postProductReq.getPdCategoryId(),postProductReq.getPriceOffer(),postProductReq.getDonation(),postProductReq.getPlace()};
        this.template.update(productQuery,productParams);

        String imageQuery = "INSERT INTO BoardImage(board_id,img_url,is_representive) VALUE(?,?,?)";
        for (int i=0; i<postProductReq.getImages().size(); i++){
            this.template.update(imageQuery, boardId,postProductReq.getImages().get(i).getImgUrl(),postProductReq.getImages().get(i).getIsRepresentive());
        }

        return boardId;

    }

    // 게시물 1개 조회
    public GetBoardRes getBoard(long postId){
        long getPostParam = postId;
        String getImageQuery =
                "SELECT image_id, img_url FROM BoardImage WHERE board_id=? AND status='ACTIVE';";
        String getPostQuery =
                "SELECT B.board_id,B.title,B.content,PD.sale_status, " +
                        "U.nickname, U.profile_img, U.manner_temp,R.region_name AS region, " +
                        "(SELECT pd_category_name FROM ProductCategory PC WHERE PC.pd_category_id = PD.pd_category_id) AS category, " +
                        "B.pulled_at, B.content, B.price," +
                        "CASE PD.price_offer " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'price_offer',  " +
                        "CASE PD.donation " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'donation',  " +
                        "CASE PD.hide_status " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'hide', " +
                        "(SELECT COUNT(*) FROM LikePost L WHERE L.post_id = B.board_id ) AS like_num " +
                        "FROM Board B " +
                        "INNER JOIN Product PD " +
                        "ON B.board_id = PD.board_id " +
                        "INNER JOIN UserInfo U " +
                        "ON B.user_id = U.user_id " +
                        "INNER JOIN Region R " +
                        "ON B.region_id = R.region_id " +
                        "WHERE B.board_id=? " +
                        "AND B.status='ACTIVE';";
        GetBoardRes getBoardRes= template.queryForObject(
                getPostQuery, new productMapper(),getPostParam
        );
        List<GetBoardImageRes> getBoardImageRes =
                template.query(getImageQuery,((rs, rowNum) ->
                        new GetBoardImageRes(rs.getLong("image_id"),rs.getString("img_url"))),
                        getPostParam);
        getBoardRes.setImages(getBoardImageRes);
        return getBoardRes;
    }

    // 게시물 여러개 조회
    public List<GetBoardRes> getBoards(){
        String getPostByCategoryQuery =
                "SELECT B.board_id,B.title,B.content,PD.sale_status, " +
                        "U.nickname, U.profile_img, U.manner_temp,R.region_name AS region, " +
                        "(SELECT pd_category_name FROM ProductCategory PC WHERE PC.pd_category_id = PD.pd_category_id) AS category, " +
                        "B.pulled_at, B.content, B.price, " +
                        "CASE PD.price_offer " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'price_offer',  " +
                        "CASE PD.donation " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'donation',  " +
                        "CASE PD.hide_status " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'hide', " +
                        "(SELECT COUNT(*) FROM LikePost L WHERE L.post_id = B.board_id ) AS like_num " +
                        "FROM Board B " +
                        "INNER JOIN Product PD " +
                        "ON B.board_id = PD.board_id " +
                        "INNER JOIN UserInfo U " +
                        "ON B.user_id = U.user_id " +
                        "INNER JOIN Region R " +
                        "ON B.region_id = R.region_id " +
                        "WHERE B.status='ACTIVE';" ;
        List<GetBoardRes> getBoardRes = template.query(getPostByCategoryQuery,new productMapper());
        for (int i=0; i<getBoardRes.size();i++){
            long postId= getBoardRes.get(i).getPost_id();
            String getImageQuery =
                    "SELECT image_id, img_url FROM BoardImage WHERE board_id=? AND status='ACTIVE';";
            List<GetBoardImageRes> getBoardImageRes =
                    template.query(getImageQuery,((rs, rowNum) ->
                                    new GetBoardImageRes(rs.getLong("image_id"),rs.getString("img_url"))),
                            postId);
            getBoardRes.get(0).setImages(getBoardImageRes);
        }
        return getBoardRes;
    }
    // 게시물 여러개 조회
    // 카테고리 조회
    public List<GetBoardRes> getBoardsByCategory(Integer categoryId){
        int getPostParam = categoryId;
        String getPostByCategoryQuery =
                "SELECT P.board_id,P.title,P.content,PD.sale_status, " +
                        "U.nickname, U.profile_img, U.manner_temp,R.region_name AS region, " +
                        "(SELECT pd_category_name FROM ProductCategory PC WHERE PC.pd_category_id = PD.pd_category_id) AS category, " +
                        "P.pulled_at, P.content, P.price, " +
                        "CASE PD.price_offer " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'price_offer',  " +
                        "CASE PD.donation " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'donation',  " +
                        "CASE PD.hide_status " +
                        "WHEN 0 THEN 'NO' " +
                        "    ELSE 'YES' " +
                        "    END AS 'hide', " +
                        "(SELECT COUNT(*) FROM LikePost L WHERE L.post_id = P.board_id ) AS like_num " +
                        "FROM Board P " +
                        "INNER JOIN Product PD " +
                        "ON P.board_id = PD.board_id " +
                        "INNER JOIN UserInfo U " +
                        "ON P.user_id = U.user_id " +
                        "INNER JOIN Region R " +
                        "ON P.region_id = R.region_id " +
                        "WHERE P.status='ACTIVE'" +
                        "AND P.category_id=?;";
        List<GetBoardRes> getBoardRes = template.query(getPostByCategoryQuery,new productMapper(),getPostParam);
        for (int i=0; i<getBoardRes.size();i++){
            long postId= getBoardRes.get(i).getPost_id();
            String getImageQuery =
                    "SELECT image_id, img_url FROM BoardImage WHERE board_id=? AND status='ACTIVE';";
            List<GetBoardImageRes> getBoardImageRes =
                    template.query(getImageQuery,((rs, rowNum) ->
                                    new GetBoardImageRes(rs.getLong("image_id"),rs.getString("img_url"))),
                            postId);
            getBoardRes.get(0).setImages(getBoardImageRes);
        }
    return getBoardRes;
    }

    // 중고 상품 게시물 RowMapper
    public class productMapper implements RowMapper<GetBoardRes>{
        @Override
        public GetBoardRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            GetBoardRes getBoardRes = new GetBoardRes();
            getBoardRes.setPost_id(rs.getLong("board_id"));
            getBoardRes.setTitle(rs.getString("title"));
            getBoardRes.setContent(rs.getString("content"));
            getBoardRes.setSale_status(rs.getString("sale_status"));
            getBoardRes.setNickname(rs.getString("nickname"));
            getBoardRes.setProfile_img(rs.getString("profile_img"));
            getBoardRes.setManner_temp(rs.getFloat("manner_temp"));
            getBoardRes.setRegion(rs.getString("region"));
            getBoardRes.setRegion(rs.getString("region"));
            getBoardRes.setCategory(rs.getString("category"));
            getBoardRes.setPulled_at(rs.getTimestamp("pulled_at").toLocalDateTime());
            getBoardRes.setPrice( rs.getInt("price"));
            getBoardRes.setPrice_offer(rs.getString("price_offer"));
            getBoardRes.setDonation(rs.getString("donation"));
            getBoardRes.setHide( rs.getString("hide"));
            getBoardRes.setLike_num(rs.getInt("like_num"));
            return getBoardRes;
        }
    }

    // 관심목록 조회
    public List<GetLikeBoardRes> getLikePostList(long userId){
        long getlikepostlistparam = userId;
        String getlikepostlistquery = "SELECT LP.like_id,B.board_id,C.category_name AS category, B.title, D.region_name AS region,B.price," +
                "COUNT(LP.like_id) AS 'like_num' " +
                "FROM Board B " +
                "LEFT JOIN LikePost LP " +
                "ON B.board_id = LP.post_id " +
                "LEFT JOIN BoardCategory C " +
                "ON B.category_id = C.category_id " +
                "LEFT JOIN Region D " +
                "ON B.region_id = D.region_id " +
                "LEFT JOIN LikePost E " +
                "ON B.board_id = E.post_id " +
                "WHERE LP.user_id=? " +
                "AND B.status='ACTIVE' " +
                "GROUP BY B.board_id;";

        return this.template.query
                (getlikepostlistquery,
                        (rs, rowNum) -> new GetLikeBoardRes(
                                rs.getLong("like_id"),
                                rs.getString("category"),
                                rs.getString("title"),
                                rs.getString("region"),
                                rs.getInt("price"),
                                rs.getInt("like_num")
                        ),getlikepostlistparam
                );
    }
}
