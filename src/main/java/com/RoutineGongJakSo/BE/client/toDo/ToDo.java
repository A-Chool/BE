package com.RoutineGongJakSo.BE.client.toDo;

import com.RoutineGongJakSo.BE.admin.team.Team;
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
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Column
    private String todoContent;

    @Column
    private boolean todoCheck;

}
