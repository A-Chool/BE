package com.RoutineGongJakSo.BE.teamBoard;

import com.RoutineGongJakSo.BE.admin.dto.MemberDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@NoArgsConstructor
public class TeamBoardtDto {

    @Getter
    @Setter
    public static class requestDto {
        private String groundRule;
        private String workSpace;

        @Builder
        public requestDto(String groundRule) {

            this.groundRule = groundRule;
        }

        public requestDto(String workSpace) {

            this.workSpace = workSpace;

        }
    }

    @Getter
    @Setter
    public static class responseDto {
        private Long teamId;
        private String groundRule;
        private String workSpace;

        @Builder
        public responseDto(TeamBoard entity) {
            this.teamId = entity.getTeamId();
            this.groundRule = entity.getGroundRule();
            this.workSpace = entity.getWorkSpace();
        }
    }

    @Getter
    @Setter
    public static class WeekTeamDto {
        private Long teamId;
        private String teamName;
        private String week;
        private List<MemberDto> weekTeamList;
    }
}