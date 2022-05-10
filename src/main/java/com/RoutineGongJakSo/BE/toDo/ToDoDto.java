package com.RoutineGongJakSo.BE.toDo;

import lombok.Builder;
import lombok.Getter;

public class ToDoDto {

    @Getter
    @Builder
    public static class RequestToDoDto {
        private Long teamId;
        private String todoContent;
    }

    @Getter
    public static class UpDateToDoDto{
        private String todoContent;
    }

    @Getter
    @Builder
    public static class ResponseDto{
        private Long todoId;
        private String todoContent;
        private boolean todoCheck;
    }

}
