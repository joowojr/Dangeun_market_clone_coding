package com.example.demo.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Page<T> {
    // 데이터
    private T data;
    // 출력을 원하는 데이터의 개수
    private int size;
   // 전체 데이터의 수
    private int total;
    // 현재 페이지 번호
    private int currentPage;
    // 전체 페이지 개수
    private int totalPages;
    // 시작 페이지 번호
    private int start;
    // 종료 페이지 번호
    private int end;
    // 페이징의 개수
    private int pagingCount;


    // 생성자
    // size : 한 화면에 보여질 행의 수
    public Page(int currentPage, int size, int total) {
        this.currentPage = currentPage;
        this.size = size;
        this.total = total;

        if (total == 0) {
            // select 결과가 없다면
            totalPages = 0;
            start = 0;
            end = 0;
        } else {
            // select 결과가 있다면
            // 전체 페이지 개수 구하기(전체 글의 수 / 한 화면에 보여질 행의 수)
            // 정수와 정수의 나눗셈의 결과는 정수이므로 13 / 7 = 1
            totalPages = total / size;
            // 보정해줘야 할 경우는? 나머지가 0보다 클 때
            if (total % size > 0) {
                // 전체페이지수를 1증가 처리
                totalPages++;
            }
            end = total - (currentPage-1)*size ;
            start = end - size + 1;

            pagingCount = end-start+1;

            if (end > total) end=total;
           /* if (end > totalPages) {
                end = totalPages;
            }*/
        }
    }
}