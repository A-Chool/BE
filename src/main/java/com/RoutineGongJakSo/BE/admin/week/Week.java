package com.RoutineGongJakSo.BE.admin.week;

import com.RoutineGongJakSo.BE.admin.team.Team;
import com.RoutineGongJakSo.BE.client.toDo.ToDo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

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

    @OneToMany(mappedBy = "week", cascade = CascadeType.ALL)
    private List<Team> teamList;

    public Week(String weekName) {
        this.weekName = weekName;
        this.display = false;
    }

    public void addTeam(Team team) {
        this.teamList.add(team);
    }
}
