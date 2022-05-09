package com.RoutineGongJakSo.BE.model;

import lombok.*;
import org.springframework.stereotype.Service;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class WeekTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long WeekTeamId;

    @OneToMany(mappedBy = "weekTeam", cascade = CascadeType.REMOVE)
    private List<Member> memberList;

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

    @Column
    private String roomName;

    public void addMember(Member member) {
        this.memberList.add(member);
    }
}
