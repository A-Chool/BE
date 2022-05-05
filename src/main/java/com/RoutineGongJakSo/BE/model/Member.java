package com.RoutineGongJakSo.BE.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "WEEK_TEAM_ID")
    private WeekTeam weekTeam;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

}
