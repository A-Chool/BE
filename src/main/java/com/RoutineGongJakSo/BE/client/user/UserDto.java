package com.RoutineGongJakSo.BE.client.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDto {
    private Long userId;
    private String userEmail;
    private String userName;
    private String phoneNumber;
    private String createdAt;
    private String findKakaoId;
    private String userGitHub;
    private String userTag;

    public UserDto(User user) {
        this.userId = user.getUserId();
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.createdAt = user.getCreatedAt();
        this.findKakaoId = user.getFindKakaoId();
        this.userTag = user.getUserTag();
        this.userGitHub = user.getUserGitHub();
    }
}
