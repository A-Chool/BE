package com.RoutineGongJakSo.BE.security.validator;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorResult {

    private Boolean ok;
    private String message;
}
