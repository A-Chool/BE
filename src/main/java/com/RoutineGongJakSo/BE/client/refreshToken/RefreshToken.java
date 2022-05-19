package com.RoutineGongJakSo.BE.client.refreshToken;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long refreshTokenId;

    @Column
    private String refreshToken;

}
