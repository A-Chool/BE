package com.RoutineGongJakSo.BE.teamBoard;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {

    static String findByWorkSpace /*findbyWeekteamAndWorkspaceAndGroundRule*/(String workSpace/*String weekTeam, String workSpace, String groundRule*/) {
        return workSpace;
    }
}
