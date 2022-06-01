package com.RoutineGongJakSo.BE.client.analysis.model;

import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.util.Timestamped;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Analysis extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long analysisId;

    @Column //일일 총 공부한 시간
    private String daySum;

    @Column //날짜
    private String date;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private User user;

    @OneToMany(mappedBy = "analysis", cascade = CascadeType.ALL)
    private List<CheckIn> checkIns;
}
