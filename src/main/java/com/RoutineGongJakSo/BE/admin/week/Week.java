package com.RoutineGongJakSo.BE.admin.week;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekId;

    @Column
    private String weekName;

    @Column
    private Boolean display;
}
