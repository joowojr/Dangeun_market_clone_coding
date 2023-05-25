package com.example.demo.src.User.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class GetUserRes {
    long user_id;
    String nickname;
    String profile_img;
    String email;
    float redeal_rate;
    float response_rate;
    LocalDateTime signup_date;
    String status;
    float manner_temp;
    String region;
    int badge_num;
    int pd_num;
}
