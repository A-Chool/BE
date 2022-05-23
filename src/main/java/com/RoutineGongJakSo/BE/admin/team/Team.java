package com.RoutineGongJakSo.BE.admin.team;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.admin.week.Week;
import com.RoutineGongJakSo.BE.client.toDo.ToDo;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false)
    private String teamName;

    @OneToMany(mappedBy = "team", cascade = CascadeType.REMOVE)
    private List<Member> memberList;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<ToDo> toDoList;

    @ManyToOne
    @JoinColumn(name = "WEEK_ID")
    private Week week;

    @Column(nullable = false)
    private String groundRule;

    @Column(nullable = false)
    private String workSpace;

    @Column(nullable = false)
    private String roomId;

    @Column
    private String roomName;

    public void addMember(Member member) {
        this.memberList.add(member);
    }

    public void addToDo(ToDo toDo) {
        this.toDoList.add(toDo);
    }

    public Team(String teamName) {
        this.teamName = teamName;
    }
}
