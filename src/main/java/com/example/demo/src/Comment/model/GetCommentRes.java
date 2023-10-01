package com.example.demo.src.Comment.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class GetCommentRes {
    long commentId;
    long parentId;
    String content;
    String nickname;
    String profileImg;
    String place;
    String imgUrl;
    LocalDateTime createdAt;
    String region;
    int likeNum;
}
