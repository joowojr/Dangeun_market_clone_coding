package com.example.demo.src.Board.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DeleteLikeBoardRes {
    long boardId;
    long userId;
}
