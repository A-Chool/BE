package com.RoutineGongJakSo.BE.client.myPage;

import com.RoutineGongJakSo.BE.client.user.User;
import com.RoutineGongJakSo.BE.client.user.UserRepository;
import com.RoutineGongJakSo.BE.exception.CustomException;
import com.RoutineGongJakSo.BE.security.UserDetailsImpl;
import com.RoutineGongJakSo.BE.security.validator.Validator;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import static com.RoutineGongJakSo.BE.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class MyPageService {
    private final UserRepository userRepository;
    private final S3Validator s3Validator;
    private final Validator validator;
    private AmazonS3 s3Client;

    @Value("${cloud.aws.credentials.access-key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secretKey;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    @PostConstruct
    public void setS3Client() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);

        s3Client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(this.region)
                .build();
    }

    //프로필 이미지 수정하기
    @Transactional
    public String updateImage(UserDetailsImpl userDetails, MultipartFile multipartFile) {
        User user = validator.userInfo(userDetails);

        if (user.getUserImageUrl() != null) {
            String deleteUrl = user.getUserImageUrl().replace("https://myawsssam2.s3.ap-northeast-2.amazonaws.com/", "");
            boolean isExitstObject = s3Client.doesObjectExist(bucket, deleteUrl);
            if (isExitstObject) {
                s3Client.deleteObject(bucket, deleteUrl);

                String ImageUrl = s3Validator.uploadOne(multipartFile);

                user.setUserImageUrl(ImageUrl);
                userRepository.save(user);
            }
        } else {
            String ImageUrl = s3Validator.uploadOne(multipartFile);

            user.setUserImageUrl(ImageUrl);
            userRepository.save(user);
        }

        return "프로필 이미지가 변경되었습니다.";
    }

    // 프로필 이미지 삭제
    @Transactional
    public String deleteImage(UserDetailsImpl userDetails) {
        User user = validator.userInfo(userDetails);
        if (user.getUserImageUrl() != null) {
            String deleteUrl = user.getUserImageUrl().replace("https://myawsssam2.s3.ap-northeast-2.amazonaws.com/", "");
            boolean isExitstObject = s3Client.doesObjectExist(bucket, deleteUrl);
            if (isExitstObject) {
                s3Client.deleteObject(bucket, deleteUrl);
                user.setUserImageUrl(null);
                userRepository.save(user);
            }
        } else {
            throw new CustomException(LIAR_USER_IMAGE);
        }

        return "프로필 이미지 삭제 완료!";
    }

    //프로필 정보 업데이트
    @Transactional
    public String updateUserInfo(UserDetailsImpl userDetails, MyPageDto myPageDto) {
        User user = validator.userInfo(userDetails);

        user.setUserName(myPageDto.getUserNickName());
        user.setUserTag(myPageDto.getUserTag());
        user.setUserGitHub(myPageDto.getUserGitHub());
        user.setKakaoNickName(myPageDto.getUserKakao());

        userRepository.save(user);

        return "저장 완료";
    }


    //1. 회원가입 시, 디폴트 이미지 넣어줄 것
    //2. 이미지 수정 시, null 값으로 보내면 디폴트 이미지 넣어줄 것
    //3. respones Entity 로 return 하기
}

