package com.RoutineGongJakSo.BE.client.analysis.dto;

import com.RoutineGongJakSo.BE.client.analysis.model.Analysis;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RankRedisDto {
    private Long userId;
    private String userName;

    public RankRedisDto(Analysis analysis) {
        this.userId = analysis.getUser().getUserId();
        this.userName = analysis.getUser().getUserName();
    }

}
