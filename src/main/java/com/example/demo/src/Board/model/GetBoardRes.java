package com.example.demo.src.Board.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetBoardRes {
    long post_id;
    String title;
    String content;
    /*@JsonInclude(JsonInclude.Include.NON_NULL)*/
    List<GetBoardImageRes> images;
    String sale_status;
    String nickname;
    String profile_img;
    float manner_temp;
    String region;
    String category;
    LocalDateTime pulled_at;
    int price;
    String price_offer;
    String donation;
    String hide;
    int like_num;

}
