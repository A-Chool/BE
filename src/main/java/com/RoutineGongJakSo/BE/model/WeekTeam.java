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
    private Long WeekTeamId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String week;

    @Column(nullable = false)
    private String groundRole;

    @Column(nullable = false)
    private String workSpace;

    @Column(nullable = false)
    private String roomId;

}
