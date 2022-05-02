//package com.RoutineGongJakSo.BE.board;
//
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//
//@Getter
//@NoArgsConstructor
//public class PostsUpdateRequestDto
//{
//    private String title;
//    private String modifiedAt;
//    private String detail;
//
//    @Builder
//    public PostsUpdateRequestDto(String title, String modifiedAt,String detail) {
//        this.title = title;
//        this.modifiedAt = modifiedAt;
//        this.detail = detail;
//    }
//
//    public Posts toEntity() {
//        return Posts.builder().title(title).detail(detail).build();
//    }
//}