package com.RoutineGongJakSo.BE.teamBoard;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {

    Optional<TeamBoard> findByWorkSpace /*findbyWeekteamAndWorkspaceAndGroundRule*/(String workSpace/*String weekTeam, String workSpace, String groundRule*/);
}
