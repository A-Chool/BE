package com.RoutineGongJakSo.BE.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WeekTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long WeekTeam;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private Long week;

    @Column(nullable = false)
    private String groundRole;

    @Column(nullable = false)
    private String workSpace;

    @Column(nullable = false)
    private String roomId;
}
