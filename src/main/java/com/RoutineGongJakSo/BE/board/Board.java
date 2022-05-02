package com.RoutineGongJakSo.BE.board;

import com.RoutineGongJakSo.BE.model.Timestamped;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Entity
public class Board extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @OneToMany
    private List<Board> boardList;

    public Board(Long id, String title) {
        this.id = id;
        this.title = title;
    }
}
//    // 게시판 수정 메소드
//    public void update(String id, String title, String author){
//    this.id = id;
//    this.title = title;
//    this.author = author;
//    }
