package com.example.demo.src.Comment.model;

import lombok.*;
import org.jetbrains.annotations.Nullable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostCommentReq {
    long postId;
    long userId;
    @Nullable
    Long parentId;
    String content;
    String place;
    String imgUrl;
}

