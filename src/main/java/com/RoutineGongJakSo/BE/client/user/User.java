package com.RoutineGongJakSo.BE.client.user;

import com.RoutineGongJakSo.BE.admin.member.Member;
import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.tag.Tag;
import com.RoutineGongJakSo.BE.util.Timestamped;
import lombok.*;

import javax.persistence.*;
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

    @Column
    private String userGitHub;

    @Column
    private String userImageUrl;

    @Column //카톡 친추용 아이디
    private String findKakaoId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<CheckIn> checkIn;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Member> memberList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Tag> tagList;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Analysis> analysisList;

    public void addMember(Member member) {
        this.memberList.add(member);
    }

    public void addTags(Tag tag) {
        this.tagList.add(tag);
    }

    public User(int userLevel) {
        this.userLevel = userLevel;
    }
}
