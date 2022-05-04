package com.RoutineGongJakSo.BE.model;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Analysis {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long analysisId;

    @Column //일일 총 공부한 시간
    private String daySum;

    @Column //날짜
    private String date;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL)
    List<CheckIn> checkIns;

    //db에 저장을 하고, 그 날 또 스톱워치를 사용한다면, 해당 날짜를 찾아 업뎃
}
