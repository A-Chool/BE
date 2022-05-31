package com.RoutineGongJakSo.BE.sse;

import com.RoutineGongJakSo.BE.client.checkIn.model.CheckIn;
import com.RoutineGongJakSo.BE.client.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckInResponse {
        private Long userId;
        private String userEmail;
        private String userName;
        private String phoneNumber;
        private boolean online; //로그인 여부
        private boolean lateCheck; //지각 여부

        public CheckInResponse(CheckIn checkIn, boolean lateCheck) {
            this.userId = checkIn.getUser().getUserId();
            this.userEmail = checkIn.getUser().getUserEmail();
            this.userName = checkIn.getUser().getUserName();
            this.phoneNumber = checkIn.getUser().getPhoneNumber();
            this.online = true;
            this.lateCheck = lateCheck;
        }
    }
    @Getter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckOutResponse {
        private Long userId;
        private String userEmail;
        private String userName;
        private String phoneNumber;
        private boolean online;

        public CheckOutResponse(User user) {
            this.userId = user.getUserId();
            this.userEmail = user.getUserEmail();
            this.userName = user.getUserName();
            this.phoneNumber = user.getPhoneNumber();
            this.online = false;
        }
    }
}
