package com.RoutineGongJakSo.BE.client.user;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
    private List<String> userTags;

    public UserDto(User user, List<String> userTags) {
        this.userId = user.getUserId();
        this.userEmail = user.getUserEmail();
        this.userName = user.getUserName();
        this.phoneNumber = user.getPhoneNumber();
        this.createdAt = user.getCreatedAt();
        this.findKakaoId = user.getFindKakaoId();
        this.userGitHub = user.getUserGitHub();
        this.userTags = userTags;
    }
}
