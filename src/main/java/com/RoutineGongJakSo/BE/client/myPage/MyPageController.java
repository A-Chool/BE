package com.RoutineGongJakSo.BE.client.myPage;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/user")
public class MyPageController {

    private final MyPageService myPageService;

    //마이페이지 조회
    @GetMapping("/mypage")
    public MyPageDto.ResponseDto getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메서드 [GET] /api/user/mypage");
        return myPageService.getMyPage(userDetails);
    }
    //프로필 이미지 수정
    @PutMapping("/mypage/image")
    public String updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestPart("imageUrl") MultipartFile multipartFile){
        log.info("요청 메서드 [PUT] /api/user/mypage/image");
        return myPageService.updateImage(userDetails, multipartFile);
    }

    @PutMapping("/mypage")
    public MyPageDto.ResponseDto updateUserInfo(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                 @RequestBody MyPageDto.PutRequestDto myPageDto) {
        log.info("요청 메서드 [PUT] /api/user/mypage");
        return myPageService.updateUserInfo(userDetails, myPageDto);
    }

    //프로필 이미지 삭제
    @DeleteMapping("/mypage")
    public String deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        log.info("요청 메서드 [DELETE] /api/user/mypage");
        return myPageService.deleteImage(userDetails);
    }
}
