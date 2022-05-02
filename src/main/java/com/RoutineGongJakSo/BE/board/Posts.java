//package com.RoutineGongJakSo.BE.board;
//
//import com.RoutineGongJakSo.BE.model.User;
//import lombok.*;
//import org.springframework.data.annotation.CreatedDate;
//import org.springframework.data.annotation.LastModifiedDate;
//
//import javax.persistence.*;
//
////Entity 클래스에서는 절대 Setter 메소드를 만들지 않는다.
////대신, 해당 필드의 값 변경이 필요하면 명확히 그 목적과 의도를 나타낼수 있는 메소드를 추가해야 한다.
////생성자 또는 Builder를 통해 최종값을 채운 후, DB에 삽입한다.
////값 변경이 필요한 경우 해당 이벤트에 맞는 public 메소드를 호출하여 변경한다.
//@Getter
//@Setter
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity
//
//public class Posts{
//
//    //글 id
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private long Id;
//
//    @Column
//    @CreatedDate
//    private String createdAt;
//
//    @Column
//    @LastModifiedDate
//    private String modifiedAt;
//
//    //제목
//    @Column(nullable = false)
//    private String title;
//
//    //내용
//    @Column(nullable = false)
//    private String detail;
//
//    //작성자
//    @ManyToOne
//    @JoinColumn
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name="boardId", nullable = false)
//    private Posts posts;
//
//
//    //Builder pattern class를 생성 ; 생성자 상단에 선언시 생성자에 포함된 필드만 빌더에 포함됨
//    @Builder
//    public Posts(String title, String createdAt, String detail, String author) {
//        this.title = title;
//        this.createdAt = createdAt;
//        this.detail = detail;
//        this.author = author;
//    }
//
//    public void update(String title, String modifiedAt, String detail){
//        this.title = title;
//        this.modifiedAt = modifiedAt;
//        this.detail = detail;
//    }
//}
