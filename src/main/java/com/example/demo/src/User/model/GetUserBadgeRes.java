package com.example.demo.src.User.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
public class GetUserBadgeRes {
    long badge_id;
    String name;
    String is_representive;
    String icon;
    String content1;
    String content2;
}
