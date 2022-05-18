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

        // AWS에 요청할 Client를 생성해 주는 역할
        // Builder 권장
        // 해당 객체의 standard()를 통해 AmazonS3 Client 를 생성하며, credentials 와 region 정보를 넘겨줌
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
            String deleteUrl = user.getUserImageUrl().replace("https://" + bucket + ".s3.ap-northeast-2.amazonaws.com/", "");
            boolean isExitstObject = s3Client.doesObjectExist(bucket, deleteUrl);
            String imageUrl = "";
            if (isExitstObject) {
                s3Client.deleteObject(bucket, deleteUrl);

                imageUrl = s3Validator.uploadOne(multipartFile);

                user.setUserImageUrl(imageUrl);
                userRepository.save(user);
            }
            return imageUrl;
        } else {
            String ImageUrl = s3Validator.uploadOne(multipartFile);

            user.setUserImageUrl(ImageUrl);
            userRepository.save(user);

            return ImageUrl;
        }
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
    public MyPageDto.ResponseDto updateUserInfo(UserDetailsImpl userDetails, MyPageDto.PutRequestDto myPageDto) {
        User user = validator.userInfo(userDetails);

        user.setUserName(myPageDto.getUserNickName());
        user.setUserTag(myPageDto.getUserTag());
        user.setUserGitHub(myPageDto.getUserGitHub());
        user.setKakaoNickName(myPageDto.getUserKakao());

        userRepository.save(user);

        return getMyPage(userDetails);
    }

    // 마이페이지 조회
    public MyPageDto.ResponseDto getMyPage(UserDetailsImpl userDetails) {
        User user = validator.userInfo(userDetails);

        String userImage = user.getUserImageUrl();

        if (user.getUserImageUrl() == null){
            userImage = "https://i.esdrop.com/d/f/zoDvw3Gypq/LDMVjgddH1.png";
        }

        MyPageDto.ResponseDto responseDto = MyPageDto.ResponseDto.builder()
                .userImage(userImage)
                .userNickName(user.getKakaoNickName())
                .userTag(user.getUserTag())
                .userGitHub(user.getUserGitHub())
                .userKakao(user.getKakaoNickName())
                .build();

        return responseDto;
    }
}

