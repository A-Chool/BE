package com.RoutineGongJakSo.BE.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WeekTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long WeekTeam;

    @ManyToOne
    @JoinColumn(name = "WEEK_USER_ID")
    private WeekTeamUser weekTeamUser;

    @Column(nullable = false)
    private String week;

    @Column(nullable = false)
    private String team;

    //공지사항
    @Column(nullable = false)
    private String workSpace;

    //그라운드룰
    @Column(nullable = false)
    private String groundRole;

    //채팅방ID
    @Column(nullable = false)
    private String roomId;
}
