package com.RoutineGongJakSo.BE.teamBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class TeamBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String week;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @OneToMany(mappedBy = "teamBoard", cascade = CascadeType.REMOVE)
    private List<Member> memberList;

    @Column(nullable = false)
    private String groundRule;

    @Column(nullable = false)
    private String workSpace;

    @Builder
    public TeamBoard(String groundRule) {
        this.groundRule = groundRule;
        this.workSpace = getWorkSpace();
    }
}