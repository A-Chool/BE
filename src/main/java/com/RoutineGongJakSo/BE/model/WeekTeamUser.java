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
public class WeekTeamUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long WeekUserId;

    @OneToMany(mappedBy = "weekTeamUser")
    private List<User> memberList = new ArrayList<>();

    @OneToMany(mappedBy = "weekTeamUser")
    private List<WeekTeam> weekTeamList = new ArrayList<>();
}
