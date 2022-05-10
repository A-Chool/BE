package com.RoutineGongJakSo.BE.teamBoard.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ToDoDto {
    private Long todoId;
    private String todoContent;
    private boolean todoCheck;
}
