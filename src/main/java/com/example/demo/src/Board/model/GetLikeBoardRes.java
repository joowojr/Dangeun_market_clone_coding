package com.example.demo.src.Board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetLikeBoardRes {
    long likeId;
    String category;
    String title;
    /*GetBoardImageRes thumbnail;*/
    String region;
    int price;
    int likeNum;
}
