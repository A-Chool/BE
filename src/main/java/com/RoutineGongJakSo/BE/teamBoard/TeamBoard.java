package com.RoutineGongJakSo.BE.teamBoard;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class TeamBoard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    private String week;

    private String workSpace;

    @Builder
    public TeamBoard(String week, Long teamId, String workSpace) {
        this.teamId = teamId;
        this.week = week;
        this.workSpace = workSpace;
    }

    public void update(String week, Long teamId, String workSpace) {
        this.teamId = teamId;
        this.week = week;
        this.workSpace = workSpace;
    }
}
/* @Column(nullable = false)
    private String workSpace;

    @Column(nullable = false)
    private String groundRules;*/

//    @OneToOne(mappedBy = WeekTeam)
//    private String weakTeamBoard;

