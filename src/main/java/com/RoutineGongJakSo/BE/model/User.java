package com.RoutineGongJakSo.BE.model;

import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
public class User extends Timestamped {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long userId;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false)
    private String userPw;

    @Column
    private String phoneNumber;

    @Column
    private int userLevel;

    @Column(unique = true)
    private Long kakaoId;

    @Column(unique = true)
    private String naverId;

    @OneToOne(mappedBy = "user", cascade = CascadeType.REMOVE)
    private Member member;

//    @OneToMany(mappedBy = "user")
//    private List<CheckIn> checkIn;

}
