package com.example.demo.src.Board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostProductReq {
    long userId;
    long categoryId;
    long regionId;
    String title;
    String content;
    int price;

    List<PostBoardImageReq> images;

    long pdCategoryId;
    int priceOffer;
    int donation;
    String place;
}
