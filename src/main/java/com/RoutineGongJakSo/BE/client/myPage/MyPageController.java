package com.RoutineGongJakSo.BE.client.myPage;

import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
public class MyPageController {

    private final MyPageService myPageService;

    //프로필 이미지 수정
    @PutMapping("/api/user/mypage/image")
    public String updateImage(@AuthenticationPrincipal UserDetailsImpl userDetails,
                              @RequestPart("imageUrl") MultipartFile multipartFile){
        return myPageService.updateImage(userDetails, multipartFile);
    }

    //프로필 이미지 삭제
    @DeleteMapping("/api/user/mypage")
    public String deleteImage(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return myPageService.deleteImage(userDetails);
    }

}
