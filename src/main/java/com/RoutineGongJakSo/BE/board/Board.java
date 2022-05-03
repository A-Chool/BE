package com.RoutineGongJakSo.BE.board;

import com.RoutineGongJakSo.BE.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardId;

    @Column(nullable = false)
    private String boardName;

    //권한 -> user, admin
//    @Enumerated
    @Column(nullable = false)
    private int boardLevel;

}

//
//    public Board(Long id, String boardName, int boardLevel) {
//        this.id = id;
//        this.boardName = boardName;
//        this.boardLevel = boardLevel;
//    }
//
//
//    @OneToMany
//    private List<Posts> postsList;
//
//    // 게시판 수정 메소드
//    public void update(String id, String title, String author){
//    this.id = id;
//    this.title = title;
//    this.author = author;
//    }
