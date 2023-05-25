package com.example.demo.src.Review.Model;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetReviewRes {
    long review_id;
    String type;
    String nickname;
    String profile_img;
    String badge_img;
    String region;
    String content;
    LocalDateTime created_at;
}