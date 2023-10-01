package com.example.demo.src.Board.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetProductRes {
    long postId;
    String title;
    String content;
    /*@JsonInclude(JsonInclude.Include.NON_NULL)*/
    List<GetBoardImageRes> images;
    String saleStatus;
    String nickname;
    String profileImg;
    float mannerTemp;
    String region;
    String category;
    LocalDateTime pulledAt;
    int price;
    String priceOffer;
    String donation;
    String hide;
    int likeNum;

}
