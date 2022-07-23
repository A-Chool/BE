package com.RoutineGongJakSo.BE.client.refreshToken;

import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.security.jwt.JwtTokenUtils;
import lombok.*;

import javax.persistence.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long refreshTokenId;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String userEmail;

    public RefreshToken(User user) {
        this.refreshToken = JwtTokenUtils.generateRefreshToken();
        this.userEmail = user.getUserEmail();
    }
}
