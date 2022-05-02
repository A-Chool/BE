package com.RoutineGongJakSo.BE.admin.controller;

import com.RoutineGongJakSo.BE.admin.dto.AdminDto;
import com.RoutineGongJakSo.BE.admin.service.AdminService;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;

    //관리자 로그인
    @PostMapping("/login")
    public HttpHeaders login(@RequestBody AdminDto.RequestDto adminDto) {
        return adminService.login(adminDto);
    }

    //전체 유저 조회
    @GetMapping("/userList")
    public List<AdminDto.ResponseDto> getAllUser(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.getAllUser(userDetails);
    }

    //권한 변경
    @PutMapping("/{userId}")
    public String updateLevel(@PathVariable Long userId, @RequestBody AdminDto.UpdateDto update, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.updateLevel(userId, update, userDetails);
    }

    //유저 삭제
    @DeleteMapping("/{userId}")
    public String deleteUser(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return adminService.deleteUser(userId, userDetails);
    }
}
