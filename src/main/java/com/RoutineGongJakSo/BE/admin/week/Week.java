package com.RoutineGongJakSo.BE.admin.week;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
public class Week {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long weekId;

    @Column
    private String weekName;

    @Column
    private Boolean display;

    public Week(String weekName) {
        this.weekName = weekName;
        this.display = false;
    }
}
