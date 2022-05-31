package com.RoutineGongJakSo.BE.client.user;

import com.RoutineGongJakSo.BE.security.validator.ErrorResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/api/user/signup")
    public ErrorResult join(@Valid @RequestBody JoinDto joinDto, Errors errors) {

        log.info("요청 메서드 [POST] /api/user/signup");

        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return new ErrorResult(false, message);
        }

        return userService.join(joinDto);
    }
}
