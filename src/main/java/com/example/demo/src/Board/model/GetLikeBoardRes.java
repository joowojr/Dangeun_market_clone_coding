package com.example.demo.src.Board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLikeBoardRes {
    long like_post_id;
    String category;
    String title;
    /*GetBoardImageRes thumbnail;*/
    String region;
    int price;
    int like_num;
}
