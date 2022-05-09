package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.model.WeekTeam;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private String phoneNumber;

    @Column(unique = true)
    private Long kakaoId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @OneToMany
    @JoinColumn(name = "WeekTeam_WeekTeamId")
    private WeekTeam weekTeam;

    @Column(nullable = false)
    private String groundRule;

    @Column(nullable = false)
    private String workSpace;

    @Builder
    public TeamBoard(String groundRule) {
        this.groundRule = groundRule;
    }
}