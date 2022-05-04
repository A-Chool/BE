package com.RoutineGongJakSo.BE.model;

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
public class CheckIn {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    Long checkInId;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column
    private String date;

    @Column
    private String checkIn;

    @Column
    private String checkOut;
}
