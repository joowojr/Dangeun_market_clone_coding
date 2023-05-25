package com.example.demo.src.Comment.model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetCommentRes {
    long comment_id;
    long parent_id;
    String content;
    String nickname;
    String profile_img;
    String place;
    String img_url;
    LocalDateTime created_at;
    String region;
    int like_num;
}
