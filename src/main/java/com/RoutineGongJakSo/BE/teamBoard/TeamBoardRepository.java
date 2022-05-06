package com.RoutineGongJakSo.BE.teamBoard;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamBoardRepository extends JpaRepository<TeamBoard, Long> {
    void update(String teamBoard);
}
