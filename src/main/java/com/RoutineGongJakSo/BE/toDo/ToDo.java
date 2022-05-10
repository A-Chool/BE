package com.RoutineGongJakSo.BE.toDo;

import com.RoutineGongJakSo.BE.model.WeekTeam;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long toDoId;

    @ManyToOne
    @JoinColumn(name = "WEEK_TEAM_ID")
    private WeekTeam weekTeam;

    @Column
    private String todoContent;

    @Column
    private boolean todoCheck;

}
