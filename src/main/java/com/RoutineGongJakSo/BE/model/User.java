package com.RoutineGongJakSo.BE.model;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "user", cascade =CascadeType.REMOVE)
    private List<Member> memberList;

    public void addMember(Member member) {
        this.memberList.add(member);
    }
}
