package com.example.demo.src.Board;

import com.example.demo.src.Board.model.*;
import org.jetbrains.annotations.NotNull;
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

    //
    public int checkProductId(long boardId){
        String checkProductIdQuery = "SELECT exists(SELECT board_id FROM Product WHERE board_id=?)";
        long checkProductIdParam = boardId;
        return this.template.queryForObject(checkProductIdQuery,int.class,checkProductIdParam);
    }

    // 중고 상품게시물 작성
    public long writeProduct(@NotNull PostProductReq postProductReq){
        String boardQuery = "INSERT INTO Board(user_id,category_id,region_id,title,content,price) VALUE(?,?,?,?,?,?);";
        Object[] boardParams ={postProductReq.getUserId(),1,postProductReq.getRegionId(),postProductReq.getTitle(),postProductReq.getContent(),postProductReq.getPrice()};
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
    public GetProductRes getProduct(long postId){
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
                        "(SELECT COUNT(*) FROM LikeBoard L WHERE L.board_id = B.board_id ) AS like_num " +
                        "FROM Board B " +
                        "INNER JOIN Product PD " +
                        "ON B.board_id = PD.board_id " +
                        "INNER JOIN UserInfo U " +
                        "ON B.user_id = U.user_id " +
                        "INNER JOIN Region R " +
                        "ON B.region_id = R.region_id " +
                        "WHERE B.board_id=? " +
                        "AND B.status='ACTIVE';";
        GetProductRes getProductRes = template.queryForObject(
                getPostQuery, new productMapper(),getPostParam
        );
        List<GetBoardImageRes> getBoardImageRes =
                template.query(getImageQuery,((rs, rowNum) ->
                        new GetBoardImageRes(rs.getLong("image_id"),rs.getString("img_url"))),
                        getPostParam);
        getProductRes.setImages(getBoardImageRes);
        return getProductRes;
    }

    // 게시물 여러개 조회
    public List<GetProductRes> getBoards(){
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
                        "(SELECT COUNT(*) FROM LikeBoard L WHERE L.board_id = B.board_id ) AS like_num " +
                        "FROM Board B " +
                        "INNER JOIN Product PD " +
                        "ON B.board_id = PD.board_id " +
                        "INNER JOIN UserInfo U " +
                        "ON B.user_id = U.user_id " +
                        "INNER JOIN Region R " +
                        "ON B.region_id = R.region_id " +
                        "WHERE B.status='ACTIVE';" ;
        List<GetProductRes> getProductRes = template.query(getPostByCategoryQuery,new productMapper());
        for (int i = 0; i< getProductRes.size(); i++){
            long postId= getProductRes.get(i).getPostId();
            String getImageQuery =
                    "SELECT image_id, img_url FROM BoardImage WHERE board_id=? AND status='ACTIVE';";
            List<GetBoardImageRes> getBoardImageRes =
                    template.query(getImageQuery,((rs, rowNum) ->
                                    new GetBoardImageRes(rs.getLong("image_id"),rs.getString("img_url"))),
                            postId);
            getProductRes.get(0).setImages(getBoardImageRes);
        }
        return getProductRes;
    }

    // 중고 게시물 목록 조회
    public List<GetProductRes> getProductList(){
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
                        "(SELECT COUNT(*) FROM LikeBoard L WHERE L.board_id = P.board_id ) AS like_num " +
                        "FROM Board P " +
                        "INNER JOIN Product PD " +
                        "ON P.board_id = PD.board_id " +
                        "INNER JOIN UserInfo U " +
                        "ON P.user_id = U.user_id " +
                        "INNER JOIN Region R " +
                        "ON P.region_id = R.region_id " +
                        "WHERE P.status='ACTIVE'" +
                        "AND P.category_id=1;";
        List<GetProductRes> getProductRes = template.query(getPostByCategoryQuery,new productMapper());
        for (int i = 0; i< getProductRes.size(); i++){
            long postId= getProductRes.get(i).getPostId();
            String getImageQuery =
                    "SELECT image_id, img_url FROM BoardImage WHERE board_id=? AND status='ACTIVE';";
            List<GetBoardImageRes> getBoardImageRes =
                    template.query(getImageQuery,((rs, rowNum) ->
                                    new GetBoardImageRes(rs.getLong("image_id"),rs.getString("img_url"))),
                            postId);
            getProductRes.get(0).setImages(getBoardImageRes);
        }
    return getProductRes;
    }

    // 중고 상품 게시물 RowMapper
    public class productMapper implements RowMapper<GetProductRes>{
        @Override
        public GetProductRes mapRow(ResultSet rs, int rowNum) throws SQLException {
            GetProductRes getProductRes = new GetProductRes();
            getProductRes.setPostId(rs.getLong("board_id"));
            getProductRes.setTitle(rs.getString("title"));
            getProductRes.setContent(rs.getString("content"));
            getProductRes.setSaleStatus(rs.getString("sale_status"));
            getProductRes.setNickname(rs.getString("nickname"));
            getProductRes.setProfileImg(rs.getString("profile_img"));
            getProductRes.setMannerTemp(rs.getFloat("manner_temp"));
            getProductRes.setRegion(rs.getString("region"));
            getProductRes.setRegion(rs.getString("region"));
            getProductRes.setCategory(rs.getString("category"));
            getProductRes.setPulledAt(rs.getTimestamp("pulled_at").toLocalDateTime());
            getProductRes.setPrice( rs.getInt("price"));
            getProductRes.setPriceOffer(rs.getString("price_offer"));
            getProductRes.setDonation(rs.getString("donation"));
            getProductRes.setHide( rs.getString("hide"));
            getProductRes.setLikeNum(rs.getInt("like_num"));
            return getProductRes;
        }
    }
    //게시물 삭제
    public long deleteBoard(long boardId){
        String deleteBoardQuery = "DELETE FROM Board WHERE board_id=?;";
        long boardIdParam = boardId;
        this.template.queryForObject(deleteBoardQuery,long.class,boardIdParam);
        return boardId;
    }
    //중고 상품 삭제
    public long deleteProduct(long boardId){
        String deleteProductQuery = "DELETE FROM Product WHERE board_id=?;";
        long boardIdParam = boardId;
        this.template.queryForObject(deleteProductQuery,long.class,boardIdParam);
        return boardId;
    }

    // 관심목록 조회
    public List<GetLikeBoardRes> getLikePostList(long userId){
        long getLikeBoardlistparam = userId;
        String getlikeBoardlistquery = "SELECT LP.like_id,B.board_id,C.category_name AS category, B.title, D.region_name AS region,B.price," +
                "COUNT(LP.like_id) AS 'like_num' " +
                "FROM Board B " +
                "LEFT JOIN LikeBoard LP " +
                "ON B.board_id = LP.board_id " +
                "LEFT JOIN BoardCategory C " +
                "ON B.category_id = C.category_id " +
                "LEFT JOIN Region D " +
                "ON B.region_id = D.region_id " +
                "LEFT JOIN LikeBoard E " +
                "ON B.board_id = E.board_id " +
                "WHERE LP.user_id=? " +
                "AND B.status='ACTIVE' " +
                "GROUP BY B.board_id;";

        return this.template.query
                (getlikeBoardlistquery,
                        (rs, rowNum) -> new GetLikeBoardRes(
                                rs.getLong("like_id"),
                                rs.getString("category"),
                                rs.getString("title"),
                                rs.getString("region"),
                                rs.getInt("price"),
                                rs.getInt("like_num")
                        ),getLikeBoardlistparam
                );
    }
    // 관심 조회
    public int checkLike(long boardId, long userId){
        String checkLikeQuery = "SELECT exists(SELECT * FROM LikeBoard WHERE user_id=? AND board_id=?) AS 'check';";
        Object[] params = {boardId,userId};
        return this.template.queryForObject(checkLikeQuery,int.class,params);}

    // 관심 누르기 
    public PostLikeBoardRes likeBoard(long boardId, long userId){
        Object[] postLikeBoardParams = {boardId, userId};
        String postLikeBoardQuery = "INSERT INTO LikeBoard(board_id, user_id) VALUE (?,?);";
        this.template.update(postLikeBoardQuery, postLikeBoardParams);
        return new PostLikeBoardRes(boardId, userId);

    }
    // 관심 취소
    public DeleteLikeBoardRes unlikeBoard(long boardId, long userId){
        Object[] patchLikeBoardParams = {boardId, userId};
        String patchLikeBoardQuery = "DELETE FROM LikeBoard WHERE board_id=? AND user_id=?;";
        this.template.update(patchLikeBoardQuery,patchLikeBoardParams);
        return new DeleteLikeBoardRes(boardId, userId);
    }
}
