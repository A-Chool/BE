package com.RoutineGongJakSo.BE.admin.admin;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {
    private final AdminService adminService;

    //관리자 로그인
    @PostMapping("/login")
    public HttpHeaders login(@RequestBody AdminDto.RequestDto adminDto) {
        log.info("요청 메소드 [POST] /api/admin/login");
        return adminService.login(adminDto);
    }

    //전체 유저 조회
    @GetMapping("/userList")
    public List<AdminDto.ResponseDto> getAllUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메소드 [GET] /api/admin/userList");
        return adminService.getAllUser(userDetails);
    }

    //권한 변경
    @PutMapping("/{userId}")
    public String updateLevel(@PathVariable Long userId, @RequestBody AdminDto.UpdateDto update, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메소드 [PUT] /api/admin/{userId}");
        return adminService.updateLevel(userId, update, userDetails);
    }

    //유저 삭제
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메소드 [DELETE] /api/admin/{userId}");
        return adminService.deleteUser(userId, userDetails);
    }
}
