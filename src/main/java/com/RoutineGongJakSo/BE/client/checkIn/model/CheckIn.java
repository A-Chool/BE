package com.RoutineGongJakSo.BE.client.checkIn.model;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class CheckIn {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long checkInId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "ANALYSIS_ID")
    private Analysis analysis;

    @Column
    private String date;

    @Column
    private String checkIn;

    @Column
    private String checkOut;

}
