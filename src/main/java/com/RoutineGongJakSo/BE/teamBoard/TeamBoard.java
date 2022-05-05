package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.model.Timestamped;
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
public class TeamBoard extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column
    private String week;

   @Column
    private String workSpace;


   /* @Column(nullable = false)
    private String workSpace;

    @Column(nullable = false)
    private String groundRules;*/

//    @OneToOne(mappedBy = WeekTeam)
//    private String weakTeamBoard;
}
